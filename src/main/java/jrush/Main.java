package jrush;

import javafx.application.Application;
import javafx.stage.Stage;
import jrush.gui.TestView;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) {
    new TestView(primaryStage).display();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
