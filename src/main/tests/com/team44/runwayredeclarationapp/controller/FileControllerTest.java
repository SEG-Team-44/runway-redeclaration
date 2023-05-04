package com.team44.runwayredeclarationapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.team44.runwayredeclarationapp.TestConstants;
import com.team44.runwayredeclarationapp.event.AlertListener;
import com.team44.runwayredeclarationapp.event.DataLoadedListener;
import com.team44.runwayredeclarationapp.event.ErrorListListener;
import com.team44.runwayredeclarationapp.event.FileUploadSuccessfulListener;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Obstacle;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.stubbing.Answer;
import org.xmlunit.assertj3.XmlAssert;
import org.xmlunit.builder.Input;

/**
 * Tests for validating importing and exported xml files, including the state xml file
 */
class FileControllerTest {

    private static final Logger logger = LogManager.getLogger(FileControllerTest.class);

    private FileController fileController;

    private DataLoadedListener dataSetListener;
    private DataLoadedListener dataAddListener;

    private AlertListener errorListener;
    private ErrorListListener multipleErrorsListener;
    private FileUploadSuccessfulListener fileUploadSuccessfulListener;

    @BeforeEach
    void setUp() {
        logger.info("Running test (setting up mocks)");
        fileController = new FileController();

        // Mocks
        dataSetListener = mock(DataLoadedListener.class);
        dataAddListener = mock(DataLoadedListener.class);
        errorListener = mock(AlertListener.class);
        multipleErrorsListener = mock(ErrorListListener.class);
        fileUploadSuccessfulListener = mock(FileUploadSuccessfulListener.class);

        // Set the listeners
        fileController.setDataSetListener(dataSetListener);
        fileController.setDataAddListener(dataAddListener);
        fileController.setErrorListener(errorListener);
        fileController.setMultipleErrorsListener(multipleErrorsListener);
        fileController.setFileUploadSuccessfulListener(fileUploadSuccessfulListener);
    }

    @AfterEach
    void tearDown() {
        logger.info("Test done");
    }

    @DisplayName("Upload valid XML file")
    @Test
    void uploadXMLFile_Valid() {
        // Create the doAnswer's for the listener
        testDataLoadedListener(dataAddListener, getImport1Airports(), getImport2Obstacles());
        testDataLoadedListener(dataSetListener, getImport1Airports(), getImport2Obstacles());

        // When false, dataAddListener is called, else it's dataSetListener
        // Data add listener
        fileController.uploadXMLFile(
            Paths.get("src", "main", "tests", "resources", "xml", "valid-import-1.xml").toFile(),
            false);

        // Data set listener
        fileController.uploadXMLFile(
            Paths.get("src", "main", "tests", "resources", "xml", "valid-import-1.xml").toFile(),
            true);

        // Verify the listeners that should be called
        verify(dataAddListener).load(any(Airport[].class), any(Obstacle[].class));
        verify(dataSetListener).load(any(Airport[].class), any(Obstacle[].class));
        verify(fileUploadSuccessfulListener, times(2)).uploadSuccessful();

        // Verify the error listeners that should NOT be called
        verify(errorListener, never()).alert(anyString(), anyString(), anyString());
        verify(multipleErrorsListener, never()).alert(any(String[].class));
    }

    @DisplayName("Upload invalid XML file")
    @Test
    void uploadXMLFile_InvalidXML() {
        // Upload XML in wrong format (does not match schema)
        fileController.uploadXMLFile(
            Paths.get("src", "main", "tests", "resources", "xml", "invalid-import-1.xml").toFile(),
            true);

        // Verify the listeners that should NOT be called
        verify(dataSetListener, never()).load(any(Airport[].class), any(Obstacle[].class));
        verify(fileUploadSuccessfulListener, never()).uploadSuccessful();
        verify(multipleErrorsListener, never()).alert(any(String[].class));

        // Verify the error listeners that SHOULD be called
        verify(errorListener).alert(anyString(), anyString(), anyString());
    }

    @DisplayName("Upload XML file with invalid data")
    @Test
    void uploadXMLFile_InvalidData() {
        // Upload test XML file with invalid data
        fileController.uploadXMLFile(
            Paths.get("src", "main", "tests", "resources", "xml", "invalid-import-2.xml").toFile(),
            true);

        // Verify the listeners that should NOT be called
        verify(dataSetListener, never()).load(any(Airport[].class), any(Obstacle[].class));
        verify(fileUploadSuccessfulListener, never()).uploadSuccessful();
        verify(errorListener, never()).alert(anyString(), anyString(), anyString());

        // Verify the error listeners that SHOULD be called
        verify(multipleErrorsListener).alert(any(String[].class));
    }

    @DisplayName("Export XML file")
    @Test
    void exportXMLFile() throws IOException {
        // Create temp file to export to
        var tempFile = createTempXMLFile();

        // Export with the file controller
        fileController.exportXMLFile(getImport1Airports(), getImport2Obstacles(), tempFile);

        // Specify actual and expected files
        var expectedFile = Paths.get("src", "main", "tests", "resources", "xml",
            "valid-import-1.xml").toFile();

        // Assert the 2 xml files
        assertXML(tempFile, expectedFile, true);
    }

    @DisplayName("Upload state XML file with valid data and matching XML schema")
    @Test
    void loadInitialState_ValidXML() throws IOException {
        // Input state file
        var inputStateFile = Paths.get("src", "main", "tests", "resources", "xml",
            "valid-import-1.xml").toFile();

        // Set the temporary file to save the state to
        var tempFile = createTempXMLFile("tempStateXML", inputStateFile);
        fileController.setStateFileDirectory(tempFile);

        // Create the doAnswer's for the listener
        testDataLoadedListener(dataSetListener, getImport1Airports(), getImport2Obstacles());

        // Data set listener
        fileController.loadInitialState();

        // Verify the data set listener that should be called
        verify(dataSetListener).load(any(Airport[].class), any(Obstacle[].class));
    }

    @DisplayName("Upload state XML file with invalid data and XML")
    @ParameterizedTest(name = "{index} File \"{0}\" is not valid and should import predefined lists")
    @ValueSource(strings = {"invalid-import-1.xml", "invalid-import-2.xml"})
    void loadInitialState_Invalid(String fileNameToImport) throws IOException {
        // Input state file
        var inputStateFile = Paths.get("src", "main", "tests", "resources", "xml",
            fileNameToImport).toFile();

        // Set the temporary file to save the state to
        var tempFile = createTempXMLFile("tempStateXML", inputStateFile);
        fileController.setStateFileDirectory(tempFile);

        // Check if invalid import leads to the predefined airports and obstacles being automatically added
        // Create the doAnswer's for the listener
        testDataLoadedListener(dataSetListener, FileController.getPredefinedAirports(),
            FileController.getPredefinedObstacles());

        // Data set listener
        fileController.loadInitialState();

        // Verify the data set listener that should be called
        verify(dataSetListener).load(any(Airport[].class), any(Obstacle[].class));
    }

    @DisplayName("Save state to XML file")
    @Test
    void setState() throws IOException {
        // Create temp file to export the state file to
        var tempFile = createTempXMLFile("tempStateXML");

        // Set the temp file to save the state to
        fileController.setStateFileDirectory(tempFile);

        // Export the state file
        fileController.setState(getImport1Airports(), getImport2Obstacles());

        // Specify actual and expected files
        var expectedFile = Paths.get("src", "main", "tests", "resources", "xml",
            "valid-import-1.xml").toFile();

        // Assert the 2 xml files
        assertXML(tempFile, expectedFile, true);
    }

    @Test
    void resetState() throws IOException {
        // Input state file
        var inputStateFile = Paths.get("src", "main", "tests", "resources", "xml",
            "invalid-import-1.xml").toFile();

        // Set the temporary file to save the state to
        var tempFile = createTempXMLFile("tempStateXML", inputStateFile);
        fileController.setStateFileDirectory(tempFile);

        // When reset, the predefined lists should be automatically set
        // Create the doAnswer's for the listener
        testDataLoadedListener(dataSetListener, FileController.getPredefinedAirports(),
            FileController.getPredefinedObstacles());

        // Reset the state
        fileController.resetState();

        // Verify the data set listener that should be called
        verify(dataSetListener).load(any(Airport[].class), any(Obstacle[].class));
    }


    /**
     * Get the list of airports that match the list in valid-import1.xml
     *
     * @return the list of airports
     */
    private static Airport[] getImport1Airports() {
        // Airports that match valid-import-1.xml
        var airport1 = new Airport("Airport example 1");
        var airport2 = new Airport("Airport example 2");
        airport1.addRunway(TestConstants.RUNWAY_09L_27R);
        airport1.addRunway(TestConstants.RUNWAY_09R_27L);
        airport1.addRunway(TestConstants.RUNWAY_01_19);
        airport2.addRunway(TestConstants.RUNWAY_02C_20C);

        return new Airport[]{airport1, airport2};
    }

    /**
     * Get the list of obstacles that match the list in valid-import1.xml
     *
     * @return the list of obstacles
     */
    private static Obstacle[] getImport2Obstacles() {
        // Obstacles that match valid-import-1.xml
        var obstacle1 = new Obstacle("Obstacle example 1", 12);
        var obstacle2 = new Obstacle("Obstacle example 2", 16);
        var obstacle3 = new Obstacle("Obstacle example 3", 17);

        return new Obstacle[]{obstacle1, obstacle2, obstacle3};
    }

    /**
     * Create a temporary XML file to save and read from
     *
     * @return the temporary file object
     * @throws IOException when there is an input output error creating the file
     */
    private File createTempXMLFile() throws IOException {
        return createTempXMLFile("tempXML");
    }

    /**
     * Create a temporary XML file to save and read from
     *
     * @param prefix the prefix of the name of the temp file
     * @return the temporary file object
     * @throws IOException when there is an input output error creating the file
     */
    private File createTempXMLFile(String prefix) throws IOException {
        // Create temp file to export to
        var tempFile = Files.createTempFile(prefix, ".xml");
        tempFile.toFile().deleteOnExit();

        logger.debug("XML file saved to temp file: " + tempFile.toFile().getAbsolutePath());
        return tempFile.toFile();
    }

    /**
     * Create a temporary XML file to save and read from
     *
     * @param prefix          the prefix of the name of the temp file
     * @param existingXMLFile the existing XML file to copy its contents from
     * @return the temporary file object
     * @throws IOException when there is an input output error creating the file
     */
    private File createTempXMLFile(String prefix, File existingXMLFile) throws IOException {
        // Create reader for existing xml file
        FileReader fileReader = new FileReader(existingXMLFile);

        // Create writer for temporary xml file
        var tempFile = createTempXMLFile(prefix);
        FileWriter fileWriter = new FileWriter(tempFile);

        // Copy data from existingXMLFile to temporary file
        int i;
        while ((i = fileReader.read()) != -1) {
            fileWriter.write(i);
        }

        // Close the streams
        fileWriter.close();
        fileReader.close();

        // Return the file
        return tempFile;
    }

    /**
     * Assert equal xml files
     *
     * @param actualXMLFile   the actual xml file
     * @param expectedXMLFile the expected xml file
     * @param areIdentical    whether to check if the files are identical (false = check not
     *                        identical)
     */
    private void assertXML(File actualXMLFile, File expectedXMLFile, boolean areIdentical) {
        logger.info("Comparing if xml files are " + (areIdentical ? "identical" : "not identical"));

        var xmlAssert = XmlAssert.assertThat(Input.fromFile(actualXMLFile).build()).and(
                Input.fromFile(expectedXMLFile).build()).ignoreComments()
            .ignoreWhitespace()
            .ignoreElementContentWhitespace()
            .ignoreChildNodesOrder()
            .normalizeWhitespace();

        if (areIdentical) {
            xmlAssert.areIdentical();
        } else {
            xmlAssert.areNotIdentical();
        }
    }

    /**
     * Test the data loaded listener interface (used for dataSetListener and dataAddListener)
     *
     * @param callbackListener  the listener that takes in list of airports and obstacles
     * @param expectedAirports  the expected list of airports
     * @param expectedObstacles the expected list of obstacles
     */
    private void testDataLoadedListener(DataLoadedListener callbackListener,
        Airport[] expectedAirports,
        Obstacle[] expectedObstacles) {
        doAnswer((Answer<Void>) invocation -> {
            // Get the actual list of airports and obstacles
            Airport[] airports = invocation.getArgument(0);
            Obstacle[] obstacles = invocation.getArgument(1);

            // Assert if the list lengths are equal so no index exception
            assertEquals(airports.length, expectedAirports.length,
                "List of airports does not match");
            assertEquals(obstacles.length, expectedObstacles.length,
                "List of obstacles does not match");

            // Assert the actual lists to the expected lists
            for (int i = 0; i < airports.length; i++) {
                var airport = airports[i];
                var expectedAirport = expectedAirports[i];
                assertEquals(airport, expectedAirport,
                    "Actual airport with name \"" + airport.getName()
                        + "\" does not match expected airport with name \""
                        + expectedAirport.getName() + "\"");
            }
            for (int i = 0; i < obstacles.length; i++) {
                var obstacle = obstacles[i];
                var expectedObstacle = expectedObstacles[i];
                assertEquals(obstacle, expectedObstacle,
                    "Actual obstacle with name \"" + obstacle.getObstName()
                        + "\" does not match expected obstacle with name \""
                        + expectedObstacle.getObstName() + "\"");
            }

            return null;
        }).when(callbackListener)
            .load(any(Airport[].class), any(Obstacle[].class));
    }
}
