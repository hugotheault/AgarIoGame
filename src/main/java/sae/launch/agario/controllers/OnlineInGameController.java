package sae.launch.agario.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import sae.launch.agario.models.serverFiles.Server;

public class OnlineInGameController implements Initializable {
    private @FXML Pane pane;
    private OnlineGame game;
    private Server server;

    @Override
    public void initialize(URL u, ResourceBundle r){
        this.game = new OnlineGame();

        server = new Server();




    }

    @FXML
    protected void onQuitButton() {
        Platform.exit();
    }

    @FXML
    protected void onMenuButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sae/launch/agario/AppView.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

        stage.show();

    }

}
