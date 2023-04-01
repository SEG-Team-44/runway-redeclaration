package com.team44.runwayredeclarationapp.event;

/**
 * The listener when an airport/runway/obstacle has been deleted
 */
public interface OnDeleteListener {

    /**
     * Called when an airport/runway/obstacle has been deleted
     *
     * @param object the object that has been deleted
     */
    void delete(Object object);
}
