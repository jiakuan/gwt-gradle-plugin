package org.docstr.gwt;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ScanResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.List;
import java.util.logging.LogManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.io.TempDir;

@Slf4j
public abstract class AbstractGwtTest {

  static {
    try (InputStream stream =
        GwtPluginFunctionalTest.class.getResourceAsStream(
            "/logging.properties")) {
      LogManager.getLogManager().readConfiguration(stream);
    } catch (IOException ex) {
      log.error("Could not set up logging", ex);
    }
  }

  @TempDir
  File projectDir;

  protected void setupSampleProject() throws IOException {
    ClassGraph classGraph = new ClassGraph()
        .acceptPaths("/sample-gwt-project")
        .addClassLoader(getClass().getClassLoader())
        .acceptPackages("sample-gwt-project");
    try (ScanResult scanResult = classGraph.scan()) {
      List<Resource> resources = scanResult.getAllResources();
      for (Resource resource : resources) {
        String resourcePath = resource.getPath();
        if (containsIgnoreCase(resourcePath, ".idea")
            || containsIgnoreCase(resourcePath, "/.gradle/")
            || containsIgnoreCase(resourcePath, "/gradle/")
            || containsIgnoreCase(resourcePath, "gradlew")) {
          // log.warn("Skipping file: {}", resourcePath);
          continue;
        }
        String fileName = resourcePath.replace("sample-gwt-project/", "");

        File file = new File(projectDir, fileName);
        boolean mkdirs = file.getParentFile().mkdirs();
        if (mkdirs) {
          log.warn("Created directory: {}", file.getParentFile());
        }
        try (Writer writer = new FileWriter(file)) {
          writer.write(resource.getContentAsString());
          log.warn("Wrote file: {}", file);
        }
      }
    }
  }
}
