package com.team44.runwayredeclarationapp.model;

import java.util.ArrayList;
import java.util.List;

public class Airport {


    private List<Runway> runways;

    public Airport() {
        runways = new ArrayList<>();
    }

    public void addRunway(Runway newRunway) {
        runways.add(newRunway);
    }

    public List<Runway> getRunways() {return runways;}
}
