package sae.launch.agario.models;

public class Player extends MovableObject {
    public Player(int ID, double x, double y, double mass) {
        super(ID, x, y, mass);
    }

    @Override
    public double getSpeed() {
        return 0;
    }

    @Override
    public double getSpeed(double xCursor, double yCursor) {
        if(isInSlowCircle(xCursor,yCursor)){
            double slowRate = 100 * Math.abs((this.getX() - xCursor)/(this.getX()+this.getSlowRangeRay()));
            return this.getBaseMouvementSpeed() * slowRate / this.getMass();
        } else{
            return this.getBaseMouvementSpeed();
        }
    }

    public boolean isInSlowCircle(double xCursor, double yCursor){
        boolean isInSlowCircleRight = xCursor >= this.getX() && xCursor <= this.getX() +this.getSlowRangeRay();
        boolean isInSlowCircleLeft = xCursor <= this.getX() && xCursor >= this.getX() -this.getSlowRangeRay();
        boolean isInSlowCircleUp = yCursor >= this.getY() && yCursor <= this.getY() +this.getSlowRangeRay();
        boolean isInSlowCircleDown = yCursor <= this.getY() && yCursor >= this.getY() -this.getSlowRangeRay();
        if((isInSlowCircleRight && isInSlowCircleUp ) || (isInSlowCircleRight && isInSlowCircleDown )
                || (isInSlowCircleLeft && isInSlowCircleUp ) || (isInSlowCircleLeft && isInSlowCircleDown )){
            return true;
        } else {
            return false;
        }
    }


}
