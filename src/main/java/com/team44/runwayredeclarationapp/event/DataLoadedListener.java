package com.team44.runwayredeclarationapp.event;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Obstacle;

/**
 * The listener called when data has been loaded from the xml file
 */
public interface DataLoadedListener {

    /**
     * Called when data has been loaded
     *
     * @param airports  the list of airports
     * @param obstacles the list of obstacles
     */
    void load(Airport[] airports, Obstacle[] obstacles);
}
