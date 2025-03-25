package sae.launch.agario.models;

public class Game {
    private double mapSize;
    private double initialSize;
    private double sizeScaleToEat; //Ex: 1.33 -> You need 33% more mass to eat someone else
    private double pelletNb;
    private double sizeToDivide;

    //Default constructor with default values
    public Game(){
        this.mapSize = 1000;
        this.initialSize = 10;
        this.sizeScaleToEat = 1.15;
        this.pelletNb = 100000;
        this.sizeToDivide = 50;
    }
    public Game(double mapSize, double initialSize, double sizeScaleToEat, double pelletNb, double sizeToDivide){
        this.mapSize = mapSize;
        this.initialSize = initialSize;
        this.sizeScaleToEat = sizeScaleToEat;
        this.pelletNb = pelletNb;
        this.sizeToDivide = sizeToDivide;
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
}
