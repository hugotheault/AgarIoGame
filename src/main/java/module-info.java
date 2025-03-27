module sae.launch.agario {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.annotation;

    opens sae.launch.agario.models to javafx.fxml;
    exports sae.launch.agario.models;
    opens sae.launch.agario.controllers to javafx.fxml;
    exports sae.launch.agario.controllers;
    exports sae.launch.agario.models.serverFiles;
    opens sae.launch.agario.models.serverFiles to javafx.fxml;
    exports sae.launch.agario;
}