package sae.launch.agario.controllers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sae.launch.agario.models.PelletModel;

import java.util.Random;

public class PelletController {

    private Circle circle;
    private PelletModel pellet;

    /**
     *
     * @param pellet Pellet
     * @param width width of the game field
     * @param height height of the game field
     */
    public PelletController(PelletModel pellet, double width, double height) {
        Random random = new Random();
        this.pellet = pellet;
        this.circle = new Circle(random.nextDouble(height-2), random.nextDouble(width-2), pellet.getRadius(), Color.color(Math.random(), Math.random(), Math.random()));
    }

}
