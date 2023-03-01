package com.team44.runwayredeclarationapp.model;

public class Runway {

    private int degree;
    private int runwayL;
    private int runwayW;
    private int stripL;
    private int stripW;
    private int stopwayL;
    private int tora;
    private int toda;
    private int asda;
    private int lda;
    private int disThresh;
    private int clearway;

    public Runway(int d, int rl, int rw, int stripL, int stripW, int stopwayL, int tora, int toda, int asda, int lda, int disThresh, int clearway) {
        this.degree = d;
        this.runwayL = rl;
        this.runwayW = rw;
        this.stripL = stripL;
        this.stripW = stripW;
        this.stopwayL = stopwayL;
        this.tora = tora;
        this.toda = toda;
        this.asda = asda;
        this.lda = lda;
        this.disThresh = disThresh;
        this.clearway = clearway;
    }

    public Runway(int d, char pos, int rl, int rw, int stripL, int stripW, int stopwayL, int tora, int toda, int asda, int lda, int disThresh, int clearway) {
        this.degree = d;
        this.runwayL = rl;
        this.runwayW = rw;
        this.stripL = stripL;
        this.stripW = stripW;
        this.stopwayL = stopwayL;
        this.tora = tora;
        this.toda = toda;
        this.asda = asda;
        this.lda = lda;
        this.disThresh = disThresh;
        this.clearway = clearway;
    }

    public int getDegree() {return degree;}
    public int getRunwayL() {return runwayL;}
    public int getRunwayW() {return runwayW;}
    public int getStripL() {return stripL;}
    public int getStripW() {return stripW;}
    public int getStopwayL() {return stopwayL;}
    public int getTORA() {return tora;}
    public int getTODA() {return toda;}
    public int getASDA() {return asda;}
    public int getLDA() {return lda;}
    public int getDisThresh() {return disThresh;}
    public int getClearway() {return clearway;}
}
