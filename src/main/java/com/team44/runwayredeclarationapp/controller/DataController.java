package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.ErrorObjectPair;
import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.SRunway;
import com.team44.runwayredeclarationapp.view.component.alert.ConfirmAlert;
import com.team44.runwayredeclarationapp.view.component.alert.InfoAlert;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * The data controller for handling the list of airports, runways and obstacles
 */
public class DataController {

    /**
     * The observable list of airports
     */
    private final ObservableList<Airport> airportObservableList = FXCollections.observableArrayList();
    /**
     * The observable list of runways
     */
    private final ObservableList<Runway> runwayObservableList = FXCollections.observableArrayList();
    /**
     * The observable list of obstacles
     */
    private final ObservableList<Obstacle> obstacleObservableList = FXCollections.observableArrayList();

    /**
     * The log of the user actions
     */
    private final ObservableList<String> userLog = FXCollections.observableArrayList();

    /**
     * Add a new airport to the list of airports
     *
     * @param airportName the airport name
     * @return the pair containing the created airport and a list of errors
     */
    public ErrorObjectPair<Airport> addAirport(String airportName) {
        // Validate the airport inputs
        var validationErrors = ValidationController.validateAirport(airportName);

        //Create new airport
        var newAirport = new Airport(airportName);

        // Successful
        if (validationErrors.isEmpty()) {
            // Add airport to the list
            airportObservableList.add(newAirport);

            // Log the action
            logAction("Airport Added",
                "Airport (" + airportName + ") has been successfully added.");
        }

        return new ErrorObjectPair<>(newAirport, validationErrors);
    }

    /**
     * Edit an airport in the list of airports
     *
     * @param airport     the airport to modify
     * @param airportName the airport name
     * @return the pair containing the modified airport and a list of errors
     */
    public ErrorObjectPair<Airport> editAirport(Airport airport, String airportName) {
        // Validate the airport inputs
        var validationErrors = ValidationController.validateAirport(airportName);

        // Get the index of the current airport in the observable list
        var index = airportObservableList.indexOf(airport);

        // Create new airport and set list of runways
        var newAirport = new Airport(airportName);
        newAirport.setRunways(airport.getRunways());

        // Successful
        if (validationErrors.isEmpty()) {
            // Replace current airport with new (modified) airport
            airportObservableList.set(index, newAirport);

            // Log the action
            logAction("Airport Edited",
                "Airport (" + airportName + ") has been successfully edited.");
        }

        return new ErrorObjectPair<>(newAirport, validationErrors);
    }

    /**
     * Add a runway to the list of runways
     *
     * @param airport    the airport to add the runway to
     * @param pos1       the position 1 char
     * @param pos2       the position 2 char
     * @param degree1    the degree of logical runway 1
     * @param degree2    the degree of logical runway 2
     * @param parameters the array of parameters
     * @return the pair containing the runway and a list of errors
     */
    public ErrorObjectPair<Runway> addRunway(Airport airport, Character pos1, Character pos2,
        int degree1, int degree2, double[] parameters) {

        // Validate the runway inputs
        var validationErrors = ValidationController.validateRunway(
            pos1, pos2, degree1, degree2, parameters, airport);

        // Add the corresponding runway
        Runway newRunway = null;
        if (validationErrors.size() == 0) {
            // Check if it's a parallel runway
            boolean isParallel = (pos1 != null && pos2 != null);

            // Add airport to the list
            if (isParallel) {
                newRunway = new PRunway(degree1, degree2, pos1, pos2, parameters);
                airport.addRunway(newRunway);
            } else {
                newRunway = new SRunway(degree1, degree2, parameters);
                airport.addRunway(newRunway);
            }

            // Log the action
            logAction("Runway Added",
                "Runway (" + newRunway.getPhyId() + ") has been successfully added.");
        }

        // Return the added runway and errors
        return new ErrorObjectPair<>(newRunway, validationErrors);
    }

    /**
     * Edit a runway in the list of runways
     *
     * @param runway     the runway to edit
     * @param airport    the airport of the runway
     * @param pos1       the position 1 char
     * @param pos2       the position 2 char
     * @param degree1    the degree of logical runway 1
     * @param degree2    the degree of logical runway 2
     * @param parameters the new parameters
     * @return the pair containing the runway and a list of errors
     */
    public ErrorObjectPair<Runway> editRunway(Runway runway, Airport airport, Character pos1,
        Character pos2,
        int degree1, int degree2, double[] parameters) {

        // Check if logic id has been changed
        String newID;
        if (runway instanceof PRunway) {
            newID = PRunway.createPhyId(degree1, degree2, pos1, pos2);
        } else {
            newID = Runway.createPhyId(degree1, degree2);
        }

        // Validate the runway inputs
        var validationErrors = ValidationController.validateRunway(
            pos1, pos2, degree1, degree2, parameters, airport, newID.equals(runway.getPhyId()));

        // Check there are no errors
        if (validationErrors.size() == 0) {
            if (runway instanceof PRunway) {
                ((PRunway) runway).setPosition(pos1, pos2);
            }
            runway.setDegree(degree1, degree2);
            runway.updateParameters(parameters);

            // Log the action
            logAction("Runway Edited",
                "Runway (" + runway.getPhyId() + ") has been successfully edited.");
        }

        // Return the edited runway and errors
        return new ErrorObjectPair<>(runway, validationErrors);
    }

    /**
     * Add a new obstacle to the list of obstacles
     *
     * @param obstacleName   the obstacle name
     * @param obstacleHeight the obstacle height
     * @return the pair containing the created obstacle and a list of errors
     */
    public ErrorObjectPair<Obstacle> addObstacle(String obstacleName, double obstacleHeight) {
        // Validate the obstacle inputs
        var validationErrors = ValidationController.validateObstacle(obstacleName, obstacleHeight);

        // Create the new obstacle
        var newObstacle = new Obstacle(obstacleName, obstacleHeight);

        // Successful
        if (validationErrors.isEmpty()) {
            // Add obstacle to list
            obstacleObservableList.add(newObstacle);

            // Log the action
            logAction("Obstacle Added",
                "Obstacle (" + obstacleName + ") has been successfully added.");
        }

        return new ErrorObjectPair<>(newObstacle, validationErrors);
    }

    /**
     * Edit an obstacle from the list of obstacles
     *
     * @param obstacle       the obstacle to edit
     * @param obstacleName   the obstacle name
     * @param obstacleHeight the obstacle height
     * @return the pair containing the edited obstacle and a list of errors
     */
    public ErrorObjectPair<Obstacle> editObstacle(Obstacle obstacle, String obstacleName,
        double obstacleHeight) {
        // Validate the obstacle inputs
        var validationErrors = ValidationController.validateObstacle(obstacleName, obstacleHeight);

        // Get the index of the obstacle to edit in the list
        var index = obstacleObservableList.indexOf(obstacle);

        // Create the new obstacle
        var newObstacle = new Obstacle(obstacleName, obstacleHeight);

        // Successful
        if (validationErrors.isEmpty()) {
            // Replace it with the current obstacle in list
            obstacleObservableList.set(index, newObstacle);

            // Log the action
            logAction("Obstacle Edited",
                "Obstacle (" + obstacleName + ") has been successfully edited.");
        }

        return new ErrorObjectPair<>(newObstacle, validationErrors);
    }

    /**
     * Handle when an airport is to be deleted
     *
     * @param airport airport to be deleted
     */
    public void deleteAirport(Airport airport) {
        Alert confirmAlert = createConfirmAlert(airport.getName() + "Airport");

        //delete the airport if OK btn pressed
        Optional<ButtonType> btnType = confirmAlert.showAndWait();
        if (btnType.get() == ButtonType.OK) {
            airportObservableList.remove(airport);

            // Log
            logAction("Airport Deleted",
                "Airport (" + airport.getName() + ") has been successfully deleted.");

            printInfoAlert(airport.getName() + "Airport");
        }
    }

    /**
     * Handle when a runway is to be deleted
     *
     * @param airport current airport
     * @param runway  runway to be deleted
     */
    public void deleteRunway(Airport airport, Runway runway) {
        Alert confirmAlert = createConfirmAlert("runway" + runway.getPhyId());

        //remove the runway from the airport when OK btn pressed
        Optional<ButtonType> btnType = confirmAlert.showAndWait();
        if (btnType.get() == ButtonType.OK) {
            airport.removeRunway(runway);

            // Log
            logAction("Runway Deleted",
                "Runway (" + runway.getPhyId() + ") has been successfully deleted.");

            printInfoAlert("runway" + runway.getPhyId());
        }
    }

    /**
     * Handle when an obstacle is to be deleted
     *
     * @param obstacle obstacle to be deleted
     */
    public void deleteObstacle(Obstacle obstacle) {
        Alert confirmAlert = createConfirmAlert(obstacle.getObstName());

        //delete the obstacle if OK btn pressed
        Optional<ButtonType> btnType = confirmAlert.showAndWait();
        if (btnType.get() == ButtonType.OK) {
            obstacleObservableList.remove(obstacle);

            // Log
            logAction("Obstacle Deleted",
                "Obstacle (" + obstacle.getObstName() + ") has been successfully deleted.");

            printInfoAlert(obstacle.getObstName());
        }
    }

    /**
     * Generate confirmation alert
     *
     * @param name item name
     * @return confirmation alert
     */
    private ConfirmAlert createConfirmAlert(String name) {
        return new ConfirmAlert("Confirmation", "Do you want to remove " +
            name + " from the system?");
    }

    /**
     * Print an information alert informing action success
     *
     * @param name item name
     */
    private void printInfoAlert(String name) {
        //inform user that deletion is successful
        InfoAlert infoAlert = new InfoAlert("Delete successful", "Delete successful",
            name + " has been removed");
        infoAlert.show();
    }

    /**
     * Get the observable list of airports
     *
     * @return the observable list of airports
     */
    public ObservableList<Airport> getAirportObservableList() {
        return airportObservableList;
    }

    /**
     * Get the observable list of runways
     *
     * @return the observable list of runways
     */
    public ObservableList<Runway> getRunwayObservableList() {
        return runwayObservableList;
    }

    /**
     * Get the observable list of obstacles
     *
     * @return the observable list of obstacles
     */
    public ObservableList<Obstacle> getObstacleObservableList() {
        return obstacleObservableList;
    }

    /**
     * Log an action to the program logger
     *
     * @param action      the action
     * @param description the description of the action
     */
    public void logAction(String action, String description) {
        // Get the current date and time
        var timeNow = LocalDateTime.now();
        var timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        var logString = "[" + action + "] " + timeNow.format(timeFormatter) + " - " + description;

        // Add the string to the log array
        userLog.add(logString);
    }

    /**
     * Get the observable list of the log
     *
     * @return the log observable list
     */
    public ObservableList<String> getUserLog() {
        return userLog;
    }
}
