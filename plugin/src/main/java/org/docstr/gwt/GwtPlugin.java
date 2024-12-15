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

import java.util.List;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.testing.Test;

/**
 * A plugin that adds GWT support to a project.
 */
public class GwtPlugin implements Plugin<Project> {

  private static final String GWT_VERSION = "2.12.1";

  private Project project;

  public void apply(Project project) {
    this.project = project;

    // Ensure the Java plugin is applied if it hasn't been applied yet
    if (!project.getPlugins().hasPlugin(JavaPlugin.class)) {
      project.getPlugins().apply(JavaPlugin.class);
    }

    GwtPluginExtension extension = createGwtExtension();
    configureGwtProject(extension);
    configureGwtTasks(extension);
  }

  private GwtPluginExtension createGwtExtension() {
    // Register the GWT extension
    GwtPluginExtension extension = project.getExtensions()
        .create("gwt", GwtPluginExtension.class);

    // Set default values for the extension
    extension.getWar().convention(project.getLayout().getBuildDirectory()
        .dir("gwt"));
    extension.getDeploy().convention(project.getLayout().getBuildDirectory()
        .dir("gwt/deploy"));
    extension.getExtra().convention(project.getLayout().getBuildDirectory()
        .dir("gwt/extra"));
    extension.getCacheDir().convention(project.getLayout().getBuildDirectory()
        .dir("gwt-unitCache"));
    return extension;
  }

  private void configureGwtProject(GwtPluginExtension extension) {
    project.afterEvaluate(p -> {
      // default to GWT_VERSION if not set
      String gwtVersion = extension.getGwtVersion().getOrElse(GWT_VERSION);

      // Add GWT dependencies automatically based on the gwtVersion in the extension
      project.getDependencies()
          .add("implementation", "org.gwtproject:gwt-user:" + gwtVersion);
      project.getDependencies()
          .add("implementation", "org.gwtproject:gwt-dev:" + gwtVersion);
      project.getDependencies()
          .add("implementation", "org.gwtproject:gwt-codeserver:" + gwtVersion);

      SourceSetContainer sourceSets = project.getExtensions()
          .getByType(SourceSetContainer.class);
      // Add 'src/main/java' as a resource directory for the main source set
      SourceSet mainSourceSet = sourceSets.getByName(
          SourceSet.MAIN_SOURCE_SET_NAME);
      mainSourceSet.getResources().srcDir("src/main/java");
    });
  }

  private void configureGwtTasks(GwtPluginExtension extension) {
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

    // Register the GwtSuperDevTask task
    TaskProvider<GwtSuperDevTask> gwtSuperDevTask = project.getTasks()
        .register("gwtSuperDev", GwtSuperDevTask.class,
            new GwtSuperDevConfig(extension));
    // Ensure that gwtSuperDev always runs
    gwtSuperDevTask.configure(
        task -> task.getOutputs().upToDateWhen(t -> false));

    // Configure the GWT test tasks
    project.afterEvaluate(p -> {
      ListProperty<String> testTasks = extension.getGwtTest().getTestTasks();
      if (testTasks.isPresent()) {
        List<String> testTaskNames = testTasks.get();
        project.getTasks()
            .withType(Test.class)
            .matching(t -> testTaskNames.isEmpty() || testTaskNames.contains(
                t.getName()))
            .configureEach(new GwtTestConfig(project, extension));
      }
    });
  }
}
