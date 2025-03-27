package sae.launch.agario.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;


import java.io.IOException;

import static sae.launch.agario.models.App.loadFXML;

public class AppController {
    @FXML
    private Label welcomeText;

    private static Scene scene;

    @FXML
    protected void onLocalButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sae/launch/agario/InGameView.fxml"));
        loader.setController(new SoloInGameController());
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    protected void onMultiButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(loadFXML("OnlineGameConnectionView"), 600, 400);
        stage.setScene(scene);
        stage.show();
        stage.getIcons().add(new Image("icon.png"));
    }
}