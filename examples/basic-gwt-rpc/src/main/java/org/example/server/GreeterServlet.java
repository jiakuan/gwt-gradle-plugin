package org.example.server;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet; // Jakarta (requires newer Tomcat)
//import com.google.gwt.user.server.rpc.RemoteServiceServlet; // legacy (requires older Tomcat)
import org.example.client.dto.GreetingRequest;
import org.example.client.dto.GreetingResponse;
import org.example.client.rpc.GreeterService;

public final class GreeterServlet extends RemoteServiceServlet implements GreeterService {

	@Override
	public GreetingResponse getGreeting(final GreetingRequest request) {
		if(request == null || request.name() == null || request.name().length() > 50) {
			// Our client should have handled all these conditions,
			// so this means someone is invoking the API to hack it.
			// E.g., from command line.
			throw new RuntimeException("Validation failed");
		}

		final var name = request.name().isBlank()
				? "Stranger"
				: SafeHtmlUtils.fromString(request.name().strip()).asString();

		return new GreetingResponse("Hello, " + name);
	}

}
