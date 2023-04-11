package com.team44.runwayredeclarationapp.view.component.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * Class responsible for creating a confirmation alert
 */
public class ConfirmAlert extends Alert {

    /**
     * Create a confirmation alert
     *
     * @param header  header
     * @param content content
     */
    public ConfirmAlert(String header, String content) {
        super(AlertType.CONFIRMATION);
        this.setHeaderText(header);
        Label alertText = new Label(content);
        alertText.setWrapText(true);
        this.getDialogPane().setContent(alertText);
    }
}
