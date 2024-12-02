package com.example.client;

import com.google.gwt.user.client.ui.Label;

public class UpperCasingLabel extends Label {

  public UpperCasingLabel() {
    super();
  }

  public UpperCasingLabel(String text) {
    super(text);
  }

  @Override
  public void setText(String text) {
    super.setText(text.toUpperCase());
  }
}
