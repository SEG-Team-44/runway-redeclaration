package com.team44.runwayredeclarationapp.model;

import java.util.Objects;

/**
 * The class refers to a parallel runway in real world Extends Runway
 */
public class PRunway extends Runway {

    /**
     * Position character of one logical runway
     */
    private char pos1;
    /**
     * Position character of the other logical runway
     */
    private char pos2;

    /**
     * Initialise a parallel runway
     *
     * @param d1         degree of one logical runway
     * @param d2         degree of the other logical runway
     * @param pos1       position character of one logical runway
     * @param pos2       position character of the other logical runway
     * @param parameters all numerical parameters of the runway
     */
    public PRunway(int d1, int d2, char pos1, char pos2, double[] parameters) {
        super(d1, d2, pos1, pos2, parameters);

        this.pos1 = pos1;
        this.pos2 = pos2;

        setPhyId();
        setLogicId();
    }

    /**
     * Set the physical id of the runway, insert the position characters into the id generated in
     * Runway
     */
    @Override
    public void setPhyId() {
        super.setPhyId();
        phyId = phyId.substring(0, 2) + pos1 + phyId.substring(2) + pos2;
    }

    /**
     * Generate a physical ID given both of the degree values
     *
     * @param degree1 the degree for logical runway 1
     * @param degree2 the degree for logical runway 2
     * @param pos1    the position of logical runway 1
     * @param pos2    the position of logical runway 2
     * @return the physical id string
     */
    public static String createPhyId(int degree1, int degree2, char pos1, char pos2) {
        return getDegreeInString(degree1) + pos1 + "/" + getDegreeInString(degree2) + pos2;
    }

    /**
     * Set the logical ids for the 2 logical runways, insert the position characters into the ids
     * generated in Runway
     */
    @Override
    public void setLogicId() {
        super.setLogicId();
        logicId1 += pos1;
        logicId2 += pos2;
    }

    /**
     * Update the position of the runway based on the given values
     *
     * @param pos1 the position of logical runway 1
     * @param pos2 the position of logical runway 2
     */
    public void setPosition(char pos1, char pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;

        setPhyId();
        setLogicId();
    }

    public char getPos1() {
        return pos1;
    }

    public char getPos2() {
        return pos2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PRunway)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        PRunway runway = (PRunway) o;
        return pos1 == runway.pos1 && pos2 == runway.pos2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pos1, pos2);
    }
}
