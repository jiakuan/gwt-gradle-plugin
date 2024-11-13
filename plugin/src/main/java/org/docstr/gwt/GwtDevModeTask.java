/**
 * Copyright (C) 2024 Document Node Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.docstr.gwt;

import javax.inject.Inject;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.compile.JavaCompile;

/**
 * Task for running the GWT development mode.
 */
public abstract class GwtDevModeTask extends AbstractBaseTask {

  /**
   * The main class for the GWT development mode.
   */
  public static final String DEV_MODE_CLASS = "com.google.gwt.dev.DevMode";

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
  @Input
  @Optional
  private final Property<String> modulePathPrefix;

  /**
   * Constructs a new GwtDevModeTask.
   *
   * @param objects The object factory
   */
  @Inject
  public GwtDevModeTask(ObjectFactory objects) {
    super(objects);
    this.startServer = objects.property(Boolean.class);
    this.port = objects.property(Integer.class);
    this.logdir = objects.directoryProperty();
    this.bindAddress = objects.property(String.class);
    this.codeServerPort = objects.property(Integer.class);
    this.superDevMode = objects.property(Boolean.class);
    this.server = objects.property(String.class);
    this.startupUrl = objects.property(String.class);
    this.modulePathPrefix = objects.property(String.class);

    // Set GWT compiler as the main class
    getMainClass().set(DEV_MODE_CLASS);

    // This task will depend on the compileJava task automatically
    dependsOn(getProject().getTasks().withType(JavaCompile.class));
  }

  /**
   * Starts a servlet container serving the directory specified by
   * the -war flag. (defaults to ON)
   *
   * @return The startServer property
   */
  public Property<Boolean> getStartServer() {
    return startServer;
  }

  /**
   * Specifies the TCP port for the embedded web server (defaults to 8888)
   *
   * @return The port property
   */
  public Property<Integer> getPort() {
    return port;
  }

  /**
   * Logs to a file in the given directory, as well as graphically
   *
   * @return The logdir property
   */
  public DirectoryProperty getLogdir() {
    return logdir;
  }

  /**
   * Specifies the bind address for the code server and web server
   * (defaults to 127.0.0.1)
   *
   * @return The bind address
   */
  public Property<String> getBindAddress() {
    return bindAddress;
  }

  /**
   * Specifies the TCP port for the code server (defaults to 9997 for
   * classic Dev Mode or 9876 for Super Dev Mode)
   *
   * @return The code server port
   */
  public Property<Integer> getCodeServerPort() {
    return codeServerPort;
  }

  /**
   * Runs Super Dev Mode instead of classic Development Mode. (defaults to ON)
   *
   * @return The superDevMode property
   */
  public Property<Boolean> getSuperDevMode() {
    return superDevMode;
  }

  /**
   * Specify a different embedded web server to run
   * (must implement ServletContainerLauncher)
   *
   * @return The server property
   */
  public Property<String> getServer() {
    return server;
  }

  /**
   * Automatically launches the specified URL
   *
   * @return The startup URL
   */
  public Property<String> getStartupUrl() {
    return startupUrl;
  }

  /**
   * The prefix to prepend to module path
   *
   * @return The module path prefix
   */
  public Property<String> getModulePathPrefix() {
    return modulePathPrefix;
  }

  @Override
  public void exec() {
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

    if (getModulePathPrefix().isPresent()) {
      args("-modulePathPrefix", getModulePathPrefix().get());
    }

    super.exec();
  }
}
