package sae.launch.agario.models;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import sae.launch.agario.QuadTree;
import sae.launch.agario.controllers.PelletController;

import java.util.Random;

public class Game {
    private double mapSize;
    private double initialSize;
    private double sizeScaleToEat; //Ex: 1.33 -> You need 33% more mass to eat someone else
    private int maxPelletNb;
    private double sizeToDivide;
    private double maxSpeed;
    private double pelletSize;

    private QuadTree quadTree;
    private Player player;
    private ThreadWorld threadWorld;
    private Pane pane;
    


    private double playerXPercent;
    private double playerYPercent;


    //Default constructor with default values
    public Game(Pane pane){
        this.mapSize = 1000;
        this.initialSize = 10;
        this.sizeScaleToEat = 1.15;
        this.maxPelletNb = 100000;
        this.sizeToDivide = 50;
        this.maxSpeed = 2;
        this.pelletSize = 5;
        this.quadTree = new QuadTree(mapSize, mapSize, 6);
        this.player = new Player(10, 100, 100, 50);
        this.pane = pane;
        this.threadWorld = new ThreadWorld(this, new Runnable() {
            @Override
            public void run() {
                updateGame();
            }
        });
        threadWorld.start();

    }

    public Game(double mapSize, double initialSize, double sizeScaleToEat, int pelletNb, double sizeToDivide, double maxSpeed, double pelletSize){
        this.mapSize = mapSize;
        this.initialSize = initialSize;
        this.sizeScaleToEat = sizeScaleToEat;
        this.maxPelletNb = pelletNb;
        this.sizeToDivide = sizeToDivide;
        this.maxSpeed = maxSpeed;
        this.quadTree = new QuadTree(mapSize, mapSize, 6);
        this.pelletSize = pelletSize;
    }

    private void updateGame() {
        updatePlayerPosition();

        updatePelletsNumber();

        System.out.println("----------------");
        System.out.println("NB pellet : " + quadTree.getPelletsNumber());
    }

    private int compteur;
    private void updatePelletsNumber() {
        if(compteur <= 0) {
            System.out.println("Creation pellets");
            if (quadTree.getPelletsNumber() < this.maxPelletNb) {
                IDGenerator generator = IDGenerator.getGenerator();
                Random random = new Random();
                int nbToAdd = this.maxPelletNb - quadTree.getPelletsNumber();
                while (nbToAdd > 0){
                    Pellet pellet = new Pellet(generator.NextID(), random.nextDouble(quadTree.getLength()), random.nextDouble(quadTree.getWidth()), pelletSize);
                    quadTree.insert(pellet);
                    nbToAdd--;
                }
            }
            compteur = 30;
        } else{
            compteur--;
        }
    }

    private void updatePlayerPosition() {
        double directionX = playerXPercent - 0.5;
        double directionY = playerYPercent - 0.5;

        double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);

        if (magnitude != 0) {
            directionX /= magnitude;
            directionY /= magnitude;
        }

        double deltaX = directionX * maxSpeed;
        double deltaY = directionY * maxSpeed;

        player.setX(player.getX() + deltaX);
        player.setY(player.getY() + deltaY);
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

    public Player getPlayer(){return  this.player;}

    public void setPlayerXPercent(double playerXPercent) {
        this.playerXPercent = playerXPercent;
    }

    public void setPlayerYPercent(double playerYPercent) {
        this.playerYPercent = playerYPercent;
    }
}
