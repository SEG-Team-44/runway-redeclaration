package com.team44.runwayredeclarationapp.model.form;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.view.component.inputs.RegexField;

/**
 * Form for adding a new airport
 */
public class AirportForm extends BaseForm {

    /**
     * Input field for airport name
     */
    private final RegexField airportNameInput = new RegexField("^.{0,60}$");

    /**
     * Set an existing airport, if any, to modify
     *
     * @param airport the existing airport
     */
    public void setAirport(Airport airport) {
        airportNameInput.setText(airport.getName());
    }

    /**
     * Validate the fields in the form
     *
     * @return whether all the fields are valid
     */
    @Override
    public Boolean isValid() {
        return airportNameInput.isInputValid();
    }

    public RegexField getAirportNameInput() {
        return airportNameInput;
    }
}
