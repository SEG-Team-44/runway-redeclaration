package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.event.RecalculatedRunwayListener;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.RunwayObstacle;
import java.util.HashMap;
import java.util.Objects;

/**
 * The class responsible for carrying out the calculations and updating the GUI after
 */
public class RecalculationController {

    /**
     * The listener to call after the runway has been recalculated
     */
    private RecalculatedRunwayListener recalculatedRunwayListener;

    /**
     * The data controller of the program
     */
    private DataController dataController;

    /**
     * Create a recalculation controller
     */
    public RecalculationController() {
    }

    /**
     * Create a recalculation controller
     *
     * @param dataController the data controller of the program (used to log the process)
     */
    public RecalculationController(DataController dataController) {
        this.dataController = dataController;
    }

    /**
     * Recalculate the TORA parameter
     *
     * @param runway          the runway
     * @param logicID         the id of the logical runway
     * @param rwObst          the obstacle on the runway
     * @param blastProtection the blast protection value
     * @return the new TORA value
     */
    public HashMap<String, Double> recalculateTORA(Runway runway, String logicID,
        RunwayObstacle rwObst,
        double blastProtection) {
        // Create hashmap to store all the parameters necessary for the calculations
        var valueMap = new HashMap<String, Double>();

        // Work out the appropriate data values
        var runway1 = runway.getLogicId1();
        var runway2 = runway.getLogicId2();

        // Work out the sides
        var isRunway1Or2 = Objects.equals(logicID, runway1) ? 1 : 2;
        var isObstacleLeftOrRight = rwObst.isObstacleOnRightOfRunway() ? 1 : 2;

        var slopeCalc = rwObst.getObst().getSlope();

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
                isRunway1Or2 == 1 ? rwObst.getPositionL() : rwObst.getPositionR());
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
                isRunway1Or2 == 1 ? rwObst.getPositionL() : rwObst.getPositionR());
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

        valueMap.put("recalTORA", result);
        return valueMap;
    }


    /**
     * Recalculate the ASDA parameter
     *
     * @param runway  the runway
     * @param logicID the id of the logical runway
     * @param rwObst  the obstacle on the runway
     * @param tora    the recalculated tora value
     * @return the new ASDA value
     */
    public HashMap<String, Double> recalculateASDA(Runway runway, String logicID,
        RunwayObstacle rwObst,
        double tora) {
        // Create hashmap to store all the parameters necessary for the calculations
        var valueMap = new HashMap<String, Double>();

        // Work out the appropriate data values
        var runway1 = runway.getLogicId1();
        var runway2 = runway.getLogicId2();

        // Work out the sides
        var isRunway1Or2 = Objects.equals(logicID, runway1) ? 1 : 2;
        var isObstacleLeftOrRight = rwObst.isObstacleOnRightOfRunway() ? 1 : 2;

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

        valueMap.put("recalASDA", result);
        return valueMap;
    }


    /**
     * Recalculate the TODA parameter
     *
     * @param runway  the runway
     * @param logicID the id of the logical runway
     * @param rwObst  the obstacle on the runway
     * @param tora    the recalculated tora value
     * @return the new TODA value
     */

    public HashMap<String, Double> recalculateTODA(Runway runway, String logicID,
        RunwayObstacle rwObst, double tora) {

        // Create hashmap to store all the parameters necessary for the calculations
        var valueMap = new HashMap<String, Double>();

        // Work out the appropriate data values
        var runway1 = runway.getLogicId1();
        var runway2 = runway.getLogicId2();

        // Work out the sides
        var isRunway1Or2 = Objects.equals(logicID, runway1) ? 1 : 2;
        var isObstacleLeftOrRight = rwObst.isObstacleOnRightOfRunway() ? 1 : 2;

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

        valueMap.put("recalTODA", result);
        return valueMap;
    }

    /**
     * Recalculate the LDA parameter
     *
     * @param runway  the runway
     * @param logicID the id of the logical runway
     * @param rwObst  the obstacle on the runway
     * @return the new LDA value
     */
    public HashMap<String, Double> recalculateLDA(Runway runway, String logicID,
        RunwayObstacle rwObst) {
        // Create hashmap to store all the parameters necessary for the calculations
        var valueMap = new HashMap<String, Double>();

        // Work out the appropriate data values
        var runway1 = runway.getLogicId1();

        // Work out the sides
        var isRunway1Or2 = Objects.equals(logicID, runway1) ? 1 : 2;
        var isObstacleLeftOrRight = rwObst.isObstacleOnRightOfRunway() ? 1 : 2;

        // Calculate the slope angle of the obstacle
        var slopeCalc = rwObst.getObst().getSlope();

        double result;

        // Take off towards
        if (isRunway1Or2 != isObstacleLeftOrRight) {
            // Add the values to the hashmap
            valueMap.put("ogLDA", runway.getLda(logicID));
            valueMap.put("distanceFromThreshold",
                isRunway1Or2 == 1 ? rwObst.getPositionL() : rwObst.getPositionR());
            valueMap.put("slopeCalculation", slopeCalc);
            valueMap.put("stripEnd", runway.getStripL());

            result = valueMap.get("ogLDA") * 2;
        }
        // Take off away
        else {
            // Add the values to the hashmap
            valueMap.put("distanceFromThreshold",
                isRunway1Or2 == 1 ? rwObst.getPositionL() : rwObst.getPositionR());
            valueMap.put("stripEnd", runway.getStripL());
            valueMap.put("resa", runway.getResaL());

            result = valueMap.get("distanceFromThreshold") * 2;
        }

        // Take away every other element from the first value
        for (Double val : valueMap.values()) {
            result -= val;
        }

        valueMap.put("recalLDA", result);
        return valueMap;
    }

    public boolean isTakeOffTowards(String currentLogicRw, String runway1, RunwayObstacle rwObst) {

        // Work out the sides
        var isRunway1Or2 = Objects.equals(currentLogicRw, runway1) ? 1 : 2;
        var isObstacleLeftOrRight = rwObst.isObstacleOnRightOfRunway() ? 1 : 2;

        return isRunway1Or2 != isObstacleLeftOrRight;
    }

    /**
     * Set the listener to send the recalculated runway back to the view
     *
     * @param recalculatedRunwayListener the listener
     */
    public void setRecalculatedRunwayListener(
        RecalculatedRunwayListener recalculatedRunwayListener) {
        this.recalculatedRunwayListener = recalculatedRunwayListener;
    }

    /**
     * Recalculate a given runway
     *
     * @param rwObst the runway-obstacle pairing
     * @return the updated runway
     */
    public Runway recalculateRunway(RunwayObstacle rwObst) {
        // Clone the current runway to edit
        var runway = rwObst.getOriginalRw();
        var recalculatedRunway = runway.clone();
        var blastProtection = rwObst.getBlastPro();

        var runway1ID = runway.getLogicId1();
        var runway2ID = runway.getLogicId2();

        double tora1, asda1, toda1, lda1;
        double tora2, asda2, toda2, lda2;

        // Recalculate the parameters separately
        // Logical Runway 1
        tora1 = recalculateTORA(runway, runway1ID, rwObst, blastProtection).get("recalTORA");
        asda1 = recalculateASDA(runway, runway1ID, rwObst, tora1).get("recalASDA");
        toda1 = recalculateTODA(runway, runway1ID, rwObst, tora1).get("recalTODA");
        lda1 = recalculateLDA(runway, runway1ID, rwObst).get("recalLDA");

        // Logical Runway 2
        tora2 = recalculateTORA(runway, runway2ID, rwObst, blastProtection).get("recalTORA");
        asda2 = recalculateASDA(runway, runway2ID, rwObst, tora2).get("recalASDA");
        toda2 = recalculateTODA(runway, runway2ID, rwObst, tora2).get("recalTODA");
        lda2 = recalculateLDA(runway, runway2ID, rwObst).get("recalLDA");

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
        if (recalculatedRunwayListener != null) {
            var newRunwayObstacle = new RunwayObstacle(rwObst.getObst(), recalculatedRunway,
                rwObst.getPositionL(), rwObst.getPositionR(), rwObst.getDistCR(),
                rwObst.getBlastPro());

            // Update the runway on gui
            recalculatedRunwayListener.updateRunway(newRunwayObstacle);
        }

        // Log the action
        if (dataController != null) {
            dataController.logAction("Recalculated",
                "Runway (" + runway.getPhyId() + ") has been recalculated.");
        }

        return recalculatedRunway;
    }
}
