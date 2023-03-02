package com.team44.runwayredeclarationapp.model;

import java.util.Objects;

/**
 * Represents a coordinate value
 */
public class Coord {

    private final double x;
    private final double y;

    /**
     * Create a coordinate
     *
     * @param x the x-value
     * @param y the y-value
     */
    public Coord(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x-value
     *
     * @return the x value
     */
    public double getX() {
        return x;
    }

    /**
     * Get the y-value
     *
     * @return the y value
     */
    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coord coord = (Coord) o;
        return Double.compare(coord.x, x) == 0 && Double.compare(coord.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
