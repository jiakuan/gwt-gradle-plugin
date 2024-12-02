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
  id "org.docstr.gwt" version "2.1.2"
}
```

## Configure the GWT module

The GWT Gradle Plugin requires minimal configuration to use. The following is
the minimal configuration required to use the plugin:

```groovy
gwt {
  // e.g. modules = ['com.example.MyModule']
  modules['<YOUR-GWT-MODULE>']
}
```

Once you have enabled the plugin, you only need to specify the GWT module you
want to compile. The plugin will take care of the rest. For more configuration
options, refer to the [Configuration](Configuration.md) documentation.

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
