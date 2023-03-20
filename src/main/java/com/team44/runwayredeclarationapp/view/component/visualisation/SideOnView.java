package com.team44.runwayredeclarationapp.view.component.visualisation;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
        addText(tDesignator1, 14, leftClearwayStartX - 35, runwayY1 + 15);
        addText(tDesignator2, 14, rightClearwayEndX + 20, runwayY1 + 15);
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

    /**
     * Draw TOCS/ALS
     */
    @Override
    protected void drawTOCSandALS() {
        var gc = getGraphicsContext2D();
        //draw TOCS/ALS as a line
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);
        gc.strokeLine(tocsStartX, tocsStartY, tocsEndX, tocsEndY);

        gc.setFill(Color.WHITE);
        //get the angle between TOCS/ALS and the horizon in degree
        var angle = Math.toDegrees(Math.atan(obstacleHeight/slope));

        //print the label above the line pivoted at centre & rotated at the same angle with the line
        if (isObstacleOnLeftSide) {
            gc.translate((tocsEndX + slope/1.5), (tocsStartY + tocsEndY)/2 - 3);
            gc.rotate(angle);
            String text = "TOCS/ALS";
            double textWidth = gc.getFont().getSize() * text.length() / 2.0;
            gc.fillText(text,-textWidth,0);
            gc.rotate(-angle);
            gc.translate(-(tocsEndX + slope/1.5), -(tocsStartY + tocsEndY)/2 + 3);
        }
        else {
            gc.translate((tocsEndX - slope/2.5), (tocsStartY + tocsEndY)/2 - 4);
            gc.rotate(-angle);
            String text = "TOCS/ALS";
            double textWidth = gc.getFont().getSize() * text.length() / 2.0;
            gc.fillText(text,-textWidth,0);
            gc.rotate(angle);
            gc.translate(-(tocsEndX - slope/2.5), -(tocsStartY + tocsEndY)/2 + 4);
        }
    }

}
