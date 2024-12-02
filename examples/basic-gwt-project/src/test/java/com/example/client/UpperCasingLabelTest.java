package com.example.client;

import com.google.gwt.junit.client.GWTTestCase;

public class UpperCasingLabelTest extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "com.example.MyModule";
  }

  public void testUpperCasingLabel() {
    UpperCasingLabel upperCasingLabel = new UpperCasingLabel();

    upperCasingLabel.setText("foo");
    assertEquals("FOO", upperCasingLabel.getText());

    upperCasingLabel.setText("BAR");
    assertEquals("BAR", upperCasingLabel.getText());

    upperCasingLabel.setText("BaZ");
    assertEquals("BAZ", upperCasingLabel.getText());
  }
}
