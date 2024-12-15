package org.example.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../api/greet")
public interface GreeterService extends RemoteService {

	String getGreeting(String name);

}
