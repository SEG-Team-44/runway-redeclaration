package com.team44.runwayredeclarationapp.model;

/**
 * The class is responsible for placing an Obstacle on a Runway with its distance from the
 * thresholds and the center line
 */
public class RunwayObstacle {

    private final Runway recalRw;
    private final Obstacle obst;
    private final double posL;
    private final double posR;
    private final double distCR;

    private final double blastPro;

    /**
     * Create a runway obstacle
     *
     * @param obst     the obstacle
     * @param recalRw  the runway
     * @param posL     the distance from the left threshold
     * @param posR     the distance from the right threshold
     * @param distCR   the distance from the centreline
     * @param blastPro the current blast protection
     */
    public RunwayObstacle(Obstacle obst, Runway recalRw, double posL, double posR, double distCR,
        double blastPro) {
        this.obst = obst;
        this.recalRw = recalRw;
        this.posL = posL;
        this.posR = posR;
        this.distCR = distCR;
        this.blastPro = blastPro;
    }

    public Obstacle getObst() {
        return obst;
    }

    public Runway getRecalculatedRw() {
        return recalRw;
    }

    public Runway getOriginalRw() {
        return recalRw;
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

    public double getBlastPro() {
        return blastPro;
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
