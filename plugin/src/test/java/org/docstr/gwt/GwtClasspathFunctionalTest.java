package org.docstr.gwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;

/**
 * Verifies GWT compilation with a classpath exceeding the Windows command line limit.
 */
class GwtClasspathFunctionalTest extends AbstractGwtTest {

  @Test
  void compilesWithLongClasspathAndSpaces() throws IOException {
    setupSampleProject();
    for (int i = 1; i <= 250; i++) {
      Files.createDirectories(projectDir.toPath()
          .resolve("additional sources/library-" + i + "-" + "x".repeat(100)));
    }
    Files.writeString(projectDir.toPath().resolve("build.gradle"), """

        tasks.named('gwtCompile') {
          classpath += files((1..250).collect {
            file('additional sources/library-' + it + '-' + 'x' * 100)
          })
        }
        """, StandardOpenOption.APPEND);

    GradleRunner runner = GradleRunner.create()
        .withPluginClasspath()
        .withProjectDir(projectDir)
        .withArguments("gwtCompile", "--rerun", "--configuration-cache", "--info", "--stacktrace");

    BuildResult firstBuild = runner.build();
    assertSuccessfulCompilation(firstBuild);

    BuildResult secondBuild = runner.build();
    assertSuccessfulCompilation(secondBuild);
    assertThat(secondBuild.getOutput()).contains("Reusing configuration cache.");
  }

  private void assertSuccessfulCompilation(BuildResult result) {
    assertThat(result.task(":gwtCompile"))
        .isNotNull()
        .extracting(BuildTask::getOutcome)
        .isEqualTo(TaskOutcome.SUCCESS);
    assertThat(result.getOutput()).contains("Compilation succeeded", "Linking succeeded");
  }
}
