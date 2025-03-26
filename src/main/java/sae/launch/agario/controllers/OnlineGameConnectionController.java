package sae.launch.agario.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class OnlineGameConnectionController implements Initializable {


    @FXML
    TextField inputIPCo;

    @FXML
    TextField inputPortCo;

    @Override
    public void initialize(URL u, ResourceBundle r){

    }

    @FXML
    protected void onConnectButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sae/launch/agario/InGameView.fxml"));
            loader.setController(new OnlineInGameController(inputIPCo.getText(), Integer.parseInt(inputPortCo.getText())));
            Parent root = loader.load();

            InetAddress ip = InetAddress.getLocalHost();
            System.out.println(ip.getHostAddress());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void onHostButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sae/launch/agario/InGameView.fxml"));
            loader.setController(new OnlineInGameController());
            Parent root = loader.load();

            InetAddress ip = InetAddress.getLocalHost();
            System.out.println(ip.getHostAddress());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
