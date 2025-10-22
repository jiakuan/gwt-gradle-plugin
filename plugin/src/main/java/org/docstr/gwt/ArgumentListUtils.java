package org.docstr.gwt;

import lombok.experimental.UtilityClass;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Provider;

import java.util.Collection;

/**
 * Utility-Methods for mapping Lazy Task Properties to command line arguments for GWT.
 */
@UtilityClass
public class ArgumentListUtils {

  static void addStringArg(Collection<String> args, String argName, DirectoryProperty property) {

    if (property.isPresent()) {
      args.add("-" + argName);
      args.add(property.get().getAsFile().getAbsolutePath());
    }
  }

  static void addListArg(Collection<String> args, String argName, ListProperty<String> property) {

    if (property.isPresent()) {
      property.get().forEach(key -> {
        args.add("-" + argName);
        args.add(key);
      });
    }
  }

  static void addStringArg(Collection<String> args, String argName, Provider<?> property) {

    if (property.isPresent()) {
      args.add("-" + argName);
      args.add(property.get().toString());
    }
  }

  static void addBooleanArg(Collection<String> args, String argName, Provider<Boolean> property) {

    if (property.isPresent()) {
      if (property.get()) {
        args.add("-" + argName);
      }
      else {
        if (argName.startsWith("X")) {
          args.add("-Xno" + argName.substring(1));
        }
        else {
          args.add("-no" + argName);
        }
      }

    }
  }
}
