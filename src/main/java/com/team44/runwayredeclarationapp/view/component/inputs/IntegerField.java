package com.team44.runwayredeclarationapp.view.component.inputs;

/**
 * Text field that accepts integers
 */
public class IntegerField extends RegexField {

    /**
     * The max number of digits
     */
    private Integer numberOfDigits;

    /**
     * Whether to allow negative numbers
     */
    private boolean allowNegative = false;

    /**
     * Create a text field that accepts integers
     */
    public IntegerField() {
        this(null, 6);
    }

    /**
     * Create a text field that accepts integers
     *
     * @param numberOfDigits the max number of digits allowed
     */
    public IntegerField(Integer numberOfDigits) {
        this(null, numberOfDigits);
    }

    /**
     * Create a text field that accepts integers
     *
     * @param value          the value to go in the field
     * @param numberOfDigits the max number of digits allowed
     */
    public IntegerField(Integer value, Integer numberOfDigits) {
        super(createRegex(numberOfDigits, false), String.valueOf(value == null ? "" : value));

        setDigits(numberOfDigits);
    }

    /**
     * Set the max number of digits allowed in the field
     *
     * @param numberOfDigits the max number of digits allowed
     */
    public void setDigits(Integer numberOfDigits) {
        this.numberOfDigits = numberOfDigits;

        // Create and set the regex format
        regex = createRegex(numberOfDigits, allowNegative);
        setRegexFormat(regex);
    }

    /**
     * Set whether negative numbers should be allowed
     *
     * @param allow bool indicating whether negative numbers are allowed
     */
    public void setAllowNegative(Boolean allow) {
        regex = createRegex(numberOfDigits, allow);

        setRegexFormat(regex);
    }

    /**
     * Create the regex to allow integer values
     *
     * @param numberOfDigits the max number of digits allowed
     * @param allowNegative  bool indicating whether negative numbers are allowed
     * @return the regex
     */
    private static String createRegex(Integer numberOfDigits, Boolean allowNegative) {
        return "^" + (allowNegative ? "-?" : "") + "[0-9]{0," + numberOfDigits + "}$";
    }

    /**
     * Get the value in the field as an integer type
     *
     * @return the value as an integer
     */
    public Integer getValue() {
        // Make sure that it is a valid integer before parsing it
        if (isInputValid()) {
            return Integer.parseInt(getText());
        }
        return 0;
    }

    /**
     * Set the value of the field
     *
     * @param value the integer value of the field
     */
    public void setValue(int value) {
        this.setText(String.valueOf(value));
    }
}
