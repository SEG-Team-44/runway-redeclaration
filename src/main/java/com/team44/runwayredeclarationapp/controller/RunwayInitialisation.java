package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.SRunway;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
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

        //Components for physical inputs
        Label phyParameter = new Label("Physical parameters:");
        phyParameter.setFont(new Font(17));
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

        //Layout the physical input components
        GridPane phyPane = new GridPane();
        phyPane.setHgap(5);
        phyPane.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints(60), new ColumnConstraints(), new ColumnConstraints(60));
        phyPane.add(phyParameter, 0,0,4,1);
        phyPane.addRow(1, runwayL, runwayLTf, stripL, stripLTf);
        phyPane.addRow(2, runwayW, runwayWTf, stripW, stripWTf);
        phyPane.addRow(3, clearW, clearWTf, resa, resaTf);

        //Components for one logical runway inputs
        String lbl1 = "Parameters For One Logical Runway:";
        TextField degreeTf1 = new TextField();
        CheckBox posCb1 = new CheckBox();
        TextField posTf1 = new TextField();

        //Display the TextField only when checkbox is selected
        posTf1.visibleProperty().bind(posCb1.selectedProperty());

        //Clear the Text in the TextField when deselecting the checkbox
        posCb1.setOnAction(ActionEvent -> {
            if (!posCb1.isSelected()) {
                posTf1.clear();
            }
        });

        TextField toraTf1 = new TextField();
        TextField todaTf1 = new TextField();
        TextField asdaTf1 = new TextField();
        TextField ldaTf1 = new TextField();
        TextField disThreshTf1 = new TextField();

        //Layout of components for logical runway input 1
        GridPane runway1 = getLayout(lbl1, degreeTf1, posCb1, posTf1, toraTf1, todaTf1, asdaTf1, ldaTf1, disThreshTf1);

        //Components for the other logical runway inputs
        String lbl2 = "Parameters for corresponding logical runway:";
        TextField degreeTf2 = new TextField();
        CheckBox posCb2 = new CheckBox();
        TextField posTf2 = new TextField();

        //Display the TextField only when checkbox is selected
        posTf2.visibleProperty().bind(posCb2.selectedProperty());

        //Clear the Text in the TextField when deselecting the checkbox
        posCb2.setOnAction(ActionEvent -> {
            if (!posCb2.isSelected()) {
                posTf2.clear();
            }
        });

        TextField toraTf2 = new TextField();
        TextField todaTf2 = new TextField();
        TextField asdaTf2 = new TextField();
        TextField ldaTf2 = new TextField();
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

        //Layout of components for logical runway input 2
        GridPane runway2 = getLayout(lbl2, degreeTf2, posCb2, posTf2, toraTf2, todaTf2, asdaTf2, ldaTf2, disThreshTf2);

        //Setup main pane
        GridPane mainPane = new GridPane();
        mainPane.setAlignment(Pos.CENTER_LEFT);
        mainPane.setPadding(new Insets(5, 5, 5, 5));
        mainPane.setVgap(10);

        //Add all layouts & components to main pane
        mainPane.add(phyPane, 0, 0, 2,1);
        mainPane.add(runway1, 0,1);
        mainPane.add(runway2,0,2);
        mainPane.add(addBtn,1,2);
        GridPane.setHalignment(addBtn, HPos.RIGHT);
        GridPane.setValignment(addBtn, VPos.BOTTOM);

        //Set scene
        Scene scene = new Scene(mainPane);
        mainPane.requestFocus();
        scene.setOnMouseClicked(mouseEvent -> {
            mainPane.requestFocus();
        });

        stage.setTitle("Log In New Runway");
        stage.setScene(scene);
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        stage.show();
    }

    private GridPane getLayout(String lblContent, TextField degree, CheckBox posCb, TextField pos, TextField tora, TextField toda, TextField asda, TextField lda, TextField disThresh) {
        Label lbl = new Label(lblContent);
        lbl.setFont(new Font(17));
        Label degreeLbl = new Label("Degree (01-26)");
        Label posLbl = new Label("Position (L/C/R)");
        Label toraLbl = new Label("TORA (m)");
        Label todaLbl = new Label("TODA (m)");
        Label asdaLbl = new Label("ASDA (m)");
        Label ldaLbl = new Label("LDA (m)");
        Label disThreshLbl = new Label("Displaced Threshold (m)");

        HBox posBox = new HBox();
        posBox.getChildren().addAll(posLbl, posCb);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints(60);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.getColumnConstraints().addAll(col1, col2, col1, col2);
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.add(lbl,0,0,4,1);
        gridPane.addRow(1,degreeLbl, degree, posBox, pos);
        gridPane.addRow(2, toraLbl, tora, asdaLbl, asda);
        gridPane.addRow(3, todaLbl, toda, ldaLbl, lda);
        gridPane.addRow(4, disThreshLbl, disThresh);

        return gridPane;
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
