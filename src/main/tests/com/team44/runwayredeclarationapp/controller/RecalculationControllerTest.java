package com.team44.runwayredeclarationapp.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.model.PRunway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test to see if the recalculated values are correct
 */
class RecalculationControllerTest {

    private static final Logger logger = LogManager.getLogger(RecalculationController.class);

    /**
     * The recalculation controller responsible for recalculating
     */
    RecalculationController recalculationController;

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
     * Runway object representing 09R/27L
     */
    PRunway runway09R27L = new PRunway(9, 27, 'R', 'L', new double[]{
        3660,//runwayL
        100,//runwayW
        60,//stripL
        100,//stripW
        100,//clearwayW
        240, //resaL
        3660,//tora1
        3660,//toda1
        3660,//asda1
        3353,//lda1
        3660,//tora2
        3660,//toda2
        3660,//asda2
        3660,//lda2
        0,//disThresh1
        307//disThresh2
    });

    /**
     * Obstacle object representing the obstacle specified in Scenario 1 in the spec
     */
    Obstacle obstacle1 = new Obstacle("Scenario 1 Obs", 12, -50, 3646);
    /**
     * Obstacle object representing the obstacle specified in Scenario 2 in the spec
     */
    Obstacle obstacle2 = new Obstacle("Scenario 2 Obs", 25, 2853, 500);
    /**
     * Obstacle object representing the obstacle specified in Scenario 2 in the spec
     */
    Obstacle obstacle3 = new Obstacle("Scenario 3 Obs", 15, 150, 3203);
    /**
     * Obstacle object representing the obstacle specified in Scenario 2 in the spec
     */
    Obstacle obstacle4 = new Obstacle("Scenario 4 Obs", 20, 3546, 50);

    /**
     * Set up a new recalculation controller before each test
     */
    @BeforeEach
    void setUp() {
        logger.info("Running test");
        recalculationController = new RecalculationController();
    }

    @AfterEach
    void tearDown() {
        logger.info("Test done");
    }

    @Test
    @DisplayName("Recalculate scenario 1")
    void recalculateRunwayScenario1() {
        testPRunway(runway09L27R, obstacle1,
            3346, 3346, 3346, 2985,
            2986, 2986, 2986, 3346);
    }

    @Test
    @DisplayName("Recalculate scenario 2")
    void recalculateRunwayScenario2() {
        testPRunway(runway09R27L, obstacle2,
            1850, 1850, 1850, 2553,
            2860, 2860, 2860, 1850);
    }

    @Test
    @DisplayName("Recalculate scenario 3")
    void recalculateRunwayScenario3() {
        testPRunway(runway09R27L, obstacle3,
            2903, 2903, 2903, 2393,
            2393, 2393, 2393, 2903);
    }

    @Test
    @DisplayName("Recalculate scenario 4")
    void recalculateRunwayScenario4() {
        testPRunway(runway09L27R, obstacle4,
            2792, 2792, 2792, 3246,
            3534, 3612, 3534, 2774);
    }

    /**
     * Test that the recalculated parameters of a parallel runway is correct
     *
     * @param ogRunway the original runway (before recalculating)
     * @param obstacle the obstacle on the runway
     * @param newTora1 the expected recalculated TORA value for logical runway 1
     * @param newToda1 the expected recalculated TODA value for logical runway 1
     * @param newAsda1 the expected recalculated ASDA value for logical runway 1
     * @param newLda1  the expected recalculated LDA value for logical runway 1
     * @param newTora2 the expected recalculated TORA value for logical runway 2
     * @param newToda2 the expected recalculated TODA value for logical runway 2
     * @param newAsda2 the expected recalculated ASDA value for logical runway 2
     * @param newLda2  the expected recalculated LDA value for logical runway 2
     */
    private void testPRunway(PRunway ogRunway, Obstacle obstacle,
        double newTora1, double newToda1, double newAsda1, double newLda1,
        double newTora2, double newToda2, double newAsda2, double newLda2) {
        // Get the IDs of both logical runways
        var runway1ID = ogRunway.getLogicId1();
        var runway2ID = ogRunway.getLogicId2();

        // Recalculate with the class method
        var actualRunway = recalculationController.recalculateRunway(ogRunway, obstacle,
            300);

        // Specify the expected recalculated runway object
        var expectedRunway = new PRunway(
            ogRunway.getDegree1(), ogRunway.getDegree2(), ogRunway.getPos1(), ogRunway.getPos2(),
            new double[]{
                ogRunway.getRunwayL(),//runwayL
                ogRunway.getRunwayW(),//runwayW
                ogRunway.getStripL(),//stripL
                ogRunway.getStripW(),//stripW
                ogRunway.getClearwayW(),//clearwayW
                ogRunway.getResaL(), //resaL
                newTora1,//tora1
                newToda1,//toda1
                newAsda1,//asda1
                newLda1,//lda1
                newTora2,//tora2
                newToda2,//toda2
                newAsda2,//asda2
                newLda2,//lda2
                ogRunway.getDisThresh(runway1ID),//disThresh1
                ogRunway.getDisThresh(runway2ID)//disThresh2
            });

        // Assert all the class variables
        assertAll("Assertion of Runway variables",
            () -> assertEquals(expectedRunway.getRunwayL(), actualRunway.
                getRunwayL(), "RunwayL"),
            () -> assertEquals(expectedRunway.getRunwayW(), actualRunway.
                getRunwayW(), "RunwayW"),
            () -> assertEquals(expectedRunway.getStripL(), actualRunway.
                getStripL(), "StripL"),
            () -> assertEquals(expectedRunway.getStripW(), actualRunway.
                getStripW(), "StripW"),
            () -> assertEquals(expectedRunway.getClearwayW(), actualRunway.
                    getClearwayW(),
                "ClearwayW"),
            () -> assertEquals(expectedRunway.getResaL(), actualRunway.
                getResaL(), "ResaL"),
            () -> assertEquals(expectedRunway.getTora(runway1ID), actualRunway.
                    getTora(runway1ID),
                "Tora1"),
            () -> assertEquals(expectedRunway.getToda(runway1ID), actualRunway.
                    getToda(runway1ID),
                "Toda1"),
            () -> assertEquals(expectedRunway.getAsda(runway1ID), actualRunway.
                    getAsda(runway1ID),
                "Asda1"),
            () -> assertEquals(expectedRunway.getLda(runway1ID), actualRunway.
                    getLda(runway1ID),
                "Lda1"),
            () -> assertEquals(expectedRunway.getTora(runway2ID), actualRunway.
                    getTora(runway2ID),
                "Tora2"),
            () -> assertEquals(expectedRunway.getToda(runway2ID), actualRunway.
                    getToda(runway2ID),
                "Toda2"),
            () -> assertEquals(expectedRunway.getAsda(runway2ID), actualRunway.
                    getAsda(runway2ID),
                "Asda2"),
            () -> assertEquals(expectedRunway.getLda(runway2ID), actualRunway.
                    getLda(runway2ID),
                "Lda2"),
            () -> assertEquals(expectedRunway.getDisThresh(runway1ID),
                actualRunway.getDisThresh(runway1ID), "DisThresh1"),
            () -> assertEquals(expectedRunway.getDisThresh(runway2ID),
                actualRunway.getDisThresh(runway2ID), "DisThresh2")
        );
    }
}

