package org.example.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GreeterServiceAsync {

	void getGreeting(String name, AsyncCallback<String> callback);

}
