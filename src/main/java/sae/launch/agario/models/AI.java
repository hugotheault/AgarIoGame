package sae.launch.agario.models;

import sae.launch.agario.QuadTree;

import java.util.HashMap;

public class AI extends MovableObject {
    private AIStrategy strategy;
    private QuadTree tree;
    public AI(int ID, double x, double y, double mass,QuadTree tree,AIStrategy strategy) {
        super(ID, x, y, mass);
        this.tree = tree;
        this.strategy = strategy;
    }

    @Override
    public double getSpeed() {
        return (this.getBaseMouvementSpeed() - (this.getspeedSlowMultiplier() * Math.log(1 + this.getMass())));
    }

    @Override
    public double getSpeed(double xCursor, double yCursor, double paneCenterX, double paneCenterY) {
        throw new IllegalCallerException(" AI ne peux pas appeler cette méthode");
    }

    public HashMap<String,Double> getCoordone(){
        return strategy.execStrategy(this.getX(),this.getY(),tree);
    }

}
