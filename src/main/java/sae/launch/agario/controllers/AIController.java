package sae.launch.agario.controllers;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import sae.launch.agario.models.AI;

import java.util.Random;

public class AIController {

    private AI ai;
    @FXML
    private Circle AICircle;

    public AIController(AI ai, double width, double height){
        this.ai = ai;
        Random rand =new Random();
        AICircle.setCenterX(rand.nextDouble(width));
        AICircle.setCenterY(rand.nextDouble(height));
        AICircle.setRadius(10*Math.sqrt(ai.getMass()));
    }

}
