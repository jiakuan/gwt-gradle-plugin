# Quickstart

This chapter will guide you through the first steps to configure gwt-gradle-plugin for your web application project.

## Plugin dependency

GWT Gradle Plugin is available in the [Gradle Plugin Portal](https://plugins.gradle.org/plugin/org.docstr.gwt).

Using the plugins DSL:

```
plugins {
  id "org.docstr.gwt" version "1.1.31"
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
    classpath "org.docstr:gwt-gradle-plugin:1.1.31"
  }
}

apply plugin: "org.docstr.gwt"
```

GWT Gradle Plugin is also available in [Maven central repository](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.docstr%22%20AND%20a%3A%22gwt-gradle-plugin%22).

The following example shows the code to set up gwt-gradle-plugin for a GWT web application project using Maven/Gradle standard layout.

    buildscript {
        repositories {
            mavenCentral()
            maven {
                url "https://plugins.gradle.org/m2/"
            }
        }
        dependencies {
            classpath 'org.docstr:gwt-gradle-plugin:1.1.31'
        }
    }


## Basic web application setup

Supposed you already applied the gradle "war" plugin to your project, you have to also apply the "gwt" plugin:

```
apply plugin: 'war'
apply plugin: 'gwt'
```

Now you have to configure the GWT modules to compile into your web application:

```
gwt {
    modules '<YOUR-GWT-MODULE>'
}
```

## GWT dependencies

If you want to use automatically configured GWT dependencies (gwt-dev, gwt-user, ...) you have to extend the configuration to set the desired GWT version:

```
gwt {
    gwtVersion='2.10.0'
    modules '<YOUR-GWT-MODULE>'
}
```

## Activating compiler optimizations

The plugin applies the default optimization settings but that can be adjusted. The following example shows how to adjust some of the flags (please refer to the official documentation on what these flags do):

```
gwt {
    compiler {
        disableClassMetadata = true; // activates -XdisableClassMetadata
        disableCastChecking = true; // activates -XdisableCastChecking
    }
}
```

## Gradle tasks

To build the *.war file including your compiled GWT modules, simply call "gradle build". If you want to start the GWT development mode simply call "gradle gwtDev".

## How to debug Development Mode?

When running the task “gwtDev” you can specify a system property “-DgwtDev.debug=true” to enable debugging. This causes the build to stop when starting development mode and waiting for a debuger to attach to port 5005. Now you can configure your IDE to connect to that debug port.
