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
import java.util.*;

import sae.launch.agario.QuadTree;
import sae.launch.agario.models.*;

public class SoloInGameController implements Initializable {
    private @FXML VBox leaderboard;
    private @FXML Pane pane;
    private @FXML Label scoreLabel;
    private ThreadWorld threadWorld;
    private QuadTree quadTree;
    private PelletController pelletController;

    private ArrayList<Player> players;

    private GameRenderer gameRenderer;

    private Player currentPlayer;

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

    private final double nbRandomsAI = 50;
    private final double nbPelletAI = 0;
    private final double nbChaserAI = 0;

    @Override
    public void initialize(URL u, ResourceBundle r){
        this.mapSize = 2000;
        this.initialSize = 50;
        this.sizeScaleToEat = 1.15;
        this.maxPelletNb = 500;
        this.sizeToDivide = 50;
        this.pelletSize = 10;

        quadTree = new QuadTree(mapSize, mapSize, 6, 0, 0);

        int idBase = IDGenerator.getGenerator().NextID();

        Player player = new Player(idBase, 100, 100, initialSize);
        currentPlayer = player;
        quadTree.insert(player);
        baseMass = player.getMass();

        this.pelletController = new PelletController(quadTree, maxPelletNb, pelletSize);
        pelletController.generatePellets();

        // test implements IA

        for (int i = 0; i < nbRandomsAI; i++) {
            Random rand = new Random();
            int xSpawn = rand.nextInt(2000);
            int ySpawn = rand.nextInt(2000);
            AI iaPlayer = new AI(IDGenerator.getGenerator().NextID(), xSpawn, ySpawn, 50, quadTree, new AIChaser());
            quadTree.insert(iaPlayer);
        }
        //----------

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
        });

        this.classement = new Classement(baseMass);
        classement.addMovableObject(new Player(11,0,0,10));
        classement.addMovableObject(new Player(12,0,0,50));
        classement.addMovableObject(new Player(13,0,0,500));
        classement.addMovableObject(new Player(14,0,0,2));
        classement.addMovableObject(player);
        classement.updateClassement(leaderboard, quadTree.getAllMovableObjects(), player);

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
        updateAIs();
        gameRenderer.updateVisuals(quadTree, players);
    }

    @FXML
    protected void onQuitButton() {
        showExitConfirmation();
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
           // System.out.println(((Node) event.getSource()).getScene().getWindow().getHeight());
            stage.setScene(new Scene(root));
            stage.setHeight(400);
            stage.setWidth(600);
            stage.show();
        }
    }


    /**
     *Update the position of all the players, and wheter they can eat or get eaten
     */
    private void updatePlayers() {

        for(Player player: quadTree.getPlayers()){
            quadTree.remove(player);
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
            quadTree.insert(player);
        }

        for(Player joueur: quadTree.getAllPlayers()){
            for(Entity cible: quadTree.getEntitiesAroundPlayer((Player) joueur)){
                if(cible.equals(joueur)) continue;
                if(joueur.canEat(cible)){
                    joueur.setMass(joueur.getMass()+cible.getMass());
                    quadTree.remove(cible);
                    Platform.runLater(()->{
                        scoreLabel.setText(""+(joueur.getMass()-baseMass));
                        this.classement.updateClassement(leaderboard, quadTree.getAllMovableObjects(), joueur);
                    });
                }
            }
        }
    }

    private void updateAIs() {
        for (AI ai : quadTree.getAllIAs()) {
            ai.setTree(quadTree);
            HashMap<String, Double> strategyObjective = ai.execStrategy();

            double targetX = strategyObjective.get("x");
            double targetY = strategyObjective.get("y");

            double dx = targetX - ai.getX();
            double dy = targetY - ai.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            quadTree.remove(ai);

            if (distance > ai.getSpeed()) {
                // Mouvement fluide avec interpolation
                ai.setX(ai.getX() + (dx / distance) * ai.getSpeed());
                ai.setY(ai.getY() + (dy / distance) * ai.getSpeed());
            } else {
                // L'IA arrive directement à la cible si elle est proche
                ai.setX(targetX);
                ai.setY(targetY);
            }

            quadTree.insert(ai);
        }

        for (AI ai : quadTree.getAllIAs()) {

            for (Entity cible : quadTree.getEntitiesAroundMovableObject(ai)) {
                if (cible.equals(ai)) continue;



                if (ai.canEat(cible)) {



                    ai.Absorb(cible);
                    quadTree.remove(cible);

                    Platform.runLater(()->{
                        this.classement.updateClassement(leaderboard, quadTree.getAllMovableObjects(), this.currentPlayer);
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
