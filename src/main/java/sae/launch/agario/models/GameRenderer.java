package sae.launch.agario.models;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sae.launch.agario.QuadTree;

import java.util.ArrayList;

public class GameRenderer {
    private Pane pane;
    private Camera camera;

    // Default constructor
    public GameRenderer(Pane pane){
        this.pane = pane;
        this.camera = new Camera();
        camera.setZoomFactor(0.1);
    }

    /**
     * The method called every time the game is updated
     */
    public void updateVisuals(QuadTree quadTree, ArrayList<PlayerComponant> players) {
        updatePlayers(players);
        camera.updatePosition(quadTree);
        render(quadTree, players);
    }

    /**
     * Met à jour la position des joueurs avant l'affichage.
     */
    private void updatePlayers(ArrayList<PlayerComponant> players) {
        for (PlayerComponant player : players) {
            if (player instanceof PlayerLeaf) {
                ((PlayerLeaf) player).updatePosition();
            } else if (player instanceof PlayerComposite) {
                for (PlayerComponant subPlayer : ((PlayerComposite) player).getAllPlayer()) {
                    if (subPlayer instanceof PlayerLeaf) {
                        ((PlayerLeaf) subPlayer).updatePosition();
                    }
                }
            }
        }
    }

    /**
     * Render all the Entities on the pane
     */
    private void render(QuadTree quadTree, ArrayList<PlayerComponant> players) {
        // Exécute les opérations sur le thread de JavaFX
        Platform.runLater(() -> {
            pane.getChildren().clear();

            double centerX = pane.getWidth() / 2;
            double centerY = pane.getHeight() / 2;

            // Récupère toutes les entités visibles
            for (Entity entity: quadTree.getEntitiesInRegion(
                    camera.getX() - centerX,
                    camera.getY() - centerY,
                    camera.getX() + centerX,
                    camera.getY() + centerY)) {
                drawEntity(centerX, centerY, entity);
            }

            // Dessine tous les joueurs (humains et IA)
            for (PlayerComponant player : players) {
                drawPlayer(centerX, centerY, player);
            }
        });
    }

    /**
     * Dessine un joueur (PlayerLeaf ou PlayerGroup)
     */
    private void drawPlayer(double centerX, double centerY, PlayerComponant player) {
        if (player instanceof PlayerLeaf) {
            drawEntity(centerX, centerY, (PlayerLeaf) player);
        } else if (player instanceof PlayerComposite) {
            for (PlayerComponant subPlayer : ((PlayerComposite) player).getAllPlayer()) {
                drawPlayer(centerX, centerY, subPlayer);
            }
        }
    }

    /**
     * Dessine une entité (pellets, joueurs individuels)
     */
    private void drawEntity(double centerX, double centerY, Entity entity) {
        double entityX = (entity.getX() - camera.getX() + centerX);
        double entityY = (entity.getY() - camera.getY() + centerY);
        double entityRadius = entity.getRadius();

        Circle circle = new Circle(entityX, entityY, entityRadius);

        if (entity instanceof PlayerLeaf) {
            circle.setFill(((PlayerLeaf) entity).isAI() ? Color.RED : Color.BLUE);
        } else {
            circle.setFill(((Pellet) entity).getColor());
        }

        pane.getChildren().add(circle);
    }
}
