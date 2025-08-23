# Version History

**Note:** This version history is for the v1 plugin. For v2 plugin version history, please see the GitHub releases page.

## 1.1+

Please see Github

## 1.0.0+ (2017-07-09)

* Compatibility
    * Gradle 4.0+ (tested 4.0.1)
    * GWT 2.x (tested 2.8.1)
* Changes/fixes
    * Upgraded Gradle version to 4.0.1
    * Upgraded GWT version to 2.8.1

## 0.6 (2014-12-07)

* Compatibility
    * Gradle 1.6+ (tested 2.0 - 2.1)
    * GWT 2.x (tested 2.7.0)
* Changes/fixes
    * Added new flags introduced in GWT 2.7 and 2.6 (GitHub issue #55)
    * Added new task 'checkGwt' for validation of GWT sources (GitHub issue #58)
    * Fixed configuration of test task for GWT (GitHub issue #57)

## 0.5 (2014-09-08)

* Compatibility
    * Gradle 1.6+ (tested 1.12 - 2.1)
    * GWT 2.x (tested 2.6.1)
* Changes/fixes
    * Fixed devModules property in GwtPluginExtension (GitHub issue #40)
    * Added new Flag 'useClasspathForSrc' to GwtSuperDevOptions/GwtSuperDev to work around a bug in GWT (GitHub issues #34 and #37)
    * Added example with non-Maven layout
    * Several cosmetic changes

## 0.4 (2014-03-21)

* Compatibility
    * Gradle 1.6+ (tested 1.6-1.11)
    * GWT 2.x (tested 2.6.0)
* Changes/fixes
    * Internal changes to improve compatibility with several Gradle versions
    * Fixed an issue that caused incompatibilities with Gradle 1.12
    * Added more example projects
    * Improved and extended the documentation
* Breaking changes
    * The inheritance of several tasks is changed. Do a clean build if anything behaves strange.

## 0.3 (2014-01-09)

* Compatibility
    * Gradle 1.8+ (tested 1.8-1.10)
    * GWT 2.x (tested 2.5.1)
* Changes/fixes
    * The plugin was split into several smaller plugins (gwt, gwt-base, gwt-compiler) to better work with GWT libraries and non-webapp projects.

## 0.2 (2013-12-08)

* Compatibility
    * Gradle 1.6+ (tested 1.6-1.9)
    * GWT 2.x (tested 2.5.1)
* Changes/fixes
    * Improved JavaDocs (including external links)
    * The log level of GWT related tasks is now bound to Gradle's log level by default
    * Addition of "gwtSdk" configuration to fix Eclipse classpath when using GWT_CONTAINER
* Breaking changes
    * If you manually define your GWT dependencies, you have to change gwt-dev and gwt-user to be added to the new gwtSdk configuration.

## 0.1 (2013-10-18)

* Compatibility
    * Gradle 1.6+ (tested 1.6-1.8)
    * GWT 2.x (tested 2.5.1)
* Changes
    * Initial release
