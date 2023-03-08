package com.team44.runwayredeclarationapp.model;

/**
 * The class refers to a parallel runway in real world
 * Extends Runway
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
     * @param d1 degree of one logical runway
     * @param d2 degree of the other logical runway
     * @param pos1 position character of one logical runway
     * @param pos2 position character of the other logical runway
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
     * Set the physical id of the runway, insert the position characters into the id generated in Runway
     */
    @Override
    protected void setPhyId() {
        super.setPhyId();
        phyId = phyId.substring(0, 2) + pos1 + phyId.substring(2) + pos2;
    }

    /**
     * Set the logical ids for the 2 logical runways, insert the position characters into the ids generated in Runway
     */
    @Override
    protected void setLogicId() {
        super.setLogicId();
        logicId1 += pos1;
        logicId2 += pos2;
    }

    public char getPos1() {
        return pos1;
    }

    public char getPos2() {
        return pos2;
    }
}
