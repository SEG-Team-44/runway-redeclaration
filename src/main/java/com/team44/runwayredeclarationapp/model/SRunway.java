package com.team44.runwayredeclarationapp.model;

public class SRunway extends Runway {

    public SRunway(int d, double rl, double rw, double stripL, double stripW, double stopwayL, double stopwayW, double clearwayL, double clearwayW, double tora, double toda, double asda, double lda, double disThresh, double resa) {
        super(d, rl, rw, stripL, stripW, stopwayL, stopwayW, clearwayL, clearwayW, tora, toda, asda, lda, disThresh, resa);
        setId();
    }


}
