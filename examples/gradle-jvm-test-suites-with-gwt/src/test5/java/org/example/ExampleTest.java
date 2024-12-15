package org.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExampleTest {

	@Test
	void test() {
		assertThat("hello").startsWith("hell");
	}
}
