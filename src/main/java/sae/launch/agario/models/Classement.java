package sae.launch.agario.models;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Classement {

    private ArrayList<Player> classement;

    private double baseWeight;

    public Classement(double baseWeight){
        this.baseWeight = baseWeight;
        this.classement = new ArrayList<>();
    }

    public void updateClassement(VBox leaderboard, ArrayList<Player> players) {
        leaderboard.getChildren().clear();
        classement.clear();
        System.out.println("Affichage classement");
        for(MovableObject obj : classement){
            System.out.println(obj);
        }
        for( Player player : players ){
            classement.add(player);
        }
        Collections.sort(classement, (p1, p2) -> Double.compare(p2.getMass(), p1.getMass()));
        for (int i = 0; i < Math.min(3, classement.size()); i++) {
            Player p = classement.get(i);
            System.out.println("Classement " + (i + 1) + ": " + p.getID() + " - " + p.getMass());
        }
        for(Player m : classement) {
            leaderboard.getChildren().add(new Label((classement.indexOf(m)+1)+": "+m.getID()+" - " + (m.getMass()-this.baseWeight)));
        }
    }

    public void addPlayer(Player p) {
        classement.add(p);
    }

    public void removePlayer(Player p) {
        classement.remove(p);
    }

    public Player getPlayer(Integer indice, VBox leaderboard, ArrayList<Player> players) {
        updateClassement(leaderboard, players);
        return classement.get(indice);
    }

    public ArrayList<Player> getClassement() {
        return this.classement;
    }
}
