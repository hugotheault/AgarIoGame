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

    /* Fxml elements in the different views */
    private @FXML VBox leaderboard;
    private @FXML Pane pane;
    private @FXML Label scoreLabel;
    private @FXML Canvas minimap;

    /* Variables for the player management */
    private ArrayList<Player> players;

    private GameRenderer gameRenderer;

    private PlayerComposite currentPlayer;

    private double playerXPercent;
    private double playerYPercent;
    private double coX;
    private double coY;

    /* Different variables for all the game */
    private double baseMass;
    private double mapSize;
    private double initialSize;
    private double sizeScaleToEat;
    private int maxPelletNb;
    private double sizeToDivide;
    private double pelletSize;
    private Classement classement;
    private ThreadWorld threadWorld;
    private QuadTree quadTree;
    private PelletController pelletController;
    private double mouseXCursor;
    private double mouseYCursor;

    /* Information of the param page to launch the game */
    private int nbRandomsAI = 0;
    private int nbPelletAI = 0;
    private int nbChaserAI = 0;
    private boolean choiceSpecialPellet;

    private PlayerComposite playerGroup;

    /**
     * Initialisation of the game
     * @param u a pointer to the ressource file
     * @param r
     */
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
        currentPlayer = new PlayerComposite(idBase, player.getX(), player.getY(), baseMass, baseMass*2, this.mapSize);
        currentPlayer.addPlayer(player);
        quadTree.insert(currentPlayer);

        this.pelletController = new PelletController(quadTree, maxPelletNb, pelletSize);
        pelletController.generatePellets(choiceSpecialPellet);

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
        classement.updateClassement(leaderboard, quadTree.getAllMovableObjects(), currentPlayer);

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

    /**
     * Show window for exit confirmation
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
     * Function to create a random coordinate
     * @return a random int between 0 and mapSize
     */
    private double randomCoordinate(){
        Random rand = new Random();
        return rand.nextDouble(mapSize);
    }

    /**
     * Function to update the game, called every 33ms
     */
    private void updateGame() {
        updatePlayers();
        pelletController.generatePellets(choiceSpecialPellet);
        updateAIs();
        gameRenderer.updateVisuals(quadTree, quadTree.getAllPlayers());
        updateMinimap();
    }

    /**
     * Quit button to exit the application
     */
    @FXML
    protected void onQuitButton() {
        showExitConfirmation();
    }

    /**
     * Button to return to the menu of the application
     * @param event click on the button
     * @throws IOException
     */
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
     * Update the position of all the players, and whether they can eat or get eaten
     */
    private void updatePlayers() {

        for(PlayerComposite player: quadTree.getPlayers()){
            if (player.canMergeByTime() && player.canMergeByDistance()) {
                player.mergeClosestParts();
            } else {
                player.handleCollisions();
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

        for (PlayerComponent player : currentPlayer.getPlayers()) { // Utilisation du composite
            if (player instanceof MovableObject) {
                MovableObject p = (MovableObject) player;

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

    /**
     * Update the position of all the AIs, and wheter they can eat or get eaten
     */
    private void updateAIs() {

        for (AI ai : quadTree.getAllIAs()) {
            ai.setTree(quadTree);
            HashMap<String, Double> strategyObjective = ai.execStrategy();

            double targetX = strategyObjective.get("x");
            double targetY = strategyObjective.get("y");

            double dx = targetX - ai.getX();
            double dy = targetY - ai.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (coordonneeInMap(targetX, targetY)) {
                quadTree.remove(ai);
                if (distance > ai.getSpeed()) {
                    ai.setX(ai.getX() + (dx / distance) * ai.getSpeed());
                    ai.setY(ai.getY() + (dy / distance) * ai.getSpeed());
                } else {
                    ai.setX(targetX);
                    ai.setY(targetY);
                }
                quadTree.insert(ai);
            }

            // Vérification si l'IA peut manger le joueur
            for (Entity cible : quadTree.getEntitiesAroundMovableObject(ai)) {
                if (cible.equals(ai)) continue;

                // Vérifie si l'IA peut manger cette entité
                if (ai.canEat(cible)) {
                    if (cible instanceof PlayerLeaf) {
                        // Ici, l'IA peut manger une partie du joueur
                        if (ai.getMass() > ((PlayerLeaf) cible).getMass()) {
                            ai.Absorb(cible);  // L'IA absorbe la partie du joueur
                            PlayerComposite ancientPlayer = currentPlayer;
                            if( ancientPlayer.getPlayers().size() > 1 ){
                                ancientPlayer.removePlayer((PlayerLeaf) cible); // Retirer le joueur du QuadTree
                            } else {
                                currentPlayer = new PlayerComposite(ancientPlayer.getID(), randomCoordinate(), randomCoordinate(), 0, baseMass*2, mapSize);
                                PlayerLeaf newPlayer = new PlayerLeaf(currentPlayer.getID(), currentPlayer.getX(), currentPlayer.getY(), baseMass);
                                currentPlayer.addPlayer(newPlayer);
                                quadTree.remove(ancientPlayer);
                            }

                            Platform.runLater(() -> {
                                this.classement.updateClassement(leaderboard, quadTree.getAllMovableObjects(), this.currentPlayer);
                            });

                            // Si une IA mange une partie du joueur, le joueur perd une partie
                            if (!(cible instanceof PlayerComposite)) {
                                if (cible instanceof AI iaCible) {
                                    // Gestion de la création d'une nouvelle IA si elle est mangée
                                    if (iaCible.getStrategy() instanceof AIRamdom) {
                                        AI iaPlayer = new AI(IDGenerator.getGenerator().NextID(), randomCoordinate(), randomCoordinate(), baseMass, quadTree, new AIRamdom());
                                        quadTree.insert(iaPlayer);
                                    } else if (iaCible.getStrategy() instanceof AIPellet) {
                                        AI iaPlayer = new AI(IDGenerator.getGenerator().NextID(), randomCoordinate(), randomCoordinate(), baseMass, quadTree, new AIPellet());
                                        quadTree.insert(iaPlayer);
                                    } else if (iaCible.getStrategy() instanceof AIChaser) {
                                        AI iaPlayer = new AI(IDGenerator.getGenerator().NextID(), randomCoordinate(), randomCoordinate(), baseMass, quadTree, new AIChaser());
                                        quadTree.insert(iaPlayer);
                                    }
                                }
                            }
                        }
                    } else {
                        ai.Absorb(cible);
                        quadTree.remove(cible);
                        Platform.runLater(() -> {
                            this.classement.updateClassement(leaderboard, quadTree.getAllMovableObjects(), this.currentPlayer);
                        });
                    }
                }
            }
        }
    }

    /**
     * Update the view of the minimap
     */
    private void updateMinimap() {
        Platform.runLater(() -> {
            double minimapSize = 150;
            double scale = minimapSize / mapSize;

            GraphicsContext gc = minimap.getGraphicsContext2D();

            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, minimapSize, minimapSize);

            gc.setStroke(Color.BLACK);
            gc.strokeRect(0, 0, minimapSize, minimapSize);

            double visionWidth = 450;
            double visionHeight = 350;
            double visionScaleX = visionWidth / mapSize;
            double visionScaleY = visionHeight / mapSize;

            double visionX = currentPlayer.getX() * scale - visionWidth / 2 * visionScaleX;
            double visionY = currentPlayer.getY() * scale - visionHeight / 2 * visionScaleY;

            gc.setStroke(Color.RED);
            gc.strokeRect(visionX, visionY, visionWidth * visionScaleX, visionHeight * visionScaleY);

            gc.setFill(Color.RED);
            for (AI ia : quadTree.getAllIAs()) {
                double iaX = ia.getX() * scale;
                double iaY = ia.getY() * scale;
                double iaSize = Math.log(ia.getMass() + 1) * 1.5;

                if (iaX >= visionX && iaX <= visionX + visionWidth * visionScaleX &&
                        iaY >= visionY && iaY <= visionY + visionHeight * visionScaleY) {
                    gc.fillOval(iaX - iaSize / 2, iaY - iaSize / 2, iaSize, iaSize);
                }
            }

            gc.setFill(Color.BLUE);
            double playerX = currentPlayer.getX() * scale;
            double playerY = currentPlayer.getY() * scale;
            double playerSize = Math.log(currentPlayer.getMass() + 1) * 1.5;

            if (playerX >= visionX && playerX <= visionX + visionWidth * visionScaleX &&
                    playerY >= visionY && playerY <= visionY + visionHeight * visionScaleY) {
                gc.fillOval(playerX - playerSize / 2, playerY - playerSize / 2, playerSize, playerSize);
            }
        });
    }

    private boolean coordonneeInMap(double x, double y){
        return (x > 0 && x < mapSize && y > 0 && y < mapSize);
    }

   /* ------ Getter and setters ------ */

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

    public void setChoiceSpecialPellet(boolean choiceSpecialPellet) {
        this.choiceSpecialPellet = choiceSpecialPellet;
    }
}
