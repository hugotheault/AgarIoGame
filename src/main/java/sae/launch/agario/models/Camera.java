package sae.launch.agario.models;

import sae.launch.agario.Boundary;
import sae.launch.agario.QuadTree;
import java.util.ArrayList;

public class Camera {
    private double x;
    private double y;
    private double zoomFactor;

    public Camera(double x, double y, double zoomFactor) {
        this.x = x;
        this.y = y;
        this.zoomFactor = zoomFactor;
    }

    public Camera() {

    }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getZoomFactor() { return zoomFactor; }
    public void setZoomFactor(double zoomFactor) { this.zoomFactor = zoomFactor; }

    /**
     * Updates the Camera's position based on followed players
     */
    public void updatePosition(QuadTree quadtree) {
        ArrayList<PlayerComposite> players = quadtree.getPlayers();

        if (players.isEmpty()) return;
        double cpt = 0;
        // Calcul du barycentre des joueurs suivis
        double sumX = 0, sumY = 0;
        for (PlayerLeaf player : players.get(0).getPlayers()) {
            sumX += player.getX();
            sumY += player.getY();
            cpt++;
        }

        // Nouvelle position centrée sur la moyenne des positions des joueurs

        this.x = sumX / cpt;
        this.y = sumY / cpt;
    }
    public void updatePosition(PlayerComposite player) {
        this.x = player.getX();
        this.y = player.getY();
    }

    /**
     * @return the visible zone based on Camera's position and zoom
     */
    public Boundary getVisibleRegion(double screenWidth, double screenHeight) {
        double viewWidth = screenWidth / zoomFactor;
        double viewHeight = screenHeight / zoomFactor;

        double x1 = x - viewWidth / 2;
        double y1 = y - viewHeight / 2;
        double x2 = x + viewWidth / 2;
        double y2 = y + viewHeight / 2;

        return new Boundary(x1, y1, viewWidth, viewHeight);
    }
}
