package com.team44.runwayredeclarationapp.ui;

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
import javafx.scene.layout.HBox;
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

        // Components for physical inputs
        Label phyParameter = new Label("Physical parameters:");
        Label runwayL = new Label("Runway Length (m)");
        Label runwayW = new Label("Runway Width (m)");
        Label stripL = new Label("Distance between runway \n end & strip end (m)");
        Label stripW = new Label("Distance between runway \n centreline & strip edge (m)");
        Label clearW = new Label("Clearway Vertical Width (m)");
        Label resa = new Label("RESA Length (m) ");

        // Styling
        phyParameter.setFont(new Font(17));
        runwayForm.getResaTf().setPrefSize(60, 20);

        // Layout the physical input components
        GridPane phyPane = new GridPane();
        phyPane.setHgap(5);
        phyPane.setVgap(5);
        phyPane.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints(60),
            new ColumnConstraints(), new ColumnConstraints(60));

        // Add physical parameter inputs
        phyPane.add(phyParameter, 0, 0, 4, 1);
        phyPane.addRow(1, runwayL, runwayForm.getRunwayLTf(), runwayW, runwayForm.getRunwayWTf());
        phyPane.addRow(2, stripL, runwayForm.getStripLTf(), stripW, runwayForm.getStripWTf());
        phyPane.addRow(3, clearW, runwayForm.getClearWTf(), resa, runwayForm.getResaTf());

        // Components for one logical runway inputs
        String lbl1 = "Logical Runway 1 Parameters:";
        GridPane runway1 = getLayout(lbl1, runwayForm.getDegreeTf1(),
            runwayForm.getPosCb1(), runwayForm.getToraTf1(), runwayForm.getTodaTf1(),
            runwayForm.getAsdaTf1(),
            runwayForm.getLdaTf1(), runwayForm.getDisThreshTf1());

        //Components for the other logical runway inputs
        String lbl2 = "Logical Runway 2 Parameters:";
        GridPane runway2 = getLayout(lbl2, runwayForm.getDegreeTf2(),
            runwayForm.getPosCb2(), runwayForm.getToraTf2(), runwayForm.getTodaTf2(),
            runwayForm.getAsdaTf2(),
            runwayForm.getLdaTf2(), runwayForm.getDisThreshTf2());

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
                errorAlert.setErrors(validationErrors.getErrors().toArray(new String[]{}));
                errorAlert.show();
            }
        });

        // Setup main pane
        GridPane mainPane = new GridPane();
        mainPane.setAlignment(Pos.CENTER);

        mainPane.setPadding(new Insets(5, 5, 5, 5));
        mainPane.setVgap(10);

        // Add all layouts & components to main pane
        mainPane.add(phyPane, 0, 0, 2, 1);
        mainPane.add(runway1, 0, 1);
        mainPane.add(runway2, 0, 2);
        mainPane.add(addBtn, 1, 2);
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

        // Create input labels
        Label degreeLbl = new Label("Degree (01-36)");
        Label toraLbl = new Label("TORA (m)");
        Label todaLbl = new Label("TODA (m)");
        Label asdaLbl = new Label("ASDA (m)");
        Label ldaLbl = new Label("LDA (m)");
        Label disThreshLbl = new Label("Displaced Threshold (m)");

        // Create layout for position input
        HBox posBox = new HBox();
        Label posLbl = new Label("Position (L/C/R)");
        posBox.getChildren().add(posLbl);

        // Column constraints for grid
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints(60);

        // Adding all components to the GridPane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.getColumnConstraints().addAll(col1, col2, col1, col2);

        // Add grid columns
        gridPane.add(lbl, 0, 0, 4, 1);
        gridPane.addRow(1, degreeLbl, degree, posBox, pos);
        gridPane.addRow(2, toraLbl, tora, asdaLbl, asda);
        gridPane.addRow(3, todaLbl, toda, ldaLbl, lda);
        gridPane.addRow(4, disThreshLbl, disThresh);

        return gridPane;
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
