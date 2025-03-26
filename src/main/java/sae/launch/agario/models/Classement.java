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
        // Triez la liste des joueurs en fonction de leur masse (ordonnée de manière décroissante)
        Collections.sort(classement, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                // Comparer les masses des joueurs (ordre décroissant)
                return Double.compare(p2.getMass(), p1.getMass());
            }
        });

        // Optionnel : Mettre à jour le classement si tu veux afficher les 3 premiers par exemple
        // Mais il est déjà trié, donc tu peux juste afficher les 3 premiers joueurs :
        for (int i = 0; i < Math.min(3, classement.size()); i++) {
            Player p = classement.get(i);
            System.out.println("Classement " + (i + 1) + ": " + p.getID() + " - " + p.getMass());
        }
    }
}
