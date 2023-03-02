package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.SRunway;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;


public class RunwayInitialisation {

    public RunwayInitialisation(Window parent, Airport airport) {
        Stage stage = new Stage();

        //Physical runway parameter inputs
        Label phyParameter = new Label("Physical parameters:");
        phyParameter.setFont(new Font(15));
        Label runwayL = new Label("Runway Length");
        TextField runwayLTf = new TextField();
        Label runwayW = new Label("Runway Width");
        TextField runwayWTf = new TextField();
        Label stripL = new Label("Strip Length");
        TextField stripLTf = new TextField();
        Label stripW = new Label("Strip Width");
        TextField stripWTf = new TextField();
        Label stopL = new Label("Stopway Length");
        TextField stopLTf = new TextField();
        Label stopW = new Label("Stopway Width");
        TextField stopWTf = new TextField();
        Label clearL = new Label("Clearway Length");
        TextField clearLTf = new TextField();
        Label clearW = new Label("Clearway Width");
        TextField clearWTf = new TextField();

        Label resa = new Label("RESA (for both logical runways): ");
        resa.setFont(new Font(15));
        TextField resaTf = new TextField();

        //One logical runway parameter inputs
        Label parameter1 = new Label("Parameters for one logical runway:");
        parameter1.setFont(new Font(15));
        Label degree1 = new Label("Degree");
        TextField degreeTf1 = new TextField();
        //Position character
        Label pos1 = new Label("Position (L/C/R)");
        CheckBox posCb1 = new CheckBox();
        TextField posTf1 = new TextField();
        //Display the TextField only when checkbox is selected
        posTf1.visibleProperty().bind(posCb1.selectedProperty());
        //Clear the Text in the TextField when unselecting the checkbox
        posCb1.setOnAction(ActionEvent -> {
            if (!posCb1.isSelected()) {
                posTf1.clear();
            }
        });
        HBox posBox1 = new HBox();
        posBox1.getChildren().addAll(pos1, posCb1);
        Label tora1 = new Label("TORA");
        TextField toraTf1 = new TextField();
        Label toda1 = new Label("TODA");
        TextField todaTf1 = new TextField();
        Label asda1 = new Label("ASDA");
        TextField asdaTf1 = new TextField();
        Label lda1 = new Label("LDA");
        TextField ldaTf1 = new TextField();
        Label disThresh1 = new Label("Displaced Threshold");
        TextField disThreshTf1 = new TextField();

        //Corresponding logical runway parameter inputs
        Label parameter2 = new Label("Parameters for corresponding logical runway:");
        parameter2.setFont(new Font(15));
        Label degree2 = new Label("Degree");
        TextField degreeTf2 = new TextField();
        //Position character
        Label pos2 = new Label("Position (L/C/R)");
        CheckBox posCb2 = new CheckBox();
        TextField posTf2 = new TextField();
        //Display the TextField only when checkbox is selected
        posTf2.visibleProperty().bind(posCb2.selectedProperty());
        //Clear the Text in the TextField when unselecting the checkbox
        posCb2.setOnAction(ActionEvent -> {
            if (!posCb2.isSelected()) {
                posTf2.clear();
            }
        });
        HBox posBox2 = new HBox();
        posBox2.getChildren().addAll(pos2, posCb2);
        Label tora2 = new Label("TORA");
        TextField toraTf2 = new TextField();
        Label toda2 = new Label("TODA");
        TextField todaTf2 = new TextField();
        Label asda2 = new Label("ASDA");
        TextField asdaTf2 = new TextField();
        Label lda2 = new Label("LDA");
        TextField ldaTf2 = new TextField();
        Label disThresh2 = new Label("Displaced Threshold");
        TextField disThreshTf2 = new TextField();

        Button addBtn = new Button("Log in");

        GridPane getDataPage = new GridPane();
        getDataPage.setAlignment(Pos.CENTER_LEFT);
        getDataPage.setPadding(new Insets(10, 10, 10, 10));
        getDataPage.setVgap(7);
        getDataPage.setHgap(5);

        ColumnConstraints col1 = new ColumnConstraints();
        getDataPage.getColumnConstraints().addAll(col1, new ColumnConstraints(60),
                col1, new ColumnConstraints(60), col1, new ColumnConstraints(60),col1, new ColumnConstraints(60));

        getDataPage.add(parameter1, 0,0,4,1);
        getDataPage.addRow(1,degree1, degreeTf1, posBox1, posTf1);
        getDataPage.addRow(2, tora1, toraTf1, asda1, asdaTf1);
        getDataPage.addRow(3, toda1, todaTf1, lda1, ldaTf1);
        getDataPage.addRow(4, disThresh1, disThreshTf1);

        getDataPage.add(parameter2, 0, 5, 4, 1);
        getDataPage.addRow(6, degree2, degreeTf2, posBox2, posTf2);
        getDataPage.addRow(7, tora2, toraTf2, asda2, asdaTf2);
        getDataPage.addRow(8, toda2, todaTf2, lda2, ldaTf2);
        getDataPage.addRow(9,  disThresh2, disThreshTf2);

        getDataPage.add(phyParameter, 4,0,4,1);
        getDataPage.addColumn(4, runwayL,runwayW, stopL, stopW);
        getDataPage.addColumn(5,runwayLTf,runwayWTf,stopLTf,stopWTf);
        getDataPage.addColumn(6, stripL, stripW, clearL,clearW);
        getDataPage.addColumn(7, stripLTf,stripWTf,clearLTf,clearWTf);

        getDataPage.add(resa, 4,6,3,1);
        getDataPage.add(resaTf, 7,6);

        getDataPage.add(addBtn,7,9);
        GridPane.setHalignment(addBtn, HPos.RIGHT);

        addBtn.setOnAction(ActionEvent -> {
            List<TextField> textFields = new ArrayList<>(
                Arrays.asList(runwayLTf, runwayWTf, stripLTf, stripWTf, stopLTf, stopWTf, clearLTf, clearWTf,
                        toraTf1, todaTf1, asdaTf1, ldaTf1, disThreshTf1, toraTf2, todaTf2, asdaTf2, ldaTf2, disThreshTf2, resaTf));

            if (!validInput(textFields, posCb1.isSelected(), posCb2.isSelected(), posTf1.getText(),
                    posTf2.getText(), degreeTf1.getText(), degreeTf2.getText())) {
                printAlert(false);
            }

            else {
                addNewRunway(posCb1.isSelected() && posCb2.isSelected(), degreeTf1.getText(),
                        degreeTf2.getText(), posTf1.getText(), posTf2.getText(), textFields, airport);
                printAlert(true);
                stage.close();
            }
        });

        Scene scene = new Scene(getDataPage);
        stage.setTitle("Log in new runway");
        stage.setScene(scene);

        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        stage.show();
    }

    private Boolean validInput(List<TextField> textFields, Boolean pos1Selected, Boolean pos2Selected, String pos1, String pos2, String degree1, String degree2) {
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

            if ((p1[0] != 'L' && p1[0] != 'C' && p1[0] != 'R') || (p2[0]!= 'L' && p2[0] != 'C' && p2[0] != 'R')) {
                return false;
            }
        }

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

    private double[] convertTextTodouble(List<TextField> textFields) {
        double[] data = new double[textFields.size()];
        int p = 0;
        for (TextField textField : textFields) {
            data[p] = Double.parseDouble(textField.getText());
            p++;
        }

        return data;
    }

    private void addNewRunway(Boolean isParallel, String degree1, String degree2, String pos1, String pos2, List<TextField> textFields, Airport airport) {
        int d1 = Integer.parseInt(degree1);
        int d2 = Integer.parseInt(degree2);

        double[] data = convertTextTodouble(textFields);

        if (isParallel) {
            char p1 = pos1.toCharArray()[0];
            char p2 = pos2.toCharArray()[0];

            Runway newRunway = new PRunway(d1, d2, p1, p2, data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7],
                    data[8], data[9], data[10], data[11], data[12], data[13], data[14],data[15], data[16], data[17], data[18]);
            airport.addRunway(newRunway);
        }

        else {
            Runway newRunway = new SRunway(d1, d2, data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7],
                    data[8], data[9], data[10], data[11], data[12], data[13], data[14],data[15], data[16], data[17], data[18]);
            airport.addRunway(newRunway);
        }
    }

    private void printAlert(Boolean success) {
        Alert a;

        if (success) {
            a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Runway has been logged");
        } else {
            a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Invalid Input");
        }

        a.show();
    }
}
