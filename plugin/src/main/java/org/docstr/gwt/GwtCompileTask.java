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
 * Task for compiling GWT modules.
 */
public abstract class GwtCompileTask extends GwtBaseTask {

  @Input
  @Optional
  private final Property<Boolean> closureFormattedOutput;
  @Input
  @Optional
  private final Property<Boolean> compileReport;
  @Input
  @Optional
  private final Property<Boolean> strict;
  @Input
  @Optional
  private final Property<Boolean> classMetadata;
  @Input
  @Optional
  private final Property<Boolean> draftCompile;
  @Input
  @Optional
  private final Property<Boolean> checkAssertions;
  @Input
  @Optional
  private final Property<Integer> fragmentCount;
  @Input
  @Optional
  private final Property<String> namespace;
  @Input
  @Optional
  private final Property<Integer> optimize;
  @Input
  @Optional
  private final Property<Boolean> saveSource;
  @Input
  @Optional
  private final Property<Boolean> validateOnly;
  @Input
  @Optional
  private final Property<Integer> localWorkers;
  @OutputDirectory
  @Optional
  private final DirectoryProperty saveSourceOutput;

  /**
   * Constructs a new GwtCompileTask.
   *
   * @param objects The object factory
   */
  @Inject
  public GwtCompileTask(ObjectFactory objects) {
    super(objects);
    this.closureFormattedOutput = objects.property(Boolean.class);
    this.compileReport = objects.property(Boolean.class);
    this.strict = objects.property(Boolean.class);
    this.classMetadata = objects.property(Boolean.class);
    this.draftCompile = objects.property(Boolean.class);
    this.checkAssertions = objects.property(Boolean.class);
    this.fragmentCount = objects.property(Integer.class);
    this.namespace = objects.property(String.class);
    this.optimize = objects.property(Integer.class);
    this.saveSource = objects.property(Boolean.class);
    this.validateOnly = objects.property(Boolean.class);
    this.localWorkers = objects.property(Integer.class);
    this.saveSourceOutput = objects.directoryProperty();

    // Set GWT compiler as the main class
    getMainClass().set("com.google.gwt.dev.Compiler");

    // This task will depend on the compileJava task automatically
    dependsOn(getProject().getTasks().withType(JavaCompile.class));
  }

  /**
   * Include assert statements in compiled output
   *
   * @return The property
   */
  public Property<Boolean> getClosureFormattedOutput() {
    return closureFormattedOutput;
  }

  /**
   * Compile a report that tells the "Story of Your Compile"
   *
   * @return The property
   */
  public Property<Boolean> getCompileReport() {
    return compileReport;
  }

  /**
   * Include metadata for some java.lang.Class methods
   *
   * @return The property
   */
  public Property<Boolean> getStrict() {
    return strict;
  }

  /**
   * Compile quickly with minimal optimizations
   *
   * @return The property
   */
  public Property<Boolean> getClassMetadata() {
    return classMetadata;
  }

  /**
   * Include assert statements in compiled output
   *
   * @return The property
   */
  public Property<Boolean> getDraftCompile() {
    return draftCompile;
  }

  /**
   * Include assert statements in compiled output
   *
   * @return The property
   */
  public Property<Boolean> getCheckAssertions() {
    return checkAssertions;
  }

  /**
   * The number of fragments into which the output JS should be split
   *
   * @return The property
   */
  public Property<Integer> getFragmentCount() {
    return fragmentCount;
  }

  /**
   * Puts most JavaScript globals into namespaces
   *
   * @return The property
   */
  public Property<String> getNamespace() {
    return namespace;
  }

  /**
   * The optimization level used by the compiler
   *
   * @return The property
   */
  public Property<Integer> getOptimize() {
    return optimize;
  }

  /**
   * Enables saving source code needed by debuggers
   *
   * @return The property
   */
  public Property<Boolean> getSaveSource() {
    return saveSource;
  }

  /**
   * Validate all source code, but do not compile
   *
   * @return The property
   */
  public Property<Boolean> getValidateOnly() {
    return validateOnly;
  }

  /**
   * The number of local workers to use when compiling permutations
   *
   * @return The property
   */
  public Property<Integer> getLocalWorkers() {
    return localWorkers;
  }

  /**
   * Overrides where source files useful to debuggers will be written
   *
   * @return The directory
   */
  public DirectoryProperty getSaveSourceOutput() {
    return saveSourceOutput;
  }

  @Override
  public void exec() {
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

    getProject().getLogger()
        .lifecycle("inputs: {}", getInputs().getFiles().getFiles());
    super.exec();
  }
}
