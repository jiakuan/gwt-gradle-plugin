/**
 * Copyright (C) 2024 Document Node Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.docstr.gwt.options;

import java.io.File;
import org.docstr.gwt.AbstractBaseOptions;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

/**
 * GWT test options
 */
public abstract class GwtTestOptions extends AbstractBaseOptions {

  /**
   * Builds the parameter string for the GWT test options 'gwt.args'
   *
   * @return The parameter string
   */
  public String getParameterString() {
    final StringBuilder builder = new StringBuilder();

    dirArgIfSet(builder, "-war", getWar());
    dirArgIfSet(builder, "-deploy", getDeploy());
    dirArgIfSet(builder, "-extra", getExtra());
    dirArgIfSet(builder, "-workDir", getWorkDir());
    dirArgIfSet(builder, "-gen", getGen());

    argIfSet(builder, "-logLevel", getLogLevel());
    argIfSet(builder, "-sourceLevel", getSourceLevel());

    argIfSet(builder, "-port", getPort());
    argIfSet(builder, "-whitelist", getWhitelist());
    argIfSet(builder, "-blacklist", getBlacklist());
    dirArgIfSet(builder, "-logdir", getLogdir());
    argIfSet(builder, "-codeServerPort", getCodeServerPort());

    argIfSet(builder, "-style", getStyle());
    argIfEnabled(builder, getEa(), "-ea");
    argIfEnabled(builder, getDisableClassMetadata(), "-XdisableClassMetadata");
    argIfEnabled(builder, getDisableCastChecking(), "-XdisableCastChecking");
    argIfEnabled(builder, getDraftCompile(), "-draftCompile");
    argIfSet(builder, "-localWorkers", getLocalWorkers());
    argIfEnabled(builder, getProd(), "-prod");
    argIfSet(builder, "-testMethodTimeout", getTestMethodTimeout());
    argIfSet(builder, "-testBeginTimeout", getTestBeginTimeout());
    argIfSet(builder, "-runStyle", getRunStyle());
    argIfEnabled(builder, getNotHeadless(), "-notHeadless");
    argIfEnabled(builder, getStandardsMode(), "-standardsMode");
    argIfEnabled(builder, getQuirksMode(), "-quirksMode");
    argIfSet(builder, "-Xtries", getTries());
    argIfSet(builder, "-userAgents", getUserAgents());

    return builder.toString();
  }

  private void argIfEnabled(
      StringBuilder builder, Property<Boolean> condition, String arg) {
    if (condition.isPresent() && Boolean.TRUE.equals(condition.get())) {
      arg(builder, arg);
    }
  }

  private void dirArgIfSet(
      StringBuilder builder, String arg, DirectoryProperty dir) {
    if (dir != null && dir.isPresent()) {
      File dirAsFile = dir.getAsFile().get();
      dirAsFile.mkdirs();
      arg(builder, arg, dirAsFile);
    }
  }

  private <T> void argIfSet(
      StringBuilder builder, String arg, Property<T> value) {
    if (value != null && value.isPresent()) {
      arg(builder, arg, value.get());
    }
  }

  private void arg(StringBuilder builder, Object... args) {
    for (Object arg : args) {
      if (builder.length() > 0) {
        builder.append(' ');
      }
      builder.append(arg.toString());
    }
  }

  /**
   * <code>-port</code>
   * Specifies the TCP port for the embedded web server (defaults to 8888)
   *
   * @return The port
   */
  public abstract Property<Integer> getPort();

  /**
   * <code>-logdir</code>
   * Logs to a file in the given directory, as well as graphically
   *
   * @return The log directory
   */
  public abstract DirectoryProperty getLogdir();

  /**
   * <code>-whitelist</code>
   *
   * @return The whitelist
   */
  public abstract Property<String> getWhitelist();

  /**
   * <code>-blacklist</code>
   *
   * @return The blacklist
   */
  public abstract Property<String> getBlacklist();

  /**
   * <code>-codeServerPort</code>
   * Specifies the TCP port for the code server (defaults to 9997 for
   * classic Dev Mode or 9876 for Super Dev Mode)
   *
   * @return The code server port
   */
  public abstract Property<Integer> getCodeServerPort();

  /**
   * <code>-ea</code>
   *
   * @return The ea flag
   */
  public abstract Property<Boolean> getEa();

  /**
   * <code>-XdisableClassMetadata</code>
   *
   * @return The disable class metadata flag
   */
  public abstract Property<Boolean> getDisableClassMetadata();

  /**
   * <code>-XdisableCastChecking</code>
   *
   * @return The disable cast checking flag
   */
  public abstract Property<Boolean> getDisableCastChecking();

  /**
   * <code>-draftCompile</code>
   *
   * @return The draft compile flag
   */
  public abstract Property<Boolean> getDraftCompile();

  /**
   * <code>-localWorkers</code>
   *
   * @return The local workers
   */
  public abstract Property<Integer> getLocalWorkers();

  /**
   * <code>-prod</code>
   *
   * @return The prod flag
   */
  public abstract Property<Boolean> getProd();

  /**
   * <code>-testMethodTimeout</code>
   *
   * @return The test method timeout
   */
  public abstract Property<Integer> getTestMethodTimeout();

  /**
   * <code>-testBeginTimeout</code>
   *
   * @return The test begin timeout
   */
  public abstract Property<Integer> getTestBeginTimeout();

  /**
   * <code>-runStyle</code>
   *
   * @return The run style
   */
  public abstract Property<String> getRunStyle();

  /**
   * <code>-notHeadless</code>
   *
   * @return The not headless flag
   */
  public abstract Property<Boolean> getNotHeadless();

  /**
   * <code>-standardsMode</code>
   *
   * @return The standards mode flag
   */
  public abstract Property<Boolean> getStandardsMode();

  /**
   * <code>-quirksMode</code>
   *
   * @return The quirks mode flag
   */
  public abstract Property<Boolean> getQuirksMode();

  /**
   * <code>-Xtries</code>
   *
   * @return The tries
   */
  public abstract Property<Integer> getTries();

  /**
   * <code>-userAgents</code>
   *
   * @return The user agents
   */
  public abstract Property<String> getUserAgents();

  /**
   * Names of the test tasks to configure for GWT.
   * <p>
   *     This defaults to an empty list.
   *     For backwards compatibility, an empty list is interpreted to mean all tasks of type {@link org.gradle.api.tasks.testing.Test Test}.
   * </p>
   * <p>
   *     To disable any test task configuration, this can be set to {@code null}.
   * </p>
   *
   * @return The test task names.
   */
  public abstract ListProperty<String> getTestTasks();
}
