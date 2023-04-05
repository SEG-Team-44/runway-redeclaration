package com.team44.runwayredeclarationapp.event;

/**
 * Listener to call when an alert is needed to be shown
 */
public interface AlertListener {

    /**
     * Called when an alert has to be shown
     *
     * @param title   the alert title
     * @param header  the alert header
     * @param content the alert content
     */
    void alert(String title, String header, String content);

}
