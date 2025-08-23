package com.example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.example.shared.SharedClass;
import com.example.extra.ExtraSourceClass;

public class MyEntryPoint implements EntryPoint {
    @Override
    public void onModuleLoad() {
        Label label = new Label("Hello from main source: " + SharedClass.getMessage());
        RootPanel.get().add(label);
        
        Label extraLabel = new Label("Hello from extra source: " + ExtraSourceClass.getExtraMessage());
        RootPanel.get().add(extraLabel);
    }
}