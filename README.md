# GWT Gradle Plugin (v2)

[![Build Status](https://github.com/jiakuan/gwt-gradle-plugin/actions/workflows/gradle.yml/badge.svg)](https://github.com/jiakuan/gwt-gradle-plugin/actions)

The **GWT Gradle Plugin** (v2) simplifies the configuration required to build
projects with the [Google Web Toolkit (GWT)](http://www.gwtproject.org/). This
version is a complete rewrite of the original plugin, offering a cleaner
codebase and more intuitive configuration options.

## Sponsors

This project is sponsored by [Document Node](https://documentnode.io), a platform dedicated to empowering content creators with tools that streamline productivity, enhance collaboration, and simplify content management.

Check out their latest Chrome extension, [MindPane](https://mindpane.net/), which transforms any web page into an interactive mind map, helping you quickly grasp the main structure of documents and navigate through topics with ease. ([Chrome Web Store](https://chrome.google.com/webstore/detail/ioimcileegaodabmbcnadppghhakneae))

[![icon-128.png](doc/images/icon-128.png)](https://mindpane.net/) [![x-icon.png](doc/images/x-icon.png)](https://x.com/document_node)

## Key Features

- **Minimal configuration** required to use the plugin
- **Simpler codebase** compared to the v1 plugin
- No need to manually add core GWT dependencies like `gwt-user`, `gwt-dev` and `gwt-codeserver`
- **Enhanced source tracking** for GWT Modules. This feature enables targeted
  tracking of source code specifically related to GWT modules. Only the source
  paths defined within each GWT module are monitored, ensuring that the
  gwtCompile task is triggered only when relevant GWT source files are modified.
  This refined tracking minimizes unnecessary recompilation by focusing solely
  on changes within GWT-specific code, optimizing the build process for faster
  iterations and more efficient resource usage.
- **Configuration aligned with GWT compiler options** for ease of use
- **Support for GWT 2.12+** and the latest Gradle 8.10.2
- Built-in tasks for GWT compilation and dev mode

## Usage

The **GWT Gradle Plugin** is available on
the [Gradle Plugin Portal](https://plugins.gradle.org/plugin/org.docstr.gwt).

To use this plugin, add the following to your `build.gradle` file:

```
plugins {
  id "org.docstr.gwt" version "2.1.2"
}

gwt {
  // Optional: Set the GWT version, defaults to 2.12.1
  // gwtVersion = '2.12.1'
  modules ['<YOUR-GWT-MODULE>']
}
```

Alternatively, you can apply the plugin using the legacy method:

```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "org.docstr.gwt:gwt-gradle-plugin:2.1.2"
  }
}

apply plugin: "org.docstr.gwt"

gwt {
    modules ['<YOUR-GWT-MODULE>']
}
```

## Available Tasks

When you apply the GWT Gradle Plugin, the following tasks are added to your
project:

- `gwtCompile`: Compiles your GWT modules. It will be executed automatically
  when you run `gradle build`.
- `gwtDevMode`: Starts GWT Development Mode (Super Dev Mode is enabled by
  default).

For the full list of available options, refer to
the [Configuration](doc/Configuration.md) documentation.

## Version 1 (v1) Plugin

The original GWT Gradle Plugin (v1) source code is still available on
the [v1 branch](https://github.com/jiakuan/gwt-gradle-plugin/tree/v1) of this
repository. Documentation for v1 can be found
at [http://gwt-gradle.docstr.org](http://gwt-gradle.docstr.org).

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

## Release Process

- Build and test the plugin locally
- Commit all changes to the `main` branch
- Publish the plugin to the Gradle Plugin Portal with `make publish`
- Create a new release on GitHub with `make release`
