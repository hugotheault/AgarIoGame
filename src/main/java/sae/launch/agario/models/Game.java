package sae.launch.agario.models;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sae.launch.agario.QuadTree;
import sae.launch.agario.controllers.PelletController;


import java.util.ArrayList;
import java.util.Random;

public class Game {
    private double mapSize;
    private double initialSize;
    private double sizeScaleToEat; //Ex: 1.33 -> You need 33% more mass to eat someone else
    private int maxPelletNb;
    private double sizeToDivide;
    private double pelletSize;

    private QuadTree quadTree;
    private ArrayList<Integer> playerIDs;
    private ThreadWorld threadWorld;
    private Pane pane;
    private Camera camera;

    private PelletController pelletController;

    private double playerXPercent;
    private double playerYPercent;
    private double coX;
    private double coY;


    //Default constructor with default values
    public Game(Pane pane){
        this.mapSize = 2000;
        this.initialSize = 50;
        this.sizeScaleToEat = 1.15;
        this.maxPelletNb = 500;
        this.sizeToDivide = 50;
        this.pelletSize = 10;
        this.quadTree = new QuadTree(mapSize, mapSize, 6, 0, 0);

        this.pelletController = new PelletController(this.quadTree, maxPelletNb, pelletSize);
        pelletController.generatePellets();

        int idBase = IDGenerator.getGenerator().NextID();
        this.quadTree.insert(new Player(idBase, 100, 100, initialSize));
        this.playerIDs = new ArrayList<>();
        playerIDs.add(idBase);
        this.pane = pane;
        this.camera = new Camera();
        camera.setZoomFactor(0.1);

        this.threadWorld = new ThreadWorld(this, new Runnable() {
            @Override
            public void run() {
                updateGame();
            }
        });
        threadWorld.start();

    }

    /**
     * The method called every time the game is updated
     */
    private void updateGame() {
        updatePlayers();

        pelletController.generatePellets();

        camera.updatePosition(quadTree, playerIDs);

        render();
    }

    /**
     * Render all the Entities on the pane
     */
    private void render() {
        // Exécute les opérations sur le thread de JavaFX
        Platform.runLater(() -> {
            pane.getChildren().clear();

            double centerX = pane.getWidth() / 2;
            double centerY = pane.getHeight() / 2;

            for (Entity entity: quadTree.getEntitiesInRegion(
                    camera.getX() - centerX,
                    camera.getY() - centerY,
                    camera.getX() + centerX,
                    camera.getY() + centerY)) {
                drawEntity(centerX, centerY, entity);
            }
            ArrayList<Player> entites = quadTree.getAllPlayers();
            for(Entity entity: entites){
                drawEntity(centerX, centerY, entity);
            }
        });
    }

    /**
     * Draw an entity on the pane
     * @param centerX The x axis center of the entity
     * @param centerY The y axis center of the entity
     * @param entity The entity
     */
    private void drawEntity(double centerX, double centerY, Entity entity) {
        double entityX = (entity.getX() - camera.getX() + centerX);
        double entityY = (entity.getY() - camera.getY() + centerY);
        double entityRadius = entity.getRadius();

        Circle circle = new Circle(entityX, entityY, entityRadius);

        if (playerIDs.contains(entity.getID())) {
            circle.setFill(Color.BLUE);
        } else {
            circle.setFill(((Pellet) entity).getColor());
        }

        pane.getChildren().add(circle);
    }

    //On utilise un compteur a 30 pour éviter de réactualiser le nombre de pellet a chaque actualisation (peu utile et couteux)
    private int compteur;
    private void updatePelletsNumber() {
        if(compteur <= 0) {
            if (quadTree.getPelletsNumber() < this.maxPelletNb) {
                IDGenerator generator = IDGenerator.getGenerator();
                Random random = new Random();
                int nbToAdd = this.maxPelletNb - quadTree.getPelletsNumber();
                while (nbToAdd > 0){
                    Pellet pellet = new Pellet(generator.NextID(), random.nextDouble(quadTree.getLength()), random.nextDouble(quadTree.getHeight()), pelletSize);
                    quadTree.insert(pellet);
                    nbToAdd--;
                }
            }
            compteur = 30;
        } else{
            compteur--;
        }
    }

    /**
     *Update the position of all the players, and wheter they can eat or get eaten
     */
    private void updatePlayers() {

        for(Player player: quadTree.getPlayersByIds(playerIDs)){
            //Update position du joueur principal
            double directionX = playerXPercent - 0.5;
            double directionY = playerYPercent - 0.5;
            double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
            if (magnitude != 0) {
                directionX /= magnitude;
                directionY /= magnitude;
            }
            double deltaX = directionX * player.getSpeed(coX, coY);
            double deltaY = directionY * player.getSpeed(coX, coY);
            player.setX(player.getX() + deltaX);
            player.setY(player.getY() + deltaY);
        }



        //todo update la position des joueurs IA

        for(Player joueur: quadTree.getAllPlayers()){
            for(Entity cible: quadTree.getEntitiesAroundPlayer((Player) joueur)){
                if(cible.equals(joueur)) continue;
                if(joueur.canEat(cible)){
                    joueur.setMass(joueur.getMass()+cible.getMass());
                    quadTree.remove(cible);
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
}
