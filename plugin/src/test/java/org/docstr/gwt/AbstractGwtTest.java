/**
 * Copyright (C) 2024 Document Node Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.docstr.gwt;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ScanResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.List;
import java.util.logging.LogManager;
import lombok.extern.slf4j.Slf4j;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.io.TempDir;

@Slf4j
public abstract class AbstractGwtTest {

  static {
    try (InputStream stream =
        GwtPluginFunctionalTest.class.getResourceAsStream(
            "/logging.properties")) {
      LogManager.getLogManager().readConfiguration(stream);
    } catch (IOException ex) {
      log.error("Could not set up logging", ex);
    }
  }

  @TempDir
  File projectDir;

  protected void setupSampleProject() throws IOException {
    ClassGraph classGraph = new ClassGraph()
        .acceptPaths("/sample-gwt-project")
        .addClassLoader(getClass().getClassLoader())
        .acceptPackages("sample-gwt-project");
    try (ScanResult scanResult = classGraph.scan()) {
      List<Resource> resources = scanResult.getAllResources();
      for (Resource resource : resources) {
        String resourcePath = resource.getPath();
        if (containsIgnoreCase(resourcePath, ".idea")
            || containsIgnoreCase(resourcePath, "/.gradle/")
            || containsIgnoreCase(resourcePath, "/gradle/")
            || containsIgnoreCase(resourcePath, "gradlew")) {
          // log.warn("Skipping path: {}", resourcePath);
          continue;
        }
        String fileName = resourcePath.replace("sample-gwt-project/", "");

        File file = new File(projectDir, fileName);
        boolean mkdirs = file.getParentFile().mkdirs();
        if (mkdirs) {
          log.warn("Created directory: {}", file.getParentFile());
        }
        try (Writer writer = new FileWriter(file)) {
          writer.write(resource.getContentAsString());
          log.warn("Wrote path: {}", file);
        }
      }
    }
  }

  protected Project setupProject() {
    Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply("org.docstr.gwt");
    project.getExtensions().configure("gwt", ext -> {
    });

    // Load the temp directory onto the standard sourceset locations
    SourceSetContainer sourceSets = project.getExtensions()
            .getByType(SourceSetContainer.class);
    SourceSet mainSourceSet = sourceSets.getByName(
            SourceSet.MAIN_SOURCE_SET_NAME);

    mainSourceSet.getJava().srcDir(projectDir.toPath().resolve("src/main/java"));
    mainSourceSet.getResources().srcDir(projectDir.toPath().resolve("src/main/resources"));
    return project;
  }
}
