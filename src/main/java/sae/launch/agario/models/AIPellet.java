package sae.launch.agario.models;


import sae.launch.agario.QuadTree;

import java.util.ArrayList;
import java.util.HashMap;

public class AIPellet implements AIStrategy {
    @Override
    public HashMap<String, Double> execStrategy(Double x, Double y, QuadTree quadtree,AI ai) {

        HashMap<String, Double> coordinates = new HashMap<>();
       // ArrayList<Entity> potentialTargets = quadtree.getEntitiesInRegion(x * 100, x * -100, y * 100, y * -100);
        ArrayList<Entity> potentialTargets = quadtree.getEntitiesAroundMovableObjectLarger(ai);
        Double lenghtBetweenEntites = 100000000.0;

        for (Entity e : potentialTargets) {
            if (e.isPellet()) {
                if (Math.sqrt(Math.pow((e.getX() - x), 2) + Math.pow((e.getY() - y), 2)) < lenghtBetweenEntites) {
                    coordinates.put("x", e.getX());
                    coordinates.put("y", e.getY());
                    lenghtBetweenEntites = Math.sqrt(Math.pow((e.getX() - x), 2) + Math.pow((e.getY() - y), 2));
                }
            }
        }
        if(coordinates.get("x") == null || coordinates.get("y") == null ){
            return new AIRamdom().execStrategy(x,y,quadtree,ai);
        }
        return coordinates;
    }

}
