package com.team44.runwayredeclarationapp.view.component.inputs;

/**
 * Text field that accepts doubles
 */
public class DoubleField extends RegexField {

    /**
     * The max number of digits
     */
    private Integer numberOfDigits;
    /**
     * The max number of decimals
     */
    private Integer numberOfDecimals;
    /**
     * Whether to allow negative numbers
     */
    private boolean allowNegative = false;

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
        this(null, numberOfDigits, numberOfDecimals);
    }

    /**
     * Create a text field that accepts doubles
     *
     * @param value            the value to go in the field
     * @param numberOfDigits   the max number of digits allowed
     * @param numberOfDecimals the max number of decimals allowed
     */
    public DoubleField(Double value, Integer numberOfDigits, Integer numberOfDecimals) {
        super(createRegex(numberOfDigits, numberOfDecimals, false),
            String.valueOf(value == null ? "" : value));

        setDigitsDecimals(numberOfDigits, numberOfDecimals);
    }

    /**
     * Set the max number of digits and decimals allowed in the field
     *
     * @param numberOfDigits   the max number of digits allowed
     * @param numberOfDecimals the max number of decimals allowed
     */
    public void setDigitsDecimals(Integer numberOfDigits, Integer numberOfDecimals) {
        this.numberOfDigits = numberOfDigits;
        this.numberOfDecimals = numberOfDecimals;

        // Create and set the regex format
        regex = createRegex(numberOfDigits, numberOfDecimals, allowNegative);
        setRegexFormat(regex);
    }

    /**
     * Set whether negative numbers should be allowed
     *
     * @param allow bool indicating whether negative numbers are allowed
     */
    public void setAllowNegative(Boolean allow) {
        regex = createRegex(numberOfDigits, numberOfDecimals, allow);
        setRegexFormat(regex);
    }

    /**
     * Create the regex to allow double values
     *
     * @param numberOfDigits   the max number of digits allowed
     * @param numberOfDecimals the max number of decimals allowed
     * @param allowNegative    bool indicating whether negative numbers are allowed
     * @return the regex
     */
    private static String createRegex(Integer numberOfDigits, Integer numberOfDecimals,
        Boolean allowNegative) {
        return "^" + (allowNegative ? "-?" : "") + "(?:\\d{1," + numberOfDigits + "}(?:\\.\\d{0,"
            + numberOfDecimals + "})?)?$";
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

    /**
     * Set the value of the field
     *
     * @param value the double value of the field
     */
    public void setValue(double value) {
        this.setText(String.valueOf(value));
    }
}
