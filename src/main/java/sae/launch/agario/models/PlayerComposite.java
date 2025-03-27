package sae.launch.agario.models;

import java.util.ArrayList;
import java.util.List;

public class PlayerComposite extends MovableObject implements PlayerComponent {

    private static final double MERGE_DISTANCE_THRESHOLD = 20;
    private ArrayList<PlayerLeaf> players = new ArrayList<>();
    private final double minimumMassToDivide;
    private long timeSinceSplit = System.currentTimeMillis();
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
        return Math.max(1, 10 / Math.sqrt(getMass())); // Même logique que PlayerLeaf
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

    public void divide() {
        if (players.size() < 16) { // Exemple : on ne divise pas plus de 16 fragments
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
                        coordX2 = Math.min(Math.max(1, coordX2), mapSize-1);
                        coordY2 = Math.min(Math.max(1, coordY2), mapSize-1);
                    }
                    PlayerLeaf p1 = new PlayerLeaf(player.getID(), player.getX() + player.getRadius()/2, player.getY() - player.getRadius()/2, newMass);
                    PlayerLeaf p2 = new PlayerLeaf(player.getID(), player.getX() - player.getRadius()/2, player.getY() + player.getRadius()/2, newMass);
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

    public boolean canMerge() {
        long elapsedTime = System.currentTimeMillis() - timeSinceSplit;
        return elapsedTime > 5000; // Fusion possible après 5 secondes
    }

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

    public PlayerLeaf getClosestMergeCandidate(PlayerLeaf part) {
        PlayerLeaf closest = null;
        double minDistance = Double.MAX_VALUE;

        for (PlayerLeaf other : this.players) {
            if (part != other) { // Éviter de tester avec soi-même
                double distance = part.getDistance(other);
                if (distance < MERGE_DISTANCE_THRESHOLD && distance < minDistance) {
                    closest = other;
                    minDistance = distance;
                }
            }
        }
        return closest; // Retourne le plus proche à fusionner
    }

    public void mergeClosestParts() {
        if (this.players.size() < 2) {
            return; // Impossible de fusionner avec une seule cellule
        }

        PlayerLeaf part1 = null;
        PlayerLeaf part2 = null;
        double minDistance = Double.MAX_VALUE;

        // Trouver les deux cellules les plus proches
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

        // Vérifier qu'on a trouvé deux cellules valides
        if (part1 != null && part2 != null) {
            // Créer une nouvelle cellule fusionnée
            PlayerLeaf mergedCell = new PlayerLeaf(IDGenerator.getGenerator().NextID(), this.getX(),this.getY(), part1.getMass()+part2.getMass());

            // Ajouter la nouvelle cellule et retirer les anciennes
            this.players.add(mergedCell);
            this.players.remove(part1);
            this.players.remove(part2);
        }
    }

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
