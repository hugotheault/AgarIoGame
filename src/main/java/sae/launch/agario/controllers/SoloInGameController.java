package sae.launch.agario.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;


import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import sae.launch.agario.QuadTree;
import sae.launch.agario.models.*;

public class SoloInGameController implements Initializable {
    private @FXML VBox leaderboard;
    private @FXML Pane pane;
    private @FXML Label scoreLabel;
    private @FXML Canvas minimap;
    private ThreadWorld threadWorld;
    private QuadTree quadTree;
    private PelletController pelletController;

    private ArrayList<Player> players;

    private GameRenderer gameRenderer;

    private PlayerComposite currentPlayer;

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

    private int nbRandomsAI = 0;
    private int nbPelletAI = 0;
    private int nbChaserAI = 0;

    private PlayerComposite playerGroup;

    @Override
    public void initialize(URL u, ResourceBundle r){
        this.mapSize = 2000;
        this.initialSize = 50;
        this.sizeScaleToEat = 1.15;
        this.maxPelletNb = 500;
        this.sizeToDivide = 50*2;
        this.pelletSize = 10;
        this.baseMass = 50;

        quadTree = new QuadTree(mapSize, mapSize, 6, 0, 0);

        int idBase = IDGenerator.getGenerator().NextID();

        PlayerLeaf player = new PlayerLeaf(idBase, randomCoordinate(), randomCoordinate(), initialSize);
        this.playerGroup = new PlayerComposite(idBase, player.getX(), player.getY(), baseMass, baseMass*2, this.mapSize);
        this.playerGroup.addPlayer(player);
        currentPlayer = this.playerGroup;
        quadTree.insert(this.playerGroup);

        this.pelletController = new PelletController(quadTree, maxPelletNb, pelletSize);
        pelletController.generatePellets();



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
            // Implementation AI
                // Random AI
            for (int i = 0; i < nbRandomsAI; i++) {
                AI iaPlayer = new AI(IDGenerator.getGenerator().NextID(), randomCoordinate(), randomCoordinate(), baseMass, quadTree, new AIRamdom());
                quadTree.insert(iaPlayer);
            }
                // Pellet AI
            for (int i = 0; i < nbPelletAI; i++) {
                AI iaPlayer = new AI(IDGenerator.getGenerator().NextID(), randomCoordinate(), randomCoordinate(), baseMass, quadTree, new AIPellet());
                quadTree.insert(iaPlayer);
            }
                // Chase AI
            for (int i = 0; i < nbChaserAI; i++) {
                AI iaPlayer = new AI(IDGenerator.getGenerator().NextID(), randomCoordinate(), randomCoordinate(), baseMass, quadTree, new AIChaser());
                quadTree.insert(iaPlayer);
            }
        });

        this.classement = new Classement(baseMass);
        classement.addMovableObject(player);
        classement.updateClassement(leaderboard, quadTree.getAllMovableObjects(), this.playerGroup);

        if (minimap == null) {
            System.out.println("Erreur: minimap est null !");
        } else {
            System.out.println("Minimap bien initialisée.");
        }

        pane.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("SPACE")) {
                dividePlayer(currentPlayer);
            }
        });
    }

    private void dividePlayer(PlayerComposite currentPlayer) {
        System.out.println("Division");
        currentPlayer.divide();
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

    private int randomCoordinate(){
        Random rand = new Random();
        return rand.nextInt(2000);
    }

    private void updateGame() {
        updatePlayers();
        pelletController.generatePellets();
        updateAIs();
        gameRenderer.updateVisuals(quadTree, players);
        updateMinimap();
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

        for(PlayerComposite player: quadTree.getPlayers()){
            if (player instanceof PlayerComposite composite && composite.canMerge() && composite.canMergeByDistance()) {
                composite.mergeClosestParts();
            } else {
                player.handleCollisions();
                //Update position du joueur principal
                double directionX = playerXPercent - 0.5;
                double directionY = playerYPercent - 0.5;
                double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
                if (magnitude != 0) {
                    directionX /= magnitude;
                    directionY /= magnitude;
                }
                double deltaX = directionX * player.getSpeed(mouseXCursor, mouseYCursor, pane.getWidth() / 2, pane.getHeight() / 2);
                double deltaY = directionY * player.getSpeed(mouseXCursor, mouseYCursor, pane.getWidth() / 2, pane.getHeight() / 2);
                player.setX(player.getX() + deltaX);
                player.setY(player.getY() + deltaY);
            }
        }

        for (PlayerComponent player : playerGroup.getPlayers()) { // Utilisation du composite
            if (player instanceof MovableObject) {
                MovableObject p = (MovableObject) player;

                // Mise à jour de la position du joueur principal
                double directionX = playerXPercent - 0.5;
                double directionY = playerYPercent - 0.5;
                double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
                if (magnitude != 0) {
                    directionX /= magnitude;
                    directionY /= magnitude;
                }
                double deltaX = directionX * p.getSpeed(mouseXCursor, mouseYCursor, pane.getWidth() / 2, pane.getHeight() / 2);
                double deltaY = directionY * p.getSpeed(mouseXCursor, mouseYCursor, pane.getWidth() / 2, pane.getHeight() / 2);

                if (coordonneeInMap(p.getX() + deltaX, p.getY() + deltaY)) {
                    p.setX(p.getX() + deltaX);
                    p.setY(p.getY() + deltaY);
                }
            }
        }


        for(PlayerComposite joueur: quadTree.getAllPlayers()){
            for(PlayerLeaf sousJoueur : joueur.getPlayers()){
                for(Entity cible: quadTree.getEntitiesAroundPlayer((PlayerLeaf) sousJoueur)){
                    if(cible.equals(joueur)) continue;
                    if(sousJoueur.canEat(cible)){
                        sousJoueur.Absorb(cible);
                        quadTree.remove(cible);
                        Platform.runLater(()->{
                            scoreLabel.setText(""+(joueur.getMass()-baseMass));
                            this.classement.updateClassement(leaderboard, quadTree.getAllMovableObjects(), joueur);
                        });
                        if(!(cible instanceof Pellet)){
                            if(((AI)cible).getStrategy() instanceof AIRamdom) {
                                AI iaPlayer = new AI(IDGenerator.getGenerator().NextID(), randomCoordinate(), randomCoordinate(), baseMass, quadTree, new AIRamdom());
                                quadTree.insert(iaPlayer);
                            }else if(((AI)cible).getStrategy() instanceof AIPellet) {
                                AI iaPlayer = new AI(IDGenerator.getGenerator().NextID(), randomCoordinate(), randomCoordinate(), baseMass, quadTree, new AIPellet());
                                quadTree.insert(iaPlayer);
                            }else if(((AI)cible).getStrategy() instanceof AIChaser) {
                                AI iaPlayer = new AI(IDGenerator.getGenerator().NextID(), randomCoordinate(), randomCoordinate(), baseMass, quadTree, new AIChaser());
                                quadTree.insert(iaPlayer);
                            }
                        }
                    }
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

            if( coordonneeInMap(targetX,targetY)) {
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
                    if(cible == currentPlayer){
                        PlayerComposite player = new PlayerComposite(IDGenerator.getGenerator().NextID(), randomCoordinate(), randomCoordinate(), initialSize, baseMass*2, this.mapSize);
                        currentPlayer = player;
                        quadTree.insert(player);
                    }else if(!(cible instanceof PlayerComposite)){
                        if(cible instanceof AI iaCible) {

                            if(iaCible.getStrategy() instanceof AIRamdom) {
                                AI iaPlayer = new AI(IDGenerator.getGenerator().NextID(), randomCoordinate(), randomCoordinate(), baseMass, quadTree, new AIRamdom());
                                quadTree.insert(iaPlayer);
                            } else if(iaCible.getStrategy() instanceof AIPellet) {
                                AI iaPlayer = new AI(IDGenerator.getGenerator().NextID(), randomCoordinate(), randomCoordinate(), baseMass, quadTree, new AIPellet());
                                quadTree.insert(iaPlayer);
                            } else if(iaCible.getStrategy() instanceof AIChaser){
                                AI iaPlayer = new AI(IDGenerator.getGenerator().NextID(), randomCoordinate(), randomCoordinate(), baseMass, quadTree, new AIChaser());
                                quadTree.insert(iaPlayer);
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateMinimap() {
        Platform.runLater(() -> {
            double minimapSize = 150;
            double scale = minimapSize / mapSize;

            GraphicsContext gc = minimap.getGraphicsContext2D();

            // Remplir le fond de la minimap en blanc
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, minimapSize, minimapSize);

            // Dessiner la bordure de la minimap
            gc.setStroke(Color.BLACK);
            gc.strokeRect(0, 0, minimapSize, minimapSize);

            // Définir les dimensions du rectangle de vision (peut être ajusté selon les besoins)
            double visionWidth = 450; // Largeur de la zone de vision
            double visionHeight = 350; // Hauteur de la zone de vision
            double visionScaleX = visionWidth / mapSize;
            double visionScaleY = visionHeight / mapSize;

            // Position de la vision du joueur en minimap (ajustée à la position du joueur)
            double visionX = currentPlayer.getX() * scale - visionWidth / 2 * visionScaleX;
            double visionY = currentPlayer.getY() * scale - visionHeight / 2 * visionScaleY;

            // Dessiner le rectangle de vision du joueur en rouge
            gc.setStroke(Color.RED);
            gc.strokeRect(visionX, visionY, visionWidth * visionScaleX, visionHeight * visionScaleY);

            // Dessiner les IA et joueurs dans la zone de vision du joueur
            gc.setFill(Color.RED);
            for (AI ia : quadTree.getAllIAs()) {
                double iaX = ia.getX() * scale;
                double iaY = ia.getY() * scale;

                // Si l'IA est dans la zone de vision du joueur
                if (iaX >= visionX && iaX <= visionX + visionWidth * visionScaleX &&
                        iaY >= visionY && iaY <= visionY + visionHeight * visionScaleY) {
                    gc.fillOval(iaX, iaY, 4, 4);
                }
            }

            // Dessiner le joueur dans la zone de vision
            gc.setFill(Color.BLUE);
            double playerX = currentPlayer.getX() * scale;
            double playerY = currentPlayer.getY() * scale;

            if (playerX >= visionX && playerX <= visionX + visionWidth * visionScaleX &&
                    playerY >= visionY && playerY <= visionY + visionHeight * visionScaleY) {
                gc.fillOval(playerX, playerY, 5, 5);
            }
        });
    }




    private boolean coordonneeInMap(double x, double y){
        return (x > 0 && x < mapSize && y > 0 && y < mapSize);
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

    public void setNbRandomsAI(int nbRandomsAI) {
        this.nbRandomsAI = nbRandomsAI;
    }

    public void setNbPelletAI(int nbPelletAI) {
        this.nbPelletAI = nbPelletAI;
    }

    public void setNbChaserAI(int nbChaserAI) {
        this.nbChaserAI = nbChaserAI;
    }

}
