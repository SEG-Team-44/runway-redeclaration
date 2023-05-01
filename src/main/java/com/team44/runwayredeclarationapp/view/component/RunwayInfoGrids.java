package com.team44.runwayredeclarationapp.view.component;

import com.team44.runwayredeclarationapp.model.Runway;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Orientation;
import javafx.scene.layout.HBox;

/**
 * HBox containing runway information grids
 */
public class RunwayInfoGrids extends HBox {

    /**
     * Properties for the runway information
     */
    private final SimpleStringProperty runwayID;
    private final SimpleDoubleProperty runwayL, runwayW, resa;

    private final SimpleStringProperty designator1, designator2;
    private final SimpleDoubleProperty clearway1, clearway2;
    private final SimpleDoubleProperty stopway1, stopway2;
    private final SimpleDoubleProperty disThresh1, disThresh2;


    /**
     * Create the runway information grids
     */
    public RunwayInfoGrids() {
        setSpacing(90);

        // Create the property values
        runwayID = new SimpleStringProperty();
        runwayL = new SimpleDoubleProperty();
        runwayW = new SimpleDoubleProperty();
        resa = new SimpleDoubleProperty();
        designator1 = new SimpleStringProperty();
        designator2 = new SimpleStringProperty();
        clearway1 = new SimpleDoubleProperty();
        clearway2 = new SimpleDoubleProperty();
        stopway1 = new SimpleDoubleProperty();
        stopway2 = new SimpleDoubleProperty();
        disThresh1 = new SimpleDoubleProperty();
        disThresh2 = new SimpleDoubleProperty();

        // Create the 2 grids
        var mainInfoGrid = new Grid();
        var extraInfoGrid = new Grid();

        // Add separators to the grids
        mainInfoGrid.addSeparator(Orientation.VERTICAL, 1, 0, 1, 4);
        extraInfoGrid.addSeparator(Orientation.VERTICAL, 1, 0, 1, 4);

        // Add the rows and columns to the main info grid
        mainInfoGrid.addTitleText("Designator", 0, 0);
        mainInfoGrid.addBoundedText(designator1, 2, 0);
        mainInfoGrid.addBoundedText(designator2, 3, 0);
        mainInfoGrid.addTitleText("Clearway", 0, 1);
        mainInfoGrid.addBoundedText(clearway1, 2, 1);
        mainInfoGrid.addBoundedText(clearway2, 3, 1);
        mainInfoGrid.addTitleText("Stopway", 0, 2);
        mainInfoGrid.addBoundedText(stopway1, 2, 2);
        mainInfoGrid.addBoundedText(stopway2, 3, 2);
        mainInfoGrid.addTitleText("Displaced Threshold", 0, 3);
        mainInfoGrid.addBoundedText(disThresh1, 2, 3);
        mainInfoGrid.addBoundedText(disThresh2, 3, 3);

        // Add the rows and columns to the extra info grid
        extraInfoGrid.addTitleText("Runway", 0, 0);
        extraInfoGrid.addBoundedText(runwayID, 2, 0);
        extraInfoGrid.addTitleText("Runway Length", 0, 1);
        extraInfoGrid.addBoundedText(runwayL, 2, 1);
        extraInfoGrid.addTitleText("Runway Width", 0, 2);
        extraInfoGrid.addBoundedText(runwayW, 2, 2);
        extraInfoGrid.addTitleText("RESA", 0, 3);
        extraInfoGrid.addBoundedText(resa, 2, 3);

        // Add the grids to the HBox
        this.getChildren().addAll(mainInfoGrid, extraInfoGrid);
    }

    /**
     * Set a runway to be displayed in the information grids
     *
     * @param runway the runway to be displayed
     */
    public void setRunway(Runway runway) {
        var runway1 = runway.getLogicId1();
        var runway2 = runway.getLogicId2();

        // Set the property values
        runwayID.set(runway.getPhyId());
        runwayL.set(runway.getRunwayL());
        runwayW.set(runway.getRunwayW());
        resa.set(runway.getResaL());
        designator1.set(runway.getLogicId1());
        designator2.set(runway.getLogicId2());
        clearway1.set(runway.getClearwayL(runway1));
        clearway2.set(runway.getClearwayL(runway2));
        stopway1.set(runway.getStopwayL(runway1));
        stopway2.set(runway.getStopwayL(runway2));

        disThresh1.set(runway.getDisThresh(runway2));
        disThresh2.set(runway.getDisThresh(runway1));
    }
}
