package com.team44.runwayredeclarationapp.ui;

import com.team44.runwayredeclarationapp.event.AddAirportListener;
import com.team44.runwayredeclarationapp.model.Airport;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * The modal window to select and then edit an airport
 */
public class ModifyAirportWindow {

    private Stage stage;
    private final Window parent;

    /**
     * The observable list of airports
     */
    private final ObservableList<Airport> airportObservableList;

    /**
     * The listener called when an airport has been successfully edited
     */
    private AddAirportListener editAirportListener;

    /**
     * Create the select and edit airport modal windows
     *
     * @param parent                the parent window/stage
     * @param airportObservableList the observable list of airports
     */
    public ModifyAirportWindow(Window parent, ObservableList<Airport> airportObservableList) {
        this.parent = parent;
        this.airportObservableList = airportObservableList;

        // Pop up option window for user to choose an airport
        showOptionScene();
    }

    /**
     * Show the selection window
     */
    private void showOptionScene() {
        // Create the select window
        var selectWindow = new SelectWindow<Airport>(parent, "Airport To Edit",
            airportObservableList);
        selectWindow.setStringMethod(Airport::getName);

        // On select event
        selectWindow.setOnSelect((airport) -> {
            showModifyScene((Airport) airport);
        });

        this.stage = selectWindow;
    }

    /**
     * Show the edit airport modal window
     *
     * @param airport the airport to be edited
     */
    private void showModifyScene(Airport airport) {
        var modifyWindow = new AddAirportWindow(stage, airportObservableList, airport);

        // Call listener
        modifyWindow.setAddAirportListener(modifiedAirport -> {
            if (editAirportListener != null) {
                editAirportListener.addAirport(modifiedAirport);
            }
        });
    }

    /**
     * Set the listener to be called when an airport has been successfully edited
     *
     * @param editAirportListener the listener
     */
    public void setEditAirportListener(AddAirportListener editAirportListener) {
        this.editAirportListener = editAirportListener;
    }
}
