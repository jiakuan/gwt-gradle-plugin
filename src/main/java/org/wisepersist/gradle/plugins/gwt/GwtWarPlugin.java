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
package org.wisepersist.gradle.plugins.gwt;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.IConventionAware;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.plugins.WarPlugin;
import org.gradle.api.plugins.WarPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.War;

import java.io.File;
import java.util.concurrent.Callable;

public class GwtWarPlugin implements Plugin<Project> {

  public static final String TASK_WAR_TEMPLATE = "warTemplate";
  public static final String TASK_DRAFT_WAR = "draftWar";
  public static final String TASK_GWT_DEV = "gwtDev";
  public static final String TASK_GWT_SUPER_DEV = "gwtSuperDev";

  private static final Logger logger = Logging.getLogger(GwtWarPlugin.class);

  @Override
  public void apply(final Project project) {
    project.getPlugins().apply(WarPlugin.class);
    final GwtBasePlugin gwtBasePlugin = project.getPlugins().apply(
        GwtBasePlugin.class);

    final GwtPluginExtension extension = gwtBasePlugin.getExtension();

    final GwtCompile compileTask = (GwtCompile) project.getTasks()
        .getByName(GwtCompilerPlugin.TASK_COMPILE_GWT);

    final GwtDraftCompile draftCompileTask = (GwtDraftCompile) project
        .getTasks().getByName(GwtCompilerPlugin.TASK_DRAFT_COMPILE_GWT);

    final TaskProvider<War> warTaskProvider = project.getTasks().named(WarPlugin.WAR_TASK_NAME, War.class);

    logger.debug("Configuring war plugin with GWT settings");

    project.afterEvaluate(p -> warTaskProvider.configure(warTask -> {
      ConfigurableFileCollection files = project.files(compileTask.getWar()).builtBy(compileTask);

      String modulePathPrefix = extension.getModulePathPrefix();
      if (modulePathPrefix == null || modulePathPrefix.isEmpty()) {
        warTask.from(files);
        return;
      }

      warTask.into(modulePathPrefix, spec -> {
        spec.from(files);
        spec.exclude("WEB-INF");
      });
      warTask.into("", spec -> {
        spec.from(files);
        spec.include("WEB-INF");
      });
    }));

    project.getTasks().register(TASK_WAR_TEMPLATE, ExplodedWar.class, task -> {
      final WarPluginConvention warPluginConvention = (WarPluginConvention) project
              .getConvention().getPlugins().get("war");
      task.setGroup(GwtBasePlugin.GWT_TASK_GROUP);
      task.from((Callable<File>) warPluginConvention::getWebAppDir);
      task.dependsOn(
              (Callable<FileCollection>) () -> project.getConvention()
                      .getPlugin(JavaPluginConvention.class).getSourceSets().getByName(
                              SourceSet.MAIN_SOURCE_SET_NAME).getRuntimeClasspath());
      task.classpath(warTaskProvider.map(War::getClasspath));
      ((IConventionAware) task).getConventionMapping()
              .map("destinationDir", (Callable<File>) extension::getDevWar);
      task.setDescription(
              "Creates an exploded web application template to be used by GWT dev mode and eclipse to ensure src/main/webapp stays clean"
      );
    });

    project.getTasks().register(TASK_GWT_DEV, GwtDev.class, task -> {
      task.setDescription("Runs the GWT development mode");
      ((IConventionAware) task).getConventionMapping()
              .map("war", (Callable<File>) extension::getDevWar);
      task.dependsOn(project.getTasks().named(JavaPlugin.CLASSES_TASK_NAME));
      task.dependsOn(project.getTasks().named(TASK_WAR_TEMPLATE));
    });

    project.getTasks().register(TASK_DRAFT_WAR, War.class, draftWar -> {
      draftWar.from(draftCompileTask.getOutputs());

      String appendix = "draft";
      draftWar.getArchiveAppendix().convention(appendix);
      draftWar.getArchiveAppendix().set(appendix);
      draftWar.setDescription("Creates a war using the output of the task "
              + GwtCompilerPlugin.TASK_DRAFT_COMPILE_GWT);
    });
  }

}
