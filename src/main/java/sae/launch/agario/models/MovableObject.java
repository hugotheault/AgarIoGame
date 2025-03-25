package sae.launch.agario.models;

import static java.lang.Math.round;

public abstract class MovableObject extends Entity {

    private final int rangeHorizonModifier = 100;
    public MovableObject(int ID, double x, double y, double mass) {
        super(ID, x, y, mass);
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
    public abstract double getSpeed(double xCursor,double yCursor);

    /**
     * Add the absorbedEntity mass to the MovableObject mass, this function didn't check anything
     * throw IllegalArgumentException if param is null
     * @param absorbedEntity
     */
    public void Absorb(Entity absorbedEntity){
        if( absorbedEntity == null){
            throw new IllegalArgumentException("MovableObject : the absorbedEntity is null");
        } else{
            super.setMass(this.getMass() + absorbedEntity.getMass());
        }
    }


}
