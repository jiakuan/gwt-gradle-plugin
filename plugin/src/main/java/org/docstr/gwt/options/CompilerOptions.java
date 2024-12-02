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
 * Compiler options
 * https://www.gwtproject.org/doc/latest/DevGuideCompilingAndDebugging.html#DevGuideCompilerOptions
 * https://github.com/gwtproject/gwt/blob/main/dev/core/src/com/google/gwt/dev/PrecompileTaskOptionsImpl.java
 */
public abstract class CompilerOptions extends AbstractBaseOptions {

  /**
   * <code>-X[no]closureFormattedOutput</code>
   * <p>EXPERIMENTAL: Enables Javascript output suitable for
   * post-compilation by Closure Compiler (defaults to OFF)
   *
   * @return The closure formatted output
   */
  public abstract Property<Boolean> getClosureFormattedOutput();

  /**
   * <code>-[no]compileReport</code>
   * Compile a report that tells the "Story of Your Compile".
   * (defaults to OFF)
   *
   * @return The compile report
   */
  public abstract Property<Boolean> getCompileReport();

  /**
   * <code>-strict</code>
   * <p>Enables strict mode for GWT compiler. (defaults to OFF)
   *
   * @return The strict mode
   */
  public abstract Property<Boolean> getStrict();

  /**
   * <code>-X[no]classMetadata</code>
   * EXPERIMENTAL: Include metadata for some java.lang.Class methods
   * (e.g. getName()). (defaults to ON)
   *
   * @return The class metadata
   */
  public abstract Property<Boolean> getClassMetadata();

  /**
   * <code>-[no]draftCompile</code>
   * Compile quickly with minimal optimizations. (defaults to OFF)
   *
   * @return The draft compile
   */
  public abstract Property<Boolean> getDraftCompile();

  /**
   * <code>-[no]checkAssertions</code>
   * Include assert statements in compiled output. (defaults to OFF)
   *
   * @return The check assertions
   */
  public abstract Property<Boolean> getCheckAssertions();

  /**
   * <code>-XfragmentCount</code>
   * EXPERIMENTAL: Limits of number of fragments using a code splitter
   * that merges split points.
   *
   * @return The fragment count
   */
  public abstract Property<Integer> getFragmentCount();

  /**
   * <code>-Xnamespace</code>
   * Puts most JavaScript globals into namespaces. Default: PACKAGE
   * for -draftCompile, otherwise NONE
   *
   * @return The namespace
   */
  public abstract Property<String> getNamespace();

  /**
   * <code>-optimize</code>
   * Sets the optimization level used by the compiler.  0=none 9=maximum.
   *
   * @return The optimization level
   */
  public abstract Property<Integer> getOptimize();

  /**
   * <code>-[no]saveSource</code>
   * Enables saving source code needed by debuggers. Also see -debugDir.
   * (defaults to OFF)
   *
   * @return The save source
   */
  public abstract Property<Boolean> getSaveSource();

  /**
   * <code>-[no]validateOnly</code>
   * Validate all source code, but do not compile. (defaults to OFF)
   *
   * @return The validate only
   */
  public abstract Property<Boolean> getValidateOnly();

  /**
   * <code>-localWorkers</code>
   * The number of local workers to use when compiling permutations
   *
   * @return The local workers
   */
  public abstract Property<Integer> getLocalWorkers();

  /**
   * <code>-saveSourceOutput</code>
   * Overrides where source files useful to debuggers will be written.
   * Default: saved with extras.
   *
   * @return The save source output
   */
  public abstract DirectoryProperty getSaveSourceOutput();
}
