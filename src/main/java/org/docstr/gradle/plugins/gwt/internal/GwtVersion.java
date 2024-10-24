package org.docstr.gradle.plugins.gwt.internal;

import static java.lang.String.format;

/**
 * A representation of a parsed GWT version string.
 */
public final class GwtVersion {

  private static final String PARSING_ERROR_MESSAGE_FORMAT =
      "GWT version %s can not be parsed. Valid versions must have "
          + "the format major.minor.patch (where major and minor "
          + "are positive integer numbers) or HEAD-* where * is an arbitrary string.";

  private final int major;
  private final int minor;
  private final String patch;
  private final String name;

  private GwtVersion(final int major, final int minor, final String patch) {
    this.major = major;
    this.minor = minor;
    this.patch = patch;
    this.name = null;
  }

  private GwtVersion(String name) {
    this.name = name;
    this.major = Integer.MAX_VALUE;
    this.minor = Integer.MAX_VALUE;
    this.patch = "";
  }
  /**
   * @param gwtVersion the gwt version string to be parsed
   * @return a newly created {@linkplain GwtVersion} instance representing
   * the parsed GWT version string argument. null if gwtVersion is null or empty.
   * @throws IllegalArgumentException if any of the passed arguments are invalid
   */
  public static GwtVersion parse(final String gwtVersion) {
    if (gwtVersion == null || gwtVersion.trim().isEmpty()) {
        return null;
    }
    try {
      final String[] versionParts = gwtVersion.split("[-\\.]", 3);
      if (versionParts.length > 0 && "HEAD".equals(versionParts[0])) {
        return new GwtVersion(gwtVersion);
      }
      if (versionParts.length >= 3) {
        return new GwtVersion(Integer.parseUnsignedInt(versionParts[0]),
            Integer.parseUnsignedInt(versionParts[1]),
            versionParts[2]);
      } else if (versionParts.length >= 2) {
        return new GwtVersion(Integer.parseUnsignedInt(versionParts[0]),
            Integer.parseUnsignedInt(versionParts[1]),
            "0");
      }
    } catch (final NumberFormatException e) {
      throw new IllegalArgumentException(
          format(PARSING_ERROR_MESSAGE_FORMAT, gwtVersion), e);
    }
    throw new IllegalArgumentException(format(PARSING_ERROR_MESSAGE_FORMAT, gwtVersion));
  }

  public int getMajor() {
    return major;
  }

  public int getMinor() {
    return minor;
  }

  public String getPatch() {
    return patch;
  }

  /**
   * @return the original parsed string
   */
  @Override
  public String toString() {
    return name == null ? format("%d.%d.%s", major, minor, patch) : name;
  }

  public boolean isAtLeast(int majorMin, int minorMin) {
    return major > majorMin || (major == majorMin && minor >= minorMin);
  }
}
