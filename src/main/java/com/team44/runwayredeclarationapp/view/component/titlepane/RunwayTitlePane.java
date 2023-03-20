package com.team44.runwayredeclarationapp.view.component.titlepane;

import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.ui.AddRunwayWindow;
import com.team44.runwayredeclarationapp.ui.ModifyRunwayWindow;
import com.team44.runwayredeclarationapp.view.MainScene;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorAlert;
import com.team44.runwayredeclarationapp.view.component.inputs.SelectComboBox;
import java.util.Objects;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * The Titled Pane for selecting the runway
 */
public class RunwayTitlePane extends TitledPane {

    /**
     * The combobox to select the runway
     */
    private final SelectComboBox<Runway> runwaySelectComboBox;

    /**
     * The Gridpane displays the runway information
     */
    private GridPane runwayInfo;

    /**
     * Create the titled pane
     *
     * @param mainScene the main scene
     */
    public RunwayTitlePane(MainScene mainScene) {
        // Create titled pane for selecting the runway
        this.setText("Step 2: Select Runway");
        this.setExpanded(false);
        this.setCollapsible(true);

        runwayInfo = new GridPane();

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

        // Create the combobox to select the obstacle
        runwaySelectComboBox = new SelectComboBox<>(mainScene.getRunwayObservableList());
        runwaySelectComboBox.setStringMethod(Runway::getPhyId);
        runwaySelectComboBox.setPromptText("Select Runway");
        runwaySelectComboBox.setMaxWidth(Double.MAX_VALUE);
        runwaySelectComboBox.setOnAction((e) -> {
            // Ensure selected runway is not empty
            var selectedVal = runwaySelectComboBox.getValue();
            if (selectedVal != null) {
                // Set the gui to show the selected runway
                mainScene.updateInitialRunway(runwaySelectComboBox.getValue());
                updateRunwayInfo(runwayBox, runwaySelectComboBox.getValue());
            }
        });

        //Add runway button
        Button addRunwayBtn = new Button("Add Runway");
        addRunwayBtn.setMaxWidth(Double.MAX_VALUE);
        //Generate init window when button clicked
        addRunwayBtn.setOnAction(ActionEvent -> {
            AddRunwayWindow initPage = new AddRunwayWindow(mainScene.getMainWindow().getStage(),
                mainScene.getAirport());

            // Set the new runway listener
            initPage.setNewRunwayListener(runway -> {
                mainScene.updateInitialRunway(runway);
                runwaySelectComboBox.getSelectionModel().select(runway);
            });
        });

        // Edit runway button
        Button modifyBtn = new Button("Edit Runway");
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
                    mainScene.getAirport());

                // Set the runway listener to update
                modifyPage.setNewRunwayListener(runway -> {
                    var selectedRunway = runwaySelectComboBox.getValue();
                    // Only update the gui if the selected runway was the one modified
                    if (selectedRunway != null && Objects.equals(runway.getPhyId(),
                        selectedRunway.getPhyId())) {

                        mainScene.updateInitialRunway(runway);
                        runwaySelectComboBox.setValue(runway);
                    }
                });
            }
        });

        // Add rows
        buttonSelectGridPane.addRow(0, runwaySelectComboBox, addRunwayBtn, modifyBtn);
    }

    /**
     * Check if inputs are valid and show necessary errors
     *
     * @return whether the inputs are valid
     */
    public boolean checkInputsValid() {
        // Create an alert
        ErrorAlert errorAlert = new ErrorAlert();

        // Add the corresponding error messages
        if (runwaySelectComboBox.getValue() == null) {
            errorAlert.addError("Select a runway.");
        }

        // Show the error
        var numberOfErrors = errorAlert.getErrors().size();
        errorAlert.show();

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
     * Update the displayed runway values
     *
     * @param runwayBox VBox containing the buttons & runway info
     * @param runway    current selected runway
     */
    private void updateRunwayInfo(VBox runwayBox, Runway runway) {
        //remove runway info of the previous selected runway
        if (runwayInfo != null) {
            runwayBox.getChildren().remove(runwayInfo);

        }
        //update the info section with new runway values
        runwayInfo = getRunwayInfo(runway);
        runwayBox.getChildren().add(runwayInfo);
    }

    /**
     * Generate the section displaying all runway info
     *
     * @param runway current selected runway
     * @return GridPane containing runway info
     */
    private GridPane getRunwayInfo(Runway runway) {
        GridPane runwayInfo = new GridPane();
        runwayInfo.setVgap(5);
        runwayInfo.setHgap(5);
        runwayInfo.getColumnConstraints()
            .addAll(new ColumnConstraints(100), new ColumnConstraints(80),
                new ColumnConstraints(100), new ColumnConstraints(80));

        Text phyRw = new Text(runway.getPhyId() + ":");
        Text rwL = new Text("Length");
        Text rwLValue = new Text(runway.getRunwayL() + "m");
        Text resa = new Text("RESA");
        Text resaValue = new Text(runway.getResaL() + "m");
        Text designator = new Text("Designator");
        Text rw1 = new Text(runway.getLogicId1());
        Text rw2 = new Text(runway.getLogicId2());
        Text clearway = new Text("Clearway");
        Text clearway1 = new Text(runway.getClearwayL(runway.getLogicId1()) + "m");
        Text clearway2 = new Text(runway.getClearwayL(runway.getLogicId2()) + "m");
        Text stopway = new Text("Stopway");
        Text stopway1 = new Text(runway.getStopwayL(runway.getLogicId1()) + "m");
        Text stopway2 = new Text(runway.getStopwayL(runway.getLogicId2()) + "m");
        Text thresh = new Text("Displaced \nThreshold");
        Text thresh1 = new Text(runway.getDisThresh(runway.getLogicId1()) + "m");
        Text thresh2 = new Text(runway.getDisThresh(runway.getLogicId2()) + "m");

        runwayInfo.addRow(0, designator, rw1, rw2, phyRw);
        runwayInfo.addRow(1, clearway, clearway1, clearway2, rwL, rwLValue);
        runwayInfo.addRow(2, stopway, stopway1, stopway2, resa, resaValue);
        runwayInfo.addRow(3, thresh, thresh1, thresh2);

        return runwayInfo;
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
     * Clear all the inputs
     */
    public void clearInputs() {
        runwaySelectComboBox.setValue(null);
    }
}
