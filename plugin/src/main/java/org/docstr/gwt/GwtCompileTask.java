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
 * Task for compiling GWT modules.
 */
public abstract class GwtCompileTask extends JavaExec {

  @Input
  @Optional
  private final Property<String> logLevel;
  @OutputDirectory
  @Optional
  private final DirectoryProperty workDir;
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
  @OutputDirectory
  @Optional
  private final DirectoryProperty gen;
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
  private final Property<String> methodNameDisplayMode;
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
  private final ListProperty<String> setProperty;
  @Input
  @Optional
  private final Property<String> style;
  @Input
  @Optional
  private final Property<Boolean> failOnError;
  @Input
  @Optional
  private final Property<Boolean> validateOnly;
  @Input
  @Optional
  private final Property<String> sourceLevel;
  @Input
  @Optional
  private final Property<Integer> localWorkers;
  @Input
  @Optional
  private final Property<Boolean> incremental;
  @OutputDirectory
  @Optional
  private final DirectoryProperty war;
  @OutputDirectory
  @Optional
  private final DirectoryProperty deploy;
  @OutputDirectory
  @Optional
  private final DirectoryProperty extra;
  @OutputDirectory
  @Optional
  private final DirectoryProperty saveSourceOutput;
  @Input
  private final ListProperty<String> modules;

  /**
   * Constructs a new GwtCompileTask.
   * @param objects The object factory
   */
  @Inject
  public GwtCompileTask(ObjectFactory objects) {
    this.logLevel = objects.property(String.class);
    this.workDir = objects.directoryProperty();
    this.closureFormattedOutput = objects.property(Boolean.class);
    this.compileReport = objects.property(Boolean.class);
    this.strict = objects.property(Boolean.class);
    this.classMetadata = objects.property(Boolean.class);
    this.draftCompile = objects.property(Boolean.class);
    this.checkAssertions = objects.property(Boolean.class);
    this.fragmentCount = objects.property(Integer.class);
    this.gen = objects.directoryProperty();
    this.generateJsInteropExports = objects.property(Boolean.class);
    this.includeJsInteropExports = objects.listProperty(String.class);
    this.excludeJsInteropExports = objects.listProperty(String.class);
    this.methodNameDisplayMode = objects.property(String.class);
    this.namespace = objects.property(String.class);
    this.optimize = objects.property(Integer.class);
    this.saveSource = objects.property(Boolean.class);
    this.setProperty = objects.listProperty(String.class);
    this.style = objects.property(String.class);
    this.failOnError = objects.property(Boolean.class);
    this.validateOnly = objects.property(Boolean.class);
    this.sourceLevel = objects.property(String.class);
    this.localWorkers = objects.property(Integer.class);
    this.incremental = objects.property(Boolean.class);
    this.war = objects.directoryProperty();
    this.deploy = objects.directoryProperty();
    this.extra = objects.directoryProperty();
    this.saveSourceOutput = objects.directoryProperty();
    this.modules = objects.listProperty(String.class);

    // Set GWT compiler as the main class
    getMainClass().set("com.google.gwt.dev.Compiler");

    // This task will depend on the compileJava task automatically
    dependsOn(getProject().getTasks().withType(JavaCompile.class));
  }

  /**
   * The level of logging detail: ERROR, WARN, INFO, TRACE, DEBUG, SPAM
   * @return The log level
   */
  public Property<String> getLogLevel() {
    return logLevel;
  }

  /**
   * The compiler's working directory for internal use (must be writeable)
   * @return The working directory
   */
  public DirectoryProperty getWorkDir() {
    return workDir;
  }

  /**
   * Include assert statements in compiled output
   * @return The property
   */
  public Property<Boolean> getClosureFormattedOutput() {
    return closureFormattedOutput;
  }

  /**
   * Compile a report that tells the "Story of Your Compile"
   * @return The property
   */
  public Property<Boolean> getCompileReport() {
    return compileReport;
  }

  /**
   * Include metadata for some java.lang.Class methods
   * @return The property
   */
  public Property<Boolean> getStrict() {
    return strict;
  }

  /**
   * Compile quickly with minimal optimizations
   * @return The property
   */
  public Property<Boolean> getClassMetadata() {
    return classMetadata;
  }

  /**
   * Include assert statements in compiled output
   * @return The property
   */
  public Property<Boolean> getDraftCompile() {
    return draftCompile;
  }

  /**
   * Include assert statements in compiled output
   * @return The property
   */
  public Property<Boolean> getCheckAssertions() {
    return checkAssertions;
  }

  /**
   * The number of fragments into which the output JS should be split
   * @return The property
   */
  public Property<Integer> getFragmentCount() {
    return fragmentCount;
  }

  /**
   * The directory into which generated files will be written
   * @return The directory
   */
  public DirectoryProperty getGen() {
    return gen;
  }

  /**
   * Generate exports for JsInterop purposes
   * @return The property
   */
  public Property<Boolean> getGenerateJsInteropExports() {
    return generateJsInteropExports;
  }

  /**
   * Include members and classes while generating JsInterop exports
   * @return The property
   */
  public ListProperty<String> getIncludeJsInteropExports() {
    return includeJsInteropExports;
  }

  /**
   * Exclude members and classes while generating JsInterop exports
   * @return The property
   */
  public ListProperty<String> getExcludeJsInteropExports() {
    return excludeJsInteropExports;
  }

  /**
   * The display mode for method names in stack traces
   * @return The property
   */
  public Property<String> getMethodNameDisplayMode() {
    return methodNameDisplayMode;
  }

  /**
   * Puts most JavaScript globals into namespaces
   * @return The property
   */
  public Property<String> getNamespace() {
    return namespace;
  }

  /**
   * The optimization level used by the compiler
   * @return The property
   */
  public Property<Integer> getOptimize() {
    return optimize;
  }

  /**
   * Enables saving source code needed by debuggers
   * @return The property
   */
  public Property<Boolean> getSaveSource() {
    return saveSource;
  }

  /**
   * Sets a named property to the specified value
   * @return The property
   */
  public ListProperty<String> getSetProperty() {
    return setProperty;
  }

  /**
   * The script output style
   * @return The property
   */
  public Property<String> getStyle() {
    return style;
  }

  /**
   * Fail compilation if any input file contains an error
   * @return The property
   */
  public Property<Boolean> getFailOnError() {
    return failOnError;
  }

  /**
   * Validate all source code, but do not compile
   * @return The property
   */
  public Property<Boolean> getValidateOnly() {
    return validateOnly;
  }

  /**
   * Specifies Java source level
   * @return The property
   */
  public Property<String> getSourceLevel() {
    return sourceLevel;
  }

  /**
   * The number of local workers to use when compiling permutations
   * @return The property
   */
  public Property<Integer> getLocalWorkers() {
    return localWorkers;
  }

  /**
   * Compiles faster by reusing data from the previous compile
   * @return The property
   */
  public Property<Boolean> getIncremental() {
    return incremental;
  }

  /**
   * The directory into which deployable output files will be written
   * @return The directory
   */
  public DirectoryProperty getWar() {
    return war;
  }

  /**
   * The directory into which deployable output files will be written
   * @return The directory
   */
  public DirectoryProperty getDeploy() {
    return deploy;
  }

  /**
   * Extra arguments to be passed to the compiler
   * @return The directory
   */
  public DirectoryProperty getExtra() {
    return extra;
  }

  /**
   * Overrides where source files useful to debuggers will be written
   * @return The directory
   */
  public DirectoryProperty getSaveSourceOutput() {
    return saveSourceOutput;
  }

  /**
   * The modules to compile
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

    if (getLogLevel().isPresent()) {
      args("-logLevel", getLogLevel().get());
    }

    if (getWorkDir().isPresent()) {
      args("-workDir", getWorkDir().get().getAsFile().getPath());
    }

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

    if (getGen().isPresent()) {
      args("-gen", getGen().get().getAsFile().getPath());
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

    if (getMethodNameDisplayMode().isPresent()) {
      args("-XmethodNameDisplayMode", getMethodNameDisplayMode().get());
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

    if (getSetProperty().isPresent()) {
      getSetProperty().get()
          .forEach(property -> args("-setProperty", property));
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

    if (getValidateOnly().isPresent()) {
      if (getValidateOnly().get()) {
        args("-validateOnly");
      } else {
        args("-novalidateOnly");
      }
    }

    if (getSourceLevel().isPresent()) {
      args("-sourceLevel", getSourceLevel().get());
    }

    if (getLocalWorkers().isPresent()) {
      args("-localWorkers", getLocalWorkers().get());
    }

    if (getIncremental().isPresent()) {
      if (getIncremental().get()) {
        args("-incremental");
      } else {
        args("-noincremental");
      }
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

    if (getSaveSourceOutput().isPresent()) {
      args("-saveSourceOutput",
          getSaveSourceOutput().get().getAsFile().getPath());
    }

    getModules().get().forEach(module -> args(module));

    // Logging the command line arguments
    getProject().getLogger()
        .lifecycle("gwtCompile args: {}", getArgs());

    super.exec();
  }
}
