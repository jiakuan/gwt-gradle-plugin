package org.wisepersist.gradle.plugins.gwt.internal;

import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.wisepersist.gradle.plugins.gwt.GwtJsInteropExportsOptions;

public class GwtJsInteropExportsOptionsImpl implements GwtJsInteropExportsOptions {

	private boolean shouldGenerate;

	private List<String> includePatterns;

	private List<String> excludePatterns;

	public GwtJsInteropExportsOptionsImpl() {
		this.shouldGenerate = false;
		this.includePatterns = new ArrayList<>();
		this.excludePatterns = new ArrayList<>();
	}

	@Override
	public boolean shouldGenerate() {
		return shouldGenerate;
	}

	@Override
	public void setGenerate(final boolean shouldGenerate) {
		this.shouldGenerate = shouldGenerate;
	}

	@Override
	public List<String> getIncludePatterns() {
		return unmodifiableList(includePatterns);
	}

	@Override
	public void setIncludePatterns(final String... includePatterns) {
		this.includePatterns = toStream(includePatterns)
				.filter(Objects::nonNull)
				.collect(toList());
	}

	@Override
	public List<String> getExcludePatterns() {
		return unmodifiableList(excludePatterns);
	}

	@Override
	public void setExcludePatterns(final String... excludePatterns) {
		this.excludePatterns = toStream(excludePatterns)
				.filter(Objects::nonNull)
				.collect(toList());
	}

	private static Stream<String> toStream(final String... patterns) {
		return (patterns != null) ? stream(patterns) : Stream.empty();
	}
}
