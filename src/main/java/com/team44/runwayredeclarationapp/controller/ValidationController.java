package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Runway;
import java.util.ArrayList;
import java.util.List;

/**
 * The class that is responsible for updating the initial parameters of runways
 */
public abstract class ValidationController {

    /**
     * Check whether the inputs are valid when a user add a new runway to the system
     *
     * @param pos1       position character input by the user
     * @param pos2       the other position character input by the user
     * @param degree1    degree of one logical runway
     * @param degree2    degree of the other logical runway // * @param textFields   contains all
     *                   numerical inputs
     * @param parameters the runway parameters
     * @param airport    the airport that the runway is in
     * @return the list of errors
     */
    public static List<String> validateRunwayData(
        Character pos1, Character pos2,
        int degree1, int degree2, double[] parameters, Airport airport) {

        // todo:: validate phyID etc

        // Create list of errors
        List<String> errors = new ArrayList<>();

        //return false if degree1 or degree2 are <01, >36 or their difference is not equal to 18
        if (degree1 < 1 || degree1 > 36 || degree2 < 1 || degree2 > 36
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
            //return false if the pair of position chars is not L&R or C&C
            else if (!((pos1 == 'C' && pos2 == 'C') || (pos1 == 'L' && pos2 == 'R') || (
                pos1 == 'R' || pos2 == 'L'))) {
                errors.add("Positions must either be L&R, C&C or R&L.");
            }

            //insert position chars into the id if needed
            phyId = phyId.substring(0, 2) + pos1 + phyId.substring(2) + pos2;
        }

        //loop through current runways logged in the airport, return false if the runway already exist in the system
        for (Runway runway : airport.getRunways()) {
            if (runway.getPhyId().equals(phyId)) {
                errors.add("This runway already exists.");
            }
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

        //return false if any of the physical parameters is 0
        for (int i = 0; i < 6; i++) {
            if (parametersList.get(i) < 1) {
                errors.add("Physical parameters cannot be 0.");
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
}
