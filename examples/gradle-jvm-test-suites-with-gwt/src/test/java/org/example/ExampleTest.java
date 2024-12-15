package org.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExampleTest {

	@Mock
	Foo foo;

	@Test
	public void test() {
		when(foo.greet()).thenReturn("Hello");

		assertThat(foo.greet(), is(equalTo("Hello")));
	}

	private interface Foo {
		String greet();
	}
}
