# Overview (v1)

This plugin makes it easy to build projects using GWT. It provides several tasks to support the development and configures several aspects of your project to work with GWT automatically.

Among other things, the plugin provides these features:

* Running the GWT compiler and automatic inclusion of the compiled stuff in your *.war file
* Starting GWT Dev Mode
* Support for Super Dev Mode (GWT 2.8+)
* Configuration of different GWT modules for development and production
* GWT specific configuration of Eclipse projects

## Examples

Several example projects can be found in [/examples](https://github.com/jiakuan/gwt-gradle-plugin/tree/master/examples).

## Usage

GWT Gradle Plugin is available in [Maven central repository](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.docstr%22AND%20a%3A%22gwt-gradle-plugin%22).

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

    apply plugin: 'war'
    apply plugin: 'gwt'

    gwt {
        gwtVersion='2.10.0'
        modules '<YOUR-GWT-MODULE>'
    }

This will configure your GWT web project to execute the GWT compiler and include the compiler output into your *.war file. The code shown above also configures all GWT core dependencies (gwt-dev, gwt-user, gwt-servlet, ...).

To build the *.war file including your compiled GWT modules, simply call "gradle build".
If you want to start the GWT development mode simply call "gradle gwtDev".

To learn about different scenarios or more specific configuration needs, please refer to the [Documentation](http://gwt-gradle.docstr.org).


## How to contribute

All pull requests are welcome.

Normal process would be:

* Create a new issue for things to discuss
* After discussion about changes, fork the project and make changes
* Build projects to make sure no errors
    1. run `just clean build` under project root.
    2. run `./gradlew clean build` under /examples
* Create a new pull request for review and discussion
* After confirmation, we will merge the pull request to master
