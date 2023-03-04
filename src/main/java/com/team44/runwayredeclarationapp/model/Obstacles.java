package com.team44.runwayredeclarationapp.model;

public class Obstacles {

    private String obstName;
    private double height;
    private double positionL;
    private double positionR;

    public Obstacles(String obstName, double height, double positionL, double positionR){
        this.obstName = obstName;
        this.height = height;
        this.positionL = positionL;
        this.positionR = positionR;
    }

    public String getObstName(){
        return obstName;
    }
    public double getHeight() {
        return height;
    }

    public void setObstName (String obstName){
        this.obstName = obstName;
    }

    public void setHeight (double height){
        this.obstName = obstName;
    }

    public void setPositionL(double position){ this.positionL = position; }

    public void setPositionR(double positionR) { this.positionR = positionR; }

    public double getPositionL() { return positionL; }

    public double getPositionR() { return positionR; }
}
