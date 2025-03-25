package sae.launch.agario;

import sae.launch.agario.models.Entity;

import java.util.ArrayList;
import java.util.HashSet;

public class QuadTree {
    private QuadTree NWTree;
    private QuadTree NETree;
    private QuadTree SWTree;
    private QuadTree SETree;
    private final int depth;
    private final double length;
    private final double width;

    private HashSet<Entity> entities;

    public QuadTree(double length, double width, int depth){
        this.depth = depth;
        this.length = length;
        this.width = width;

        if(depth > 0){
            this.NWTree = new QuadTree(length/2, width/2, depth-1);
            this.NETree = new QuadTree(length/2, width/2, depth-1);
            this.SWTree = new QuadTree(length/2, width/2, depth-1);
            this.SETree = new QuadTree(length/2, width/2, depth-1);
        } else{
            entities = new HashSet<>();
        }
    }

    public boolean insert(Entity entity, double x, double y){
        if (x < 0 || x > length || y < 0 || y > width) {
            return false;
        }

        if (depth == 0) {
            entities.add(entity);
            return true;
        }

        double halfLength = length / 2;
        double halfWidth = width / 2;

        if (x < halfLength && y < halfWidth) {
            // NW
            if (NWTree == null) {
                NWTree = new QuadTree(halfLength, halfWidth, depth - 1);
            }
            return NWTree.insert(entity, x, y);
        } else if (x >= halfLength && y < halfWidth) {
            // NE
            if (NETree == null) {
                NETree = new QuadTree(halfLength, halfWidth, depth - 1);
            }
            return NETree.insert(entity, x, y);
        } else if (x < halfLength && y >= halfWidth) {
            // SW
            if (SWTree == null) {
                SWTree = new QuadTree(halfLength, halfWidth, depth - 1);
            }
            return SWTree.insert(entity, x, y);
        } else {
            // SE
            if (SETree == null) {
                SETree = new QuadTree(halfLength, halfWidth, depth - 1);
            }
            return SETree.insert(entity, x, y);
        }
    }


    public ArrayList<Entity> getEntitiesInRegion(double x1, double y1, double x2, double y2) {
        ArrayList<Entity> result = new ArrayList<>();

        if (depth == 0) {
            for (Entity entity : entities) {
                if (isEntityInRegion(entity, x1, y1, x2, y2)) {
                    result.add(entity);
                }
            }
            return result;
        }

        if (NWTree != null) {
            result.addAll(NWTree.getEntitiesInRegion(x1, y1, x2, y2));
        }
        if (NETree != null) {
            result.addAll(NETree.getEntitiesInRegion(x1, y1, x2, y2));
        }
        if (SWTree != null) {
            result.addAll(SWTree.getEntitiesInRegion(x1, y1, x2, y2));
        }
        if (SETree != null) {
            result.addAll(SETree.getEntitiesInRegion(x1, y1, x2, y2));
        }

        return result;
    }
    private boolean isEntityInRegion(Entity entity, double x1, double y1, double x2, double y2) {
        double ex = entity.getX();
        double ey = entity.getY();
        return (ex >= x1 && ex <= x2 && ey >= y1 && ey <= y2);
    }

}
