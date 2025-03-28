package sae.launch.agario.models;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class PlayerComposite extends MovableObject implements PlayerComponent {

    private static final double MERGE_DISTANCE_THRESHOLD = 20;  //If other PlayerComposite are within this distance value, they merge
    private ArrayList<PlayerLeaf> players = new ArrayList<>();
    private final double minimumMassToDivide;   //You can't create Leafs if you're not big enough
    private long timeSinceSplit = System.currentTimeMillis();   //You can't create Leafs if you have not waited enough
    private double mapSize;

    public PlayerComposite(int ID, double x, double y, double mass, double minMassToDivide, double mapSize) {
        super(ID, x, y, mass);
        this.minimumMassToDivide = minMassToDivide;
        this.mapSize = mapSize;
    }

    @Override
    public double getMass() {
        if( this.players == null || this.players.size() == 0 ){
            return 0;
        }
        double totalMass = 0;
        for( PlayerLeaf p : this.players ){
            totalMass += p.getMass();
        }
        return totalMass;
    }

    @Override
    public void setMass(double mass) {
        double totalMass = getMass();
        for (PlayerLeaf player : players) {
            double proportion = player.getMass() / totalMass;
            player.setMass(mass * proportion);
        }
    }

    @Override
    public double getX(){
        double totalX = 0;
        int cpt = 0;
        for (PlayerLeaf player : players) {
            totalX += player.getX();
            cpt+=1;
        }
        return totalX/cpt;
    }

    @Override
    public double getY(){
        double totalY = 0;
        int cpt = 0;
        for (PlayerLeaf player : players) {
            totalY += player.getY();
            cpt+=1;
        }
        return totalY/cpt;
    }

    @Override
    public int getID(){
        return super.getID();
    }

    @Override
    public double getSpeed() {
        return Math.max(1, 10 / Math.sqrt(getMass()));
    }

    @Override
    public double getSpeed(double xCursor, double yCursor, double paneCenterX, double paneCenterY) {
        return getSpeed();
    }

    public void addPlayer(PlayerLeaf player) {
        players.add(player);
    }

    public void removePlayer(PlayerLeaf player) {
        players.remove(player);
    }

    public List<PlayerLeaf> getPlayers() {
        return players;
    }

    private boolean coordonneeInMap(double x, double y){
        return (x > 0 && x < mapSize && y > 0 && y < mapSize);
    }

    /**
     * Divides this Entity into an ArrayList of Leafs
     */
    public void divide() {
        if (players.size() < 16) {
            ArrayList<PlayerLeaf> newPlayers = new ArrayList<>();
            for (PlayerLeaf player : players) {
                if( player.getMass() >= minimumMassToDivide ){
                    double newMass = player.getMass() / 2;
                    double coordX1 = player.getX() + player.getRadius()/2;
                    double coordY1 = player.getY() - player.getRadius()/2;
                    double coordX2 = player.getX() - player.getRadius()/2;
                    double coordY2 = player.getY() + player.getRadius()/2;
                    if( !coordonneeInMap(coordX1, coordY1)) {
                        coordX1 = Math.min(Math.max(0, coordX1), mapSize);
                        coordY1 = Math.min(Math.max(0, coordY1), mapSize);
                    }
                    if( !coordonneeInMap(coordX2, coordY2)) {
                        coordX2 = Math.min(Math.max(5, coordX2), mapSize-5);
                        coordY2 = Math.min(Math.max(5, coordY2), mapSize-5);
                    }
                    PlayerLeaf p1 = new PlayerLeaf(player.getID(), coordX1, coordY1, newMass);
                    PlayerLeaf p2 = new PlayerLeaf(player.getID(), coordX2, coordY2, newMass);
                    newPlayers.add(p1);
                    newPlayers.add(p2);
                } else {
                    newPlayers.add(player);
                }
            }
            this.players = newPlayers;
            timeSinceSplit = System.currentTimeMillis();
        }
    }

    /**
     * @return whether this Entity existed for long enough to merge with others
     */
    public boolean canMergeByTime() {
        for( PlayerLeaf part1 : this.players ) {
            for (PlayerLeaf part2 : this.players) {
                if ( (System.currentTimeMillis() - part1.getTimer()) > (5000 + part1.getMass()/100) && (System.currentTimeMillis() - part2.getTimer() > 5000 + part2.getMass())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return whether this Entity is close enough to merge with others
     */
    public boolean canMergeByDistance() {
        for (PlayerLeaf part1 : this.players) {
            for (PlayerLeaf part2 : this.players) {
                if (part1 != part2 && part1.getDistance(part2) < MERGE_DISTANCE_THRESHOLD) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Search for entities to merge with
     * @deprecated
     * @param part
     * @return
     */
    public PlayerLeaf getClosestMergeCandidate(PlayerLeaf part) {
        PlayerLeaf closest = null;
        double minDistance = Double.MAX_VALUE;

        for (PlayerLeaf other : this.players) {
            if (part != other) {
                double distance = part.getDistance(other);
                if (distance < MERGE_DISTANCE_THRESHOLD && distance < minDistance) {
                    closest = other;
                    minDistance = distance;
                }
            }
        }
        return closest;
    }

    /**
     * Merge this Entity with the closest Leafs
     */
    public void mergeClosestParts() {
        if (this.players.size() < 2) {
            return;
        }

        PlayerLeaf part1 = null;
        PlayerLeaf part2 = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < this.players.size(); i++) {
            for (int j = i + 1; j < this.players.size(); j++) {
                PlayerLeaf p1 = this.players.get(i);
                PlayerLeaf p2 = this.players.get(j);
                double distance = p1.getDistance(p2);

                if (distance < minDistance) {
                    minDistance = distance;
                    part1 = p1;
                    part2 = p2;
                }
            }
        }

        if (part1 != null && part2 != null) {
            PlayerLeaf mergedCell = new PlayerLeaf(IDGenerator.getGenerator().NextID(), this.getX(),this.getY(), part1.getMass()+part2.getMass());

            this.players.add(mergedCell);
            this.players.remove(part1);
            this.players.remove(part2);
        }
    }

    /**
     * Is executed when a collision between different entities of the same player occurs
     */
    public void handleCollisions() {
        for (int i = 0; i < this.players.size(); i++) {
            for (int j = i + 1; j < this.players.size(); j++) {
                PlayerLeaf p1 = this.players.get(i);
                PlayerLeaf p2 = this.players.get(j);

                if (p1.collidesWith(p2)) {
                    resolveCollision(p1, p2);
                }
            }
        }
    }

    private void resolveCollision(PlayerLeaf p1, PlayerLeaf p2) {
        p1.bounceOff(p2);
    }

}
