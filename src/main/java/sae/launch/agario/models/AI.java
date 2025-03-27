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

    /**
     * @return the speed of an AI
     */
    @Override
    public double getSpeed() {
        return aiBaseRatioSpeed * getSpecialPelletSpeedBoost() * (this.getBaseMouvementSpeed() - (this.getSpeedSlowMultiplier() * Math.log(1 + this.getMass())));
    }

    /**
     * @return the speed of a player
     */
    @Override
    public double getSpeed(double xCursor, double yCursor, double paneCenterX, double paneCenterY) {
        throw new IllegalCallerException(" AI ne peux pas appeler cette méthode");
    }

    /**
     * Defines the Ai's next move
     * @return the coordinates of the AI's destination
     */
    public HashMap<String,Double> execStrategy(){
        return strategy.execStrategy(this.getX(), this.getY(), tree, this);
    }

    public void setTree(QuadTree tree) { this.tree = tree; }

    public AIStrategy getStrategy() { return strategy; }

}
