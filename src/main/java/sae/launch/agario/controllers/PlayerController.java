package sae.launch.agario.controllers;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import sae.launch.agario.models.Player;

public class PlayerController {

    @FXML
    private Circle playerCircle;
    private Player player;

    public void PlayerController(Player player){
        this.update(player);
    }

    public void update(Player player){
        this.player = player;
        playerCircle.setRadius(player.getRadius());
        playerCircle.setCenterY(this.player.getY());
        playerCircle.setCenterX(this.player.getX());
    }

}
