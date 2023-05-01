package com.team44.runwayredeclarationapp.event;

import com.team44.runwayredeclarationapp.model.Runway;

/**
 * The listener to call when a runway has been edited
 */
public interface EditRunwayListener {


    /**
     * Called when a new runway has been edited
     *
     * @param oldRunway the old runway before edited
     * @param newRunway the new runway after edited
     */
    void editRunway(Runway oldRunway, Runway newRunway);

}
