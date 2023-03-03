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
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;


public class RunwayInitialisation {

    public RunwayInitialisation(Window parent, Airport airport) {
        Stage stage = new Stage();

        //Physical runway parameter inputs
        Label phyParameter = new Label("Physical parameters:");
        phyParameter.setFont(Font.font("SansSerif", FontWeight.BOLD, 15));
        Label runwayL = new Label("Runway Length (m)");
        TextField runwayLTf = new TextField();
        Label runwayW = new Label("Runway Width (m)");
        TextField runwayWTf = new TextField();
        Label stripL = new Label("Strip Horizontal Length (m)");
        TextField stripLTf = new TextField();
        Label stripW = new Label("Strip Vertical Width (m)");
        TextField stripWTf = new TextField();
        Label clearW = new Label("Clearway Vertical Width (m)");
        TextField clearWTf = new TextField();

        Label resa = new Label("RESA For Both Logical Runways (m) ");
        TextField resaTf = new TextField();
        resaTf.setPrefSize(60, 20);
        HBox resaBox = new HBox();
        resaBox.getChildren().addAll(resa, resaTf);

        //One logical runway inputs
        Label parameter1 = new Label("Parameters for one logical runway:");
        parameter1.setFont(Font.font("SansSerif", FontWeight.BOLD, 15));
        Label degree1 = new Label("Degree (01-26)");
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

        //parameter inputs
        HBox posBox1 = new HBox();
        posBox1.getChildren().addAll(pos1, posCb1);
        Label tora1 = new Label("TORA (m)");
        TextField toraTf1 = new TextField();
        Label toda1 = new Label("TODA (m)");
        TextField todaTf1 = new TextField();
        Label asda1 = new Label("ASDA (m)");
        TextField asdaTf1 = new TextField();
        Label lda1 = new Label("LDA (m)");
        TextField ldaTf1 = new TextField();
        Label disThresh1 = new Label("Displaced Threshold (m)");
        TextField disThreshTf1 = new TextField();

        //Corresponding logical runway inputs
        Label parameter2 = new Label("Parameters for corresponding logical runway:");
        parameter2.setFont(Font.font("SansSerif", FontWeight.BOLD, 15));
        Label degree2 = new Label("Degree (01-36)");
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

        //parameter inputs
        HBox posBox2 = new HBox();
        posBox2.getChildren().addAll(pos2, posCb2);
        Label tora2 = new Label("TORA (m)");
        TextField toraTf2 = new TextField();
        Label toda2 = new Label("TODA (m)");
        TextField todaTf2 = new TextField();
        Label asda2 = new Label("ASDA (m)");
        TextField asdaTf2 = new TextField();
        Label lda2 = new Label("LDA (m)");
        TextField ldaTf2 = new TextField();
        Label disThresh2 = new Label("Displaced Threshold (m)");
        TextField disThreshTf2 = new TextField();

        //'Add runway' button
        Button addBtn = new Button("Log in");
        addBtn.setFont(new Font(18));
        addBtn.setOnAction(ActionEvent -> {
            List<TextField> textFields = new ArrayList<>(
                    Arrays.asList(runwayLTf, runwayWTf, stripLTf, stripWTf, clearWTf,
                            toraTf1, todaTf1, asdaTf1, ldaTf1, disThreshTf1, toraTf2, todaTf2, asdaTf2, ldaTf2, disThreshTf2, resaTf));

            //Check if all inputs are valid
            if (!validInput(textFields, posCb1.isSelected(), posCb2.isSelected(), posTf1.getText(),
                    posTf2.getText(), degreeTf1.getText(), degreeTf2.getText())) {
                printAlert(false);
            }

            //If inputs are valid then create the runway & add to airport
            else {
                addNewRunway(posCb1.isSelected() && posCb2.isSelected(), degreeTf1.getText(),
                        degreeTf2.getText(), posTf1.getText(), posTf2.getText(), textFields, airport);
                printAlert(true);

                //Window close when runway is added
                stage.close();
            }
        });

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints(60);

        //logical runway 1 input interface Layout
        GridPane runway1 = new GridPane();
        runway1.setHgap(5);
        runway1.getColumnConstraints().addAll(col1, col2, col1, col2);
        runway1.setAlignment(Pos.CENTER_LEFT);
        runway1.add(parameter1,0,0,4,1);
        runway1.addRow(1,degree1, degreeTf1, posBox1, posTf1);
        runway1.addRow(2, tora1, toraTf1, asda1, asdaTf1);
        runway1.addRow(3, toda1, todaTf1, lda1, ldaTf1);
        runway1.addRow(4, disThresh1, disThreshTf1);

        //logical runway 2 input interface Layout
        GridPane runway2 = new GridPane();
        runway2.setHgap(5);
        runway2.getColumnConstraints().addAll(col1, col2, col1, col2);
        runway2.setAlignment(Pos.CENTER_LEFT);
        runway2.add(parameter2,0,0,4,1);
        runway2.addRow(1, degree2, degreeTf2, posBox2, posTf2);
        runway2.addRow(2, tora2, toraTf2, asda2, asdaTf2);
        runway2.addRow(3, toda2, todaTf2, lda2, ldaTf2);
        runway2.addRow(4, disThresh2, disThreshTf2);


        GridPane phyPane = new GridPane();
        phyPane.setHgap(5);
        phyPane.getColumnConstraints().addAll(col1, col2, col1, col2);
        phyPane.add(phyParameter, 0,0,4,1);
        phyPane.addRow(1, runwayL, runwayLTf, stripL, stripLTf);
        phyPane.addRow(2, runwayW, runwayWTf, stripW, stripWTf);
        phyPane.addRow(3, clearW, clearWTf, resa, resaTf);

        //Setup main pane
        GridPane getDataPane = new GridPane();
        getDataPane.setAlignment(Pos.CENTER_LEFT);
        getDataPane.setPadding(new Insets(5, 5, 5, 5));
        getDataPane.setVgap(10);

        //Add all layout & component to main pane
        getDataPane.add(runway1, 0,1);
        getDataPane.add(runway2,0,2);
        getDataPane.add(phyPane, 0, 0, 2,1);
        getDataPane.add(addBtn,1,2);
        GridPane.setHalignment(addBtn, HPos.RIGHT);
        GridPane.setValignment(addBtn, VPos.BOTTOM);

        //Set scene
        Scene scene = new Scene(getDataPane);
        stage.setTitle("Log In New Runway");
        stage.setScene(scene);

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

            if ((p1[0] != 'L' && p1[0] != 'C' && p1[0] != 'R') || (p2[0] != 'L' && p2[0] != 'C' && p2[0] != 'R')) {
                return false;
            }

            else if (!((p1[0] == 'C' && p2[0] == 'C') || (p1[0] == 'L' && p2[0] == 'R') || (p1[0] == 'R' || p2[0] == 'L'))) {
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

    private double[] convertTextToDouble(List<TextField> textFields) {
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

        double[] data = convertTextToDouble(textFields);

        if (isParallel) {
            char p1 = pos1.toCharArray()[0];
            char p2 = pos2.toCharArray()[0];

            Runway newRunway = new PRunway(d1, d2, p1, p2, data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7],
                    data[8], data[9], data[10], data[11], data[12], data[13], data[14],data[15]);
            airport.addRunway(newRunway);
        }

        else {
            Runway newRunway = new SRunway(d1, d2, data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7],
                    data[8], data[9], data[10], data[11], data[12], data[13], data[14],data[15]);
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
