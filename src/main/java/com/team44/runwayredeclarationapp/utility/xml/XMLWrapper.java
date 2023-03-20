package com.team44.runwayredeclarationapp.utility.xml;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Obstacle;
import java.util.ArrayList;

/**
 * The root XML wrapper, used to store both the list of airports and obstacles
 */
public class XMLWrapper {

    /**
     * List of airports
     */
    private final AirportXMLObj[] airportXMLObjs;
    /**
     * List of obstacles
     */
    private final Obstacle[] obstacles;

    /**
     * Create the root XML wrapper
     *
     * @param airportXMLObjs the list of airports
     * @param obstacles      the list of obstacles
     */
    public XMLWrapper(AirportXMLObj[] airportXMLObjs, Obstacle[] obstacles) {
        this.airportXMLObjs = airportXMLObjs;
        this.obstacles = obstacles;
    }

    /**
     * Get the list of airport XML objects
     *
     * @return the list of airport xml objects
     */
    public AirportXMLObj[] getAirportXMLObjs() {
        return airportXMLObjs;
    }

    /**
     * Get the list of obstacles
     *
     * @return the list of obstacles
     */
    public Obstacle[] getObstacles() {
        return obstacles;
    }

    /**
     * Get the list of xml airport objects that have been converted to regular airport objects
     *
     * @return the list of airport objects
     */
    public Airport[] getAirports() {
        var airportsArray = new ArrayList<Airport>();

        // Convert each of the airport xml objects to regular objects
        for (AirportXMLObj airport : airportXMLObjs) {
            airportsArray.add(airport.toAirport());
        }

        // Return the list of airports
        return airportsArray.toArray(Airport[]::new);
    }
}
