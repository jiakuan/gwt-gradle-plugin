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
import java.util.concurrent.Callable;
import org.gradle.api.internal.ConventionMapping;
import org.gradle.api.internal.IConventionAware;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.OutputDirectory;

/**
 * Task to run the GWT compiler for production quality output.
 */
@CacheableTask
public class GwtCompile extends AbstractGwtCompile {

  /** {@inheritDoc} */
  @Override
  @OutputDirectory
  public File getWar() {
    return super.getWar();
  }

  /** {@inheritDoc} */
  @Override
  protected boolean isDevTask() {
    return false;
  }

  @Override
  protected void configure(final GwtCompileOptions options) {
    super.configure(options);

    ConventionMapping conventionMapping = ((IConventionAware) this)
        .getConventionMapping();
    conventionMapping.map("draftCompile",
        (Callable<Boolean>) () -> options.getDraftCompile());
    conventionMapping.map("compileReport",
        (Callable<Boolean>) () -> options.getCompileReport());
    conventionMapping.map("compilerMetrics",
        (Callable<Boolean>) () -> options.getCompilerMetrics());
    conventionMapping.map("validateOnly",
        (Callable<Boolean>) () -> options.getValidateOnly());
    conventionMapping.map("disableGeneratingOnShards",
        (Callable<Boolean>) () -> options.getDisableGeneratingOnShards());
    conventionMapping.map("optimize",
        (Callable<Integer>) () -> options.getOptimize());
    conventionMapping.map("disableAggressiveOptimization",
        (Callable<Boolean>) () -> options.getDisableAggressiveOptimization());
    conventionMapping.map("disableClassMetadata",
        (Callable<Boolean>) () -> options.getDisableClassMetadata());
    conventionMapping.map("disableCastChecking",
        (Callable<Boolean>) () -> options.getDisableCastChecking());
    conventionMapping.map("ea", (Callable<Boolean>) () -> options.getEa());
    conventionMapping.map("disableRunAsync",
        (Callable<Boolean>) () -> options.getDisableRunAsync());
    conventionMapping.map("style", (Callable<Style>) () -> options.getStyle());
    conventionMapping.map("soycDetailed",
        (Callable<Boolean>) () -> options.getSoycDetailed());
    conventionMapping.map("strict",
        (Callable<Boolean>) () -> options.getStrict());
    conventionMapping.map("disableSoycHtml",
        (Callable<Boolean>) () -> options.getDisableSoycHtml());
    conventionMapping.map("fragmentCount",
        (Callable<Integer>) () -> options.getFragmentCount());
    conventionMapping.map("missingDepsFile",
        (Callable<File>) () -> options.getMissingDepsFile());
    conventionMapping.map("namespace",
        (Callable<Namespace>) () -> options.getNamespace());
    conventionMapping.map("enforceStrictResources",
        (Callable<Boolean>) () -> options.getEnforceStrictResources());
    conventionMapping.map("incrementalCompileWarnings",
        (Callable<Boolean>) () -> options.getIncrementalCompileWarnings());
    conventionMapping.map("overlappingSourceWarnings",
        (Callable<Boolean>) () -> options.getOverlappingSourceWarnings());
    conventionMapping.map("saveSource",
        (Callable<Boolean>) () -> options.getSaveSource());
    conventionMapping.map("saveSourceOutput",
        (Callable<File>) () -> options.getSaveSourceOutput());
    conventionMapping
        .map("closureFormattedOutput", options::getClosureFormattedOutput);
  }
}
