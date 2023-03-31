package com.team44.runwayredeclarationapp.view.component.titlepane;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.ui.AddRunwayWindow;
import com.team44.runwayredeclarationapp.ui.ModifyRunwayWindow;
import com.team44.runwayredeclarationapp.view.MainScene;
import com.team44.runwayredeclarationapp.view.component.inputs.SelectComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * The Titled Pane for selecting the airport
 */
public class AirportTitlePane extends TitledPane {

    /**
     * The combobox to select the airport
     */
    private final SelectComboBox<Airport> airportSelectComboBox;


    /**
     * Create the titled pane
     *
     * @param mainScene the main scene
     */
    public AirportTitlePane(MainScene mainScene) {
        // Create titled pane for selecting the airport
        this.setText("Step 1: Select Airport");
        this.setExpanded(true);
        this.setCollapsible(true);

        // Create the horizontal box for selecting airport
        var buttonSelectGridPane = new GridPane();
        buttonSelectGridPane.setHgap(5);
        buttonSelectGridPane.setVgap(5);
        buttonSelectGridPane.getColumnConstraints()
            .addAll(new ColumnConstraints(180), new ColumnConstraints(100),
                new ColumnConstraints(100));
        this.setContent(buttonSelectGridPane);

        // Create the combobox to select the airport
        airportSelectComboBox = new SelectComboBox<>(mainScene.getAirportObservableList());
        airportSelectComboBox.setStringMethod(Airport::getName);
        airportSelectComboBox.setPromptText("Select Airport");
        airportSelectComboBox.setMaxWidth(Double.MAX_VALUE);
        airportSelectComboBox.setOnAction((e) -> {
            // Ensure selected airport is not empty
            var selectedVal = airportSelectComboBox.getValue();
            if (selectedVal != null) {
                // mainScene.updateInitialRunway(airportSelectComboBox.getValue()); // todo
            }
        });

        //Add airport button
        Button addAirportBtn = new Button("Add Airport");
        addAirportBtn.setMaxWidth(Double.MAX_VALUE);
        //Generate init window when button clicked
        addAirportBtn.setOnAction(ActionEvent -> {
            AddRunwayWindow initPage = new AddRunwayWindow(mainScene.getMainWindow().getStage(),
                mainScene.getAirport()); // todo

            // Set the new runway listener
            // initPage.setNewRunwayListener(runway -> {
            //     mainScene.updateInitialRunway(runway);
            //     airportSelectComboBox.getSelectionModel().select(runway);
            // });
        });

        // Edit airport button
        Button modifyBtn = new Button("Edit Airport");
        modifyBtn.setMaxWidth(Double.MAX_VALUE);
        // Generate the airport selection window when button clicked
        modifyBtn.setOnAction(ActionEvent -> {
            if (mainScene.getAirport().getRunways().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("There are no airports recorded on the system.");
                a.show();
            } else {
                ModifyRunwayWindow modifyPage = new ModifyRunwayWindow(
                    mainScene.getMainWindow().getStage(),
                    mainScene.getAirport());

                // Set the runway listener to update
                modifyPage.setNewRunwayListener(runway -> {
                    var selectedRunway = airportSelectComboBox.getValue();
                    // Only update the gui if the selected airport was the one modified
                    // if (selectedRunway != null && Objects.equals(runway.getPhyId(),
                    //     selectedRunway.getPhyId())) {
                    //
                    //     mainScene.updateInitialRunway(runway);
                    //     airportSelectComboBox.setValue(runway);
                    // }
                });
            }
        });

        // Add rows
        buttonSelectGridPane.addRow(0, airportSelectComboBox, addAirportBtn, modifyBtn);
    }
}
