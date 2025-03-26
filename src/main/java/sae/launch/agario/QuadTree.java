package sae.launch.agario;

import sae.launch.agario.models.Entity;
import sae.launch.agario.models.Pellet;
import sae.launch.agario.models.Player;

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
     * @param length
     * @param height
     * @param depth
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
     * Return a list of all entities inside of the specified region
     * @param x1 X of the first point
     * @param y1 Y of the first point
     * @param x2 X of the second point
     * @param y2 Y of the second point
     * @return a list of all entities inside the region (a rectangle whose corners are the 2 points
     */
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


        if (isRegionIntersecting(x1, y1, x2, y2, xOffset, yOffset, xOffset + length, yOffset + height)) {
            result.addAll(NWTree.getEntitiesInRegion(x1, y1, x2, y2));
        }
        if (isRegionIntersecting(x1, y1, x2, y2, xOffset + length / 2, yOffset, xOffset + length, yOffset + height)) {
            result.addAll(NETree.getEntitiesInRegion(x1, y1, x2, y2));
        }
        if (isRegionIntersecting(x1, y1, x2, y2, xOffset, yOffset + height / 2, xOffset + length, yOffset + height)) {
            result.addAll(SWTree.getEntitiesInRegion(x1, y1, x2, y2));
        }
        if (isRegionIntersecting(x1, y1, x2, y2, xOffset + length / 2, yOffset + height / 2, xOffset + length, yOffset + height)) {
            result.addAll(SETree.getEntitiesInRegion(x1, y1, x2, y2));
        }

        return result;
    }


    private boolean isRegionIntersecting(double x1, double y1, double x2, double y2, double regionX1, double regionY1, double regionX2, double regionY2) {
        return !(x1 > regionX2 || x2 < regionX1 || y1 > regionY2 || y2 < regionY1);
    }

    private boolean isEntityInRegion(Entity entity, double x1, double y1, double x2, double y2) {
        return (entity.getX() >= x1 && entity.getX() <= x2 && entity.getY() >= y1 && entity.getY() <= y2);
    }

    /**
     *
     * @return  GEt the number of pellets inside of the quadtree
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

    public double getHeight() {
        return this.height;
    }

    public double getLength() {
        return this.length;
    }

    /**
     *
     * @return A list of all the players in the quadtree
     */
    public ArrayList<Player> getAllPlayers() {
        ArrayList<Player> result = new ArrayList<>();

        if (entities != null) {
            for (Entity entity : entities) {
                if (entity instanceof Player) {
                    result.add((Player) entity);
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

    /**
     *
     * @param player the player you want to check's stuff around
     * @return A list of all entites around the player
     */
    public ArrayList<Entity> getEntitiesAroundPlayer(Player player) {
        ArrayList<Entity> result = new ArrayList<>();
        double playerX = player.getX();
        double playerY = player.getY();
        double playerRadius = player.getRadius();
        double radius = 2 * playerRadius;

        double x1 = playerX - radius;
        double y1 = playerY - radius;
        double x2 = playerX + radius;
        double y2 = playerY + radius;

        result.addAll(getEntitiesInRegion(x1, y1, x2, y2));

        return result;
    }

    /**
     * Remove an entity from the quadtree
     * @param entity the entity to remove
     */
    public void remove(Entity entity) {
        if (depth == 0) {
            System.out.println(entities.size());
            entities.remove(entity);
            System.out.println(entities.size());
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

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();

        if (depth == 0) {
            for (Entity entity : entities) {
                if (entity instanceof Player) {
                    players.add((Player) entity);
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
