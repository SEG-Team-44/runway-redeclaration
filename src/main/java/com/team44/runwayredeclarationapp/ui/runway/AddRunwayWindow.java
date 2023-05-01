package com.team44.runwayredeclarationapp.ui.runway;

import com.team44.runwayredeclarationapp.controller.DataController;
import com.team44.runwayredeclarationapp.event.SetRunwayListener;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.ErrorObjectPair;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.form.RunwayForm;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorAlert;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorListAlert;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * The class responsible for generate & display the UI for adding/modifying a runway
 */
public class AddRunwayWindow {

    /**
     * The listener called when a runway has been added/modified
     */
    private SetRunwayListener setRunwayListener;
    /**
     * The runway form with all the text-field inputs
     */
    private final RunwayForm runwayForm = new RunwayForm();
    /**
     * The selected runway (if any) to modify
     */
    private final Runway selectedRunway;

    /**
     * Column constraints for the logical runway inputs grid
     */
    private final ColumnConstraints[] logicalRunwayConstraints = new ColumnConstraints[]{
        new ColumnConstraints(20), new ColumnConstraints(100), new ColumnConstraints(55),
        new ColumnConstraints(60), new ColumnConstraints(60), new ColumnConstraints(60),
        new ColumnConstraints(60), new ColumnConstraints()
    };

    /**
     * Create the add/modify runway window
     *
     * @param parent         the parent window
     * @param dataController the data controller
     * @param airport        the selected airport
     */
    public AddRunwayWindow(Window parent, DataController dataController, Airport airport) {
        this(parent, dataController, airport, null);
    }

    /**
     * Create the add/modify runway window
     *
     * @param parent         the parent window
     * @param dataController the data controller
     * @param airport        the selected airport
     * @param runway         the current runway (to modify)
     */
    public AddRunwayWindow(Window parent, DataController dataController, Airport airport,
        Runway runway) {
        Stage stage = new Stage();

        // Set selected runway
        this.selectedRunway = runway;
        if (runway != null) {
            runwayForm.setRunway(runway);
        }

        // Input labels for physical runway info
        Label phyParameterLbl = new Label("Physical parameters:");
        Label runwayLLbl = new Label("Runway Length (m)");
        Label runwayWLbl = new Label("Runway Width (m)");
        Label stripLLbl = new Label("Strip End (m)");
        Label clearWLbl = new Label("Clearway Width (m)");
        Label resaLbl = new Label("RESA Length (m) ");

        // Input labels for logical runways
        Label degreeLbl = new Label("Degree (01-36)");
        Label posLbl = new Label("Position");
        Label toraLbl = new Label("TORA (m)");
        Label todaLbl = new Label("TODA (m)");
        Label asdaLbl = new Label("ASDA (m)");
        Label ldaLbl = new Label("LDA (m)");
        Label disThreshLbl = new Label("Displaced Threshold (m)");

        // Row of logical runway input labels
        var logicalRunwaysLabelRow = new GridPane();
        logicalRunwaysLabelRow.setHgap(5);
        logicalRunwaysLabelRow.setVgap(5);
        logicalRunwaysLabelRow.setAlignment(Pos.CENTER_LEFT);

        // Add to row
        logicalRunwaysLabelRow.addRow(0,
            new Pane(), degreeLbl, posLbl, toraLbl, asdaLbl, todaLbl, ldaLbl, disThreshLbl);
        logicalRunwaysLabelRow.getColumnConstraints().setAll(logicalRunwayConstraints);

        // Styling
        phyParameterLbl.setFont(new Font(17));
        runwayForm.getResaTf().setPrefSize(60, 20);

        // Logical runways title
        Label lbl = new Label("Logical Runways:");
        lbl.setFont(new Font(17));

        // Layout the physical input components
        GridPane phyPane = new GridPane();
        phyPane.setHgap(5);
        phyPane.setVgap(5);
        var col = new ColumnConstraints(110);
        phyPane.getColumnConstraints().addAll(col, col, col, col, col);

        // Add physical parameter inputs
        phyPane.add(phyParameterLbl, 0, 0, 4, 1);
        phyPane.addRow(1, runwayLLbl, runwayWLbl, stripLLbl, resaLbl, clearWLbl);
        phyPane.addRow(2,
            runwayForm.getRunwayLTf(), runwayForm.getRunwayWTf(), runwayForm.getStripLTf(),
            runwayForm.getResaTf(), runwayForm.getClearWTf());

        // Components for logical runway 1 runway inputs
        GridPane runway1 = getLayout("1",
            runwayForm.getDegreeTf1(), runwayForm.getPosCb1(), runwayForm.getToraTf1(),
            runwayForm.getTodaTf1(), runwayForm.getAsdaTf1(), runwayForm.getLdaTf1(),
            runwayForm.getDisThreshTf1());

        // Components for logical runway 2 runway inputs
        GridPane runway2 = getLayout("2",
            runwayForm.getDegreeTf2(), runwayForm.getPosCb2(), runwayForm.getToraTf2(),
            runwayForm.getTodaTf2(), runwayForm.getAsdaTf2(), runwayForm.getLdaTf2(),
            runwayForm.getDisThreshTf2());

        // Add/modify runway button
        Button addBtn = new Button(runway == null ? "Add" : "Modify");
        addBtn.setFont(new Font(17));

        // Add/modify event
        addBtn.setOnAction(ActionEvent -> {
            // Validate inputs
            if (!runwayForm.isValid()) {
                new ErrorAlert("Invalid inputs", "Invalid inputs provided",
                    "Please ensure that the inputs are not empty and of the correct type.").show();
                return;
            }

            // Check if its to modify
            ErrorObjectPair<Runway> validationErrors;
            if (runway != null) {
                // Edit runway
                validationErrors = dataController.editRunway(runway, airport,
                    runwayForm.getPosCb1().getValue(),
                    runwayForm.getPosCb2().getValue(),
                    runwayForm.getDegreeTf1().getValue(),
                    runwayForm.getDegreeTf2().getValue(),
                    runwayForm.getParameters());
            } else {
                // Add runway
                validationErrors = dataController.addRunway(airport,
                    runwayForm.getPosCb1().getValue(),
                    runwayForm.getPosCb2().getValue(),
                    runwayForm.getDegreeTf1().getValue(),
                    runwayForm.getDegreeTf2().getValue(),
                    runwayForm.getParameters());
            }

            // Check for validation errors with adding/modifying runway
            if (!validationErrors.hasErrors()) {
                // Call the listener to set the new runway to the UI
                setRunwayListener.updateRunway(validationErrors.getObject());

                // Show success alert
                printAlert(true);
                stage.close();

            } else {
                // Display errors
                var errorAlert = new ErrorListAlert();
                errorAlert.setErrors(validationErrors.getErrorsArray());
                errorAlert.show();
            }
        });

        // Setup main pane
        GridPane mainPane = new GridPane();
        mainPane.setAlignment(Pos.CENTER);

        // Set main pane styling
        mainPane.setPadding(new Insets(15, 15, 15, 15));
        mainPane.setVgap(10);

        // Add all layouts & components to main pane
        mainPane.add(phyPane, 0, 0, 2, 1);
        mainPane.add(lbl, 0, 1);
        mainPane.add(logicalRunwaysLabelRow, 0, 2);
        mainPane.add(runway1, 0, 3);
        mainPane.add(runway2, 0, 4);
        mainPane.add(addBtn, 0, 5);
        GridPane.setHalignment(addBtn, HPos.RIGHT);
        GridPane.setValignment(addBtn, VPos.BOTTOM);

        // Set scene
        Scene scene = new Scene(mainPane);
        mainPane.requestFocus();
        scene.setOnMouseClicked(mouseEvent -> {
            mainPane.requestFocus();
        });

        // Set stage properties
        stage.setTitle("Add new runway");
        stage.setScene(scene);
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        stage.show();
    }

    /**
     * Generate the UI containing given components that ask for inputs for logical runways
     *
     * @param lblContent section title
     * @param degree     TextField for degree
     * @param pos        ComboBox for position character
     * @param tora       TextField for TORA
     * @param toda       TextField for TODA
     * @param asda       TextField for ASDA
     * @param lda        TextField for LDA
     * @param disThresh  TextField for displaced threshold
     * @return a gridPane includes all components
     */
    private GridPane getLayout(String lblContent, TextField degree, ComboBox<Character> pos,
        TextField tora, TextField toda, TextField asda, TextField lda, TextField disThresh) {
        // Create title label
        Label lbl = new Label(lblContent);
        lbl.setFont(new Font(17));

        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(5);
        mainGrid.setVgap(5);
        mainGrid.setAlignment(Pos.CENTER_LEFT);
        mainGrid.getColumnConstraints().setAll(logicalRunwayConstraints);

        // Add grid columns
        mainGrid.addRow(0, lbl, degree, pos, tora, asda, toda, lda, disThresh);

        return mainGrid;
    }

    /**
     * Set the listener to be called when a runway has been selected or updated
     *
     * @param setRunwayListener the listener
     */
    public void setNewRunwayListener(SetRunwayListener setRunwayListener) {
        this.setRunwayListener = setRunwayListener;
    }

    /**
     * Print an alert correspond to the boolean
     *
     * @param success if tasks have been successfully performed
     */
    protected void printAlert(boolean success) {

        Alert a;
        if (success) {
            a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Runway has been logged.");
        } else {
            a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Invalid Input.");
        }

        a.show();
    }
}
