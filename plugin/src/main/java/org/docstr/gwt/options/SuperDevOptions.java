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
 * Super Dev options
 */
public abstract class SuperDevOptions extends AbstractBaseOptions {

  /**
   * <code>-[no]allowMissingSrc</code>
   * Allows -src flags to reference missing directories. (defaults to OFF)
   *
   * @return The allow missing src flag
   */
  public abstract Property<Boolean> getAllowMissingSrc();

  /**
   * <code>-[no]compileTest</code>
   * Exits after compiling the modules. The exit code will be 0 if the
   * compile succeeded. (defaults to OFF)
   *
   * @return The compile test flag
   */
  public abstract Property<Boolean> getCompileTest();

  /**
   * <code>-compileTestRecompiles</code>
   * The number of times to recompile (after the first one) during a compile test.
   *
   * @return The compile test recompiles
   */
  public abstract Property<Integer> getCompileTestRecompiles();

  /**
   * <code>-[no]precompile</code>
   * Precompile modules. (defaults to ON)
   *
   * @return The precompile flag
   */
  public abstract Property<Boolean> getPrecompile();

  /**
   * <code>-port</code>
   * The port where the code server will run.
   *
   * @return The code server port
   */
  public abstract Property<Integer> getPort();

  /**
   * <code>-src</code>
   * A directory containing GWT source to be prepended to the classpath for
   * compiling.
   *
   * @return The source directory
   */
  public abstract DirectoryProperty getSrc();

  /**
   * <code>-launcherDir</code>
   * An output directory where files for launching Super Dev Mode will
   * be written. (Optional.)
   *
   * @return The launcher directory
   */
  public abstract DirectoryProperty getLauncherDir();

  /**
   * <code>-bindAddress</code>
   * Specifies the bind address for the code server and web server
   * (defaults to 127.0.0.1)
   *
   * @return The bind address
   */
  public abstract Property<String> getBindAddress();

  /**
   * <code>-X[no]closureFormattedOutput</code>
   * EXPERIMENTAL: Enables Javascript output suitable for post-compilation
   * by Closure Compiler (defaults to OFF)
   *
   * @return The closure formatted output
   */
  public abstract Property<Boolean> getClosureFormattedOutput();
}
