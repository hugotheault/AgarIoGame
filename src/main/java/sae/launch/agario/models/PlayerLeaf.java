package sae.launch.agario.models;

public class PlayerLeaf extends MovableObject implements PlayerComponent {
    //A PlayerLeaf is a part of a player that doesn't have sons

    public PlayerLeaf(int ID, double x, double y, double mass) {
        super(ID, x, y, mass);
    }

    @Override
    public int getID(){
        return super.getID();
    }

    @Override
    public double getMass() {
        return super.getMass();
    }

    @Override
    public void setMass(double mass) {
        super.setMass(mass);
    }

    /**
     * @see Player#getSpeed()
     * @return
     */
    @Override
    public double getSpeed() {
        throw new IllegalCallerException(" Player ne peux pas appeler cette méthode");
    }

    /**
     * @see Player#getSpeed(double, double, double, double) 
     * @param xCursor
     * @param yCursor
     * @param paneCenterX
     * @param paneCenterY
     * @return
     */
    @Override
    public double getSpeed(double xCursor, double yCursor, double paneCenterX, double paneCenterY) {
        if(isInSlowCircle(xCursor,yCursor,paneCenterX,paneCenterY)){
            double slowRateX = Math.abs((paneCenterX - xCursor)/this.getSlowRangeRay());
            double slowRateY = Math.abs((paneCenterY - yCursor)/this.getSlowRangeRay());
            double speed = (this.getBaseMouvementSpeed() - (getSpeedSlowMultiplier() * Math.log(1 + this.getMass()))) * Math.max(slowRateX,slowRateY);
            return Math.max(speed,0) ;
        } else{
            double speed = (this.getBaseMouvementSpeed() - (getSpeedSlowMultiplier() * Math.log(1 + this.getMass())));
            return Math.max(speed,0);
        }
    }

    /**
     * @see Player#getSpeed(double, double, double, double)  
     * @param xCursor
     * @param yCursor
     * @param paneCenterX
     * @param paneCenterY
     * @return
     */
    public boolean isInSlowCircle(double xCursor, double yCursor, double paneCenterX,double paneCenterY){
        boolean isInSlowCircleRight = xCursor >= paneCenterX && xCursor <= paneCenterX +this.getSlowRangeRay();
        boolean isInSlowCircleLeft = xCursor <= paneCenterX && xCursor >= paneCenterX -this.getSlowRangeRay();
        boolean isInSlowCircleUp = yCursor >= paneCenterY && yCursor <= paneCenterY +this.getSlowRangeRay();
        boolean isInSlowCircleDown = yCursor <= paneCenterY && yCursor >= paneCenterY -this.getSlowRangeRay();
        if((isInSlowCircleRight && isInSlowCircleUp ) || (isInSlowCircleRight && isInSlowCircleDown )
                || (isInSlowCircleLeft && isInSlowCircleUp ) || (isInSlowCircleLeft && isInSlowCircleDown )){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Calculates the distance between this Leaf and one of it's brother
     * @param other the brother
     * @return
     */
    public double getDistance(PlayerLeaf other) {
        double dx = this.getX() - other.getX();
        double dy = this.getY() - other.getY();
        return Math.sqrt(dx * dx + dy * dy); // Distance euclidienne
    }

    /**
     * Indicates if this Leaf touches one of it's brother
     * @param other the brother
     * @return
     */
    public boolean collidesWith(PlayerLeaf other) {
        double distance = this.getDistance(other);
        double sumRadii = this.getRay() / 2 + other.getRay() / 2;
        return distance < sumRadii;
    }

    /**
     * Set both Leaf's position to make them "bounce" on each other
     * @param other the brother
     */
    public void bounceOff(PlayerLeaf other) {
        // Calculer la direction entre les deux objets
        double dx = (this.getX() - other.getX()) - (this.getRay() + other.getRay());
        double dy = (this.getY() - other.getY()) - (this.getRay() + other.getRay());
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Somme des rayons des deux cellules
        double sumOfRadii = this.getRay() / 2.0 + other.getRay() / 2.0;

        // Vérifier si la distance entre les deux cellules est inférieure ou égale à la somme de leurs rayons
        if (distance <= sumOfRadii) {
            // Normaliser la direction
            double nx = dx / distance;
            double ny = dy / distance;

            // Calcul de la vitesse du joueur courant
            double speed = this.getSpeed(this.getX(), this.getY(), 0, 0); // On utilise 0, 0 pour la référence

            // Appliquer un facteur de rebond en inversant la direction (simule le rebond)
            double reboundSpeed = speed * 0.5; // 50% de la vitesse pour le rebond

            // Ajuster la position avec la vitesse du rebond
            this.setX(this.getX() + reboundSpeed * nx);
            this.setY(this.getY() + reboundSpeed * ny);

            // Calcul de la vitesse de l'autre joueur et appliquer le rebond
            double otherSpeed = other.getSpeed(other.getX(), other.getY(), 0, 0);
            double otherReboundSpeed = otherSpeed * 0.5;

            other.setX(other.getX() - otherReboundSpeed * nx);
            other.setY(other.getY() - otherReboundSpeed * ny);
        }
    }

}
