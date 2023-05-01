package com.team44.runwayredeclarationapp.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.team44.runwayredeclarationapp.TestConstants;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.RunwayObstacle;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Test to see if the recalculated values are correct
 */
@TestMethodOrder(OrderAnnotation.class)
class RecalculationControllerTest {

    private static final Logger logger = LogManager.getLogger(RecalculationController.class);

    /**
     * The recalculation controller responsible for recalculating
     */
    RecalculationController recalculationController;

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

    @Order(1)
    @Test
    @DisplayName("Recalculate scenario 1")
    void recalculateRunwayScenario1() {
        testPRunway(TestConstants.RUNWAY_09L_27R, TestConstants.RUNWAY_OBSTACLE_1,
            3346, 3346, 3346, 2985,
            2986, 2986, 2986, 3346);
    }

    @Order(2)
    @Test
    @DisplayName("Recalculate scenario 2")
    void recalculateRunwayScenario2() {
        testPRunway(TestConstants.RUNWAY_09R_27L, TestConstants.RUNWAY_OBSTACLE_2,
            1850, 1850, 1850, 2553,
            2860, 2860, 2860, 1850);
    }

    @Order(3)
    @Test
    @DisplayName("Recalculate scenario 3")
    void recalculateRunwayScenario3() {
        testPRunway(TestConstants.RUNWAY_09R_27L, TestConstants.RUNWAY_OBSTACLE_3,
            2903, 2903, 2903, 2393,
            2393, 2393, 2393, 2903);
    }

    @Order(4)
    @Test
    @DisplayName("Recalculate scenario 4")
    void recalculateRunwayScenario4() {
        testPRunway(TestConstants.RUNWAY_09L_27R, TestConstants.RUNWAY_OBSTACLE_4,
            2792, 2792, 2792, 3246,
            3534, 3612, 3534, 2774);
    }

    @Order(5)
    @Test
    @DisplayName("Calculation breakdown TORA (Logical Runway 1)")
    void tora1CalculationBreakdown() {
        var expected = new HashMap<String, Double>();
        expected.put("displacedThreshold", 306.0);
        expected.put("distanceFromThreshold", -50.0);
        expected.put("blastProtection", 300.0);
        expected.put("recalTORA", 3346.0);
        expected.put("ogTORA", 3902.0);

        assertTrue(testRecalculatedHashmaps(expected,
            recalculationController.recalculateTORA(TestConstants.RUNWAY_09L_27R, "09L",
                TestConstants.RUNWAY_OBSTACLE_1, 300)));
    }

    @Order(6)
    @Test
    @DisplayName("Calculation breakdown TORA (Logical Runway 2)")
    void tora2CalculationBreakdown() {
        var expected = new HashMap<String, Double>();
        expected.put("distanceFromThreshold", 3646.0);
        expected.put("slopeCalculation", 600.0);
        expected.put("stripEnd", 60.0);
        expected.put("recalTORA", 2986.0);

        assertTrue(testRecalculatedHashmaps(expected,
            recalculationController.recalculateTORA(TestConstants.RUNWAY_09L_27R, "27R",
                TestConstants.RUNWAY_OBSTACLE_1, 300)));
    }

    @Order(7)
    @Test
    @DisplayName("Calculation breakdown TODA (Logical Runway 1)")
    void toda1CalculationBreakdown() {
        var expected = new HashMap<String, Double>();
        expected.put("clearway", 0.0);
        expected.put("recalTODA", 300.0);
        expected.put("tora", 300.0);

        assertTrue(testRecalculatedHashmaps(expected,
            recalculationController.recalculateTODA(TestConstants.RUNWAY_09L_27R, "09L",
                TestConstants.RUNWAY_OBSTACLE_1, 300)));
    }

    @Order(8)
    @Test
    @DisplayName("Calculation breakdown TODA (Logical Runway 2)")
    void toda2CalculationBreakdown() {
        var expected = new HashMap<String, Double>();
        expected.put("recalTODA", 300.0);
        expected.put("tora", 300.0);

        assertTrue(testRecalculatedHashmaps(expected,
            recalculationController.recalculateTODA(TestConstants.RUNWAY_09L_27R, "27R",
                TestConstants.RUNWAY_OBSTACLE_1, 300)));
    }

    @Order(9)
    @Test
    @DisplayName("Calculation breakdown ASDA (Logical Runway 1)")
    void asda1CalculationBreakdown() {
        var expected = new HashMap<String, Double>();
        expected.put("stopway", 0.0);
        expected.put("recalASDA", 300.0);
        expected.put("tora", 300.0);

        assertTrue(testRecalculatedHashmaps(expected,
            recalculationController.recalculateASDA(TestConstants.RUNWAY_09L_27R, "09L",
                TestConstants.RUNWAY_OBSTACLE_1, 300)));
    }

    @Order(10)
    @Test
    @DisplayName("Calculation breakdown ASDA (Logical Runway 2)")
    void asda2CalculationBreakdown() {
        var expected = new HashMap<String, Double>();
        expected.put("recalASDA", 300.0);
        expected.put("tora", 300.0);

        assertTrue(testRecalculatedHashmaps(expected,
            recalculationController.recalculateASDA(TestConstants.RUNWAY_09L_27R, "27R",
                TestConstants.RUNWAY_OBSTACLE_1, 300)));
    }

    @Order(111)
    @Test
    @DisplayName("Calculation breakdown LDA (Logical Runway 1)")
    void lda1CalculationBreakdown() {
        var expected = new HashMap<String, Double>();
        expected.put("distanceFromThreshold", -50.0);
        expected.put("slopeCalculation", 600.0);
        expected.put("recalLDA", 2985.0);
        expected.put("ogLDA", 3595.0);
        expected.put("stripEnd", 60.0);

        assertTrue(testRecalculatedHashmaps(expected,
            recalculationController.recalculateLDA(TestConstants.RUNWAY_09L_27R, "09L",
                TestConstants.RUNWAY_OBSTACLE_1)));
    }

    @Order(12)
    @Test
    @DisplayName("Calculation breakdown LDA (Logical Runway 2)")
    void lda2CalculationBreakdown() {
        var expected = new HashMap<String, Double>();
        expected.put("distanceFromThreshold", 3646.0);
        expected.put("resa", 240.0);
        expected.put("recalLDA", 3346.0);
        expected.put("stripEnd", 60.0);

        assertTrue(testRecalculatedHashmaps(expected,
            recalculationController.recalculateLDA(TestConstants.RUNWAY_09L_27R, "27R",
                TestConstants.RUNWAY_OBSTACLE_1)));
    }

    /**
     * Compare a hashmap with another hashmap, disregarding the order
     *
     * @param expected the expected outcome
     * @param actual   the actual outcome
     * @return whether the hashmaps are equal
     */
    private Boolean testRecalculatedHashmaps(HashMap<String, Double> expected,
        HashMap<String, Double> actual) {
        // Check if they're the same size
        if (actual.size() != expected.size()) {
            return false;
        }

        // Iterate through each outcome and compare with expected
        return actual.entrySet().stream().allMatch(stringDoubleEntry -> {
            return stringDoubleEntry.getValue().equals(expected.get(stringDoubleEntry.getKey()));
        });
    }

    /**
     * Test that the recalculated parameters of a parallel runway is correct
     *
     * @param ogRunway the original runway (before recalculating)
     * @param rwObs    the obstacle on the runway
     * @param newTora1 the expected recalculated TORA value for logical runway 1
     * @param newToda1 the expected recalculated TODA value for logical runway 1
     * @param newAsda1 the expected recalculated ASDA value for logical runway 1
     * @param newLda1  the expected recalculated LDA value for logical runway 1
     * @param newTora2 the expected recalculated TORA value for logical runway 2
     * @param newToda2 the expected recalculated TODA value for logical runway 2
     * @param newAsda2 the expected recalculated ASDA value for logical runway 2
     * @param newLda2  the expected recalculated LDA value for logical runway 2
     */
    private void testPRunway(PRunway ogRunway, RunwayObstacle rwObs,
        double newTora1, double newToda1, double newAsda1, double newLda1,
        double newTora2, double newToda2, double newAsda2, double newLda2) {
        // Get the IDs of both logical runways
        var runway1ID = ogRunway.getLogicId1();
        var runway2ID = ogRunway.getLogicId2();

        // Recalculate with the class method
        var actualRunway = recalculationController.recalculateRunway(rwObs);

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
