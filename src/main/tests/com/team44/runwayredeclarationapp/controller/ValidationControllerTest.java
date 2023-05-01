package com.team44.runwayredeclarationapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.team44.runwayredeclarationapp.model.Airport;
import java.util.List;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for validating airport, runway and obstacle inputs
 */
class ValidationControllerTest {

    private static final Logger logger = LogManager.getLogger(ValidationController.class);

    @BeforeEach
    void setUp() {
        logger.info("Running test");
    }

    @AfterEach
    void tearDown() {
        logger.info("Test done");
    }

    @DisplayName("Valid airport inputs")
    @ParameterizedTest(name = "{index} Airport name \"{0}\" is valid")
    @ValueSource(strings = {"a", "1", "HGJUy", "G8JEw Th QoT", "Uo97IeCf0zrjH TTtkF7qRoweupcqsH",
        "C8Jk58S1tRXIcclRynyYXhmH62wp2aQ88ChaOyxRNEa7I",
        "48684379855363115832241 8396361071063553497991",
        "621689318546",
        "hmLVDNt5l1vkP9Gpc1MKRlPGBjB4Q9C4o35TLeU70025q5a27wnfn3L7hn5",
        "hmLVDNt5l1vkP9Gpc1MKRlPGBjB4Q9C 4o35TLeU70025q5a27wnfn3L7hn5"})
    void validateAirport_NameIsValid(String airportName) {
        var validationErrors = ValidationController.validateAirport(airportName);

        assertEquals(0, validationErrors.size(),
            formatErrorList(validationErrors));
    }

    @DisplayName("Invalid airport inputs")
    @ParameterizedTest(name = "{index} Airport name \"{0}\" is invalid")
    @ValueSource(strings = {
        "lNLKcwgzUKbrABEfmZwlW9xmv5lbAJwPLov78fYh8spIbRdQ1oZaM0bjyw1yv",
        "L8ZNx3K58SqA8dca7i8ZZdd3axQkW0mt4zypPiRRjIX1fbNkQ1uY83Ume6d87wXEbcKY2V",
        "NnydyLnIumRYOnBPFLsaIgzGHvWEsDZmZUQVewbrXOzxayDJNGfWSfsHeVsqTkAXW",
        "88638558545566609103177393489921514225706367818096383368941568526",
        "hmLVDNt5l1vkP9Gpc1MKRlPGBjB4Q9C 4o35TLeU70025q5a27wnfn3L7hn56"})
    @EmptySource
    void validateAirport_NameIsInvalid(String airportName) {
        var validationErrors = ValidationController.validateAirport(airportName);

        assertTrue(validationErrors.size() > 0,
            "Airport name (" + airportName
                + ") should not be valid as it's >60 characters or empty.");
    }

    @DisplayName("Valid obstacle inputs")
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
    void validateObstacle_IsValid(String obstacleName, double obstacleHeight) {
        if (obstacleName == null) {
            obstacleName = "";
        }
        var validationErrors = ValidationController.validateObstacle(obstacleName, obstacleHeight);

        assertEquals(0, validationErrors.size(),
            formatErrorList(validationErrors));
    }

    @DisplayName("Invalid obstacle inputs")
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
    void validateObstacle_IsInvalid(String obstacleName, double obstacleHeight) {
        if (obstacleName == null) {
            obstacleName = "";
        }
        var validationErrors = ValidationController.validateObstacle(obstacleName, obstacleHeight);

        assertTrue(validationErrors.size() > 0,
            "Obstacle (" + obstacleName + ", " + obstacleHeight
                + ") should not be valid as its name is > 60 characters or empty, "
                + "OR height<=0 or height>=500 or greater than 2 decimal places.");
    }

    @DisplayName("Valid runway inputs")
    @ParameterizedTest(name = "{index} Runway ({2}{0}/{3}{1}) is valid")
    @MethodSource("validRunwayInputs")
    void validateRunway_IsValid(Character pos1, Character pos2,
        int degree1, int degree2, double[] parameters) {
        var airport = new Airport("Test Airport");

        var validationErrors = ValidationController.validateRunway(pos1, pos2, degree1, degree2,
            parameters, airport);

        assertEquals(0, validationErrors.size(),
            formatErrorList(validationErrors));
    }

    @DisplayName("Invalid runway inputs")
    @ParameterizedTest(name = "{index} Runway ({2}{0}/{3}{1}) is invalid")
    @MethodSource("invalidRunwayInputs")
    void validateRunway_IsInvalid(Character pos1, Character pos2,
        int degree1, int degree2, double[] parameters) {
        var airport = new Airport("Test Airport");

        var validationErrors = ValidationController.validateRunway(pos1, pos2, degree1, degree2,
            parameters, airport);

        assertTrue(validationErrors.size() > 0,
            "Runway (" + degree1 + "/" + degree2 + ", Pos:" + pos1 + "/" + pos2
                + ") should not be valid.");
    }


    /**
     * Runway parameters that are valid (boundary and decimal places)
     */
    private static final double[] validRunwayParameter1Pass = new double[]{3902, 20000, 60, 20000,
        100, 240, 3902, 3902, 3902, 3595, 3884, 3962, 3884, 3884, 0, 306};
    private static final double[] validRunwayParameter2Pass = new double[]{3660, 1, 60, 100, 100,
        240, 3660, 3660, 3660, 3353, 3660, 3660, 3660, 3660, 0, 307};
    private static final double[] validRunwayParameter3Pass = new double[]{3660.12, 100.5, 60.12,
        100.12, 100.12, 240.12, 3660.12, 3660.12, 3660.12, 3353.12, 3660.12, 3660.12, 3660.12,
        3660.12, 0.99, 307.00};
    private static final double[] validRunwayParameter4Pass = new double[]{20000, 100, 60, 100, 100,
        240, 3660, 3660, 3660, 3353, 3660, 3660, 3660, 3660, 0, 307};

    /**
     * Runway parameters that are not valid (boundary and decimal places)
     */
    private static final double[] validRunwayParameter1Fail = new double[]{20001, 2000, 60, 20000,
        100, 240, 3902, 3902, 3902, 3595, 3884, 3962, 3884, 3884, 0, 306};
    private static final double[] validRunwayParameter2Fail = new double[]{20001, 100, 60, 100, 100,
        240, 3660, 3660, 3660, 3353, 3660, 3660, 3660, 3660, 0, 307};
    private static final double[] validRunwayParameter3Fail = new double[]{3660.123, 100.5, 60.12,
        100.12, 100.12, 240.12, 3660.12, 3660.12, 3660.122, 3353, 3660.12, 3660.12, 3660.12,
        3660.12,
        0.99, 307.999};


    /**
     * Stream of inputs for valid runway data
     *
     * @return the stream of object inputs
     */
    static Stream<Object[]> validRunwayInputs() {
        return Stream.of(
            // Positions
            new Object[]{'R', 'L', 9, 27, validRunwayParameter1Pass},
            new Object[]{'L', 'R', 9, 27, validRunwayParameter2Pass},
            new Object[]{null, null, 9, 27, validRunwayParameter1Pass},

            // Degrees
            new Object[]{'R', 'L', 16, 34, validRunwayParameter1Pass},
            new Object[]{'L', 'R', 9, 27, validRunwayParameter2Pass},
            new Object[]{'R', 'L', 1, 19, validRunwayParameter2Pass},
            new Object[]{'R', 'L', 4, 22, validRunwayParameter1Pass},

            // Degree boundaries
            new Object[]{'R', 'L', 0, 18, validRunwayParameter1Pass},
            new Object[]{'R', 'L', 18, 36, validRunwayParameter1Pass},

            // Parameter decimals
            new Object[]{'L', 'R', 1, 19, validRunwayParameter3Pass},
            new Object[]{'R', 'L', 18, 36, validRunwayParameter3Pass},

            // Parameter boundaries (>0 and <20000)
            new Object[]{'L', 'R', 1, 19, validRunwayParameter4Pass}
        );
    }

    /**
     * Stream of inputs for invalid runway data
     *
     * @return the stream of object inputs
     */
    static Stream<Object[]> invalidRunwayInputs() {
        return Stream.of(
            // Positions
            new Object[]{'L', 'L', 9, 27, validRunwayParameter1Pass},
            new Object[]{'R', 'R', 9, 27, validRunwayParameter1Pass},
            new Object[]{null, 'L', 9, 27, validRunwayParameter2Pass},
            new Object[]{'R', null, 9, 27, validRunwayParameter1Pass},

            // Degrees
            new Object[]{'R', 'L', 9, 30, validRunwayParameter1Pass},
            new Object[]{'R', 'L', 27, 45, validRunwayParameter2Pass},
            new Object[]{'R', 'L', -2, 16, validRunwayParameter1Pass},
            new Object[]{'R', 'L', 0, 19, validRunwayParameter2Pass},
            new Object[]{'L', 'R', 1, 27, validRunwayParameter1Pass},
            new Object[]{'R', 'R', 4, 24, validRunwayParameter2Pass},

            // Degree boundaries
            new Object[]{'R', 'L', -1, 17, validRunwayParameter2Pass},
            new Object[]{'L', 'R', 19, 37, validRunwayParameter1Pass},

            // Parameter decimals
            new Object[]{'L', 'R', 1, 19, validRunwayParameter3Fail},
            new Object[]{'R', 'L', 18, 36, validRunwayParameter3Fail},

            // Parameter boundaries
            new Object[]{'L', 'R', 1, 19, validRunwayParameter1Fail},
            new Object[]{'R', 'L', 18, 36, validRunwayParameter2Fail}
        );
    }

    /**
     * Format a list of errors so that the errors are on new lines
     *
     * @param errorList the list of errors
     * @return the formatted string of errors
     */
    private String formatErrorList(List<String> errorList) {
        return String.join("\n", errorList);
    }
}
