package sae.launch.agario.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import sae.launch.agario.models.Game;

public class InGameController implements Initializable {
    private @FXML Circle circleUser;
    private @FXML Pane pane;

    @Override
    public void initialize(URL u, ResourceBundle r){
        Game game = new Game();

       pane.setOnMouseMoved(event ->{
            game.setPlayerXPercent(event.getX() / pane.getWidth());
            game.setPlayerYPercent(event.getY() / pane.getHeight());
        });
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
