package de.richsource.gradle.plugins.gwt;

import java.io.File;
import java.util.concurrent.Callable;

import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;

import de.richsource.gradle.plugins.gwt.internal.GwtCompileOptionsImpl;


//-logLevel               The level of logging detail: ERROR, WARN, INFO, TRACE, DEBUG, SPAM, or ALL
//-workDir                The compiler's working directory for internal use (must be writeable; defaults to a system temp dir)
//-gen                    Debugging: causes normally-transient generated types to be saved in the specified directory
//-style                  Script output style: OBF[USCATED], PRETTY, or DETAILED (defaults to OBF)
//-ea                     Debugging: causes the compiled output to check assert statements
//-XdisableClassMetadata  EXPERIMENTAL: Disables some java.lang.Class methods (e.g. getName())
//-XdisableCastChecking   EXPERIMENTAL: Disables run-time checking of cast operations
//-validateOnly           Validate all source code, but do not compile
//-draftCompile           Enable faster, but less-optimized, compilations
//-optimize               Sets the optimization level used by the compiler.  0=none 9=maximum.
//-compileReport          Create a compile report that tells the Story of Your Compile
//-strict                 Only succeed if no input files have errors
//-localWorkers           The number of local workers to use when compiling permutations
//-war                    The directory into which deployable output files will be written (defaults to 'war')
//-deploy                 The directory into which deployable but not servable output files will be written (defaults to 'WEB-INF/deploy' under the -war directory/jar, and may be the same as the -extra directory/jar)
//-extra                  The directory into which extra files, not intended for deployment, will be written
public class AbstractGwtCompile extends AbstractGwtTask implements GwtCompileOptions {
	
	private final GwtCompileOptions options = new GwtCompileOptionsImpl();

	@Override
	protected String getClassName() {
		return "com.google.gwt.dev.Compiler";
	}
	
	@Override
	protected void addArgs() {
		super.addArgs();
		
		argIfSet("-localWorkers", getLocalWorkers());
		argIfEnabled(getDraftCompile(), "-draftCompile");
		argIfEnabled(getCompileReport(), "-compileReport");
		argIfEnabled(getCompilerMetrics(), "-XcompilerMetrics");

		argIfEnabled(getValidateOnly(), "-validateOnly");
		argIfEnabled(getDisableGeneratingOnShards(), "-XdisableGeneratingOnShards");
		
		argIfSet("-optimize", getOptimize());
		argIfEnabled(getDisableAggressiveOptimization(), "-XdisableAggressiveOptimization");
		argIfEnabled(getDisableClassMetadata(), "-XdisableClassMetadata");
		argIfEnabled(getDisableCastChecking(), "-XdisableCastChecking");
		argIfEnabled(getEa(), "-ea");
		argIfEnabled(getDisableRunAsync(), "-XdisableRunAsync");
		argIfSet("-style", getStyle());
		argIfEnabled(getSoycDetailed(), "-XsoycDetailed");
		argIfEnabled(getStrict(), "-strict");
		argIfEnabled(getDisableSoycHtml(), "-XdisableSoycHtml");
		argIfEnabled(getEnableClosureCompiler(), "-XenableClosureCompiler");
		argIfSet("-XfragmentCount", getFragmentCount());
	}
	
	protected void configure(final GwtCompileOptions options) {
		conventionMapping("localWorkers", new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return options.getLocalWorkers();
			}
		});
	}

	/** {@inheritDoc} */
	@Override
	@OutputDirectory
	public File getWar() {
		return super.getWar();
	}

	/** {@inheritDoc} */
	@Override
	public Integer getLocalWorkers() {
		return options.getLocalWorkers();
	}

	/** {@inheritDoc} */
	@Override
	public void setLocalWorkers(Integer localWorkers) {
		options.setLocalWorkers(localWorkers);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Boolean getDraftCompile() {
		return options.getDraftCompile();
	}

	/** {@inheritDoc} */
	@Override
	public void setDraftCompile(Boolean draftCompile) {
		options.setDraftCompile(draftCompile);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Boolean getCompileReport() {
		return options.getCompileReport();
	}

	/** {@inheritDoc} */
	@Override
	public void setCompileReport(Boolean compileReport) {
		options.setCompileReport(compileReport);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Boolean getCompilerMetrics() {
		return options.getCompilerMetrics();
	}

	/** {@inheritDoc} */
	@Override
	public void setCompilerMetrics(Boolean compilerMetrics) {
		options.setCompilerMetrics(compilerMetrics);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Boolean getValidateOnly() {
		return options.getValidateOnly();
	}

	/** {@inheritDoc} */
	@Override
	public void setValidateOnly(Boolean validateOnly) {
		options.setValidateOnly(validateOnly);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Boolean getDisableGeneratingOnShards() {
		return options.getDisableGeneratingOnShards();
	}

	/** {@inheritDoc} */
	@Override
	public void setDisableGeneratingOnShards(Boolean disableGeneratingOnShards) {
		options.setDisableGeneratingOnShards(disableGeneratingOnShards);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Integer getOptimize() {
		return options.getOptimize();
	}

	/** {@inheritDoc} */
	@Override
	public void setOptimize(Integer optimize) {
		options.setOptimize(optimize);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Boolean getDisableAggressiveOptimization() {
		return options.getDisableAggressiveOptimization();
	}

	/** {@inheritDoc} */
	@Override
	public void setDisableAggressiveOptimization(
			Boolean disableAggressiveOptimization) {
		options.setDisableAggressiveOptimization(disableAggressiveOptimization);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Boolean getDisableClassMetadata() {
		return options.getDisableClassMetadata();
	}

	/** {@inheritDoc} */
	@Override
	public void setDisableClassMetadata(Boolean disableClassMetadata) {
		options.setDisableClassMetadata(disableClassMetadata);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Boolean getDisableCastChecking() {
		return options.getDisableCastChecking();
	}

	/** {@inheritDoc} */
	@Override
	public void setDisableCastChecking(Boolean disableCastChecking) {
		options.setDisableCastChecking(disableCastChecking);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Boolean getEa() {
		return options.getEa();
	}

	/** {@inheritDoc} */
	@Override
	public void setEa(Boolean ea) {
		options.setEa(ea);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Boolean getDisableRunAsync() {
		return options.getDisableRunAsync();
	}

	/** {@inheritDoc} */
	@Override
	public void setDisableRunAsync(Boolean disableRunAsync) {
		options.setDisableRunAsync(disableRunAsync);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Style getStyle() {
		return options.getStyle();
	}

	/** {@inheritDoc} */
	@Override
	public void setStyle(Style style) {
		options.setStyle(style);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Boolean getSoycDetailed() {
		return options.getSoycDetailed();
	}

	/** {@inheritDoc} */
	@Override
	public void setSoycDetailed(Boolean soycDetailed) {
		options.setSoycDetailed(soycDetailed);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Boolean getStrict() {
		return options.getStrict();
	}

	/** {@inheritDoc} */
	@Override
	public void setStrict(Boolean strict) {
		options.setStrict(strict);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Boolean getDisableSoycHtml() {
		return options.getDisableSoycHtml();
	}

	/** {@inheritDoc} */
	@Override
	public void setDisableSoycHtml(Boolean disableSoycHtml) {
		options.setDisableSoycHtml(disableSoycHtml);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Boolean getEnableClosureCompiler() {
		return options.getEnableClosureCompiler();
	}

	/** {@inheritDoc} */
	@Override
	public void setEnableClosureCompiler(Boolean enableClosureCompiler) {
		options.setEnableClosureCompiler(enableClosureCompiler);
	}

	/** {@inheritDoc} */
	@Override
	@Input
	@Optional
	public Integer getFragmentCount() {
		return options.getFragmentCount();
	}

	/** {@inheritDoc} */
	@Override
	public void setFragmentCount(Integer fragmentCount) {
		options.setFragmentCount(fragmentCount);
	}
}
