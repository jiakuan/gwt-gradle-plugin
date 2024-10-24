/**
 * Copyright (C) 2024 Document Node Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.docstr.gwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

/**
 * A simple unit test for the 'org.docstr.gwt' plugin.
 */
class GwtPluginTest {

  @Test
  void passArgumentsFromExtensionToGwtCompileTask() {
    /*
     * -------------------------------------------------------------------------
     * Given
     * -------------------------------------------------------------------------
     */
    // Create a test project and apply the plugin
    Project project = ProjectBuilder.builder().build();

    /*
     * -------------------------------------------------------------------------
     * When
     * -------------------------------------------------------------------------
     */
    project.getPlugins().apply("org.docstr.gwt");
    // Configure the gwt extension programmatically
    project.getExtensions().configure("gwt", ext -> {
      GwtPluginExtension extension = (GwtPluginExtension) ext;
      extension.getMinHeapSize().set("111M");
      extension.getMaxHeapSize().set("222M");
      extension.getLogLevel().set("SPAM");
      extension.getWorkDir().set(project.file("workDir"));
      extension.getGen().set(project.file("gen"));
      extension.getGenerateJsInteropExports().set(true);
      extension.getIncludeJsInteropExports()
          .set(List.of("com.example.MyIncludeExport"));
      extension.getExcludeJsInteropExports()
          .set(List.of("com.example.MyExcludeExport"));
      extension.getMethodNameDisplayMode().set("FULL");
      extension.getSetProperty().set(List.of("name=value"));
      extension.getStyle().set("PRETTY");
      extension.getFailOnError().set(true);
      extension.getSourceLevel().set("17");
      extension.getIncremental().set(true);
      extension.getWar().set(project.file("war"));
      extension.getDeploy().set(project.file("deploy"));
      extension.getExtra().set(project.file("extra"));
      extension.getCacheDir().set(project.file("cacheDir"));
      extension.getModules().set(List.of("com.example.MyModule"));
    });

    /*
     * -------------------------------------------------------------------------
     * Then
     * -------------------------------------------------------------------------
     */
    // Verify the result
    TaskContainer tasks = project.getTasks();
    GwtCompileTask task = (GwtCompileTask) tasks.findByName("gwtCompile");
    assertThat(task).isNotNull();
    assertThat(task.getMinHeapSize()).isEqualTo("111M");
    assertThat(task.getMaxHeapSize()).isEqualTo("222M");
    assertThat(task.getLogLevel().get()).isEqualTo("SPAM");
    assertThat(task.getWorkDir().get().getAsFile()).isEqualTo(
        project.file("workDir"));
    assertThat(task.getGen().get().getAsFile()).isEqualTo(project.file("gen"));
    assertThat(task.getGenerateJsInteropExports().get()).isTrue();
    assertThat(task.getIncludeJsInteropExports().get())
        .containsExactly("com.example.MyIncludeExport");
    assertThat(task.getExcludeJsInteropExports().get())
        .containsExactly("com.example.MyExcludeExport");
    assertThat(task.getMethodNameDisplayMode().get()).isEqualTo("FULL");
    assertThat(task.getSetProperty().get()).containsExactly("name=value");
    assertThat(task.getStyle().get()).isEqualTo("PRETTY");
    assertThat(task.getFailOnError().get()).isTrue();
    assertThat(task.getSourceLevel().get()).isEqualTo("17");
    assertThat(task.getIncremental().get()).isTrue();
    assertThat(task.getWar().get().getAsFile()).isEqualTo(project.file("war"));
    assertThat(task.getDeploy().get().getAsFile()).isEqualTo(
        project.file("deploy"));
    assertThat(task.getExtra().get().getAsFile()).isEqualTo(
        project.file("extra"));
    assertThat(task.getCacheDir().get().getAsFile()).isEqualTo(
        project.file("cacheDir"));
    assertThat(task.getModules().get())
        .containsExactly("com.example.MyModule");
  }

  @Test
  void registerGwtCompileTask() {
    /*
     * -------------------------------------------------------------------------
     * Given
     * -------------------------------------------------------------------------
     */
    // Create a test project and apply the plugin
    Project project = ProjectBuilder.builder().build();

    /*
     * -------------------------------------------------------------------------
     * When
     * -------------------------------------------------------------------------
     */
    project.getPlugins().apply("org.docstr.gwt");
    // Configure the gwt extension programmatically
    project.getExtensions().configure("gwt", ext -> {
      GwtPluginExtension extension = (GwtPluginExtension) ext;
      extension.getCompiler().getLogLevel().set("SPAM");
      extension.getCompiler().getWorkDir().set(project.file("workDir"));
      extension.getCompiler().getClosureFormattedOutput().set(true);
      extension.getCompiler().getCompileReport().set(true);
      extension.getCompiler().getStrict().set(true);
      extension.getCompiler().getClassMetadata().set(false);
      extension.getCompiler().getDraftCompile().set(true);
      extension.getCompiler().getCheckAssertions().set(true);
      extension.getCompiler().getFragmentCount().set(42);
      extension.getCompiler().getGen().set(project.file("gen"));
      extension.getCompiler().getGenerateJsInteropExports().set(true);
      extension.getCompiler().getIncludeJsInteropExports()
          .set(List.of("com.example.MyIncludeExport"));
      extension.getCompiler().getExcludeJsInteropExports()
          .set(List.of("com.example.MyExcludeExport"));
      extension.getCompiler().getMethodNameDisplayMode().set("FULL");
      extension.getCompiler().getNamespace().set("com.example.MyNamespace");
      extension.getCompiler().getOptimize().set(12);
      extension.getCompiler().getSaveSource().set(true);
      extension.getCompiler().getSetProperty().set(List.of("name=value"));
      extension.getCompiler().getStyle().set("PRETTY");
      extension.getCompiler().getFailOnError().set(true);
      extension.getCompiler().getValidateOnly().set(true);
      extension.getCompiler().getSourceLevel().set("17");
      extension.getCompiler().getLocalWorkers().set(4);
      extension.getCompiler().getIncremental().set(true);
      extension.getCompiler().getWar().set(project.file("war"));
      extension.getCompiler().getDeploy().set(project.file("deploy"));
      extension.getCompiler().getExtra().set(project.file("extra"));
      extension.getCompiler().getCacheDir().set(project.file("cacheDir"));
      extension.getCompiler().getSaveSourceOutput()
          .set(project.file("saveSourceOutput"));
      extension.getCompiler().getModules().set(List.of("com.example.MyModule"));
    });

    /*
     * -------------------------------------------------------------------------
     * Then
     * -------------------------------------------------------------------------
     */
    // Verify the result
    TaskContainer tasks = project.getTasks();
    GwtCompileTask task = (GwtCompileTask) tasks.findByName("gwtCompile");
    assertThat(task).isNotNull();
    assertThat(task.getLogLevel().get()).isEqualTo("SPAM");
    assertThat(task.getWorkDir().get().getAsFile()).isEqualTo(
        project.file("workDir"));
    assertThat(task.getClosureFormattedOutput().get()).isTrue();
    assertThat(task.getCompileReport().get()).isTrue();
    assertThat(task.getStrict().get()).isTrue();
    assertThat(task.getClassMetadata().get()).isFalse();
    assertThat(task.getDraftCompile().get()).isTrue();
    assertThat(task.getCheckAssertions().get()).isTrue();
    assertThat(task.getFragmentCount().get()).isEqualTo(42);
    assertThat(task.getGen().get().getAsFile()).isEqualTo(project.file("gen"));
    assertThat(task.getGenerateJsInteropExports().get()).isTrue();
    assertThat(task.getIncludeJsInteropExports().get())
        .containsExactly("com.example.MyIncludeExport");
    assertThat(task.getExcludeJsInteropExports().get())
        .containsExactly("com.example.MyExcludeExport");
    assertThat(task.getMethodNameDisplayMode().get()).isEqualTo("FULL");
    assertThat(task.getNamespace().get()).isEqualTo("com.example.MyNamespace");
    assertThat(task.getOptimize().get()).isEqualTo(12);
    assertThat(task.getSaveSource().get()).isTrue();
    assertThat(task.getSetProperty().get()).containsExactly("name=value");
    assertThat(task.getStyle().get()).isEqualTo("PRETTY");
    assertThat(task.getFailOnError().get()).isTrue();
    assertThat(task.getValidateOnly().get()).isTrue();
    assertThat(task.getSourceLevel().get()).isEqualTo("17");
    assertThat(task.getLocalWorkers().get()).isEqualTo(4);
    assertThat(task.getIncremental().get()).isTrue();
    assertThat(task.getWar().get().getAsFile()).isEqualTo(project.file("war"));
    assertThat(task.getDeploy().get().getAsFile()).isEqualTo(
        project.file("deploy"));
    assertThat(task.getExtra().get().getAsFile()).isEqualTo(
        project.file("extra"));
    assertThat(task.getCacheDir().get().getAsFile()).isEqualTo(
        project.file("cacheDir"));
    assertThat(task.getSaveSourceOutput().get().getAsFile())
        .isEqualTo(project.file("saveSourceOutput"));
    assertThat(task.getModules().get())
        .containsExactly("com.example.MyModule");
  }

  @Test
  void passArgumentsFromExtensionToGwtDevModeTask() {
    /*
     * -------------------------------------------------------------------------
     * Given
     * -------------------------------------------------------------------------
     */
    // Create a test project and apply the plugin
    Project project = ProjectBuilder.builder().build();

    /*
     * -------------------------------------------------------------------------
     * When
     * -------------------------------------------------------------------------
     */
    project.getPlugins().apply("org.docstr.gwt");
    // Configure the gwt extension programmatically
    project.getExtensions().configure("gwt", ext -> {
      GwtPluginExtension extension = (GwtPluginExtension) ext;
      extension.getMinHeapSize().set("111M");
      extension.getMaxHeapSize().set("222M");
      extension.getLogLevel().set("SPAM");
      extension.getWorkDir().set(project.file("workDir"));
      extension.getGen().set(project.file("gen"));
      extension.getGenerateJsInteropExports().set(true);
      extension.getIncludeJsInteropExports()
          .set(List.of("com.example.MyIncludeExport"));
      extension.getExcludeJsInteropExports()
          .set(List.of("com.example.MyExcludeExport"));
      extension.getMethodNameDisplayMode().set("FULL");
      extension.getSetProperty().set(List.of("name=value"));
      extension.getStyle().set("PRETTY");
      extension.getFailOnError().set(true);
      extension.getSourceLevel().set("17");
      extension.getIncremental().set(true);
      extension.getWar().set(project.file("war"));
      extension.getDeploy().set(project.file("deploy"));
      extension.getExtra().set(project.file("extra"));
      extension.getCacheDir().set(project.file("cacheDir"));
      extension.getModules().set(List.of("com.example.MyModule"));
    });

    /*
     * -------------------------------------------------------------------------
     * Then
     * -------------------------------------------------------------------------
     */
    // Verify the result
    TaskContainer tasks = project.getTasks();
    GwtDevModeTask task = (GwtDevModeTask) tasks.findByName("gwtDevMode");
    assertThat(task).isNotNull();
    assertThat(task.getMinHeapSize()).isEqualTo("111M");
    assertThat(task.getMaxHeapSize()).isEqualTo("222M");
    assertThat(task.getLogLevel().get()).isEqualTo("SPAM");
    assertThat(task.getWorkDir().get().getAsFile()).isEqualTo(
        project.file("workDir"));
    assertThat(task.getGen().get().getAsFile()).isEqualTo(project.file("gen"));
    assertThat(task.getGenerateJsInteropExports().get()).isTrue();
    assertThat(task.getIncludeJsInteropExports().get())
        .containsExactly("com.example.MyIncludeExport");
    assertThat(task.getExcludeJsInteropExports().get())
        .containsExactly("com.example.MyExcludeExport");
    assertThat(task.getMethodNameDisplayMode().get()).isEqualTo("FULL");
    assertThat(task.getSetProperty().get()).containsExactly("name=value");
    assertThat(task.getStyle().get()).isEqualTo("PRETTY");
    assertThat(task.getFailOnError().get()).isTrue();
    assertThat(task.getSourceLevel().get()).isEqualTo("17");
    assertThat(task.getIncremental().get()).isTrue();
    assertThat(task.getWar().get().getAsFile()).isEqualTo(project.file("war"));
    assertThat(task.getDeploy().get().getAsFile()).isEqualTo(
        project.file("deploy"));
    assertThat(task.getExtra().get().getAsFile()).isEqualTo(
        project.file("extra"));
    assertThat(task.getCacheDir().get().getAsFile()).isEqualTo(
        project.file("cacheDir"));
    assertThat(task.getModules().get())
        .containsExactly("com.example.MyModule");
  }

  @Test
  void registerGwtDevModeTask() {
    /*
     * -------------------------------------------------------------------------
     * Given
     * -------------------------------------------------------------------------
     */
    // Create a test project and apply the plugin
    Project project = ProjectBuilder.builder().build();

    /*
     * -------------------------------------------------------------------------
     * When
     * -------------------------------------------------------------------------
     */
    project.getPlugins().apply("org.docstr.gwt");
    // Configure the gwt extension programmatically
    project.getExtensions().configure("gwt", ext -> {
      GwtPluginExtension extension = (GwtPluginExtension) ext;
      extension.getDevMode().getStartServer().set(false);
      extension.getDevMode().getPort().set(8080);
      extension.getDevMode().getLogdir().set(project.file("logdir"));
      extension.getDevMode().getLogLevel().set("SPAM");
      extension.getDevMode().getGen().set(project.file("gen"));
      extension.getDevMode().getBindAddress().set("test-address");
      extension.getDevMode().getCodeServerPort().set(9999);
      extension.getDevMode().getSuperDevMode().set(false);
      extension.getDevMode().getServer().set("test-server");
      extension.getDevMode().getStartupUrl().set("test-url");
      extension.getDevMode().getWar().set(project.file("war"));
      extension.getDevMode().getDeploy().set(project.file("deploy"));
      extension.getDevMode().getExtra().set(project.file("extra"));
      extension.getDevMode().getCacheDir().set(project.file("cacheDir"));
      extension.getDevMode().getModulePathPrefix().set("test-prefix");
      extension.getDevMode().getWorkDir().set(project.file("workDir"));
      extension.getDevMode().getMethodNameDisplayMode().set("FULL");
      extension.getDevMode().getSourceLevel().set("17");
      extension.getDevMode().getGenerateJsInteropExports().set(true);
      extension.getDevMode().getIncludeJsInteropExports()
          .set(List.of("com.example.MyIncludeExport"));
      extension.getDevMode().getExcludeJsInteropExports()
          .set(List.of("com.example.MyExcludeExport"));
      extension.getDevMode().getIncremental().set(true);
      extension.getDevMode().getStyle().set("PRETTY");
      extension.getDevMode().getFailOnError().set(true);
      extension.getDevMode().getSetProperty().set(List.of("name=value"));
      extension.getDevMode().getModules().set(List.of("com.example.MyModule"));
    });

    /*
     * -------------------------------------------------------------------------
     * Then
     * -------------------------------------------------------------------------
     */
    // Verify the result
    TaskContainer tasks = project.getTasks();
    GwtDevModeTask task = (GwtDevModeTask) tasks.findByName("gwtDevMode");
    assertThat(task).isNotNull();
    assertThat(task.getStartServer().get()).isFalse();
    assertThat(task.getPort().get()).isEqualTo(8080);
    assertThat(task.getLogdir().get().getAsFile()).isEqualTo(
        project.file("logdir"));
    assertThat(task.getLogLevel().get()).isEqualTo("SPAM");
    assertThat(task.getGen().get().getAsFile()).isEqualTo(project.file("gen"));
    assertThat(task.getBindAddress().get()).isEqualTo("test-address");
    assertThat(task.getCodeServerPort().get()).isEqualTo(9999);
    assertThat(task.getSuperDevMode().get()).isFalse();
    assertThat(task.getServer().get()).isEqualTo("test-server");
    assertThat(task.getStartupUrl().get()).isEqualTo("test-url");
    assertThat(task.getWar().get().getAsFile()).isEqualTo(project.file("war"));
    assertThat(task.getDeploy().get().getAsFile()).isEqualTo(
        project.file("deploy"));
    assertThat(task.getExtra().get().getAsFile()).isEqualTo(
        project.file("extra"));
    assertThat(task.getCacheDir().get().getAsFile()).isEqualTo(
        project.file("cacheDir"));
    assertThat(task.getModulePathPrefix().get()).isEqualTo("test-prefix");
    assertThat(task.getWorkDir().get().getAsFile()).isEqualTo(
        project.file("workDir"));
    assertThat(task.getMethodNameDisplayMode().get()).isEqualTo("FULL");
    assertThat(task.getSourceLevel().get()).isEqualTo("17");
    assertThat(task.getGenerateJsInteropExports().get()).isTrue();
    assertThat(task.getIncludeJsInteropExports().get())
        .containsExactly("com.example.MyIncludeExport");
    assertThat(task.getExcludeJsInteropExports().get())
        .containsExactly("com.example.MyExcludeExport");
    assertThat(task.getIncremental().get()).isTrue();
    assertThat(task.getStyle().get()).isEqualTo("PRETTY");
    assertThat(task.getFailOnError().get()).isTrue();
    assertThat(task.getSetProperty().get()).containsExactly("name=value");
    assertThat(task.getModules().get())
        .containsExactly("com.example.MyModule");
  }
}
