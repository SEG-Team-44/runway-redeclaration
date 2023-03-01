package com.team44.runwayredeclarationapp.model;

public class PRunway extends Runway {

    private char pos;

    public PRunway(int d, char pos, int rl, int rw, int stripL, int stripW, int stopwayL, int tora, int toda, int asda, int lda, int disThresh, int clearway) {
        super(d, pos, rl, rw, stripL, stripW, stopwayL, tora, toda, asda, lda, disThresh, clearway);
        this.pos = pos;
    }

    public char getPos() {return pos;}
}
