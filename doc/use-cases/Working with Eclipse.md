# Working with Eclipse

> From 1.12.0, the eclipse related code has been removed from the plugin code, please use the GWT eclipse plugin etc. to setup your projects.
> 
> The information on this page is outdated.

This Eclipse feature has been tested with Eclipse 4.7 and the gwt plugin version 3.0.0.

This will now not work with gwt plugin versions less than 3.0.0.

## Project Layout

TODO GPE vs Maven/Gradle Standard-Layout

## Project Import

In general there are two methods of importing Gradle projects into Eclipse:

1. Running "gradle eclipse" and import the projects as existing projects
1. By using Gradle integration for Eclipse (GitHub repository)

Both methods have advantages and the gwt-gradle-plugin doesn't force you to use either one.

## Working with "gradle eclipse"

Importing projects using this method should be working out of the box as the plugin manipulates the eclipse model accordingly. What you have to do is:

1. Apply the Gradle plugin "eclipse" by adding "apply plugin: 'eclipse'" to your build.gradle
1. Run "gradle eclipse" in your project
1. In Eclipse go to File > Import and import the project as "Existing Projects into Workspace"

Whenever you change something that is relevant for eclipse, simply rerun "gradle eclipse".

## Working with Gradle Integration for Eclipse

Importing a project to Eclipse using this approach can be a little bit tricky, but if you pay attention to some details, this will work very well.

As the gwt-gradle-plugin must specify special settings for the Eclipse configuration, you have to "apply plugin: 'eclipse'" in your build.gradle.

Assumed you already installed Gradle Integration for Eclipse, the following steps are necessary to import the project to your Eclipse workspace:

1. Open the File > Import... dialog and select "Gradle Project" in the list
1. Select the root of your Gradle project
1. Press "Build Model"
1. Select your project in the tree
1. Adjust the parameters as needed. Pay special attention to set "Run before" to the default value "cleanEclipse eclipse". Other settings will work too, but leaving this setting empty will not(!)

If you have enable "dependency management" and you are faced with Classloading issues that do not occur in Gradle, try the following: Go to the project settings and open the page "gradle". Select "As returned by build script" for "Classpath sorting strategy".

Whenever you change something that is relevant for eclipse, right-click on your project and select best-matching "Refresh" command in the menu named "Gradle".
