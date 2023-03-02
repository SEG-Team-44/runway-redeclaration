package com.team44.runwayredeclarationapp.model;

public class Obstacles {

    private String obstName;
    private double height;
    private double position;

    public Obstacles(String obstName, double height, double position){
        this.obstName = obstName;
        this.height = height;
        this.position = position;
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

    public void setPosition(double position){ this.position = position; }

    public double getPosition() { return position; }
}
