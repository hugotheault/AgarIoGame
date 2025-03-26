package sae.launch.agario.models;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.image.Image;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @FXML
    private VBox vbox;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("AppView"), 720, 480);
        stage.setScene(scene);
        stage.show();
        stage.getIcons().add(new Image("icon.png"));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/sae/launch/agario/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
