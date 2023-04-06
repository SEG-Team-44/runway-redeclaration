package com.team44.runwayredeclarationapp.model;

/**
 * The class refers to a single runway in real world Extends Runway
 */
public class SRunway extends Runway {

    /**
     * Initialise a single (non-parallel) runway
     *
     * @param d1         degree of one logical runway
     * @param d2         degree of the other logical runway
     * @param parameters all numerical inputs
     */
    public SRunway(int d1, int d2, double[] parameters) {

        super(d1, d2, parameters);
        setPhyId();
        setLogicId();
    }
}
