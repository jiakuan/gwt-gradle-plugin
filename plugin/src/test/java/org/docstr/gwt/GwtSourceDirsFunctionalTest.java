package org.docstr.gwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;

/**
 * Verifies that dependency sources can be consumed by GWT without recompiling them with javac.
 */
class GwtSourceDirsFunctionalTest extends AbstractGwtTest {

  @Test
  void compilesDependencySourcesWithoutAddingThemToJavaSources() throws IOException {
    setupSharedProject();
    Path sharedSource = projectDir.toPath().resolve("shared/src/main/java/com/shared/Shared.java");
    Path sharedModule = projectDir.toPath().resolve("shared/src/main/resources/com/shared/Shared.gwt.xml");
    String source = Files.readString(sharedSource);

    GradleRunner runner = GradleRunner.create()
        .withPluginClasspath()
        .withProjectDir(projectDir)
        .withArguments("gwtCompile", "--build-cache", "--configuration-cache", "--info", "--stacktrace");

    assertOutcome(runner.build(), TaskOutcome.SUCCESS);
    assertThat(projectDir.toPath().resolve("shared/build/classes/java/main/com/shared/Shared.class"))
        .isRegularFile();
    assertThat(projectDir.toPath().resolve("build/classes/java/main/com/shared/Shared.class"))
        .doesNotExist();

    BuildResult unchangedBuild = runner.build();
    assertOutcome(unchangedBuild, TaskOutcome.UP_TO_DATE);
    assertThat(unchangedBuild.getOutput()).contains("Reusing configuration cache.");

    Files.writeString(sharedSource, source.replace("Shared message", "Changed message"));
    assertOutcome(runner.build(), TaskOutcome.SUCCESS);

    Files.writeString(sharedModule, "\n<!-- Resource-only change -->\n", StandardOpenOption.APPEND);
    BuildResult resourceChangedBuild = runner.build();
    assertOutcome(resourceChangedBuild, TaskOutcome.SUCCESS);
    assertThat(resourceChangedBuild.task(":shared:compileJava"))
        .isNotNull()
        .extracting(BuildTask::getOutcome)
        .isEqualTo(TaskOutcome.UP_TO_DATE);

    BuildResult cachedBuild = runner
        .withArguments("clean", "gwtCompile", "--build-cache", "--configuration-cache", "--info", "--stacktrace")
        .build();
    assertOutcome(cachedBuild, TaskOutcome.FROM_CACHE);
    assertThat(projectDir.toPath().resolve("build/classes/java/main/com/shared/Shared.class"))
        .doesNotExist();
  }

  @Test
  void runsGwtJUnitTestsWithDependencySources() throws IOException {
    setupSharedProject();
    Files.writeString(projectDir.toPath().resolve("build.gradle"), """

        dependencies {
          testImplementation 'junit:junit:4.13.2'
        }
        gwt {
          gwtTest {
            testTasks = ['test']
            runStyle = 'HtmlUnit'
            prod = true
            draftCompile = true
            localWorkers = 1
            port = 0
          }
        }
        test {
          useJUnit()
        }
        """, StandardOpenOption.APPEND);
    Path testModule = projectDir.toPath().resolve("src/test/java/com/example/SharedTest.gwt.xml");
    Files.createDirectories(testModule.getParent());
    Files.writeString(testModule, """
        <module>
          <inherits name="com.google.gwt.user.User"/>
          <inherits name="com.shared.Shared"/>
          <source path="client"/>
        </module>
        """);
    Path testSource = testModule.getParent().resolve("client/SharedTest.java");
    Files.createDirectories(testSource.getParent());
    Files.writeString(testSource, """
        package com.example.client;
        import com.google.gwt.core.client.GWT;
        import com.google.gwt.junit.client.GWTTestCase;
        import com.shared.Shared;
        public class SharedTest extends GWTTestCase {
          public String getModuleName() {
            return "com.example.SharedTest";
          }
          public void testSharedMessage() {
            assertTrue(GWT.isClient());
            assertEquals("Shared message", Shared.message());
          }
        }
        """);

    GradleRunner runner = GradleRunner.create()
        .withPluginClasspath()
        .withProjectDir(projectDir)
        .withArguments("test", "--configuration-cache", "--stacktrace");
    BuildResult result = runner.build();
    assertThat(result.task(":test")).isNotNull()
        .extracting(BuildTask::getOutcome).isEqualTo(TaskOutcome.SUCCESS);
    assertThat(Files.readString(projectDir.toPath().resolve(
        "build/test-results/test/TEST-com.example.client.SharedTest.xml")))
        .contains("tests=\"1\"", "failures=\"0\"", "errors=\"0\"");
    assertThat(projectDir.toPath().resolve("build/classes/java/main/com/shared/Shared.class"))
        .doesNotExist();

    BuildResult unchangedBuild = runner.build();
    assertThat(unchangedBuild.task(":test")).isNotNull()
        .extracting(BuildTask::getOutcome).isEqualTo(TaskOutcome.UP_TO_DATE);
    assertThat(unchangedBuild.getOutput()).contains("Reusing configuration cache.");

    Path sharedSource = projectDir.toPath().resolve("shared/src/main/java/com/shared/Shared.java");
    Files.writeString(sharedSource, "\n// Source-only change\n", StandardOpenOption.APPEND);
    assertThat(runner.build().task(":test")).isNotNull()
        .extracting(BuildTask::getOutcome).isEqualTo(TaskOutcome.SUCCESS);
  }

  private void setupSharedProject() throws IOException {
    setupSampleProject();
    Files.writeString(projectDir.toPath().resolve("settings.gradle"), """
        include 'shared'
        buildCache {
          local {
            directory = file('build-cache')
          }
        }
        """);
    Files.writeString(projectDir.toPath().resolve("build.gradle"), """

        dependencies {
          implementation project(':shared')
        }
        gwt {
          gwtSourceDirs.from(files('shared/src/main/java', 'shared/src/main/resources'))
        }
        """, StandardOpenOption.APPEND);
    Path sharedSource = projectDir.toPath().resolve("shared/src/main/java/com/shared/Shared.java");
    Files.createDirectories(sharedSource.getParent());
    Files.writeString(projectDir.toPath().resolve("shared/build.gradle"), """
        plugins {
          id 'java-library'
        }
        """);
    String source = """
        package com.shared;
        public class Shared {
          public static String message() {
            return "Shared message";
          }
        }
        """;
    Files.writeString(sharedSource, source);
    Path sharedModule = projectDir.toPath().resolve("shared/src/main/resources/com/shared/Shared.gwt.xml");
    Files.createDirectories(sharedModule.getParent());
    Files.writeString(sharedModule, """
        <module>
          <source path=""/>
        </module>
        """);
    Path module = projectDir.toPath().resolve("src/main/java/com/example/MyModule.gwt.xml");
    Files.writeString(module, Files.readString(module)
        .replace("<module rename-to=\"mymodule\">",
            "<module rename-to=\"mymodule\"><inherits name=\"com.shared.Shared\"/>"));
    Path entryPoint = projectDir.toPath().resolve("src/main/java/com/example/client/MyEntryPoint.java");
    Files.writeString(entryPoint, Files.readString(entryPoint)
        .replace("\"Hello, GWT!\"", "com.shared.Shared.message()"));
  }

  private void assertOutcome(BuildResult result, TaskOutcome outcome) {
    assertThat(result.task(":gwtCompile"))
        .isNotNull()
        .extracting(BuildTask::getOutcome)
        .isEqualTo(outcome);
  }
}
