package com.team44.runwayredeclarationapp.model;

public class Obstacles {

    private String obstName;
    private int height;

    public Obstacles(String obstName, int height){
        this.obstName = obstName;
        this.height = height;
    }

    public String getObstName(){
        return obstName;
    }
    public int getHeight() {
        return height;
    }

    public void setObstName (String obstName){
        this.obstName = obstName;
    }

    public void setHeight (int height){
        this.obstName = obstName;
    }


}
