package com.team44.runwayredeclarationapp.model;

/**
 * The class is responsible for creating Obstacles
 */
public class Obstacle {

    private String obstName;
    private double height;

    public Obstacle(String obstName, double height) {
        this.obstName = obstName;
        this.height = height;
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

    /**
     * Get the slope calculation of the object
     *
     * @return the slope calculation
     */
    public double getSlope() {
        return height * 50;
    }
}

