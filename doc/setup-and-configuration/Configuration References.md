# Configuration References

## Plugin configuration

The plugin registers an extension named "gwt" of type [GwtPluginExtension](../javadoc/org/wisepersist/gradle/plugins/gwt/GwtPluginExtension.html) with the Gradle model. This extension defines the conventions/defaults for all GWT related tasks. If you use the extension to do the configuration, the plugin ensures that the configuration properties are consistently set as default values to all related tasks. All properties that are common to multiple/all GWT related tasks are defined in the extension itself (e.g. used GWT modules). Properties that a re specific to one kind of task are defined in specific sub-objects.

An example of both kinds of properties looks this way:

```
gwt {
    gwtVersion='2.9.0'
    minHeapSize = "512M"
    maxHeapSize = "1024M"
    logLevel = 'INFO'
    modules 'org.wisepersist.gradle.plugins.gwt.example.Example'

    jsInteropExports {
        shouldGenerate = true
    }

    compiler {
        strict = true
        disableClassMetadata = true
        disableCastChecking = true
    }
    
    dev {
        noserver = true
        port = 1337
    }
}
```

In addition to specifying properties in the plugin extension, it's also possible to overwrite the defaults for specific tasks.

An example of configuring the "compileGwt" task to use different GWT modules looks like this:

```
compileGwt {
    modules = ['org.wisepersist.different.ModuleName']
}
```

### Available configuration parameters

In the following list you can find the interfaces/classes that define specific tasks as well as their associated configuration object in the plugin extension:

* compileGwt ([GwtCompile](../javadoc/org/wisepersist/gradle/plugins/gwt/GwtCompile.html))
    * Extension object: compiler ([GwtCompileOptions](../javadoc/org/wisepersist/gradle/plugins/gwt/GwtCompileOptions.html))
* gwtDev ([GwtDev](../javadoc/org/wisepersist/gradle/plugins/gwt/GwtDev.html))
    * Extension object: dev ([GwtDevOptions](../javadoc/org.wisepersist/gradle/plugins/gwt/GwtDevOptions.html))
* gwtSuperDev ([GwtSuperDev](../javadoc/org/wisepersist/gradle/plugins/gwt/GwtSuperDev.html))
    * Extension object: dev ([GwtSuperDevOptions](../javadoc/org/wisepersist/gradle/plugins/gwt/GwtSuperDevOptions.html))

### Test configuration

The plugin's support of testing is not based on a custom task but instead extends the existing Test task of Gradle.

To do this, every instance of Gradle's Test task is dynamically extended to have a property `gwt` of type [GwtTestExtension](../javadoc/org/wisepersist/gradle/plugins/gwt/GwtTestExtension.html). In addition, an instance of [GwtTestOptions](../javadoc/de/richsource/gradle/plugins/gwt/GwtTestOptions.html) is added as property `test` to the plugin's extension object. Using this, you can again specify defaults for all instances of the Test task.

To activate the manipulation of Test tasks to support GWT tests, you have to add the foollowing to your build.gradle:

```
gwt {
    test {
        hasGwtTests = true
    }
}
```

## Common cases

### Memory settings

You can change the memory settings for all GWT related tasks (compileGwt, gwtDev, ...) with these properties:

```
gwt {
    minHeapSize = "512M"
    maxHeapSize = "1024M"
}
```

To change those settings for only one specific task you can also set these settings for this task only:

```
compileGwt {
    minHeapSize = "512M";
    maxHeapSize = "1024M";
}
```

The default value for both, minHeapSize and maxHeapSize is "256M".

### Extra JVM arguments

Extra JVM arguments can be provided through the `extraJvmArgs` property.

```
gwt {
    extraJvmArgs = ["-Djava.io.tmpdir=${buildDir}"]
}
```

### Log level

The log level of GWT tasks is automatically configured depending on Gradle's log level. So by default this is "ERROR".

If you change Gradle's log level (e.g. by adding "--info" or "--debug" on the command line), this also affects the log level of GWT related tasks.

To change the log level independent of Gradle's log level, you can do this:

```
gwt {
    logLevel = 'INFO'
}
```

The available log levels are defined in the enum [LogLevel](../javadoc/org/wisepersist/gradle/plugins/gwt/LogLevel.html).
