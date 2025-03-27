package sae.launch.agario.controllers;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sae.launch.agario.models.Effect;
import sae.launch.agario.models.SpecialPellet;

import java.util.Random;

public class SpecialPelletController {

    SpecialPellet specialPellet;
    @FXML
    private Circle specialPelletCircle;

    /**
     * Defines the graphic representation of a special pellet 
     * @see SpecialPellet
     * @param specialPellet Special pellet
     * @param width width of the game field
     * @param height height of the game field
     */
    public SpecialPelletController(SpecialPellet specialPellet, double width, double height){
        Random random = new Random();
        this.specialPellet = specialPellet;
        if( this.specialPellet.getEffect() != Effect.DIVISION){
            specialPelletCircle.setFill(Color.color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        } else {
            specialPelletCircle.setFill(Color.color(0,255,0));
        }
        specialPelletCircle.setCenterY(random.nextDouble(height));
        specialPelletCircle.setCenterX(random.nextDouble(width));
    }

}
