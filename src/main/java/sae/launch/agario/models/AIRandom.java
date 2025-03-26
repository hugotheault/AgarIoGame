package sae.launch.agario.models;

import sae.launch.agario.QuadTree;

import java.util.HashMap;
import java.util.Random;

public class AIRandom implements AIStrategy{
    @Override
    public HashMap<String,Double> execStrategy(Double x, Double y, QuadTree quadtree) {
        Random rand = new Random();
        HashMap<String, Double> coordinates = new HashMap<>();
        coordinates.put("x", rand.nextDouble()%100);
        coordinates.put("y", rand.nextDouble()%100);
        return coordinates;
    }

}
