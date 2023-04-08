package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.ErrorObjectPair;
import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.SRunway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
     * Add a new airport to the list of airports
     *
     * @param airportName the airport name
     * @return the pair containing the created airport and a list of errors
     */
    public ErrorObjectPair<Airport> addAirport(String airportName) {
        // Validate the airport inputs
        var validationErrors = ValidationController.validateAirport(airportName);

        // Create a new airport and add it to the list
        var newAirport = new Airport(airportName);
        airportObservableList.add(newAirport);

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

        // Replace current airport with new (modified) airport
        airportObservableList.set(index, newAirport);

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

        // Validate the runway inputs
        var validationErrors = ValidationController.validateRunway(
            pos1, pos2, degree1, degree2, parameters, airport);

        // Check there are no errors
        if (validationErrors.size() == 0) {
            runway.setDegree(degree1, degree2);
            runway.updateParameters(parameters);

            // todo:: update the observable list
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

        // Create the new obstacle and add it to the list
        var newObstacle = new Obstacle(obstacleName, obstacleHeight);
        obstacleObservableList.add(newObstacle);

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

        // Create the new obstacle and replace it with the current obstacle in list
        var newObstacle = new Obstacle(obstacleName, obstacleHeight);
        obstacleObservableList.set(index, newObstacle);

        return new ErrorObjectPair<>(newObstacle, validationErrors);
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
}
