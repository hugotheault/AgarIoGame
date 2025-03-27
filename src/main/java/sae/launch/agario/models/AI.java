package sae.launch.agario.models;

import sae.launch.agario.QuadTree;

import java.awt.*;
import java.util.HashMap;

import static java.awt.Color.red;

public class AI extends MovableObject {

    private final AIStrategy strategy;
    private QuadTree tree;
    private final double aiBaseRatioSpeed = 0.7;
    private Color couleur = red;

    public AI(int ID, double x, double y, double mass, QuadTree tree, AIStrategy strategy) {
        super(ID, x, y, mass);
        this.tree = tree;
        this.strategy = strategy;
    }

    @Override
    public double getSpeed() {
        return aiBaseRatioSpeed * getSpecialPelletSpeedBoost() * (this.getBaseMouvementSpeed() - (this.getSpeedSlowMultiplier() * Math.log(1 + this.getMass())));
    }

    @Override
    public double getSpeed(double xCursor, double yCursor, double paneCenterX, double paneCenterY) {
        throw new IllegalCallerException(" AI ne peux pas appeler cette méthode");
    }

    public HashMap<String,Double> execStrategy(){
        return strategy.execStrategy(this.getX(), this.getY(), tree, this);
    }

    public void setTree(QuadTree tree) { this.tree = tree; }

}
