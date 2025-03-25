module sae.lauch.agario {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens sae.lauch.agario.views to javafx.fxml;
    exports sae.lauch.agario.models;
    opens sae.lauch.agario.models to javafx.fxml;
    exports sae.lauch.agario.controllers;
    opens sae.lauch.agario.controllers to javafx.fxml;
}