module jrush.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens jrush.app to javafx.fxml;
    exports jrush.app;
}