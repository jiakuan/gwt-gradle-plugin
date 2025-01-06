package org.example.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.example.client.dto.GreetingRequest;
import org.example.client.dto.GreetingResponse;

public interface GreeterServiceAsync {

	void getGreeting(final GreetingRequest request, final AsyncCallback<GreetingResponse> callback);

}
