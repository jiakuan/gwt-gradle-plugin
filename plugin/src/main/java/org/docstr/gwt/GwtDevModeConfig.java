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
 * Configures the GWT dev mode task.
 */
public class GwtDevModeConfig implements Action<GwtDevModeTask> {

  private final GwtPluginExtension extension;

  /**
   * Constructor.
   * @param extension The GWT plugin extension
   */
  public GwtDevModeConfig(GwtPluginExtension extension) {
    this.extension = extension;
  }

  @Override
  public void execute(GwtDevModeTask task) {
    task.getStartServer()
        .set(extension.getDevMode().getStartServer().getOrNull());
    task.getPort().set(extension.getDevMode().getPort().getOrNull());
    task.getLogdir().set(extension.getDevMode().getLogdir().getOrNull());
    if (extension.getDevMode().getLogLevel().isPresent()) {
      task.getLogLevel().set(extension.getDevMode().getLogLevel().get());
    } else {
      task.getLogLevel().set(extension.getLogLevel().getOrNull());
    }
    if (extension.getDevMode().getGen().isPresent()) {
      task.getGen().set(extension.getDevMode().getGen().get());
    } else {
      task.getGen().set(extension.getGen().getOrNull());
    }
    task.getBindAddress()
        .set(extension.getDevMode().getBindAddress().getOrNull());
    task.getCodeServerPort()
        .set(extension.getDevMode().getCodeServerPort().getOrNull());
    task.getSuperDevMode()
        .set(extension.getDevMode().getSuperDevMode().getOrNull());
    task.getServer().set(extension.getDevMode().getServer().getOrNull());
    task.getStartupUrl()
        .set(extension.getDevMode().getStartupUrl().getOrNull());
    if (extension.getDevMode().getWar().isPresent()) {
      task.getWar().set(extension.getDevMode().getWar().get());
    } else {
      task.getWar().set(extension.getWar().getOrNull());
    }
    if (extension.getDevMode().getDeploy().isPresent()) {
      task.getDeploy().set(extension.getDevMode().getDeploy().get());
    } else {
      task.getDeploy().set(extension.getDeploy().getOrNull());
    }
    if (extension.getDevMode().getExtra().isPresent()) {
      task.getExtra().set(extension.getDevMode().getExtra().get());
    } else {
      task.getExtra().set(extension.getExtra().getOrNull());
    }
    task.getModulePathPrefix()
        .set(extension.getDevMode().getModulePathPrefix().getOrNull());
    if (extension.getDevMode().getWorkDir().isPresent()) {
      task.getWorkDir().set(extension.getDevMode().getWorkDir().get());
    } else {
      task.getWorkDir().set(extension.getWorkDir().getOrNull());
    }
    if (extension.getDevMode().getMethodNameDisplayMode().isPresent()) {
      task.getMethodNameDisplayMode()
          .set(extension.getDevMode().getMethodNameDisplayMode().get());
    } else {
      task.getMethodNameDisplayMode()
          .set(extension.getMethodNameDisplayMode().getOrNull());
    }
    if (extension.getDevMode().getSourceLevel().isPresent()) {
      task.getSourceLevel().set(extension.getDevMode().getSourceLevel().get());
    } else {
      task.getSourceLevel().set(extension.getSourceLevel().getOrNull());
    }
    if (extension.getDevMode().getGenerateJsInteropExports().isPresent()) {
      task.getGenerateJsInteropExports()
          .set(extension.getDevMode().getGenerateJsInteropExports().get());
    } else {
      task.getGenerateJsInteropExports()
          .set(extension.getGenerateJsInteropExports().getOrNull());
    }
    if (extension.getDevMode().getIncludeJsInteropExports().isPresent()
        && !extension.getDevMode().getIncludeJsInteropExports().get()
        .isEmpty()) {
      task.getIncludeJsInteropExports()
          .set(extension.getDevMode().getIncludeJsInteropExports().get());
    } else {
      task.getIncludeJsInteropExports()
          .set(extension.getIncludeJsInteropExports().getOrNull());
    }
    if (extension.getDevMode().getExcludeJsInteropExports().isPresent()
        && !extension.getDevMode().getExcludeJsInteropExports().get()
        .isEmpty()) {
      task.getExcludeJsInteropExports()
          .set(extension.getDevMode().getExcludeJsInteropExports().get());
    } else {
      task.getExcludeJsInteropExports()
          .set(extension.getExcludeJsInteropExports().getOrNull());
    }
    if (extension.getDevMode().getIncremental().isPresent()) {
      task.getIncremental().set(extension.getDevMode().getIncremental().get());
    } else {
      task.getIncremental().set(extension.getIncremental().getOrNull());
    }
    if (extension.getDevMode().getStyle().isPresent()) {
      task.getStyle().set(extension.getDevMode().getStyle().get());
    } else {
      task.getStyle().set(extension.getStyle().getOrNull());
    }
    if (extension.getDevMode().getFailOnError().isPresent()) {
      task.getFailOnError().set(extension.getDevMode().getFailOnError().get());
    } else {
      task.getFailOnError().set(extension.getFailOnError().getOrNull());
    }
    if (extension.getDevMode().getSetProperty().isPresent()
        && !extension.getDevMode().getSetProperty().get().isEmpty()) {
      task.getSetProperty().set(extension.getDevMode().getSetProperty().get());
    } else {
      task.getSetProperty().set(extension.getSetProperty().getOrNull());
    }
    if (extension.getDevMode().getModules().isPresent()
        && !extension.getDevMode().getModules().get().isEmpty()) {
      task.getModules().set(extension.getDevMode().getModules().get());
    } else {
      task.getModules().set(extension.getModules().get());
    }

    // Check if the modules property is specified
    if (task.getModules().get().isEmpty()) {
      throw new GradleException(
          "gwtDevMode failed: 'modules' property is required. Please specify at least one GWT module in the gwt { ... } block.");
    }
  }
}
