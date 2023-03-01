package com.team44.runwayredeclarationapp.model;

public class Runway {

    private int degree;
    private int logicDegree;
    private int runwayL;
    private int runwayW;
    private int stripL;
    private int stripW;
    private int stopwayL;
    private int stopwayW;
    private int clearwayL;
    private int clearwayW;
    private int tora;
    private int toda;
    private int asda;
    private int lda;
    private int disThresh;

    public Runway(int d, int rl, int rw, int stripL, int stripW, int stopwayL, int stopwayW, int clearwayL, int clearwayW, int tora, int toda, int asda, int lda, int disThresh) {
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

    public Runway(int d, char pos, int rl, int rw, int stripL, int stripW, int stopwayL, int stopwayW, int clearwayL, int clearwayW, int tora, int toda, int asda, int lda, int disThresh) {
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

    private void setLogicalDecree() {
        if (degree <= 18) {
            logicDegree = degree + 18;
        }

        else logicDegree = degree - 18;
    }

    public int getDegree() {return degree;}
    public int getLogicDegree() {return logicDegree;}
    public int getRunwayL() {return runwayL;}
    public int getRunwayW() {return runwayW;}
    public int getStripL() {return stripL;}
    public int getStripW() {return stripW;}
    public int getStopwayL() {return stopwayL;}
    public int getStopwayW() {return stopwayW;}
    public int getClearwayL() {return clearwayL;}
    public int getClearwayW() {return clearwayW;}
    public int getTORA() {return tora;}
    public int getTODA() {return toda;}
    public int getASDA() {return asda;}
    public int getLDA() {return lda;}
    public int getDisThresh() {return disThresh;}

}
