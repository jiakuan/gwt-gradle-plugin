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

  @Input
  @Optional
  private final Property<Boolean> allowMissingSrc;
  @Input
  @Optional
  private final Property<Boolean> compileTest;
  @Input
  @Optional
  private final Property<Integer> compileTestRecompiles;
  @Input
  @Optional
  private final Property<Boolean> precompile;
  @Input
  @Optional
  private final Property<Integer> port;
  @InputDirectory
  @Optional
  private final DirectoryProperty src;
  @OutputDirectory
  @Optional
  private final DirectoryProperty launcherDir;
  @Input
  @Optional
  private final Property<String> bindAddress;
  @Input
  @Optional
  private final Property<Boolean> closureFormattedOutput;

  /**
   * Constructs a new GwtDevModeTask.
   *
   * @param objects The object factory
   */
  @Inject
  public GwtSuperDevTask(ObjectFactory objects) {
    super(objects);
    this.allowMissingSrc = objects.property(Boolean.class);
    this.compileTest = objects.property(Boolean.class);
    this.compileTestRecompiles = objects.property(Integer.class);
    this.precompile = objects.property(Boolean.class);
    this.port = objects.property(Integer.class);
    this.src = objects.directoryProperty();
    this.launcherDir = objects.directoryProperty();
    this.bindAddress = objects.property(String.class);
    this.closureFormattedOutput = objects.property(Boolean.class);

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
  public Property<Boolean> getAllowMissingSrc() {
    return allowMissingSrc;
  }

  /**
   * <code>-[no]compileTest</code>
   * Exits after compiling the modules. The exit code will be 0 if the
   * compile succeeded. (defaults to OFF)
   *
   * @return The compile test flag
   */
  public Property<Boolean> getCompileTest() {
    return compileTest;
  }

  /**
   * <code>-compileTestRecompiles</code>
   * The number of times to recompile (after the first one) during a compile test.
   *
   * @return The compile test recompiles
   */
  public Property<Integer> getCompileTestRecompiles() {
    return compileTestRecompiles;
  }

  /**
   * <code>-[no]precompile</code>
   * Precompile modules. (defaults to ON)
   *
   * @return The precompile flag
   */
  public Property<Boolean> getPrecompile() {
    return precompile;
  }

  /**
   * <code>-port</code>
   * The port where the code server will run.
   *
   * @return The code server port
   */
  public Property<Integer> getPort() {
    return port;
  }

  /**
   * <code>-src</code>
   * A directory containing GWT source to be prepended to the classpath for
   * compiling.
   *
   * @return The source directory
   */
  public DirectoryProperty getSrc() {
    return src;
  }

  /**
   * <code>-launcherDir</code>
   * An output directory where files for launching Super Dev Mode will
   * be written. (Optional.)
   *
   * @return The launcher directory
   */
  public DirectoryProperty getLauncherDir() {
    return launcherDir;
  }

  /**
   * <code>-bindAddress</code>
   * Specifies the bind address for the code server and web server
   * (defaults to 127.0.0.1)
   *
   * @return The bind address
   */
  public Property<String> getBindAddress() {
    return bindAddress;
  }

  /**
   * <code>-X[no]closureFormattedOutput</code>
   * EXPERIMENTAL: Enables Javascript output suitable for post-compilation
   * by Closure Compiler (defaults to OFF)
   *
   * @return The closure formatted output
   */
  public Property<Boolean> getClosureFormattedOutput() {
    return closureFormattedOutput;
  }

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
