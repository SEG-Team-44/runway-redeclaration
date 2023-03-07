package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.event.SetRunwayListener;
import com.team44.runwayredeclarationapp.model.Obstacles;
import com.team44.runwayredeclarationapp.model.Runway;
import java.util.HashMap;
import java.util.Objects;

/**
 * The class responsible for carrying out the calculations and updating the GUI after
 */
public class RecalculationController {

    /**
     * The listener to call after the runway has been recalculated
     */
    private SetRunwayListener setRunwayListener;

    /**
     * Recalculate the TORA parameter
     *
     * @param runway          the runway
     * @param logicID         the id of the logical runway
     * @param obstacle        the obstacle
     * @param blastProtection the blast protection value
     * @return the new TORA value
     */
    private double recalculateTORA(Runway runway, String logicID, Obstacles obstacle,
        double blastProtection) {
        // Create hashmap to store all the parameters necessary for the calculations
        var valueMap = new HashMap<String, Double>();

        // Work out the appropriate data values
        var runway1 = runway.getLogicId1();
        var runway2 = runway.getLogicId2();

        // Work out the sides
        var isRunway1Or2 = Objects.equals(logicID, runway1) ? 1 : 2;
        var isObstacleLeftOrRight = obstacle.isObstacleOnRightOfRunway() ? 1 : 2;

        var slopeCalc = obstacle.getSlope();

        double displacedThreshold;
        double result;

        // Take off towards
        if (isRunway1Or2 != isObstacleLeftOrRight) {
            // Set the displaced threshold for the correct side
            displacedThreshold = runway.getDisThresh(
                isObstacleLeftOrRight == 1 ? runway1 : runway2);

            // Add the values to the hashmap
            valueMap.put("ogTORA", runway.getTora(logicID));
            if (blastProtection >= (runway.getStripL() + runway.getResaL())) {
                valueMap.put("blastProtection", blastProtection);
            } else {
                valueMap.put("stripEnd", runway.getStripL());
                valueMap.put("resa", runway.getResaL());
            }
            valueMap.put("distanceFromThreshold",
                isRunway1Or2 == 1 ? obstacle.getPositionL() : obstacle.getPositionR());
            if (displacedThreshold > 0) {
                valueMap.put("displacedThreshold", displacedThreshold);
            }

            result = valueMap.get("ogTORA") * 2;
        }
        // Take off away
        else {
            // Set the displaced threshold for the correct side
            displacedThreshold = runway.getDisThresh(
                isObstacleLeftOrRight == 1 ? runway2 : runway1);

            // Add the values to the hashmap
            valueMap.put("distanceFromThreshold",
                isRunway1Or2 == 1 ? obstacle.getPositionL() : obstacle.getPositionR());
            if (displacedThreshold > 0) {
                valueMap.put("displacedThreshold", -displacedThreshold);
            }
            valueMap.put("slopeCalculation", slopeCalc);
            valueMap.put("stripEnd", runway.getStripL());

            result = valueMap.get("distanceFromThreshold") * 2;
        }

        // Take away every other element from the first value
        for (Double val : valueMap.values()) {
            result -= val;
        }

        return result;
    }


    /**
     * Recalculate the ASDA parameter
     *
     * @param runway   the runway
     * @param logicID  the id of the logical runway
     * @param obstacle the obstacle
     * @param tora     the recalculated tora value
     * @return the new ASDA value
     */
    private double recalculateASDA(Runway runway, String logicID, Obstacles obstacle,
        double tora) {
        // Create hashmap to store all the parameters necessary for the calculations
        var valueMap = new HashMap<String, Double>();

        // Work out the appropriate data values
        var runway1 = runway.getLogicId1();
        var runway2 = runway.getLogicId2();

        // Work out the sides
        var isRunway1Or2 = Objects.equals(logicID, runway1) ? 1 : 2;
        var isObstacleLeftOrRight = obstacle.isObstacleOnRightOfRunway() ? 1 : 2;

        // Get the stopway for the correct side
        var stopway = runway.getStopwayL(isRunway1Or2 == 1 ? runway1 : runway2);

        double result;

        // Add the values to the hashmap
        valueMap.put("tora", tora);
        // Take off towards
        if (isRunway1Or2 != isObstacleLeftOrRight) {
            valueMap.put("stopway", stopway);

            result = valueMap.get("tora") + valueMap.get("stopway");
        }
        // Take off away
        else {
            result = valueMap.get("tora");
        }

        return result;
    }


    /**
     * Recalculate the TODA parameter
     *
     * @param runway   the runway
     * @param logicID  the id of the logical runway
     * @param obstacle the obstacle
     * @param tora     the recalculated tora value
     * @return the new TODA value
     */
    private double recalculateTODA(Runway runway, String logicID, Obstacles obstacle, double tora) {
        // Create hashmap to store all the parameters necessary for the calculations
        var valueMap = new HashMap<String, Double>();

        // Work out the appropriate data values
        var runway1 = runway.getLogicId1();
        var runway2 = runway.getLogicId2();

        // Work out the sides
        var isRunway1Or2 = Objects.equals(logicID, runway1) ? 1 : 2;
        var isObstacleLeftOrRight = obstacle.isObstacleOnRightOfRunway() ? 1 : 2;

        // Get the clearway for the correct side
        var clearway = runway.getClearwayL(isRunway1Or2 == 1 ? runway1 : runway2);

        double result;

        // Add the values to the hashmap
        valueMap.put("tora", tora);
        // Take off towards
        if (isRunway1Or2 != isObstacleLeftOrRight) {
            valueMap.put("clearway", clearway);

            result = valueMap.get("tora") + valueMap.get("clearway");
        }
        // Take off away
        else {
            result = valueMap.get("tora");
        }

        return result;
    }

    /**
     * Recalculate the LDA parameter
     *
     * @param runway   the runway
     * @param logicID  the id of the logical runway
     * @param obstacle the obstacle
     * @return the new LDA value
     */
    private double recalculateLDA(Runway runway, String logicID, Obstacles obstacle) {
        // Create hashmap to store all the parameters necessary for the calculations
        var valueMap = new HashMap<String, Double>();

        // Work out the appropriate data values
        var runway1 = runway.getLogicId1();

        // Work out the sides
        var isRunway1Or2 = Objects.equals(logicID, runway1) ? 1 : 2;
        var isObstacleLeftOrRight = obstacle.isObstacleOnRightOfRunway() ? 1 : 2;

        // Calculate the slope angle of the obstacle
        var slopeCalc = obstacle.getSlope();

        double result;

        // Take off towards
        if (isRunway1Or2 != isObstacleLeftOrRight) {
            // Add the values to the hashmap
            valueMap.put("ogLDA", runway.getLda(logicID));
            valueMap.put("distanceFromThreshold",
                isRunway1Or2 == 1 ? obstacle.getPositionL() : obstacle.getPositionR());
            valueMap.put("slopeCalculation", slopeCalc);
            valueMap.put("stripEnd", runway.getStripL());

            result = valueMap.get("ogLDA") * 2;

        }
        // Take off away
        else {
            // Add the values to the hashmap
            valueMap.put("distanceFromThreshold",
                isRunway1Or2 == 1 ? obstacle.getPositionL() : obstacle.getPositionR());
            valueMap.put("stripEnd", runway.getStripL());
            valueMap.put("resa", runway.getResaL());

            result = valueMap.get("distanceFromThreshold") * 2;
        }

        // Take away every other element from the first value
        for (Double val : valueMap.values()) {
            result -= val;
        }

        return result;
    }

    /**
     * Set the listener to send the recalculated runway back to the view
     *
     * @param setRunwayListener the listener
     */
    public void setSetRunwayListener(SetRunwayListener setRunwayListener) {
        this.setRunwayListener = setRunwayListener;
    }

    /**
     * Recalculate a given runway
     *
     * @param runway          the runway
     * @param obstacle        the obstacle on the runway
     * @param blastProtection the blast protection value
     */
    public void recalculateRunway(Runway runway, Obstacles obstacle, double blastProtection) {
        // Clone the current runway to edit
        var recalculatedRunway = runway.clone();

        var runway1ID = runway.getLogicId1();
        var runway2ID = runway.getLogicId2();

        double tora1, asda1, toda1, lda1;
        double tora2, asda2, toda2, lda2;

        // Recalculate the parameters separately
        // Logical Runway 1
        tora1 = recalculateTORA(runway, runway1ID, obstacle, blastProtection);
        asda1 = recalculateASDA(runway, runway1ID, obstacle, tora1);
        toda1 = recalculateTODA(runway, runway1ID, obstacle, tora1);
        lda1 = recalculateLDA(runway, runway1ID, obstacle);

        // Logical Runway 2
        tora2 = recalculateTORA(runway, runway2ID, obstacle, blastProtection);
        asda2 = recalculateASDA(runway, runway2ID, obstacle, tora2);
        toda2 = recalculateTODA(runway, runway2ID, obstacle, tora2);
        lda2 = recalculateLDA(runway, runway2ID, obstacle);

        // Update the parameters of the cloned runway object
        recalculatedRunway.updateParameters(new double[]{
            runway.getRunwayL(), // runwayL
            runway.getRunwayW(), // runwayW
            runway.getStripL(), // stripL
            runway.getStripW(), // stripW
            runway.getClearwayW(), // clearwayW
            runway.getResaL(), // resaL
            tora1, // tora1
            toda1, // toda1
            asda1, // asda1
            lda1, // lda1
            tora2, // tora2
            toda2, // toda2
            asda2, // asda2
            lda2, // lda2
            runway.getDisThresh(runway.getLogicId1()), // disThresh1
            runway.getDisThresh(runway.getLogicId2()), // disThresh2
        });

        // Call the listener to update the GUI
        setRunwayListener.updateRunway(recalculatedRunway);
    }
}
