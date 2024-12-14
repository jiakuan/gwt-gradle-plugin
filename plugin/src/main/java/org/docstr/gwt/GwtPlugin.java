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
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.dsl.DependencyHandler;
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

  /**
   * Dependency Scope configuration, which extends from {@link JavaPlugin#IMPLEMENTATION_CONFIGURATION_NAME implementation} dependency scope configuration.
   */
  public static final String GWT_DEV_CONFIGURATION_NAME = "gwtDev";
  /**
   * Resolvable configuration, which extends from {@link #GWT_DEV_CONFIGURATION_NAME}.
   */
  public static final String GWT_DEV_RUNTIME_CLASSPATH_CONFIGURATION_NAME = "gwtDevRuntimeClasspath";

  private static final String GWT_VERSION = "2.12.1";

  private Project project;

  public void apply(Project project) {
    this.project = project;

    // Ensure the Java plugin is applied if it hasn't been applied yet
    if (!project.getPlugins().hasPlugin(JavaPlugin.class)) {
      project.getPlugins().apply(JavaPlugin.class);
    }

    configureConfigurations();

    GwtPluginExtension extension = createGwtExtension();
    configureGwtProject(extension);
    configureGwtTasks(extension);
  }

  private void configureConfigurations() {
    createDependencyScopeAndResolvable(
            JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME,
            GWT_DEV_CONFIGURATION_NAME,
            GWT_DEV_RUNTIME_CLASSPATH_CONFIGURATION_NAME);
  }

  private void createDependencyScopeAndResolvable(
          String extendFromConfigurationName,
          String dependencyScopeName,
          String runtimeClasspathName) {
    ConfigurationContainer configurations = project.getConfigurations();
    Configuration extendedConfiguration = configurations.getByName(extendFromConfigurationName);

    // Create a dependency scope that extends from the given dependency scope
    Configuration dependencyScope = configurations.create(dependencyScopeName, it -> {
      it.setCanBeResolved(false);
      it.setCanBeConsumed(false);
      it.extendsFrom(extendedConfiguration);
    });
    // Gradle 8.4 (incubating API; final in 9?) would allow us to do the same with:
//    NamedDomainObjectProvider<DependencyScopeConfiguration> dependencyScope
//            = configurations.dependencyScope(dependencyScopeName,
//            it -> it.extendsFrom(extendedConfiguration));

    // Create a resolvable configuration that extends from the created dependency scope
    configurations.create(runtimeClasspathName, it -> {
      it.setCanBeConsumed(false);
      it.setCanBeResolved(true);
      it.extendsFrom(dependencyScope);
    });
    // Gradle 8.4 (incubating API; final in 9?) would allow us to do the same with:
//    configurations.resolvable(runtimeClasspathName,
//            it -> it.extendsFrom(dependencyScope.get()));
  }

  private GwtPluginExtension createGwtExtension() {
    // Register the GWT extension
    GwtPluginExtension extension = project.getExtensions()
        .create("gwt", GwtPluginExtension.class);

    // Set default values for the extension
    extension.getWar().convention(project.getLayout().getBuildDirectory()
        .dir("gwt/war"));
    extension.getDeploy().convention(project.getLayout().getBuildDirectory()
        .dir("gwt/deploy"));
    extension.getGen().convention(project.getLayout().getBuildDirectory()
        .dir("gwt/gen"));
    extension.getExtra().convention(project.getLayout().getBuildDirectory()
        .dir("gwt/extra"));
    extension.getCacheDir().convention(project.getLayout().getBuildDirectory()
        .dir("gwt/gwt-unitCache"));

    // Set default values for test options
    extension.getGwtTest().getShowStandardStreams().convention(false);

    return extension;
  }

  private void configureGwtProject(GwtPluginExtension extension) {
    project.afterEvaluate(p -> {
      DependencyHandler dependencies = project.getDependencies();

      // default to GWT_VERSION if not set
      String gwtVersion = extension.getGwtVersion().getOrElse(GWT_VERSION);

      // default to Jakarta packages, unless explicitly set to false
      Boolean useJakarta = extension.getJakarta().getOrElse(true);
      String gwtServlet = useJakarta ? "org.gwtproject:gwt-servlet-jakarta" : "org.gwtproject:gwt-servlet";
      String servletApi = useJakarta ? "jakarta.servlet:jakarta.servlet-api:6.1.0" : "javax.servlet:javax.servlet-api";

      // Add GWT dependencies automatically based on the gwtVersion in the extension.
      dependencies.add(
              JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME,
              dependencies.platform("org.gwtproject:gwt:" + gwtVersion));

      // Assume that the servlet API is also provided by the servlet container.  Hence, the compileOnly here.
      dependencies.add(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME, servletApi);
      dependencies.add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, gwtServlet);

      // gwtCompile and gwtDevMode requires gwt-dev (the compiler proper) and gwt-user (JRE emulation, Widgets, etc)
      dependencies.add(GWT_DEV_CONFIGURATION_NAME, "org.gwtproject:gwt-dev");
      dependencies.add(GWT_DEV_CONFIGURATION_NAME, "org.gwtproject:gwt-user");
      dependencies.add(GWT_DEV_CONFIGURATION_NAME, "org.gwtproject:gwt-codeserver");

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
