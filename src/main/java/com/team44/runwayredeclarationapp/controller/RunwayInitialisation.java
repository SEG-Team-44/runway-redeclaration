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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;


public class RunwayInitialisation {

    public RunwayInitialisation(Window parent, Airport airport) {
        Stage stage = new Stage();

        Label degree = new Label("Degree");
        TextField degreeTf = new TextField();

        Label pos = new Label("Position (L/C/R)");
        CheckBox posCb = new CheckBox();

        TextField posTf = new TextField();
        //Display the TextField only when checkbox is selected
        posTf.visibleProperty().bind(posCb.selectedProperty());

        //Clear the Text in the TextField when unselecting the checkbox
        posCb.setOnAction(ActionEvent -> {
            if (!posCb.isSelected()) {
                posTf.clear();
            }
        });

        HBox posBox = new HBox();
        posBox.setSpacing(5);
        posBox.getChildren().addAll(pos, posCb);

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

        Label tora = new Label("TORA");
        TextField toraTf = new TextField();

        Label toda = new Label("TODA");
        TextField todaTf = new TextField();

        Label asda = new Label("ASDA");
        TextField asdaTf = new TextField();

        Label lda = new Label("LDA");
        TextField ldaTf = new TextField();

        Label disThresh = new Label("Displaced Threshold");
        TextField disThreshTf = new TextField();

        Button addBtn = new Button("Log in");
        addBtn.setAlignment(Pos.CENTER_RIGHT);

        GridPane getDataPage = new GridPane();
        getDataPage.setAlignment(Pos.CENTER_LEFT);

        getDataPage.setPadding(new Insets(10, 10, 10, 10));
        getDataPage.setVgap(7);
        getDataPage.setHgap(5);

        getDataPage.addColumn(0, degree, runwayL, runwayW, stopL, stopW, tora, asda, disThresh);
        getDataPage.addColumn(1, degreeTf, runwayLTf, runwayWTf, stopLTf, stopWTf, toraTf, asdaTf,
            disThreshTf);
        getDataPage.addColumn(2, posBox, stripL, stripW, clearL, clearW, toda, lda);
        getDataPage.addColumn(3, posTf, stripWTf, stripLTf, clearLTf, clearWTf, todaTf, ldaTf,
            addBtn);
        GridPane.setHalignment(addBtn, HPos.RIGHT);

        addBtn.setOnAction(ActionEvent -> {
            List<TextField> textFields = new ArrayList<>(
                Arrays.asList(runwayLTf, runwayWTf, stripLTf, stripWTf, stopLTf, stopWTf, clearLTf,
                    clearWTf, toraTf, todaTf, asdaTf, ldaTf, disThreshTf));
            Boolean isParallel = posCb.isSelected();

            if (isParallel) {
                char[] posChar = posTf.getText().toCharArray();
                if (validInput(textFields, degreeTf.getText()) && posChar.length == 1 && (
                    Character.toUpperCase(posChar[0]) == 'L'
                        || Character.toUpperCase(posChar[0]) == 'C'
                        || Character.toUpperCase(posChar[0]) == 'R')) {
                    addPRunway(convertTextTodouble(textFields), Integer.valueOf(degreeTf.getText()),
                        posChar[0], airport);
                    printAlert(true);
                    stage.close();
                }
            } else if (validInput(textFields, degreeTf.getText())) {
                addSRunway(convertTextTodouble(textFields), Integer.valueOf(degreeTf.getText()),
                    airport);
                printAlert(true);
                stage.close();
            } else {
                printAlert(false);
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

    private Boolean validInput(List<TextField> textFields, String degree) {
        try {
            Integer.parseInt(degree);
        } catch (Exception e) {
            return false;
        }

        for (TextField textField : textFields) {
            if (!textField.getText().isEmpty()) {
                try {
                    Double.parseDouble(textField.getText());
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }

    private double[] convertTextTodouble(List<TextField> textFields) {
        double[] data = new double[textFields.size()];
        int p = 0;
        for (TextField textField : textFields) {
            data[p] = Double.valueOf(textField.getText());
            p++;
        }

        return data;
    }

    private void addPRunway(double[] data, int degree, char pos, Airport airport) {
        Runway newRunway = new PRunway(degree, pos, data[0], data[1], data[2], data[3], data[4],
            data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12]);
        airport.addRunway(newRunway);
    }

    private void addSRunway(double[] data, int degree, Airport airport) {
        Runway newRunway = new SRunway(degree, data[0], data[1], data[2], data[3], data[4], data[5],
            data[6], data[7], data[8], data[9], data[10], data[11], data[12]);
        airport.addRunway(newRunway);
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
