package com.team44.runwayredeclarationapp.utility.xml;

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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * The handler to read and write HTML files
 */
public class XMLHandler {

    private static final Logger logger = LogManager.getLogger(XMLHandler.class);

    /**
     * The XStream handler
     */
    private final XStream xstream = new XStream(new StaxDriver());

    /**
     * The file to save to
     */
    private File stateFile = new File("." + File.separator + "state.xml");

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
     * The xml validator
     */
    private final XMLValidator xmlValidator = new XMLValidator();

    /**
     * The XSD validation file
     */
    private URL validationFile;

    /**
     * Create an XML handler
     */
    public XMLHandler() {
        logger.info("Setting up XML handler");

        // Set the validation schema
        try {
            this.validationFile = XMLValidator.class.getResource("/xml/validate.xsd");
        } catch (Exception e) {
            logger.error("Validation file could not be found: " + e.getMessage());
        }

        // XML aliases
        xstream.alias("parallelrunway", PRunway.class);
        xstream.alias("singlerunway", SRunway.class);
        xstream.alias("airports", AirportXMLObj[].class);
        xstream.alias("airport", AirportXMLObj.class);
        xstream.alias("obstacle", Obstacle.class);
        xstream.alias("root", XMLWrapper.class);

        // Omit fields for runway
        xstream.omitField(Runway.class, "phyId");
        xstream.omitField(Runway.class, "logicId1");
        xstream.omitField(Runway.class, "logicId2");

        // XStream properties
        xstream.addPermission(AnyTypePermission.ANY);

    }

    /**
     * Write the *state* file with the new list of airports and obstacles
     *
     * @param airports  list of airports
     * @param obstacles list of obstacles
     */
    public void saveToXML(Airport[] airports, Obstacle[] obstacles) {
        saveToXML(airports, obstacles, stateFile);
    }

    /**
     * Write the file with the new list of airports and obstacles
     *
     * @param airports  list of airports
     * @param obstacles list of obstacles
     * @param file      the file to save XML to
     */
    public void saveToXML(Airport[] airports, Obstacle[] obstacles, File file) {
        logger.info("Saving to XML");

        // File handling
        try {
            // Output
            fileOutputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);

            // Create the root wrapper
            var wrapper = new XMLWrapper(
                Arrays.stream(airports).map(AirportXMLObj::new).toArray(AirportXMLObj[]::new),
                obstacles);

            // Write to file
            xstream.toXML(wrapper, writer);

        } catch (FileNotFoundException e) {
            logger.error("File could not be found.");

        } catch (Exception e) {
            logger.error("I/O error in writing to file.");

        } finally {
            // Close the stream
            if (fileInputStream != null && reader != null) {
                try {
                    fileInputStream.close();
                    reader.close();
                    logger.info("I/O streams closed.");

                } catch (IOException e) {
                    logger.error("I/O error in closing the write stream.");
                }

            }
        }
    }

    /**
     * Read and parse the xml from the *state* file
     *
     * @return the parsed xml object
     */
    public XMLWrapper readStateXML() {
        try {
            return readXML(stateFile);
        } catch (SAXException e) {
            return null;
        }
    }


    /**
     * /** Read and parse the xml from the file
     *
     * @param file the file to read from
     * @return the parsed xml object
     * @throws SAXException when there is a parsing error
     */
    public XMLWrapper readXML(File file) throws SAXException {
        logger.info("Reading from XML");

        // File handling
        try {
            // Validate the xml file
            xmlValidator.validateWithSchema(validationFile, file);

            // Input
            fileInputStream = new FileInputStream(file);
            reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            // Read from file
            return ((XMLWrapper) xstream.fromXML(reader));

        } catch (FileNotFoundException e) {
            logger.error("XML file could not be found.");
            return null;

        } catch (SAXException e) {
            // Parsing error
            throw e;

        } catch (Exception e) {
            logger.error("Error while reading file: " + e.getMessage());
            return null;

        } finally {
            // Close the stream
            if (fileInputStream != null && reader != null) {
                try {
                    fileInputStream.close();
                    reader.close();
                    logger.info("I/O streams closed.");

                } catch (IOException e) {
                    logger.error("I/O error in closing the read stream.");
                }
            }
        }
    }

    /**
     * Clear the state by deleting the state file
     */
    public void clearState() {
        if (stateFile.delete()) {
            logger.info("State file was successfully deleted.");
        } else {
            logger.error("State file could not be deleted.");
        }
    }

    /**
     * Set a different directory for the state file
     *
     * @param stateFile the new file
     */
    public void setStateFile(File stateFile) {
        this.stateFile = stateFile;
    }
}
