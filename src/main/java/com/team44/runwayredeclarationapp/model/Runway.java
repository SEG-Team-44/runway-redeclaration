package com.team44.runwayredeclarationapp.model;

public class Runway {
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
     * The physical distance between the end of the runway and the edge of strip
     * Identical on both ends
     */
    private double stripL;
    /**
     * The physical distance between the centerline of the runway and the edge of strip
     * Identical on both sides
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
     * ID identifying one logical runways, with its corresponding TORA, TODA, ASDA, LDA,
     * Displaced Threshold, horizontal stopway length & clearway length
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

    public Runway(int d1, int d2, double[] parameters) {
        degree1 = d1;
        degree2 = d2;

        updateParameters(parameters);
    }

    public Runway(int d1, int d2, char pos1, char pos2, double[] parameters) {
        degree1 = d1;
        degree2 = d2;

        updateParameters(parameters);
    }

    protected void setPhyId() {
        phyId = getDegreeInString(degree1) + "/" + getDegreeInString(degree2);
    }

    protected void setLogicId() {
        logicId1 = getDegreeInString(degree1);
        logicId2 = getDegreeInString(degree2);
    }

    private String getDegreeInString(int degree) {
        String newDegree;

        if (degree < 10) {
            newDegree = "0" + degree;
        }
        else newDegree = String.valueOf(degree);

        return newDegree;
    }

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

    public String getPhyId() {return phyId;}
    public double getRunwayL() {return runwayL;}
    public double getRunwayW() {return runwayW;}
    public double getStripL() {return stripL;}
    public double getStripW() {return stripW;}
    public double getStopwayL(String logicId) {
        if (logicId.equals(logicId1)) {
            return stopwayL1;
        }
        else return stopwayL2;
    }
    public double getStopwayW() {return stopwayW;}

    public double getTora(String logicId) {
        if (logicId.equals(logicId1)) {
            return tora1;
        } else return tora2;
    }

    public double getToda(String logicId) {
        if (logicId.equals(logicId1)) {
            return toda1;
        } else return toda2;
    }

    public double getAsda(String logicId) {
        if (logicId.equals(logicId1)) {
            return asda1;
        } else return asda2;
    }

    public double getLda(String logicId) {
        if (logicId.equals(logicId1)) {
            return lda1;
        } else return lda2;
    }

    public double getDisThresh(String logicId) {
        if (logicId.equals(logicId1)) {
            return disThresh1;
        } else return disThresh2;
    }

    public double getClearwayL(String logicId) {
        if (logicId.equals(logicId1)) {
            return clearwayL1;
        } else return clearwayL2;
    }

    public double getClearwayW() {return clearwayW;}
    public String getLogicId1() {return logicId1;}
    public String getLogicId2() {return logicId2;}


    public int getDegree1() {return degree1;}


    public int getDegree2() {return degree2;}

    public double getResaL() {return resaL;}
}
