package org.docstr.gwt;

import static org.docstr.gwt.GwtSuperDevTask.CODE_SERVER_CLASS;

import java.util.ArrayList;
import javax.inject.Inject;
import org.gradle.api.GradleException;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;

/**
 * Base class for several GWT related tasks that share specific parameters.
 */
public abstract class AbstractBaseTask extends JavaExec {

  @Input
  @Optional
  private final Property<String> logLevel;
  @OutputDirectory
  @Optional
  private final DirectoryProperty workDir;
  @OutputDirectory
  @Optional
  private final DirectoryProperty gen;
  @OutputDirectory
  @Optional
  private final DirectoryProperty war;
  @OutputDirectory
  @Optional
  private final DirectoryProperty deploy;
  @OutputDirectory
  @Optional
  private final DirectoryProperty extra;
  @OutputDirectory
  @Optional
  private final DirectoryProperty cacheDir;
  @Input
  @Optional
  private final Property<String> sourceLevel;
  @Input
  @Optional
  private final Property<String> methodNameDisplayMode;
  @Input
  @Optional
  private final Property<Boolean> generateJsInteropExports;
  @Input
  @Optional
  private final ListProperty<String> includeJsInteropExports;
  @Input
  @Optional
  private final ListProperty<String> excludeJsInteropExports;
  @Input
  @Optional
  private final Property<String> style;
  @Input
  @Optional
  private final Property<Boolean> failOnError;
  @Input
  @Optional
  private final ListProperty<String> setProperty;
  @Input
  @Optional
  private final Property<Boolean> incremental;
  @Input
  private final ListProperty<String> modules;

  /**
   * Constructs a new GwtCompileTask.
   *
   * @param objects The object factory
   */
  @Inject
  public AbstractBaseTask(ObjectFactory objects) {
    logLevel = objects.property(String.class);
    workDir = objects.directoryProperty();
    gen = objects.directoryProperty();
    war = objects.directoryProperty();
    deploy = objects.directoryProperty();
    extra = objects.directoryProperty();
    cacheDir = objects.directoryProperty();
    sourceLevel = objects.property(String.class);
    methodNameDisplayMode = objects.property(String.class);
    generateJsInteropExports = objects.property(Boolean.class);
    includeJsInteropExports = objects.listProperty(String.class);
    excludeJsInteropExports = objects.listProperty(String.class);
    style = objects.property(String.class);
    failOnError = objects.property(Boolean.class);
    setProperty = objects.listProperty(String.class);
    incremental = objects.property(Boolean.class);
    modules = objects.listProperty(String.class);
  }

  @Override
  public void exec() {
    // Ensure the classpath includes compiled classes, resources, and source files
    setClasspath(getProject().files(
        getProject().getLayout().getBuildDirectory().dir("classes/java/main"),
        getProject().getLayout().getBuildDirectory().dir("resources/main"),
        getProject().file("src/main/java"),
        getProject().getConfigurations().getByName("runtimeClasspath")
    ));

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
      // Ensure the war directory exists
      if (!getWar().get().getAsFile().exists()) {
        boolean mkdirs = getWar().get().getAsFile().mkdirs();
        if (!mkdirs) {
          throw new GradleException(
              "Failed to create war directory: " + getWar().get().getAsFile());
        }
      }
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

    getModules().get().forEach(module -> args(module));

    getProject().getLogger()
        .lifecycle("jvmArgs: {}",
            getJvmArguments().getOrElse(new ArrayList<>()));
    getProject().getLogger()
        .lifecycle("args: {}", getArgs());

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
  public final Property<String> getLogLevel() {
    return logLevel;
  }

  /**
   * The compiler's working directory for internal use (must be writeable;
   * defaults to a system temp dir)
   *
   * @return The working directory
   */
  public final DirectoryProperty getWorkDir() {
    return workDir;
  }

  /**
   * Debugging: causes normally-transient generated types to be saved in the
   * specified directory
   *
   * @return The generated types directory
   */
  public final DirectoryProperty getGen() {
    return gen;
  }

  /**
   * The directory into which deployable output files will be written (defaults
   * to 'war')
   *
   * @return The war directory
   */
  public final DirectoryProperty getWar() {
    return war;
  }

  /**
   * The directory into which deployable but not servable output files will be
   * written (defaults to 'WEB-INF/deploy' under the -war directory/jar, and may
   * be the same as the -extra directory/jar)
   *
   * @return The deploy directory
   */
  public final DirectoryProperty getDeploy() {
    return deploy;
  }

  /**
   * The directory into which extra files, not intended for deployment, will be
   * written
   *
   * @return The extra directory
   */
  public final DirectoryProperty getExtra() {
    return extra;
  }

  /**
   * The directory to use for the persistent unit cache
   *
   * @return The cache directory
   */
  public final DirectoryProperty getCacheDir() {
    return cacheDir;
  }

  /**
   * The source level of the java code
   *
   * @return The source level
   */
  public final Property<String> getSourceLevel() {
    return sourceLevel;
  }

  /**
   * The method name display mode
   *
   * @return The method name display mode
   */
  public final Property<String> getMethodNameDisplayMode() {
    return methodNameDisplayMode;
  }

  /**
   * Generate exports for JsInterop purposes. If no
   * -includeJsInteropExport/-excludeJsInteropExport provided, generates all
   * exports. (defaults to OFF)
   *
   * @return The generate JsInterop exports flag
   */
  public final Property<Boolean> getGenerateJsInteropExports() {
    return generateJsInteropExports;
  }

  /**
   * Include members and classes while generating JsInterop exports
   *
   * @return The include JsInterop exports
   */
  public final ListProperty<String> getIncludeJsInteropExports() {
    return includeJsInteropExports;
  }

  /**
   * Exclude members and classes while generating JsInterop exports
   *
   * @return The exclude JsInterop exports
   */
  public final ListProperty<String> getExcludeJsInteropExports() {
    return excludeJsInteropExports;
  }

  /**
   * The style of output JavaScript: OBF, PRETTY, DETAILED, or DRAFT (defaults
   * to OBF)
   *
   * @return The style
   */
  public final Property<String> getStyle() {
    return style;
  }

  /**
   * Fail on errors
   *
   * @return The fail on error flag
   */
  public final Property<Boolean> getFailOnError() {
    return failOnError;
  }

  /**
   * Set the values of a property in the form of
   * propertyName=value1[,value2...].
   *
   * @return The set property
   */
  public final ListProperty<String> getSetProperty() {
    return setProperty;
  }

  /**
   * Incremental compilation
   *
   * @return The incremental flag
   */
  public final Property<Boolean> getIncremental() {
    return incremental;
  }

  /**
   * The modules to run
   *
   * @return The modules
   */
  public final ListProperty<String> getModules() {
    return modules;
  }
}
