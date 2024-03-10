module com.example.rapizzapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.rapizzapp to javafx.fxml;
    opens com.example.rapizzapp.controllers to javafx.fxml;
    exports com.example.rapizzapp.controllers to javafx.fxml;
    exports com.example.rapizzapp;

}