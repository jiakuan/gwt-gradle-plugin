/**
 * Copyright (C) 2024 Document Node Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
import org.gradle.api.Project;

/**
 * Configures the GWT super dev task.
 */
public class GwtSuperDevConfig implements Action<GwtSuperDevTask> {

  private final GwtPluginExtension extension;

  /**
   * Constructor.
   *
   * @param extension The GWT plugin extension
   */
  public GwtSuperDevConfig(GwtPluginExtension extension) {
    this.extension = extension;
  }

  @Override
  public void execute(GwtSuperDevTask task) {
    Project project = task.getProject();
    if (extension.getSuperDev().getMinHeapSize().isPresent()) {
      task.setMinHeapSize(extension.getSuperDev().getMinHeapSize().get());
    } else {
      task.setMinHeapSize(extension.getMinHeapSize().getOrElse("256M"));
    }
    if (extension.getSuperDev().getMaxHeapSize().isPresent()) {
      task.setMaxHeapSize(extension.getSuperDev().getMaxHeapSize().get());
    } else {
      task.setMaxHeapSize(extension.getMaxHeapSize().getOrElse("512M"));
    }

    task.getAllowMissingSrc()
        .set(extension.getSuperDev().getAllowMissingSrc().getOrNull());
    task.getCompileTest()
        .set(extension.getSuperDev().getCompileTest().getOrNull());
    task.getCompileTestRecompiles()
        .set(extension.getSuperDev().getCompileTestRecompiles().getOrNull());

    if (extension.getSuperDev().getFailOnError().isPresent()) {
      task.getFailOnError().set(extension.getSuperDev().getFailOnError().get());
    } else {
      task.getFailOnError().set(extension.getFailOnError().getOrNull());
    }

    task.getPrecompile()
        .set(extension.getSuperDev().getPrecompile().getOrNull());

    task.getPort().set(extension.getSuperDev().getPort().getOrNull());
    task.getSrc().set(extension.getSuperDev().getSrc().getOrNull());

    if (extension.getSuperDev().getWorkDir().isPresent()) {
      task.getWorkDir().set(extension.getSuperDev().getWorkDir().get());
    } else {
      task.getWorkDir().set(extension.getWorkDir().getOrNull());
    }

    task.getLauncherDir()
        .set(extension.getSuperDev().getLauncherDir().getOrNull());
    task.getClosureFormattedOutput()
        .set(extension.getSuperDev().getClosureFormattedOutput().getOrNull());
    task.getBindAddress()
        .set(extension.getSuperDev().getBindAddress().getOrNull());

    if (extension.getSuperDev().getStyle().isPresent()) {
      task.getStyle().set(extension.getSuperDev().getStyle().get());
    } else {
      task.getStyle().set(extension.getStyle().getOrNull());
    }

    if (extension.getSuperDev().getLogLevel().isPresent()) {
      task.getLogLevel().set(extension.getSuperDev().getLogLevel().get());
    } else {
      task.getLogLevel().set(extension.getLogLevel().getOrNull());
    }
    if (extension.getSuperDev().getMethodNameDisplayMode().isPresent()) {
      task.getMethodNameDisplayMode()
          .set(extension.getSuperDev().getMethodNameDisplayMode().get());
    } else {
      task.getMethodNameDisplayMode()
          .set(extension.getMethodNameDisplayMode().getOrNull());
    }
    if (extension.getSuperDev().getSourceLevel().isPresent()) {
      task.getSourceLevel().set(extension.getSuperDev().getSourceLevel().get());
    } else {
      task.getSourceLevel().set(extension.getSourceLevel().getOrNull());
    }
    if (extension.getSuperDev().getGenerateJsInteropExports().isPresent()) {
      task.getGenerateJsInteropExports()
          .set(extension.getSuperDev().getGenerateJsInteropExports().get());
    } else {
      task.getGenerateJsInteropExports()
          .set(extension.getGenerateJsInteropExports().getOrNull());
    }
    if (extension.getSuperDev().getIncludeJsInteropExports().isPresent()
        && !extension.getSuperDev().getIncludeJsInteropExports().get()
        .isEmpty()) {
      task.getIncludeJsInteropExports()
          .set(extension.getSuperDev().getIncludeJsInteropExports().get());
    } else {
      task.getIncludeJsInteropExports()
          .set(extension.getIncludeJsInteropExports().getOrNull());
    }
    if (extension.getSuperDev().getExcludeJsInteropExports().isPresent()
        && !extension.getSuperDev().getExcludeJsInteropExports().get()
        .isEmpty()) {
      task.getExcludeJsInteropExports()
          .set(extension.getSuperDev().getExcludeJsInteropExports().get());
    } else {
      task.getExcludeJsInteropExports()
          .set(extension.getExcludeJsInteropExports().getOrNull());
    }
    if (extension.getSuperDev().getIncremental().isPresent()) {
      task.getIncremental().set(extension.getSuperDev().getIncremental().get());
    } else {
      task.getIncremental().set(extension.getIncremental().getOrNull());
    }
    if (extension.getSuperDev().getSetProperty().isPresent()
        && !extension.getSuperDev().getSetProperty().get().isEmpty()) {
      task.getSetProperty().set(extension.getSuperDev().getSetProperty().get());
    } else {
      task.getSetProperty().set(extension.getSetProperty().getOrNull());
    }
    if (extension.getSuperDev().getModules().isPresent()
        && !extension.getSuperDev().getModules().get().isEmpty()) {
      task.getModules().set(extension.getSuperDev().getModules().get());
    } else {
      task.getModules().set(extension.getModules().get());
    }

    // Set extra source directories if specified
    if (!extension.getSuperDev().getExtraSourceDirs().isEmpty()) {
      task.getExtraSourceDirs().from(extension.getSuperDev().getExtraSourceDirs());
    } else if (!extension.getExtraSourceDirs().isEmpty()) {
      task.getExtraSourceDirs().from(extension.getExtraSourceDirs());
    }

    // Check if the modules property is specified
    if (task.getModules().get().isEmpty()) {
      throw new GradleException(
          "gwtSuperDev failed: 'modules' property is required. Please specify at least one GWT module in the gwt { ... } block.");
    }

    // Configure classpath and arguments during configuration phase for Configuration Cache compatibility
    task.configureClasspath(project);
    task.configureArgs();
  }
}
