package com.team44.runwayredeclarationapp.model;

public class PRunway extends Runway {

    private char pos1;
    private char pos2;

    public PRunway(int d1, int d2, char pos1, char pos2, double[] parameters) {

        super(d1, d2, pos1, pos2, parameters);

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
