# Project Types (v1)

This document describes various project types that can be worked with gwt-gradle0-plugin.

## Web application

Using gwt-gradle-plugin to configure a web application project is the most common use-case. [Quickstart](quickstart.html) shows how to configure GWT based web application projects.

## Library project

If you create your own GWT library (e.g. widget library) you will only need some of the funtionality provided by the plugin. What you need for this kind of project are basically the GWT dependencies. As such a project does not provide an entry point, there's nothing to compile or run.

To configure a library project, simply use the "gwt-base" plugin instead of applying "gwt" and "war" plugins. Additionally, you do not have to specify any GWT module.

```
buildscript {
    [...]
}

apply plugin: 'gwt-base'

gwt {
    gwtVersion='2.10.0'
}
```

## Compile only

This is the kind of project if you use GWT without deploying your application as a *.war file. In this case you typically don't use the "war" plugin but running the GWT compiler is needed. To configure this kind of project, you can use the "gwt-compiler" plugin:

```
buildscript {
    [...]
}

apply plugin: 'gwt-compiler'

gwt {
    gwtVersion='2.10.0'
    modules '<YOUR-GWT-MODULE>'
}
```

As nobody but you knows what you want to do with the GWT compiler output, the plugin does nothing else than configuring the task "compileGwt". You are responsible the this task is called by setting appropriate task dependencies.

Typical use-cases for this are:
* Uploading the GWT compiler output to a maven repository to be consumed by other projects.
* Consuming the GWT compiler output an another project of a multi-module build

The first use-case can be configured the following way:

```
buildscript {
    [...]
}

apply plugin: 'gwt-compiler'
apply plugin: 'maven-publish'

gwt {
    gwtVersion='2.10.0'
    modules '<YOUR-GWT-MODULE>'
}

task gwtZip(type: Zip) {
    from tasks.compileGwt.outputs
}

group='<YOUR-GROUP-ID>'
version='<YOUR-VERSION>'
publishing {
    publications {
        mavenJava(MavenPublication) {
            artifact gwtZip {
                extension = 'zip'
                classifier = 'gwt'
            }
        }
    }
    repositories {
        maven {
            url project.file('repo').toURI()
        }
    }
}
```

The second use-case can be set up in the following way if the consuming project is a web project:

```
apply plugin: 'war'

war {
    from project(':compile-only').tasks.compileGwt.outputs
}
```

## Library Project (gwt-base) vs Compile Only (gwt-compiler)

Here is the difference between 'gwt-base' and 'gwt-compiler':

* The **gwt-base** plugin only contains GWT related configurations such as required dependencies, which are normally used by library projects. Library projects normally don't have GWT entry point classes but contain reusable client side code. Guava-gwt is a good example of library projects.
* The **gwt-compiler** plugin extends **gwt-base** plugin and contains the actual `gwtCompile`, `gwtDraftCompile` and `gwtCheck` gradle tasks. When gwt modules (with entry points) are configured, this plugin will actually compile the GWT modules. This plugin is useful when you want to compile the GWT modules but we don't want to use them in a web application. A typical example is when you want to develop JavaScript libraries using GWT.
* The **gwt** plugin is simply a combination of **gwt-compiler** plugin and **war** plugin.
