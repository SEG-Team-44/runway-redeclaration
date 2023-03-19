package com.team44.runwayredeclarationapp.view.component.inputs;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

/**
 * A text field that is verified and validated using a regex
 */
public class RegexField extends TextField {

    /**
     * The regex to verify and validate with
     */
    protected String regex;

    /**
     * Create text field that accepts values that match a regex
     *
     * @param regex the regex
     */
    public RegexField(String regex) {
        this(regex, "");
    }

    /**
     * Create text field that accepts values that match a regex
     *
     * @param regex the regex
     * @param text  the text to go into the field
     */
    public RegexField(String regex, String text) {
        super(text);

        this.regex = regex;
        setRegexFormat(regex);
    }

    /**
     * Set the regex for the formatter of the text field
     *
     * @param regex the regex
     */
    protected void setRegexFormat(String regex) {
        setTextFormatter(new TextFormatter<Object>(change ->
            // only change if regex matches
            (change.getControlNewText().matches(regex)) ? change
                : null
        ));
    }

    /**
     * Get the regex used for verifying and validating
     *
     * @return the regex
     */
    public String getRegex() {
        return regex;
    }

    /**
     * Check if text field is empty
     *
     * @return whether the text field is empty
     */
    public Boolean isEmpty() {
        return getText().isEmpty();
    }

    /**
     * Check if the text field matches the regex
     *
     * @return whether the regex matches the text in the text field
     */
    public Boolean isRegexMatch() {
        return getText().matches(regex);
    }

    /**
     * Check if the input is valid by the regex and not empty
     *
     * @return whether the input is valid or not
     */
    public Boolean isInputValid() {
        // Check the regex and text empty
        return isRegexMatch() && !isEmpty();
    }

}
