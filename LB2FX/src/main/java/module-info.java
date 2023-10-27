module com.example.lb2fx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.lb2fx to javafx.fxml;
    exports com.example.lb2fx;
}