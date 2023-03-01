package com.team44.runwayredeclarationapp.model;

import java.util.ArrayList;
import java.util.List;

public class Airport {

    private String name;
    private List<Runway> runways;

    public Airport(String name) {
        this.name = name;
        runways = new ArrayList<>();
    }

    public void addRunway(Runway newRunway) {
        runways.add(newRunway);
    }

    public List<Runway> getRunways() {return runways;}
}
