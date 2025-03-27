package sae.launch.agario.models;

import sae.launch.agario.controllers.SoloInGameController;

import java.util.ArrayList;
import java.util.List;

public class PlayerComposite extends MovableObject implements PlayerComponant {
    private ArrayList<PlayerLeaf> players;
    private double totalMass;
    private double minimumMassToDivide;

    // Constructor
    public PlayerComposite(int ID, double x, double y, double mass, double minimumMass) {
        super(ID, x, y, mass);
        this.players = new ArrayList<PlayerLeaf>();
        totalMass = mass;
        this.minimumMassToDivide = minimumMass;
    }

    @Override
    public void updatePosition(double deltaX, double deltaY) {
        // Mettre à jour la position de tous les joueurs dans le groupe
        for (PlayerLeaf player : players) {
            player.updatePosition(deltaX, deltaY);
        }
    }

    @Override
    public double getMass() {
        return totalMass;
    }

    public double getRadius() {
        return Math.sqrt(totalMass);
    }

    @Override
    public double getX() {
        return super.getX();
    }

    @Override
    public double getY() {
        return super.getY();
    }

    @Override
    public double getRay() {
        return super.getRadius();
    }

    @Override
    public double getSpeed() {
        throw new IllegalCallerException(" Player ne peux pas appeler cette méthode");
    }

    @Override
    public double getSpeed(double mouseXCursor, double mouseYCursor, double v, double v1) {
        if (players.isEmpty()) return 0;
        return players.get(0).getSpeed(mouseXCursor, mouseYCursor, v, v1);
    }

    // Déplacer tout le groupe de joueurs
    public void move(double deltaX, double deltaY) {
        /*for (PlayerLeaf cell : this.getAllPlayer()) {
            cell.updatePosition(deltaX, deltaY);
        }*/
        for (PlayerLeaf cell : this.getAllPlayer()) {
            cell.updatePosition(deltaX, deltaY);
        }
        this.setX(this.getX() + deltaX);
        this.setY(this.getY() + deltaY);
    }

    public ArrayList<PlayerLeaf> getAllPlayer() {
        return players;
    }

    public void setMass(PlayerLeaf playerLeaf, double mass){
        if (players.contains(playerLeaf)) {
            playerLeaf.setMass(playerLeaf.getMass() + mass);
            totalMass += mass;
        } else {
            System.out.println("Erreur : PlayerLeaf non trouvé dans PlayerComposite");
        }
    }

    public void divide(double xCursor, double yCursor, double paneCenterX,double paneCenterY){
        for(PlayerLeaf player : getAllPlayer()){
            System.out.println(player.toString());
        }
        ArrayList<PlayerLeaf> tempPlayers = new ArrayList<PlayerLeaf>();
        for(PlayerLeaf player : getAllPlayer()){
            tempPlayers.add(player);
        }
        for( PlayerLeaf player : tempPlayers){
            if(player.getMass() >= minimumMassToDivide){
                player.setMass(player.getMass()/2);
                PlayerLeaf newPlayer = new PlayerLeaf(this.getID(), player.getX() + player.getRay(), player.getY() + player.getRay(), player.getMass());
                newPlayer.getSpeed(xCursor, yCursor, paneCenterX, paneCenterY);
                this.players.add(newPlayer);
            }
        }

    }

    public void addPlayer(PlayerLeaf player) {
        this.players.add(player);
    }

    public void removePlayer(PlayerComponant player) {
        this.players.remove(player);
    }

}
