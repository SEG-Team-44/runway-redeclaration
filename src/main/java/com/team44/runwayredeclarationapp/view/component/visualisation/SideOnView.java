package com.team44.runwayredeclarationapp.view.component.visualisation;

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
        super(width, height);
        runwayWidth = 20;
    }


    /**
     * Paint custom canvas features for side-on view
     */
    @Override
    protected void paintCanvasCustom() {
        // Set new size
        var gc = getGraphicsContext2D();

        // Displaced threshold coords
        var leftDisplaced = runwayX1 + displacedThresholdL;
        var rightDisplaced = runwayX2 - displacedThresholdR;

        // Draw displaced threshold line
        gc.setStroke(Color.WHITE);
        gc.setLineDashes();
        gc.setLineWidth(1.6);
        if (displacedThresholdL > 0) {
            gc.strokeLine(leftDisplaced, runwayY1, leftDisplaced, runwayY2);
        }
        if (displacedThresholdR > 0) {
            gc.strokeLine(rightDisplaced, runwayY1, rightDisplaced, runwayY2);
        }

        // Clearway coords
        var leftClearwayStartX = runwayX1 - leftClearwayLength;
        var rightClearwayEndX = runwayX2 + rightClearwayLength;

        // Clearway
        gc.setStroke(Color.WHITE);
        gc.setLineDashes();
        gc.strokeRect(leftClearwayStartX, runwayY1, leftClearwayLength,
            runwayWidth);
        gc.strokeRect(runwayX2, runwayY1, rightClearwayLength, runwayWidth);

        // Threshold designators
        addText("09L", 14, leftClearwayStartX - 35, runwayY1 + 15);
        addText("27R", 14, rightClearwayEndX + 20, runwayY1 + 15);
    }

    /**
     * Paint custom canvas background for side-on view
     */
    @Override
    protected void paintCanvasBackground() {
        // Set new size
        double width = getWidth();
        double height = getHeight();
        var gc = getGraphicsContext2D();

        // Draw the background for below the runway
        gc.setFill(Color.SADDLEBROWN);
        gc.fillRect(0, runwayY1, width, height - runwayY1);
    }

    /**
     * Draw the obstacle
     */
    @Override
    protected void drawObstacle() {
        var gc = getGraphicsContext2D();

        // Draw the background for below the runway
        gc.setFill(Color.RED);
        gc.fillRect(runwayX1 + obstacleDistanceFromStart, runwayY1 - obstacleHeight, 20,
            obstacleHeight);
    }

}
