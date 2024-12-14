package org.docstr.gwt;

import java.io.File;
import java.util.Set;
import org.docstr.gwt.options.GwtTestOptions;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.testing.Test;

/**
 * Configures the test task for GWT.
 */
public class GwtTestConfig implements Action<Test> {

  private final Project project;
  private final GwtPluginExtension extension;

  /**
   * Constructor.
   *
   * @param project The project
   * @param extension The GWT plugin extension
   */
  public GwtTestConfig(Project project, GwtPluginExtension extension) {
    this.project = project;
    this.extension = extension;
  }

  @Override
  public void execute(Test test) {
    // Make standard output display configurable
    test.testLogging(testLogging -> {
      // Only show standard streams if specifically enabled
      testLogging.setShowStandardStreams(
          extension.getGwtTest().getShowStandardStreams().getOrElse(false));
    });

    if (extension.getDevMode().getMinHeapSize().isPresent()) {
      test.setMinHeapSize(extension.getDevMode().getMinHeapSize().get());
    } else {
      test.setMinHeapSize(extension.getMinHeapSize().getOrElse("256M"));
    }
    if (extension.getDevMode().getMaxHeapSize().isPresent()) {
      test.setMaxHeapSize(extension.getDevMode().getMaxHeapSize().get());
    } else {
      test.setMaxHeapSize(extension.getMaxHeapSize().getOrElse("512M"));
    }

    GwtTestOptions testOptions = extension.getGwtTest();
    if (!testOptions.getWar().isPresent()) {
      testOptions.getWar().set(extension.getWar().getOrNull());
    }
    if (!testOptions.getDeploy().isPresent()) {
      testOptions.getDeploy().set(extension.getDeploy().getOrNull());
    }
    if (!testOptions.getExtra().isPresent()) {
      testOptions.getExtra().set(extension.getExtra().getOrNull());
    }
    if (!testOptions.getCacheDir().isPresent()) {
      testOptions.getCacheDir().set(extension.getCacheDir().getOrNull());
    }

    // Retrieve the main source set
    SourceSetContainer sourceSets = project.getExtensions()
        .getByType(SourceSetContainer.class);

    SourceSet mainSourceSet = sourceSets.getByName(
        SourceSet.MAIN_SOURCE_SET_NAME);
    Set<File> mainSourcePaths = mainSourceSet.getAllSource().getSrcDirs();
    FileCollection mainOutputClasspath = mainSourceSet.getOutput()
        .getClassesDirs()
        .plus(project.files(mainSourceSet.getOutput().getResourcesDir()));

    SourceSet testSourceSet = sourceSets.getByName(
        SourceSet.TEST_SOURCE_SET_NAME);
    Set<File> testSourcePaths = testSourceSet.getAllSource().getSrcDirs();
    FileCollection testOutputClasspath = testSourceSet.getOutput()
        .getClassesDirs()
        .plus(project.files(testSourceSet.getOutput().getResourcesDir()));

    // Ensure the test *compile* classpath includes the GWT compiler
    project.getConfigurations().getByName(test.getName() + "Implementation", c -> {
      c.extendsFrom(
              project.getConfigurations().getByName(GwtPlugin.GWT_DEV_RUNTIME_CLASSPATH_CONFIGURATION_NAME)
      );
    });

    // Ensure the classpath includes compiled classes, resources, and source files
    test.setClasspath(test.getClasspath().plus(project.files(
        mainSourcePaths,
        mainOutputClasspath,
        mainSourceSet.getRuntimeClasspath(),

        testSourcePaths,
        testOutputClasspath,
        testSourceSet.getRuntimeClasspath()
    )));

    String gwtArgs = testOptions.getParameterString();
    test.systemProperty("gwt.args", gwtArgs);
    Logger log = project.getLogger();
    log.info("Using gwt.args for test: {}", gwtArgs);

    DirectoryProperty cacheDirProperty = extension.getCacheDir();
    if (cacheDirProperty != null && cacheDirProperty.isPresent()) {
      File cacheDir = cacheDirProperty.getAsFile().get();
      test.systemProperty("gwt.persistentunitcachedir", cacheDir);
      cacheDir.mkdirs();
      log.info("Using gwt.persistentunitcachedir for test: {0}",
          cacheDir);
    }
  }
}
