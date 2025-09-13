# Quickstart

This guide will help you get started with the **latest version** of the **GWT
Gradle Plugin**. The plugin provides full control over the GWT compilation
process and the development mode. The configuration options are aligned with the
GWT compiler options for ease of use.

## Create a new project

Create a new Gradle project following the
official [Gradle documentation](https://docs.gradle.org/current/userguide/part1_gradle_init.html).

A typical structure would look like this:

```
my-gwt-project/
  ├── build.gradle
  └── src/
      └── main/
          └── java/
          └── resources/
```

## Add the GWT Gradle Plugin

Add the GWT Gradle Plugin to your `build.gradle` file:

```groovy
plugins {
  id "org.docstr.gwt" version "2.2.6"
}
```

## Configure the GWT module

The GWT Gradle Plugin requires minimal configuration to use. The following is
the minimal configuration required to use the plugin:

```groovy
gwt {
  // e.g. modules = ['com.example.MyModule']
  modules = ['<YOUR-GWT-MODULE>']
  
  // Optional: Add extra source directories for multi-module projects,
  // generated sources, or annotation processor outputs
  // extraSourceDirs = files("path/to/extra/sources")
}
```

Once you have enabled the plugin, you only need to specify the GWT module you
want to compile. The plugin will take care of the rest. 

### Advanced Configuration

For projects with additional source directories (such as multi-module setups or
generated sources), you can use the `extraSourceDirs` property:

```groovy
gwt {
  modules = ['com.example.MyModule']
  
  // Include additional source directories
  extraSourceDirs = files(
    "src/generated/java",
    "../shared-module/src/main/java"
  )
}
```

For more configuration options, refer to the [Configuration](Configuration.md) documentation.

## Build the project

To build the project, run:

```shell
gradle build
```

The task `gwtCompile` will be executed automatically when you run
`gradle build`, which compiles your GWT modules. The compiled output will be
included in your `build/gwt` directory by default. You can change the output
directory by configuring the `war` option in the `gwt` block.

## Start the development mode

To start the GWT development mode, run:

```shell
gradle gwtDevMode
```

This will start the GWT Development Mode with Super Dev Mode enabled by default.
