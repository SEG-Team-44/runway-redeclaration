package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.SRunway;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public abstract class ParameterController {

    protected boolean validInitInput(boolean pos1Selected, boolean pos2Selected, String pos1, String pos2, String degree1, String degree2, TextField[] textFields) {
        try {
            //Check if degree1 & degree2 are int value
            int d1 = Integer.parseInt(degree1);
            int d2 = Integer.parseInt(degree2);

            //Check if degree1 & degree2 are with in the right range
            if (d1 < 1 || d1 > 36 || d2 < 1 || d2 > 36 ||  Math.abs(d1 - d2) != 18) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        //Check if parallel runway selection is valid
        if ((!pos1Selected && pos2Selected) || (pos1Selected && !pos2Selected)) {
            return false;
        }

        else if (pos1Selected && pos2Selected) {
            char[] p1 = pos1.toCharArray();
            char[] p2 = pos2.toCharArray();

            //Check if position chars are valid
            if (p1.length != 1 || p2.length != 1) {
                return false;
            }

            if ((p1[0] != 'L' && p1[0] != 'C' && p1[0] != 'R') || (p2[0] != 'L' && p2[0] != 'C' && p2[0] != 'R')) {
                return false;
            }

            else if (!((p1[0] == 'C' && p2[0] == 'C') || (p1[0] == 'L' && p2[0] == 'R') || (p1[0] == 'R' || p2[0] == 'L'))) {
                return false;
            }
        }

        return  validNumericalInput(textFields);
    }


    protected boolean validNumericalInput(TextField[] textFields) {

        //check if all numerical inputs can be converted into double values
        for (TextField textField : textFields) {
            if (textField.getText().isEmpty()) {
                return false;
            } else {
                try {
                    Double.parseDouble(textField.getText());
                } catch (Exception e) {
                    return false;
                }
            }
        }

        return true;
    }

    protected boolean addNewRunway(boolean posCb1, boolean posCb2, String pos1, String pos2, String degree1, String degree2, TextField[] textFields, Airport airport) {

        if (validInitInput(posCb1, posCb2, pos1, pos2, degree1, degree2, textFields)) {
            int d1 = Integer.parseInt(degree1);
            int d2 = Integer.parseInt(degree2);

            double[] parameters = convertTextToDouble(textFields);
            boolean isParallel = posCb1 && posCb2;

            if (isParallel) {
                char p1 = pos1.toCharArray()[0];
                char p2 = pos2.toCharArray()[0];

                Runway newRunway = new PRunway(d1, d2, p1, p2, parameters);
                airport.addRunway(newRunway);
            }

            else {
                Runway newRunway = new SRunway(d1, d2, parameters);
                airport.addRunway(newRunway);
            }

            return true;
        }

        else return false;
    }

    protected double[] convertTextToDouble(TextField[] textFields) {
        double[] parameters = new double[textFields.length];
        int p = 0;
        for (TextField textField : textFields) {
            parameters[p] = Double.parseDouble(textField.getText());
            p++;
        }

        return parameters;
    }

    protected void printAlert(boolean success) {}
}