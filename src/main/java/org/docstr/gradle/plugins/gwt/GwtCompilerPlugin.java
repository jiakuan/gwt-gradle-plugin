/**
 * Copyright (C) 2013-2017 Steffen Schaefer
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
package org.docstr.gradle.plugins.gwt;

import java.io.File;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaBasePlugin;
import org.gradle.api.plugins.JavaPlugin;

public class GwtCompilerPlugin implements Plugin<Project> {

  public static final String OUT_DIR = "out";
  public static final String DRAFT_OUT_DIR = "draftOut";

  public static final String TASK_COMPILE_GWT = "compileGwt";
  public static final String TASK_DRAFT_COMPILE_GWT = "draftCompileGwt";
  public static final String TASK_CHECK = "checkGwt";

  @Override
  public void apply(final Project project) {
    project.getPlugins().apply(GwtBasePlugin.class);

    final File gwtBuildDir = new File(project.getBuildDir(),
        GwtBasePlugin.BUILD_DIR);

    project.getTasks().register(TASK_COMPILE_GWT, GwtCompile.class, task -> {
      task.setWar(new File(gwtBuildDir, OUT_DIR));
      task.setDescription("Runs the GWT compiler to translate Java sources to JavaScript for production ready output");
      task.dependsOn(project.getTasks().named(JavaPlugin.COMPILE_JAVA_TASK_NAME),
              project.getTasks().named(JavaPlugin.PROCESS_RESOURCES_TASK_NAME));
    });

    project.getTasks().register(TASK_DRAFT_COMPILE_GWT, GwtDraftCompile.class, task -> {
      task.setWar(new File(gwtBuildDir, DRAFT_OUT_DIR));
      task.setDescription("Runs the GWT compiler to produce draft quality output used for development");
      task.dependsOn(project.getTasks().named(JavaPlugin.COMPILE_JAVA_TASK_NAME),
              project.getTasks().named(JavaPlugin.PROCESS_RESOURCES_TASK_NAME));
    });

    project.getTasks().register(TASK_CHECK, task -> {
      task.setDescription("Runs the GWT compiler to validate the relevant sources");
      task.dependsOn(project.getTasks().named(JavaPlugin.COMPILE_JAVA_TASK_NAME),
              project.getTasks().named(JavaPlugin.PROCESS_RESOURCES_TASK_NAME));
      task.setGroup(JavaBasePlugin.VERIFICATION_GROUP);
    });
  }
}
