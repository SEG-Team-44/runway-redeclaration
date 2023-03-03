package com.team44.runwayredeclarationapp.model;

public class Runway {

    protected String id;
    private double runwayL;
    private double runwayW;
    private double stripL;
    private double stripW;
    private double stopwayL;
    private double stopwayW;
    private double clearwayL;
    private double clearwayW;
    protected int degree1;
    private double tora1;
    private double toda1;
    private double asda1;
    private double lda1;
    private double disThresh1;
    private double resa;
    protected int degree2;
    private double tora2;
    private double toda2;
    private double asda2;
    private double lda2;
    private double disThresh2;

    public Runway(int d1, int d2, double rl, double rw, double stripL, double stripW, double clearwayW,
                  double tora1, double toda1, double asda1, double lda1, double disThresh1,
                  double tora2, double toda2, double asda2, double lda2, double disThresh2, double resa) {

        //Physical parameters
        this.runwayL = rl;
        this.runwayW = rw;
        this.stripL = stripL;
        this.stripW = stripW;
        this.stripW = stripW;
        this.stopwayL = asda1 - tora1;
        this.clearwayL = toda1 - tora1;
        this.clearwayW = clearwayW;

        //One logical parameters
        degree1 = d1;
        this.tora1 = tora1;
        this.toda1 = toda1;
        this.asda1 = asda1;
        this.lda1 = lda1;
        this.disThresh1 = disThresh1;

        //The other logical parameters
        degree2 = d2;
        this.tora2 = tora2;
        this.toda2 = toda2;
        this.asda2 = asda2;
        this.lda2 = lda2;
        this.disThresh2 = disThresh2;

        this.resa = resa;
    }

    public Runway(int d1, int d2, char pos1, char pos2, double rl, double rw, double stripL, double stripW, double clearwayW,
                  double tora1, double toda1, double asda1, double lda1, double disThresh1,
                  double tora2, double toda2, double asda2, double lda2, double disThresh2, double resa) {

        //Physical parameters
        this.runwayL = rl;
        this.runwayW = rw;
        this.stripL = stripL;
        this.stripW = stripW;
        this.stopwayL = asda1 - tora1;
        this.stopwayW = rw;
        this.clearwayL = toda1 - tora1;
        this.clearwayW = clearwayW;

        //One logical parameters
        degree1 = d1;
        this.tora1 = tora1;
        this.toda1 = toda1;
        this.asda1 = asda1;
        this.lda1 = lda1;
        this.disThresh1 = disThresh1;

        //The other logical parameters
        degree2 = d2;
        this.tora2 = tora2;
        this.toda2 = toda2;
        this.asda2 = asda2;
        this.lda2 = lda2;
        this.disThresh2 = disThresh2;

        this.resa = resa;
    }

    protected void setId() {
        if (degree1 < 10) {
            id = "0" + degree1;
        }
        else id = String.valueOf(degree1);

        if (degree2 < 10) {
            id += "/0" + degree2;
        }

        else id += "/" + degree2;
    }

    public String getId() {return id;}
    public int getDegree() {return degree1;}
    public int getLogicDegree() {return degree2;}
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

    public double getTORA2() {return tora2;}
    public double getTODA2() {return toda2;}
    public double getASDA2() {return asda2;}
    public double getLDA2() {return lda2;}
    public double getDisThresh2() {return disThresh2;}
    public double getResa() {return resa;}
}
