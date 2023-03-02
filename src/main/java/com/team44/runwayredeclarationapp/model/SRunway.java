package com.team44.runwayredeclarationapp.model;

public class SRunway extends Runway {

    public SRunway(int d, double rl, double rw, double stripL, double stripW, double stopwayL, double stopwayW, double clearwayL, double clearwayW,
                   double tora1, double toda1, double asda1, double lda1, double disThresh1, double resa,
                   double tora2, double toda2, double asda2, double lda2, double disThresh2) {

        super(d, rl, rw, stripL, stripW, stopwayL, stopwayW, clearwayL, clearwayW,
                tora1, toda1, asda1, lda1, disThresh1, resa,
                tora2, toda2, asda2, lda2, disThresh2);

        setId();
    }


}
