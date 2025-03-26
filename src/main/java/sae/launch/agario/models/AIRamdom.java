package sae.launch.agario.models;

import sae.launch.agario.QuadTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;

public class AIRamdom implements AIStrategy{
    @Override
    public HashMap<String,Double> execStrategy(Double x, Double y, QuadTree quadtree) {
        Random rand = new Random();
        HashMap<String, Double> coordinates = new HashMap<>();
        coordinates.put("x", rand.nextDouble());
        coordinates.put("y", rand.nextDouble());
        return coordinates;
    }

}
