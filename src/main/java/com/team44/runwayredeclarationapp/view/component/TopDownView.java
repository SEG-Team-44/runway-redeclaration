package com.team44.runwayredeclarationapp.view.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The side-on view visualisation canvas for the runway
 */
public class TopDownView extends Canvas {

    private static final Logger logger = LogManager.getLogger(TopDownView.class);

    /** Create a new top-down view canvas of the runway
     * @param width the initial width of the canvas
     * @param height the initial height of the canvas
     */
    public TopDownView(double width, double height) {
        setWidth(width);
        setHeight(height);

        paint();

        // Redraw the canvas whenever the window is resized
        widthProperty().addListener(evt -> paint());
        heightProperty().addListener(evt -> paint());
    }

    /**
     * Paint the canvas to visualise the runway
     */
    private void paint() {
        double width = getWidth();
        double height = getHeight();
        var gc = getGraphicsContext2D();

        // Clear the current canvas
        gc.clearRect(0, 0, width, height);

        // Draw the new canvas
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        // Draw the runway strip
        gc.setFill(Color.DARKGREY);
        gc.fillRect(70, (height / 2) - 40, width - 140, 80);

        // Draw the runway centre line
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1);
        gc.setLineDashes(10);
        gc.strokeLine(150, height / 2, width - 150, height / 2);
    }
}
