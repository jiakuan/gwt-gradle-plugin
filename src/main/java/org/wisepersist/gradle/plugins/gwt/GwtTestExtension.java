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
import java.util.concurrent.Callable;
import org.gradle.api.internal.IConventionAware;

/**
 * GWT specific extension for the Test task.
 */
public class GwtTestExtension extends GwtTestOptionsBase {

  private File war;
  private File deploy;
  private File extra;
  private File workDir;
  private File gen;
  private File cacheDir;

  protected String getParameterString() {
    final StringBuilder builder = new StringBuilder();

    dirArgIfSet(builder, "-war", getWar());
    dirArgIfSet(builder, "-deploy", getDeploy());
    dirArgIfSet(builder, "-extra", getExtra());
    dirArgIfSet(builder, "-workDir", getWorkDir());
    dirArgIfSet(builder, "-gen", getGen());

    argIfSet(builder, "-logLevel", getLogLevel());

    argIfSet(builder, "-port",
        Boolean.TRUE.equals(getAutoPort()) ? "auto" : getPort());
    argIfSet(builder, "-whitelist", getWhitelist());
    argIfSet(builder, "-blacklist", getBlacklist());
    argIfSet(builder, "-logdir", getLogDir());
    argIfSet(builder, "-codeServerPort",
        Boolean.TRUE.equals(getAutoCodeServerPort()) ? "auto"
            : getCodeServerPort());

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

  private void argIfEnabled(StringBuilder builder, Boolean condition,
      String arg) {
    if (Boolean.TRUE.equals(condition)) {
      arg(builder, arg);
    }
  }

  private void dirArgIfSet(StringBuilder builder, String arg, File dir) {
    if (dir != null) {
      dir.mkdirs();
      arg(builder, arg, dir);
    }
  }

  private void argIfSet(StringBuilder builder, String arg, Object value) {
    if (value != null) {
      arg(builder, arg, value);
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

  protected void configure(final GwtPluginExtension extension,
      final IConventionAware conventionAware) {
    final GwtTestOptions testOptions = extension.getTest();

    conventionAware.getConventionMapping().map("war",
        (Callable<File>) () -> extension.getDevWar());
    conventionAware.getConventionMapping().map("extra",
        (Callable<File>) () -> extension.getExtraDir());
    conventionAware.getConventionMapping().map("workDir",
        (Callable<File>) () -> extension.getWorkDir());
    conventionAware.getConventionMapping().map("gen",
        (Callable<File>) () -> extension.getGenDir());
    conventionAware.getConventionMapping().map("cacheDir",
        (Callable<File>) () -> extension.getCacheDir());
    conventionAware.getConventionMapping().map("logLevel",
        (Callable<LogLevel>) () -> extension.getLogLevel());

    conventionAware.getConventionMapping().map("port",
        (Callable<Integer>) () -> testOptions.getPort());
    conventionAware.getConventionMapping().map("autoPort",
        (Callable<Boolean>) () -> testOptions.getAutoPort());
    conventionAware.getConventionMapping().map("whitelist",
        (Callable<String>) () -> testOptions.getWhitelist());
    conventionAware.getConventionMapping().map("blacklist",
        (Callable<String>) () -> testOptions.getBlacklist());
    conventionAware.getConventionMapping().map("logDir",
        (Callable<File>) () -> testOptions.getLogDir());
    conventionAware.getConventionMapping().map("codeServerPort",
        (Callable<Integer>) () -> testOptions.getCodeServerPort());
    conventionAware.getConventionMapping().map("autoCodeServerPort",
        (Callable<Boolean>) () -> testOptions.getAutoCodeServerPort());

    conventionAware.getConventionMapping().map("style",
        (Callable<Style>) () -> testOptions.getStyle());
    conventionAware.getConventionMapping().map("ea",
        (Callable<Boolean>) () -> testOptions.getEa());
    conventionAware.getConventionMapping().map("disableClassMetadata",
        (Callable<Boolean>) () -> testOptions.getDisableClassMetadata());
    conventionAware.getConventionMapping().map("disableCastChecking",
        (Callable<Boolean>) () -> testOptions.getDisableCastChecking());
    conventionAware.getConventionMapping().map("draftCompile",
        (Callable<Boolean>) () -> testOptions.getDraftCompile());
    conventionAware.getConventionMapping().map("localWorkers",
        (Callable<Integer>) () -> testOptions.getLocalWorkers());
    conventionAware.getConventionMapping().map("prod",
        (Callable<Boolean>) () -> testOptions.getProd());
    conventionAware.getConventionMapping().map("testMethodTimeout",
        (Callable<Integer>) () -> testOptions.getTestMethodTimeout());
    conventionAware.getConventionMapping().map("testBeginTimeout",
        (Callable<Integer>) () -> testOptions.getTestBeginTimeout());
    conventionAware.getConventionMapping().map("runStyle",
        (Callable<String>) () -> testOptions.getRunStyle());
    conventionAware.getConventionMapping().map("notHeadless",
        (Callable<Boolean>) () -> testOptions.getNotHeadless());
    conventionAware.getConventionMapping().map("standardsMode",
        (Callable<Boolean>) () -> testOptions.getStandardsMode());
    conventionAware.getConventionMapping().map("quirksMode",
        (Callable<Boolean>) () -> testOptions.getQuirksMode());
    conventionAware.getConventionMapping().map("tries",
        (Callable<Integer>) () -> testOptions.getTries());
    conventionAware.getConventionMapping().map("userAgents",
        (Callable<String>) () -> testOptions.getUserAgents());
  }

  public File getWar() {
    return war;
  }

  public void setWar(File war) {
    this.war = war;
  }

  public File getDeploy() {
    return deploy;
  }

  public void setDeploy(File deploy) {
    this.deploy = deploy;
  }

  public File getExtra() {
    return extra;
  }

  public void setExtra(File extra) {
    this.extra = extra;
  }

  public File getWorkDir() {
    return workDir;
  }

  public void setWorkDir(File workDir) {
    this.workDir = workDir;
  }

  public File getGen() {
    return gen;
  }

  public void setGen(File gen) {
    this.gen = gen;
  }

  public File getCacheDir() {
    return cacheDir;
  }

  public void setCacheDir(File cacheDir) {
    this.cacheDir = cacheDir;
  }
}
