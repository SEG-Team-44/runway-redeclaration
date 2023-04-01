package com.team44.runwayredeclarationapp.event;

import com.team44.runwayredeclarationapp.model.Airport;

/**
 * The listener to add a new airport
 */
public interface AddAirportListener {

    /**
     * Called when an airport has been added/edited
     *
     * @param airport the airport object
     */
    void addAirport(Airport airport);
}
