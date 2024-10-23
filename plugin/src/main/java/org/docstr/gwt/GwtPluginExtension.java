package org.docstr.gwt;

import org.gradle.api.Action;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Nested;

/**
 * GWT plugin extension
 */
public abstract class GwtPluginExtension extends GwtBaseOptions {

  /**
   * The GWT version to use
   * @return The GWT version
   */
  public abstract Property<String> getGwtVersion();

  /**
   * Nested extension for compiler options
   * @return The compiler options
   */
  @Nested
  public abstract CompilerOptions getCompiler();

  /**
   * Nested extension for dev mode options
   * @return The dev mode options
   */
  @Nested
  public abstract DevModeOptions getDevMode();

  /**
   * Configures the compiler options
   * @param action The action to configure the compiler options
   */
  public void compiler(Action<? super CompilerOptions> action) {
    action.execute(getCompiler());
  }

  /**
   * Configures the dev mode options
   * @param action The action to configure the dev mode options
   */
  public void devMode(Action<? super DevModeOptions> action) {
    action.execute(getDevMode());
  }

  /**
   * Nested class for Compiler options
   * https://www.gwtproject.org/doc/latest/DevGuideCompilingAndDebugging.html#DevGuideCompilerOptions
   * https://github.com/gwtproject/gwt/blob/main/dev/core/src/com/google/gwt/dev/PrecompileTaskOptionsImpl.java
   */
  public static abstract class CompilerOptions extends GwtBaseOptions {

    /**
     * <code>-X[no]closureFormattedOutput</code>
     * <p>EXPERIMENTAL: Enables Javascript output suitable for
     * post-compilation by Closure Compiler (defaults to OFF)
     * @return The closure formatted output
     */
    public abstract Property<Boolean> getClosureFormattedOutput();

    /**
     * <code>-[no]compileReport</code>
     * Compile a report that tells the "Story of Your Compile".
     * (defaults to OFF)
     * @return The compile report
     */
    public abstract Property<Boolean> getCompileReport();

    /**
     * <code>-strict</code>
     * <p>Enables strict mode for GWT compiler. (defaults to OFF)
     * @return The strict mode
     */
    public abstract Property<Boolean> getStrict();

    /**
     * <code>-X[no]classMetadata</code>
     * EXPERIMENTAL: Include metadata for some java.lang.Class methods
     * (e.g. getName()). (defaults to ON)
     * @return The class metadata
     */
    public abstract Property<Boolean> getClassMetadata();

    /**
     * <code>-[no]draftCompile</code>
     * Compile quickly with minimal optimizations. (defaults to OFF)
     * @return The draft compile
     */
    public abstract Property<Boolean> getDraftCompile();

    /**
     * <code>-[no]checkAssertions</code>
     * Include assert statements in compiled output. (defaults to OFF)
     * @return The check assertions
     */
    public abstract Property<Boolean> getCheckAssertions();

    /**
     * <code>-XfragmentCount</code>
     * EXPERIMENTAL: Limits of number of fragments using a code splitter
     * that merges split points.
     * @return The fragment count
     */
    public abstract Property<Integer> getFragmentCount();

    /**
     * <code>-Xnamespace</code>
     * Puts most JavaScript globals into namespaces. Default: PACKAGE
     * for -draftCompile, otherwise NONE
     * @return The namespace
     */
    public abstract Property<String> getNamespace();

    /**
     * <code>-optimize</code>
     * Sets the optimization level used by the compiler.  0=none 9=maximum.
     * @return The optimization level
     */
    public abstract Property<Integer> getOptimize();

    /**
     * <code>-[no]saveSource</code>
     * Enables saving source code needed by debuggers. Also see -debugDir.
     * (defaults to OFF)
     * @return The save source
     */
    public abstract Property<Boolean> getSaveSource();

    /**
     * <code>-[no]validateOnly</code>
     * Validate all source code, but do not compile. (defaults to OFF)
     * @return The validate only
     */
    public abstract Property<Boolean> getValidateOnly();

    /**
     * <code>-localWorkers</code>
     * The number of local workers to use when compiling permutations
     * @return The local workers
     */
    public abstract Property<Integer> getLocalWorkers();

    /**
     * <code>-saveSourceOutput</code>
     * Overrides where source files useful to debuggers will be written.
     * Default: saved with extras.
     * @return The save source output
     */
    public abstract DirectoryProperty getSaveSourceOutput();
  }

  /**
   * Nested class for Dev Mode options
   */
  public static abstract class DevModeOptions extends GwtBaseOptions {

    /**
     * <code>-[no]startServer</code>
     * Starts a servlet container serving the directory specified by
     * the -war flag. (defaults to ON)
     * @return The start server flag
     */
    public abstract Property<Boolean> getStartServer();

    /**
     * <code>-port</code>
     * Specifies the TCP port for the embedded web server (defaults to 8888)
     * @return The port
     */
    public abstract Property<Integer> getPort();

    /**
     * <code>-logdir</code>
     * Logs to a file in the given directory, as well as graphically
     * @return The log directory
     */
    public abstract DirectoryProperty getLogdir();

    /**
     * <code>-bindAddress</code>
     * Specifies the bind address for the code server and web server
     * (defaults to 127.0.0.1)
     * @return The bind address
     */
    public abstract Property<String> getBindAddress();

    /**
     * <code>-codeServerPort</code>
     * Specifies the TCP port for the code server (defaults to 9997 for
     * classic Dev Mode or 9876 for Super Dev Mode)
     * @return The code server port
     */
    public abstract Property<Integer> getCodeServerPort();

    /**
     * <code>-[no]superDevMode</code>
     * Runs Super Dev Mode instead of classic Development Mode. (defaults to ON)
     * @return The super dev mode
     */
    public abstract Property<Boolean> getSuperDevMode();

    /**
     * <code>-server</code>
     * Specify a different embedded web server to run
     * (must implement ServletContainerLauncher)
     * @return The server
     */
    public abstract Property<String> getServer();

    /**
     * <code>-startupUrl</code>
     * Automatically launches the specified URL
     * @return The startup URL
     */
    public abstract Property<String> getStartupUrl();

    /**
     * <code>-modulePathPrefix</code>
     * The subdirectory inside the war dir where DevMode will create
     * module directories. (defaults empty for top level)
     * @return The module path prefix
     */
    public abstract Property<String> getModulePathPrefix();
  }
}
