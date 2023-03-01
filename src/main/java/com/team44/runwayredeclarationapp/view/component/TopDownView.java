package com.team44.runwayredeclarationapp.view.component;

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
        setWidth(width);
        setHeight(height);

        setInitialParameters(3902, 3902, 3902, 3902, 3595, 3884, 3962, 3884, 3884, 306, 0, 0, 0, 78,
            0);

        // Redraw the canvas whenever the window is resized
        widthProperty().addListener(evt -> paint());
        heightProperty().addListener(evt -> paint());

        logger.info("Initialised the Top Down View");
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
        gc.setFill(Color.DARKGREEN);
        gc.fillRect(0, 0, width, height);

        // Runway cords and info:
        this.runwayHeight = 55;
        this.runwayX1 = width * 0.15;
        this.runwayY1 = (height / 2) - (runwayHeight / 2);
        this.runwayWidth = width - (runwayX1 * 2);
        this.runwayX2 = runwayX1 + runwayWidth;
        this.runwayY2 = runwayY1 + runwayHeight;
        updateValues();

        // Draw background rectangle to join with the hexagon
        gc.setFill(Color.CORNFLOWERBLUE);
        double rectangleSize = 50; // distance from top of runway to top of rectangle
        double rectangleY1 = runwayY1 - rectangleSize;
        double rectangleHeight = runwayHeight + (rectangleSize * 2);
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

        // Draw the runway strip
        gc.setFill(Color.DIMGRAY);
        gc.fillRect(runwayX1, runwayY1, runwayWidth, runwayHeight);

        // Draw the runway line markings
        gc.setStroke(Color.WHITE);
        gc.setLineDashes();
        gc.setLineWidth(1.6);
        int numberOfMarkingLines = 10;
        double dashMarkingWidth = 30;
        double distanceBetween = runwayHeight / (numberOfMarkingLines + 1);
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

        // Left side stopway
        gc.setFill(Color.web("2e2e2e"));
        gc.fillRect(runwayX1 - leftStopwayLength, runwayY1, leftStopwayLength, runwayHeight);

        // Right side stopway
        gc.fillRect(runwayX2, runwayY1, rightStopwayLength, runwayHeight);

        // Clearway
        gc.setLineDashes();
        gc.strokeRect(runwayX1 - leftClearwayLength, runwayY1 - 15, leftClearwayLength,
            runwayHeight + 30);
        gc.strokeRect(runwayX2, runwayY1 - 15, rightClearwayLength, runwayHeight + 30);

        // todo:: make these class variables
        addText("09L", 18, centreLineX1 - 20, runwayY1 + 15, 90);
        addText("27R", 18, centreLineX2 + 20, runwayY2 - 15, -90);

        // Draw initial arrows and guidelines
        if (isObstacleOnLeftSide == null) {
            drawAllArrowsInitial();
            drawAllGuidelinesInitial();
        } else if (isObstacleOnLeftSide) {
            drawAllArrowsRecalculatedLeft();
            drawAllGuidelinesRecalculatedLeft();
            drawObstacle();
        } else {
            drawAllArrowsRecalculatedRight();
            drawAllGuidelinesRecalculatedRight();
            drawObstacle();
        }

    }


    /**
     * Draw the initial guidelines (when there is no obstacle)
     */
    private void drawAllGuidelinesInitial() {
        // Top guidelines (logical runway 1)
        addGuideline(runwayX1, runwayY1, runwayY1 - arrowsFromRunwayOffset - 60);
        addGuideline(runwayX2, runwayY1, runwayY1 - arrowsFromRunwayOffset - 60);
        if (displacedThresholdL > 0) {
            addGuideline(runwayX1 + displacedThresholdL, runwayY1,
                runwayY1 - arrowsFromRunwayOffset - 15);
        }

        // Bottom guidelines (logical runway 2)
        addGuideline(runwayX1, runwayY2, runwayY2 + arrowsFromRunwayOffset + 60);
        addGuideline(runwayX2, runwayY2, runwayY2 + arrowsFromRunwayOffset + 60);
        if (displacedThresholdR > 0) {
            addGuideline(runwayX2 - displacedThresholdR, runwayY2,
                runwayY2 + arrowsFromRunwayOffset + 15);
        }
    }

    /**
     * Draw guidelines for when the parameters have been re-calculated and the obstacle is on the
     * left
     */
    private void drawAllGuidelinesRecalculatedLeft() {
        // todo :: add these to base class with parameters for x1
        var obstacleCoord = runwayX1 + obstacleDistanceFromStart;
        addGuideline(obstacleCoord, 0, getHeight());
        addGuideline(runwayX1 + obstacleDistanceFromStart + slope,
            runwayY1 - arrowsFromRunwayOffset - 15, runwayY1);
        addGuideline(runwayX1 + obstacleDistanceFromStart + slope + stripEnd,
            runwayY1 - arrowsFromRunwayOffset - 15, runwayY1);
    }

    /**
     * Draw guidelines for when the parameters have been re-calculated and the obstacle is on the
     * right
     */
    private void drawAllGuidelinesRecalculatedRight() {
        // todo
    }

    /**
     * Draw a circle representing the obstacle on the runway
     */
    private void drawObstacle() {
        var gc = getGraphicsContext2D();

        // Set properties
        var radius = 5;
        gc.setFill(Color.RED);

        // Draw circle
        gc.fillOval(runwayX1 + obstacleDistanceFromStart - radius, centreLineY1, radius * 2,
            radius * 2);
    }
}
