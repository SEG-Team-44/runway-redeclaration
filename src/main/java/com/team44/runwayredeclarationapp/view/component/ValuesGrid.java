package com.team44.runwayredeclarationapp.view.component;

import com.team44.runwayredeclarationapp.model.Runway;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * The runway parameter values grid
 */
public class ValuesGrid extends GridPane {

    private SimpleStringProperty runway1, runway2;
    private SimpleStringProperty tora1, tora2;
    private SimpleStringProperty toda1, toda2;
    private SimpleStringProperty asda1, asda2;
    private SimpleStringProperty lda1, lda2;


    /**
     * Create a runway parameter values grid
     *
     * @param title the title of the grid
     */
    public ValuesGrid(String title) {
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
        Separator lineBelow = new Separator(Orientation.HORIZONTAL);
        Separator lineAbove = new Separator(Orientation.HORIZONTAL);
        lineBelow.getStyleClass().add("grid-title-box-separator");
        lineAbove.getStyleClass().add("grid-title-box-separator");
        add(lineBelow, 0, 3, 5, 1);
        add(lineAbove, 0, 1, 5, 1);
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
        this.lda1.set(String.valueOf(runway.getAsda(runway1ID)));

        // Set the new property values for logical runway 2
        this.runway2.set(runway2ID);
        this.tora2.set(String.valueOf(runway.getTora(runway2ID)));
        this.toda2.set(String.valueOf(runway.getToda(runway2ID)));
        this.asda2.set(String.valueOf(runway.getAsda(runway2ID)));
        this.lda2.set(String.valueOf(runway.getAsda(runway2ID)));

    }

    /**
     * Add a styled title text to a given cell in the grid
     *
     * @param text the text
     * @param col  the column number
     * @param row  the row number
     */
    private void addTitleText(String text, int col, int row) {
        addTitleText(text, col, row, 1, 1);
    }

    /**
     * Add a styled title text to a given cell in the grid
     *
     * @param text    the text
     * @param col     the column number
     * @param row     the row number
     * @param colSpan the column span
     * @param rowSpan the row span
     */
    private void addTitleText(String text, int col, int row, int colSpan, int rowSpan) {
        var textObj = new Text(text);
        textObj.getStyleClass().add("grid-title");

        // Add the text to the grid
        add(textObj, col, row, colSpan, rowSpan);
    }

    /**
     * Add a styled text property to a given cell in the grid
     *
     * @param textProperty the text property
     * @param col          the column number
     * @param row          the row number
     */
    private void addBoundedText(SimpleStringProperty textProperty, int col, int row) {
        var textObj = new Text();
        textObj.textProperty().bindBidirectional(textProperty);

        // Add the text to the grid
        add(textObj, col, row);
    }
}
