package com.team44.runwayredeclarationapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test to see if the calculated getter values of the runway are correct
 */
class RunwayTest {

    private static final Logger logger = LogManager.getLogger(RunwayTest.class);

    /**
     * Runway object representing 09L/27R
     */
    PRunway runway09L27R = new PRunway(9, 27, 'L', 'R', new double[]{
        3902,//runwayL
        100,//runwayW
        60,//stripL
        100,//stripW
        100,//clearwayW
        240, //resaL
        3902,//tora1
        3902,//toda1
        3902,//asda1
        3595,//lda1
        3884,//tora2
        3962,//toda2
        3884,//asda2
        3884,//lda2
        0,//disThresh1
        306//disThresh2
    });

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
    @DisplayName("Getting the stopway length")
    void getStopwayL() {
        assertEquals(0, runway09L27R.getStopwayL("09L"), "09L Stopway");
        assertEquals(0, runway09L27R.getStopwayL("27R"), "27R Stopway");
    }

    @Test
    @DisplayName("Getting the clearway length")
    void getClearwayL() {
        assertEquals(0, runway09L27R.getClearwayL("09L"), "09L Clearway");
        assertEquals(78, runway09L27R.getClearwayL("27R"), "27R Clearway");

    }

    @Test
    @DisplayName("Getting the id of logical runway 1")
    void getLogicId1() {
        assertEquals("09L", runway09L27R.getLogicId1(), "Logic ID 09L");
    }

    @Test
    @DisplayName("Getting the id of logical runway 2")
    void getLogicId2() {
        assertEquals("27R", runway09L27R.getLogicId2(), "Logic ID 27R");
    }

    @Test
    @DisplayName("Getting the physical id")
    void getPhyId() {
        assertEquals("09L/27R", runway09L27R.getPhyId(), "Physical ID of 09L/27R");
    }
}
