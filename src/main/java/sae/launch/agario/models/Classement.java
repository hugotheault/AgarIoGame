package sae.launch.agario.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Classement {

    private ArrayList<Player> classement;

    public Classement() {
        this.classement = new ArrayList<>();
    }

    public void updateClassement() {
        Collections.sort(classement, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Double.compare(p2.getMass(), p1.getMass());
            }
        });
        for (int i = 0; i < Math.min(3, classement.size()); i++) {
            Player p = classement.get(i);
            System.out.println("Classement " + (i + 1) + ": " + p.getID() + " - " + p.getMass());
        }
    }

    public void addPlayer(Player p) {
        classement.add(p);
    }

    public void removePlayer(Player p) {
        classement.remove(p);
    }

    public Player getPlayer(Integer indice) {
        updateClassement();
        return classement.get(indice);
    }
}
