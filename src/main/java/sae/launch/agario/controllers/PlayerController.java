package sae.launch.agario.controllers;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import sae.launch.agario.models.PlayerModel;
public class PlayerController {

    @FXML
    private Circle playerCircle;
    private PlayerModel player;

    public void PlayerController(PlayerModel player){
        this.update(player);
    }

    public void update(PlayerModel player){
        this.player = player;
        playerCircle.setRadius(10*Math.sqrt(this.player.getMass()));
        playerCircle.setCenterY(this.player.getY());
        playerCircle.setCenterX(this.player.getX());
    }

}
