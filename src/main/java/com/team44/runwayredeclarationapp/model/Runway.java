package com.team44.runwayredeclarationapp.model;

import java.util.Objects;

/**
 * The class refers to a runway in real world
 */
public class Runway implements Cloneable {

    /**
     * ID identifying the physical runway
     */
    protected String phyId;
    /**
     * Horizontal physical length of the runway
     */
    private double runwayL;
    /**
     * Vertical physical width of the runway
     */
    private double runwayW;
    /**
     * The physical distance between the end of the runway and the edge of strip Identical on both
     * ends
     */
    private double stripL;
    /**
     * The physical distance between the centerline of the runway and the edge of strip Identical on
     * both sides
     */
    private double stripW;
    /**
     * Vertical width of stopways, identical on both sides
     */
    private double stopwayW;
    /**
     * Vertical width of clearway, identical on both sides
     */
    private double clearwayW;
    /**
     * Horizontal length of resa
     */
    private double resaL;

    /**
     * ID identifying one logical runways, with its corresponding TORA, TODA, ASDA, LDA, Displaced
     * Threshold, horizontal stopway length & clearway length
     */
    protected String logicId1;
    protected int degree1;
    private double tora1;
    private double toda1;
    private double asda1;
    private double lda1;
    private double disThresh1;
    private double stopwayL1;
    private double clearwayL1;

    /**
     * ID identifying the other logical runways, with its corresponding TORA, TODA, ASDA, LDA,
     * Displaced Threshold, horizontal stopway length & clearway length
     */
    protected String logicId2;
    protected int degree2;
    private double tora2;
    private double toda2;
    private double asda2;
    private double lda2;
    private double disThresh2;
    private double clearwayL2;
    private double stopwayL2;

    /**
     * Constructor for single (non-parallel) runways
     *
     * @param degree1    degree of one logical runway
     * @param degree2    degree of the other logical runway
     * @param parameters all numerical inputs (parameters)
     */
    public Runway(int degree1, int degree2, double[] parameters) {
        this.degree1 = degree1;
        this.degree2 = degree2;

        updateParameters(parameters);
    }

    /**
     * Constructor for parallel runways
     *
     * @param degree1    degree of one logical runway
     * @param degree2    degree of the other logical runway
     * @param pos1       position character of one logical runway
     * @param pos2       position character of the other logical runway
     * @param parameters all numerical inputs (parameters)
     */
    public Runway(int degree1, int degree2, char pos1, char pos2, double[] parameters) {
        this.degree1 = degree1;
        this.degree2 = degree2;

        updateParameters(parameters);
    }

    /**
     * Set a physical id for the runway
     */
    public void setPhyId() {
        phyId = getDegreeInString(degree1) + "/" + getDegreeInString(degree2);
    }

    /**
     * Set logical id for the 2 logical runways based on their degrees (positions)
     */
    public void setLogicId() {
        logicId1 = getDegreeInString(degree1);
        logicId2 = getDegreeInString(degree2);
    }

    /**
     * Base on the degree given, return the degree in string and in the correct format (01-36)
     *
     * @param degree degree of a runway
     * @return degree in string and in correct format
     */
    public static String getDegreeInString(int degree) {
        String newDegree;

        if (degree < 10) {
            newDegree = "0" + degree;
        } else {
            newDegree = String.valueOf(degree);
        }

        return newDegree;
    }

    /**
     * Update the degrees of the runway based on the given values
     *
     * @param degree1 the degree for logical runway 1
     * @param degree2 the degree for logical runway 2
     */
    public void setDegree(int degree1, int degree2) {
        this.degree1 = degree1;
        this.degree2 = degree2;

        setPhyId();
        setLogicId();
    }

    /**
     * Update all parameters of the runway based on the given values
     *
     * @param parameters new parameters to be updated
     */
    public void updateParameters(double[] parameters) {
        runwayL = parameters[0];
        runwayW = parameters[1];
        stripL = parameters[2];
        stripW = parameters[3];
        clearwayW = parameters[4];
        resaL = parameters[5];
        tora1 = parameters[6];
        toda1 = parameters[7];
        asda1 = parameters[8];
        lda1 = parameters[9];
        tora2 = parameters[10];
        toda2 = parameters[11];
        asda2 = parameters[12];
        lda2 = parameters[13];
        disThresh1 = parameters[14];
        disThresh2 = parameters[15];

        stopwayL1 = asda1 - tora1;
        stopwayL2 = asda2 - tora2;
        this.stopwayW = runwayW;
        this.clearwayL1 = toda1 - tora1;
        this.clearwayL2 = toda2 - tora2;
    }

    public String getPhyId() {
        return phyId;
    }

    public double getRunwayL() {
        return runwayL;
    }

    public double getRunwayW() {
        return runwayW;
    }

    public double getStripL() {
        return stripL;
    }

    public double getStripW() {
        return stripW;
    }

    public double getStopwayL(String logicId) {
        if (logicId.equals(logicId1)) {
            return stopwayL1;
        } else {
            return stopwayL2;
        }
    }

    public double getStopwayW() {
        return stopwayW;
    }

    public double getTora(String logicId) {
        if (logicId.equals(logicId1)) {
            return tora1;
        } else {
            return tora2;
        }
    }

    public double getToda(String logicId) {
        if (logicId.equals(logicId1)) {
            return toda1;
        } else {
            return toda2;
        }
    }

    public double getAsda(String logicId) {
        if (logicId.equals(logicId1)) {
            return asda1;
        } else {
            return asda2;
        }
    }

    public double getLda(String logicId) {
        if (logicId.equals(logicId1)) {
            return lda1;
        } else {
            return lda2;
        }
    }

    public double getDisThresh(String logicId) {
        if (logicId.equals(logicId1)) {
            return disThresh1;
        } else {
            return disThresh2;
        }
    }

    public double getClearwayL(String logicId) {
        if (logicId.equals(logicId1)) {
            return clearwayL1;
        } else {
            return clearwayL2;
        }
    }

    public double getClearwayW() {
        return clearwayW;
    }

    public String getLogicId1() {
        return logicId1;
    }

    public String getLogicId2() {
        return logicId2;
    }


    public int getDegree1() {
        return degree1;
    }


    public int getDegree2() {
        return degree2;
    }

    public double getResaL() {
        return resaL;
    }

    /**
     * Generate a physical ID given both of the degree values
     *
     * @param degree1 the degree for logical runway 1
     * @param degree2 the degree for logical runway 2
     * @return the physical id string
     */
    public static String createPhyId(int degree1, int degree2) {
        return getDegreeInString(degree1) + "/" + getDegreeInString(degree2);
    }

    /**
     * Get the list of runway parameters
     *
     * @return the list of runway parameters
     */
    public double[] getParameters() {
        return new double[]{runwayL, runwayW, stripL, stripW, clearwayW, resaL,
            tora1, toda1, asda1, lda1,
            tora2, toda2, asda2, lda2, disThresh2,
            disThresh1};
    }

    /**
     * Get a cloned version of this runway but with the logical runways switched
     *
     * @return the switched threshold runway
     */
    public Runway getSwitchedThresholdRunway() {
        // Clone the runway
        var runway = this.clone();

        // Edit the degree and position
        runway.setDegree(runway.getDegree2(), runway.getDegree1());
        if (runway instanceof PRunway pRunway) {
            pRunway.setPosition(pRunway.getPos2(), pRunway.getPos1());
        }

        // Switch the parameters
        runway.updateParameters(new double[]{
            runwayL, // runwayL
            runwayW, // runwayW
            stripL, // stripL
            stripW, // stripW
            clearwayW, // clearwayW
            resaL, // resaL
            tora2, // tora2
            toda2, // toda2
            asda2, // asda2
            lda2, // lda2
            tora1, // tora1
            toda1, // toda1
            asda1, // asda1
            lda1, // lda1
            disThresh2, // disThresh2
            disThresh1, // disThresh1
        });

        return runway;
    }

    /**
     * Clone the runway object
     *
     * @return the cloned runway object
     */
    @Override
    public Runway clone() {
        try {
            return (Runway) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Runway)) {
            return false;
        }
        Runway runway = (Runway) o;
        return Double.compare(runway.runwayL, runwayL) == 0
            && Double.compare(runway.runwayW, runwayW) == 0
            && Double.compare(runway.stripL, stripL) == 0
            && Double.compare(runway.stripW, stripW) == 0
            && Double.compare(runway.stopwayW, stopwayW) == 0
            && Double.compare(runway.clearwayW, clearwayW) == 0
            && Double.compare(runway.resaL, resaL) == 0 && degree1 == runway.degree1
            && Double.compare(runway.tora1, tora1) == 0
            && Double.compare(runway.toda1, toda1) == 0
            && Double.compare(runway.asda1, asda1) == 0
            && Double.compare(runway.lda1, lda1) == 0
            && Double.compare(runway.disThresh1, disThresh1) == 0
            && Double.compare(runway.stopwayL1, stopwayL1) == 0
            && Double.compare(runway.clearwayL1, clearwayL1) == 0 && degree2 == runway.degree2
            && Double.compare(runway.tora2, tora2) == 0
            && Double.compare(runway.toda2, toda2) == 0
            && Double.compare(runway.asda2, asda2) == 0
            && Double.compare(runway.lda2, lda2) == 0
            && Double.compare(runway.disThresh2, disThresh2) == 0
            && Double.compare(runway.clearwayL2, clearwayL2) == 0
            && Double.compare(runway.stopwayL2, stopwayL2) == 0 && phyId.equals(runway.phyId)
            && logicId1.equals(runway.logicId1) && logicId2.equals(runway.logicId2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phyId, runwayL, runwayW, stripL, stripW, stopwayW, clearwayW, resaL,
            logicId1, degree1, tora1, toda1, asda1, lda1, disThresh1, stopwayL1, clearwayL1,
            logicId2,
            degree2, tora2, toda2, asda2, lda2, disThresh2, clearwayL2, stopwayL2);
    }
}
