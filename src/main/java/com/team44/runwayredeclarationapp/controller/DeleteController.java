package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.view.MainScene;
import com.team44.runwayredeclarationapp.view.component.alert.ConfirmAlert;
import com.team44.runwayredeclarationapp.view.component.alert.InfoAlert;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Class responsible for removing an item from the system
 * and informing the UI to update
 */
public class DeleteController {

    /**
     * Handle when an airport is to be deleted
     * @param airport airport to be deleted
     * @param airportObservableList list of airports
     */
    public void deleteAirport(Airport airport, ObservableList<Airport> airportObservableList) {
        Alert confirmAlert = createConfirmAlert(airport.getName() + "Airport");

        //delete the airport if OK btn pressed
        Optional<ButtonType> btnType = confirmAlert.showAndWait();
        if (btnType.get() == ButtonType.OK) {
            airportObservableList.remove(airport);

            printInfoAlert(airport.getName() + "Airport");
        }
    }

    /**
     * Handle when a runway is to be deleted
     * @param airport current airport
     * @param runway runway to be deleted
     */
    public void deleteRunway(Airport airport, Runway runway) {
        Alert confirmAlert = createConfirmAlert("runway" + runway.getPhyId());

        //remove the runway from the airport when OK btn pressed
        Optional<ButtonType> btnType = confirmAlert.showAndWait();
        if (btnType.get() == ButtonType.OK) {
            airport.removeRunway(runway);

            printInfoAlert("runway" + runway.getPhyId());
        }
    }

    /**
     * Handle when an obstacle is to be deleted
     * @param obstacle obstacle to be deleted
     * @param obstacles list of obstacles
     */
    public void deleteObstacle(Obstacle obstacle, ObservableList<Obstacle> obstacles) {
        Alert confirmAlert = createConfirmAlert(obstacle.getObstName());

        //delete the obstacle if OK btn pressed
        Optional<ButtonType> btnType = confirmAlert.showAndWait();
        if (btnType.get() == ButtonType.OK) {
            obstacles.remove(obstacle);

            printInfoAlert(obstacle.getObstName());
        }
    }

    /**
     * Generate confirmation alert
     * @param name item name
     * @return confirmation alert
     */
    private ConfirmAlert createConfirmAlert(String name) {
        return new ConfirmAlert("Confirmation", "Do you want to remove " +
                name + " from the system?");
    }

    /**
     * Print an information alert informing action success
     * @param name item name
     */
    private void printInfoAlert(String name) {
        //inform user that deletion is successful
        InfoAlert infoAlert = new InfoAlert("Delete successful", "Delete successful",
                name + " has been removed");
        infoAlert.show();
    }
}

