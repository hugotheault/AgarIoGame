package sae.launch.agario.controllers;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import sae.launch.agario.models.Player;

public class PlayerController {

    @FXML
    private Circle playerCircle; //Graphic representation of the player
    private Player player;

    public void PlayerController(Player player){
        this.update(player);
    }

    /*
     * Update the coordinates of the playerCircle
     */
    public void update(Player player){
        this.player = player;
        playerCircle.setRadius(10*Math.sqrt(this.player.getMass()));
        playerCircle.setCenterY(this.player.getY());
        playerCircle.setCenterX(this.player.getX());
    }

}
