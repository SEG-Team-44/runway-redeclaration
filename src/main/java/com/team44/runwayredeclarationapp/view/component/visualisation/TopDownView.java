package com.team44.runwayredeclarationapp.view.component.visualisation;

import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The side-on view visualisation canvas for the runway
 */
public class TopDownView extends VisualisationBase {

    private static final Logger logger = LogManager.getLogger(TopDownView.class);

    /**
     * Create a new top-down view canvas of the runway
     *
     * @param width  the initial width of the canvas
     * @param height the initial height of the canvas
     */
    public TopDownView(double width, double height) {
        super(width, height);
        runwayWidth = 55;

        logger.info("Initialised the Top Down View");
    }


    /**
     * Paint custom canvas features for the top-down view
     */
    @Override
    protected void paintCanvasCustom() {
        // Set new size
        double height = getHeight();
        var gc = getGraphicsContext2D();

        // Clearway
        gc.setStroke(Color.WHITE);
        gc.setLineDashes();
        gc.strokeRect(runwayX1 - leftClearwayLength, runwayY1 - 15, leftClearwayLength,
            runwayWidth + 30);
        gc.strokeRect(runwayX2, runwayY1 - 15, rightClearwayLength, runwayWidth + 30);

        // Draw the runway line markings (Displaced threshold)
        gc.setStroke(Color.WHITE);
        gc.setLineDashes();
        gc.setLineWidth(1.6);
        int numberOfMarkingLines = 10;
        double dashMarkingWidth = 30;
        double distanceBetween = runwayWidth / (numberOfMarkingLines + 1);
        // Left side markings
        for (int i = 1; i <= numberOfMarkingLines; i++) {
            double x1 = runwayX1 + displacedThresholdL;
            double y = runwayY1 + (i * distanceBetween);

            gc.strokeLine(x1, y, x1 + dashMarkingWidth, y);
        }
        // Right side markings
        for (int i = 1; i <= numberOfMarkingLines; i++) {
            double x1 = (runwayX2 - dashMarkingWidth) - displacedThresholdR;
            double y = runwayY1 + (i * distanceBetween);

            gc.strokeLine(x1, y, x1 + dashMarkingWidth, y);
        }

        // Draw the runway centre line
        gc.setLineWidth(1);
        gc.setLineDashes(7);
        double centreLineToThresholdOffset = 30;
        double centreLineX1 =
            runwayX1 + dashMarkingWidth + centreLineToThresholdOffset
                + displacedThresholdL;
        centreLineY1 = height / 2;
        double centreLineX2 =
            runwayX2 - dashMarkingWidth - centreLineToThresholdOffset
                - displacedThresholdR, centreLineY2 =
            height / 2;
        gc.strokeLine(centreLineX1, centreLineY1, centreLineX2, centreLineY2);

        // Threshold designators
        addText(tDesignator1, 18, centreLineX1 - 20, runwayY1 + 15, 90);
        addText(tDesignator2, 18, centreLineX2 + 20, runwayY2 - 15, -90);
    }

    /**
     * Paint custom canvas background for the top down-view
     */
    @Override
    protected void paintCanvasBackground() {
        // Set new size
        double width = getWidth();
        var gc = getGraphicsContext2D();

        // Draw background rectangle to join with the hexagon
        gc.setFill(Color.CORNFLOWERBLUE);
        double rectangleSize = 50; // distance from top of runway to top of rectangle
        double rectangleY1 = runwayY1 - rectangleSize;
        double rectangleHeight = runwayWidth + (rectangleSize * 2);
        gc.fillRect(0, rectangleY1, width, rectangleHeight);

        // Draw hexagon
        double gradedAreaOffset = 50; // offset from the side edges of the runway inwards
        double hexagonOffsetHeight = 50; // offset from top/bottom of rectangle outwards
        double hexagonOffsetWidth =
            gradedAreaOffset + 60; //offset from the side edges of the runway inwards
        gc.fillPolygon(
            new double[]{runwayX1 + gradedAreaOffset,
                runwayX1 + gradedAreaOffset,
                runwayX1 + hexagonOffsetWidth,
                runwayX2 - hexagonOffsetWidth,
                runwayX2 - gradedAreaOffset,
                runwayX2 - gradedAreaOffset,
                runwayX2 - hexagonOffsetWidth,
                runwayX1 + hexagonOffsetWidth},
            new double[]{rectangleY1 + rectangleHeight,
                rectangleY1,
                rectangleY1 - hexagonOffsetHeight,
                rectangleY1 - hexagonOffsetHeight,
                rectangleY1,
                rectangleY1 + rectangleHeight,
                rectangleY1 + rectangleHeight + hexagonOffsetHeight,
                rectangleY1 + rectangleHeight + hexagonOffsetHeight}, 8);
    }

    /**
     * Draw a circle representing the obstacle on the runway
     */
    @Override
    protected void drawObstacle() {
        var gc = getGraphicsContext2D();

        // Set properties
        var radius = 5;
        gc.setFill(Color.RED);

        // Draw circle
        gc.fillOval(
            runwayX1 + obstacleDistanceFromStart - radius,
            centreLineY1 - radius - obstacleDistanceFromCentreLine,
            radius * 2,
            radius * 2);
    }
}
