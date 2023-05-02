package com.team44.runwayredeclarationapp.model;

import java.util.Objects;

/**
 * The class is responsible for creating Obstacles
 */
public class Obstacle implements Cloneable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Obstacle)) {
            return false;
        }
        Obstacle obstacle = (Obstacle) o;
        return Double.compare(obstacle.height, height) == 0 && obstName.equals(
            obstacle.obstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(obstName, height);
    }

    @Override
    public Obstacle clone() {
        try {
            return (Obstacle) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
