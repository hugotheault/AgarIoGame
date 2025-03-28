package sae.launch.agario.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AIChoiceController implements Initializable {

    @FXML private Label randomAI;
    @FXML private Label pelletAI;
    @FXML private Label chaserAI;
    @FXML private CheckBox checkBoxSpecialPellet;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    protected void onQuitButton() {
        showExitConfirmation();
    }

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
    protected void onSoloPlayButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sae/launch/agario/InGameView.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        stage.setFullScreen(true);

            SoloInGameController soloInGameController = loader.getController();
            soloInGameController.setNbRandomsAI(Integer.parseInt(randomAI.getText()));
            soloInGameController.setNbPelletAI(Integer.parseInt(pelletAI.getText()));
            soloInGameController.setNbChaserAI(Integer.parseInt(chaserAI.getText()));
            soloInGameController.setChoiceSpecialPellet(checkBoxSpecialPellet.isSelected());
    }

    @FXML
    protected void onRandomAIPlusButton(ActionEvent event) throws IOException {
        int res = Integer.parseInt(randomAI.getText());
        if(res >= 20){
            randomAI.setText(Integer.toString(20));
        }else{
            res += 1;
            randomAI.setText(Integer.toString(res));
        }
    }

    @FXML
    protected void onRandomAIMinusButton(ActionEvent event) throws IOException {
        int res = Integer.parseInt(randomAI.getText());
        if(res <= 0){
            randomAI.setText(Integer.toString(0));
        }else{
            res -= 1;
            randomAI.setText(Integer.toString(res));
        }
    }

    @FXML
    protected void onPelletAIPlusButton(ActionEvent event) throws IOException {
        int res = Integer.parseInt(pelletAI.getText());
        if(res >= 20){
            pelletAI.setText(Integer.toString(20));
        }else{
            res += 1;
            pelletAI.setText(Integer.toString(res));
        }
    }

    @FXML
    protected void onPelletAIMinusButton(ActionEvent event) throws IOException {
        int res = Integer.parseInt(pelletAI.getText());
        if(res <= 0){
            pelletAI.setText(Integer.toString(0));
        }else{
            res -= 1;
            pelletAI.setText(Integer.toString(res));
        }
    }

    @FXML
    protected void onChaserAIPlusButton(ActionEvent event) throws IOException {
        int res = Integer.parseInt(chaserAI.getText());
        if(res >= 20){
            chaserAI.setText(Integer.toString(20));
        }else{
            res += 1;
            chaserAI.setText(Integer.toString(res));
        }
    }

    @FXML
    protected void onChaserAIMinusButton(ActionEvent event) throws IOException {
        int res = Integer.parseInt(chaserAI.getText());
        if(res <= 0){
            chaserAI.setText(Integer.toString(0));
        }else{
            res -= 1;
            chaserAI.setText(Integer.toString(res));
        }
    }
}
