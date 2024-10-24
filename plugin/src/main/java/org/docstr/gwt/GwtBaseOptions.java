/**
 * Copyright (C) 2024 Document Node Pty Ltd
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
package org.docstr.gwt;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

/**
 * Base options for GWT compiler and dev mode
 */
public abstract class GwtBaseOptions {

  /**
   * Minimum heap size for the JVM
   * @return The minimum heap size
   */
  public abstract Property<String> getMinHeapSize();

  /**
   * Maximum heap size for the JVM
   * @return The maximum heap size
   */
  public abstract Property<String> getMaxHeapSize();

  /**
   * <code>-logLevel</code>
   * <p>The level of logging detail: ERROR, WARN, INFO, TRACE, DEBUG, SPAM
   * or ALL (defaults to INFO)
   * @return The log level
   */
  public abstract Property<String> getLogLevel();

  /**
   * <code>-workDir</code>
   * <p>The compiler's working directory for internal use (must be writeable;
   * defaults to a system temp dir)
   * @return The working directory
   */
  public abstract DirectoryProperty getWorkDir();

  /**
   * <code>-gen</code>
   * <p>Debugging: causes normally-transient generated types to be saved
   * in the specified directory
   * @return The generated types directory
   */
  public abstract DirectoryProperty getGen();

  /**
   * <code>-war</code>
   * The directory into which deployable output files will be written
   * (defaults to 'war')
   * @return The war directory
   */
  public abstract DirectoryProperty getWar();

  /**
   * <code>-deploy</code>
   * The directory into which deployable but not servable output files
   * will be written (defaults to 'WEB-INF/deploy' under the -war
   * directory/jar, and may be the same as the -extra directory/jar)
   * @return The deploy directory
   */
  public abstract DirectoryProperty getDeploy();

  /**
   * <code>-extra</code>
   * The directory into which extra files, not intended for
   * deployment, will be written
   * @return The extra directory
   */
  public abstract DirectoryProperty getExtra();

  /**
   * <code>-[no]generateJsInteropExports</code>
   * Generate exports for JsInterop purposes. If no
   * -includeJsInteropExport/-excludeJsInteropExport provided, generates
   * all exports. (defaults to OFF)
   * @return The generate JsInterop exports flag
   */
  public abstract Property<Boolean> getGenerateJsInteropExports();

  /**
   * <code>-includeJsInteropExports/excludeJsInteropExports</code>
   * Include/exclude members and classes while generating JsInterop
   * exports. Flag could be set multiple times to expand the pattern.
   * (The flag has only effect if exporting is enabled
   * via -generateJsInteropExports)
   * @return The include/exclude JsInterop exports
   */
  public abstract ListProperty<String> getIncludeJsInteropExports();

  /**
   * <code>-includeJsInteropExports/excludeJsInteropExports</code>
   * Include/exclude members and classes while generating JsInterop
   * exports. Flag could be set multiple times to expand the pattern.
   * (The flag has only effect if exporting is enabled
   * via -generateJsInteropExports)
   * @return The include/exclude JsInterop exports
   */
  public abstract ListProperty<String> getExcludeJsInteropExports();

  /**
   * <code>-XmethodNameDisplayMode</code>
   * EXPERIMENTAL: Specifies method display name mode for chrome devtools:
   * NONE, ONLY_METHOD_NAME, ABBREVIATED or FULL (defaults to NONE)
   * @return The method name display mode
   */
  public abstract Property<String> getMethodNameDisplayMode();

  /**
   * <code>-sourceLevel</code>
   * Specifies Java source level (defaults to 1.8)
   * @return The source level
   */
  public abstract Property<String> getSourceLevel();

  /**
   * <code>-[no]incremental</code>
   * Compiles faster by reusing data from the previous compile.
   * (defaults to OFF)
   * @return The incremental flag
   */
  public abstract Property<Boolean> getIncremental();

  /**
   * <code>-style</code>
   * Script output style: DETAILED, OBFUSCATED or PRETTY
   * (defaults to OBFUSCATED)
   * @return The style
   */
  public abstract Property<String> getStyle();

  /**
   * <code>-[no]failOnError</code>
   * Fail compilation if any input file contains an error. (defaults to OFF)
   * @return The fail on error flag
   */
  public abstract Property<Boolean> getFailOnError();

  /**
   * <code>-setProperty</code>
   * Set the values of a property in the form of
   * propertyName=value1[,value2...].
   * Example: -setProperties = ["user.agent=safari", "locale=default"]
   * would add the parameters -setProperty user.agent=safari -setProperty locale=default
   * @return The set properties
   */
  public abstract ListProperty<String> getSetProperty();

  /**
   * <code>module[s]</code>
   * Specifies the name(s) of the module(s) to host
   * @return The modules
   */
  public abstract ListProperty<String> getModules();

}
