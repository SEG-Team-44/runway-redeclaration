package com.team44.runwayredeclarationapp.utility.xml;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.model.PRunway;
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
import java.nio.charset.StandardCharsets;
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
        logger.info("Setting up XML handler");

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
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                    reader.close();

                } catch (IOException e) {
                    logger.error("I/O error in closing the write stream.");
                }

            }
        }
    }

    /**
     * Read and parse the xml from the file
     *
     * @return the parsed xml object
     */
    public XMLWrapper readXML() {
        logger.info("Reading from XML");

        // File handling
        try {
            // Input
            fileInputStream = new FileInputStream(file);
            reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            // Read from file
            return ((XMLWrapper) xstream.fromXML(reader));

        } catch (FileNotFoundException e) {
            logger.error("File could not be found.");
            return null;

        } catch (Exception e) {
            logger.error("The file may be empty.");
            return null;

        } finally {
            // Close the stream
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                    reader.close();

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
        if (file.delete()) {
            logger.info("State file was successfully deleted.");
        } else {
            logger.error("State file could not be deleted.");
        }
    }
}

