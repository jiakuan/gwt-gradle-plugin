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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;

/**
 * A simple functional test for the 'org.docstr.gwt' plugin.
 */
@Slf4j
class GwtPluginFunctionalTest extends AbstractGwtTest {

  @Test
  void canRunTask() throws IOException {
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
    GradleRunner runner = GradleRunner.create();
    runner.forwardOutput();
    runner.withPluginClasspath();
    runner.withArguments("gwtCompile", "--info");
    runner.withProjectDir(projectDir);
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
    assertTrue(result.getOutput().contains("Compilation succeeded"));
    assertTrue(result.getOutput().contains("Linking succeeded"));
  }
}
