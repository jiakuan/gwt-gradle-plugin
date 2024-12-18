package org.docstr.gwt;

import static org.assertj.core.api.Assertions.assertThat;

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
  void findModuleFile() throws IOException {
    // Given
    setupSampleProject();

    Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply("org.docstr.gwt");
    project.getExtensions().configure("gwt", ext -> {
    });

    String moduleName = "com.example.MyModule";
    Set<File> sourceFiles = Files.walk(projectDir.toPath())
        .filter(Files::isRegularFile)
        .map(Path::toFile)
        .collect(Collectors.toSet());

    // When
    GwtPluginExtension extension = project.getExtensions()
        .getByType(GwtPluginExtension.class);
    GwtCompileConfig config = new GwtCompileConfig(extension);
    File moduleFile = config.findModuleFile(moduleName, sourceFiles);

    // Then
    assertThat(moduleFile).exists();
  }

  @Test
  void extractSourcePaths_fromEntryPoint() throws IOException {
    // Given
    setupSampleProject();

    Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply("org.docstr.gwt");
    project.getExtensions().configure("gwt", ext -> {
    });

    String moduleName = "com.example.MyModule";
    Set<File> sourceFiles = Files.walk(projectDir.toPath())
        .filter(Files::isRegularFile)
        .map(Path::toFile)
        .collect(Collectors.toSet());

    // When
    GwtPluginExtension extension = project.getExtensions()
        .getByType(GwtPluginExtension.class);
    GwtCompileConfig config = new GwtCompileConfig(extension);
    File moduleFile = config.findModuleFile(moduleName, sourceFiles);
    TreeSet<File> sourcePaths = config.extractSourcePaths(moduleFile);

    // Then
    assertThat(sourcePaths).isNotEmpty();
    assertThat(sourcePaths).allMatch(File::exists);

    assertThat(sourcePaths.first()).isFile();
    assertThat(sourcePaths.last()).isDirectory();

    assertThat(sourcePaths).hasSize(2);
    assertThat(sourcePaths.first().getAbsolutePath()).isEqualTo(
        moduleFile.getAbsolutePath());
    assertThat(sourcePaths.last().getName()).isEqualTo("client");
  }

  @Test
  void extractSourcePaths_fromSourceNodes() throws IOException {
    // Given
    setupSampleProject();

    Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply("org.docstr.gwt");
    project.getExtensions().configure("gwt", ext -> {
    });

    String moduleName = "com.example.MyModule2";
    Set<File> sourceFiles = Files.walk(projectDir.toPath())
        .filter(Files::isRegularFile)
        .map(Path::toFile)
        .collect(Collectors.toSet());

    // When
    GwtPluginExtension extension = project.getExtensions()
        .getByType(GwtPluginExtension.class);
    GwtCompileConfig config = new GwtCompileConfig(extension);
    File moduleFile = config.findModuleFile(moduleName, sourceFiles);
    TreeSet<File> sourcePaths = config.extractSourcePaths(moduleFile);

    // Then
    assertThat(sourcePaths).isNotEmpty();
    assertThat(sourcePaths).allMatch(File::exists);

    assertThat(sourcePaths.first()).isFile();
    List<File> pathList = sourcePaths.stream().collect(Collectors.toList());
    assertThat(
        pathList.get(1)).isDirectory();
    assertThat(sourcePaths.last()).isDirectory();

    assertThat(sourcePaths).hasSize(3);
    assertThat(sourcePaths.first().getAbsolutePath()).isEqualTo(
        moduleFile.getAbsolutePath());
    assertThat(pathList.get(1).getName()).isEqualTo("client");
    assertThat(sourcePaths.last().getName()).isEqualTo("shared");
  }

  @Test
  void extractSourcePaths_fromPublicNodes() throws IOException {
    // Given
    setupSampleProject();

    Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply("org.docstr.gwt");
    project.getExtensions().configure("gwt", ext -> {
    });

    String moduleName = "com.example.MyModule3";
    Set<File> sourceFiles = Files.walk(projectDir.toPath())
        .filter(Files::isRegularFile)
        .map(Path::toFile)
        .collect(Collectors.toSet());

    // When
    GwtPluginExtension extension = project.getExtensions()
        .getByType(GwtPluginExtension.class);
    GwtCompileConfig config = new GwtCompileConfig(extension);
    File moduleFile = config.findModuleFile(moduleName, sourceFiles);
    TreeSet<File> sourcePaths = config.extractSourcePaths(moduleFile);

    // Then
    assertThat(sourcePaths).isNotEmpty();
    assertThat(sourcePaths).allMatch(File::exists);

    List<File> pathList = sourcePaths.stream().collect(Collectors.toList());
    assertThat(pathList.get(0)).isFile();
    assertThat(pathList.get(1)).isDirectory();
    assertThat(pathList.get(2)).isDirectory();
    assertThat(pathList.get(3)).isDirectory();

    assertThat(pathList).hasSize(4);
    assertThat(pathList.get(0).getAbsolutePath())
        .isEqualTo(moduleFile.getAbsolutePath());
    assertThat(pathList.get(1).getName()).isEqualTo("client");
    assertThat(pathList.get(2).getName()).isEqualTo("pub");
    assertThat(pathList.get(3).getName()).isEqualTo("shared");
  }
}
