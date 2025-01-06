package org.example.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Almost a record, but GWT serializable.
 */
public final class GreetingRequest implements IsSerializable {

	@SuppressWarnings("FieldMayBeFinal") // GWT serialization requires non-final
	private String name;

	@SuppressWarnings("unused") // GWT serialization requires zero-arg-ctor
	private GreetingRequest() {}

	public GreetingRequest(final String name) {
		this.name = name;
	}

	public String name() {
		return name;
	}
}
