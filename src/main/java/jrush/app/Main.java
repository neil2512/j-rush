package jrush.app;

import javafx.application.Application;
import javafx.stage.Stage;
import jrush.app.gui.ViewNavigator;

/**
 * Point d'entrée de l'application JRush.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ViewNavigator navigator = new ViewNavigator(primaryStage);
        navigator.showHome();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
