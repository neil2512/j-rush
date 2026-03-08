module jrush {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;
  requires contract;

  exports jrush;
  exports jrush.gui;
  exports jrush.util;
  exports jrush.model;

  opens jrush.gui to javafx.fxml;
}