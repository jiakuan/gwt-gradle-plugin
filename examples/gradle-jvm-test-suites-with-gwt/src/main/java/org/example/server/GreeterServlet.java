package org.example.server;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.example.client.rpc.GreeterService;

public class GreeterServlet extends RemoteServiceServlet implements GreeterService {

	@Override
	public String getGreeting(String name) {
		if(name == null)
			return "Hello, Stranger!";
		name = SafeHtmlUtils.fromString(name).asString();
		return "Hello, " + name + "!";
	}

}
