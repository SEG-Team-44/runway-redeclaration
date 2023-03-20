package com.team44.runwayredeclarationapp.view.component.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

/**
 * Information alert that has wrapped content text
 */
public class InfoAlert {

    private String title, header, content;

    /**
     * Create an information alert
     *
     * @param title   the title text
     * @param header  the header text
     * @param content the content text
     */
    public InfoAlert(String title, String header, String content) {
        this.title = title;
        this.header = header;
        this.content = content;
    }

    /**
     * Show the error alert
     */
    public void show() {
        // Create alert and text
        var alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(header);
        var alertText = new Label(content);
        alertText.setWrapText(true);
        alert.getDialogPane().setContent(alertText);

        // Show the alert
        alert.show();
    }
}
