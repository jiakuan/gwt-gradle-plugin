package org.example.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Almost a record, but GWT serializable.
 */
public final class GreetingResponse implements IsSerializable {

	@SuppressWarnings("FieldMayBeFinal") // GWT serialization requires non-final
	private String greeting;

	@SuppressWarnings("unused") // GWT serialization requires zero-arg-ctor
	private GreetingResponse() {}

	public GreetingResponse(final String greeting) {
		this.greeting = greeting;
	}

	public String greeting() {
		return greeting;
	}
}
