# GWT Gradle Plugin

[![Build Status](https://travis-ci.org/jiakuan/gwt-gradle-plugin.svg?branch=master)](https://travis-ci.org/jiakuan/gwt-gradle-plugin)

This plugin makes it easy to build projects using [GWT](http://www.gwtproject.org/). It provides several tasks to support the development and configures several aspects of your project to work with GWT automatically.

It was originally [created by Steffen Schaefer](https://github.com/steffenschaefer/gwt-gradle-plugin). Really appreciate his effort on making this great plugin.

In last two years, there were no updates in the original git repo, so I plan to continue the maintenance in this fork.

For more information, please see the new documentation site I created: [http://gwt-gradle-plugin.documentnode.io](http://gwt-gradle-plugin.documentnode.io)

## Usage

GWT Gradle Plugin is available in the [Gradle Plugin Portal](https://plugins.gradle.org/plugin/org.wisepersist.gwt).

Using the plugins DSL:

```
plugins {
  id "org.wisepersist.gwt" version "1.1.0"
}
```

Using legacy plugin application:

```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.org.wisepersist:gwt-gradle-plugin:1.1.0"
  }
}

apply plugin: "org.wisepersist.gwt"
```

GWT Gradle Plugin is also available in [Maven central repository](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.wisepersist%22AND%20a%3A%22gwt-gradle-plugin%22).

The following example shows the code to set up gwt-gradle-plugin for a GWT web application project using Maven/Gradle standard layout.

    buildscript {
        repositories {
            mavenCentral()
        }
        dependencies {
            classpath 'org.wisepersist:gwt-gradle-plugin:1.0.10'
        }
    }

    apply plugin: 'war'
    apply plugin: 'gwt'

    gwt {
        gwtVersion='2.9.0'
        modules '<YOUR-GWT-MODULE>'
    }

This will configure your GWT web project to execute the GWT compiler and include the compiler output into your *.war file. The code shown above also configures all GWT core dependencies (gwt-dev, gwt-user, gwt-servlet, ...).

To build the *.war file including your compiled GWT modules, simply call "gradle build".
If you want to start the GWT development mode simply call "gradle gwtDev".

To learn about different scenarios or more specific configuration needs, please refer to the [Documentation](http://gwt-gradle-plugin.documentnode.io).

## How to build

If you are on macOS or Linux, you can use the following commands to clean and build the project.

```
make clean build
```

If you are on Windows, you can use gradle wrapper to build.

```
gradlew.bat clean build
```

## Gradle 5.2+

If you are using Gradle 5.2+, please use `gwt-gradle-plugin:1.0.8+`, which is compatible with Gradle 4.6+.

For Gradle <=4.5, please use `gwt-gradle-plugin:1.0.6`.
