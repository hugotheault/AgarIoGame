package sae.launch.agario.models;

import sae.launch.agario.QuadTree;
import sae.launch.agario.models.serverFiles.ServerSocketRunnerWorld;

public class Game {
    private double mapSize;
    private double initialSize;
    private double sizeScaleToEat; //Ex: 1.33 -> You need 33% more mass to eat someone else
    private double pelletNb;
    private double sizeToDivide;
    private double maxSpeed;

    private QuadTree quadTree;
    private ThreadWorld threadWorld;


    private double playerXPercent;
    private double playerYPercent;


    //Default constructor with default values
    public Game(){
        this.mapSize = 1000;
        this.initialSize = 10;
        this.sizeScaleToEat = 1.15;
        this.pelletNb = 100000;
        this.sizeToDivide = 50;
        this.maxSpeed = 2;
        this.quadTree = new QuadTree(mapSize, mapSize, 6);
        this.threadWorld = new ThreadWorld(this, new Runnable() {
            @Override
            public void run() {
                updateGame();
            }
        });
        threadWorld.start();

    }

    private void updateGame() {
        System.out.println("----------------");
        System.out.println("X : " + playerXPercent);
        System.out.println("Y : " + playerYPercent);
    }

    public Game(double mapSize, double initialSize, double sizeScaleToEat, double pelletNb, double sizeToDivide, double maxSpeed){
        this.mapSize = mapSize;
        this.initialSize = initialSize;
        this.sizeScaleToEat = sizeScaleToEat;
        this.pelletNb = pelletNb;
        this.sizeToDivide = sizeToDivide;
        this.maxSpeed = maxSpeed;
        this.quadTree = new QuadTree(mapSize, mapSize, 6);
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
        return pelletNb;
    }

    public void setPelletNb(double pelletNb) {
        this.pelletNb = pelletNb;
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
}
