package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.List;

public class ParameterModification {

    Stage stage;

    public ParameterModification(Window parent, Airport airport) {
        stage = new Stage();
        stage.setTitle("Parameters Modification");
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        optionScene(airport);
    }

    private void optionScene(Airport airport) {
        ScrollPane scroll = new ScrollPane();
        ListView<String> options = new ListView<>();

        for (Runway runway : airport.getRunways()) {
            options.getItems().add(runway.getId());
        }
         scroll.setContent(options);

        Button modifyBtn = new Button("Modify");
        modifyBtn.setDisable(true);

        options.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, t1) -> modifyBtn.setDisable(false));

        modifyBtn.setOnAction(ActionEvent ->
            modifyScene(airport, options.getSelectionModel().getSelectedItem())
        );
        VBox optionBox = new VBox();
        optionBox.getChildren().addAll(scroll, modifyBtn);
        optionBox.setAlignment(Pos.CENTER);
        optionBox.setSpacing(5);

        Scene scene = new Scene(optionBox);
        stage.setScene(scene);
        stage.show();
    }

    private void modifyScene(Airport airport, String id) {
        Runway runway = airport.getRunway(id);
        double[] parameters = {runway.getRunwayL(), runway.getRunwayW(), runway.getStripL(), runway.getStripW(),
        runway.getClearwayW(), runway.getResa(), runway.getTORA1(), runway.getTODA1(), runway.getASDA1(), runway.getLDA1(),
        runway.getDisThresh1(), runway.getTORA2(), runway.getTODA2(), runway.getASDA2(), runway.getLDA2(), runway.getDisThresh2()};

        List<String> paraInString = convertDataToStrings(parameters);

        //Physical runway parameter inputs
        Label phyParameter = new Label("Current Physical Parameters of " + id + ":");
        phyParameter.setFont(Font.font("SansSerif", FontWeight.BOLD, 15));

        Label runwayL = new Label("Runway Length (m)");
        TextField runwayLTf = new TextField(paraInString.get(0));
        Label runwayW = new Label("Runway Width (m)");
        TextField runwayWTf = new TextField(paraInString.get(1));
        Label stripL = new Label("Strip Horizontal Length (m)");
        TextField stripLTf = new TextField(paraInString.get(2));
        Label stripW = new Label("Strip Vertical Width (m)");
        TextField stripWTf = new TextField(paraInString.get(3));
        Label clearW = new Label("Clearway Vertical Width (m)");
        TextField clearWTf = new TextField(paraInString.get(4));
        Label resa = new Label("RESA For Both Logical Runways (m) ");
        TextField resaTf = new TextField(paraInString.get(5));
        resaTf.setPrefSize(60, 20);

        GridPane phyPane = new GridPane();
        phyPane.setHgap(5);
        phyPane.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints(60),
                new ColumnConstraints(), new ColumnConstraints(60));
        phyPane.add(phyParameter, 0,0,4,1);
        phyPane.addRow(1, runwayL, runwayLTf, stripL, stripLTf);
        phyPane.addRow(2, runwayW, runwayWTf, stripW, stripWTf);
        phyPane.addRow(3, clearW, clearWTf, resa, resaTf);

        String runway1;
        String runway2;

        if (runway instanceof PRunway) {
            runway1 = String.valueOf(runway.getDegree()) + ((PRunway) runway).getPos();
            runway2 = String.valueOf(runway.getLogicDegree() + ((PRunway) runway).getLogicPos());
        }
        else {
            runway1 = String.valueOf(runway.getDegree());
            runway2 = String.valueOf(runway.getLogicDegree());
        }

        TextField toraTf1 = new TextField(paraInString.get(6));
        TextField todaTf1 = new TextField(paraInString.get(7));
        TextField asdaTf1 = new TextField(paraInString.get(8));
        TextField ldaTf1 = new TextField(paraInString.get(9));
        TextField disThreshTf1 = new TextField(paraInString.get(10));

        //logical runway 1 input interface Layout
        GridPane gridPane1 = getLayout(runway1, toraTf1, todaTf1, asdaTf1, ldaTf1, disThreshTf1);

        TextField toraTf2 = new TextField(paraInString.get(11));
        TextField todaTf2 = new TextField(paraInString.get(12));
        TextField asdaTf2 = new TextField(paraInString.get(13));
        TextField ldaTf2 = new TextField(paraInString.get(14));
        TextField disThreshTf2 = new TextField(paraInString.get(15));

        //logical runway 2 input interface Layout
        GridPane gridPane2 = getLayout(runway2, toraTf2, todaTf2, asdaTf2, ldaTf2, disThreshTf2);

        Button modifyBtn = new Button("Modify");

        Button returnBtn = new Button("Return to Runway Selection");

        //Setup main pane
        GridPane mainPane = new GridPane();
        mainPane.setAlignment(Pos.CENTER_LEFT);
        mainPane.setPadding(new Insets(5, 5, 5, 5));
        mainPane.setVgap(10);

        //Add all layouts & components to main pane
        mainPane.add(phyPane, 0, 0, 2,1);
        mainPane.add(gridPane1, 0,1);
        mainPane.add(gridPane2,0,2);

        //Setup scene
        Scene scene = new Scene(mainPane);
        mainPane.requestFocus();
        scene.setOnMouseClicked(mouseEvent -> {
            mainPane.requestFocus();
        });
        stage.setScene(scene);
        stage.show();
    }

    private GridPane getLayout(String degree,  TextField toraTf, TextField todaTf, TextField asdaTf,
                               TextField ldaTf, TextField disThreshTf) {
        GridPane gridPane = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints(60);
        gridPane.setHgap(5);

        Label toraLbl = new Label("TORA (m)");
        Label todaLbl = new Label("TODA (m)");
        Label asdaLbl = new Label("ASDA (m)");
        Label ldaLbl = new Label("LDA (m)");
        Label disThreshLbl = new Label("Displaced Threshold (m)");
        Label lbl = new Label();
        lbl.setFont(Font.font("SansSerif", FontWeight.BOLD, 15));
        lbl.setText("Current Parameters of " + degree + ":");

        gridPane.getColumnConstraints().addAll(col1, col2, col1, col2);
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.add(lbl,0,0,4,1);
        gridPane.addRow(1, toraLbl, toraTf, asdaLbl, asdaTf);
        gridPane.addRow(2, todaLbl, todaTf, ldaLbl, ldaTf);
        gridPane.addRow(3, disThreshLbl, disThreshTf);

        return gridPane;
    }

    private List<String> convertDataToStrings(double[] parameters) {

        List<String> paraInString = new ArrayList<>();
        for (double parameter : parameters) {
            paraInString.add(String.valueOf(parameter));
        }

        return paraInString;
    }

    private void modifyBtnClicked() {

    }
}
