package com.team44.runwayredeclarationapp.view.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The side-on view visualisation canvas for the runway
 */
public class TopDownView extends Canvas {

    private static final Logger logger = LogManager.getLogger(TopDownView.class);

    private int toraDistance = 0;
    private int todaDistance = 0;
    private int asdaDistance = 0;
    private int ldaDistance = 0;

    /**
     * Create a new top-down view canvas of the runway
     *
     * @param width  the initial width of the canvas
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
        gc.setFill(Color.INDIGO);
        gc.fillRect(0, 0, width, height);

        // Runway cords and info:
        double runwayHeight = 80;
        double runwayX1 = width * 0.1, runwayY1 = (height / 2) - (runwayHeight / 2);
        double runwayWidth = width - (runwayX1 * 2);
        double runwayX2 = runwayX1 + runwayWidth;

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

        // Draw the runway centre line
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1);
        gc.setLineDashes(10);
        double centreLineX1 = runwayX1 + 80, centreLineY1 = height / 2;
        double centreLineX2 = width - centreLineX1, centreLineY2 = height / 2;
        gc.strokeLine(centreLineX1, centreLineY1, centreLineX2, centreLineY2);

        // Draw the runway line markings
        gc.setLineDashes();
        gc.setLineWidth(2);
        int numberOfMarkingLines = 10;
        double dashMarkingWidth = 40;
        double distanceBetween = runwayHeight / (numberOfMarkingLines + 1);
        // Left side markings
        for (int i = 1; i <= numberOfMarkingLines; i++) {
            double x1 = runwayX1 + 10;
            double y = runwayY1 + (i * distanceBetween);

            gc.strokeLine(x1, y, x1 + dashMarkingWidth, y);
        }
        // Right side markings
        for (int i = 1; i <= numberOfMarkingLines; i++) {
            double x1 = (runwayX2 - dashMarkingWidth) - 10;
            double y = runwayY1 + (i * distanceBetween);

            gc.strokeLine(x1, y, x1 + dashMarkingWidth, y);
        }

        // TESTING --
        addText("Test Text", 20, 150, 170);

        addHArrow(runwayX1, (rectangleY1 + rectangleHeight + hexagonOffsetHeight + 40),
            runwayX1 + hexagonOffsetWidth);
        addVArrow(runwayX1 + (runwayWidth * 0.7), 0, centreLineY1);
    }

    /**
     * Add text to the canvas
     *
     * @param text the text to add
     * @param size the size of the text
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     */
    private void addText(String text, int size, double x, double y) {
        var gc = getGraphicsContext2D();

        // Text
        gc.setLineWidth(0.7);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Helvetica", size));
        gc.fillText(text, x, y);
    }

    /**
     * Add a horizontal (double arrowhead) arrow to the canvas
     *
     * @param x1 the starting x point of the arrow
     * @param y1 the starting y point of the arrow
     * @param x2 the ending x point of the arrow
     */
    private void addHArrow(double x1, double y1, double x2) {
        drawArrow(x1, y1, x2, y1, false);
    }

    /**
     * Add a vertical (double arrowhead) arrow to the canvas
     *
     * @param x1 the starting x point of the arrow
     * @param y1 the starting y point of the arrow
     * @param y2 the ending y point of the arrow
     */
    private void addVArrow(double x1, double y1, double y2) {
        drawArrow(x1, y1, x1, y2, true);
    }

    /**
     * Draw an arrow to the canvas
     *
     * @param x1       the start x coordinate
     * @param y1       the start y coordinate
     * @param x2       the finish x coordinate
     * @param y2       the finish y coordinate
     * @param vertical whether the line is vertical (default is horizontal)
     */
    private void drawArrow(double x1, double y1, double x2, double y2,
        Boolean vertical) {
        var gc = getGraphicsContext2D();

        // Set the pen properties
        double arrowPointWidth = 7;
        gc.setFill(Color.WHITE);
        gc.setLineWidth(1);

        // Draw the arrow heads
        double[] arrowHeadStartXCoords, arrowHeadStartYCoords; // the starting arrowhead
        double[] arrowHeadEndXCoords, arrowHeadEndYCoords; // the ending arrowhead
        if (vertical) {
            gc.strokeLine(x1, y1 + arrowPointWidth, x2, y2 - arrowPointWidth);

            // Set the x and y coordinates for the arrow head triangles
            arrowHeadStartXCoords = new double[]{x1, x1 - arrowPointWidth, x1 + arrowPointWidth};
            arrowHeadStartYCoords = new double[]{y1, y1 + arrowPointWidth, y1 + arrowPointWidth};
            arrowHeadEndXCoords = new double[]{x2, x2 - arrowPointWidth, x2 + arrowPointWidth};
            arrowHeadEndYCoords = new double[]{y2, y2 - arrowPointWidth, y2 - arrowPointWidth};
        } else {
            gc.strokeLine(x1 + arrowPointWidth, y1, x2 - arrowPointWidth, y2);

            // Set the x and y coordinates for the arrow head triangles
            arrowHeadStartXCoords = new double[]{x1, x1 + arrowPointWidth, x1 + arrowPointWidth};
            arrowHeadStartYCoords = new double[]{y1, y1 - arrowPointWidth, y1 + arrowPointWidth};
            arrowHeadEndXCoords = new double[]{x2, x2 - arrowPointWidth, x2 - arrowPointWidth};
            arrowHeadEndYCoords = new double[]{y2, y2 - arrowPointWidth, y2 + arrowPointWidth};
        }

        // Draw both the arrowheads
        gc.fillPolygon(
            arrowHeadStartXCoords,
            arrowHeadStartYCoords, 3);
        gc.fillPolygon(
            arrowHeadEndXCoords,
            arrowHeadEndYCoords, 3);
    }

    /**
     * Update the runway parameters
     *
     * @param toraDistance the Take-Off Run Available
     * @param todaDistance the Take-Off Distance Available
     * @param asdaDistance the Accelerate-Stop Distance Available
     * @param ldaDistance  the Landing Distance Available
     */
    public void updateParameters(int toraDistance, int todaDistance, int asdaDistance,
        int ldaDistance) {
        this.toraDistance = toraDistance;
        this.todaDistance = todaDistance;
        this.asdaDistance = asdaDistance;
        this.ldaDistance = ldaDistance;

        paint();
    }
}
