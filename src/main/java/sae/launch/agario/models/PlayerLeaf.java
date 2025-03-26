package sae.launch.agario.models;

public class PlayerLeaf extends PlayerComponant {
    private boolean isAI;
    private double targetX, targetY; // Position du curseur ou cible IA

    public PlayerLeaf(int ID, double x, double y, double mass, boolean isAI) {
        super(ID, x,y, mass);
        this.isAI = isAI;
    }

    public void setTarget(double targetX, double targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    @Override
    public void updatePosition() {
        if (isAI) {
            moveAI();
        } else {
            moveTowardsTarget();
        }

        System.out.println("Position mise à jour : X=" + super.getX() + ", Y=" + super.getY());
    }


    /**
     * Déplace le joueur vers la cible (curseur ou objectif IA).
     */
    private void moveTowardsTarget() {
        double dx = targetX - super.getX();
        double dy = targetY - super.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        System.out.println("📢 Déplacement : dx=" + dx + ", dy=" + dy + ", distance=" + distance);

        if (distance > 1) { // Évite les micro-mouvements inutiles
            super.setX(super.getX() + (dx / distance) * getSpeed());
            super.setY(super.getY() + (dy / distance) * getSpeed());
            System.out.println("✅ Nouvelle position : X=" + super.getX() + ", Y=" + super.getX());
        }
    }


    /**
     * Logique simple pour une IA : se dirige vers un point aléatoire ou un joueur plus gros.
     */
    private void moveAI() {
        // Simulation d'un comportement simple : mouvement aléatoire ou vers la plus grosse masse
        if (Math.random() < 0.02) { // Change de direction parfois
            targetX = super.getX() + (Math.random() - 0.5) * 200;
            targetY = super.getY() + (Math.random() - 0.5) * 200;
        }

        moveTowardsTarget();
    }

    public boolean isAI() { return isAI; }

    @Override
    public double split() {
        return 0;
    }

    @Override
    public double getSpeed(double xCursor, double yCursor, double paneCenterX, double paneCenterY) {
        return super.getSpeed();
    }
}
