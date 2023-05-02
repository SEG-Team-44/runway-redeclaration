package com.team44.runwayredeclarationapp.ui.airport;

import com.team44.runwayredeclarationapp.controller.DataController;
import com.team44.runwayredeclarationapp.event.EditAirportListener;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.ui.SelectWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * The modal window to select and then edit an airport
 */
public class ModifyAirportWindow {

    private Stage stage;
    private final Window parent;

    /**
     * The data controller to edit an airport
     */
    private final DataController dataController;

    /**
     * The listener called when an airport has been successfully edited
     */
    private EditAirportListener editAirportListener;

    /**
     * Create the select and edit airport modal windows
     *
     * @param parent         the parent window/stage
     * @param dataController the data controller
     */
    public ModifyAirportWindow(Window parent, DataController dataController) {
        this.parent = parent;
        this.dataController = dataController;

        // Pop up option window for user to choose an airport
        showOptionScene();
    }

    /**
     * Show the selection window
     */
    private void showOptionScene() {
        // Create the select window
        var selectWindow = new SelectWindow<Airport>(parent, "Airport To Edit",
            dataController.getAirportObservableList());
        selectWindow.setStringMethod(Airport::getName);

        // On select event
        selectWindow.setOnSelect((airport) -> {
            showModifyScene((Airport) airport);
        });

        //on delete
        selectWindow.setOnDelete((airport) -> {
            deleteIsPressed((Airport) airport);
        });

        this.stage = selectWindow;
    }

    /**
     * Show the edit airport modal window
     *
     * @param airport the airport to be edited
     */
    private void showModifyScene(Airport airport) {
        var initialAirport = airport.clone();
        var modifyWindow = new AddAirportWindow(stage, dataController, airport);

        // Call listener
        modifyWindow.setAddAirportListener(modifiedAirport -> {
            if (editAirportListener != null) {
                editAirportListener.editAirport(initialAirport, modifiedAirport);
            }
        });
    }

    /**
     * Set the listener to be called when an airport has been successfully edited
     *
     * @param editAirportListener the listener
     */
    public void setEditAirportListener(EditAirportListener editAirportListener) {
        this.editAirportListener = editAirportListener;
    }

    /**
     * Call the delete controller when delete button pressed
     *
     * @param airport airport to be deleted
     */
    private void deleteIsPressed(Airport airport) {
        dataController.deleteAirport(airport);
    }
}
