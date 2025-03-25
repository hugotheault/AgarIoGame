package sae.launch.agario.models;

import sae.launch.agario.QuadTree;

import java.util.ArrayList;
import java.util.HashMap;

public class AIChaser implements AIStrategy{
    @Override
    public HashMap<String,Double> execStrategy(Double x, Double y) {
        // A ENLEVER //
        QuadTree quad = new QuadTree(1000,1000,6);
        //           //
        HashMap<String,Double> coordinates = new HashMap<>();
        ArrayList<Entity> potentialTargets = quad.getEntitiesInRegion(x*100, x*-100, y*100, y*-100);
        Double lenghtBetweenEntites= 100000000.0;
        for(Entity e : potentialTargets){
            if(!e.isPellet()) {
                if (Math.sqrt(Math.pow((e.getX() - x), 2) + Math.pow((e.getY() - y), 2)) < lenghtBetweenEntites) {
                    coordinates.put("x", e.getX());
                    coordinates.put("y", e.getY());
                    lenghtBetweenEntites = Math.sqrt(Math.pow((e.getX() - x), 2) + Math.pow((e.getY() - y), 2));
                }
            }
        }
        return coordinates;
    }
}
