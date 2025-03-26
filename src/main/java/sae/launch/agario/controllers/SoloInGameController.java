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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import sae.launch.agario.QuadTree;
import sae.launch.agario.models.*;

public class SoloInGameController implements Initializable {
    private @FXML VBox leaderboard;
    private @FXML Pane pane;
    private @FXML Label scoreLabel;
    private ThreadWorld threadWorld;
    private QuadTree quadTree;
    private PelletController pelletController;

    private ArrayList<PlayerComponant> players;

    private GameRenderer gameRenderer;


    private double playerXPercent;
    private double playerYPercent;
    private double coX;
    private double coY;

    private double baseMass;
    private double mapSize;
    private double initialSize;
    private double sizeScaleToEat; //Ex: 1.33 -> You need 33% more mass to eat someone else
    private int maxPelletNb;
    private double sizeToDivide;
    private double pelletSize;
    private Classement classement;

    private double mouseXCursor;
    private double mouseYCursor;

    @Override
    public void initialize(URL u, ResourceBundle r){
        this.mapSize = 2000;
        this.initialSize = 50;
        this.sizeScaleToEat = 1.15;
        this.maxPelletNb = 500;
        this.sizeToDivide = this.initialSize * 2;
        this.pelletSize = 10;

        quadTree = new QuadTree(mapSize, mapSize, 6, 0, 0);

        int idBase = IDGenerator.getGenerator().NextID();
        quadTree.insert(new PlayerLeaf(idBase, 100, 100, initialSize));

        this.pelletController = new PelletController(quadTree, maxPelletNb, pelletSize);
        pelletController.generatePellets();

        this.players = new ArrayList<>();
        PlayerComponant player1 = new PlayerLeaf(idBase, 50, 50, initialSize);
        players.add(player1);
        baseMass = player1.getMass();

        this.gameRenderer = new GameRenderer(pane);

        pane.setOnMouseMoved(event ->{
            setPlayerXPercent(event.getX() / pane.getWidth());
            setPlayerYPercent(event.getY() / pane.getHeight());
            setMouseXCursor(event.getX());
            setMouseYCursor(event.getY());
        });

        this.threadWorld = new ThreadWorld (gameRenderer, new Runnable() {
            @Override
            public void run() {
                updateGame();
            }
        });
        threadWorld.start();

        Platform.runLater(() -> {
            Stage stage = (Stage) pane.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                event.consume();
                showExitConfirmation();
            });
            pane.requestFocus();
        });

        this.classement = new Classement(baseMass);
        classement.addPlayer(player1);
        System.out.println("Classement mis à jour : ");

        pane.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("SPACE")) {
                dividePlayer(quadTree.getAllPlayers().getLast());
            }
        });

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

    private void updateGame() {
        updatePlayers();
        pelletController.generatePellets();
        gameRenderer.updateVisuals(quadTree, players);
    }

    private void dividePlayer(PlayerComponant player) {
        System.out.println(player.getMass()+" : "+sizeToDivide);
        System.out.println(player.getRay());
        if(player.getMass() >= sizeToDivide) {
            PlayerLeaf p = new PlayerLeaf(player.getID(), player.getX()+player.getRay()/10, player.getY()+player.getRay()/10, player.getMass()/2);
            player.setMass(player.getMass()/2);
            quadTree.insert(p);
            updateGame();
        }
    }

    @FXML
    protected void onQuitButton() {
        // create the alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de sortie");
        alert.setHeaderText("Voulez-vous vraiment quitter ?");
        alert.setContentText("Cliquez sur OK pour quitter ou Annuler pour rester.");

        // Show dialog box and wait to the user
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Close the application
            Platform.exit();
            System.exit(0);
        }
    }

    @FXML
    protected void onMenuButton(ActionEvent event) throws IOException {
        // create the alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de sortie");
        alert.setHeaderText("Voulez-vous vraiment Retourner au menu principal ?");
        alert.setContentText("Cliquez sur OK pour quitter ou Annuler pour rester.");

        // Show dialog box and wait to the user
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Return to home menu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sae/launch/agario/AppView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            System.out.println(((Node) event.getSource()).getScene().getWindow().getHeight());
            stage.setScene(new Scene(root));

            stage.show();
        }
    }


    /**
     *Update the position of all the players, and wheter they can eat or get eaten
     */
    private void updatePlayers() {

        for(PlayerComponant player: quadTree.getPlayers()){
            //Update position du joueur principal
            double directionX = playerXPercent - 0.5;
            double directionY = playerYPercent - 0.5;
            double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
            if (magnitude != 0) {
                directionX /= magnitude;
                directionY /= magnitude;
            }
            double deltaX = directionX * player.getSpeed(mouseXCursor, mouseYCursor,pane.getWidth() / 2,pane.getHeight() / 2);
            double deltaY = directionY * player.getSpeed(mouseXCursor, mouseYCursor,pane.getWidth() / 2,pane.getHeight() / 2);
            player.setX(player.getX() + deltaX);
            player.setY(player.getY() + deltaY);
        }



        //todo update la position des joueurs IA

        for(PlayerComponant joueur: quadTree.getAllPlayers()){
            for(Entity cible: quadTree.getEntitiesAroundPlayer((PlayerLeaf) joueur)){
                if(cible.equals(joueur)) continue;
                if(joueur.canEat(cible)){
                    joueur.setMass(joueur.getMass()+cible.getMass());
                    quadTree.remove(cible);
                    Platform.runLater(()->{
                        scoreLabel.setText(""+(joueur.getMass()-baseMass));
                        this.classement.updateClassement(leaderboard, quadTree.getAllPlayers());
                    });
                }
            }
        }
    }


    /*
    Getters and Setters
     */
    public double getMapSize() {
        return mapSize;
    }

    public void setMapSize(double mapSize) {
        this.mapSize = mapSize;
    }

    public double getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(double initialSize) {
        this.initialSize = initialSize;
    }

    public double getSizeScaleToEat() {
        return sizeScaleToEat;
    }

    public void setSizeScaleToEat(double sizeScaleToEat) {
        this.sizeScaleToEat = sizeScaleToEat;
    }

    public double getPelletNb() {
        return maxPelletNb;
    }

    public void setPelletNb(int pelletNb) {
        this.maxPelletNb = pelletNb;
    }

    public double getSizeToDivide() {
        return sizeToDivide;
    }

    public void setSizeToDivide(double sizeToDivide) {
        this.sizeToDivide = sizeToDivide;
    }

    public void setPlayerXPercent(double playerXPercent) {
        this.playerXPercent = playerXPercent;
    }

    public void setPlayerYPercent(double playerYPercent) {
        this.playerYPercent = playerYPercent;
    }

    public void setCoX(double x) {
        this.coX = x;
    }
    public void setCoY(double y) {
        this.coY = y;
    }

    public void setMouseXCursor(double mouseXCursor) {
        this.mouseXCursor = mouseXCursor;
    }

    public void setMouseYCursor(double mouseYCursor) {
        this.mouseYCursor = mouseYCursor;
    }

}
