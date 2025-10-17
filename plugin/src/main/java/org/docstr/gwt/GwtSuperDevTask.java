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
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.compile.JavaCompile;

/**
 * Task for running the GWT super dev.
 * <a href="https://www.gwtproject.org/articles/superdevmode.html">
 * https://www.gwtproject.org/articles/superdevmode.html
 * </a>
 */
public abstract class GwtSuperDevTask extends AbstractBaseTask {

  /**
   * The main class for the GWT super dev.
   */
  public static final String CODE_SERVER_CLASS = "com.google.gwt.dev.codeserver.CodeServer";

  /**
   * Constructs a new GwtDevModeTask.
   */
  public GwtSuperDevTask() {

    // Set GWT compiler as the main class
    getMainClass().set(CODE_SERVER_CLASS);

    // This task will depend on the compileJava task automatically
    dependsOn(getProject().getTasks().withType(JavaCompile.class));
  }

  /**
   * <code>-[no]allowMissingSrc</code>
   * Allows -src flags to reference missing directories. (defaults to OFF)
   *
   * @return The allow missing src flag
   */
  @Input
  @Optional
  public abstract Property<Boolean> getAllowMissingSrc();

  /**
   * <code>-[no]compileTest</code>
   * Exits after compiling the modules. The exit code will be 0 if the
   * compile succeeded. (defaults to OFF)
   *
   * @return The compile test flag
   */
  @Input
  @Optional
  public abstract Property<Boolean> getCompileTest();

  /**
   * <code>-compileTestRecompiles</code>
   * The number of times to recompile (after the first one) during a compile test.
   *
   * @return The compile test recompiles
   */
  @Input
  @Optional
  public abstract Property<Integer> getCompileTestRecompiles();

  /**
   * <code>-[no]precompile</code>
   * Precompile modules. (defaults to ON)
   *
   * @return The precompile flag
   */
  @Input
  @Optional
  public abstract Property<Boolean> getPrecompile();

  /**
   * <code>-port</code>
   * The port where the code server will run.
   *
   * @return The code server port
   */
  @Input
  @Optional
  public abstract Property<Integer> getPort();

  /**
   * <code>-src</code>
   * A directory containing GWT source to be prepended to the classpath for
   * compiling.
   *
   * @return The source directory
   */
  @InputDirectory
  @Optional
  public abstract DirectoryProperty getSrc();

  /**
   * <code>-launcherDir</code>
   * An output directory where files for launching Super Dev Mode will
   * be written. (Optional.)
   *
   * @return The launcher directory
   */
  @OutputDirectory
  @Optional
  public abstract DirectoryProperty getLauncherDir();

  /**
   * <code>-bindAddress</code>
   * Specifies the bind address for the code server and web server
   * (defaults to 127.0.0.1)
   *
   * @return The bind address
   */
  @Input
  @Optional
  public abstract Property<String> getBindAddress();

  /**
   * <code>-X[no]closureFormattedOutput</code>
   * EXPERIMENTAL: Enables Javascript output suitable for post-compilation
   * by Closure Compiler (defaults to OFF)
   *
   * @return The closure formatted output
   */
  @Input
  @Optional
  public abstract Property<Boolean> getClosureFormattedOutput();

  @Override
  public void exec() {
    if (getAllowMissingSrc().isPresent()) {
      if (getAllowMissingSrc().get()) {
        args("-allowMissingSrc");
      } else {
        args("-noallowMissingSrc");
      }
    }

    if (getCompileTest().isPresent()) {
      if (getCompileTest().get()) {
        args("-compileTest");
      } else {
        args("-nocompileTest");
      }
    }

    if (getCompileTestRecompiles().isPresent()) {
      args("-compileTestRecompiles", getCompileTestRecompiles().get());
    }

    if (getPrecompile().isPresent()) {
      if (getPrecompile().get()) {
        args("-precompile");
      } else {
        args("-noprecompile");
      }
    }

    if (getPort().isPresent()) {
      args("-port", getPort().get());
    }

    if (getSrc().isPresent()) {
      args("-src", getSrc().get().getAsFile().getPath());
    }

    if (getLauncherDir().isPresent()) {
      args("-launcherDir", getLauncherDir().get().getAsFile().getPath());
    }

    if (getBindAddress().isPresent()) {
      args("-bindAddress", getBindAddress().get());
    }

    if (getClosureFormattedOutput().isPresent()) {
      if (getClosureFormattedOutput().get()) {
        args("-XclosureFormattedOutput");
      } else {
        args("-XnoclosureFormattedOutput");
      }
    }

    super.exec();
  }
}
