package com.team44.runwayredeclarationapp.model;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The class representing an airport containing runways
 */
public class Airport implements Cloneable {

    /**
     * Name of airport
     */
    private String name;

    /**
     * Runways on airport
     */
    private final ObservableList<Runway> runways;

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

    /**
     * Set the list of runways of the airport
     *
     * @param runways the list of runways
     */
    public void setRunways(List<Runway> runways) {
        this.runways.setAll(runways);
    }

    /**
     * Remove specified runway from the airport
     *
     * @param runway the runway to remove
     */
    public void removeRunway(Runway runway) {
        runways.remove(runway);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Airport)) {
            return false;
        }
        Airport airport = (Airport) o;
        return name.equals(airport.name) && runways.equals(airport.runways);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, runways);
    }

    @Override
    public Airport clone() {
        try {
            return (Airport) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
