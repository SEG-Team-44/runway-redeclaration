package com.team44.runwayredeclarationapp.event;

import com.team44.runwayredeclarationapp.model.Airport;

/**
 * The listener called when an airport has been edited
 */
public interface EditAirportListener {

    /**
     * Called when an airport has been edited
     *
     * @param oldAirport the old airport before edited
     * @param newAirport the new airport after edited
     */
    void editAirport(Airport oldAirport, Airport newAirport);
}
