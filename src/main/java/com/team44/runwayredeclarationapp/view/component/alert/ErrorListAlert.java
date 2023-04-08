package com.team44.runwayredeclarationapp.view.component.alert;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

/**
 * Error alert that is able to show a list of errors
 */
public class ErrorListAlert {

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

    public void setErrors(String... errorsToSet) {
        errors.clear();
        errors.addAll(List.of(errorsToSet));
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
        var errorText = new Text(String.join("\n", errors));
        errorText.setWrappingWidth(500);
        alert.getDialogPane().setContent(errorText);

        // Show the alert
        alert.show();

        // Reset current errors
        errors.clear();
    }
}
