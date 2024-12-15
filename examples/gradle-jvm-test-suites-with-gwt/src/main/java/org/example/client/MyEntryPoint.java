package org.example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.example.client.rpc.GreeterService;
import org.example.client.rpc.GreeterServiceAsync;

public class MyEntryPoint implements EntryPoint {
	public void onModuleLoad() {
		GreeterServiceAsync greeter = GWT.create(GreeterService.class);

		greeter.getGreeting("John", new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Failed to greet: " + caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				Window.alert(result);
			}
		});
	}
}
