package sae.launch.agario.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class OnlineGameConnectionController implements Initializable {

    @FXML
    BorderPane borderPane;

    /**
     * Load the view of the online game
     */
    @FXML
    TextField inputIPCo;

    @FXML
    TextField inputPortCo;

    @Override
    public void initialize(URL u, ResourceBundle r){
        Platform.runLater(() -> {
            Stage stage = (Stage) borderPane.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                event.consume();
                showExitConfirmation();
            });
        });
    }

    /**
     * Displays a popup to confirm the quit action
     * @see #onQuitButton()
     */
    private void showExitConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quitter l'application");
        alert.setHeaderText("Êtes-vous sûr de vouloir quitter ?");
        alert.setContentText("Cliquez sur Oui pour quitter, ou Non pour annuler.");

        // Customize buttons
        ButtonType yesButton = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Close the application
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            Platform.exit();
            System.exit(0);
        }
    }

    @FXML
    protected void onQuitButton() {
        showExitConfirmation();
    }

    @FXML
    protected void onMenuButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sae/launch/agario/AppView.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setHeight(400);
        stage.setWidth(600);
        stage.show();
    }

    @FXML
    protected void onConnectButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sae/launch/agario/OnlineGamePlayView.fxml"));
            loader.setController(new OnlineGamePlayViewController(inputIPCo.getText(), Integer.parseInt(inputPortCo.getText())));
            Parent root = loader.load();

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
