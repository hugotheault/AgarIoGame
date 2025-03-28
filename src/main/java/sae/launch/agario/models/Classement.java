package sae.launch.agario.models;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Classement {

    private ArrayList<MovableObject> classement;  //Players and Ai in the game
    private PlayerComposite currentPlayer;  //Solo player
    private double baseWeight;

    public Classement(double baseWeight){
        this.baseWeight = baseWeight;
        this.classement = new ArrayList<>();
    }

    /**
     * Updates the leaderBoard based on players and AI mass
     * @param leaderboard   Graphic element
     * @param players   List of players and AI alive
     * @param currentPlayer     Solo player
     */
    public void updateClassement(VBox leaderboard, ArrayList<MovableObject> players, PlayerComposite currentPlayer) {
        this.currentPlayer = currentPlayer;
        leaderboard.getChildren().clear();
        classement.clear();
        classement.addAll(players);
        classement.sort((p1, p2) -> Double.compare(p2.getMass(), p1.getMass()));
        for (int i = 0; i < Math.min(3, classement.size()); i++) {
            MovableObject p = classement.get(i);
        }
        int count = 0;
        for(MovableObject m : classement) {
            if(count < 10){
                Label label = new Label((classement.indexOf(m) + 1) + ": " + m.getID() + " - " + (m.getMass() - this.baseWeight));
                label.setStyle("-fx-text-fill: white;");
                if(m.getID() == currentPlayer.getID()){
                    label.setStyle("-fx-font-weight: bold;-fx-effect: dropshadow(gaussian, gold, 10, 0.5, 0, 0);-fx-text-fill: white;");
                }
                leaderboard.getChildren().add(label);
            }
            count ++;
        }
    }

    public void addMovableObject(MovableObject p) {
        classement.add(p);
    }

    public void removeMovableObject(MovableObject p) {
        classement.remove(p);
    }

    public MovableObject getMovableObject(Integer indice, VBox leaderboard, ArrayList<MovableObject> players) {
        updateClassement(leaderboard, players, this.currentPlayer);
        return classement.get(indice);
    }

    public ArrayList<MovableObject> getClassement() {
        return this.classement;
    }
}
