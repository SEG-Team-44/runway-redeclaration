package com.team44.runwayredeclarationapp.model;

public class PRunway extends Runway {

    private char pos1;
    private char pos2;

    public PRunway(int d1, int d2, char pos1, char pos2, double[] parameters) {
        super(d1, d2, pos1, pos2, parameters);

        this.pos1 = pos1;
        this.pos2 = pos2;

        setPhyId();
        setLogicId();
    }

    @Override
    protected void setPhyId() {
        super.setPhyId();
        phyId = phyId.substring(0, 2) + pos1 + phyId.substring(2) + pos2;
    }

    @Override
    protected void setLogicId() {
        super.setLogicId();
        logicId1 += pos1;
        logicId2 += pos2;
    }

    public char getPos1() {return pos1;}

    public char getPos2() {return pos2;}
}
