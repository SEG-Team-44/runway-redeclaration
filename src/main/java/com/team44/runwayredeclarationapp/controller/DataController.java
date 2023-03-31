package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.event.DataLoadedListener;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.utility.xml.XMLHandler;
import com.team44.runwayredeclarationapp.utility.xml.XMLWrapper;

/**
 * The controller responsible for handling the storing of user data
 */
public class DataController {

    /**
     * The XML handler to read and write with
     */
    private XMLHandler xmlHandler = new XMLHandler(); // todo - change this to handle import/exports too
    /**
     * The initial xml state, if any
     */
    private XMLWrapper initialState;

    /**
     * Listener to call once the data has been retrieved
     */
    private DataLoadedListener dataLoadedListener;

    /**
     * Create a data controller to store and retrieve data
     */
    public DataController() {
    }


    /**
     * Load the initial state to the gui by calling the listener
     */
    public void loadInitialState() {
        initialState = xmlHandler.readXML();

        // Write the pre-defined state if obstacle is empty
        if (initialState == null) {
            savePredefinedValues();
        } else {
            callListener();
        }
    }

    /**
     * Save the pre-defined values into the state (overwrites)
     */
    private void savePredefinedValues() {
        xmlHandler.saveToXML(getPredefinedAirports(), getPredefinedObstacles());
        initialState = xmlHandler.readXML();

        // Call the listener to update
        callListener();
    }

    /**
     * Call the listener to update the gui
     */
    private void callListener() {
        // Don't call the listener if it hasn't been set yet
        if (dataLoadedListener == null) {
            return;
        }

        dataLoadedListener.load(getInitialAirports(), getInitialObstacles());
    }

    /**
     * Get the list of obstacles initially stored in state
     *
     * @return the initial list of obstacles
     */
    private Obstacle[] getInitialObstacles() {
        // If something went wrong reading the file, just return the predefined obstacles
        return initialState != null ? initialState.getObstacles() : getPredefinedObstacles();
    }

    /**
     * Get the list of airports initially stored in state
     *
     * @return the initial list of airports
     */
    private Airport[] getInitialAirports() {
        // If something went wrong reading the file, just return the predefined airports
        return initialState != null ? initialState.getAirports() : getPredefinedAirports();
    }

    /**
     * Set the current state of program's data
     *
     * @param airports  the list of airports
     * @param obstacles the list of obstacles
     */
    public void setState(Airport[] airports, Obstacle[] obstacles) {
        xmlHandler.saveToXML(airports, obstacles);
    }

    /**
     * Reset the data in the state
     */
    public void resetState() {
        savePredefinedValues();
    }

    /**
     * Set the listener to be called when the data has been retrieved
     *
     * @param dataLoadedListener the listener
     */
    public void setDataLoadedListener(
        DataLoadedListener dataLoadedListener) {
        this.dataLoadedListener = dataLoadedListener;
    }

    /**
     * Get a list of predefined airports
     *
     * @return the list of predefined airports
     */
    public static Airport[] getPredefinedAirports() {
        var airports = new Airport[]{new Airport("Scenarios")};
        airports[0].addRunway(new PRunway(9, 27, 'L', 'R', new double[]{
            3902,//runwayL
            100,//runwayW
            60,//stripL
            100,//stripW
            100,//clearwayW
            240, //resaL
            3902,//tora1
            3902,//toda1
            3902,//asda1
            3595,//lda1
            3884,//tora2
            3962,//toda2
            3884,//asda2
            3884,//lda2
            0,//disThresh1
            306//disThresh2
        }));
        airports[0].addRunway(new PRunway(9, 27, 'R', 'L', new double[]{
            3660,//runwayL
            100,//runwayW
            60,//stripL
            100,//stripW
            100,//clearwayW
            240, //resaL
            3660,//tora1
            3660,//toda1
            3660,//asda1
            3353,//lda1
            3660,//tora2
            3660,//toda2
            3660,//asda2
            3660,//lda2
            0,//disThresh1
            307//disThresh2
        }));

        return airports;
    }


    /**
     * Get a list of predefined obstacles
     *
     * @return the list of predefined obstacles
     */
    public static Obstacle[] getPredefinedObstacles() {
        return new Obstacle[]{
            new Obstacle("Airbus A319", 12),
            new Obstacle("Airbus A330", 16),
            new Obstacle("Airbus A340", 17),
            new Obstacle("Airbus A380", 24),
            new Obstacle("Boeing 737 MAX", 13),
            new Obstacle("Boeing 747", 19),
            new Obstacle("Boeing 757", 14),
            new Obstacle("Boeing 767", 17),
            new Obstacle("Boeing 777", 18),
            new Obstacle("Boeing 787 Dreamliner", 17),
            new Obstacle("Cessna 172", 2),
            new Obstacle("Gulfstream G650", 7),
            new Obstacle("Embraer E145", 6)
        };
    }
}
