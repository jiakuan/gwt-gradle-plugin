# GWT Gradle Plugin (v2)

[![Build Status](https://github.com/jiakuan/gwt-gradle-plugin/actions/workflows/gradle.yml/badge.svg)](https://github.com/jiakuan/gwt-gradle-plugin/actions)

The **GWT Gradle Plugin** (v2) simplifies the configuration required to build projects with the [Google Web Toolkit (GWT)](http://www.gwtproject.org/). This version is a complete rewrite of the original plugin, offering a cleaner codebase and more intuitive configuration options.

## Key Features

- **Minimal configuration** required to use the plugin
- **Simpler codebase** compared to the v1 plugin
- No need to manually add core GWT dependencies like `gwt-user` and `gwt-dev`
- **Configuration aligned with GWT compiler options** for ease of use
- **Support for GWT 2.11.0** and the latest Gradle 8.10.2
- Built-in tasks for GWT compilation and dev mode

## Usage

The **GWT Gradle Plugin** is available on the [Gradle Plugin Portal](https://plugins.gradle.org/plugin/org.docstr.gwt).


To use this plugin, add the following to your `build.gradle` file:

Add the following to your `build.gradle` file to use the plugin:

```
plugins {
  id "org.docstr.gwt version "2.0.1-alpha"
}

gwt {
  modules ['<YOUR-GWT-MODULE>']
}
```

Alternatively, you can apply the plugin using the legacy method:

```
buildscript {
  repositories {
    maven {
      url = uri("https://plugins.gradle.org/m2/")
    }
  }
  dependencies {
    classpath("org.docstr.gwt:gwt-gradle-plugin:2.0.1-alpha")
  }
}

apply(plugin = "org.docstr.gwt")

gwt {
    modules ['<YOUR-GWT-MODULE>']
}
```

## Available Tasks

When you apply the GWT Gradle Plugin, the following tasks are added to your project:

- `gwtCompile`: Compiles your GWT modules.
- `gwtDevMode`: Starts GWT Development Mode (Super Dev Mode is enabled by default).


## Version 1 (v1) Plugin

The original GWT Gradle Plugin (v1) source code is still available on the [v1 branch](https://github.com/jiakuan/gwt-gradle-plugin/tree/v1) of this repository. Documentation for v1 can be found at [http://gwt-gradle.docstr.org](http://gwt-gradle.docstr.org).

## Building the Project

### On macOS or Linux

You can clean and build the project using the following commands:

```
make clean build
```

### On Windows

Use the Gradle wrapper to clean and build the project:

```
gradlew.bat clean build
```

