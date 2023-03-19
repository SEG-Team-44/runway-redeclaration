package com.team44.runwayredeclarationapp.model;

/**
 * The class is responsible for placing an Obstacle on a Runway with its distance from the
 * thresholds and the center line
 */
public class RunwayObstacle {

    private Runway rw;
    private final Obstacle obst;
    private final double posL;
    private final double posR;
    private final double distCR;

    /**
     * Create a runway obstacle
     *
     * @param obst   the obstacle
     * @param rw     the runway
     * @param posL   the distance from the left threshold
     * @param posR   the distance from the right threshold
     * @param distCR the distance from the centreline
     */
    public RunwayObstacle(Obstacle obst, Runway rw, double posL, double posR, double distCR) {
        this.obst = obst;
        this.rw = rw;
        this.posL = posL;
        this.posR = posR;
        this.distCR = distCR;
    }

    public Obstacle getObst() {
        return obst;
    }

    public Runway getRw() {
        return rw;
    }

    public double getPositionL() {
        return posL;
    }

    public double getPositionR() {
        return posR;
    }

    public double getDistCR() {
        return distCR;
    }

    /**
     * Check if the obstacle is on the left side of the runway (near start of logical runway 1)
     *
     * @return whether obstacle is on the left
     */
    public boolean isObstacleOnLeftOfRunway() {
        return posL < posR;
    }

    /**
     * Check if the obstacle is on the right side of the runway (near start of logical runway 2)
     *
     * @return whether obstacle is on the right
     */
    public boolean isObstacleOnRightOfRunway() {
        return posL > posR;
    }

}
