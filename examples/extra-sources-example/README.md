# Extra Sources Example

This example demonstrates the `extraSourceDirs` feature of the GWT Gradle Plugin, which allows you to include additional source directories in your GWT compilation.

## Overview

The `extraSourceDirs` property is useful in several scenarios:

- **Multi-module projects**: Include sources from other Gradle modules
- **Generated sources**: Include output from annotation processors or code generators
- **Shared code**: Include common code that lives outside the main source tree
- **Legacy code**: Include sources from non-standard directory structures

## Project Structure

```
extra-sources-example/
├── build.gradle              # Main build configuration with extraSourceDirs
├── extra-src/                 # Additional source directory
│   └── com/example/
│       ├── Extra.gwt.xml     # GWT module for extra sources
│       └── extra/
│           └── ExtraSourceClass.java
└── src/main/java/
    └── com/example/
        ├── MyModule.gwt.xml  # Main GWT module
        ├── client/
        │   └── MyEntryPoint.java
        └── shared/
            └── SharedClass.java
```

## Configuration

The key configuration in `build.gradle`:

```gradle
gwt {
    gwtVersion = '2.12.1'
    modules = ['com.example.MyModule']
    
    // Example of adding extra source directories
    // These will be automatically added to both GWT compilation and Java source sets
    // This could be sources from another module or generated sources
    extraSourceDirs = files("$projectDir/extra-src")
    
    compiler {
        style = 'PRETTY'
    }
}
```

## Key Features Demonstrated

### 1. Automatic Source Set Integration
When you specify `extraSourceDirs`, the plugin automatically:
- Adds the directories to the Java source sets
- Includes them in the GWT compilation classpath
- Ensures proper dependency tracking for incremental builds

### 2. Cross-Directory Module References
The example shows how code in the main source directory can reference and use classes from the extra source directory:

- `ExtraSourceClass` in `extra-src/` is available to the main module
- The GWT module can inherit from modules defined in extra source directories
- All sources are compiled together seamlessly

### 3. Multiple Source Directories
You can specify multiple extra source directories:

```gradle
extraSourceDirs = files(
    "$projectDir/extra-src",
    "$projectDir/generated-src",
    "../shared-module/src/main/java"
)
```

## Usage Scenarios

### Multi-Module Projects
```gradle
// In a multi-module setup
gwt {
    modules = ['com.example.MyApp']
    extraSourceDirs = files(
        project(':shared-core').file('src/main/java'),
        project(':shared-ui').file('src/main/java')
    )
}
```

### Generated Sources
```gradle
// Include annotation processor outputs
gwt {
    modules = ['com.example.MyApp']
    extraSourceDirs = files('build/generated/sources/annotationProcessor/java/main')
}
```

### Custom Source Generation
```gradle
task generateSources(type: Exec) {
    // Your source generation logic
    outputs.dir 'src/generated/java'
}

gwt {
    modules = ['com.example.MyApp']
    extraSourceDirs = files('src/generated/java')
}

// Ensure sources are generated before GWT compilation
tasks.gwtCompile.dependsOn(generateSources)
```

## Running the Example

1. **Compile the project:**
   ```bash
   ./gradlew gwtCompile
   ```

2. **Run in development mode:**
   ```bash
   ./gradlew gwtDevMode
   ```

3. **Build the complete project:**
   ```bash
   ./gradlew build
   ```

## Expected Output

When you run `gwtCompile`, you should see that:
- Sources from both `src/main/java` and `extra-src` are included in compilation
- The `ExtraSourceClass` is available to the main application
- The GWT compiler successfully processes all sources together

## Advanced Configuration

### Per-Mode Configuration
You can also configure different extra sources for different modes:

```gradle
gwt {
    modules = ['com.example.MyApp']
    extraSourceDirs = files('common-extra-src')
    
    compiler {
        extraSourceDirs = files('common-extra-src', 'compile-only-src')
    }
    
    devMode {
        extraSourceDirs = files('common-extra-src', 'dev-only-src')
    }
}
```

### With FileCollections
You can use Gradle's `FileCollection` for more complex scenarios:

```gradle
configurations {
    extraSources
}

dependencies {
    extraSources files('path1', 'path2')
    extraSources project(':other-module').sourceSets.main.java.srcDirs
}

gwt {
    modules = ['com.example.MyApp']
    extraSourceDirs = configurations.extraSources
}
```

This example demonstrates the flexibility and power of the `extraSourceDirs` feature for handling complex project structures and source management scenarios.