package com.team44.runwayredeclarationapp.event;

import com.team44.runwayredeclarationapp.model.Runway;

/**
 * The listener to set a new runway to the GUI
 */
public interface NewRunwayListener {

    /**
     * Called when a new runway has been created/selected/updated
     *
     * @param runway the runway object
     */
    void newRunway(Runway runway);

}
