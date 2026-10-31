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
 * Verifies build cache reuse for GWT compilation.
 */
class GwtCompileCacheFunctionalTest extends AbstractGwtTest {

  @Test
  void reusesCompilationWithDifferentLocalWorkers() throws IOException {
    setupSampleProject();
    Files.writeString(projectDir.toPath().resolve("settings.gradle"), """
        buildCache {
          local {
            directory = file('build-cache')
          }
        }
        """);
    Files.writeString(projectDir.toPath().resolve("build.gradle"), """

        gwt.compiler.localWorkers = providers.gradleProperty('gwtWorkers').map { it.toInteger() }
        """, StandardOpenOption.APPEND);

    GradleRunner runner = GradleRunner.create()
        .withPluginClasspath()
        .withProjectDir(projectDir);

    BuildResult firstBuild = runner
        .withArguments("gwtCompile", "--build-cache", "--configuration-cache", "-PgwtWorkers=1", "--info")
        .build();
    assertThat(firstBuild.task(":gwtCompile"))
        .isNotNull()
        .extracting(BuildTask::getOutcome)
        .isEqualTo(TaskOutcome.SUCCESS);

    BuildResult secondBuild = runner
        .withArguments("gwtCompile", "--build-cache", "--configuration-cache", "-PgwtWorkers=2", "--info")
        .build();
    assertThat(secondBuild.task(":gwtCompile"))
        .isNotNull()
        .extracting(BuildTask::getOutcome)
        .isEqualTo(TaskOutcome.UP_TO_DATE);

    BuildResult cachedBuild = runner
        .withArguments("clean", "gwtCompile", "--build-cache", "--configuration-cache", "-PgwtWorkers=2", "--info")
        .build();
    assertThat(cachedBuild.task(":gwtCompile"))
        .isNotNull()
        .extracting(BuildTask::getOutcome)
        .isEqualTo(TaskOutcome.FROM_CACHE);
  }
}
