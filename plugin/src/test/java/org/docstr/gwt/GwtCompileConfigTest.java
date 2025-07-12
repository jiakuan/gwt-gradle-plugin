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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

class GwtCompileConfigTest extends AbstractGwtTest {


  @Test
  void findAllModules() throws IOException {
    // Given
    setupSampleProject();
    Project project = setupProject();
    String moduleName = "com.example.MyModule";

    // When
    GwtPluginExtension extension = project.getExtensions()
            .getByType(GwtPluginExtension.class);
    GwtCompileConfig config = new GwtCompileConfig(extension);

    var modules = config.findAllModules(project);

    // Then
    assertThat(modules)
            .isNotNull()
            .isNotEmpty()
            .anySatisfy(it -> {
              assertThat(it.name()).isEqualTo(moduleName);
            })
            .allSatisfy(it -> {
              assertThatNoException().isThrownBy(it::sourceDirectorySetRootPath);
            });
  }

  @Test
  void findModuleFile() throws IOException {
    // Given
    setupSampleProject();
    Project project = setupProject();
    String moduleName = "com.example.MyModule";

    // When
    GwtPluginExtension extension = project.getExtensions()
        .getByType(GwtPluginExtension.class);
    GwtCompileConfig config = new GwtCompileConfig(extension);

    var modules = config.findAllModules(project);
    var module = modules.stream().filter(m -> moduleName.equals(m.name())).findFirst().orElse(null);

    // Then
    assertThat(module)
            .isNotNull()
            .satisfies(m -> {
              assertThat(m.name()).isEqualTo(moduleName);
              assertThatNoException().isThrownBy(m::sourceDirectorySetRootPath);
            });
  }

  @Test
  void extractSourcePaths_fromEntryPoint() throws IOException {
    // Given
    setupSampleProject();
    Project project = setupProject();
    String moduleName = "com.example.MyModule";

    // When
    GwtPluginExtension extension = project.getExtensions()
        .getByType(GwtPluginExtension.class);
    GwtCompileConfig config = new GwtCompileConfig(extension);

    var modules = config.findAllModules(project);
    var module = modules.stream().filter(m -> moduleName.equals(m.name())).findFirst().orElse(null);
    TreeSet<Path> sourcePaths = config.extractSourcePaths(module);

    // Then
    assertThat(sourcePaths)
            .isNotEmpty()
            .hasSize(2)
            .allSatisfy(f -> {
              assertThat(f).exists();
            });

    assertThat(sourcePaths.first())
            .isRegularFile()
            .isEqualTo(module.path());
    assertThat(sourcePaths.last())
            .isDirectory()
            .satisfies(p -> {
              assertThat(p.getFileName().toString()).isEqualTo("client");
            });
  }

  @Test
  void extractSourcePaths_fromSourceNodes() throws IOException {
    // Given
    setupSampleProject();
    Project project = setupProject();
    String moduleName = "com.example.MyModule2";

    // When
    GwtPluginExtension extension = project.getExtensions()
        .getByType(GwtPluginExtension.class);
    GwtCompileConfig config = new GwtCompileConfig(extension);

    var modules = config.findAllModules(project);
    var module = modules.stream().filter(m -> moduleName.equals(m.name())).findFirst().orElse(null);
    TreeSet<Path> sourcePaths = config.extractSourcePaths(module);

    // Then
    assertThat(sourcePaths)
            .isNotEmpty()
            .hasSize(3)
            .allSatisfy(f -> {
              assertThat(f).exists();
            });

    List<Path> pathList = sourcePaths.stream().toList();
    assertThat(sourcePaths.first())
            .isRegularFile()
            .isEqualTo(pathList.getFirst())
            .isEqualTo(module.path());
    assertThat(pathList.get(1))
            .isDirectory()
            .satisfies(p -> {
              assertThat(p.getFileName().toString()).isEqualTo("client");
            });
    assertThat(sourcePaths.last())
            .isDirectory()
            .isEqualTo(pathList.getLast())
            .satisfies(p -> {
              assertThat(p.getFileName().toString()).isEqualTo("shared");
            });
  }

  @Test
  void extractSourcePaths_fromPublicNodes() throws IOException {
    // Given
    setupSampleProject();
      Project project = setupProject();
    String moduleName = "com.example.MyModule3";

    // When
    GwtPluginExtension extension = project.getExtensions()
        .getByType(GwtPluginExtension.class);
    GwtCompileConfig config = new GwtCompileConfig(extension);

    var modules = config.findAllModules(project);
    var module = modules.stream().filter(m -> moduleName.equals(m.name())).findFirst().orElse(null);
    TreeSet<Path> sourcePaths = config.extractSourcePaths(module);

    // Then
    assertThat(sourcePaths)
            .isNotEmpty()
            .hasSize(4)
            .allSatisfy(f -> {
              assertThat(f).exists();
            });

    List<Path> pathList = sourcePaths.stream().toList();
    assertThat(sourcePaths.first())
            .isRegularFile()
            .isEqualTo(pathList.getFirst())
            .isEqualTo(module.path());
    assertThat(pathList.get(1))
            .isDirectory()
            .satisfies(p -> {
              assertThat(p.getFileName().toString()).isEqualTo("client");
            });
    assertThat(pathList.get(2))
            .isDirectory()
            .satisfies(p -> {
              assertThat(p.getFileName().toString()).isEqualTo("pub");
            });
    assertThat(sourcePaths.last())
            .isDirectory()
            .isEqualTo(pathList.getLast())
            .satisfies(p -> {
              assertThat(p.getFileName().toString()).isEqualTo("shared");
            });
  }
}
