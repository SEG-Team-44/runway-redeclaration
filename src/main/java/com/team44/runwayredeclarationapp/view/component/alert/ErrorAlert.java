package com.team44.runwayredeclarationapp.view.component.alert;

import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

/**
 * Error alert that is able to show a list of errors
 */
public class ErrorAlert {

    /**
     * The list of errors shown in the alert
     */
    private final ArrayList<String> errors = new ArrayList<>();

    /**
     * Add an error to the list of errors
     *
     * @param errorText the error text
     */
    public void addError(String errorText) {
        errors.add(errorText);
    }

    /**
     * Get the list of errors currently in the list
     *
     * @return the list of errors
     */
    public ArrayList<String> getErrors() {
        return errors;
    }

    /**
     * Clear the list of errors currently in the list
     */
    public void clearErrors() {
        errors.clear();
    }

    /**
     * Show the error alert
     */
    public void show() {
        if (errors.isEmpty()) {
            return;
        }

        // Create alert and text
        var alert = new Alert(AlertType.ERROR);
        alert.setHeaderText("Please fix the following errors:");
        var errorText = new Label(String.join("\n", errors));
        errorText.setWrapText(true);
        alert.getDialogPane().setContent(errorText);

        // Show the alert
        alert.show();

        // Reset current errors
        errors.clear();
    }
}
