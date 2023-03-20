package com.team44.runwayredeclarationapp.utility.xml;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Runway;

/**
 * The airport object that is used in the XML, that uses a regular array of runways than an
 * observable list
 */
public class AirportXMLObj {

    private final Runway[] runways;

    /**
     * Create an airport XML object with a given airport object
     *
     * @param airport the airport object
     */
    public AirportXMLObj(Airport airport) {
        runways = airport.getRunways().toArray(Runway[]::new);
    }

    /**
     * Get the array of runways
     *
     * @return the array of runways
     */
    public Runway[] getRunways() {
        return runways;
    }

    /**
     * Convert the airport xml object into a regular airport object
     *
     * @return the airport object
     */
    public Airport toAirport() {
        var airport = new Airport();
        // Set the list of runways to the observable list
        airport.getRunwayObservableList().setAll(runways);

        return airport;
    }
}