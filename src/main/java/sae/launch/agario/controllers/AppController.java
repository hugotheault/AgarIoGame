package sae.launch.agario.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static sae.launch.agario.models.App.loadFXML;

public class AppController implements Initializable {

    private static Scene scene;
    private @FXML StackPane stackPane;

    /**
     * Load the game window
     */
    public void initialize(URL u, ResourceBundle r){
        Platform.runLater(() -> {
            Stage stage = (Stage) stackPane.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                event.consume();
                showExitConfirmation();
            });
        });
    }

    /**
     * Displays a popup to confirm the quit action
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

    /**
     * Load the AI choice menu
     * Uses "src/main/ressources/sae.launch.agario.AIChoiceView"
     * @param event  a mouse click
     * @throws IOException
     */
    @FXML
    protected void onLocalButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(loadFXML("AIChoiceView"));
        stage.setScene(scene);
        stage.setWidth(600);
        stage.setHeight(400);
        stage.show();
    }

    /**
     * Load the menu to join an online game
     * @param event
     * @throws IOException
     */
    @FXML
    protected void onMultiButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(loadFXML("OnlineGameConnectionView"), 600, 400);
        stage.setScene(scene);
        stage.show();
        stage.getIcons().add(new Image("icon.png"));
    }
}