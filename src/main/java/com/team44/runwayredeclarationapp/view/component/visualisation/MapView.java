package com.team44.runwayredeclarationapp.view.component.visualisation;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * The map view visualisation canvas for the runway
 */
public class MapView extends TopDownView {

    /**
     * The runway top-down view image
     */
    private Image runwayImage = new Image(
        getClass().getResource("/images/runway1.png").toExternalForm());


    /**
     * Create a new map view canvas of the runway
     *
     * @param width  the initial width of the canvas
     * @param height the initial height of the canvas
     */
    public MapView(double width, double height) {
        super(width, height);

        runwayWidth = 0;
    }

    @Override
    protected void paintCanvasBackground() {
        // Set new size
        var gc = getGraphicsContext2D();
        var width = getWidth();
        var height = getHeight();

        // Draw the image
        var scale = getWidth() / 1818.18;
        gc.drawImage(runwayImage,
            (width - runwayImage.getWidth() * scale) / 2,
            (height - runwayImage.getHeight() * scale) / 2,
            runwayImage.getWidth() * scale,
            runwayImage.getHeight() * scale
        );

        // Make background darker
        gc.setGlobalAlpha(0.3);
        gc.setFill(Color.BLACK);
        gc.fillRect(-width, -height, width * 3, height * 3);
        gc.setGlobalAlpha(1);
    }

    @Override
    protected void paintKeyCustom(double nextTextX, double nextKeyBoxX) {
    }
}
