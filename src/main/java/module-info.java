module jrush {
  requires javafx.controls;
  requires java.desktop;
  requires javafx.fxml;
  requires contract;

  exports jrush;
  exports jrush.gui;
  exports jrush.util;
  exports jrush.model;

  opens jrush.gui to javafx.fxml;
  exports jrush.model.components;
  exports jrush.model.logic;
}