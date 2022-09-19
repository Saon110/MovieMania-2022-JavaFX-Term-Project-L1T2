module myjfx {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    opens client to javafx.graphics, javafx.fxml;
    opens controllers to javafx.fxml, javafx.graphics;
}