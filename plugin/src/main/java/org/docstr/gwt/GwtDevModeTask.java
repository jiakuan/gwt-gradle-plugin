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
import org.gradle.api.tasks.compile.JavaCompile;

/**
 * Task for running the GWT development mode.
 */
public abstract class GwtDevModeTask extends JavaExec {

  @Input
  @Optional
  private final Property<Boolean> startServer;
  @Input
  @Optional
  private final Property<Integer> port;
  @OutputDirectory
  @Optional
  private final DirectoryProperty logdir;
  @Input
  @Optional
  private final Property<String> logLevel;
  @OutputDirectory
  @Optional
  private final DirectoryProperty gen;
  @Input
  @Optional
  private final Property<String> bindAddress;
  @Input
  @Optional
  private final Property<Integer> codeServerPort;
  @Input
  @Optional
  private final Property<Boolean> superDevMode;
  @Input
  @Optional
  private final Property<String> server;
  @Input
  @Optional
  private final Property<String> startupUrl;
  @OutputDirectory
  @Optional
  private final DirectoryProperty war;
  @OutputDirectory
  @Optional
  private final DirectoryProperty deploy;
  @OutputDirectory
  @Optional
  private final DirectoryProperty extra;
  @Input
  @Optional
  private final Property<String> modulePathPrefix;
  @OutputDirectory
  @Optional
  private final DirectoryProperty workDir;
  @Input
  @Optional
  private final Property<String> methodNameDisplayMode;
  @Input
  @Optional
  private final Property<String> sourceLevel;
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
  private final Property<Boolean> incremental;
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
  private final ListProperty<String> modules;

  /**
   * Constructs a new GwtDevModeTask.
   * @param objects The object factory
   */
  @Inject
  public GwtDevModeTask(ObjectFactory objects) {
    this.startServer = objects.property(Boolean.class);
    this.port = objects.property(Integer.class);
    this.logdir = objects.directoryProperty();
    this.logLevel = objects.property(String.class);
    this.gen = objects.directoryProperty();
    this.bindAddress = objects.property(String.class);
    this.codeServerPort = objects.property(Integer.class);
    this.superDevMode = objects.property(Boolean.class);
    this.server = objects.property(String.class);
    this.startupUrl = objects.property(String.class);
    this.war = objects.directoryProperty();
    this.deploy = objects.directoryProperty();
    this.extra = objects.directoryProperty();
    this.modulePathPrefix = objects.property(String.class);
    this.workDir = objects.directoryProperty();
    this.methodNameDisplayMode = objects.property(String.class);
    this.sourceLevel = objects.property(String.class);
    this.generateJsInteropExports = objects.property(Boolean.class);
    this.includeJsInteropExports = objects.listProperty(String.class);
    this.excludeJsInteropExports = objects.listProperty(String.class);
    this.incremental = objects.property(Boolean.class);
    this.style = objects.property(String.class);
    this.failOnError = objects.property(Boolean.class);
    this.setProperty = objects.listProperty(String.class);
    this.modules = objects.listProperty(String.class);

    // Set GWT compiler as the main class
    getMainClass().set("com.google.gwt.dev.DevMode");

    // This task will depend on the compileJava task automatically
    dependsOn(getProject().getTasks().withType(JavaCompile.class));
  }

  /**
   * Starts a servlet container serving the directory specified by
   * the -war flag. (defaults to ON)
   * @return The startServer property
   */
  public Property<Boolean> getStartServer() {
    return startServer;
  }

  /**
   * Specifies the TCP port for the embedded web server (defaults to 8888)
   * @return The port property
   */
  public Property<Integer> getPort() {
    return port;
  }

  /**
   * Logs to a file in the given directory, as well as graphically
   * @return The logdir property
   */
  public DirectoryProperty getLogdir() {
    return logdir;
  }

  /**
   * The level of logging detail: ERROR, WARN, INFO, TRACE, DEBUG, SPAM
   * or ALL (defaults to INFO)
   * @return The log level
   */
  public Property<String> getLogLevel() {
    return logLevel;
  }

  /**
   * Debugging: causes normally-transient generated types to be saved
   * in the specified directory
   * @return The generated types directory
   */
  public DirectoryProperty getGen() {
    return gen;
  }

  /**
   * Specifies the bind address for the code server and web server
   * (defaults to 127.0.0.1)
   * @return The bind address
   */
  public Property<String> getBindAddress() {
    return bindAddress;
  }

  /**
   * Specifies the TCP port for the code server (defaults to 9997 for
   * classic Dev Mode or 9876 for Super Dev Mode)
   * @return The code server port
   */
  public Property<Integer> getCodeServerPort() {
    return codeServerPort;
  }

  /**
   * Runs Super Dev Mode instead of classic Development Mode. (defaults to ON)
   * @return The superDevMode property
   */
  public Property<Boolean> getSuperDevMode() {
    return superDevMode;
  }

  /**
   * Specify a different embedded web server to run
   * (must implement ServletContainerLauncher)
   * @return The server property
   */
  public Property<String> getServer() {
    return server;
  }

  /**
   * Automatically launches the specified URL
   * @return The startup URL
   */
  public Property<String> getStartupUrl() {
    return startupUrl;
  }

  /**
   * The directory into which deployable output files will be written
   * (defaults to 'war')
   * @return The war directory
   */
  public DirectoryProperty getWar() {
    return war;
  }

  /**
   * The directory into which deployable but not servable output files
   * will be written (defaults to 'WEB-INF/deploy' under the -war
   * directory/jar, and may be the same as the -extra directory/jar)
   * @return The deploy directory
   */
  public DirectoryProperty getDeploy() {
    return deploy;
  }

  /**
   * The directory into which extra files, not intended for
   * deployment, will be written
   * @return The extra directory
   */
  public DirectoryProperty getExtra() {
    return extra;
  }

  /**
   * The prefix to prepend to module path
   * @return The module path prefix
   */
  public Property<String> getModulePathPrefix() {
    return modulePathPrefix;
  }

  /**
   * The subdirectory inside the war dir where DevMode will create
   * module directories. (defaults empty for top level)
   * @return The work directory
   */
  public DirectoryProperty getWorkDir() {
    return workDir;
  }

  /**
   * The method name display mode
   * @return The method name display mode
   */
  public Property<String> getMethodNameDisplayMode() {
    return methodNameDisplayMode;
  }

  /**
   * The source level
   * @return The source level
   */
  public Property<String> getSourceLevel() {
    return sourceLevel;
  }

  /**
   * Generate exports for JsInterop purposes. If no
   * -includeJsInteropExport/-excludeJsInteropExport provided, generates
   * all exports. (defaults to OFF)
   * @return The generate JsInterop exports flag
   */
  public Property<Boolean> getGenerateJsInteropExports() {
    return generateJsInteropExports;
  }

  /**
   * Include members and classes while generating JsInterop exports
   * @return The include JsInterop exports
   */
  public ListProperty<String> getIncludeJsInteropExports() {
    return includeJsInteropExports;
  }

  /**
   * Exclude members and classes while generating JsInterop exports
   * @return The exclude JsInterop exports
   */
  public ListProperty<String> getExcludeJsInteropExports() {
    return excludeJsInteropExports;
  }

  /**
   * Incremental compilation
   * @return The incremental flag
   */
  public Property<Boolean> getIncremental() {
    return incremental;
  }

  /**
   * The style of output JavaScript: OBF, PRETTY, DETAILED, or
   * DRAFT (defaults to OBF)
   * @return The style
   */
  public Property<String> getStyle() {
    return style;
  }

  /**
   * Fail on errors
   * @return The fail on error flag
   */
  public Property<Boolean> getFailOnError() {
    return failOnError;
  }

  /**
   * Set the values of a property in the form of
   * propertyName=value1[,value2...].
   * @return The set property
   */
  public ListProperty<String> getSetProperty() {
    return setProperty;
  }

  /**
   * The modules to run
   * @return The modules
   */
  public ListProperty<String> getModules() {
    return modules;
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

    if (getStartServer().isPresent()) {
      if (getStartServer().get()) {
        args("-startServer");
      } else {
        args("-nostartServer");
      }
    }

    if (getPort().isPresent()) {
      args("-port", getPort().get());
    }

    if (getLogdir().isPresent()) {
      args("-logdir", getLogdir().get().getAsFile().getPath());
    }

    if (getLogLevel().isPresent()) {
      args("-logLevel", getLogLevel().get());
    }

    if (getGen().isPresent()) {
      args("-gen", getGen().get().getAsFile().getPath());
    }

    if (getBindAddress().isPresent()) {
      args("-bindAddress", getBindAddress().get());
    }

    if (getCodeServerPort().isPresent()) {
      args("-codeServerPort", getCodeServerPort().get());
    }

    if (getSuperDevMode().isPresent()) {
      if (getSuperDevMode().get()) {
        args("-superDevMode");
      } else {
        args("-nosuperDevMode");
      }
    }

    if (getServer().isPresent()) {
      args("-server", getServer().get());
    }

    if (getStartupUrl().isPresent()) {
      args("-startupUrl", getStartupUrl().get());
    }

    if (getWar().isPresent()) {
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

    if (getDeploy().isPresent()) {
      args("-deploy", getDeploy().get().getAsFile().getPath());
    }

    if (getExtra().isPresent()) {
      args("-extra", getExtra().get().getAsFile().getPath());
    }

    if (getModulePathPrefix().isPresent()) {
      args("-modulePathPrefix", getModulePathPrefix().get());
    }

    if (getWorkDir().isPresent()) {
      args("-workDir", getWorkDir().get().getAsFile().getPath());
    }

    if (getMethodNameDisplayMode().isPresent()) {
      args("-XmethodNameDisplayMode", getMethodNameDisplayMode().get());
    }

    if (getSourceLevel().isPresent()) {
      args("-sourceLevel", getSourceLevel().get());
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

    if (getIncremental().isPresent()) {
      if (getIncremental().get()) {
        args("-incremental");
      } else {
        args("-noincremental");
      }
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

    getModules().get().forEach(module -> args(module));

    // Logging the command line arguments
    getProject().getLogger()
        .lifecycle("gwtDev args: {}", getArgs());

    super.exec();
  }
}
