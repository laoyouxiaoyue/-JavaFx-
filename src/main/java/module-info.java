module com.example.fxtest {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.media;

    opens com.example.fxtest to javafx.fxml;
    exports com.example.fxtest;
}