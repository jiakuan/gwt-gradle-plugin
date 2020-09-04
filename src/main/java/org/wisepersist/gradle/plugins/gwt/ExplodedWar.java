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
import java.util.Collections;
import java.util.concurrent.Callable;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.CopySpec;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

public class ExplodedWar extends DefaultTask {

  private File destinationDir;
  private File webXml;

  private FileCollection classpath;

  private final CopySpec root;
  private final CopySpec webInf;

  public ExplodedWar() {
    root = getProject().copySpec(spec -> {
    });

    webInf = root.into("WEB-INF", spec -> {
    });

    webInf.into("", spec -> spec.from(
        (Callable<File>) () -> getWebXml()).rename(".*", "web.xml"));

    webInf.into("classes",
        spec -> spec.from(
            (Callable<Iterable<File>>) () -> {
              final FileCollection classpath = getClasspath();
              return classpath == null ? Collections.emptyList()
                  : classpath.filter(file -> file.isDirectory());
            }));
    webInf.into("lib", spec -> spec.from(
        (Callable<Iterable<File>>) () -> {
          final FileCollection classpath = getClasspath();
          return classpath == null ? Collections.emptyList()
              : classpath.filter(file -> file.isFile());
        }));
  }

  @TaskAction
  protected void buildWarTemplate() {
    getProject().copy(spec -> {
      spec.into(getDestinationDir());
      spec.with(root);
    });
  }

  @InputFiles
  @Optional
  public FileCollection getClasspath() {
    return classpath;
  }

  public void setClasspath(FileCollection classpath) {
    this.classpath = classpath;
  }

  public void classpath(Object... classpath) {
    FileCollection oldClasspath = getClasspath();
    this.classpath = oldClasspath == null ? getProject().files(classpath)
        : getProject().files(this.classpath, classpath);
  }

  @InputFile
  @Optional
  public File getWebXml() {
    return webXml;
  }

  public void setWebXml(File webXml) {
    this.webXml = webXml;
  }

  @OutputDirectory
  public File getDestinationDir() {
    return destinationDir;
  }

  public void setDestinationDir(File destinationDir) {
    this.destinationDir = destinationDir;
  }

  public CopySpec from(Object... input) {
    return root.from(input);
  }
}
