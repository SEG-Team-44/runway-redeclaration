package com.team44.runwayredeclarationapp.ui.airport;

import com.team44.runwayredeclarationapp.event.AddAirportListener;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorListAlert;
import com.team44.runwayredeclarationapp.view.component.inputs.RegexField;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * The modal window to add/edit an airport
 */
public class AddAirportWindow {

    /**
     * The input taking in the airport name
     */
    private final RegexField airportNameInput;
    /**
     * The error alert
     */
    private final ErrorListAlert errorListAlert = new ErrorListAlert();
    /**
     * The listener called when an airport has been added/edited
     */
    private AddAirportListener addAirportListener;


    /**
     * Create the add airport modal window
     *
     * @param parent                the parent window/stage
     * @param airportObservableList the observable list of airports
     */
    public AddAirportWindow(Window parent, ObservableList<Airport> airportObservableList) {
        this(parent, airportObservableList, null);
    }

    /**
     * Create the add airport modal window
     *
     * @param parent                the parent window/stage
     * @param airportObservableList the observable list of airports
     */
    public AddAirportWindow(Window parent, ObservableList<Airport> airportObservableList,
        Airport airport) {
        Stage stage = new Stage();

        //Setup main pane
        GridPane mainPane = new GridPane();
        mainPane.setAlignment(Pos.CENTER);

        // Grid properties
        mainPane.setPadding(new Insets(5, 5, 5, 5));
        mainPane.setHgap(15);
        mainPane.setVgap(10);

        // Add button
        Button addBtn = new Button(airport == null ? "Add" : "Update");
        addBtn.setFont(new Font(17));
        GridPane.setHalignment(addBtn, HPos.RIGHT);
        GridPane.setValignment(addBtn, VPos.BOTTOM);

        // Inputs
        String airportNameRegex = "^.{0,60}$";
        airportNameInput = new RegexField(airportNameRegex);

        // If there is an existing airport, set its values
        if (airport != null) {
            airportNameInput.setText(airport.getName());
        }

        // Add event
        addBtn.setOnAction(event -> {
            if (isInputValid()) {
                var newAirport = new Airport(airportNameInput.getText());

                if (airport == null) {
                    // Create and add the airport to the observable list
                    airportObservableList.add(newAirport);
                } else {
                    // Update the airport in the observable list
                    var index = airportObservableList.indexOf(airport);

                    // Set airport list of runways
                    newAirport.setRunways(airport.getRunways());

                    airportObservableList.set(index, newAirport);
                }
                // Call the listener
                if (addAirportListener != null) {
                    addAirportListener.addAirport(newAirport);
                }

                // Close the window
                stage.close();
            } else {
                // Show alert with errors if input is not valid
                errorListAlert.show();
            }
        });

        // Add rows to grid
        mainPane.addRow(1, new Text("Airport Name:"), airportNameInput);
        mainPane.add(addBtn, 0, 3, 2, 1);

        //Set scene
        Scene scene = new Scene(mainPane);
        mainPane.requestFocus();
        scene.setOnMouseClicked(mouseEvent -> mainPane.requestFocus());

        // Set stage properties and make it a modal window
        stage.setTitle((airport == null ? "Add" : "Update") + " airport");
        stage.setScene(scene);
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        stage.show();
    }

    /**
     * Check if the user inputs are valid
     *
     * @return whether user input is valid or not
     */
    private Boolean isInputValid() {
        // Check the regex and text empty
        var checkRegex = airportNameInput.isRegexMatch();
        var checkEmpty = airportNameInput.isEmpty();

        // Add error messages if needed
        if (!checkRegex) {
            errorListAlert.addError("Name must be under 60 characters.");
        }
        if (checkEmpty) {
            errorListAlert.addError("Inputs cannot be empty.");
        }

        // Return if the inputs are valid or not
        return checkRegex && !checkEmpty;
    }

    /**
     * Set the listener to be called when an airport is added/edited
     *
     * @param addAirportListener the listener
     */
    public void setAddAirportListener(AddAirportListener addAirportListener) {
        this.addAirportListener = addAirportListener;
    }
}
