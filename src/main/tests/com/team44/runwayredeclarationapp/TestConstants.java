package com.team44.runwayredeclarationapp;

import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.RunwayObstacle;
import com.team44.runwayredeclarationapp.model.SRunway;

/**
 * Test constants that can be used in the unit tests
 */
public class TestConstants {

    /**
     * Runway object representing 09L/27R
     */
    public static final PRunway RUNWAY_09L_27R = new PRunway(9, 27, 'L', 'R', new double[]{
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
    public static final PRunway RUNWAY_09R_27L = new PRunway(9, 27, 'R', 'L', new double[]{
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
     * Runway object representing 01/19 (from valid-import-1.xml)
     */
    public static final SRunway RUNWAY_01_19 = new SRunway(1, 19, new double[]{
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
     * Runway object representing 02C/20C (from valid-import-1.xml)
     */
    public static final PRunway RUNWAY_02C_20C = new PRunway(2, 20, 'C', 'C', new double[]{
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
     * Obstacle object representing the obstacle specified in Scenario 1 in the spec
     */
    public static final Obstacle OBSTACLE_1 = new Obstacle("Obstacle 1", 12);
    public static final RunwayObstacle RUNWAY_OBSTACLE_1 = new RunwayObstacle(OBSTACLE_1,
        RUNWAY_09L_27R,
        -50.0, 3646.0,
        0.0, 240);
    /**
     * Obstacle object representing the obstacle specified in Scenario 2 in the spec
     */
    public static final Obstacle OBSTACLE_2 = new Obstacle("Obstacle 2", 25);
    public static final RunwayObstacle RUNWAY_OBSTACLE_2 = new RunwayObstacle(OBSTACLE_2,
        RUNWAY_09R_27L,
        2853.0, 500.0,
        -20.0, 240);
    /**
     * Obstacle object representing the obstacle specified in Scenario 3 in the spec
     */
    public static final Obstacle OBSTACLE_3 = new Obstacle("Obstacle 3", 15);
    public static final RunwayObstacle RUNWAY_OBSTACLE_3 = new RunwayObstacle(OBSTACLE_3,
        RUNWAY_09R_27L,
        150.0, 3203.0,
        60.0, 240);
    /**
     * Obstacle object representing the obstacle specified in Scenario 4 in the spec
     */
    public static final Obstacle OBSTACLE_4 = new Obstacle("Obstacle 4", 20);
    public static final RunwayObstacle RUNWAY_OBSTACLE_4 = new RunwayObstacle(OBSTACLE_4,
        RUNWAY_09L_27R,
        3546.0, 50.0,
        20.0, 240);

}
