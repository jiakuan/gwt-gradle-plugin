package org.example.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.example.client.dto.GreetingRequest;
import org.example.client.dto.GreetingResponse;
import org.example.client.rpc.GreeterService;
import org.example.client.rpc.GreeterServiceAsync;

public final class MyApp extends Composite {

	private final GreeterServiceAsync greeter = GWT.create(GreeterService.class);

	private final Label label = new Label("Who should I greet?");
	private final Button button = new Button("Greet");

	// The input from the client to server
	private final TextBox name = new TextBox();
	// The output from server to client
	private final Label greeting = new Label("");


	public MyApp() {
		name.setMaxLength(50);
		name.setFocus(true);
		name.addKeyUpHandler(event -> {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				buttonClicked();
			}
		});

		button.addClickHandler(ignored -> buttonClicked());

		final var panel = new VerticalPanel();
		panel.setSpacing(10);
		panel.add(label);
		panel.add(name);
		panel.add(button);
		panel.add(greeting);

		initWidget(panel);
	}

	private void buttonClicked() {
		final var request = new GreetingRequest(name.getText());

		greeter.getGreeting(request, AsyncCallbackBuilder
				.onSuccess(this::greetingReceived)
				.onFailure(this::greetingFailed));

		name.setFocus(true);
		name.selectAll();
	}

	private void greetingReceived(final GreetingResponse response) {
		greeting.setText(response.greeting());
	}

	private void greetingFailed(final Throwable caught) {
		Window.alert("Failed to greet: " + caught.getMessage());
		greeting.setText("");
	}
}
