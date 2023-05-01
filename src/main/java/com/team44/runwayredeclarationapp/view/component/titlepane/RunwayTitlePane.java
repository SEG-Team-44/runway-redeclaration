package com.team44.runwayredeclarationapp.view.component.titlepane;

import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.ui.runway.AddRunwayWindow;
import com.team44.runwayredeclarationapp.ui.runway.ModifyRunwayWindow;
import com.team44.runwayredeclarationapp.view.MainScene;
import com.team44.runwayredeclarationapp.view.component.RunwayInfoGrids;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorListAlert;
import com.team44.runwayredeclarationapp.view.component.inputs.SelectComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * The Titled Pane for selecting the runway
 */
public class RunwayTitlePane extends TitledPane {

    /**
     * The combobox to select the runway
     */
    private final SelectComboBox<Runway> runwaySelectComboBox;

    /**
     * The hbox that contains the grids displaying the runway information
     */
    private final RunwayInfoGrids runwayInfoGrids;

    /**
     * Create the titled pane
     *
     * @param mainScene the main scene
     */
    public RunwayTitlePane(MainScene mainScene) {
        // Create titled pane for selecting the runway
        this.setText("Step 2: Select Runway");
        this.setExpanded(true);
        this.setCollapsible(true);

        // Create the horizontal box for selecting runway
        var buttonSelectGridPane = new GridPane();
        buttonSelectGridPane.setHgap(5);
        buttonSelectGridPane.setVgap(5);
        buttonSelectGridPane.getColumnConstraints()
            .addAll(new ColumnConstraints(180), new ColumnConstraints(100),
                new ColumnConstraints(100));

        VBox runwayBox = new VBox();
        runwayBox.setSpacing(10);
        runwayBox.getChildren().add(buttonSelectGridPane);

        this.setContent(runwayBox);

        // Runway information grids
        runwayInfoGrids = new RunwayInfoGrids();

        // Create the combobox to select the obstacle
        runwaySelectComboBox = new SelectComboBox<>(mainScene.getRunwayObservableList());
        runwaySelectComboBox.setStringMethod(Runway::getPhyId);
        runwaySelectComboBox.setPromptText("Select Runway");
        runwaySelectComboBox.setMaxWidth(Double.MAX_VALUE);
        runwaySelectComboBox.setDisable(true);
        runwaySelectComboBox.setOnAction((e) -> {
            // Ensure selected runway is not empty
            var selectedVal = runwaySelectComboBox.getValue();
            if (selectedVal != null) {
                // Set the gui to show the selected runway
                mainScene.updateInitialRunway(runwaySelectComboBox.getValue());
            }
        });

        //Add runway button
        Button addRunwayBtn = new Button("Add Runway");
        addRunwayBtn.setDisable(true);
        addRunwayBtn.setMaxWidth(Double.MAX_VALUE);
        //Generate init window when button clicked
        addRunwayBtn.setOnAction(ActionEvent -> {
            AddRunwayWindow initPage = new AddRunwayWindow(mainScene.getMainWindow().getStage(),
                mainScene.getDataController(), mainScene.getAirport());

            // Set the new runway listener
            initPage.setNewRunwayListener(runway -> {
                mainScene.updateInitialRunway(runway);
                runwaySelectComboBox.getSelectionModel().select(runway);
                runwayInfoGrids.setRunway(runway);
            });
        });

        // Edit runway button
        Button modifyBtn = new Button("Edit Runway");
        modifyBtn.setDisable(true);
        modifyBtn.setMaxWidth(Double.MAX_VALUE);
        // Generate the runway selection window when button clicked
        modifyBtn.setOnAction(ActionEvent -> {
            if (mainScene.getAirport().getRunways().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("There are no runways recorded on the system.");
                a.show();
            } else {
                ModifyRunwayWindow modifyPage = new ModifyRunwayWindow(
                    mainScene.getMainWindow().getStage(),
                    mainScene.getDataController(),
                    mainScene.getAirport());

                // Set the runway listener to update
                modifyPage.setNewRunwayListener((oldRunway, newRunway) -> {
                    var selectedRunway = runwaySelectComboBox.getValue();
                    // Only update the gui if the selected runway was the one modified
                    if (oldRunway.equals(selectedRunway)) {
                        mainScene.updateInitialRunway(newRunway);
                        // Clear and set the combobox
                        runwaySelectComboBox.getSelectionModel().clearSelection();
                        runwaySelectComboBox.setValue(newRunway);
                        runwayInfoGrids.setRunway(newRunway);
                    }
                });
            }
        });

        // Only enable the button if airport selected
        mainScene.getAirportProperty().addListener((obsValue, oldAirport, newAirport) -> {
            if (newAirport != null) {
                runwaySelectComboBox.setDisable(false);
                addRunwayBtn.setDisable(false);
                modifyBtn.setDisable(false);
            } else {
                runwaySelectComboBox.setDisable(true);
                addRunwayBtn.setDisable(true);
                modifyBtn.setDisable(true);
            }
        });

        // Add rows
        buttonSelectGridPane.addRow(0, runwaySelectComboBox, addRunwayBtn, modifyBtn);

        // Only show the runway information if runway selected
        runwaySelectComboBox.valueProperty().addListener((obsValue, oldRunway, newRunway) -> {
            if (newRunway != null) {
                runwayInfoGrids.setRunway(newRunway);

                // Ensure that the grid hasn't already been added
                if (!runwayBox.getChildren().contains(runwayInfoGrids)) {
                    runwayBox.getChildren().add(runwayInfoGrids);
                }
            } else {
                runwayBox.getChildren().remove(runwayInfoGrids);
            }
        });
    }

    /**
     * Check if inputs are valid and show necessary errors
     *
     * @return whether the inputs are valid
     */
    public boolean checkInputsValid() {
        // Create an alert
        ErrorListAlert errorListAlert = new ErrorListAlert();

        // Add the corresponding error messages
        if (runwaySelectComboBox.getValue() == null) {
            errorListAlert.addError("Select a runway.");
        }

        // Show the error
        var numberOfErrors = errorListAlert.getErrors().size();
        errorListAlert.show();

        // Return whether the inputs are valid or not
        return numberOfErrors == 0;
    }

    /**
     * Set a runway to be selected in the combobox
     *
     * @param runway the runway
     */
    public void setSelectedRunway(Runway runway) {
        runwaySelectComboBox.setValue(runway);
    }

    /**
     * Get the currently selected runway
     *
     * @return the runway
     */
    public Runway getSelectedRunway() {
        return runwaySelectComboBox.getValue();
    }

    /**
     * Get the combobox to select the runway
     *
     * @return the combobox
     */
    public SelectComboBox<Runway> getRunwaySelectComboBox() {
        return runwaySelectComboBox;
    }

    /**
     * Clear all the inputs
     */
    public void clearInputs() {
        runwaySelectComboBox.setValue(null);
    }
}
