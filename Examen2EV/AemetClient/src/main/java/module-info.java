module es.aemet {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires java.net.http;

    opens es.aemet.model to com.fasterxml.jackson.databind;
    opens es.aemet.ui to javafx.fxml;

    exports es.aemet.ui;
    exports es.aemet.model;
    exports es.aemet.service;
}
