package com.team44.runwayredeclarationapp.view.component.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

/**
 * Information alert that has wrapped content text
 */
public class ErrorAlert {

    private final String title;
    private final String header;
    private final String content;

    /**
     * Create an error alert
     *
     * @param title   the title text
     * @param header  the header text
     * @param content the content text
     */
    public ErrorAlert(String title, String header, String content) {
        this.title = title;
        this.header = header;
        this.content = content;
    }

    /**
     * Show the error alert
     */
    public void show() {
        // Create alert and text
        var alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        var alertText = new Text(content);
        alertText.setWrappingWidth(500);
        alert.getDialogPane().setContent(alertText);

        // Show the alert
        alert.show();
    }
}
