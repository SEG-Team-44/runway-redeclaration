package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.event.RecalculatedViewListener;

/**
 * The class responsible for carrying out the calculations and updating the GUI after
 */
public class RecalculationController {

    private RecalculatedViewListener recalculatedViewListener;

    public RecalculationController() {
    }

    public void setRecalculatedViewListener(RecalculatedViewListener recalculatedViewListener) {
        this.recalculatedViewListener = recalculatedViewListener;
    }

    public void recalculateRunway() {
        recalculatedViewListener.recalculate();
    }
}
