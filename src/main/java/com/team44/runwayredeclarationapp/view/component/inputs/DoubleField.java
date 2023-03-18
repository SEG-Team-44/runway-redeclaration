package com.team44.runwayredeclarationapp.view.component.inputs;

/**
 * Text field that accepts doubles
 */
public class DoubleField extends RegexField {

    /**
     * The max number of digits
     */
    private Integer numberofDigits;
    /**
     * The max number of decimals
     */
    private Integer numberOfDecimals;

    /**
     * Create a text field that accepts doubles
     */
    public DoubleField() {
        this(6, 2);
    }

    /**
     * Create a text field that accepts doubles
     *
     * @param numberOfDigits   the max number of digits allowed
     * @param numberOfDecimals the max number of decimals allowed
     */
    public DoubleField(Integer numberOfDigits, Integer numberOfDecimals) {
        super(createRegex(numberOfDigits, numberOfDecimals));

        setDigitsDecimals(numberOfDigits, numberOfDecimals);
    }

    /**
     * Set the max number of digits and decimals allowed in the field
     *
     * @param numberOfDigits   the max number of digits allowed
     * @param numberOfDecimals the max number of decimals allowed
     */
    public void setDigitsDecimals(Integer numberOfDigits, Integer numberOfDecimals) {
        this.numberofDigits = numberOfDigits;
        this.numberOfDecimals = numberOfDecimals;

        // Create and set the regex format
        regex = createRegex(numberOfDigits, numberOfDecimals);
        setRegexFormat(regex);
    }

    /**
     * Create the regex to allow double values
     *
     * @param numberOfDigits   the max number of digits allowed
     * @param numberOfDecimals the max number of decimals allowed
     * @return the regex
     */
    private static String createRegex(Integer numberOfDigits, Integer numberOfDecimals) {
        return "^(?:\\d{1," + numberOfDigits + "}(?:\\.\\d{0," + numberOfDecimals + "})?)?$";
    }

    /**
     * Get the value in the field as a double type
     *
     * @return the value as a double
     */
    public Double getValue() {
        // Make sure that it is a valid double before parsing it
        if (isInputValid()) {
            return Double.parseDouble(getText());
        }
        return 0.0;
    }
}
