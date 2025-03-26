package sae.launch.agario.controllers;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import sae.launch.agario.models.PlayerComponant;
import sae.launch.agario.models.PlayerLeaf;

public class PlayerController {

    @FXML
    private Circle playerCircle;
    private PlayerComponant player;

    public void PlayerController(PlayerComponant player){
        this.update(player);
    }

    public void update(PlayerComponant player){
        this.player = player;
        if (player instanceof PlayerLeaf) {
            PlayerLeaf leaf = (PlayerLeaf) player;
            playerCircle.setRadius(10 * Math.sqrt(leaf.getMass()));
            playerCircle.setCenterY(leaf.getY());
            playerCircle.setCenterX(leaf.getX());
        }
    }
}