package com.team44.runwayredeclarationapp.model;

/**
 * The class is responsible for placing an Obstacle on a Runway
 * with its distance from the thresholds and the center line
 */
public class RunwayObstacle {

  private final Obstacle obst;
  private final Runway rw;

  private Runway reclaculatedRunway;
  private final double posL;
  private final double posR;
  private final double distCR;

  public RunwayObstacle(Obstacle obst, Runway rw, double posL, double posR, double distCR){
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
