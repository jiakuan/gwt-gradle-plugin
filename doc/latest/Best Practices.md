# Best Practices

GWT has a long history and has proven to be reliable for compiling Java code into optimized JavaScript. As browser technology, the JavaScript ecosystem, and Gradle continue to evolve rapidly, the best way to use this GWT Gradle plugin is to adapt accordingly.

This page is dedicated to documenting the latest best practices.

## GWT Dependencies

### Transitive Dependencies

`gwt-dev` brings 55 additional JAR files with it. 32 of them contain reported vulnerabilities, totaling at 246 CVEs. The last GWT release (2.11 -> 2.12.1) did not update a single one of these dependencies. (For details, see https://github.com/vmj/gwt-dev-vulnerabilities)

### Dev Dependencies

During development, you need

- `gwt-user.jar` (JRE emulation, Widgets, etc)
- `gwt-dev.jar` (compiler, dev mode)
- `gwt-codeserver.jar` (Jetty-based server that calls GWT compiler and serves the compiler output. It depends on gwt-dev.jar)
- Any third party GWT library (e.g., GWT UI Frameworks, elemental2)

None of these should ever be deployed in a `*.war` file.

### Runtime Dependencies

If needed, you only need to deploy (and their transitive dependencies):

- `gwt-servlet.jar` / gwt-servlet-jakarta.jar, if you use GWT-RPC
- `requestfactory-server.jar`, if you use RequestFactory


## Project Structure

### Single Project Setup

It’s convenient to have a single project setup when using GWT RPC and/or `RequestFactory` because you can place shared interfaces and code in a common package for both the client and backend.

If you create a Gradle project with a single module that combines GWT and the web application, make sure that all development dependencies are excluded from the runtime classpath.

Please see [examples/basic-gwt-rpc](../examples/basic-gwt-rpc) for a single module setup.

### Multi-Module Project

An alternative setup is to split that single Gradle module into two Gradle modules, e.g., gwt-ui and webapp, and then include the various GWT compiler outputs of gwt-ui in the war task of the webapp module. Then you automatically have two dedicated classpath, and you can put all GWT-related dependencies in the gwt-ui module in scope implementation.

Once you have two gradle modules, you need to figure out a way to share the gwt compiler outputs of the gwt-ui module with the webapp modules war task. To do so you should use configurations, see below.

multi-module-project/settings.gradle:

``` 
rootProject.name = 'multi-module-project'

include ':gwt-ui'
include ':webapp'
```

multi-module-project/gwt-ui/build.gradle:

```
plugins {
    id 'java'
    id "org.docstr.gwt" version "2.1.6"
}

group = 'com.example.gwt'
version = '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

dependencies {
   // your GWT dependencies, e.g. guava-gwt
   // implementation "..."
}

gwt {
    modules = ['com.example.gwt.GwtUi']
    // Relocate some GWT outputs so they each have a dedicated folder.
    // This works better when sharing the GWT output with other projects because
    // you can precisely control what to share.

    // The final JS App (optional configurations below)
    war = file("${layout.buildDirectory}/gwt/war")
    // Server side GWT data, e.g. symbol maps, sourcemaps
    deploy = file("${layout.buildDirectory}/gwt/deploy")
    // Source files generated by GWT compiler
    gen = file("${layout.buildDirectory}/gwt/gen")
    // Compile report as information for the GWT developer
    extra = file("${layout.buildDirectory}/gwt/extra")
}

// Additional configurations each holding a single zip file with
// the corresponding GWT compiler output.
configurations {
    gwtJsZipFile
    gwtDeployZipFile
}

// Zip task to package GWT JS output
tasks.register("zipGwtJs", Zip) {
    dependsOn("gwtCompile")
    from (gwt.war)
    destinationDirectory = layout.buildDirectory.dir('gwt')
    archiveBaseName = "${project.name}-js"
}

// Zip task to package GWT deploy output
tasks.register("zipGwtDeploy", Zip) {
    dependsOn("gwtCompile")
    from (gwt.deploy)
    destinationDirectory = layout.buildDirectory.dir('gwt')
    archiveBaseName = "${project.name}-deploy"
}

// Add each zip file as artifact to its corresponding configuration.
// These can then be consumed by other projects.
artifacts {
    gwtJsZipFile tasks.named("zipGwtJs")
    gwtDeployZipFile tasks.named("zipGwtDeploy")
}
```

multi-module-project/webapp/build.gradle:

```
plugins {
    id 'java'
    id 'war'
}

group = 'com.example.gwt'
version = '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

// Additional configurations that each will hold a single zip file
// with the corresponding GWT compiler output
configurations {
    gwtJsZipFile
    gwtDeployZipFile
}

dependencies {
    // web app dependencies
    compileOnly 'javax.servlet:javax.servlet-api:3.1.0'

    // Depend on the zip files produced by 'gwt-ui' module by depending
    // on the corresponding configurations.
    // These should be unzipped into the final war file
    gwtJsZipFile project(path: ':gwt-ui', configuration: 'gwtJsZipFile')
    gwtDeployZipFile project(path: ':gwt-ui', configuration: 'gwtDeployZipFile')
}

war {
    // Ensure that 'gwt-ui' has been compiled and zip files are ready
    dependsOn(configurations.gwtJsZipFile)
    dependsOn(configurations.gwtDeployZipFile)
    // include the JS app to the root of the war file
    into ('/') {
        from zipTree(configurations.gwtJsZipFile.singleFile)
    }
    // include the deploy files to WEB-INF/deploy as they are only used by the server,
    // e.g. during client exception stack trace deobfuscation (if the JS app sends the
    // stack trace to the server)
    into ('WEB-INF/deploy') {
        from zipTree(configurations.gwtDeployZipFile.singleFile)
    }
}
```

With the above, the final war file will never have any GWT libraries packaged because it only depends on the GWT JS output. An alternative to using Gradle configurations is to use Gradle variants, but I think configurations are a bit easier to set up and are good enough in this situation here.

If you’re not building a WAR file or are using a backend technology other than Java, you can configure the JavaScript output directory to point directly to your frontend folder.

```
gwt {
    // ... other gwt configurations
    war = file('../my-frontend/scripts')
}
```
