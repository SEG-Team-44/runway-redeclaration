package com.team44.runwayredeclarationapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.team44.runwayredeclarationapp.TestConstants;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.ErrorObjectPair;
import com.team44.runwayredeclarationapp.model.Obstacle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test for handling the list of airports, runways and obstacles
 */
class DataControllerTest {

    private static final Logger logger = LogManager.getLogger(DataControllerTest.class);

    private DataController dataController;

    @BeforeEach
    void setUp() {
        logger.info("Running test (setting up mocks)");
        dataController = new DataController();
    }

    @AfterEach
    void tearDown() {
        logger.info("Test done");
    }

    @Nested
    @DisplayName("Adding/editing airport")
    class AirportTests {

        @DisplayName("Add airport with valid names")
        @ParameterizedTest(name = "{index} Airport with name \"{0}\" is valid")
        @ValueSource(strings = {"a", "1", "HGJUy", "G8JEw Th QoT",
            "Uo97IeCf0zrjH TTtkF7qRoweupcqsH",
            "C8Jk58S1tRXIcclRynyYXhmH62wp2aQ88ChaOyxRNEa7I",
            "48684379855363115832241 8396361071063553497991",
            "621689318546",
            "hmLVDNt5l1vkP9Gpc1MKRlPGBjB4Q9C4o35TLeU70025q5a27wnfn3L7hn5",
            "hmLVDNt5l1vkP9Gpc1MKRlPGBjB4Q9C 4o35TLeU70025q5a27wnfn3L7hn5"})
        void addAirport_Valid(String airportName) {
            ErrorObjectPair<Airport> airportErrorObjectPair = dataController.addAirport(
                airportName);

            var expectedAirport = new Airport(airportName);

            assertEquals(0, airportErrorObjectPair.getErrorsArray().length,
                "There should be no errors");
            assertEquals(airportName, airportErrorObjectPair.getObject().getName(),
                "Name of airport created is different");

            // Whitebox
            assertTrue(dataController.getAirportObservableList().contains(expectedAirport),
                "Expected airport not found in the observable list");
        }

        @DisplayName("Add airport with invalid names")
        @ParameterizedTest(name = "{index} Airport with name \"{0}\" is invalid")
        @ValueSource(strings = {
            "lNLKcwgzUKbrABEfmZwlW9xmv5lbAJwPLov78fYh8spIbRdQ1oZaM0bjyw1yv",
            "L8ZNx3K58SqA8dca7i8ZZdd3axQkW0mt4zypPiRRjIX1fbNkQ1uY83Ume6d87wXEbcKY2V",
            "NnydyLnIumRYOnBPFLsaIgzGHvWEsDZmZUQVewbrXOzxayDJNGfWSfsHeVsqTkAXW",
            "88638558545566609103177393489921514225706367818096383368941568526",
            "hmLVDNt5l1vkP9Gpc1MKRlPGBjB4Q9C 4o35TLeU70025q5a27wnfn3L7hn56"})
        @EmptySource
        void addAirport_Invalid(String airportName) {
            ErrorObjectPair<Airport> airportErrorObjectPair = dataController.addAirport(
                airportName);

            var expectedAirport = new Airport(airportName);

            assertTrue(airportErrorObjectPair.getErrorsArray().length > 0,
                "There should be errors");

            // Whitebox
            assertFalse(dataController.getAirportObservableList().contains(expectedAirport),
                "Airport should not have been added to observable list");
        }

        @DisplayName("Edit airport with valid names")
        @ParameterizedTest(name = "{index} Airport with name \"{0}\" is valid")
        @ValueSource(strings = {"a", "1", "HGJUy", "G8JEw Th QoT",
            "Uo97IeCf0zrjH TTtkF7qRoweupcqsH",
            "C8Jk58S1tRXIcclRynyYXhmH62wp2aQ88ChaOyxRNEa7I",
            "48684379855363115832241 8396361071063553497991",
            "621689318546",
            "hmLVDNt5l1vkP9Gpc1MKRlPGBjB4Q9C4o35TLeU70025q5a27wnfn3L7hn5",
            "hmLVDNt5l1vkP9Gpc1MKRlPGBjB4Q9C 4o35TLeU70025q5a27wnfn3L7hn5"})
        void editAirport_Valid(String newAirportName) {
            ErrorObjectPair<Airport> airportErrorObjectPair = dataController.addAirport(
                "Airport test");
            var airportObject = airportErrorObjectPair.getObject();

            var expectedAirport = new Airport(newAirportName);

            ErrorObjectPair<Airport> editErrorObjectPair = dataController.editAirport(airportObject,
                newAirportName);

            assertEquals(0, editErrorObjectPair.getErrorsArray().length,
                "There should be no errors");
            assertEquals(newAirportName, editErrorObjectPair.getObject().getName(),
                "Airport names do not match");

            // Whitebox
            assertTrue(dataController.getAirportObservableList().contains(expectedAirport),
                "Airport should be in observable list");

        }

        @DisplayName("Edit airport with invalid names")
        @ParameterizedTest(name = "{index} Airport with name \"{0}\" is invalid")
        @ValueSource(strings = {
            "lNLKcwgzUKbrABEfmZwlW9xmv5lbAJwPLov78fYh8spIbRdQ1oZaM0bjyw1yv",
            "L8ZNx3K58SqA8dca7i8ZZdd3axQkW0mt4zypPiRRjIX1fbNkQ1uY83Ume6d87wXEbcKY2V",
            "NnydyLnIumRYOnBPFLsaIgzGHvWEsDZmZUQVewbrXOzxayDJNGfWSfsHeVsqTkAXW",
            "88638558545566609103177393489921514225706367818096383368941568526",
            "hmLVDNt5l1vkP9Gpc1MKRlPGBjB4Q9C 4o35TLeU70025q5a27wnfn3L7hn56"})
        @EmptySource
        void editAirport_Invalid(String newAirportName) {
            ErrorObjectPair<Airport> airportErrorObjectPair = dataController.addAirport(
                "Airport test");
            var airportObject = airportErrorObjectPair.getObject();

            var expectedAirport = new Airport(newAirportName);

            ErrorObjectPair<Airport> editErrorObjectPair = dataController.editAirport(airportObject,
                newAirportName);

            assertTrue(editErrorObjectPair.getErrorsArray().length > 0,
                "There should be errors");

            // Whitebox
            assertFalse(dataController.getAirportObservableList().contains(expectedAirport),
                "Airport should not have been added to observable list");
        }
    }

    @Nested
    @DisplayName("Adding/editing runway")
    class RunwayTests {

        @DisplayName("Add runway - blackbox test")
        @Test
        void addRunway() {
            // Create new airport
            var airport = new Airport("Test airport");

            var expectedRunway = TestConstants.RUNWAY_09L_27R;
            var runwayErrorObjectPair = dataController.addRunway(airport, 'L', 'R', 9, 27,
                runwayParameters1);

            assertTrue(
                airport.getRunwayObservableList().contains(runwayErrorObjectPair.getObject()),
                "Observable list should contain the runway"); // White box
            assertFalse(runwayErrorObjectPair.hasErrors(),
                "There should be no errors. Error: " + runwayErrorObjectPair.getErrors());
            assertEquals(expectedRunway, runwayErrorObjectPair.getObject(), "Runways do not match");
        }

        @DisplayName("Edit runway - blackbox test")
        @Test
        void editRunway() {
            // Create new airport
            var airport = new Airport("Test airport");

            // Edit the runway
            var expectedRunway = TestConstants.RUNWAY_09R_27L;
            var addedRunway = dataController.addRunway(airport, 'L', 'R', 9, 27,
                runwayParameters1).getObject();
            var runwayErrorObjectPair = dataController.editRunway(addedRunway, airport, 'R', 'L',
                9, 27, runwayParameters2);

            assertTrue(
                airport.getRunwayObservableList().contains(runwayErrorObjectPair.getObject()),
                "Observable list should contain the edited runway"); // White box
            assertFalse(runwayErrorObjectPair.hasErrors(),
                "There should be no errors. Error: " + runwayErrorObjectPair.getErrors());
            assertEquals(expectedRunway, runwayErrorObjectPair.getObject(), "Runways do not match");
        }

        /**
         * Runway parameters for testing with
         */
        private static final double[] runwayParameters1 = new double[]{3902, 100, 60,
            100, 100, 240, 3902, 3902, 3902, 3595, 3884, 3962, 3884, 3884, 0, 306
        };
        private static final double[] runwayParameters2 = new double[]{3660, 100, 60,
            100, 100, 240, 3660, 3660, 3660, 3353, 3660, 3660, 3660, 3660, 0, 307
        };
    }

    @Nested
    @DisplayName("Adding/editing obstacle")
    class ObstacleTests {

        @DisplayName("Add obstacle with valid inputs")
        @ParameterizedTest(name = "{index} Obstacle ({0}) is valid")
        @CsvSource({
            "367143861552, 10",
            "367143861552, 499",
            "367143861552, 1",
            "367143861552, 0.01",
            "367143861552, 0.11",
            "q7w429yUbjNq, 12",
            "GpCbV0wL4g mJNgFhkZnaLIFFW, 1",
            "GpCbV0wL4g mJNgFhkZnaLIFFW, 25",
            "GpCbV0wL4g mJNgFhkZnaLIFFW, 499.12",
            "GpCbV0wL4g mJNgFhkZnaLIFFW, 499.99",
            "7CPfsSRs09i3G5 s2EJScqFS48Q1DMzfNgbOxf1RRFr2d QsPZ0, 0.1",
            "7CPfsSRs09i3G5 s2EJScqFS48Q1DMzfNgbOxf1RRFr2d QsPZ0, 1",
            "7CPfsSRs09i3G5 s2EJScqFS48Q1DMzfNgbOxf1RRFr2d QsPZ0, 50.2",
            "7CPfsSRs09i3G5 s2EJScqFS48Q1DMzfNgbOxf1RRFr2d QsPZ0, 499.9",
            "eg9kBlnqQmXriVcCM6HjTNsWAtdkxzomKdxJCB9QuCqXSL7ZIf48uBf3Kk1, 1",
            "eg9kBlnqQmXriVcCM6HjTNsWAtdkxzomKdxJCB9QuCqXSL7ZIf48uBf3Kk1, 499",
            "eg9kBlnqQmXriVcCM6HjTNsWAtdkxzomKdxJCB9QuCqXSL7ZIf48uBf3Kk1, 499.99"
        })
        void addObstacle_Valid(String obstacleName, double obstacleHeight) {
            ErrorObjectPair<Obstacle> obstacleErrorObjectPair = dataController.addObstacle(
                obstacleName, obstacleHeight);

            var expectedObstacle = new Obstacle(obstacleName, obstacleHeight);

            assertEquals(0, obstacleErrorObjectPair.getErrorsArray().length,
                "There should be no errors");
            assertEquals(expectedObstacle, obstacleErrorObjectPair.getObject(),
                "Obstacle created is different to expected");

            // Whitebox
            assertTrue(dataController.getObstacleObservableList().contains(expectedObstacle),
                "Obstacle should have been added to observable list");
        }

        @DisplayName("Add obstacle with invalid inputs")
        @ParameterizedTest(name = "{index} Obstacle ({0}) is invalid")
        @CsvSource({
            ", 1",
            ", -10",
            "QwwK4 fg JT5, 500.01",
            "QwwK4 fg JT5, 500.014",
            "KA5wOabajfvvYpn1lwYrQqPiAwMieYbl53, 25.666",
            "KA5wOabajfvvYpn1lwY rQqPiAwMieYbl53, 1000.01",
            "dzWxpu70ADaGPafjErEufUizvHkXYFFIh1k6QBj7kpJXZ, 499.999",
            "dzWxpu70ADaGPafjErEufUizv HkXYFFIh1k6QBj7kpJXZ, 0.0001",
            "218141662940801187877072671984474160982316557659515951143884, 501",
            "QzhFlIkp2dbdJEOS7yGUtwqZhqq1lsLoGxTjJZxuKOlBGWKcjyFsURsFAVaW, 1000",
            "iGdklCzRpU25mCPV4sIG3XWnHYgk 2Tza8WZjNokR9aa7IDYGOFl1eD9mr5, -1",
            "iGdklCzRpU25mCPV4sIG3XWnHYgk42Tza8WZjNokR9aa7IDYGOFl1eD9mr5, -0.01",
            "iGdklCzRpU25mCPV4sIG3XWnHYgk42Tza8WZjNokR9aa7IDYGOFl1eD9mr5, -0.0114",
        })
        void addObstacle_Invalid(String obstacleName, double obstacleHeight) {
            ErrorObjectPair<Obstacle> obstacleErrorObjectPair = dataController.addObstacle(
                obstacleName, obstacleHeight);
            var expectedObstacle = new Obstacle(obstacleName, obstacleHeight);

            assertTrue(obstacleErrorObjectPair.getErrorsArray().length > 0,
                "There should be errors");
            // Whitebox
            assertFalse(dataController.getObstacleObservableList().contains(expectedObstacle),
                "Obstacle should not have been added to observable list");
        }

        @DisplayName("Add obstacle - boundary testing for height")
        @Test
        void addObstacle_BoundaryTest() {
            ErrorObjectPair<Obstacle> obstacleErrorObjectPair;

            // Boundaries
            var bottomLowerBoundary = 0;
            var bottomHigherBoundary = 0.01;
            var topLowerBoundary = 499.99;
            var topHigherBoundary = 500;

            // Valid
            obstacleErrorObjectPair = dataController.addObstacle("Plane 1", bottomHigherBoundary);
            assertFalse(obstacleErrorObjectPair.hasErrors(),
                bottomHigherBoundary + " should be a valid obstacle height. Errors:"
                    + obstacleErrorObjectPair.getErrors());

            obstacleErrorObjectPair = dataController.addObstacle("Plane 1", topLowerBoundary);
            assertFalse(obstacleErrorObjectPair.hasErrors(),
                topLowerBoundary + " should be a valid obstacle height"
                    + obstacleErrorObjectPair.getErrors());

            // Invalid
            obstacleErrorObjectPair = dataController.addObstacle("Plane 1", bottomLowerBoundary);
            assertTrue(obstacleErrorObjectPair.hasErrors(),
                bottomLowerBoundary + " should be an invalid obstacle height");

            obstacleErrorObjectPair = dataController.addObstacle("Plane 1", topHigherBoundary);
            assertTrue(obstacleErrorObjectPair.hasErrors(),
                topHigherBoundary + " should be an invalid obstacle height");
        }

        @DisplayName("Edit obstacle with valid inputs")
        @ParameterizedTest(name = "{index} Obstacle ({0}) is valid")
        @CsvSource({
            "367143861552, 10",
            "367143861552, 499",
            "367143861552, 1",
            "367143861552, 0.01",
            "367143861552, 0.11",
            "q7w429yUbjNq, 12",
            "GpCbV0wL4g mJNgFhkZnaLIFFW, 1",
            "GpCbV0wL4g mJNgFhkZnaLIFFW, 25",
            "GpCbV0wL4g mJNgFhkZnaLIFFW, 499.12",
            "GpCbV0wL4g mJNgFhkZnaLIFFW, 499.99",
            "7CPfsSRs09i3G5 s2EJScqFS48Q1DMzfNgbOxf1RRFr2d QsPZ0, 0.1",
            "7CPfsSRs09i3G5 s2EJScqFS48Q1DMzfNgbOxf1RRFr2d QsPZ0, 1",
            "7CPfsSRs09i3G5 s2EJScqFS48Q1DMzfNgbOxf1RRFr2d QsPZ0, 50.2",
            "7CPfsSRs09i3G5 s2EJScqFS48Q1DMzfNgbOxf1RRFr2d QsPZ0, 499.9",
            "eg9kBlnqQmXriVcCM6HjTNsWAtdkxzomKdxJCB9QuCqXSL7ZIf48uBf3Kk1, 1",
            "eg9kBlnqQmXriVcCM6HjTNsWAtdkxzomKdxJCB9QuCqXSL7ZIf48uBf3Kk1, 499",
            "eg9kBlnqQmXriVcCM6HjTNsWAtdkxzomKdxJCB9QuCqXSL7ZIf48uBf3Kk1, 499.99"
        })
        void editObstacle_Valid(String obstacleName, double obstacleHeight) {
            ErrorObjectPair<Obstacle> airportErrorObjectPair = dataController.addObstacle(
                "Valid name", 12);
            var airportObject = airportErrorObjectPair.getObject();

            ErrorObjectPair<Obstacle> editErrorObjectPair = dataController.editObstacle(
                airportObject, obstacleName, obstacleHeight);

            var expectedObstacle = new Obstacle(obstacleName, obstacleHeight);

            assertEquals(0, editErrorObjectPair.getErrorsArray().length,
                "There should be no errors");
            assertEquals(expectedObstacle, editErrorObjectPair.getObject(),
                "Obstacle does not match");

            // Whitebox
            assertTrue(dataController.getObstacleObservableList().contains(expectedObstacle),
                "Obstacle should have been edited to observable list");
        }

        @DisplayName("Edit obstacle with invalid inputs")
        @ParameterizedTest(name = "{index} Obstacle ({0}) is invalid")
        @CsvSource({
            ", 1",
            ", -10",
            "QwwK4 fg JT5, 500.01",
            "QwwK4 fg JT5, 500.014",
            "KA5wOabajfvvYpn1lwYrQqPiAwMieYbl53, 25.666",
            "KA5wOabajfvvYpn1lwY rQqPiAwMieYbl53, 1000.01",
            "dzWxpu70ADaGPafjErEufUizvHkXYFFIh1k6QBj7kpJXZ, 499.999",
            "dzWxpu70ADaGPafjErEufUizv HkXYFFIh1k6QBj7kpJXZ, 0.0001",
            "218141662940801187877072671984474160982316557659515951143884, 501",
            "QzhFlIkp2dbdJEOS7yGUtwqZhqq1lsLoGxTjJZxuKOlBGWKcjyFsURsFAVaW, 1000",
            "iGdklCzRpU25mCPV4sIG3XWnHYgk 2Tza8WZjNokR9aa7IDYGOFl1eD9mr5, -1",
            "iGdklCzRpU25mCPV4sIG3XWnHYgk42Tza8WZjNokR9aa7IDYGOFl1eD9mr5, -0.01",
            "iGdklCzRpU25mCPV4sIG3XWnHYgk42Tza8WZjNokR9aa7IDYGOFl1eD9mr5, -0.0114",
        })
        void editObstacle_Invalid(String obstacleName, double obstacleHeight) {
            ErrorObjectPair<Obstacle> obstacleErrorObjectPair = dataController.addObstacle(
                "Valid name", 12);
            var obstacleObject = obstacleErrorObjectPair.getObject();
            var expectedObstacle = new Obstacle(obstacleName, obstacleHeight);

            ErrorObjectPair<Obstacle> editErrorObjectPair = dataController.editObstacle(
                obstacleObject, obstacleName, obstacleHeight);

            assertTrue(editErrorObjectPair.getErrorsArray().length > 0,
                "There should be errors");
            // Whitebox
            assertFalse(dataController.getObstacleObservableList().contains(expectedObstacle),
                "Obstacle should not have been edited to observable list");
        }

        @DisplayName("Edit obstacle - boundary testing for height")
        @Test
        void editObstacle_BoundaryTest() {
            ErrorObjectPair<Obstacle> obstacleErrorObjectPair, editedObstacleErrorObjectPair;

            // Boundaries
            var bottomLowerBoundary = 0;
            var bottomHigherBoundary = 0.01;
            var topLowerBoundary = 499.99;
            var topHigherBoundary = 500;

            // Valid
            obstacleErrorObjectPair = dataController.addObstacle("Plane 1", 10);
            editedObstacleErrorObjectPair = dataController.editObstacle(
                obstacleErrorObjectPair.getObject(), "Plane 1", bottomHigherBoundary);
            assertFalse(editedObstacleErrorObjectPair.hasErrors(),
                bottomHigherBoundary + " should be a valid obstacle height. Errors:"
                    + editedObstacleErrorObjectPair.getErrors());

            obstacleErrorObjectPair = dataController.addObstacle("Plane 1", 10);
            editedObstacleErrorObjectPair = dataController.editObstacle(
                obstacleErrorObjectPair.getObject(), "Plane 1", topLowerBoundary);
            assertFalse(editedObstacleErrorObjectPair.hasErrors(),
                topLowerBoundary + " should be a valid obstacle height"
                    + editedObstacleErrorObjectPair.getErrors());

            // Invalid
            obstacleErrorObjectPair = dataController.addObstacle("Plane 1", 10);
            editedObstacleErrorObjectPair = dataController.editObstacle(
                obstacleErrorObjectPair.getObject(), "Plane 1", bottomLowerBoundary);
            assertTrue(editedObstacleErrorObjectPair.hasErrors(),
                bottomLowerBoundary + " should be an invalid obstacle height");

            obstacleErrorObjectPair = dataController.addObstacle("Plane 1", 10);
            editedObstacleErrorObjectPair = dataController.editObstacle(
                obstacleErrorObjectPair.getObject(), "Plane 1", topHigherBoundary);
            assertTrue(editedObstacleErrorObjectPair.hasErrors(),
                topHigherBoundary + " should be an invalid obstacle height");
        }
    }
}
