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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
  /**
   * The default GWT version if it is not overridden in the GWT configuration block.
   */
  public static final String GWT_DEFAULT_VERSION = "2.12.1";
  /**
   * The servlet dependency. Note that the versions are very important. Less than v5.0.0 uses the javax namespace. Greater than or equal uses the jakarta namespace.
   */
  private static final String SERVLET_API_DEPENDENCY = "jakarta.servlet:jakarta.servlet-api";


  private Project project;

  @Override
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
    extension.getGwtVersion().convention(GWT_DEFAULT_VERSION);

    // Set default values for test options
    extension.getGwtTest().getShowStandardStreams().convention(false);

    return extension;
  }

  private void configureGwtProject(GwtPluginExtension extension) {
    project.afterEvaluate(p -> {
      // Add extra source directories to Java source sets if configured
      SourceSetContainer sourceSets = project.getExtensions()
          .getByType(SourceSetContainer.class);
      SourceSet mainSourceSet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME);
      
      // Collect all extra source directories from different configuration locations
      // Using LinkedHashSet to maintain order and avoid duplicates
      Set<Object> allExtraSourceDirs = new LinkedHashSet<>();
      if (!extension.getExtraSourceDirs().isEmpty()) {
        allExtraSourceDirs.addAll(extension.getExtraSourceDirs().getFiles());
      }
      if (!extension.getCompiler().getExtraSourceDirs().isEmpty()) {
        allExtraSourceDirs.addAll(extension.getCompiler().getExtraSourceDirs().getFiles());
      }
      if (!extension.getDevMode().getExtraSourceDirs().isEmpty()) {
        allExtraSourceDirs.addAll(extension.getDevMode().getExtraSourceDirs().getFiles());
      }
      if (!extension.getSuperDev().getExtraSourceDirs().isEmpty()) {
        allExtraSourceDirs.addAll(extension.getSuperDev().getExtraSourceDirs().getFiles());
      }
      
      // Add all collected directories to the source set at once
      if (!allExtraSourceDirs.isEmpty()) {
        mainSourceSet.getJava().srcDirs(allExtraSourceDirs);
      }

      DependencyHandler dependencies = project.getDependencies();

      // The configured GWT version
      final String gwtVersion = extension.getGwtVersion().get();

      // default to Jakarta packages, unless explicitly set to false
      final Boolean useJakarta = extension.getJakarta().getOrElse(true);
      final String gwtServlet = useJakarta ? "org.gwtproject:gwt-servlet-jakarta" : "org.gwtproject:gwt-servlet";

      // Add GWT dependencies automatically based on the gwtVersion in the extension. Add the platform to both
      // configurations in case the caller decides to split the inheritance.
      dependencies.add(
              JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME,
              dependencies.platform("org.gwtproject:gwt:" + gwtVersion));
      dependencies.add(
              GWT_DEV_CONFIGURATION_NAME,
              dependencies.platform("org.gwtproject:gwt:" + gwtVersion));

      // Assume that the servlet API is also provided by the servlet container.  Hence, the compileOnly here.
      // Use addProvider interface because it uses Action. In Gradle's documents, Action is preferred over Closure,
      // and it is much easier to use.
      dependencies.addProvider(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME, project.getProviders().provider(() -> SERVLET_API_DEPENDENCY), d -> {
        d.version(v -> {
          if (useJakarta) {
            // Want jakarta namespace
            v.strictly("[5.0.0,)");
            v.prefer("6.1.0");
          } else {
            // Want javax namespace
            v.strictly("(,5.0.0)");
            v.prefer("4.0.4");
          }
        });
      });
      dependencies.add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, gwtServlet);

      // gwtCompile and gwtDevMode requires gwt-dev (the compiler proper) and gwt-user (JRE emulation, Widgets, etc)
      dependencies.add(GWT_DEV_CONFIGURATION_NAME, "org.gwtproject:gwt-dev");
      dependencies.add(GWT_DEV_CONFIGURATION_NAME, "org.gwtproject:gwt-user");
      dependencies.add(GWT_DEV_CONFIGURATION_NAME, "org.gwtproject:gwt-codeserver");
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
