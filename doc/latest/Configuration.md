# Configuration

## Minimal configuration

The GWT Gradle Plugin requires minimal configuration to use. The following is
the minimal configuration required to use the plugin:

```groovy
plugins {
  id "org.docstr.gwt" version "2.2.7"
}

gwt {
  modules['<YOUR-GWT-MODULE>']
}
```

Once you have enabled the plugin, you only need to specify the GWT module you
want to compile. The plugin will take care of the rest.

## All configuration options

The GWT Gradle Plugin provides full control over the GWT compilation process and
the development mode. The configuration options are aligned with the GWT
compiler options for ease of use. The following is a list of all available
configuration options:

```
gwt {
  // Optional: Set the GWT version, defaults to 2.12.1
  // gwtVersion = 'HEAD-SNAPSHOT'

  // Optional: Minimum heap size for the JVM
  minHeapSize = '256M'
  // Optional: Maximum heap size for the JVM
  maxHeapSize = '1024M'

  // Optional: The level of logging detail: ERROR, WARN, INFO, TRACE, DEBUG, SPAM or ALL (defaults to INFO)
  logLevel = 'INFO'

  // Optional: The compiler's working directory for internal use (must be writeable; defaults to a system temp dir)
  workDir = file('build/work')

  // Optional: Debugging: causes normally-transient generated types to be saved in the specified directory
  gen = file('build/gen')

  // Optional: The directory into which deployable output files will be written (defaults to 'build/gwt')
  war = file('build/gwt')

  // Optional: The directory into which deployable but not servable output files will be written (defaults to 'build/deploy')
  deploy = file('build/deploy')

  // Optional: The directory into which extra files, not intended for deployment, will be written
  extra = file('build/extra')
  
  // "-Dgwt.persistentunitcachedir=[YourCacheDir]" - The directory to use for the persistent unit cache
  cacheDir = file('build/gwt-unitCache')

  // Optional: Additional source directories to include in GWT compilation and Java source sets. 
  // These directories are automatically added to both the classpath and compilation process.
  // Useful for multi-module projects, annotation processor outputs, or generated sources.
  // Example: extraSourceDirs = files("path/to/extra/sources", "another/source/dir")
  extraSourceDirs = files('src/generated/java')

  // Optional: Generate exports for JsInterop purposes. If no -includeJsInteropExport/-excludeJsInteropExport provided, generates all exports. (defaults to OFF)
  generateJsInteropExports = false

  // Optional: Include members and classes while generating JsInterop exports. Flag could be set multiple times to expand the pattern. (The flag has only effect if exporting is enabled via -generateJsInteropExports)
  includeJsInteropExports = ['com.example.MyClass']

  // Optional: Exclude members and classes while generating JsInterop exports. Flag could be set multiple times to expand the pattern. (The flag has only effect if exporting is enabled via -generateJsInteropExports)
  excludeJsInteropExports = ['com.example.MyOtherClass']

  // Optional: EXPERIMENTAL: Specifies method display name mode for chrome devtools: NONE, ONLY_METHOD_NAME, ABBREVIATED or FULL (defaults to NONE)
  methodNameDisplayMode = 'NONE'

  // Optional: Specifies Java source level (defaults to 1.8)
  sourceLevel = '1.8'

  // Optional: Compiles faster by reusing data from the previous compile. (defaults to OFF)
  incremental = false

  // Optional: Script output style: DETAILED, OBFUSCATED or PRETTY (defaults to OBFUSCATED)
  style = 'OBFUSCATED'

  // Optional: Fail compilation if any input file contains an error. (defaults to OFF)
  failOnError = false

  // Optional: Set the values of a property in the form of propertyName=value1[,value2...]. Example: -setProperties = ["user.agent=safari", "locale=default"] would add the parameters -setProperty user.agent=safari -setProperty locale=default
  setProperty = ['user.agent=safari', 'locale=default']

  // Required: Specifies the name(s) of the module(s) to host
  modules = ['<YOUR-GWT-MODULE>']
  
  // Optional: Configures the GWT compiler
  compiler {
    //
    // All options in 'gwt' closure (except 'gwtVersion') can be overridden here
    // Including extraSourceDirs - useful for compile-specific additional sources
    //
    
    // Optional: Enables Javascript output suitable for post-compilation by Closure Compiler (defaults to OFF)
    closureFormattedOutput = false

    // Optional: Compile a report that tells the "Story of Your Compile" (defaults to OFF)
    compileReport = false

    // Optional: Enables strict mode for GWT compiler (defaults to OFF)
    strict = false

    // Optional: Include metadata for some java.lang.Class methods (e.g. getName()) (defaults to ON)
    classMetadata = true

    // Optional: Compile quickly with minimal optimizations (defaults to OFF)
    draftCompile = false

    // Optional: Include assert statements in compiled output (defaults to OFF)
    checkAssertions = false

    // Optional: Limits the number of fragments using a code splitter that merges split points
    fragmentCount = 1

    // Optional: Puts most JavaScript globals into namespaces (defaults to PACKAGE for -draftCompile, otherwise NONE)
    namespace = 'NONE'

    // Optional: Sets the optimization level used by the compiler (0=none, 9=maximum)
    optimize = 9

    // Optional: Enables saving source code needed by debuggers (defaults to OFF)
    saveSource = false

    // Optional: Validate all source code, but do not compile (defaults to OFF)
    validateOnly = false

    // Optional: The number of local workers to use when compiling permutations
    localWorkers = 4

    // Optional: Overrides where source files useful to debuggers will be written (defaults to saved with extras)
    saveSourceOutput = file('build/saveSourceOutput')
  }
  
  // Optional: Configures the GWT development mode
  devMode {
    //
    // All options in 'gwt' closure (except 'gwtVersion') can be overridden here
    // Including extraSourceDirs - useful for dev mode specific additional sources
    //
    
    // Optional: Starts a servlet container serving the directory specified by the -war flag. (defaults to ON)
    startServer = true

    // Optional: Specifies the TCP port for the embedded web server (defaults to 8888)
    port = 8888

    // Optional: Logs to a file in the given directory, as well as graphically
    logdir = file('build/logs')

    // Optional: Specifies the bind address for the code server and web server (defaults to 127.0.0.1)
    bindAddress = '127.0.0.1'

    // Optional: Specifies the TCP port for the code server (defaults to 9997 for classic Dev Mode or 9876 for Super Dev Mode)
    codeServerPort = 9876

    // Optional: Runs Super Dev Mode instead of classic Development Mode. (defaults to ON)
    superDevMode = true

    // Optional: Specify a different embedded web server to run (must implement ServletContainerLauncher)
    server = 'com.example.MyCustomServer'

    // Optional: Automatically launches the specified URL
    startupUrl = 'http://localhost:8888/MyApp.html'

    // Optional: The subdirectory inside the war dir where DevMode will create module directories. (defaults empty for top level)
    modulePathPrefix = ''
  }
  
  superDev {
    //
    // All options in 'gwt' closure (except 'gwtVersion') can be overridden here
    // Including extraSourceDirs - useful for super dev mode specific additional sources
    //
    
    // Optional: Allows -src flags to reference missing directories. (defaults to OFF)
    allowMissingSrc = false
    
    // Optional: Exits after compiling the modules. The exit code will be 0 if the compile succeeded. (defaults to OFF)
    compileTest = false
    
    // Optional: The number of times to recompile (after the first one) during a compile test.
    compileTestRecompiles = false
    
    // Optional: Precompile modules. (defaults to ON)
    precompile = true
    
    // Optional: The port where the code server will run.
    port = 9876
    
    // Optional: A directory containing GWT source to be prepended to the classpath for compiling.
    src = file('src/someDir')
    
    // Optional: An output directory where files for launching Super Dev Mode will be written. (Optional.)
    launcherDir = file('build/superDev')
    
    // Optional: Specifies the bind address for the code server and web server (defaults to 127.0.0.1)
    bindAddress = '127.0.0.1'
    
    // Optional: EXPERIMENTAL: Enables Javascript output suitable for post-compilation by Closure Compiler (defaults to OFF)
    closureFormattedOutput = false
  }
  
  // Optional: Configures the GWT test runner
  gwtTest {
    //
    // All options in 'gwt' closure (except 'gwtVersion') can be overridden here
    //
	
	// Optional: To specify which test tasks should have GWT test enabled, default to empty list, which is interpreted to mean all tasks of type {@link org.gradle.api.tasks.testing.Test Test}
	testTasks = ["test", "gwtMockitoTest"]
    
    // Please check `GwtTestOptions` for more options
  }
}
