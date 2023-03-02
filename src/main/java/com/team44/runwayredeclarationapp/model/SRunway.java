package com.team44.runwayredeclarationapp.model;

public class SRunway extends Runway {

    public SRunway(int d1, int d2, double rl, double rw, double stripL, double stripW, double stopwayL, double stopwayW,
                   double clearwayL, double clearwayW, double tora1, double toda1, double asda1, double lda1, double disThresh1,
                   double tora2, double toda2, double asda2, double lda2, double disThresh2, double resa) {

        super(d1, d2, rl, rw, stripL, stripW, stopwayL, stopwayW, clearwayL, clearwayW,
                tora1, toda1, asda1, lda1, disThresh1,
                tora2, toda2, asda2, lda2, disThresh2, resa);
        setId();
    }


}
