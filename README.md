# GWT Gradle Plugin

[![Build Status](https://travis-ci.org/jiakuan/gwt-gradle-plugin.svg?branch=master)](https://travis-ci.org/jiakuan/gwt-gradle-plugin)

This plugin makes it easy to build projects using [GWT](http://www.gwtproject.org/). It provides several tasks to support the development and configures several aspects of your project to work with GWT automatically.

It was originally [created by Steffen Schaefer](https://github.com/steffenschaefer/gwt-gradle-plugin). Really appreciate his effort on making this great plugin.

In last two years, there were no updates in the original git repo, so I plan to continue the maintenance in this fork.

For more information, please see the new documentation site I created: [http://gwt-gradle-plugin.documentnode.io](http://gwt-gradle-plugin.documentnode.io)

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

If you are using Gradle 5.2+, please use `gwt-gradle-plugin:1.0.8`, otherwise, please `gwt-gradle-plugin:1.0.6` for Gradle 4.+.
