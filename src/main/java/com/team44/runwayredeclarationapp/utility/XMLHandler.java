package com.team44.runwayredeclarationapp.utility;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.SRunway;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The handler to read and write HTML files
 */
public class XMLHandler {

    private static final Logger logger = LogManager.getLogger(XMLHandler.class);

    /**
     * The XStream handler
     */
    private XStream xstream = new XStream(new StaxDriver());

    /**
     * The file to save to
     */
    private File file = new File("." + File.separator + "state.xml");

    /**
     * The output/write stream
     */
    private FileOutputStream fileOutputStream;
    private Writer writer;

    /**
     * The input/read stream
     */
    private FileInputStream fileInputStream;
    private Reader reader;

    /**
     * Create an XML handler
     */
    public XMLHandler() {
        // File handling
        try {
            // Output
            fileOutputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);

            // Input
            fileInputStream = new FileInputStream(file);
            reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            logger.error("File could not be found.");
        }

        // XML aliases
        xstream.alias("parallelrunway", PRunway.class);
        xstream.alias("singlerunway", SRunway.class);
        xstream.alias("airport", AirportXMLObj.class);
        xstream.alias("obstacle", Obstacle.class);
        xstream.alias("root", XMLWrapper.class);

        // XStream properties
        xstream.addPermission(AnyTypePermission.ANY);

    }

    /**
     * Overwrite the file with the new list of airports and obstacles
     *
     * @param airports  list of airports
     * @param obstacles list of obstacles
     */
    public void saveToXML(Airport[] airports, Obstacle[] obstacles) {
        // Create the root wrapper
        var wrapper = new XMLWrapper(
            Arrays.stream(airports).map(AirportXMLObj::new).toArray(AirportXMLObj[]::new),
            obstacles);

        // Write to file
        xstream.toXML(wrapper, writer);
    }

    /**
     * Read and parse the xml from the file
     *
     * @return the parsed xml object
     */
    public XMLWrapper readXML() {
        try {
            // Read from file
            return ((XMLWrapper) xstream.fromXML(reader));
        } catch (Exception e) {
            logger.error("The file doesn't exist or is empty.");
            return null;
        }
    }
}

/**
 * The airport object that is used in the XML, that uses a regular array of runways than an
 * observable list
 */
class AirportXMLObj {

    private final Runway[] runways;

    /**
     * Create an airport XML object with a given airport object
     *
     * @param airport the airport object
     */
    public AirportXMLObj(Airport airport) {
        runways = airport.getRunways().toArray(Runway[]::new);
    }

    /**
     * Get the array of runways
     *
     * @return the array of runways
     */
    public Runway[] getRunways() {
        return runways;
    }

    /**
     * Convert the airport xml object into a regular airport object
     *
     * @return the airport object
     */
    public Airport toAirport() {
        var airport = new Airport();
        // Set the list of runways to the observable list
        airport.getRunwayObservableList().setAll(runways);

        return airport;
    }
}

/**
 * The root XML wrapper, used to store both the list of airports and obstacles
 */
class XMLWrapper {

    /**
     * List of airports
     */
    private final AirportXMLObj[] airportXMLObjs;
    /**
     * List of obstacles
     */
    private final Obstacle[] obstacles;

    /**
     * Create the root XML wrapper
     *
     * @param airportXMLObjs the list of airports
     * @param obstacles      the list of obstacles
     */
    public XMLWrapper(AirportXMLObj[] airportXMLObjs, Obstacle[] obstacles) {
        this.airportXMLObjs = airportXMLObjs;
        this.obstacles = obstacles;
    }

    /**
     * Get the list of airport XML objects
     *
     * @return the list of airport xml objects
     */
    public AirportXMLObj[] getAirportXMLObjs() {
        return airportXMLObjs;
    }

    /**
     * Get the list of obstacles
     *
     * @return the list of obstacles
     */
    public Obstacle[] getObstacles() {
        return obstacles;
    }

    /**
     * Get the list of xml airport objects that have been converted to regular airport objects
     *
     * @return the list of airport objects
     */
    public Airport[] getAirports() {
        var airportsArray = new ArrayList<Airport>();

        // Convert each of the airport xml objects to regular objects
        for (AirportXMLObj airport : airportXMLObjs) {
            airportsArray.add(airport.toAirport());
        }

        // Return the list of airports
        return airportsArray.toArray(Airport[]::new);
    }
}