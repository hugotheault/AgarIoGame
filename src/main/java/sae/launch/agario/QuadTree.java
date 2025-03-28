package sae.launch.agario;

import sae.launch.agario.models.*;

import java.util.ArrayList;
import java.util.HashSet;

public class QuadTree {
    private QuadTree NWTree;
    private QuadTree NETree;
    private QuadTree SWTree;
    private QuadTree SETree;
    private final int depth;
    private final double length;
    private final double height;
    private final double xOffset;
    private final double yOffset;

    private HashSet<Entity> entities;

    /**
     *
     * @param length Length of the quadtree
     * @param height Height of the quadtree
     * @param depth Depth of the entire quadtree
     * @param xOffset X offset of the quadtree
     * @param yOffset Y offset of the quadtree
     */
    public QuadTree(double length, double height, int depth, double xOffset, double yOffset) {
        this.depth = depth;
        this.length = length;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        if (depth > 0) {
            double halfLength = length / 2;
            double halfWidth = height / 2;
            this.NWTree = new QuadTree(halfLength, halfWidth, depth - 1, xOffset, yOffset);
            this.NETree = new QuadTree(halfLength, halfWidth, depth - 1, xOffset + halfLength, yOffset);
            this.SWTree = new QuadTree(halfLength, halfWidth, depth - 1, xOffset, yOffset + halfWidth);
            this.SETree = new QuadTree(halfLength, halfWidth, depth - 1, xOffset + halfLength, yOffset + halfWidth);
        } else {
            entities = new HashSet<>();
        }
    }

    /**
     * Insert a new entity into the quadtree
     * @param entity the entity to insert
     * @return if the insert is a success
     */
    public boolean insert(Entity entity) {
        double x = entity.getX();
        double y = entity.getY();

        if (depth == 0) {
            entities.add(entity);
            return true;
        }

        double halfLength = length / 2;
        double halfWidth = height / 2;

        if (x < xOffset + halfLength && y < yOffset + halfWidth) {
            // NW
            return NWTree.insert(entity);
        } else if (x >= xOffset + halfLength && y < yOffset + halfWidth) {
            // NE
            return NETree.insert(entity);
        } else if (x < xOffset + halfLength && y >= yOffset + halfWidth) {
            // SW
            return SWTree.insert(entity);
        } else {
            // SE
            return SETree.insert(entity);
        }
    }


    /**
     *
     * @param player Player to analyze
     * @return List of entities in certain range around the player
     */
    public ArrayList<Entity> getEntitiesAroundPlayer(PlayerLeaf player) {
        double radius = 2 * player.getRadius();
        Boundary searchArea = new Boundary(player.getX() - radius, player.getY() - radius,
                2 * radius, 2 * radius);
        return getEntitiesInRegion(searchArea);
    }

    public ArrayList<Entity> getEntitiesAroundMovableObject(MovableObject obj) {
        double radius = Math.max(obj.getRadius() * 1.5, 50); // Minimum 50 pour ne pas rester bloqué
        Boundary searchArea = new Boundary(obj.getX() - radius, obj.getY() - radius,
                2 * radius, 2 * radius);
        return getEntitiesInRegion(searchArea);
    }

    public ArrayList<Entity> getEntitiesAroundMovableObjectLarger(MovableObject obj,double radius) {

        Boundary searchArea = new Boundary(obj.getX() - radius, obj.getY() - radius,
                2 * radius, 2 * radius);
        return getEntitiesInRegion(searchArea);
    }

    /**
     * Get the entities contained in a region (overload)
     * @param x1 First point of the X-axis line of the area
     * @param y1 First point of the Y-axis line of the area
     * @param x2 Second point of the X-axis line of the area
     * @param y2 Second point of the Y-axis line of the area
     * @return List of entity contained in a region
     */
    public ArrayList<Entity> getEntitiesInRegion(double x1, double y1, double x2, double y2) {
        Boundary region = new Boundary(x1, y1, x2 - x1, y2 - y1);
        return getEntitiesInRegion(region);
    }

    /**
     * Get the entities contained in a region
     * @param region Region to analyze
     * @return List of entity contained in a region
     */
    public ArrayList<Entity> getEntitiesInRegion(Boundary region) {
        ArrayList<Entity> result = new ArrayList<>();
        if (depth == 0) {
            for (Entity entity : entities) {
                if (region.contains(entity)) {
                    result.add(entity);
                }
            }
            return result;
        }

        if (region.intersects(new Boundary(xOffset, yOffset, length / 2, height / 2))) {
            result.addAll(NWTree.getEntitiesInRegion(region));
        }
        if (region.intersects(new Boundary(xOffset + length / 2, yOffset, length / 2, height / 2))) {
            result.addAll(NETree.getEntitiesInRegion(region));
        }
        if (region.intersects(new Boundary(xOffset, yOffset + height / 2, length / 2, height / 2))) {
            result.addAll(SWTree.getEntitiesInRegion(region));
        }
        if (region.intersects(new Boundary(xOffset + length / 2, yOffset + height / 2, length / 2, height / 2))) {
            result.addAll(SETree.getEntitiesInRegion(region));
        }

        return result;
    }


    /**
     * Verify if an area is intersecting with a region
     * @param x1 First point of the X-axis line of the area
     * @param y1 First point of the Y-axis line of the area
     * @param x2 Second point of the X-axis line of the area
     * @param y2 Second point of the Y-axis line of the area
     * @param regionX1 First point of the X-axis line of the region
     * @param regionY1 First point of the Y-axis line of the region
     * @param regionX2 Second point of the X-axis line of the region
     * @param regionY2 Second point of the Y-axis line of the region
     * @return
     */
    private boolean isRegionIntersecting(double x1, double y1, double x2, double y2, double regionX1, double regionY1, double regionX2, double regionY2) {
        return !(x1 > regionX2 || x2 < regionX1 || y1 > regionY2 || y2 < regionY1);
    }

    /**
     * Verify if the entity is in a certain area
     * @param entity Entity
     * @param x1 First point of the X-axis line of the area
     * @param y1 First point of the Y-axis line of the area
     * @param x2 Second point of the X-axis line of the area
     * @param y2 Second point of the Y-axis line of the area
     * @return
     */
    private boolean isEntityInRegion(Entity entity, double x1, double y1, double x2, double y2) {
        return (entity.getX() >= x1 && entity.getX() <= x2 && entity.getY() >= y1 && entity.getY() <= y2);
    }

    /**
     *
     * @return  Get the number of pellets inside the quadtree
     */
    public int getPelletsNumber() {
        int count = 0;
        if (depth != 0) {
            count += NETree.getPelletsNumber();
            count += NWTree.getPelletsNumber();
            count += SETree.getPelletsNumber();
            count += SWTree.getPelletsNumber();
        } else {
            for (Entity entity : entities) {
                if (entity instanceof Pellet) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     *
     * @return The height of the actual quadtree
     */
    public double getHeight() {
        return this.height;
    }

    /**
     *
     * @return The length of the actual quadtree
     */
    public double getLength() {
        return this.length;
    }

    public ArrayList<MovableObject> getAllMovableObjects() {
        ArrayList<MovableObject> result = new ArrayList<>();

        if (entities != null) {
            for (Entity entity : entities) {
                if (entity instanceof MovableObject) {
                    result.add((MovableObject) entity);
                }
            }
        }
        if (NWTree != null) {
            result.addAll(NWTree.getAllMovableObjects());
        }
        if (NETree != null) {
            result.addAll(NETree.getAllMovableObjects());
        }
        if (SWTree != null) {
            result.addAll(SWTree.getAllMovableObjects());
        }
        if (SETree != null) {
            result.addAll(SETree.getAllMovableObjects());
        }

        return result;
    }

    /**
     *
     * @return A list of all the players in the quadtree
     */
    public ArrayList<PlayerComposite> getAllPlayers() {
        ArrayList<PlayerComposite> result = new ArrayList<>();

        if (entities != null) {
            for (Entity entity : entities) {
                if (entity instanceof PlayerComposite) {
                    result.add((PlayerComposite) entity);
                }
            }
        }
        if (NWTree != null) {
            result.addAll(NWTree.getAllPlayers());
        }
        if (NETree != null) {
            result.addAll(NETree.getAllPlayers());
        }
        if (SWTree != null) {
            result.addAll(SWTree.getAllPlayers());
        }
        if (SETree != null) {
            result.addAll(SETree.getAllPlayers());
        }

        return result;
    }

    public ArrayList<AI> getAllIAs(){
        ArrayList<AI> result = new ArrayList<>();

        if (entities != null) {
            for (Entity entity : entities) {
                if (entity instanceof AI) {
                    result.add((AI) entity);
                }
            }
        }
        if (NWTree != null) {
            result.addAll(NWTree.getAllIAs());
        }
        if (NETree != null) {
            result.addAll(NETree.getAllIAs());
        }
        if (SWTree != null) {
            result.addAll(SWTree.getAllIAs());
        }
        if (SETree != null) {
            result.addAll(SETree.getAllIAs());
        }

        return result;
    }

    /**
     * Remove an entity from the quadtree
     * @param entity the entity to remove
     */
    public void remove(Entity entity) {
        if (depth == 0) {
            entities.remove(entity);
            return;
        }

        double halfLength = length / 2;
        double halfWidth = height / 2;

        if (entity.getX() < xOffset + halfLength && entity.getY() < yOffset + halfWidth) {
            // NW
            if (NWTree != null) {
                NWTree.remove(entity);
            }
        } else if (entity.getX() >= xOffset + halfLength && entity.getY() < yOffset + halfWidth) {
            // NE
            if (NETree != null) {
                NETree.remove(entity);
            }
        } else if (entity.getX() < xOffset + halfLength && entity.getY() >= yOffset + halfWidth) {
            // SW
            if (SWTree != null) {
                SWTree.remove(entity);
            }
        } else {
            // SE
            if (SETree != null) {
                SETree.remove(entity);
            }
        }
    }

    /**
     * Clear the quadtree in order to avoid reallocation
     */
    public void clear() {
        if (depth == 0) {
            entities.clear();
        } else {
            NWTree.clear();
            NETree.clear();
            SWTree.clear();
            SETree.clear();
        }
    }

    public ArrayList<PlayerComposite> getPlayers() {
        ArrayList<PlayerComposite> players = new ArrayList<>();

        if (depth == 0) {
            for (Entity entity : entities) {
                if (entity instanceof PlayerComposite) {
                    players.add((PlayerComposite) entity);
                }
            }
            return players;
        }
        if (NWTree != null) {
            players.addAll(NWTree.getAllPlayers());
        }
        if (NETree != null) {
            players.addAll(NETree.getAllPlayers());
        }
        if (SWTree != null) {
            players.addAll(SWTree.getAllPlayers());
        }
        if (SETree != null) {
            players.addAll(SETree.getAllPlayers());
        }

        return players;

    }

}
