package sae.launch.agario.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static sae.launch.agario.models.App.loadFXML;

public class OnlineGamePlayViewController implements Initializable {

    private int ID;
    private String stringId;
    private static Scene scene;
    private String inputIPCo;
    private int inputPortCo;

    public OnlineGamePlayViewController(String text, int parseInt) {
        this.inputIPCo = text;
        this.inputPortCo = parseInt;

    }

    public String getStringId() {
        return stringId;
    }

    public int getID() {
        return ID;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void onButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sae/launch/agario/InGameView.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loader.setController(new OnlineInGameController(inputIPCo, inputPortCo));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
