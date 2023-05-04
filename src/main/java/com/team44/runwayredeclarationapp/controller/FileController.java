package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.event.AlertListener;
import com.team44.runwayredeclarationapp.event.DataLoadedListener;
import com.team44.runwayredeclarationapp.event.ErrorListListener;
import com.team44.runwayredeclarationapp.event.FileUploadSuccessfulListener;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.utility.xml.XMLHandler;
import com.team44.runwayredeclarationapp.utility.xml.XMLWrapper;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorAlert;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.xml.sax.SAXException;

/**
 * The controller responsible for handling the storing of user data
 */
public class FileController {

    /**
     * The XML handler to read and write with
     */
    private final XMLHandler xmlHandler = new XMLHandler();
    /**
     * The initial xml state, if any
     */
    private XMLWrapper initialState;

    /**
     * Listener to call to set the list of airports and runways to the gui
     */
    private DataLoadedListener dataSetListener;
    /**
     * Listener to call to add the list of airports and runways to the existing lists in the gui
     */
    private DataLoadedListener dataAddListener;
    /**
     * Listener to call to show an error on the gui
     */
    private AlertListener errorListener;
    /**
     * Listener to call to show a list of error on the gui
     */
    private ErrorListListener multipleErrorsListener;
    /**
     * Listener to call when an XML file has been successfully imported
     */
    private FileUploadSuccessfulListener fileUploadSuccessfulListener;

    /**
     * Create a data controller to store and retrieve data
     */
    public FileController() {
    }

    /**
     * Upload data from an XML file to the program
     *
     * @param file  the xml file
     * @param reset whether the reset the existing data
     */
    public void uploadXMLFile(File file, Boolean reset) {
        XMLWrapper uploadedData;
        try {
            // Parse the XML file
            uploadedData = xmlHandler.readXML(file);

            // Check for error
            if (uploadedData == null) {
                ErrorAlert errorAlert = new ErrorAlert("File could not be found!",
                    "There seems to be an unexpected error.", "Error: " + file.getName() +
                    " could not be found/read.");
                errorAlert.show();
                return;
            }

            var airports = uploadedData.getAirports();
            var obstacles = uploadedData.getObstacles();

            // Validate the data
            var errors = validateUploadedData(airports, obstacles);

            // Check if there are validation errors
            if (!errors.isEmpty()) {
                // Remove duplicate errors
                List<String> cleanErrors = new ArrayList<>(new HashSet<>(errors));

                if (multipleErrorsListener != null) {
                    multipleErrorsListener.alert(cleanErrors.toArray(String[]::new));
                }

                return;
            }

            // Call the listener to update the GUI
            if (reset) {
                callSetListener(airports, obstacles);
            } else {
                callAddListener(airports, obstacles);
            }

            // Call the listener for upload success
            if (fileUploadSuccessfulListener != null) {
                fileUploadSuccessfulListener.uploadSuccessful();
            }

        } catch (SAXException e) {
            // Parsing error
            if (errorListener != null) {
                errorListener.alert("XML parsing error", "Uploaded XML does not match schema!",
                    "Please ensure that the uploaded XML file matches the schema specified.\n\nError:\n"
                        + e.getMessage().replace("cvc-complex-type.2.4.a: ", ""));
            }
        } catch (Exception e) {
            ErrorAlert errorAlert = new ErrorAlert("Unexpected Error",
                "There seems to be an unexpected error.", "Error: " + e.getMessage());
            errorAlert.show();
        }

    }

    /**
     * Export data from program into an XML file
     *
     * @param file the file to save to
     */
    public void exportXMLFile(Airport[] airports, Obstacle[] obstacles, File file) {
        xmlHandler.saveToXML(airports, obstacles, file);
    }

    /**
     * Load the initial state to the gui by calling the listener
     */
    public void loadInitialState() {
        initialState = xmlHandler.readStateXML();

        // Write the pre-defined state if error in parsing
        if (initialState == null) {
            savePredefinedValues();
            return;
        }

        // Validate the data
        var errors = validateUploadedData(initialState.getAirports(), initialState.getObstacles());

        // Write the pre-defined state if invalid data
        if (!errors.isEmpty()) {
            savePredefinedValues();
        } else {
            callSetListener();
        }
    }

    /**
     * Save the pre-defined values into the state (overwrites)
     */
    private void savePredefinedValues() {
        xmlHandler.saveToXML(getPredefinedAirports(), getPredefinedObstacles());
        initialState = xmlHandler.readStateXML();

        // Call the listener to update
        callSetListener();
    }

    /**
     * Call the listener to update the gui by adding the list of airports and obstacles to the
     * existing lists
     *
     * @param airports  list of airports to add
     * @param obstacles list of obstacles to add
     */
    private void callAddListener(Airport[] airports, Obstacle[] obstacles) {
        // Don't call the listener if it hasn't been set yet
        if (dataAddListener == null) {
            return;
        }

        dataAddListener.load(airports, obstacles);
    }

    /**
     * Call the listener to update the gui by setting the list of airports and obstacles
     */
    private void callSetListener() {
        callSetListener(getInitialAirports(), getInitialObstacles());
    }

    /**
     * Call the listener to update the gui by setting the list of airports and obstacles
     *
     * @param airports  list of airports to set
     * @param obstacles list of obstacles to set
     */
    private void callSetListener(Airport[] airports, Obstacle[] obstacles) {
        // Don't call the listener if it hasn't been set yet
        if (dataSetListener == null) {
            return;
        }

        dataSetListener.load(airports, obstacles);
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
     * Set the listener to be called to set the list of airports and obstacles in the gui
     *
     * @param dataSetListener the listener
     */
    public void setDataSetListener(
        DataLoadedListener dataSetListener) {
        this.dataSetListener = dataSetListener;
    }

    /**
     * Set the listener to be called to add list of airports and obstacles to the existing lists in
     * the gui
     *
     * @param dataAddListener the listener
     */
    public void setDataAddListener(
        DataLoadedListener dataAddListener) {
        this.dataAddListener = dataAddListener;
    }

    /**
     * Set the listener to be called to show an error in the gui
     *
     * @param errorListener the listener
     */
    public void setErrorListener(AlertListener errorListener) {
        this.errorListener = errorListener;
    }

    /**
     * Set the listener to be called to show a list of errors in the gui
     *
     * @param multipleErrorsListener the listener
     */
    public void setMultipleErrorsListener(ErrorListListener multipleErrorsListener) {
        this.multipleErrorsListener = multipleErrorsListener;
    }

    /**
     * Set the listener to be called when an XML file has been successfully imported/uploaded
     *
     * @param fileUploadSuccessfulListener the listener
     */
    public void setFileUploadSuccessfulListener(
        FileUploadSuccessfulListener fileUploadSuccessfulListener) {
        this.fileUploadSuccessfulListener = fileUploadSuccessfulListener;
    }

    /**
     * Get a list of predefined airports
     *
     * @return the list of predefined airports
     */
    public static Airport[] getPredefinedAirports() {
        var airports = new Airport[]{new Airport("Heathrow (LHR)")};
        airports[0].addRunway(new PRunway(9, 27, 'L', 'R', new double[]{
            3901,//runwayL
            100,//runwayW
            60,//stripL
            100,//stripW
            100,//clearwayW
            240, //resaL
            3901,//tora1
            3901,//toda1
            3901,//asda1
            3592,//lda1
            3882,//tora2
            3960,//toda2
            3882,//asda2
            3882,//lda2
            0,//disThresh1
            309//disThresh2
        }));
        airports[0].addRunway(new PRunway(9, 27, 'R', 'L', new double[]{
            3658,//runwayL
            100,//runwayW
            60,//stripL
            100,//stripW
            100,//clearwayW
            240, //resaL
            3658,//tora1
            3658,//toda1
            3658,//asda1
            3350,//lda1
            3658,//tora2
            3658,//toda2
            3658,//asda2
            3658,//lda2
            0,//disThresh1
            308//disThresh2
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

    /**
     * Validate the list of airports and obstacles uploaded
     *
     * @param airports  list of airports uploaded
     * @param obstacles list of obstacles uploaded
     * @return the list of errors
     */
    private List<String> validateUploadedData(Airport[] airports, Obstacle[] obstacles) {
        // List of validation errors
        List<String> errors = new ArrayList<>();

        // Validate the data in the file
        for (Airport airport : airports) {
            // Validate the airport name
            errors.addAll(ValidationController.validateAirport(airport.getName()));

            // Validate the runways
            for (Runway runway : airport.getRunways()) {
                var isParallel = runway instanceof PRunway;
                var pos1 = isParallel ? ((PRunway) runway).getPos1() : null;
                var pos2 = isParallel ? ((PRunway) runway).getPos2() : null;

                // Validate
                var validationErrors = ValidationController.validateRunway(pos1, pos2,
                    runway.getDegree1(), runway.getDegree2(), runway.getParameters(), airport,
                    true);

                errors.addAll(validationErrors);
            }
        }

        // Validate the list of obstacles
        for (Obstacle obstacle : obstacles) {
            errors.addAll(ValidationController.validateObstacle(obstacle.getObstName(),
                obstacle.getHeight()));
        }

        return errors;
    }

    /**
     * Set a new directory (file object) to save the state to
     *
     * @param stateFile the new state file
     */
    public void setStateFileDirectory(File stateFile) {
        xmlHandler.setStateFile(stateFile);
    }
}
