package com.team44.runwayredeclarationapp.view.component;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * Grid with support for adding properties to cells and adding separators
 */
public class Grid extends GridPane {

    public Grid() {
        // Set the grid properties
        setVgap(10);
        setHgap(13);

        getStyleClass().add("values-grid");
    }

    /**
     * Add a separator to a given cell in the grid
     *
     * @param orientation the orientation of the separator
     * @param col         the column number
     * @param row         the row number
     * @param colSpan     the column span
     * @param rowSpan     the row span
     */
    public void addSeparator(Orientation orientation, int col, int row, int colSpan,
        int rowSpan) {
        Separator separator = new Separator(orientation);
        separator.getStyleClass().add("grid-title-box-separator");

        // Add to grid
        add(separator, col, row, colSpan, rowSpan);
    }

    /**
     * Add a styled title text to a given cell in the grid
     *
     * @param text the text
     * @param col  the column number
     * @param row  the row number
     */
    public void addTitleText(String text, int col, int row) {
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
    public void addTitleText(String text, int col, int row, int colSpan, int rowSpan) {
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
    public void addBoundedText(SimpleStringProperty textProperty, int col, int row) {
        var textObj = new Text();
        textObj.textProperty().bindBidirectional(textProperty);

        // Add the text to the grid
        add(textObj, col, row);
    }

    /**
     * Add a styled double property to a given cell in the grid
     *
     * @param doubleProperty the double property
     * @param col            the column number
     * @param row            the row number
     */
    public void addBoundedText(SimpleDoubleProperty doubleProperty, int col, int row) {
        var textObj = new Text();
        textObj.textProperty().bind(doubleProperty.asString());

        // Add the text to the grid
        add(textObj, col, row);
    }
}
