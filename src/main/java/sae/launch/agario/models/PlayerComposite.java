package sae.launch.agario.models;

import java.util.ArrayList;
import java.util.List;

public class PlayerComposite extends MovableObject implements PlayerComponant {
    private ArrayList<PlayerLeaf> players;

    // Constructor
    public PlayerComposite(int ID, double x, double y, double mass) {
        super(ID, x, y, mass);
        this.players = new ArrayList<PlayerLeaf>();
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
        double totalMass = 0;
        for (PlayerLeaf player : players) {
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
        } else {
            System.out.println("Erreur : PlayerLeaf non trouvé dans PlayerComposite");
        }
    }

    public void addPlayer(PlayerLeaf player) {
        this.players.add(player);
    }

    public void removePlayer(PlayerComponant player) {
        this.players.remove(player);
    }

}
