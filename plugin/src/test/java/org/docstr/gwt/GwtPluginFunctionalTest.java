/**
 * Copyright (C) 2024 Document Node Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.docstr.gwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;

/**
 * A functional test for the 'org.docstr.gwt' plugin.
 *
 * This will download (and cache) different versions of Gradle
 * and try to build the sample-gwt-project (in resources).
 */
@Slf4j
class GwtPluginFunctionalTest extends AbstractGwtTest {

  private static final Set<String> supportedGradleVersions = Set.of(
          // https://gradle.org/releases/
          // Only including the last patch release of each supported Gradle version.
          // For example, you won't see 8.10.1 here because it was superseded by 8.10.2.
          "8.13",   // Feb 25, 2025
          "8.12.1", // Jan 24, 2025
          "8.11.1", // Nov 20, 2024
          "8.10.2", // Sep 23, 2024
//          "8.9",    // Jul 11, 2024
//          "8.8",    // May 31, 2024
//          "8.7",    // Mar 22, 2024
//          "8.6",    // Feb 02, 2024
//          "8.5",    // Nov 29, 2024
//          "8.4",    // Oct 29, 2023
//          "8.3",    // Aug 17, 2023
//          "8.2.1",  // Jul 10, 2023
          "8.1.1"   // Apr 21, 2023

          // This plugin is using JavaExecSpec getJvmArguments method,
          // which was introduced in Gradle 8.1.
          // So, these are currently not supported.
          //"8.0.2", // Mar 03, 2023
          //"7.6.4", // Feb 05, 2024 (latest 7.x)
  );

  @ParameterizedTest(name = "Can run gwtCompile task with Gradle {0}")
  @FieldSource("supportedGradleVersions")
  void canRunTask(String gradleVersion) throws IOException {
    /*
     * -------------------------------------------------------------------------
     * Given
     * -------------------------------------------------------------------------
     */
    setupSampleProject();

    /*
     * -------------------------------------------------------------------------
     * When
     * -------------------------------------------------------------------------
     */
    // Run the build
    GradleRunner runner = GradleRunner.create()
            .withGradleVersion(gradleVersion)
            .forwardOutput()
            .withPluginClasspath()
            .withArguments("gwtCompile", "--info")
            .withProjectDir(projectDir);
    BuildResult result;
    try {
      result = runner.build();
    } catch (Exception e) {
      log.error("Error running build: {}", e.getMessage(), e);
      throw e;
    }

    /*
     * -------------------------------------------------------------------------
     * Then
     * -------------------------------------------------------------------------
     */
    // Verify the result
    assertThat(result.task(":gwtCompile"))
            .isNotNull()
            .extracting(BuildTask::getOutcome)
            .isEqualTo(TaskOutcome.SUCCESS);
    assertTrue(result.getOutput().contains("Compilation succeeded"));
    assertTrue(result.getOutput().contains("Linking succeeded"));
  }
}
