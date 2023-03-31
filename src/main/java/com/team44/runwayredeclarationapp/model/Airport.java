package com.team44.runwayredeclarationapp.model;

import java.util.Iterator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Airport {

    private ObservableList<Runway> runways;

    public Airport() {
        runways = FXCollections.observableArrayList();
    }

    public void addRunway(Runway newRunway) {
        runways.add(newRunway);
    }

    public ObservableList<Runway> getRunwayObservableList() {
        return runways;
    }

    public List<Runway> getRunways() {
        return runways;
    }

    public Runway getRunway(String id) {
        Iterator<Runway> it = runways.iterator();

        while (it.hasNext()) {
            Runway runway = it.next();
            if (runway.getPhyId().equals(id)) {
                return runway;
            }
        }

        return null;
    }

    public void removeRunway(Runway runway) {
        runways.remove(runway);
    }
}
