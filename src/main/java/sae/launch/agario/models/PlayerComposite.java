package sae.launch.agario.models;

import java.util.ArrayList;
import java.util.List;

public class PlayerComposite extends MovableObject implements PlayerComponant {
    private ArrayList<PlayerComponant> players;

    // Constructor
    public PlayerComposite(int ID, double x, double y, double mass) {
        super(ID, x, y, mass);
        this.players = new ArrayList<>();
    }

    @Override
    public void updatePosition(double deltaX, double deltaY) {
        // Mettre à jour la position de tous les joueurs dans le groupe
        for (PlayerComponant player : players) {
            player.updatePosition(deltaX, deltaY);
        }
    }

    @Override
    public double getMass() {
        double totalMass = 0;
        for (PlayerComponant player : players) {
            totalMass += player.getMass();
        }
        return totalMass; // Masse totale du groupe
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
        return players.get(0).getSpeed(mouseXCursor, mouseYCursor, v, v1);
    }

    // Déplacer tout le groupe de joueurs
    public void move(double deltaX, double deltaY) {
        this.updatePosition(deltaX, deltaY);
    }

    public void addPlayer(PlayerComponant player) {
        this.players.add(player);
    }

    public void removePlayer(PlayerComponant player) {
        this.players.remove(player);
    }

}
