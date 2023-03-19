package com.team44.runwayredeclarationapp.event;

import com.team44.runwayredeclarationapp.model.RunwayObstacle;

/**
 * The listener to set the recalculated runway to the GUI
 */
public interface RecalculatedRunwayListener {

    /**
     * Called when a runway has been recalculated
     *
     * @param runwayObstacle the runway-obstacle object
     */
    void updateRunway(RunwayObstacle runwayObstacle);
}
