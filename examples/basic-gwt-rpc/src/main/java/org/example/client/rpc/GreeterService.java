package org.example.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.example.client.dto.GreetingRequest;
import org.example.client.dto.GreetingResponse;

@RemoteServiceRelativePath("../api/greet")
public interface GreeterService extends RemoteService {

	GreetingResponse getGreeting(final GreetingRequest request);

}
