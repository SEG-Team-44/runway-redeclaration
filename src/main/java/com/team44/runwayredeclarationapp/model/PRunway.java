package com.team44.runwayredeclarationapp.model;

public class PRunway extends Runway {

    private char pos;
    private char logicPos;

    public PRunway(int d, char pos, double rl, double rw, double stripL, double stripW, double stopwayL, double stopwayW, double clearwayL, double clearwayW,
                   double tora1, double toda1, double asda1, double lda1, double disThresh1, double resa,
                   double tora2, double toda2, double asda2, double lda2, double disThresh2) {

        super(d, pos, rl, rw, stripL, stripW, stopwayL, stopwayW, clearwayL, clearwayW,
                tora1, toda1, asda1, lda1, disThresh1, resa,
                tora2, toda2, asda2, lda2, disThresh2);

        this.pos = pos;
        setLogicalPos();
        setId();
    }

    private void setLogicalPos() {
        if (pos == 'L') {
            logicPos = 'R';
        }

        else logicPos = 'L';
    }

    @Override
    protected void setId() {
        super.setId();
        id = id.substring(0, 2) + pos + id.substring(2) + logicPos;
    }

    public char getPos() {return pos;}

    public char getLogicPos() {return logicPos;}
}
