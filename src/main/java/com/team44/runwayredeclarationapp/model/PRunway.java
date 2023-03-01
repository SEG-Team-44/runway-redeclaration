package com.team44.runwayredeclarationapp.model;

public class PRunway extends Runway {

    private char pos;
    private char logicPos;

    public PRunway(int d, char pos, int rl, int rw, int stripL, int stripW, int stopwayL, int stopwayW, int clearwayL, int clearwayW, int tora, int toda, int asda, int lda, int disThresh) {
        super(d, pos, rl, rw, stripL, stripW, stopwayL, stopwayW, clearwayL, clearwayW, tora, toda, asda, lda, disThresh);
        this.pos = pos;
        setLogicalPos();
    }

    private void setLogicalPos() {
        if (pos == 'L') {
            logicPos = 'R';
        }

        else logicPos = 'L';
    }

    public char getPos() {return pos;}

    public char getLogicPos() {return logicPos;}
}
