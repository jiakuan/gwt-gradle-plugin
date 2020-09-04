package org.wisepersist.gradle.plugins.gwt;

import java.util.List;

/**
 * Defines the options known by the {@link AbstractGwtActionTask} task.
 */
public interface GwtJsInteropExportsOptions {

  boolean shouldGenerate();

  /**
   * Sets the "-generateJsInteropExport" flag that enables the generation
   * of JsInterop exports, disabled by default.
   *
   * @param shouldGenerate {@code true} if the "-generateJsInteropExport"
   * flag should be set, {@code false} otherwise to
   * set "-nogenerateJsInteropExports" flag
   *
   * @since <a href="http://www.gwtproject.org/release-notes.html#Release_Notes_2_8_0">GWT 2.8.0</a>
   */
  void setGenerate(final boolean shouldGenerate);

  List<String> getIncludePatterns();

  /**
   * Sets the members and classes to include while generating JsInterop
   * exports. Has only effect if {@linkplain #shouldGenerate() exporting}
   * is enabled.
   *
   * @param includePatterns the members and classes to include - adds
   * multiple "-includeJsInteropExports com.foo.*" flags
   *
   * @since <a href="http://www.gwtproject.org/release-notes.html#Release_Notes_2_8_1">GWT 2.8.1</a>
   */
  void setIncludePatterns(final String... includePatterns);

  List<String> getExcludePatterns();

  /**
   * Sets the members and classes to exclude while generating JsInterop
   * exports. Has only effect if {@linkplain #shouldGenerate() exporting}
   * is enabled and {@linkplain #getIncludePatterns()} is not empty.
   *
   * @param excludePatterns the members and classes to exclude - adds
   * multiple "-excludeJsInteropExports com.foo.internal.*" flags
   *
   * @since <a href="http://www.gwtproject.org/release-notes.html#Release_Notes_2_8_1">GWT 2.8.1</a>
   */
  void setExcludePatterns(final String... excludePatterns);
}
