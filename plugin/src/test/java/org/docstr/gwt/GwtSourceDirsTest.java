package org.docstr.gwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.gradle.api.Project;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Verifies GWT-only source configuration for all execution modes.
 */
class GwtSourceDirsTest {

  @Test
  void addsOnlyGlobalSourcesToGwtTestsAndRetainsGeneratorDependencies() {
    Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply("org.docstr.gwt");
    GwtPluginExtension extension = project.getExtensions().getByType(GwtPluginExtension.class);
    var generator = project.getTasks().register("generateGwtSources");
    extension.getGwtSourceDirs().from(project.files("generated-sources").builtBy(generator));
    extension.getCompiler().getGwtSourceDirs().from(project.file("compiler-sources"));
    extension.getDevMode().getGwtSourceDirs().from(project.file("dev-sources"));
    extension.getSuperDev().getGwtSourceDirs().from(project.file("superdev-sources"));

    var task = (org.gradle.api.tasks.testing.Test) project.getTasks().getByName("test");
    new GwtTestConfig(project, extension).execute(task);
    assertThat(task.getClasspath().getFiles())
        .contains(project.file("generated-sources"))
        .doesNotContain(project.file("compiler-sources"), project.file("dev-sources"),
            project.file("superdev-sources"));
    assertThat(task.getTaskDependencies().getDependencies(task))
        .anyMatch(dependency -> dependency.equals(generator.get()));
    SourceSetContainer sourceSets = project.getExtensions().getByType(SourceSetContainer.class);
    for (String sourceSet : List.of("main", "test")) {
      assertThat(sourceSets.getByName(sourceSet).getJava().getSrcDirs())
          .doesNotContain(project.file("generated-sources"));
    }
  }

  @ParameterizedTest
  @ValueSource(strings = {"gwtCompile", "gwtDevMode", "gwtSuperDev"})
  void combinesGlobalAndModeSpecificSourcesWithoutChangingJavaSources(String taskName) {
    Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply("org.docstr.gwt");
    GwtPluginExtension extension = project.getExtensions().getByType(GwtPluginExtension.class);
    extension.getModules().set(List.of("com.example.MyModule"));
    extension.getGwtSourceDirs().from(project.file("shared-sources"));
    extension.getExtraSourceDirs().from(project.file("extra-sources"));
    extension.getCompiler().getGwtSourceDirs().from(project.file("gwtCompile-sources"));
    extension.getDevMode().getGwtSourceDirs().from(project.file("gwtDevMode-sources"));
    extension.getSuperDev().getGwtSourceDirs().from(project.file("gwtSuperDev-sources"));

    AbstractBaseTask task = (AbstractBaseTask) project.getTasks().getByName(taskName);
    assertThat(task.getGwtSourceDirs().getFiles()).containsExactlyInAnyOrder(
        project.file("shared-sources"), project.file(taskName + "-sources"));
    ((ProjectInternal) project).evaluate();
    SourceSetContainer sourceSets = project.getExtensions().getByType(SourceSetContainer.class);
    assertThat(sourceSets.getByName("main").getJava().getSrcDirs())
        .contains(project.file("extra-sources"))
        .doesNotContain(project.file("shared-sources"), project.file("gwtCompile-sources"),
            project.file("gwtDevMode-sources"), project.file("gwtSuperDev-sources"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"gwtCompile", "gwtDevMode", "gwtSuperDev"})
  void retainsDependenciesOfGeneratedSourceDirectories(String taskName) {
    Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply("org.docstr.gwt");
    GwtPluginExtension extension = project.getExtensions().getByType(GwtPluginExtension.class);
    extension.getModules().set(List.of("com.example.MyModule"));
    var generator = project.getTasks().register("generateGwtSources");
    extension.getGwtSourceDirs().from(project.files("generated-sources").builtBy(generator));

    AbstractBaseTask task = (AbstractBaseTask) project.getTasks().getByName(taskName);
    assertThat(task.getTaskDependencies().getDependencies(task))
        .anyMatch(dependency -> dependency.equals(generator.get()));
  }
}
