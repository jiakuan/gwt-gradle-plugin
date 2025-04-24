package org.example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import org.example.client.ui.MyApp;

public final class MyEntryPoint implements EntryPoint {
	public void onModuleLoad() {
		RootPanel.get("app").add(new MyApp());
	}
}
