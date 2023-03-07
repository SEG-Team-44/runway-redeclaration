package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.event.SetRunwayListener;
import com.team44.runwayredeclarationapp.model.Obstacles;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;
import java.util.HashMap;
import java.util.Objects;

/**
 * The class responsible for carrying out the calculations and updating the GUI after
 */
public class RecalculationController {

    private SetRunwayListener setRunwayListener;

    public RecalculationController() {
    }

    private double recalculateTORA(Runway runway, String logicID, Obstacles obstacle,
        double blastProtection, double stripEnd, double resa) {
        // Create hashmap to store all the parameters necessary for the calculations
        var valueMap = new HashMap<String, Double>();

        // Work out the appropriate data values
        var runway1 = runway.getLogicId1();
        var runway2 = runway.getLogicId2();

        var isRunway1Or2 = Objects.equals(logicID, runway1) ? 1 : 2;
        var isObstacleLeftOrRight = obstacle.getPositionL() > obstacle.getPositionR() ? 1 : 2;

        double displacedThreshold;

        var slopeCalc = obstacle.getHeight() * 50;

        Double result;

        // Take off towards
        if (isRunway1Or2 != isObstacleLeftOrRight) {
            displacedThreshold = runway.getDisThresh(
                isObstacleLeftOrRight == 1 ? runway1 : runway2);

            // Add the values to the hashmap
            valueMap.put("ogTORA", runway.getTora(logicID));
            if (blastProtection >= (stripEnd + resa)) {
                valueMap.put("blastProtection", blastProtection);
            } else {
                valueMap.put("stripEnd", stripEnd);
                valueMap.put("resa", resa);
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
            displacedThreshold = runway.getDisThresh(
                isObstacleLeftOrRight == 1 ? runway2 : runway1);

            // Add the values to the hashmap
            valueMap.put("distanceFromThreshold",
                isRunway1Or2 == 1 ? obstacle.getPositionL() : obstacle.getPositionR());
            if (displacedThreshold > 0) {
                valueMap.put("displacedThreshold", -displacedThreshold);
            }
            valueMap.put("slopeCalculation", slopeCalc);
            valueMap.put("stripEnd", stripEnd);

            result = valueMap.get("distanceFromThreshold") * 2;
        }

        for (Double val : valueMap.values()) {
            result -= val;
        }

        valueMap.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });

        return result;
    }


    // todo
    private double recalculateASDA(Runway runway, String logicID, Obstacles obstacle, double tora) {
        // Create hashmap to store all the parameters necessary for the calculations
        var valueMap = new HashMap<String, Double>();

        // Work out the appropriate data values
        var runway1 = runway.getLogicId1();

        var isRunway1Or2 = Objects.equals(logicID, runway1) ? 1 : 2;
        var isObstacleLeftOrRight = obstacle.getPositionL() > obstacle.getPositionR() ? 1 : 2;

        var stopway = 100.0;
        // var stopway = isObstacleLeftOrRight == 1?runway.getStopwayL():runway.getStopwayR();

        // Add the values to the hashmap
        valueMap.put("tora", tora);
        valueMap.put("stopway", stopway);

        var result = valueMap.get("tora") + valueMap.get("stopway");

        valueMap.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });

        // return result;
        return tora;

    }

    // todo
    private double recalculateTODA(Runway runway, String logicID, Obstacles obstacle, double tora) {
        return tora;
    }

    private double recalculateLDA(Runway runway, String logicID, Obstacles obstacle,
        double stripEnd, double resa) {
        // Create hashmap to store all the parameters necessary for the calculations
        var valueMap = new HashMap<String, Double>();

        // Work out the appropriate data values
        var runway1 = runway.getLogicId1();
        var runway2 = runway.getLogicId2();

        var isRunway1Or2 = Objects.equals(logicID, runway1) ? 1 : 2;
        var isObstacleLeftOrRight = obstacle.getPositionL() > obstacle.getPositionR() ? 1 : 2;

        var slopeCalc = obstacle.getHeight() * 50;

        double result;

        // Take off towards
        if (isRunway1Or2 != isObstacleLeftOrRight) {
            // Add the values to the hashmap
            valueMap.put("ogLDA", runway.getLda(logicID));
            valueMap.put("distanceFromThreshold",
                isRunway1Or2 == 1 ? obstacle.getPositionL() : obstacle.getPositionR());
            valueMap.put("slopeCalculation", slopeCalc);
            valueMap.put("stripEnd", stripEnd);

            result = valueMap.get("ogLDA") * 2;

        }
        // Take off away
        else {
            // Add the values to the hashmap
            valueMap.put("distanceFromThreshold",
                isRunway1Or2 == 1 ? obstacle.getPositionL() : obstacle.getPositionR());
            valueMap.put("stripEnd", stripEnd);
            valueMap.put("resa", resa);

            result = valueMap.get("distanceFromThreshold") * 2;
        }

        for (Double val : valueMap.values()) {
            result -= val;
        }

        valueMap.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });

        return result;
    }

    public void setSetRunwayListener(SetRunwayListener setRunwayListener) {
        this.setRunwayListener = setRunwayListener;
    }

    public void recalculateRunway(Runway runway, Obstacles obstacle) {
        var recalculatedRunway = runway.clone();

        var tora1 = recalculateTORA(runway, runway.getLogicId1(), obstacle, 300, 60, 240);
        var toda1 = recalculateTODA(runway, runway.getLogicId1(), obstacle, tora1);
        var asda1 = recalculateASDA(runway, runway.getLogicId1(), obstacle, tora1);
        var lda1 = recalculateLDA(runway, runway.getLogicId1(), obstacle, 60, 240);

        var tora2 = recalculateTORA(runway, runway.getLogicId2(), obstacle, 300, 60, 240);
        var toda2 = recalculateTODA(runway, runway.getLogicId2(), obstacle, tora2);
        var asda2 = recalculateASDA(runway, runway.getLogicId2(), obstacle, tora2);
        var lda2 = recalculateLDA(runway, runway.getLogicId2(), obstacle, 60, 240);

        recalculatedRunway.updateParameters(new double[]{
            runway.getRunwayL(), // runwayL
            runway.getRunwayW(), // runwayW
            runway.getStripL(), // stripL
            runway.getStripW(), // stripW
            runway.getClearwayW(), // clearwayW
            tora1, // tora1
            toda1, // toda1
            asda1, // asda1
            lda1, // lda1
            runway.getDisThresh(runway.getLogicId1()), // disThresh1
            tora2, // tora2
            toda2, // toda2
            asda2, // asda2
            lda2, // lda2
            runway.getDisThresh(runway.getLogicId2()), // disThresh2
            240, // resa
        });

        setRunwayListener.updateRunway(recalculatedRunway);
    }

    public static void main(String[] args) {
        var runway = new PRunway(9, 27, 'L', 'R', new double[]{
            3902,//runwayL
            100,//runwayW
            3902,//stripL
            100,//stripW
            100,//clearwayW
            3902,//tora1
            3902,//toda1
            3902,//asda1
            3595,//lda1
            0,//disThresh1
            3884,//tora2
            3962,//toda2
            3884,//asda2
            3884,//lda2
            306,//disThresh2
            240,}); //resa

        var controller = new RecalculationController();

        // Scenario 1 (09L)
        var obstacle = new Obstacles("Something", 12, -50, 3646);
        var val = controller.recalculateTORA(runway, "09L", obstacle, 300, 60, 240);
        System.out.println("TORA:::::::::::::::   " + val);
        val = controller.recalculateLDA(runway, "09L", obstacle, 60, 240);
        System.out.println("LDA:::::::::::::::   " + val);

        // Scenario 1 (27R)
        // var obstacle = new Obstacles("Something", 12, -50, 3646);
        //  var val = controller.recalculateTORA(runway, "27R", obstacle, 300, 60, 240);
        //  System.out.println("TORA:::::::::::::::   " + val);
        //  val = controller.recalculateLDA(runway, "27R", obstacle, 60, 240);
        //  System.out.println("LDA:::::::::::::::   " + val);

        // Scenario 4 (09L)
        // var obstacle = new Obstacles("Something", 20, 3546, 50);
        // var val = controller.recalculateTORA(runway, "09L", obstacle, 300, 60, 240);
        // System.out.println("TORA:::::::::::::::   " + val);
        // val = controller.recalculateLDA(runway, "09L", obstacle, 60, 240);
        // System.out.println("LDA:::::::::::::::   " + val);

        // Scenario 4 (27R)
        // var obstacle = new Obstacles("Something", 20, 3546, 50);
        // var val = controller.recalculateTORA(runway, "27R", obstacle, 300, 60, 240);
        // System.out.println("TORA:::::::::::::::   " + val);
        // val = controller.recalculateLDA(runway, "27R", obstacle, 60, 240);
        // System.out.println("LDA:::::::::::::::   " + val);

    }
}
