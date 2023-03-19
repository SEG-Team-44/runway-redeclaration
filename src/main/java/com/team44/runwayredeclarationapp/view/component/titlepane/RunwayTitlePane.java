package com.team44.runwayredeclarationapp.view.component.titlepane;

import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.ui.AddRunwayWindow;
import com.team44.runwayredeclarationapp.ui.ModifyRunwayWindow;
import com.team44.runwayredeclarationapp.ui.RunwaySelectWindow;
import com.team44.runwayredeclarationapp.view.MainScene;
import com.team44.runwayredeclarationapp.view.component.inputs.SelectComboBox;
import java.util.Objects;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * The Titled Pane for selecting the runway
 */
public class RunwayTitlePane extends TitledPane {

    /**
     * The combobox to select the runway
     */
    private final SelectComboBox<Runway> runwaySelectComboBox;

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
        this.setContent(buttonSelectGridPane);

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

        Button modifyBtn = new Button("Edit Runway");
        modifyBtn.setMaxWidth(Double.MAX_VALUE);

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

        Button selectBtn = new Button("Select current Runway");

        selectBtn.setOnAction(ActionEvent -> {
            if (mainScene.getAirport().getRunways().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("There are no runways recorded on the system.");
                a.show();
            } else {
                RunwaySelectWindow selectPage = new RunwaySelectWindow(
                    mainScene.getMainWindow().getStage(),
                    mainScene.getAirport());

                // Set the runway listener to update
                selectPage.setNewRunwayListener(mainScene::updateInitialRunway);
            }
        });

        // Add rows
        // todo remove the old one
        buttonSelectGridPane.addRow(0, runwaySelectComboBox, addRunwayBtn, modifyBtn);
        // buttonSelectGridPane.addRow(1, runwaySelectComboBox);
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
     * Clear all the inputs
     */
    public void clearInputs() {
        runwaySelectComboBox.setValue(null);
    }
}
