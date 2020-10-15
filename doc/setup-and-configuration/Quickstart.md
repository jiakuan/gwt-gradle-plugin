# Quickstart

This chapter will guide you through the first steps to configure gwt-gradle-plugin for your web application project.

## Plugin dependency

As it is not a core Gradle plugin, you have to ensure, that Gradle knows how to get the plugin. To do this, add the following lines to your build.gradle, as GWT Gradle Plugin is available in [Maven central repository](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.wisepersist%22AND%20a%3A%22gwt-gradle-plugin%22).

    buildscript {
        repositories {
            mavenCentral()
        }
        dependencies {
            classpath 'org.wisepersist:gwt-gradle-plugin:1.0.10'
        }
    }

If you want to use the latest snapshot version, you can also use the following repository that only contains the snapshot plugin:

	buildscript {
		repositories {
			maven {
				url "https://oss.sonatype.org/content/repositories/snapshots/"
			}
			mavenCentral()
		}
		dependencies {
			classpath 'org.wisepersist:gwt-gradle-plugin:1.1.11-SNAPSHOT'
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
    gwtVersion='2.9.0'
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
