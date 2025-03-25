package sae.launch.agario.controllers;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sae.launch.agario.models.PelletModel;

import java.util.Random;

public class PelletController {

    private PelletModel pellet;

    @FXML
    private Circle pelletCircle;

    /**
     *
     * @param pellet Pellet
     * @param width width of the game field
     * @param height height of the game field
     */
    public PelletController(PelletModel pellet, double width, double height) {
        Random random = new Random();
        this.pellet = pellet;
        pelletCircle.setCenterX(random.nextDouble(width));
        pelletCircle.setCenterY(random.nextDouble(height));
        pelletCircle.setRadius(10*Math.sqrt(pellet.getMass()));
    }

}
