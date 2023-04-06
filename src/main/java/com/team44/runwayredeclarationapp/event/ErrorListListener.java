package com.team44.runwayredeclarationapp.event;

/**
 * Listener to call when a list of errors is needed to be shown
 */
public interface ErrorListListener {

    /**
     * Called when a list of errors has to be shown
     *
     * @param errors the list of errors
     */
    void alert(String[] errors);
}
