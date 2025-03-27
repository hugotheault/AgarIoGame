package sae.launch.agario.models;

import sae.launch.agario.QuadTree;

import java.util.ArrayList;
import java.util.HashMap;

public class AIChaser implements AIStrategy{
    @Override
    public HashMap<String,Double> execStrategy(Double x, Double y, QuadTree quadtree,AI ai) {
        HashMap<String,Double> coordinates = new HashMap<>();
        ArrayList<Entity> potentialTargets = quadtree.getEntitiesAroundMovableObjectLarger(ai,800);
        Double lenghtBetweenEntites= 100000000.0;
        for(Entity e : potentialTargets){
            if(!e.isPellet() && e.getID() != ai.getID() && e.getMass() < ai.getMass()) {
                if (Math.sqrt(Math.pow((e.getX() - x), 2) + Math.pow((e.getY() - y), 2)) < lenghtBetweenEntites) {
                    coordinates.put("x", e.getX());
                    coordinates.put("y", e.getY());
                    lenghtBetweenEntites = Math.sqrt(Math.pow((e.getX() - x), 2) + Math.pow((e.getY() - y), 2));
                }
            }
        }
        if(coordinates.get("x") == null || coordinates.get("y") == null ){
            return new AIPellet().execStrategy(x,y,quadtree,ai);
        }
        return coordinates;
    }
}
