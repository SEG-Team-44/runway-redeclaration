package com.team44.runwayredeclarationapp.view.component;

import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The side-on view visualisation canvas for the runway
 */
public class SideOnView extends VisualisationBase {

    private static final Logger logger = LogManager.getLogger(SideOnView.class);

    /**
     * Create a new side-on view canvas of the runway
     *
     * @param width  the initial width of the canvas
     * @param height the initial height of the canvas
     */
    public SideOnView(double width, double height) {
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
    protected void paint() {
        double width = getWidth();
        double height = getHeight();
        var gc = getGraphicsContext2D();

        // Clear the current canvas
        gc.clearRect(0, 0, width, height);

        // Draw the new canvas
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
    }
}
