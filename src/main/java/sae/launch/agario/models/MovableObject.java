package sae.launch.agario.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static java.lang.Math.round;

public abstract class MovableObject extends Entity {

    private final int rangeHorizonModifier = 100;
    private final double baseMouvementSpeed = 10;
    private final static double speedSlowMultiplier = 0.5;
    private final double slowRangeRay;

    private boolean SpecialPelletIsInvisible = false ;
    private double SpecialPelletSpeedBoost = 1;

    public MovableObject(int ID, double x, double y, double mass) {
        super(ID, x, y, mass);
        slowRangeRay = this.getRay();

    }
    public double getRay() {
        return 10*Math.sqrt(this.getMass()) ;
    }
    public int getRangeHorizonModifier() {
        return rangeHorizonModifier;
    }

    public int getRangeHorizon(){
        return (int)round(this.getMass()) + rangeHorizonModifier;
    }
    public abstract double getSpeed();

    public abstract double getSpeed(double xCursor,double yCursor,double paneCenterX,double paneCenterY);

    public double getBaseMouvementSpeed() {
        return baseMouvementSpeed;
    }

    public double getSlowRangeRay() {
        return slowRangeRay;
    }

    public double getSpeedSlowMultiplier(){return speedSlowMultiplier;}

    public double getSpecialPelletSpeedBoost() {
        return SpecialPelletSpeedBoost;
    }

    public void setSpecialPelletSpeedBoost(double specialPelletSpeedBoost) {
        SpecialPelletSpeedBoost = specialPelletSpeedBoost;
    }

    public boolean isSpecialPelletIsInvisible() {
        return SpecialPelletIsInvisible;
    }

    public void setSpecialPelletIsInvisible(boolean specialPelletIsInvisible) {
        SpecialPelletIsInvisible = specialPelletIsInvisible;
    }

    /**
     * Add the absorbedEntity mass to the MovableObject mass, this function didn't check anything
     * throw IllegalArgumentException if param is null
     * @param absorbedEntity
     */
    public void Absorb(Entity absorbedEntity){
        if( absorbedEntity == null){
            throw new IllegalArgumentException("MovableObject : the absorbedEntity is null");
        } else{
            if(absorbedEntity instanceof SpecialPellet){
                ((SpecialPellet) absorbedEntity).getEffect().ApplyEffect(this);
            }
            super.setMass(this.getMass() + absorbedEntity.getMass());
        }
    }

    public boolean canEat(Entity pellet) {
        if (pellet != null) {
            double dx = this.getX() - pellet.getX();
            double dy = this.getY() - pellet.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            // Vérifie si l'entité actuelle recouvre bien la cible
            boolean overlaps = distance <= (this.getRadius() + pellet.getRadius());

            // Vérifie si la cible est assez petite pour être absorbée
            boolean canEat = this.getMass() > pellet.getMass() * 1.2;

            return overlaps && canEat;
        }
        return false;
    }
}
