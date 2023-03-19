package com.team44.runwayredeclarationapp.ui;

import com.team44.runwayredeclarationapp.controller.ParameterController;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorAlert;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * The class responsible for generate & display the UI for modify initial parameters of runways
 */
public class ModifyWindow extends ParameterController {

    Stage stage;

    /**
     * Initialising the stage
     *
     * @param parent
     * @param airport current airport
     */
    public ModifyWindow(Window parent, Airport airport) {
        stage = new Stage();
        stage.setTitle("Parameters Modification");
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        //Pop up option window for user to choose runways
        showOptionScene(airport);
    }

    /**
     * Setup & display the option window
     *
     * @param airport current airport
     */
    private void showOptionScene(Airport airport) {
        //listing all runways recorded in the system
        ScrollPane scroll = new ScrollPane();
        ListView<String> options = new ListView<>();

        for (Runway runway : airport.getRunways()) {
            options.getItems().add(runway.getPhyId());
        }
        scroll.setContent(options);

        Button modifyBtn = new Button("Modify");
        modifyBtn.setFont(new Font(17));
        //enable button only when a user selected a runway
        modifyBtn.setDisable(true);

        options.getSelectionModel().selectedItemProperty().addListener(
            (observableValue, s, t1) -> modifyBtn.setDisable(false));

        modifyBtn.setOnAction(ActionEvent ->
            showModifyScene(airport, options.getSelectionModel().getSelectedItem())
        );

        Label lbl = new Label("Select a Runway:");
        lbl.setFont(new Font(18));

        //combine the scroll pane & button
        VBox optionBox = new VBox();
        optionBox.getChildren().addAll(lbl, scroll, modifyBtn);
        optionBox.setAlignment(Pos.CENTER);
        optionBox.setSpacing(5);
        optionBox.setPadding(new Insets(5));

        Scene scene = new Scene(optionBox);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Setup & display the UI for user to update the parameters for the selected runway
     *
     * @param airport current airport
     * @param id      physical id of the selected runway
     */
    private void showModifyScene(Airport airport, String id) {
        Runway runway = airport.getRunway(id);

        //components for physical measurements
        Label phyParameter = new Label("Current Physical Parameters of " + id + ":");
        phyParameter.setFont(new Font(17));

        Label runwayL = new Label("Runway Length (m)");
        TextField runwayLTf = new TextField(String.valueOf(runway.getRunwayL()));
        Label runwayW = new Label("Runway Width (m)");
        TextField runwayWTf = new TextField(String.valueOf(runway.getRunwayW()));
        Label stripL = new Label("Distance between runway \n end & strip end (m)");
        TextField stripLTf = new TextField(String.valueOf(runway.getStripL()));
        Label stripW = new Label("Distance between runway \n centreline & strip edge (m)");
        TextField stripWTf = new TextField(String.valueOf(runway.getStripW()));
        Label clearW = new Label("Clearway Vertical Width (m)");
        TextField clearWTf = new TextField(String.valueOf(runway.getClearwayW()));

        Label resa = new Label("RESA Length (m) ");
        TextField resaTf = new TextField(String.valueOf(runway.getResaL()));
        resaTf.setPrefSize(60, 20);

        //combine components of physical input part
        GridPane phyPane = new GridPane();
        phyPane.setHgap(5);
        phyPane.setVgap(5);
        phyPane.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints(60),
            new ColumnConstraints(), new ColumnConstraints(60));
        phyPane.add(phyParameter, 0, 0, 4, 1);
        phyPane.addRow(1, runwayL, runwayLTf, runwayW, runwayWTf);
        phyPane.addRow(2, stripL, stripLTf, stripW, stripWTf);
        phyPane.addRow(3, clearW, clearWTf, resa, resaTf);

        //components for 1 logical parameters
        String runway1 = runway.getLogicId1();
        TextField toraTf1 = new TextField(String.valueOf(runway.getTora(runway1)));
        TextField todaTf1 = new TextField(String.valueOf(runway.getToda(runway1)));
        TextField asdaTf1 = new TextField(String.valueOf(runway.getAsda(runway1)));
        TextField ldaTf1 = new TextField(String.valueOf(runway.getLda(runway1)));
        TextField disThreshTf1 = new TextField(String.valueOf(runway.getDisThresh(runway1)));

        //logical runway 1 input interface Layout
        GridPane gridPane1 = getLayout(runway1, toraTf1, todaTf1, asdaTf1, ldaTf1, disThreshTf1);

        //components for the other logical parameters
        String runway2 = runway.getLogicId2();
        TextField toraTf2 = new TextField(String.valueOf(runway.getTora(runway2)));
        TextField todaTf2 = new TextField(String.valueOf(runway.getToda(runway2)));
        TextField asdaTf2 = new TextField(String.valueOf(runway.getAsda(runway2)));
        TextField ldaTf2 = new TextField(String.valueOf(runway.getLda(runway2)));
        TextField disThreshTf2 = new TextField(String.valueOf(runway.getDisThresh(runway2)));

        //logical runway 2 input interface Layout
        GridPane gridPane2 = getLayout(runway2, toraTf2, todaTf2, asdaTf2, ldaTf2, disThreshTf2);

        TextField[] textFields = {runwayLTf, runwayWTf, stripLTf, stripWTf, clearWTf, resaTf,
            toraTf1,
            todaTf1, asdaTf1, ldaTf1, toraTf2, todaTf2, asdaTf2, ldaTf2, disThreshTf1,
            disThreshTf2};

        Button modifyBtn = new Button("Modify");
        modifyBtn.setFont(new Font(15));
        modifyBtn.setOnAction(ActionEvent -> {
            //update all values if inputs are valid
            if (validNumericalInput(textFields)) {
                runway.updateParameters(convertTextToDouble(textFields));

                // Call the listener to set the updated runway to the UI
                setRunwayListener.updateRunway(runway);
                printAlert(true);
                stage.close();
            }

            //else print alert
            else {
                var errorAlert = new ErrorAlert();
                errorAlert.setErrors(getErrors().toArray(new String[]{}));
                getErrors();
                errorAlert.show();
            }
        });

        //return to option page
        Button returnBtn = new Button("Back");
        returnBtn.setFont(new Font(15));
        returnBtn.setOnAction(ActionEvent -> {
            showOptionScene(airport);
        });

        HBox buttons = new HBox();
        buttons.setSpacing(2);
        buttons.setAlignment(Pos.BOTTOM_RIGHT);
        buttons.getChildren().addAll(returnBtn, modifyBtn);

        //Setup main pane
        GridPane mainPane = new GridPane();
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setPadding(new Insets(5, 5, 5, 5));
        mainPane.setVgap(10);

        //Add all layouts & components to main pane
        mainPane.add(phyPane, 0, 0, 2, 1);
        mainPane.add(gridPane1, 0, 1);
        mainPane.add(gridPane2, 0, 2);
        mainPane.add(buttons, 1, 2);
        GridPane.setHalignment(buttons, HPos.RIGHT);
        GridPane.setValignment(buttons, VPos.BOTTOM);

        //Setup scene
        Scene scene = new Scene(mainPane);
        mainPane.requestFocus();
        scene.setOnMouseClicked(mouseEvent -> {
            mainPane.requestFocus();
        });
        stage.setScene(scene);
        stage.show();
    }

    private GridPane getLayout(String degree, TextField toraTf, TextField todaTf, TextField asdaTf,
        TextField ldaTf, TextField disThreshTf) {
        GridPane gridPane = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints(60);
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        Label lbl = new Label();
        lbl.setFont(new Font(17));
        lbl.setText("Current Parameters of " + degree + ":");
        Label toraLbl = new Label("TORA (m)");
        Label todaLbl = new Label("TODA (m)");
        Label asdaLbl = new Label("ASDA (m)");
        Label ldaLbl = new Label("LDA (m)");
        Label disThreshLbl = new Label("Displaced Threshold (m)");

        gridPane.getColumnConstraints().addAll(col1, col2, col1, col2);
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.add(lbl, 0, 0, 4, 1);
        gridPane.addRow(1, toraLbl, toraTf, asdaLbl, asdaTf);
        gridPane.addRow(2, todaLbl, todaTf, ldaLbl, ldaTf);
        gridPane.addRow(3, disThreshLbl, disThreshTf);

        return gridPane;
    }

    @Override
    protected void printAlert(boolean success) {

        super.printAlert(success);
        Alert a;

        if (success) {
            a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Parameters updated.");
        } else {
            a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Invalid Input.");
        }

        a.show();
    }
}
