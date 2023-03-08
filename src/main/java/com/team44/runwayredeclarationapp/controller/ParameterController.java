package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.event.SetRunwayListener;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.SRunway;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TextField;

/**
 * The class that is responsible for updating the initial parameters of runways
 */
public abstract class ParameterController {

    protected SetRunwayListener setRunwayListener;

    /**
     * Check whether the inputs are valid when a user add a new runway to the system
     * @param pos1Selected boolean indicates whether the user has selected to input a position char for a logical runway
     * @param pos2Selected boolean indicates whether the user has selected to input a position char for the other logical runway
     * @param pos1 position character input by the user
     * @param pos2 the other position character input by the user
     * @param degree1 degree of one logical runway
     * @param degree2 degree of the other logical runway
     * @param textFields contains all numerical inputs
     * @return boolean indicating whether the input values are all valid
     */
    protected boolean validInitInput(boolean pos1Selected, boolean pos2Selected, String pos1,
        String pos2, String degree1, String degree2, TextField[] textFields, Airport airport) {
        try {
            //Check if degree1 & degree2 are int value
            int d1 = Integer.parseInt(degree1);
            int d2 = Integer.parseInt(degree2);

            //return false if degree1 or degree2 are <01, >36 or their difference is not equal to 18
            if (d1 < 1 || d1 > 36 || d2 < 1 || d2 > 36 || Math.abs(d1 - d2) != 18) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        //create a physical id with the degrees
        String phyId = degree1 + degree2;

        //return false if position checkboxes are not selected/deselected the same time
        if ((!pos1Selected && pos2Selected) || (pos1Selected && !pos2Selected)) {
            return false;
        }
        //when user selected to input position characters
        else if (pos1Selected && pos2Selected) {
            char[] p1 = pos1.toCharArray();
            char[] p2 = pos2.toCharArray();

            //return false if both position TextFields contains more than 1 character
            if (p1.length != 1 || p2.length != 1) {
                return false;
            }

            //return false if position chars input are not either L/C/R
            if ((p1[0] != 'L' && p1[0] != 'C' && p1[0] != 'R') || (p2[0] != 'L' && p2[0] != 'C'
                && p2[0] != 'R')) {
                return false;
            }
            //return false if the pair of position chars is not L&R or C&C
            else if (!((p1[0] == 'C' && p2[0] == 'C') || (p1[0] == 'L' && p2[0] == 'R') || (
                p1[0] == 'R' || p2[0] == 'L'))) {
                return false;
            }

            //insert position chars into the id if needed
            phyId = phyId.substring(0, 2) + pos1 + phyId.substring(2) + pos2;
        }

        //loop through current runways logged in the airport, return false if the runway already exist in the system
        for (Runway runway : airport.getRunways()) {
            if (runway.getPhyId().equals(phyId)) {
                return false;
            }
        }
        //check if all numerical inputs are valid
        return validNumericalInput(textFields);
    }

    /**
     * Check if all numerical inputs are within the correct boundaries
     * @param textFields containing the numerical parameters
     * @return boolean idicates whether inputs are valid
     */
    protected boolean validNumericalInput(TextField[] textFields) {
        List<Double> parameters = new ArrayList<>();

        //return false if any of the inputs cannot be converted into double value
        try {
            for (TextField textField : textFields) {
                parameters.add(Double.parseDouble(textField.getText()));
            }

        } catch (Exception e) {
            return false;
        }

        //return false if any of the physical parameters is 0
        for (int i = 0; i < 6; i++) {
            if (parameters.get(i) < 1) {
                return false;
            }
        }

        //return false if any of the 2 TORA > runway length
        if (parameters.get(6) > parameters.get(0) || parameters.get(10) > parameters.get(0)) {
            return false;
        }

        //return false if any of the TODAs/ASDAs smaller than their corresponding TORAs
        if (parameters.get(7) < parameters.get(6) || parameters.get(8) < parameters.get(6) ||
            parameters.get(11) < parameters.get(10) || parameters.get(12) < parameters.get(10)) {
            return false;
        }

        //return false if any of the LDAs > TORAs
        if (parameters.get(9) > parameters.get(6) || parameters.get(13) > parameters.get(10)) {
            return false;
        }

        //return false if any of the displaced threshold either smaller than 0 or larger than their corresponding TORA
        if (parameters.get(14) < 0 || parameters.get(14) > parameters.get(6) ||
            parameters.get(15) < 0 || parameters.get(15) > parameters.get(10)) {
            return false;
        }

        return true;
    }

    /**
     * Call validInitInput function, if all inputs are valid, create a new runway and add it to the Airport
     * @param posCb1 boolean indicates whether the user has selected to input a position char for a logical runway
     * @param posCb2 boolean indicates whether the user has selected to input a position char for the other logical runway
     * @param pos1 position characters
     * @param pos2 the other position characters
     * @param degree1 degree of one logical runway
     * @param degree2 degree of the other logical runway
     * @param textFields containing all parameter inputs
     * @param airport the Airport where the user is adding runway to
     * @return true if a runway has been successfully added, otherwise return false
     */
    protected boolean addNewRunway(boolean posCb1, boolean posCb2, String pos1, String pos2,
        String degree1, String degree2, TextField[] textFields, Airport airport) {

        //create a new runway and add it to the airport if inputs are valid
        if (validInitInput(posCb1, posCb2, pos1, pos2, degree1, degree2, textFields, airport)) {
            int d1 = Integer.parseInt(degree1);
            int d2 = Integer.parseInt(degree2);

            double[] parameters = convertTextToDouble(textFields);
            boolean isParallel = posCb1 && posCb2;

            Runway newRunway;
            if (isParallel) {
                char p1 = pos1.toCharArray()[0];
                char p2 = pos2.toCharArray()[0];

                newRunway = new PRunway(d1, d2, p1, p2, parameters);
                airport.addRunway(newRunway);
            } else {
                newRunway = new SRunway(d1, d2, parameters);
                airport.addRunway(newRunway);
            }

            // Call the listener to set the new runway to the UI
            setRunwayListener.updateRunway(newRunway);

            return true;
        } else {
            return false;
        }
    }

    /**
     * Given a list of TextFields, convert the contents into list of double values
     * @param textFields containing inputs that required to be double values
     * @return list of doubles
     */
    protected double[] convertTextToDouble(TextField[] textFields) {
        double[] parameters = new double[textFields.length];
        int p = 0;
        for (TextField textField : textFields) {
            parameters[p] = Double.parseDouble(textField.getText());
            p++;
        }

        return parameters;
    }

    /**
     * Print an alert according to the boolean
     * @param success if tasks have been successfully performed
     */
    protected void printAlert(boolean success) {}

    /**
     * Set the listener to be called when a runway has been selected or updated
     *
     * @param setRunwayListener the listener
     */
    public void setNewRunwayListener(SetRunwayListener setRunwayListener) {
        this.setRunwayListener = setRunwayListener;
    }
}
