package com.team44.runwayredeclarationapp.model;

public class PRunway extends Runway {

    private char pos;
    private char logicPos;

    public PRunway(int d, char pos, double rl, double rw, double stripL, double stripW, double stopwayL, double stopwayW, double clearwayL, double clearwayW, double tora, double toda, double asda, double lda, double disThresh) {
        super(d, pos, rl, rw, stripL, stripW, stopwayL, stopwayW, clearwayL, clearwayW, tora, toda, asda, lda, disThresh);
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
