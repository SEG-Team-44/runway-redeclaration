package com.team44.runwayredeclarationapp.event;

import com.team44.runwayredeclarationapp.model.Obstacle;

/**
 * The listener to add a new obstacle
 */
public interface AddObstacleListener {

    /**
     * Called when an obstacle has been added/edited
     *
     * @param obstacle the obstacle object
     */
    void addObstacle(Obstacle obstacle);
}
