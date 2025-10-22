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
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.compile.JavaCompile;

/**
 * Task for compiling GWT modules.
 */
@CacheableTask
public abstract class GwtCompileTask extends AbstractBaseTask {

  /**
   * The main class for the GWT compiler.
   */
  public static final String COMPILER_CLASS = "com.google.gwt.dev.Compiler";

  /**
   * Constructs a new GwtCompileTask.
   */
  @Inject
  public GwtCompileTask() {

    // Set GWT compiler as the main class
    getMainClass().set(COMPILER_CLASS);

    // This task will depend on the compileJava and processResources tasks automatically
    dependsOn(getProject().getTasks().withType(JavaCompile.class)
        .matching(task ->
            !task.getName().toLowerCase().contains("test")));
    dependsOn(getProject().getTasks().named("processResources"));
  }

  /**
   * Include assert statements in compiled output
   *
   * @return The property
   */
  @Input
  @Optional
  public abstract Property<Boolean> getClosureFormattedOutput();

  /**
   * Compile a report that tells the "Story of Your Compile"
   *
   * @return The property
   */
  @Input
  @Optional
  public abstract Property<Boolean> getCompileReport();

  /**
   * Include metadata for some java.lang.Class methods
   *
   * @return The property
   */
  @Input
  @Optional
  public abstract Property<Boolean> getStrict();

  /**
   * Compile quickly with minimal optimizations
   *
   * @return The property
   */
  @Input
  @Optional
  public abstract Property<Boolean> getClassMetadata();

  /**
   * Include assert statements in compiled output
   *
   * @return The property
   */
  @Input
  @Optional
  public abstract Property<Boolean> getDraftCompile();

  /**
   * Include assert statements in compiled output
   *
   * @return The property
   */
  @Input
  @Optional
  public abstract Property<Boolean> getCheckAssertions();

  /**
   * The number of fragments into which the output JS should be split
   *
   * @return The property
   */
  @Input
  @Optional
  public abstract Property<Integer> getFragmentCount();

  /**
   * Puts most JavaScript globals into namespaces
   *
   * @return The property
   */
  @Input
  @Optional
  public abstract Property<String> getNamespace();

  /**
   * The optimization level used by the compiler
   *
   * @return The property
   */
  @Input
  @Optional
  public abstract Property<Integer> getOptimize();

  /**
   * Enables saving source code needed by debuggers
   *
   * @return The property
   */
  @Input
  @Optional
  public abstract Property<Boolean> getSaveSource();

  /**
   * Validate all source code, but do not compile
   *
   * @return The property
   */
  @Input
  @Optional
  public abstract Property<Boolean> getValidateOnly();

  /**
   * The number of local workers to use when compiling permutations
   *
   * @return The property
   */
  @Input
  @Optional
  public abstract Property<Integer> getLocalWorkers();

  /**
   * Overrides where source files useful to debuggers will be written
   *
   * @return The directory
   */
  @OutputDirectory
  @Optional
  public abstract DirectoryProperty getSaveSourceOutput();

  /**
   * Configure task-specific arguments during configuration phase.
   */
  public void configureCompileArgs() {
    if (getClosureFormattedOutput().isPresent()) {
      if (getClosureFormattedOutput().get()) {
        args("-XclosureFormattedOutput");
      } else {
        args("-XnoclosureFormattedOutput");
      }
    }

    if (getCompileReport().isPresent()) {
      if (getCompileReport().get()) {
        args("-compileReport");
      } else {
        args("-nocompileReport");
      }
    }

    if (getStrict().isPresent()) {
      if (getStrict().get()) {
        args("-strict");
      }
    }

    if (getClassMetadata().isPresent()) {
      if (getClassMetadata().get()) {
        args("-XclassMetadata");
      } else {
        args("-XnoclassMetadata");
      }
    }

    if (getDraftCompile().isPresent()) {
      if (getDraftCompile().get()) {
        args("-draftCompile");
      } else {
        args("-nodraftCompile");
      }
    }

    if (getCheckAssertions().isPresent()) {
      if (getCheckAssertions().get()) {
        args("-checkAssertions");
      } else {
        args("-nocheckAssertions");
      }
    }

    if (getFragmentCount().isPresent()) {
      args("-XfragmentCount", getFragmentCount().get());
    }

    if (getNamespace().isPresent()) {
      args("-Xnamespace", getNamespace().get());
    }

    if (getOptimize().isPresent()) {
      args("-optimize", getOptimize().get());
    }

    if (getSaveSource().isPresent()) {
      if (getSaveSource().get()) {
        args("-saveSource");
      } else {
        args("-nosaveSource");
      }
    }

    if (getValidateOnly().isPresent()) {
      if (getValidateOnly().get()) {
        args("-validateOnly");
      } else {
        args("-novalidateOnly");
      }
    }

    if (getLocalWorkers().isPresent()) {
      args("-localWorkers", getLocalWorkers().get());
    }

    if (getSaveSourceOutput().isPresent()) {
      args("-saveSourceOutput",
          getSaveSourceOutput().get().getAsFile().getPath());
    }
  }

  @Override
  public void exec() {
    getLogger()
        .info("inputs: {}", getInputs().getFiles().getAsPath());
    super.exec();
  }
}
