package com.team44.runwayredeclarationapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.ErrorObjectPair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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

    @DisplayName("Add airport with valid names")
    @ParameterizedTest(name = "{index} Airport with name \"{0}\" is valid")
    @ValueSource(strings = {"a", "1", "HGJUy", "G8JEw Th QoT", "Uo97IeCf0zrjH TTtkF7qRoweupcqsH",
        "C8Jk58S1tRXIcclRynyYXhmH62wp2aQ88ChaOyxRNEa7I",
        "48684379855363115832241 8396361071063553497991",
        "621689318546",
        "hmLVDNt5l1vkP9Gpc1MKRlPGBjB4Q9C4o35TLeU70025q5a27wnfn3L7hn5",
        "hmLVDNt5l1vkP9Gpc1MKRlPGBjB4Q9C 4o35TLeU70025q5a27wnfn3L7hn5"})
    void addAirport_Valid(String airportName) {
        ErrorObjectPair<Airport> airportErrorObjectPair = dataController.addAirport(airportName);

        assertEquals(airportErrorObjectPair.getErrorsArray().length, 0,
            "There should be no errors");
        assertEquals(airportErrorObjectPair.getObject().getName(), airportName,
            "Name of airport created is different");
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
        ErrorObjectPair<Airport> airportErrorObjectPair = dataController.addAirport(airportName);

        assertTrue(airportErrorObjectPair.getErrorsArray().length > 0, "There should be errors");
    }

    @Test
    void editAirport() {
        //    todo
    }

    @Test
    void addRunway() {
    }

    @Test
    void editRunway() {
    }

    @Test
    void addObstacle() {
    }

    @Test
    void editObstacle() {
    }

    @Test
    void deleteAirport() {
    }

    @Test
    void deleteRunway() {
    }

    @Test
    void deleteObstacle() {
    }
}