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

import org.gradle.api.Action;
import org.gradle.api.GradleException;

/**
 * Configures the GWT compiler task.
 */
public class GwtCompileConfig implements Action<GwtCompileTask> {

  private final GwtPluginExtension extension;

  /**
   * Constructor.
   * @param extension The GWT plugin extension
   */
  public GwtCompileConfig(GwtPluginExtension extension) {
    this.extension = extension;
  }

  @Override
  public void execute(GwtCompileTask task) {
    if (extension.getCompiler().getMinHeapSize().isPresent()) {
      task.setMinHeapSize(extension.getCompiler().getMinHeapSize().get());
    } else {
      task.setMinHeapSize(extension.getMinHeapSize().getOrElse("256M"));
    }
    if (extension.getCompiler().getMaxHeapSize().isPresent()) {
      task.setMaxHeapSize(extension.getCompiler().getMaxHeapSize().get());
    } else {
      task.setMaxHeapSize(extension.getMaxHeapSize().getOrElse("512M"));
    }

    if (extension.getCompiler().getLogLevel().isPresent()) {
      task.getLogLevel().set(extension.getCompiler().getLogLevel().get());
    } else {
      task.getLogLevel().set(extension.getLogLevel().getOrNull());
    }
    if (extension.getCompiler().getWorkDir().isPresent()) {
      task.getWorkDir().set(extension.getCompiler().getWorkDir().get());
    } else {
      task.getWorkDir().set(extension.getWorkDir().getOrNull());
    }
    task.getClosureFormattedOutput()
        .set(extension.getCompiler().getClosureFormattedOutput());
    task.getCompileReport().set(extension.getCompiler().getCompileReport());
    task.getStrict().set(extension.getCompiler().getStrict());
    task.getClassMetadata().set(extension.getCompiler().getClassMetadata());
    task.getDraftCompile().set(extension.getCompiler().getDraftCompile());
    task.getCheckAssertions().set(extension.getCompiler().getCheckAssertions());
    task.getFragmentCount().set(extension.getCompiler().getFragmentCount());
    if (extension.getCompiler().getGen().isPresent()) {
      task.getGen().set(extension.getCompiler().getGen().get());
    } else {
      task.getGen().set(extension.getGen().getOrNull());
    }
    if (extension.getCompiler().getGenerateJsInteropExports().isPresent()) {
      task.getGenerateJsInteropExports()
          .set(extension.getCompiler().getGenerateJsInteropExports().get());
    } else {
      task.getGenerateJsInteropExports()
          .set(extension.getGenerateJsInteropExports().getOrNull());
    }
    if (extension.getCompiler().getIncludeJsInteropExports().isPresent()
        && !extension.getCompiler().getIncludeJsInteropExports().get()
        .isEmpty()) {
      task.getIncludeJsInteropExports()
          .set(extension.getCompiler().getIncludeJsInteropExports().get());
    } else {
      task.getIncludeJsInteropExports()
          .set(extension.getIncludeJsInteropExports().getOrNull());
    }
    if (extension.getCompiler().getExcludeJsInteropExports().isPresent()
        && !extension.getCompiler().getExcludeJsInteropExports().get()
        .isEmpty()) {
      task.getExcludeJsInteropExports()
          .set(extension.getCompiler().getExcludeJsInteropExports().get());
    } else {
      task.getExcludeJsInteropExports()
          .set(extension.getExcludeJsInteropExports().getOrNull());
    }
    if (extension.getCompiler().getMethodNameDisplayMode().isPresent()) {
      task.getMethodNameDisplayMode()
          .set(extension.getCompiler().getMethodNameDisplayMode().get());
    } else {
      task.getMethodNameDisplayMode()
          .set(extension.getMethodNameDisplayMode().getOrNull());
    }
    task.getNamespace().set(extension.getCompiler().getNamespace().getOrNull());
    task.getOptimize().set(extension.getCompiler().getOptimize().getOrNull());
    task.getSaveSource()
        .set(extension.getCompiler().getSaveSource().getOrNull());
    if (extension.getCompiler().getSetProperty().isPresent()
        && !extension.getCompiler().getSetProperty().get().isEmpty()) {
      task.getSetProperty().set(extension.getCompiler().getSetProperty().get());
    } else {
      task.getSetProperty().set(extension.getSetProperty().getOrNull());
    }
    if (extension.getCompiler().getStyle().isPresent()) {
      task.getStyle().set(extension.getCompiler().getStyle().get());
    } else {
      task.getStyle().set(extension.getStyle().getOrNull());
    }
    if (extension.getCompiler().getFailOnError().isPresent()) {
      task.getFailOnError().set(extension.getCompiler().getFailOnError().get());
    } else {
      task.getFailOnError().set(extension.getFailOnError().getOrNull());
    }
    task.getValidateOnly()
        .set(extension.getCompiler().getValidateOnly().getOrNull());
    if (extension.getCompiler().getSourceLevel().isPresent()) {
      task.getSourceLevel().set(extension.getCompiler().getSourceLevel().get());
    } else {
      task.getSourceLevel().set(extension.getSourceLevel().getOrNull());
    }
    task.getLocalWorkers()
        .set(extension.getCompiler().getLocalWorkers().getOrNull());
    if (extension.getCompiler().getIncremental().isPresent()) {
      task.getIncremental().set(extension.getCompiler().getIncremental().get());
    } else {
      task.getIncremental().set(extension.getIncremental().getOrNull());
    }
    if (extension.getCompiler().getWar().isPresent()) {
      task.getWar().set(extension.getCompiler().getWar().get());
    } else {
      task.getWar().set(extension.getWar().getOrNull());
    }
    if (extension.getCompiler().getDeploy().isPresent()) {
      task.getDeploy().set(extension.getCompiler().getDeploy().get());
    } else {
      task.getDeploy().set(extension.getDeploy().getOrNull());
    }
    if (extension.getCompiler().getExtra().isPresent()) {
      task.getExtra().set(extension.getCompiler().getExtra().get());
    } else {
      task.getExtra().set(extension.getExtra().getOrNull());
    }
    if (extension.getCompiler().getCacheDir().isPresent()) {
      task.getCacheDir().set(extension.getCompiler().getCacheDir().get());
    } else {
      task.getCacheDir().set(extension.getCacheDir().getOrNull());
    }
    task.getSaveSourceOutput()
        .set(extension.getCompiler().getSaveSourceOutput().getOrNull());
    if (extension.getCompiler().getModules().isPresent()
        && !extension.getCompiler().getModules().get().isEmpty()) {
      task.getModules().set(extension.getCompiler().getModules().get());
    } else {
      task.getModules().set(extension.getModules().get());
    }

    // Check if the modules property is specified
    if (task.getModules().get().isEmpty()) {
      throw new GradleException(
          "gwtCompile failed: 'modules' property is required. Please specify at least one GWT module in the gwt { ... } block.");
    }
  }
}
