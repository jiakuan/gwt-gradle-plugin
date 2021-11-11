/**
 * Copyright (C) 2013-2017 Steffen Schaefer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wisepersist.gradle.plugins.gwt;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.gradle.api.DefaultTask;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.file.FileCollection;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.ExecResult;
import org.wisepersist.gradle.plugins.gwt.internal.GwtVersion;

/**
 * Base class for all GWT related tasks.
 */
public abstract class AbstractGwtActionTask extends DefaultTask {

  private static final Logger logger =
      Logging.getLogger(AbstractGwtActionTask.class);

  private String gwtVersion;

  private List<String> modules;

  private FileCollection src;

  private FileCollection classpath;

  private String minHeapSize;

  private String maxHeapSize;

  private final String main;

  private List<Object> args = new ArrayList<>();

  private List<Object> jvmArgs = new ArrayList<>();

  private List<String> extraJvmArgs = new ArrayList<>();

  private boolean debug;

  private LogLevel logLevel;

  private String sourceLevel;

  private Boolean incremental;

  private JsInteropMode jsInteropMode;

  private GwtJsInteropExportsOptions jsInteropExports;

  private MethodNameDisplayMode methodNameDisplayMode;

  public AbstractGwtActionTask(String main) {
    this.main = main;
  }

  @TaskAction
  public void exec() {
    final ExecResult execResult = getProject()
        .javaexec(javaExecSpec -> {
          if (getSrc() == null) {
            throw new InvalidUserDataException("No Source is set");
          }
          if (getClasspath() == null) {
            throw new InvalidUserDataException("Classpath is not set");
          }
          if (getModules() == null || getModules().isEmpty()) {
            throw new InvalidUserDataException("No module[s] given");
          }
          if (getJsInteropExports().shouldGenerate()) {
            if (getJsInteropExports().getIncludePatterns().isEmpty()
                && !getJsInteropExports().getExcludePatterns().isEmpty()) {
              throw new InvalidUserDataException(
                  "No include pattern(s) for JsInterop exports given");
            }
          }

          javaExecSpec.getMainClass().set(main);
          javaExecSpec.setDebug(isDebug());

          // "Fixes" convention mapping
          javaExecSpec.setMinHeapSize(getMinHeapSize());
          javaExecSpec.setMaxHeapSize(getMaxHeapSize());

          FileCollection classpath = getClasspath();
          if (prependSrcToClasspath()) {
            classpath = getSrc().plus(classpath);
          }

          if (System.getProperty("os.name").toLowerCase()
              .contains("windows")) {
            javaExecSpec.environment("CLASSPATH", classpath.getAsPath());
          } else {
            javaExecSpec.setClasspath(classpath);
          }

          argIfSet("-XjsInteropMode", getJsInteropMode());
          if (doesSupportJsInteropExports(GwtVersion.parse(getGwtVersion()))) {
            argOnOff(getJsInteropExports().shouldGenerate(),
                "-generateJsInteropExports",
                "-nogenerateJsInteropExports");
            getJsInteropExports().getIncludePatterns()
                .forEach(includePattern
                    -> argIfSet("-includeJsInteropExports",
                    includePattern));
            getJsInteropExports().getExcludePatterns()
                .forEach(excludePattern
                    -> argIfSet("-excludeJsInteropExports",
                    excludePattern));
          }
          argIfSet("-XmethodNameDisplayMode", getMethodNameDisplayMode());
          argOnOff(getIncremental(), "-incremental", "-noincremental");
          argIfSet("-sourceLevel", getSourceLevel());
          argIfSet("-logLevel", getLogLevel());

          addArgs();
          // Configure extraJvmArgs specified by users
          for (Object extraJvmArg : getExtraJvmArgs()) {
            jvmArgs.add(extraJvmArg);
          }
          javaExecSpec.jvmArgs(jvmArgs);
          javaExecSpec.args(args);
          // the module names are expected to be the last parameters
          javaExecSpec.args(getModules());
          logger.info("main={}, gwtVersion={}, modules={}, "
                  + "minHeapSize={}, maxHeapSize={},  extraJvmArgs={} ",
              main, getGwtVersion(), getModules(), getMinHeapSize(),
              getMaxHeapSize(), getExtraJvmArgs());
        });
    execResult.assertNormalExitValue().rethrowFailure();
  }

  /**
   * If true this causes that the src is prepended to the classpath. This
   * is set to false for Super Dev Mode as the source is given to it as
   * extra parameter.
   *
   * @return true if src should be prepended to the classpath, false otherwise.
   */
  protected boolean prependSrcToClasspath() {
    return true;
  }

  /**
   * If {@code true}, this causes the "generateJsInteropExports" /
   * "-nogenerateJsInteropExports" (added in GWT 2.8) parameter to be added.
   *
   * @return {@code true} if GWT version is >= 2.8, {@code false} otherwise
   */
  private boolean doesSupportJsInteropExports(
      final GwtVersion parsedGwtVersion) {
    return (parsedGwtVersion != null) &&
        (parsedGwtVersion.getMajor() >= 2) &&
        (parsedGwtVersion.getMinor() >= 8);
  }

  @Optional
  @Input
  public String getGwtVersion() {
    return gwtVersion;
  }

  public void setGwtVersion(String gwtVersion) {
    this.gwtVersion = gwtVersion;
  }

  @Input
  public List<String> getModules() {
    return modules;
  }

  /**
   * Sets the GWT modules (fully qualified names) to be used by this task.
   *
   * @param modules
   *            GWT modules to be set for this task
   */
  public void setModules(List<String> modules) {
    this.modules = modules;
  }

  protected void args(Object... args) {
    this.args.addAll(Arrays.asList(args));
  }

  protected void jvmArgs(Object... args) {
    this.jvmArgs.addAll(Arrays.asList(args));
  }

  protected void argIfEnabled(Boolean condition, String arg) {
    if (Boolean.TRUE.equals(condition)) {
      args(arg);
    }
  }

  protected void argOnOff(Boolean condition, String onArg, String offArg) {
    if (Boolean.TRUE.equals(condition)) {
      args(onArg);
    } else if (Boolean.FALSE.equals(condition)) {
      args(offArg);
    }
  }

  protected void dirArgIfSet(String arg, File dir) {
    if (dir != null) {
      dir.mkdirs();
      args(arg, dir);
    }
  }

  protected void argIfSet(String arg, Object value) {
    if (value != null) {
      args(arg, value);
    }
  }

  /**
   * Called directly before executing this task. Subclasses are expected to
   * add all args/javaArgs needed for the execution.
   */
  protected abstract void addArgs();

  /**
   * If true the task instance is treated as being a development related
   * task. Development related tasks will have the devModules set by default.
   *
   * @return true if the task is development related, false otherwise.
   */
  @Input
  protected boolean isDevTask() {
    return true;
  }

  @InputFiles
  @PathSensitive(PathSensitivity.RELATIVE)
  public FileCollection getSrc() {
    return src;
  }

  /**
   * Sets the source directories used by this task instance. These source
   * directories are used by GWT to read java source files from.
   *
   * @param src
   *            source directories to set
   */
  public void setSrc(FileCollection src) {
    this.src = src;
  }

  @Classpath
  public FileCollection getClasspath() {
    return classpath;
  }

  /**
   * Sets the classpath for the spawned java process.
   *
   * @param classpath the classpath to set
   */
  public void setClasspath(FileCollection classpath) {
    this.classpath = classpath;
  }

  @Input
  public String getMinHeapSize() {
    return minHeapSize;
  }

  /**
   * Sets the minimum heap size for the spawned java process.
   *
   * @param minHeapSize the minimum heap size to set
   */
  public void setMinHeapSize(String minHeapSize) {
    this.minHeapSize = minHeapSize;
  }

  @Input
  public String getMaxHeapSize() {
    return maxHeapSize;
  }

  /**
   * Sets the maximum heap size for the spawned java process.
   *
   * @param maxHeapSize the maximum heap size to set
   */
  public void setMaxHeapSize(String maxHeapSize) {
    this.maxHeapSize = maxHeapSize;
  }

  @Input
  public List<String> getExtraJvmArgs() {
    return extraJvmArgs;
  }

  public void setExtraJvmArgs(List<String> extraJvmArgs) {
    this.extraJvmArgs = extraJvmArgs;
  }

  @Input
  public boolean isDebug() {
    return debug;
  }

  /**
   * If set to true this enables debugging for the spawned java process.
   *
   * @param debug true to enable debugging, false otherwise.
   */
  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  @Optional
  @Input
  public LogLevel getLogLevel() {
    return logLevel;
  }

  /**
   * Sets the {@link LogLevel} for this task.
   *
   * @param logLevel the log level to set
   */
  public void setLogLevel(LogLevel logLevel) {
    this.logLevel = logLevel;
  }

  @Optional
  @Input
  public String getSourceLevel() {
    return sourceLevel;
  }

  public void setSourceLevel(String sourceLevel) {
    this.sourceLevel = sourceLevel;
  }

  @Optional
  @Input
  public Boolean getIncremental() {
    return incremental;
  }

  public void setIncremental(Boolean incremental) {
    this.incremental = incremental;
  }

  @Optional
  @Input
  public JsInteropMode getJsInteropMode() {
    return jsInteropMode;
  }

  public void setJsInteropMode(JsInteropMode jsInteropMode) {
    this.jsInteropMode = jsInteropMode;
  }

  @Input
  public GwtJsInteropExportsOptions getJsInteropExports() {
    return jsInteropExports;
  }

  public void setJsInteropExports(GwtJsInteropExportsOptions jsInteropExports) {
    this.jsInteropExports = jsInteropExports;
  }

  @Optional
  @Input
  public MethodNameDisplayMode getMethodNameDisplayMode() {
    return methodNameDisplayMode;
  }

  /**
   * If set, this causes the "-XmethodNameDisplayMode" (added in GWT 2.7/2.8)
   * parameter to be added.
   *
   * @param methodNameDisplayMode The method name display mode specified.
   */
  public void setMethodNameDisplayMode(
      MethodNameDisplayMode methodNameDisplayMode) {
    this.methodNameDisplayMode = methodNameDisplayMode;
  }
}
