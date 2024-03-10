module com.example.rapizzapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.rapizzapp to javafx.fxml;
    opens com.example.rapizzapp.login to javafx.fxml;
    exports com.example.rapizzapp.login to javafx.fxml;
    exports com.example.rapizzapp;

}