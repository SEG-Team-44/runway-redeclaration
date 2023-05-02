package com.team44.runwayredeclarationapp.ui.runway;

import com.team44.runwayredeclarationapp.controller.DataController;
import com.team44.runwayredeclarationapp.event.EditRunwayListener;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.ui.SelectWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * The class responsible for generate & display the UI for modify initial parameters of runways
 */
public class ModifyRunwayWindow {

    /**
     * The parent window
     */
    private final Window parent;
    private Stage stage;

    /**
     * The listener that is called when the runway has been successfully modified
     */
    private EditRunwayListener setRunwayListener;
    /**
     * The data controller to modify the runway
     */
    private final DataController dataController;
    private SelectWindow<Runway> selectRunwayWindow;

    /**
     * Initialising the stage
     *
     * @param parent         the parent window
     * @param dataController the data controller
     * @param airport        current airport
     */
    public ModifyRunwayWindow(Window parent, DataController dataController, Airport airport) {
        this.parent = parent;
        this.dataController = dataController;

        //Pop up option window for user to choose runways
        showOptionScene(airport);
    }

    /**
     * Setup & display the option window
     *
     * @param airport current airport
     */
    private void showOptionScene(Airport airport) {
        //listing all runways recorded in the system
        selectRunwayWindow = new SelectWindow<>(parent, "Runway To Edit",
            airport.getRunwayObservableList());
        selectRunwayWindow.setStringMethod(Runway::getPhyId);

        // Set on select event
        selectRunwayWindow.setOnSelect((selectedRunway) -> {
            showModifyScene(airport, (Runway) selectedRunway);
        });

        // Set on runway delete
        selectRunwayWindow.setOnDelete((selectedRunway) -> {
            deleteIsPressed(airport, (Runway) selectedRunway);
        });

        this.stage = selectRunwayWindow;
    }

    /**
     * Setup & display the UI for user to update the parameters for the selected runway
     *
     * @param airport current airport
     * @param runway  the selected runway
     */
    private void showModifyScene(Airport airport, Runway runway) {
        // Show the add runway window with the selected runway
        var addRunwayWindow = new AddRunwayWindow(stage, dataController, airport, runway);

        // Set the successfully modified listener
        addRunwayWindow.setNewRunwayListener(newRunway -> {
            setRunwayListener.editRunway(runway, newRunway);

            // Refresh the selection window
            selectRunwayWindow.refresh();
        });
    }

    /**
     * Handle when a runway is to be deleted
     *
     * @param airport current airport
     * @param runway  runway to be removed
     */
    private void deleteIsPressed(Airport airport, Runway runway) {
        dataController.deleteRunway(airport, runway);
    }

    /**
     * Set the listener to be called when a runway has been selected or updated
     *
     * @param setRunwayListener the listener
     */
    public void setNewRunwayListener(EditRunwayListener setRunwayListener) {
        this.setRunwayListener = setRunwayListener;
    }
}
