package sae.launch.agario.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;


import java.io.IOException;

public class AppController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onLocalButtonClick(ActionEvent event) throws IOException {
        welcomeText.setText("Lancement du jeu en Solo!");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sae/launch/agario/InGameView.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Agar.io - JavaFX");
        stage.show();
    }

    @FXML
    protected void onMultiButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sae/launch/agario/OnlineGameConnectionView.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        System.out.println(((Node) event.getSource()).getScene().getWindow().getHeight());
        stage.setScene(new Scene(root));

        stage.show();
    }
}