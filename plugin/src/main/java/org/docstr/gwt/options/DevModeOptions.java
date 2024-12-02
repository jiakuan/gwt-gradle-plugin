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
package org.docstr.gwt.options;

import org.docstr.gwt.AbstractBaseOptions;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;

/**
 * Dev mode options
 */
public abstract class DevModeOptions extends AbstractBaseOptions {

  /**
   * <code>-[no]startServer</code>
   * Starts a servlet container serving the directory specified by
   * the -war flag. (defaults to ON)
   *
   * @return The start server flag
   */
  public abstract Property<Boolean> getStartServer();

  /**
   * <code>-port</code>
   * Specifies the TCP port for the embedded web server (defaults to 8888)
   *
   * @return The port
   */
  public abstract Property<Integer> getPort();

  /**
   * <code>-logdir</code>
   * Logs to a file in the given directory, as well as graphically
   *
   * @return The log directory
   */
  public abstract DirectoryProperty getLogdir();

  /**
   * <code>-bindAddress</code>
   * Specifies the bind address for the code server and web server
   * (defaults to 127.0.0.1)
   *
   * @return The bind address
   */
  public abstract Property<String> getBindAddress();

  /**
   * <code>-codeServerPort</code>
   * Specifies the TCP port for the code server (defaults to 9997 for
   * classic Dev Mode or 9876 for Super Dev Mode)
   *
   * @return The code server port
   */
  public abstract Property<Integer> getCodeServerPort();

  /**
   * <code>-[no]superDevMode</code>
   * Runs Super Dev Mode instead of classic Development Mode. (defaults to ON)
   *
   * @return The super dev mode
   */
  public abstract Property<Boolean> getSuperDevMode();

  /**
   * <code>-server</code>
   * Specify a different embedded web server to run
   * (must implement ServletContainerLauncher)
   *
   * @return The server
   */
  public abstract Property<String> getServer();

  /**
   * <code>-startupUrl</code>
   * Automatically launches the specified URL
   *
   * @return The startup URL
   */
  public abstract Property<String> getStartupUrl();

  /**
   * <code>-modulePathPrefix</code>
   * The subdirectory inside the war dir where DevMode will create
   * module directories. (defaults empty for top level)
   *
   * @return The module path prefix
   */
  public abstract Property<String> getModulePathPrefix();
}
