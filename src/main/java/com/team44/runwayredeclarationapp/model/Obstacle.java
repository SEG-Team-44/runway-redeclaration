package com.team44.runwayredeclarationapp.model;

public class Obstacle {

    private String obstName;
    private double height;
    private double positionL;
    private double positionR;

    public Obstacle(String obstName, double height, double positionL, double positionR) {
        this.obstName = obstName;
        this.height = height;
        this.positionL = positionL;
        this.positionR = positionR;
    }

    public String getObstName() {
        return obstName;
    }

    public double getHeight() {
        return height;
    }

    public void setObstName(String obstName) {
        this.obstName = obstName;
    }

    public void setHeight(double height) {
        this.obstName = obstName;
    }

    public void setPositionL(double position) {
        this.positionL = position;
    }

    public void setPositionR(double positionR) {
        this.positionR = positionR;
    }

    public double getPositionL() {
        return positionL;
    }

    public double getPositionR() {
        return positionR;
    }

    /**
     * Get the slope calculation of the object
     *
     * @return the slope calculation
     */
    public double getSlope() {
        return height * 50;
    }

    /**
     * Check if the obstacle is on the left side of the runway (near start of logical runway 1)
     *
     * @return whether obstacle is on the left
     */
    public boolean isObstacleOnLeftOfRunway() {
        return positionL < positionR;
    }

    /**
     * Check if the obstacle is on the right side of the runway (near start of logical runway 2)
     *
     * @return whether obstacle is on the right
     */
    public boolean isObstacleOnRightOfRunway() {
        return positionL > positionR;
    }
}
