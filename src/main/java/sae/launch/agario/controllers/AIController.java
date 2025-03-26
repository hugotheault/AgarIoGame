package sae.launch.agario.controllers;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import sae.launch.agario.models.AI;

import java.util.Random;

public class AIController {

    private AI ai;
    @FXML
    private Circle aiCircle;

    public AIController(AI ai, double width, double height){
        this.ai = ai;
        Random rand =new Random();
        aiCircle.setCenterX(rand.nextDouble(width));
        aiCircle.setCenterY(rand.nextDouble(height));
        aiCircle.setRadius(this.ai.getRadius());
    }

}
