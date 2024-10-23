package com.example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;

public class MyEntryPoint implements EntryPoint {

  public void sayHello() {
    Window.alert("Hello, GWT!");
  }

  public void onModuleLoad() {
    sayHello();
  }
}
