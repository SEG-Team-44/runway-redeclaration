package com.team44.runwayredeclarationapp.model;

public class PRunway extends Runway {

    private char pos1;
    private char pos2;

    public PRunway(int d1, int d2, char pos1, char pos2, double rl, double rw, double stripL, double stripW, double stopwayL, double stopwayW,
                   double clearwayL, double clearwayW, double tora1, double toda1, double asda1, double lda1, double disThresh1,
                   double tora2, double toda2, double asda2, double lda2, double disThresh2, double resa) {

        super(d1, d2, pos1, pos2, rl, rw, stripL, stripW, stopwayL, stopwayW, clearwayL, clearwayW,
                tora1, toda1, asda1, lda1, disThresh1,
                tora2, toda2, asda2, lda2, disThresh2, resa);

        this.pos1 = pos1;
        this.pos2 = pos2;
        setId();
    }

    @Override
    protected void setId() {
        super.setId();
        id = id.substring(0, 2) + pos1 + id.substring(2) + pos2;
    }

    public char getPos() {return pos1;}

    public char getLogicPos() {return pos2;}
}
