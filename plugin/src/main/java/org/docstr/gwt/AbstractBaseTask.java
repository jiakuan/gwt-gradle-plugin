package org.docstr.gwt;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.logging.Logger;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.*;

import java.util.stream.Collectors;

import static org.docstr.gwt.GwtSuperDevTask.CODE_SERVER_CLASS;

/**
 * Base class for several GWT related tasks that share specific parameters.
 */
public abstract class AbstractBaseTask extends JavaExec {

  /**
   * Configure task arguments during configuration phase.
   * This method should be called from task configuration actions to set up all arguments.
   */
  public void configureArgs() {
    if (getLogLevel().isPresent()) {
      args("-logLevel", getLogLevel().get());
    }

    if (getWorkDir().isPresent()) {
      args("-workDir", getWorkDir().get().getAsFile().getPath());
    }

    if (!isCodeServerTask() && getGen().isPresent()) {
      args("-gen", getGen().get().getAsFile().getPath());
    }

    if (!isCodeServerTask() && getWar().isPresent()) {
      args("-war", getWar().get().getAsFile().getPath());
    }

    if (!isCodeServerTask() && getDeploy().isPresent()) {
      args("-deploy", getDeploy().get().getAsFile().getPath());
    }

    if (!isCodeServerTask() && getExtra().isPresent()) {
      args("-extra", getExtra().get().getAsFile().getPath());
    }

    if (!isCodeServerTask() && getCacheDir().isPresent()) {
      jvmArgs("-Dgwt.persistentunitcachedir=" + getCacheDir().get().getAsFile()
          .getPath());
    }

    if (getSourceLevel().isPresent()) {
      args("-sourceLevel", getSourceLevel().get());
    }

    if (getMethodNameDisplayMode().isPresent()) {
      args("-XmethodNameDisplayMode", getMethodNameDisplayMode().get());
    }

    if (getGenerateJsInteropExports().isPresent()) {
      if (getGenerateJsInteropExports().get()) {
        args("-generateJsInteropExports");
      } else {
        args("-nogenerateJsInteropExports");
      }
    }

    if (getIncludeJsInteropExports().isPresent()) {
      getIncludeJsInteropExports().get()
          .forEach(include -> args("-includeJsInteropExports", include));
    }

    if (getExcludeJsInteropExports().isPresent()) {
      getExcludeJsInteropExports().get()
          .forEach(exclude -> args("-excludeJsInteropExports", exclude));
    }

    if (getStyle().isPresent()) {
      args("-style", getStyle().get());
    }

    if (getFailOnError().isPresent()) {
      if (getFailOnError().get()) {
        args("-failOnError");
      } else {
        args("-nofailOnError");
      }
    }

    if (getSetProperty().isPresent()) {
      getSetProperty().get()
          .forEach(property -> args("-setProperty", property));
    }

    if (getIncremental().isPresent()) {
      if (getIncremental().get()) {
        args("-incremental");
      } else {
        args("-noincremental");
      }
    }

    getModules().get().forEach(this::args);
  }

  @Override
  public void exec() {
    // Ensure the war directory exists before executing
    if (!isCodeServerTask() && getWar().isPresent()) {
      if (!getWar().get().getAsFile().exists()) {
        boolean mkdirs = getWar().get().getAsFile().mkdirs();
        if (!mkdirs) {
          throw new GradleException(
              "Failed to create war directory: " + getWar().get().getAsFile());
        }
      }
    }

    // Log the classpath and args
    Logger log = getLogger();
    getClasspath().getFiles().forEach(file -> log.debug("classpath: {}", file));
    log.info("classpath: {}", getClasspath().getAsPath());
    log.info("allJvmArgs: {}", getAllJvmArgs().stream().map(arg -> "\"" + arg + "\"").collect(Collectors.joining(", ")));
    log.info("main: {}", getMainClass().get());
    log.info("args: {}", getArgs().stream().map(arg -> "\"" + arg + "\"").collect(Collectors.joining(", ")));
    super.exec();
  }

  private boolean isCodeServerTask() {
    return CODE_SERVER_CLASS.equals(getMainClass().get());
  }

  /**
   * The level of logging detail: ERROR, WARN, INFO, TRACE, DEBUG, SPAM or ALL
   * (defaults to INFO)
   *
   * @return The log level
   */
  @Console
  public abstract Property<String> getLogLevel();

  /**
   * The compiler's working directory for internal use (must be writeable;
   * defaults to a system temp dir)
   *
   * @return The working directory
   */
  @OutputDirectory
  @Optional
  public abstract DirectoryProperty getWorkDir();

  /**
   * Debugging: causes normally-transient generated types to be saved in the
   * specified directory
   *
   * @return The generated types directory
   */
  @OutputDirectory
  @Optional
  public abstract DirectoryProperty getGen();

  /**
   * The directory into which deployable output files will be written (defaults
   * to 'war')
   *
   * @return The war directory
   */
  @OutputDirectory
  @Optional
  public abstract DirectoryProperty getWar();

  /**
   * The directory into which deployable but not servable output files will be
   * written (defaults to 'WEB-INF/deploy' under the -war directory/jar, and may
   * be the same as the -extra directory/jar)
   *
   * @return The deploy directory
   */
  @OutputDirectory
  @Optional
  public abstract DirectoryProperty getDeploy();

  /**
   * The directory into which extra files, not intended for deployment, will be
   * written
   *
   * @return The extra directory
   */
  @OutputDirectory
  @Optional
  public abstract DirectoryProperty getExtra();

  /**
   * The directory to use for the persistent unit cache
   *
   * @return The cache directory
   */
  @OutputDirectory
  @Optional
  public abstract DirectoryProperty getCacheDir();

  /**
   * The source level of the java code
   *
   * @return The source level
   */
  @Input
  @Optional
  public abstract Property<String> getSourceLevel();

  /**
   * The method name display mode
   *
   * @return The method name display mode
   */
  @Input
  @Optional
  public abstract Property<String> getMethodNameDisplayMode();

  /**
   * Generate exports for JsInterop purposes. If no
   * -includeJsInteropExport/-excludeJsInteropExport provided, generates all
   * exports. (defaults to OFF)
   *
   * @return The generate JsInterop exports flag
   */
  @Input
  @Optional
  public abstract Property<Boolean> getGenerateJsInteropExports();

  /**
   * Include members and classes while generating JsInterop exports
   *
   * @return The include JsInterop exports
   */
  @Input
  @Optional
  public abstract ListProperty<String> getIncludeJsInteropExports();

  /**
   * Exclude members and classes while generating JsInterop exports
   *
   * @return The exclude JsInterop exports
   */
  @Input
  @Optional
  public abstract ListProperty<String> getExcludeJsInteropExports();

  /**
   * The style of output JavaScript: OBF, PRETTY, DETAILED, or DRAFT (defaults
   * to OBF)
   *
   * @return The style
   */
  @Input
  @Optional
  public abstract Property<String> getStyle();

  /**
   * Fail on errors
   *
   * @return The fail on error flag
   */
  @Input
  @Optional
  public abstract Property<Boolean> getFailOnError();

  /**
   * Set the values of a property in the form of
   * propertyName=value1[,value2...].
   *
   * @return The set property
   */
  @Input
  @Optional
  public abstract ListProperty<String> getSetProperty();

  /**
   * Incremental compilation
   *
   * @return The incremental flag
   */
  @Input
  @Optional
  public abstract Property<Boolean> getIncremental();

  /**
   * The modules to run
   *
   * @return The modules
   */
  @Input
  public abstract ListProperty<String> getModules();

  /**
   * Extra source directories to include in the GWT compiler classpath
   *
   * @return The extra source directories
   */
  @InputFiles
  @PathSensitive(PathSensitivity.RELATIVE)
  @Optional
  public abstract ConfigurableFileCollection getExtraSourceDirs();

  /**
   * The GWT dev runtime classpath
   *
   * @return The GWT dev runtime classpath
   */
  @Classpath
  public abstract ConfigurableFileCollection getGwtDevRuntimeClasspath();

  /**
   * Configures the classpath for this task during configuration phase.
   * This method should be called from task configuration actions to avoid Configuration Cache issues.
   *
   * @param project The project to get source sets and configurations from
   */
  public void configureClasspath(Project project) {
    SourceSetContainer sourceSets = project.getExtensions()
        .getByType(SourceSetContainer.class);
    SourceSet mainSourceSet = sourceSets.getByName(
        SourceSet.MAIN_SOURCE_SET_NAME);

    // Collect all source paths
    FileCollection mainSourcePaths = project.files(mainSourceSet.getAllSource().getSrcDirs());
    FileCollection outputClasspath = mainSourceSet.getOutput().getClassesDirs()
        .plus(project.files(mainSourceSet.getOutput().getResourcesDir()));

    // Include extra source directories if specified
    FileCollection allSourcePaths = mainSourcePaths;
    if (!getExtraSourceDirs().isEmpty()) {
      allSourcePaths = allSourcePaths.plus(getExtraSourceDirs());
    }

    // Set up the GWT dev runtime classpath
    getGwtDevRuntimeClasspath().from(
        project.getConfigurations().getByName(GwtPlugin.GWT_DEV_RUNTIME_CLASSPATH_CONFIGURATION_NAME)
    );

    // Ensure the classpath includes compiled classes, resources, and source files
    classpath(
        allSourcePaths,
        outputClasspath,
        getGwtDevRuntimeClasspath()
    );
  }
}
