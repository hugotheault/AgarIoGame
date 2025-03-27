package sae.launch.agario.models;

import sae.launch.agario.QuadTree;
import java.util.HashMap;
import java.util.Random;

public class AIRamdom implements AIStrategy {
    private double targetX;
    private double targetY;
    private int counter = 10; // Durée avant de changer de direction
    private static final int MAX_COUNTER = 50; // Temps avant de choisir un nouvel objectif

    public AIRamdom() {
        Random rand = new Random();
        targetX = rand.nextDouble() * 500;
        targetY = rand.nextDouble() * 500;
    }

    @Override
    public HashMap<String, Double> execStrategy(Double x, Double y, QuadTree quadtree) {
        Random rand = new Random();

        if (counter <= 0) {
            targetX = rand.nextDouble() * 500;
            targetY = rand.nextDouble() * 500;
            counter = MAX_COUNTER;
        } else {
            counter--;
        }

        HashMap<String, Double> coordinates = new HashMap<>();
        coordinates.put("x", targetX);
        coordinates.put("y", targetY);
        return coordinates;
    }
}
