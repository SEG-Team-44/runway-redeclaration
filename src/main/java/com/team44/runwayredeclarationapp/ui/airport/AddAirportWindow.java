package com.team44.runwayredeclarationapp.ui.airport;

import com.team44.runwayredeclarationapp.controller.DataController;
import com.team44.runwayredeclarationapp.event.AddAirportListener;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.ErrorObjectPair;
import com.team44.runwayredeclarationapp.model.form.AirportForm;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorAlert;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorListAlert;
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
    private final AirportForm airportForm = new AirportForm();
    /**
     * The listener called when an airport has been added/edited
     */
    private AddAirportListener addAirportListener;


    /**
     * Create the add airport modal window
     *
     * @param parent         the parent window/stage
     * @param dataController the data controller
     */
    public AddAirportWindow(Window parent, DataController dataController) {
        this(parent, dataController, null);
    }

    /**
     * Create the add airport modal window
     *
     * @param parent         the parent window/stage
     * @param dataController the data controller
     * @param airport        the selected airport (if any)
     */
    public AddAirportWindow(Window parent, DataController dataController,
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

        // If there is an existing airport, set its values
        if (airport != null) {
            airportForm.setAirport(airport);
        }

        // Add event
        addBtn.setOnAction(event -> {
            if (isInputValid()) {

                ErrorObjectPair<Airport> validationErrors;
                if (airport == null) {
                    // Add airport
                    validationErrors = dataController.addAirport(
                        airportForm.getAirportNameInput().getText());
                } else {
                    // Edit airport
                    validationErrors = dataController.editAirport(airport,
                        airportForm.getAirportNameInput().getText());
                }

                // Check if there are any validation errors
                if (!validationErrors.hasErrors()) {
                    // Call the listener
                    if (addAirportListener != null) {
                        addAirportListener.addAirport(validationErrors.getObject());
                    }

                    stage.close();
                } else {
                    // Display errors
                    var errorAlert = new ErrorListAlert();
                    errorAlert.setErrors(validationErrors.getErrorsArray());
                    errorAlert.show();
                }
            }
        });

        // Add rows to grid
        mainPane.addRow(1, new Text("Airport Name:"), airportForm.getAirportNameInput());
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
        // Add error messages if needed
        if (!airportForm.isValid()) {
            new ErrorAlert("Invalid inputs", "Provided airport inputs are invalid!",
                "Ensure that the name field is not empty and under 60 characters.").show();
        }

        return airportForm.isValid();
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
