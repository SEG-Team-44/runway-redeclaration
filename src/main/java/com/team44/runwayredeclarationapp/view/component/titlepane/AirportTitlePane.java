package com.team44.runwayredeclarationapp.view.component.titlepane;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.ui.AddAirportWindow;
import com.team44.runwayredeclarationapp.ui.ModifyAirportWindow;
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
                mainScene.getAirportProperty().set(selectedVal);
                mainScene.getRunwayObservableList().setAll(selectedVal.getRunways());
            }
        });
        airportSelectComboBox.valueProperty().bindBidirectional(mainScene.getAirportProperty());

        //Add airport button
        Button addAirportBtn = new Button("Add Airport");
        addAirportBtn.setMaxWidth(Double.MAX_VALUE);
        //Generate init window when button clicked
        addAirportBtn.setOnAction(ActionEvent -> {
            AddAirportWindow initPage = new AddAirportWindow(mainScene.getMainWindow().getStage(),
                mainScene.getAirportObservableList());

        });

        // Edit airport button
        Button modifyBtn = new Button("Edit Airport");
        modifyBtn.setMaxWidth(Double.MAX_VALUE);
        // Generate the airport selection window when button clicked
        modifyBtn.setOnAction(ActionEvent -> {
            if (mainScene.getAirportObservableList().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("There are no airports recorded on the system.");
                a.show();
            } else {
                ModifyAirportWindow modifyPage = new ModifyAirportWindow(
                    mainScene.getMainWindow().getStage(),
                    mainScene.getAirportObservableList());
                modifyPage.setEditAirportListener(airportSelectComboBox::setValue);
            }
        });

        // Add rows
        buttonSelectGridPane.addRow(0, airportSelectComboBox, addAirportBtn, modifyBtn);
    }

    /**
     * Set an airport to be selected in the combobox
     *
     * @param airport the airport
     */
    public void setSelectedAirport(Airport airport) {
        airportSelectComboBox.setValue(airport);
    }

    /**
     * Clear all the inputs
     */
    public void clearInputs() {
        airportSelectComboBox.setValue(null);
    }
}
