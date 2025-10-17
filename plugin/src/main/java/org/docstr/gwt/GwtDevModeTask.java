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

import org.gradle.api.file.DirectoryProperty;
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

  /**
   * Constructs a new GwtDevModeTask.
   *
   */
  public GwtDevModeTask() {

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
  @Input
  @Optional
  public abstract Property<Boolean> getStartServer();

  /**
   * Specifies the TCP port for the embedded web server (defaults to 8888)
   *
   * @return The port property
   */
  @Input
  @Optional
  public abstract Property<Integer> getPort();

  /**
   * Logs to a file in the given directory, as well as graphically
   *
   * @return The logdir property
   */
  @OutputDirectory
  @Optional
  public abstract DirectoryProperty getLogdir();

  /**
   * Specifies the bind address for the code server and web server
   * (defaults to 127.0.0.1)
   *
   * @return The bind address
   */
  @Input
  @Optional
  public abstract Property<String> getBindAddress();

  /**
   * Specifies the TCP port for the code server (defaults to 9997 for
   * classic Dev Mode or 9876 for Super Dev Mode)
   *
   * @return The code server port
   */
  @Input
  @Optional
  public abstract Property<Integer> getCodeServerPort();

  /**
   * Runs Super Dev Mode instead of classic Development Mode. (defaults to ON)
   *
   * @return The superDevMode property
   */
  @Input
  @Optional
  public abstract Property<Boolean> getSuperDevMode();

  /**
   * Specify a different embedded web server to run
   * (must implement ServletContainerLauncher)
   *
   * @return The server property
   */
  @Input
  @Optional
  public abstract Property<String> getServer();

  /**
   * Automatically launches the specified URL
   *
   * @return The startup URL
   */
  @Input
  @Optional
  public abstract Property<String> getStartupUrl();

  /**
   * The prefix to prepend to module path
   *
   * @return The module path prefix
   */
  @Input
  @Optional
  public abstract Property<String> getModulePathPrefix();

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
