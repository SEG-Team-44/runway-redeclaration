package com.team44.runwayredeclarationapp.view.component;

import com.team44.runwayredeclarationapp.model.Runway;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Orientation;

/**
 * The runway parameter values grid
 */
public class RunwayParametersGrid extends Grid {

    /**
     * Properties for the runway parameters
     */
    private final SimpleStringProperty runway1, runway2;
    private final SimpleStringProperty tora1, tora2;
    private final SimpleStringProperty toda1, toda2;
    private final SimpleStringProperty asda1, asda2;
    private final SimpleStringProperty lda1, lda2;


    /**
     * Create a runway parameter values grid
     *
     * @param title the title of the grid
     */
    public RunwayParametersGrid(String title) {
        super();

        // Set the grid properties
        setVgap(10);
        setHgap(13);

        getStyleClass().add("values-grid");

        // Add the column names
        addTitleText("Runway", 0, 2);
        addTitleText("TORA", 1, 2);
        addTitleText("TODA", 2, 2);
        addTitleText("ASDA", 3, 2);
        addTitleText("LDA", 4, 2);

        // Add separators
        addSeparator(Orientation.HORIZONTAL, 0, 3, 5, 1);
        addSeparator(Orientation.HORIZONTAL, 0, 1, 5, 1);

        addTitleText(title, 0, 0, 5, 1);

        // Set the string properties
        runway1 = new SimpleStringProperty();
        runway2 = new SimpleStringProperty();
        tora1 = new SimpleStringProperty();
        tora2 = new SimpleStringProperty();
        toda1 = new SimpleStringProperty();
        toda2 = new SimpleStringProperty();
        asda1 = new SimpleStringProperty();
        asda2 = new SimpleStringProperty();
        lda1 = new SimpleStringProperty();
        lda2 = new SimpleStringProperty();

        // Add the contents of the table
        // Logical runway 1
        addBoundedText(runway1, 0, 4);
        addBoundedText(tora1, 1, 4);
        addBoundedText(toda1, 2, 4);
        addBoundedText(asda1, 3, 4);
        addBoundedText(lda1, 4, 4);

        // Logical runway 2
        addBoundedText(runway2, 0, 5);
        addBoundedText(tora2, 1, 5);
        addBoundedText(toda2, 2, 5);
        addBoundedText(asda2, 3, 5);
        addBoundedText(lda2, 4, 5);
    }

    /**
     * Set a new runway to be displayed on the grid/table
     *
     * @param runway the runway object
     */
    public void setRunway(Runway runway) {
        // Get the ID's of the logical runway
        var runway1ID = runway.getLogicId1();
        var runway2ID = runway.getLogicId2();

        // Set the new property values for logical runway 1
        this.runway1.set(runway1ID);
        this.tora1.set(String.valueOf(runway.getTora(runway1ID)));
        this.toda1.set(String.valueOf(runway.getToda(runway1ID)));
        this.asda1.set(String.valueOf(runway.getAsda(runway1ID)));
        this.lda1.set(String.valueOf(runway.getLda(runway1ID)));

        // Set the new property values for logical runway 2
        this.runway2.set(runway2ID);
        this.tora2.set(String.valueOf(runway.getTora(runway2ID)));
        this.toda2.set(String.valueOf(runway.getToda(runway2ID)));
        this.asda2.set(String.valueOf(runway.getAsda(runway2ID)));
        this.lda2.set(String.valueOf(runway.getLda(runway2ID)));
    }

    /**
     * Reset all the text properties
     */
    public void reset() {
        // Reset property values for logical runway 1
        this.runway1.set("");
        this.tora1.set("");
        this.toda1.set("");
        this.asda1.set("");
        this.lda1.set("");

        // Reset property values for logical runway 2
        this.runway2.set("");
        this.tora2.set("");
        this.toda2.set("");
        this.asda2.set("");
        this.lda2.set("");
    }
}
