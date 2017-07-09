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

import java.io.File;


/**
 * Defines the options known by the {@link GwtCompile} and {@link GwtDraftCompile} tasks.
 */
public interface GwtCompileOptions {

	Integer getLocalWorkers();

	/**
	 * Sets the "-localWorkers" option.
	 * 
	 * @param localWorkers Whether to add the "-localWorkers" option.
	 */
	void setLocalWorkers(Integer localWorkers);

	Boolean getDraftCompile();

	/**
	 * If set to true, this adds the "-draftCompile" flag.
	 * 
	 * @param draftCompile Whether to add the "-draftCompile" flag.
	 */
	void setDraftCompile(Boolean draftCompile);

	Boolean getCompileReport();

	/**
	 * If set to true, this adds the "-compileReport" flag.
	 * 
	 * @param compileReport Whether to add the "-compileReport" flag.
	 */
	void setCompileReport(Boolean compileReport);

	Boolean getCompilerMetrics();

	/**
	 * If set to true, this adds the "-XcompilerMetrics" flag.
	 * 
	 * @param compilerMetrics Whether to add the "-XcompilerMetrics" flag.
	 */
	void setCompilerMetrics(Boolean compilerMetrics);

	Boolean getValidateOnly();

	/**
	 * If set to true, this adds the "-validateOnly" flag.
	 * 
	 * @param validateOnly Whether to add the "-validateOnly" flag.
	 */
	void setValidateOnly(Boolean validateOnly);

	Boolean getDisableGeneratingOnShards();

	/**
	 * If set to true, this adds the "-XdisableGeneratingOnShards" flag.
	 * 
	 * @param disableGeneratingOnShards Whether to add the "-XdisableGeneratingOnShards" flag.
	 */
	void setDisableGeneratingOnShards(Boolean disableGeneratingOnShards);

	Integer getOptimize();

	/**
	 * Sets the "-optimize" option.
	 * 
	 * @param optimize the optimization level to set. Valid values are in the interval [0, 9].
	 */
	void setOptimize(Integer optimize);

	Boolean getDisableAggressiveOptimization();

	/**
	 * If set to true, this adds the "-XdisableAggressiveOptimization" flag.
	 * 
	 * @param disableAggressiveOptimization Whether to add the "-XdisableAggressiveOptimization" flag.
	 */
	void setDisableAggressiveOptimization(Boolean disableAggressiveOptimization);

	Boolean getDisableClassMetadata();

	/**
	 * If set to true, this adds the "-XdisableClassMetadata" flag.
	 * 
	 * @param disableClassMetadata Whether to add the "-XdisableClassMetadata" flag.
	 */
	void setDisableClassMetadata(Boolean disableClassMetadata);

	Boolean getDisableCastChecking();

	/**
	 * If set to true, this adds the "-XdisableCastChecking" flag.
	 * 
	 * @param disableCastChecking Whether to add the "-XdisableCastChecking" flag.
	 */
	void setDisableCastChecking(Boolean disableCastChecking);

	Boolean getEa();

	/**
	 * If set to true, this adds the "-ea" (enable assertions) flag.
	 * 
	 * @param ea Whether to add the "-ea" (enable assertions) flag.
	 */
	void setEa(Boolean ea);

	Boolean getDisableRunAsync();

	/**
	 * If set to true, this adds the "-XdisableRunAsync" flag.
	 * 
	 * @param disableRunAsync Whether to add the "-XdisableRunAsync" flag.
	 */
	void setDisableRunAsync(Boolean disableRunAsync);

	Style getStyle();

	/**
	 * Sets the "-style" option.
	 * 
	 * @param style Whether to add the "-style" option.
	 */
	void setStyle(Style style);

	Boolean getSoycDetailed();

	/**
	 * If set to true, this adds the "-XsoycDetailed" flag.
	 * 
	 * @param soycDetailed Whether to addthe "-XsoycDetailed" flag.
	 */
	void setSoycDetailed(Boolean soycDetailed);

	Boolean getStrict();

	/**
	 * If set to true, this adds the "-strict" flag.
	 * 
	 * @param strict Whether to add the "-strict" flag.
	 */
	void setStrict(Boolean strict);

	Boolean getDisableSoycHtml();

	/**
	 * If set to true, this adds the "-XdisableSoycHtml" flag.
	 * 
	 * @param disableSoycHtml Whether to add the "-XdisableSoycHtml" flag.
	 */
	void setDisableSoycHtml(Boolean disableSoycHtml);

	Integer getFragmentCount();

	/**
	 * Sets the "-XfragmentCount" option.
	 * 
	 * @param fragmentCount The "-XfragmentCount" option specified.
	 */
	void setFragmentCount(Integer fragmentCount);

	File getMissingDepsFile();

	/**
	 * @param missingDepsFile the missingDepsFile to set
	 */
	void setMissingDepsFile(File missingDepsFile);

	Namespace getNamespace();

	/**
	 * @param namespace the namespace to set
	 */
	void setNamespace(Namespace namespace);

	Boolean getEnforceStrictResources();

	/**
	 * @param enforceStrictResources the enforceStrictResources to set
	 */
	void setEnforceStrictResources(Boolean enforceStrictResources);

	Boolean getIncrementalCompileWarnings();

	/**
	 * @param incrementalCompileWarnings the incrementalCompileWarnings to set
	 */
	void setIncrementalCompileWarnings(Boolean incrementalCompileWarnings);

	Boolean getOverlappingSourceWarnings();

	/**
	 * @param overlappingSourceWarnings the overlappingSourceWarnings to set
	 */
	void setOverlappingSourceWarnings(Boolean overlappingSourceWarnings);

	Boolean getSaveSource();

	/**
	 * @param saveSource the saveSource to set
	 */
	void setSaveSource(Boolean saveSource);

	File getSaveSourceOutput();

	/**
	 * @param saveSourceOutput the saveSourceOutput to set
	 */
	void setSaveSourceOutput(File saveSourceOutput);

	Boolean getClosureFormattedOutput();

	/**
	 * If set to true, this adds the parameter -XclosureFormattedOutput.
	 * If set to false, this adds the parameter -XnoclosureFormattedOutput.
	 * Added in GWT 2.8.
	 *
	 * @param closureFormattedOutput The closure formatted output.
	 */
	void setClosureFormattedOutput(Boolean closureFormattedOutput);
}