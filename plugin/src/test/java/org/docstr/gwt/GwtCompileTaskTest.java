package org.docstr.gwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.StreamSupport;
import org.gradle.api.Project;
import org.gradle.process.CommandLineArgumentProvider;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

class GwtCompileTaskTest {

  @Test
  void passesLocalWorkersBeforeModuleNamesWithoutAddingToStaticArgs() {
    Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply("java");
    GwtCompileTask task = project.getTasks().create("gwtCompile", GwtCompileTask.class);
    task.getModules().set(List.of("com.example.MyModule"));
    task.getLocalWorkers().set(2);
    task.configureCompileArgs();

    assertThat(task.getArgs()).doesNotContain("-localWorkers", "2");
    assertThat(task.getArgumentProviders().stream()
        .flatMap(provider -> StreamSupport.stream(provider.asArguments().spliterator(), false))
        .toList())
        .containsExactly("-localWorkers", "2", "com.example.MyModule");
  }

  @Test
  void workerArgumentsFollowThePropertyAndAreOptional() {
    Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply("java");
    GwtCompileTask task = project.getTasks().create("gwtCompile", GwtCompileTask.class);
    CommandLineArgumentProvider workerArguments = task.getArgumentProviders().get(0);

    assertThat(workerArguments.asArguments()).isEmpty();
    task.getLocalWorkers().set(1);
    assertThat(workerArguments.asArguments()).containsExactly("-localWorkers", "1");
    task.getLocalWorkers().set(2);
    assertThat(workerArguments.asArguments()).containsExactly("-localWorkers", "2");
  }
}
