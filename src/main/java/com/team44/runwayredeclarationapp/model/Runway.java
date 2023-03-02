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

    private double tora1;
    private double toda1;
    private double asda1;
    private double lda1;
    private double disThresh1;
    private double resa;
    private double tora2;
    private double toda2;
    private double asda2;
    private double lda2;
    private double disThresh2;

    public Runway(int d, double rl, double rw, double stripL, double stripW, double stopwayL, double stopwayW, double clearwayL, double clearwayW,
                  double tora1, double toda1, double asda1, double lda1, double disThresh1, double resa,
                  double tora2, double toda2, double asda2, double lda2, double disThresh2) {
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
        this.tora1 = tora1;
        this.toda1 = toda1;
        this.asda1 = asda1;
        this.lda1 = lda1;
        this.disThresh1 = disThresh1;
        this.resa = resa;
        this.tora2 = tora2;
        this.toda2 = toda2;
        this.asda2 = asda2;
        this.lda2 = lda2;
        this.disThresh2 = disThresh2;
    }

    public Runway(int d, char pos, double rl, double rw, double stripL, double stripW, double stopwayL, double stopwayW, double clearwayL, double clearwayW,
                  double tora1, double toda1, double asda1, double lda1, double disThresh1, double resa,
                  double tora2, double toda2, double asda2, double lda2, double disThresh2) {
        this.degree = d;
        setLogicalDecree();
        //Physical runway parameters
        this.runwayL = rl;
        this.runwayW = rw;
        this.stripL = stripL;
        this.stripW = stripW;
        this.stopwayL = stopwayL;
        this.stopwayW = stopwayW;
        this.clearwayL = clearwayL;
        this.clearwayW = clearwayW;

        //One logical runway parameters
        this.tora1 = tora1;
        this.toda1 = toda1;
        this.asda1 = asda1;
        this.lda1 = lda1;
        this.disThresh1 = disThresh1;
        this.resa = resa;

        //The other logical runway parameters
        this.tora2 = tora2;
        this.toda2 = toda2;
        this.asda2 = asda2;
        this.lda2 = lda2;
        this.disThresh2 = disThresh2;
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

    public double getTORA1() {return tora1;}
    public double getTODA1() {return toda1;}
    public double getASDA1() {return asda1;}
    public double getLDA1() {return lda1;}
    public double getDisThresh1() {return disThresh1;}
    public double getResa() {return resa;}

    public double getTORA2() {return tora2;}
    public double getTODA2() {return toda2;}
    public double getASDA2() {return asda2;}
    public double getLDA2() {return lda2;}
    public double getDisThresh2() {return disThresh2;}
}
