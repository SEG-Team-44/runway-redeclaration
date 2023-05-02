package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;
import java.util.ArrayList;
import java.util.List;

/**
 * The class that is responsible for updating the initial parameters of runways
 */
public class ValidationController {

    /**
     * Regex for a maximum for 60 characters
     */
    private static final String sixtyCharsRegex = "^.{1,60}$";
    /**
     * Regex for a maximum of 2 decimal places
     */
    private static final String twoDpRegex = "^-?\\d+(\\.\\d{1,2})?$";

    /**
     * Lower and upper bounds for obstacle numerical inputs
     */
    private static final int obstacleHeightLowerBound = 0;
    private static final int obstacleHeightUpperBound = 500;

    /**
     * Lower and upper bounds for runway numerical inputs
     */
    private static final int runwayParametersUpperBound = 20000;

    /**
     * Lower and upper bounds for obstacle information numerical inputs
     */
    private static final int obstacleFromCentrelineLowerBound = -500;
    private static final int obstacleFromCentrelineUpperBound = 500;
    private static final int blastProtectionLowerBound = 0;
    private static final int blastProtectionUpperBound = 5000;


    /**
     * Validate the inputs of an airport
     *
     * @param airportName the airport name
     * @return the list of errors
     */
    public static List<String> validateAirport(String airportName) {
        // Create list of errors
        List<String> errors = new ArrayList<>();

        if (airportName == null || !airportName.matches(sixtyCharsRegex)) {
            errors.add("Airport name provided (\"" + airportName
                + "\") must not be empty and under 60 characters.");
        }

        return errors;
    }

    /**
     * Validate the inputs of an obstacle
     *
     * @param obstacleName   the obstacle name
     * @param obstacleHeight the obstacle height
     * @return the list of errors
     */
    public static List<String> validateObstacle(String obstacleName, double obstacleHeight) {
        // Create list of errors
        List<String> errors = new ArrayList<>();

        if (obstacleName == null || !obstacleName.matches(sixtyCharsRegex)) {
            errors.add("Obstacle name provided (\"" + obstacleName
                + "\") must not be empty and under 60 characters.");
        }

        // Validate obstacle height to 2 dp
        if (!String.valueOf(obstacleHeight).matches(twoDpRegex)) {
            errors.add("Obstacle (" + obstacleName + ": " + obstacleHeight
                + ") height must have a maximum of 2 decimal places.");
        }

        if (obstacleHeight >= obstacleHeightUpperBound) {
            errors.add("Obstacle (" + obstacleName + ") height must be under 500m.");
        } else if (obstacleHeight <= obstacleHeightLowerBound) {
            errors.add("Obstacle (" + obstacleName + ") height must be greater than 0m.");
        }

        return errors;
    }

    /**
     * Validate the inputs when a user add a new runway to the system
     *
     * @param pos1       position character input by the user
     * @param pos2       the other position character input by the user
     * @param degree1    degree of one logical runway
     * @param degree2    degree of the other logical runway
     * @param parameters the runway parameters
     * @param airport    the airport that the runway is in
     * @return the list of errors
     */
    public static List<String> validateRunway(Character pos1, Character pos2,
        int degree1, int degree2, double[] parameters, Airport airport) {
        return validateRunway(pos1, pos2, degree1, degree2, parameters, airport, false);
    }

    /**
     * Validate the inputs when a user add a new runway to the system
     *
     * @param pos1                   position character input by the user
     * @param pos2                   the other position character input by the user
     * @param degree1                degree of one logical runway
     * @param degree2                degree of the other logical runway
     * @param parameters             the runway parameters
     * @param airport                the airport that the runway is in
     * @param runwayAlreadyInAirport whether the runway has been already added to the airport
     * @return the list of errors
     */
    public static List<String> validateRunway(
        Character pos1, Character pos2,
        int degree1, int degree2, double[] parameters, Airport airport,
        boolean runwayAlreadyInAirport) {

        // Create list of errors
        List<String> errors = new ArrayList<>();

        //return false if degree1 or degree2 are <0, >36 or their difference is not equal to 18
        if (degree1 < 0 || degree1 > 36 || degree2 < 0 || degree2 > 36
            || Math.abs(degree1 - degree2) != 18) {
            errors.add("Degrees 1 and 2 must be in the range 0-36, "
                + "and have a difference of 18 between them.");
        }

        //create a physical id with the degrees
        String phyId = Runway.createPhyId(degree1, degree2);

        //return false if position checkboxes are not selected/deselected the same time
        if ((pos1 == null && pos2 != null) || (pos1 != null && pos2 == null)) {
            errors.add("You cannot set a position to just 1 logical runway.");
        }
        //when user selected to input position characters
        else if (pos1 != null) {
            //return false if position chars input are not either L/C/R
            if ((pos1 != 'L' && pos1 != 'C' && pos1 != 'R') || (pos2 != 'L' && pos2 != 'C'
                && pos2 != 'R')) {
                errors.add("Positions can only be either 'L','C' or 'R'.");
            }
            //return false if the pair of position chars is not L&R, R&L or C&C
            else if (!((pos1 == 'C' && pos2 == 'C') || (pos1 == 'L' && pos2 == 'R') || (
                pos1 == 'R' && pos2 == 'L'))) {
                errors.add("Positions must either be L&R, C&C or R&L.");
            }

            //insert position chars into the id if needed
            phyId = PRunway.createPhyId(degree1, degree2, pos1, pos2);
        }

        //loop through current runways logged in the airport, return false if the runway already exist in the system
        var count = 0;
        for (Runway runway : airport.getRunways()) {
            if (runway.getPhyId().equals(phyId)) {
                count += 1;
            }
        }
        if (count > (runwayAlreadyInAirport ? 1 : 0)) {
            errors.add(
                "Duplicate runway detected: " + phyId + " (Airport: " + airport.getName() + ")");
        }

        //check if all numerical parameter inputs are valid
        errors.addAll(validateRunwayParameters(parameters));

        // Return the list of errors
        return errors;
    }


    /**
     * Validate the runway parameters
     *
     * @param parameters the runway paramaters
     * @return the list of errors
     */
    private static List<String> validateRunwayParameters(double[] parameters) {
        // Create list of errors
        List<String> errors = new ArrayList<>();

        // Convert primitive double list into Double list
        List<Double> parametersList = new ArrayList<>();
        for (double par : parameters) {
            parametersList.add(par);
        }

        var parameterNames = new String[]{
            "Runway Length - runwayL", "Runway Width - runwayW",
            "Strip Length - stripL", "Strip Width - stripW",
            "Clearway Width - clearwayW", "RESA - resaL",
            "TORA - tora1", "TODA - toda1", "ASDA - asda1", "LDA - lda1",
            "TORA - tora2", "TODA - toda2", "ASDA - asda2", "LDA - lda2",
            "Displaced Threshold - disThresh1", "Displaced Threshold - disThresh2"
        };

        // Validate parameter lengths to 2 dp and upper bound
        for (double parameter : parameters) {
            if (!String.valueOf(parameter).matches(twoDpRegex)) {
                errors.add("Runway parameter length (" + parameter
                    + ") must have a maximum of 2 decimal places.");
            }

            if (parameter > runwayParametersUpperBound) {
                errors.add("Runway parameters must be <= 20,000.");
            }
        }

        //return false if any of the physical parameters is 0
        for (int i = 0; i < 14; i++) {
            if (parametersList.get(i) < 1) {
                errors.add("Runway parameter (" + parameterNames[i]
                    + ") cannot be less than or equal to 0.");
            }
        }

        //return false if any of the 2 TORA > runway length
        if (parametersList.get(6) > parametersList.get(0)
            || parametersList.get(10) > parametersList.get(0)) {
            errors.add("TORA values must be less than the runway length.");
        }

        //return false if any of the TODAs/ASDAs smaller than their corresponding TORAs
        if (parametersList.get(7) < parametersList.get(6)
            || parametersList.get(8) < parametersList.get(6) ||
            parametersList.get(11) < parametersList.get(10)
            || parametersList.get(12) < parametersList.get(10)) {
            errors.add(
                "TODA and ASDA values must be greater than or equal to the corresponding TORA values.");
        }

        //return false if any of the LDAs > TORAs
        if (parametersList.get(9) > parametersList.get(6)
            || parametersList.get(13) > parametersList.get(10)) {
            errors.add("LDA cannot be greater than the corresponding TORA value.");
        }

        //return false if any of the displaced threshold either smaller than 0 or larger than their corresponding TORA
        if (parametersList.get(14) < 0 || parametersList.get(14) > parametersList.get(6) ||
            parametersList.get(15) < 0 || parametersList.get(15) > parametersList.get(10)) {
            errors.add(
                "Displaced threshold must be in the range 0 to its corresponding TORA value.");
        }

        return errors;
    }

    /**
     * Validate the obstacle information inputs such as the distance from the left, right,
     * centre-line and blast protection
     *
     * @param selectedRunway         the selected runway
     * @param distanceFromLeft       the distance of the obstacle from the left threshold
     * @param distanceFromRight      the distance of the obstacle from the right threshold
     * @param distanceFromCentreline the distance of the obstacle from the centreline
     * @param blastProtection        the blast protection of the airplane
     * @return the list of errors
     */
    public static List<String> validateObstacleInformationInputs(Runway selectedRunway,
        double distanceFromLeft, double distanceFromRight, double distanceFromCentreline,
        double blastProtection) {
        // Create list of errors
        List<String> errors = new ArrayList<>();

        // Get logic ids
        var runway1 = selectedRunway.getLogicId1();
        var runway2 = selectedRunway.getLogicId2();

        // Distance bounds
        if (distanceFromCentreline >= obstacleFromCentrelineUpperBound) {
            errors.add("Obstacle distance from centreline (" + distanceFromCentreline
                + ") must be less than 500m.");
        } else if (distanceFromCentreline <= obstacleFromCentrelineLowerBound) {
            errors.add("Obstacle distance from centreline (" + distanceFromCentreline
                + ") must be greater than -500m.");
        }

        // Blast protection bounds
        if (blastProtection >= blastProtectionUpperBound) {
            errors.add("Blast protection (" + blastProtection + ") must be under 5000m.");
        } else if (blastProtection < blastProtectionLowerBound) {
            errors.add(
                "Blast protection (" + blastProtection + ") must be greater than or equal to 0m.");
        }

        // Distances add up to total length
        if ((distanceFromLeft + distanceFromRight + selectedRunway.getDisThresh(runway1)
            + selectedRunway.getDisThresh(runway2) != selectedRunway.getRunwayL())) {
            errors.add("Obstacle distance from left (" + distanceFromLeft + ") and right ("
                + distanceFromRight + ") of thresholds do not match the total runway length ("
                + selectedRunway.getRunwayL() + ")");
        }

        return errors;
    }
}
