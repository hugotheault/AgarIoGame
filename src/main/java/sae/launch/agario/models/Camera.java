package sae.launch.agario.models;

import sae.launch.agario.QuadTree;

import java.util.ArrayList;

public class Camera {
    private double x;
    private double y;
    private double zoomFactor;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    public void setZoomFactor(double zoomFactor) {this.zoomFactor = zoomFactor;}
    public double getZoomFactor() {return zoomFactor;}

    public void updatePosition(QuadTree quadtree, ArrayList<Integer> playerIDs) {
        ArrayList<Player> players = quadtree.getPlayersByIds(playerIDs);
        if(players.size()==1){
            setX(players.get(0).getX());
            setY(players.get(0).getY());
        }
    }
}
