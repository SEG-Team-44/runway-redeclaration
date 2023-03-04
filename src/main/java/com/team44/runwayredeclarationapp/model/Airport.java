package com.team44.runwayredeclarationapp.model;

import java.util.ArrayList;
import java.util.Iterator;
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

    public Runway getRunway(String id) {
        Iterator<Runway> it = runways.iterator();

        while (it.hasNext()) {
            Runway runway = it.next();
            if(runway.getId().equals(id)) {
                return runway;
            }
        }

        return null;
    }
}
