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
package org.docstr.gwt;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import lombok.extern.slf4j.Slf4j;
import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Configures the GWT compiler task.
 */
@Slf4j
public class GwtCompileConfig implements Action<GwtCompileTask> {

  private final GwtPluginExtension extension;
  private final DocumentBuilderFactory dbFactory;
  private final DocumentBuilder dBuilder;

  /**
   * Constructor.
   *
   * @param extension The GWT plugin extension
   */
  public GwtCompileConfig(GwtPluginExtension extension) {
    this.extension = extension;

    try {
      dbFactory = DocumentBuilderFactory.newInstance();
      dBuilder = dbFactory.newDocumentBuilder();

      // Create a fix for DTD loading. Google provides an http: -> https redirect on DTDs, however
      // Http(s)URLConnection will not follow across protocols. It will follow within the same protocol.
      // Therefore, markup all DTD URLs to https proactively.
      dBuilder.setEntityResolver((publicId, systemId) -> {
        try {
          return new InputSource(new URI(systemId.replace("http://", "https://")).toURL().openStream());
        } catch (URISyntaxException e) {
            throw new IOException("Unable to change DTD URL", e);
        }
      });
    } catch (FactoryConfigurationError | ParserConfigurationException e) {
      throw new GradleException("Unable to create XML parser", e);
    }
  }

  /**
   * Locate all GWT modules in the source set (both src/main/java and src/main/resources).
   * @param project The project being operated on
   * @return A set of GWT modules found within the sources (both src/main/(java|kotlin|...) and src/main/resources)
   */
  Collection<Module> findAllModules(Project project) {
    SourceSetContainer sourceSets = project.getExtensions()
            .getByType(SourceSetContainer.class);
    SourceSet mainSourceSet = sourceSets.getByName(
            SourceSet.MAIN_SOURCE_SET_NAME);

    // Need to scan both java and resources as GWT can appear in both.
    Set<Module> modules = new HashSet<>();
    mainSourceSet.getAllSource()
            .matching(pf -> pf.include("**/*.gwt.xml"))
            .visit(v -> {
              if (v.getRelativePath().isFile()) {
                var module = new Module(
                        v.getFile().toPath(),
                        Path.of(v.getRelativePath().getPathString()),
                        mainSourceSet
                );
                modules.add(module);
                log.debug("Found module: {}",
                          module.name());
              }
            });

    return modules;
  }

  /**
   * 1. Add the module path to the source paths.
   * 2. Add the package directory for each entry-point to the source paths.
   * 3. Extract the source paths from the GWT module XML path.
   * 4. Extract the public paths from the GWT module XML path.
   * <br><br>
   * As mentioned in <a href="https://www.gwtproject.org/doc/latest/DevGuideOrganizingProjects.html">https://www.gwtproject.org/doc/latest/DevGuideOrganizingProjects.html</a>
   * if no source or public element is defined in a module XML path, the
   * client and public subpackage is implicitly added to the source/public
   * path as if <source path="client" /> or <public path="public"> had been
   * found in the XML.
   */
  TreeSet<Path> extractSourcePaths(Module module) {
    TreeSet<Path> sourcePaths = new TreeSet<>();
    sourcePaths.add(module.path);
    try (InputStream moduleInputStream = Files.newInputStream(module.path)) {
      // Using InputStream to allow character set detection by the XML declaration.
      var doc = dBuilder.parse(new InputSource(moduleInputStream));
      var moduleParent = module.path.getParent();
      var sourceRoot = module.sourceDirectorySetRootPath();

      // Add the package directory for each entry-point to the source paths
      NodeList entryPointNodes = doc.getElementsByTagName("entry-point");
      for (int i = 0; i < entryPointNodes.getLength(); i++) {
        Element entryPointElement = (Element) entryPointNodes.item(i);
        String path = entryPointElement.getAttribute("class");
        String packageName = path.substring(0, path.lastIndexOf('.'));
        String packagePath = packageName.replace('.', '/');
        var packageDir = sourceRoot.resolve(packagePath);
        sourcePaths.add(packageDir);
      }

      // Extract the source paths from the GWT module XML path
      NodeList sourceNodes = doc.getElementsByTagName("source");
      if (sourceNodes.getLength() == 0) {
        var defaultClientDir = moduleParent.resolve("client");
        if (Files.isDirectory(defaultClientDir)) {
          sourcePaths.add(defaultClientDir);
        }
      } else {
        for (int i = 0; i < sourceNodes.getLength(); i++) {
          String path = sourceNodes.item(i).getAttributes().getNamedItem("path")
              .getNodeValue();
          var sourceDir = moduleParent.resolve(path);
          sourcePaths.add(sourceDir);
        }
      }

      // Extract the public paths from the GWT module XML path
      NodeList publicNodes = doc.getElementsByTagName("public");
      if (publicNodes.getLength() == 0) {
        var defaultPublicDir = moduleParent.resolve("public");
        if (Files.isDirectory(defaultPublicDir)) {
          sourcePaths.add(defaultPublicDir);
        }
      } else {
        for (int i = 0; i < publicNodes.getLength(); i++) {
          String path = publicNodes.item(i).getAttributes().getNamedItem("path")
              .getNodeValue();
          var publicDir = moduleParent.resolve(path);
          sourcePaths.add(publicDir);
        }
      }
    } catch (Exception e) {
      log.error("Error reading GWT module path: '{}'", module.path, e);
    }
    return sourcePaths;
  }

  @Override
  public void execute(GwtCompileTask task) {
    Project project = task.getProject();
    Logger log = project.getLogger();

    // Process each GWT module to find source paths
    Set<Path> trackedPaths = new HashSet<>();
    var allModules = findAllModules(project);
    var moduleNames = extension.getModules().get();

    for (var module: allModules) {
      if (!moduleNames.contains(module.name())) {
        continue;
      }
      log.info("gwtCompile - Processing GWT module: '{}' ({})", module.name(), module.path);
      trackedPaths.addAll(extractSourcePaths(module));
    }

    // Create a path collection for tracking
    FileCollection trackedFiles = project.files(trackedPaths.toArray());
    log.info("gwtCompile - Tracking GWT source files: {}",
        trackedFiles.getFiles());
    task.getInputs().files(trackedFiles);

    if (extension.getCompiler().getMinHeapSize().isPresent()) {
      task.setMinHeapSize(extension.getCompiler().getMinHeapSize().get());
    } else {
      task.setMinHeapSize(extension.getMinHeapSize().getOrElse("256M"));
    }
    if (extension.getCompiler().getMaxHeapSize().isPresent()) {
      task.setMaxHeapSize(extension.getCompiler().getMaxHeapSize().get());
    } else {
      task.setMaxHeapSize(extension.getMaxHeapSize().getOrElse("512M"));
    }

    if (extension.getCompiler().getLogLevel().isPresent()) {
      task.getLogLevel().set(extension.getCompiler().getLogLevel().get());
    } else {
      task.getLogLevel().set(extension.getLogLevel().getOrNull());
    }
    if (extension.getCompiler().getWorkDir().isPresent()) {
      task.getWorkDir().set(extension.getCompiler().getWorkDir().get());
    } else {
      task.getWorkDir().set(extension.getWorkDir().getOrNull());
    }
    task.getClosureFormattedOutput()
        .set(extension.getCompiler().getClosureFormattedOutput());
    task.getCompileReport().set(extension.getCompiler().getCompileReport());
    task.getStrict().set(extension.getCompiler().getStrict());
    task.getClassMetadata().set(extension.getCompiler().getClassMetadata());
    task.getDraftCompile().set(extension.getCompiler().getDraftCompile());
    task.getCheckAssertions().set(extension.getCompiler().getCheckAssertions());
    task.getFragmentCount().set(extension.getCompiler().getFragmentCount());
    if (extension.getCompiler().getGen().isPresent()) {
      task.getGen().set(extension.getCompiler().getGen().get());
    } else {
      task.getGen().set(extension.getGen().getOrNull());
    }
    if (extension.getCompiler().getGenerateJsInteropExports().isPresent()) {
      task.getGenerateJsInteropExports()
          .set(extension.getCompiler().getGenerateJsInteropExports().get());
    } else {
      task.getGenerateJsInteropExports()
          .set(extension.getGenerateJsInteropExports().getOrNull());
    }
    if (extension.getCompiler().getIncludeJsInteropExports().isPresent()
        && !extension.getCompiler().getIncludeJsInteropExports().get()
        .isEmpty()) {
      task.getIncludeJsInteropExports()
          .set(extension.getCompiler().getIncludeJsInteropExports().get());
    } else {
      task.getIncludeJsInteropExports()
          .set(extension.getIncludeJsInteropExports().getOrNull());
    }
    if (extension.getCompiler().getExcludeJsInteropExports().isPresent()
        && !extension.getCompiler().getExcludeJsInteropExports().get()
        .isEmpty()) {
      task.getExcludeJsInteropExports()
          .set(extension.getCompiler().getExcludeJsInteropExports().get());
    } else {
      task.getExcludeJsInteropExports()
          .set(extension.getExcludeJsInteropExports().getOrNull());
    }
    if (extension.getCompiler().getMethodNameDisplayMode().isPresent()) {
      task.getMethodNameDisplayMode()
          .set(extension.getCompiler().getMethodNameDisplayMode().get());
    } else {
      task.getMethodNameDisplayMode()
          .set(extension.getMethodNameDisplayMode().getOrNull());
    }
    task.getNamespace().set(extension.getCompiler().getNamespace().getOrNull());
    task.getOptimize().set(extension.getCompiler().getOptimize().getOrNull());
    task.getSaveSource()
        .set(extension.getCompiler().getSaveSource().getOrNull());
    if (extension.getCompiler().getSetProperty().isPresent()
        && !extension.getCompiler().getSetProperty().get().isEmpty()) {
      task.getSetProperty().set(extension.getCompiler().getSetProperty().get());
    } else {
      task.getSetProperty().set(extension.getSetProperty().getOrNull());
    }
    if (extension.getCompiler().getStyle().isPresent()) {
      task.getStyle().set(extension.getCompiler().getStyle().get());
    } else {
      task.getStyle().set(extension.getStyle().getOrNull());
    }
    if (extension.getCompiler().getFailOnError().isPresent()) {
      task.getFailOnError().set(extension.getCompiler().getFailOnError().get());
    } else {
      task.getFailOnError().set(extension.getFailOnError().getOrNull());
    }
    task.getValidateOnly()
        .set(extension.getCompiler().getValidateOnly().getOrNull());
    if (extension.getCompiler().getSourceLevel().isPresent()) {
      task.getSourceLevel().set(extension.getCompiler().getSourceLevel().get());
    } else {
      task.getSourceLevel().set(extension.getSourceLevel().getOrNull());
    }
    task.getLocalWorkers()
        .set(extension.getCompiler().getLocalWorkers().getOrNull());
    if (extension.getCompiler().getIncremental().isPresent()) {
      task.getIncremental().set(extension.getCompiler().getIncremental().get());
    } else {
      task.getIncremental().set(extension.getIncremental().getOrNull());
    }
    if (extension.getCompiler().getWar().isPresent()) {
      task.getWar().set(extension.getCompiler().getWar().get());
    } else {
      task.getWar().set(extension.getWar().getOrNull());
    }
    if (extension.getCompiler().getDeploy().isPresent()) {
      task.getDeploy().set(extension.getCompiler().getDeploy().get());
    } else {
      task.getDeploy().set(extension.getDeploy().getOrNull());
    }
    if (extension.getCompiler().getExtra().isPresent()) {
      task.getExtra().set(extension.getCompiler().getExtra().get());
    } else {
      task.getExtra().set(extension.getExtra().getOrNull());
    }
    if (extension.getCompiler().getCacheDir().isPresent()) {
      task.getCacheDir().set(extension.getCompiler().getCacheDir().get());
    } else {
      task.getCacheDir().set(extension.getCacheDir().getOrNull());
    }
    task.getSaveSourceOutput()
        .set(extension.getCompiler().getSaveSourceOutput().getOrNull());
    if (extension.getCompiler().getModules().isPresent()
        && !extension.getCompiler().getModules().get().isEmpty()) {
      task.getModules().set(extension.getCompiler().getModules().get());
    } else {
      task.getModules().set(extension.getModules().get());
    }
    
    // Set extra source directories if specified
    if (!extension.getCompiler().getExtraSourceDirs().isEmpty()) {
      task.getExtraSourceDirs().from(extension.getCompiler().getExtraSourceDirs());
    } else if (!extension.getExtraSourceDirs().isEmpty()) {
      task.getExtraSourceDirs().from(extension.getExtraSourceDirs());
    }

    // Check if the modules property is specified
    if (task.getModules().get().isEmpty()) {
      throw new GradleException(
          "gwtCompile failed: 'modules' property is required. Please specify at least one GWT module in the gwt { ... } block.");
    }

    // Configure classpath and arguments during configuration phase for Configuration Cache compatibility
    task.configureClasspath(project);
    task.configureArgs();
    task.configureCompileArgs();
  }

  /**
   * A representation of a path.
   */
  record Module(Path path, Path relativePath, SourceSet sourceSet) {
    String name() {
      return relativePath.toString()
              .replace("/", ".")
              .replace("\\", ".")
              .replace(".gwt.xml", "");
    }
    Path sourceDirectorySetRootPath() {
      // Sanity check. Should always be the case as both objects came from the source set
      if (!path.endsWith(relativePath)) {
        throw new GradleException("Invalid module. Path '" + path + "' does not end with relative part '" + relativePath +"'");
      }
      var rootPath = path.getRoot().resolve(path.subpath(0, path.getNameCount() - relativePath().getNameCount()));
      var rootFile = rootPath.toFile();

      // Check the root path is in the list of source directories for the SourceSet
      if (!sourceSet.getAllSource().getSrcDirs().contains(rootFile)) {
        throw new GradleException("Invalid module. Root path '" + rootPath + "' is not a valid source set root");
      }
      return rootPath;
    }
  }
}
