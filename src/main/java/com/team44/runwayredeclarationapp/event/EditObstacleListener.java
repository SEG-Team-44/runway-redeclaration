package com.team44.runwayredeclarationapp.event;

import com.team44.runwayredeclarationapp.model.Obstacle;

/**
 * The listener called when an obstacle has been edited
 */
public interface EditObstacleListener {

    /**
     * Called when an obstacle edited
     *
     * @param oldObstacle the obstacle object before being edited
     * @param newObstacle the obstacle object after being edited
     */
    void editObstacle(Obstacle oldObstacle, Obstacle newObstacle);
}
