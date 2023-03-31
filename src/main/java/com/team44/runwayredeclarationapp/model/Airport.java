package com.team44.runwayredeclarationapp.model;

import java.util.Iterator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Airport {

    /**
     * Name of airport
     */
    private String name;

    /**
     * Runways on airport
     */
    private ObservableList<Runway> runways;

    public Airport(String name) {
        this.name = name;

        runways = FXCollections.observableArrayList();
    }

    /**
     * Get the name of the airport
     *
     * @return the name of the airport
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the airport
     *
     * @param name the name of the airport
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Add a runway to the airport
     *
     * @param newRunway the runway to add
     */
    public void addRunway(Runway newRunway) {
        runways.add(newRunway);
    }

    /**
     * Get the observable list of runways of the airport
     *
     * @return observable list of runways
     */
    public ObservableList<Runway> getRunwayObservableList() {
        return runways;
    }

    /**
     * Get the list of runways of the airport
     *
     * @return the list of runways
     */
    public List<Runway> getRunways() {
        return runways;
    }

    /**
     * Get a specific runway in the airport
     *
     * @param id the id of the runway to get
     * @return the runway
     */
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
}
