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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;

/**
 * A plugin that adds GWT support to a project.
 */
public class GwtPlugin implements Plugin<Project> {

  private static final String GWT_VERSION = "2.11.0";

  public void apply(Project project) {
    // Ensure the Java plugin is applied if it hasn't been applied yet
    if (!project.getPlugins().hasPlugin(JavaPlugin.class)) {
      project.getPlugins().apply(JavaPlugin.class);
    }

    GwtPluginExtension extension = createGwtExtension(project);
    configureGwtProject(project, extension);
    createGwtTasks(project, extension);
  }

  private static GwtPluginExtension createGwtExtension(Project project) {
    // Register the GWT extension
    GwtPluginExtension extension = project.getExtensions()
        .create("gwt", GwtPluginExtension.class);
    extension.getWar().convention(project.getLayout().getBuildDirectory()
        .dir("gwt"));
    return extension;
  }

  private static void configureGwtProject(Project project,
      GwtPluginExtension extension) {
    project.afterEvaluate(p -> {
      // default to GWT_VERSION if not set
      String gwtVersion = extension.getGwtVersion().getOrElse(GWT_VERSION);

      // Add GWT dependencies automatically based on the gwtVersion in the extension
      project.getDependencies()
          .add("implementation", "org.gwtproject:gwt-user:" + gwtVersion);
      project.getDependencies()
          .add("implementation", "org.gwtproject:gwt-dev:" + gwtVersion);

      SourceSetContainer sourceSets = project.getExtensions()
          .getByType(SourceSetContainer.class);
      // Add 'src/main/java' as a resource directory for the main source set
      SourceSet mainSourceSet = sourceSets.getByName(
          SourceSet.MAIN_SOURCE_SET_NAME);
      mainSourceSet.getResources().srcDir("src/main/java");
      // Add 'src/test/java' as a resource directory for the test source set
      SourceSet testSourceSet = sourceSets.getByName(
          SourceSet.TEST_SOURCE_SET_NAME);
      testSourceSet.getResources().srcDir("src/test/java");
    });
  }

  private static void createGwtTasks(
      Project project, GwtPluginExtension extension) {
    // Register the GwtCompile task
    TaskProvider<GwtCompileTask> gwtCompileTask = project.getTasks()
        .register("gwtCompile", GwtCompileTask.class,
            new GwtCompileConfig(extension));

    // Ensure that gwtCompile runs automatically when build is executed
    project.getTasks().named("build")
        .configure(buildTask -> buildTask.dependsOn(gwtCompileTask));

    // Register the GwtDevModeTask task
    TaskProvider<GwtDevModeTask> gwtDevModeTask = project.getTasks()
        .register("gwtDevMode", GwtDevModeTask.class,
            new GwtDevModeConfig(extension));
    // Ensure that gwtDevMode always runs
    gwtDevModeTask.configure(
        task -> task.getOutputs().upToDateWhen(t -> false));
  }
}
