package org.example;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(GwtMockitoTestRunner.class)
public class ExampleTest {

	@Test
	public void test() {
		assertThat("hello", is(equalTo("hello")));
	}
}
