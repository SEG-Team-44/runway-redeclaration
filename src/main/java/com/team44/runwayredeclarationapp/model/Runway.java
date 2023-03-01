package com.team44.runwayredeclarationapp.model;

public class Runway {

    protected String id;
    protected int degree;
    protected int logicDegree;
    private double runwayL;
    private double runwayW;
    private double stripL;
    private double stripW;
    private double stopwayL;
    private double stopwayW;
    private double clearwayL;
    private double clearwayW;
    private double tora;
    private double toda;
    private double asda;
    private double lda;
    private double disThresh;

    public Runway(int d, double rl, double rw, double stripL, double stripW, double stopwayL, double stopwayW, double clearwayL, double clearwayW, double tora, double toda, double asda, double lda, double disThresh) {
        this.degree = d;
        setLogicalDecree();
        this.runwayL = rl;
        this.runwayW = rw;
        this.stripL = stripL;
        this.stripW = stripW;
        this.stopwayL = stopwayL;
        this.stopwayW = stopwayW;
        this.clearwayL = clearwayL;
        this.clearwayW = clearwayW;
        this.tora = tora;
        this.toda = toda;
        this.asda = asda;
        this.lda = lda;
        this.disThresh = disThresh;
    }

    public Runway(int d, char pos, double rl, double rw, double stripL, double stripW, double stopwayL, double stopwayW, double clearwayL, double clearwayW, double tora, double toda, double asda, double lda, double disThresh) {
        this.degree = d;
        setLogicalDecree();
        this.runwayL = rl;
        this.runwayW = rw;
        this.stripL = stripL;
        this.stripW = stripW;
        this.stopwayL = stopwayL;
        this.stopwayW = stopwayW;
        this.clearwayL = clearwayL;
        this.clearwayW = clearwayW;
        this.tora = tora;
        this.toda = toda;
        this.asda = asda;
        this.lda = lda;
        this.disThresh = disThresh;
    }

    protected void setId() {
        if (degree < 10) {
            id = "0" + degree;
        }
        else id = String.valueOf(degree);

        if (logicDegree < 10) {
            id += "/0" + logicDegree;
        }

        else id += String.valueOf(logicDegree);
    }

    private void setLogicalDecree() {
        if (degree <= 18) {
            logicDegree = degree + 18;
        }

        else logicDegree = degree - 18;
    }

    public int getDegree() {return degree;}
    public int getLogicDegree() {return logicDegree;}
    public double getRunwayL() {return runwayL;}
    public double getRunwayW() {return runwayW;}
    public double getStripL() {return stripL;}
    public double getStripW() {return stripW;}
    public double getStopwayL() {return stopwayL;}
    public double getStopwayW() {return stopwayW;}
    public double getClearwayL() {return clearwayL;}
    public double getClearwayW() {return clearwayW;}
    public double getTORA() {return tora;}
    public double getTODA() {return toda;}
    public double getASDA() {return asda;}
    public double getLDA() {return lda;}
    public double getDisThresh() {return disThresh;}

}
