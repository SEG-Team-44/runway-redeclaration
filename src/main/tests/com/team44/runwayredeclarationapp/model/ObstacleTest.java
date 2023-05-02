package com.team44.runwayredeclarationapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test to see if the obstacle values are correct
 */
class ObstacleTest {

    private static final Logger logger = LogManager.getLogger(ObstacleTest.class);


    /**
     * Obstacle objects with given heights
     */
    Obstacle obstacle1 = new Obstacle("Scenario 1", 12);
    Obstacle obstacle2 = new Obstacle("Scenario 2", 25);
    Obstacle obstacle3 = new Obstacle("Scenario 3", 15);
    Obstacle obstacle4 = new Obstacle("Scenario 4", 20);

    /**
     * Set up a new recalculation controller before each test
     */
    @BeforeEach
    void setUp() {
        logger.info("Running test");
    }

    @AfterEach
    void tearDown() {
        logger.info("Test done");
    }

    @Test
    @DisplayName("Getting the slope calculation")
    void getSlope() {
        assertEquals(600, obstacle1.getSlope(), "Slope of obstacle with height 12");
        assertEquals(1250, obstacle2.getSlope(), "Slope of obstacle with height 25");
        assertEquals(750, obstacle3.getSlope(), "Slope of obstacle with height 15");
        assertEquals(1000, obstacle4.getSlope(), "Slope of obstacle with height 20");
    }
}
