package com.team44.runwayredeclarationapp.view.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The abstract base class of the visualisation canvas' for the top-down and side-on views
 */
public abstract class VisualisationBase extends Canvas {

    protected static final Logger logger = LogManager.getLogger(VisualisationBase.class);

    /**
     * Runway width and height
     */
    protected double runwayHeight, runwayWidth;

    /**
     * Runway strip coordinates
     */
    protected double runwayX1, runwayX2, runwayY1, runwayY2;

    /**
     * Runway centre-line coordinate
     */
    protected double centreLineY1;

    /**
     * The actual runway length
     */
    protected double actualRunwayLength;


    /**
     * The canvas distance and actual distance of logical runway 1 distance parameters
     */
    protected double toraDistance1, toraDistanceActual1;
    protected double todaDistance1, todaDistanceActual1;
    protected double asdaDistance1, asdaDistanceActual1;
    protected double ldaDistance1, ldaDistanceActual1;


    /**
     * The canvas distance and actual distance of logical runway 2 distance parameters
     */
    protected double toraDistance2, toraDistanceActual2;
    protected double todaDistance2, todaDistanceActual2;
    protected double asdaDistance2, asdaDistanceActual2;
    protected double ldaDistance2, ldaDistanceActual2;

    /**
     * The displaced threshold of the logical runways
     */
    protected double displacedThresholdL, displacedThresholdLActual;
    protected double displacedThresholdR, displacedThresholdRActual;

    /**
     * The stopway lengths of the logical runways
     */
    protected double leftStopwayLength, leftStopwayLengthActual;
    protected double rightStopwayLength, rightStopwayLengthActual;

    /**
     * The clearway lengths of the logical runways
     */
    protected double leftClearwayLength, leftClearwayLengthActual;
    protected double rightClearwayLength, rightClearwayLengthActual;


    /**
     * The canvas distance and actual distances of the obstacle respective to the runway
     */
    protected double obstacleDistanceFromStart, obstacleDistanceFromStartActual;
    protected double obstacleDistanceFromCentreLine, obstacleDistanceFromCentreLineActual;


    /**
     * Indicating whether the obstacle is on the left (True) or the right (False)
     */
    protected Boolean isObstacleOnLeftSide;


    /**
     * The canvas value and actual values of important safety constraints that may be needed when an
     * obstacle is present
     */
    protected double resa, resaActual;
    protected double stripEnd, stripEndActual;
    protected double blastProtection, blastProtectionActual;
    protected double slope, slopeActual;

    /**
     * The canvas distance from the runway strip to the first arrow annotation, for both logical
     * runways
     */
    double arrowsFromRunwayOffset = 110;

    /**
     * Draw the background, runway and annotations to the canvas
     */
    protected abstract void paint();

    /**
     * Draw the initial annotations (arrows) when the obstacle is not present (when the runway
     * parameters has not been recalculated)
     */
    protected void drawAllArrowsInitial() {
        // Landing/takeoff to the right (logical runway 1)
        addTextArrow("TODA",
            runwayX1,
            runwayY1 - arrowsFromRunwayOffset - 60,
            runwayX2 + rightClearwayLength);
        addTextArrow("ASDA",
            runwayX1,
            runwayY1 - arrowsFromRunwayOffset - 45,
            runwayX2 + rightStopwayLength);
        addTextArrow("TORA",
            runwayX1,
            runwayY1 - arrowsFromRunwayOffset - 30,
            runwayX2);
        addTextArrow("LDA",
            runwayX1 + displacedThresholdL,
            runwayY1 - arrowsFromRunwayOffset - 15,
            runwayX2);
        if (displacedThresholdL > 0) {
            addTextArrow("DT",
                runwayX1,
                runwayY1 - arrowsFromRunwayOffset,
                runwayX1 + displacedThresholdL);
        }

        // Landing/takeoff to the left (logical runway 2)
        addTextArrow("TODA",
            runwayX2,
            runwayY2 + arrowsFromRunwayOffset + 60,
            runwayX1 - leftClearwayLength);
        addTextArrow("ASDA",
            runwayX2,
            runwayY2 + arrowsFromRunwayOffset + 45,
            runwayX1 - leftStopwayLength);
        addTextArrow("TORA",
            runwayX2,
            runwayY2 + arrowsFromRunwayOffset + 30,
            runwayX1);
        addTextArrow("LDA",
            runwayX2 - displacedThresholdR,
            runwayY2 + arrowsFromRunwayOffset + 15,
            runwayX1);
        if (displacedThresholdR > 0) {
            addTextArrow("DT",
                runwayX2,
                runwayY2 + arrowsFromRunwayOffset,
                runwayX2 - displacedThresholdR); // Displaced Right
        }
    }


    /**
     * Draw the annotations (arrows) when an obstacle is present (when the runway parameters has
     * been recalculated) and when the obstacle is placed on the left of the runway
     */
    protected void drawAllArrowsRecalculatedLeft() {
        // The x coordinate value of the obstacle
        var obstacleCoord = runwayX1 + obstacleDistanceFromStart;

        // Landing/takeoff to the right (logical runway 1)
        var blastEndCoord = obstacleCoord + blastProtection;
        addTextArrow("Blast",
            obstacleCoord,
            runwayY1 - arrowsFromRunwayOffset - 30,
            blastEndCoord);

        var slopeEndCoord = obstacleCoord + slope;
        var stripEndCoord1 = slopeEndCoord + stripEnd;
        addTextArrow("Slope",
            obstacleCoord,
            runwayY1 - arrowsFromRunwayOffset - 15,
            slopeEndCoord);
        addTextArrow("SE",
            slopeEndCoord,
            runwayY1 - arrowsFromRunwayOffset - 15,
            stripEndCoord1);

        addTextArrow("TODA",
            blastEndCoord,
            runwayY1 - arrowsFromRunwayOffset - 60,
            blastEndCoord + todaDistance1);
        addTextArrow("ASDA",
            blastEndCoord,
            runwayY1 - arrowsFromRunwayOffset - 45,
            blastEndCoord + asdaDistance1);
        addTextArrow("TORA",
            blastEndCoord,
            runwayY1 - arrowsFromRunwayOffset - 30,
            blastEndCoord + toraDistance1);
        addTextArrow("LDA",
            stripEndCoord1,
            runwayY1 - arrowsFromRunwayOffset - 15,
            stripEndCoord1 + ldaDistance1);
        if (displacedThresholdL > 0) {
            addTextArrow("DT",
                runwayX1,
                runwayY1 - arrowsFromRunwayOffset,
                runwayX1 + displacedThresholdL); // Displaced Left
        }

        // Landing/takeoff to the left (logical runway 2)
        var lda2EndCoord = runwayX2 - displacedThresholdR - ldaDistance2;
        var tora2EndCoord = runwayX2 - toraDistance2;

        addTextArrow("SE",
            lda2EndCoord,
            runwayY2 + arrowsFromRunwayOffset + 15,
            lda2EndCoord - stripEnd);
        addTextArrow("RESA",
            lda2EndCoord - stripEnd,
            runwayY2 + arrowsFromRunwayOffset + 15,
            lda2EndCoord - stripEnd - resa);

        addTextArrow("SE",
            tora2EndCoord,
            runwayY2 + arrowsFromRunwayOffset + 30,
            tora2EndCoord - stripEnd);
        addTextArrow("Slope",
            tora2EndCoord - stripEnd,
            runwayY2 + arrowsFromRunwayOffset + 30,
            tora2EndCoord - stripEnd - slope);

        addTextArrow("TODA",
            runwayX2,
            runwayY2 + arrowsFromRunwayOffset + 60,
            runwayX2 - todaDistance2);
        addTextArrow("ASDA",
            runwayX2,
            runwayY2 + arrowsFromRunwayOffset + 45,
            runwayX2 - asdaDistance2);
        addTextArrow("TORA",
            runwayX2,
            runwayY2 + arrowsFromRunwayOffset + 30,
            tora2EndCoord);
        addTextArrow("LDA",
            runwayX2 - displacedThresholdR,
            runwayY2 + arrowsFromRunwayOffset + 15,
            lda2EndCoord);
        if (displacedThresholdR > 0) {
            addTextArrow("DT",
                runwayX2,
                runwayY2 + arrowsFromRunwayOffset,
                runwayX2 - displacedThresholdR); // Displaced Right
        }
    }

    /**
     * Draw the annotations (arrows) when an obstacle is present (when the runway parameters has
     * been recalculated) and when the obstacle is placed on the right of the runway
     */
    protected void drawAllArrowsRecalculatedRight() {
        // The x coordinate value of the obstacle
        var obstacleCoord = runwayX1 + obstacleDistanceFromStart;

        // Landing/takeoff to the right (logical runway 1)
        var toraEndCoord = runwayX1 + toraDistance1;
        var stripEndCoord1 = toraEndCoord + stripEnd;
        addTextArrow("SE",
            toraEndCoord,
            runwayY1 - arrowsFromRunwayOffset - 30,
            stripEndCoord1);
        addTextArrow("Slope",
            stripEndCoord1,
            runwayY1 - arrowsFromRunwayOffset - 30,
            stripEndCoord1 + slope);

        var lda1EndCoord = runwayX1 + displacedThresholdL + ldaDistance1;
        addTextArrow("SE",
            lda1EndCoord,
            runwayY1 - arrowsFromRunwayOffset - 15,
            lda1EndCoord + stripEnd);
        addTextArrow("RESA",
            lda1EndCoord + stripEnd,
            runwayY1 - arrowsFromRunwayOffset - 15,
            lda1EndCoord + stripEnd + resa);

        addTextArrow("TODA",
            runwayX1,
            runwayY1 - arrowsFromRunwayOffset - 60,
            runwayX1 + todaDistance1);
        addTextArrow("ASDA",
            runwayX1,
            runwayY1 - arrowsFromRunwayOffset - 45,
            runwayX1 + asdaDistance1);
        addTextArrow("TORA",
            runwayX1,
            runwayY1 - arrowsFromRunwayOffset - 30,
            toraEndCoord);
        addTextArrow("LDA",
            runwayX1 + displacedThresholdL,
            runwayY1 - arrowsFromRunwayOffset - 15,
            lda1EndCoord);
        if (displacedThresholdL > 0) {
            addTextArrow("DT",
                runwayX1,
                runwayY1 - arrowsFromRunwayOffset,
                runwayX1 + displacedThresholdL); // Displaced Left
        }

        // Landing/takeoff to the left (logical runway 2)
        var stripEndStartCoord1 = obstacleCoord - slope - stripEnd;
        addTextArrow("Slope",
            obstacleCoord,
            runwayY2 + arrowsFromRunwayOffset + 15,
            obstacleCoord - slope);
        addTextArrow("SE",
            obstacleCoord - slope,
            runwayY2 + arrowsFromRunwayOffset + 15,
            stripEndStartCoord1);

        var stripEndStartCoord2 = obstacleCoord - resa - stripEnd;
        addTextArrow("RESA",
            obstacleCoord,
            runwayY2 + arrowsFromRunwayOffset + 30,
            obstacleCoord - resa);
        addTextArrow("SE",
            obstacleCoord - resa,
            runwayY2 + arrowsFromRunwayOffset + 30,
            stripEndStartCoord2);

        addTextArrow("TODA",
            stripEndStartCoord2,
            runwayY2 + arrowsFromRunwayOffset + 60,
            runwayX1 - leftClearwayLength);
        addTextArrow("ASDA",
            stripEndStartCoord2,
            runwayY2 + arrowsFromRunwayOffset + 45,
            runwayX1 - leftStopwayLength);
        addTextArrow("TORA",
            stripEndStartCoord2,
            runwayY2 + arrowsFromRunwayOffset + 30,
            runwayX1);
        addTextArrow("LDA",
            stripEndStartCoord1,
            runwayY2 + arrowsFromRunwayOffset + 15,
            stripEndStartCoord1 - ldaDistance2);
        if (displacedThresholdR > 0) {
            addTextArrow("DT",
                runwayX2,
                runwayY2 + arrowsFromRunwayOffset,
                runwayX2 - displacedThresholdR); // Displaced Right
        }
    }


    /**
     * Add an arrow with an attached text
     *
     * @param text the text for the arrow
     * @param x1   the start x coordinate
     * @param y1   the start (and finish) y coordinate
     * @param x2   the finish x coordinate
     */
    protected void addTextArrow(String text, double x1, double y1, double x2) {
        // Set how far into the arrow the text should be
        double textOnLinePercentage = 0.3; //x1 > x2 ? 0.9 : 0.1;

        // Draw the text and arrow separately
        addText(text, 12, Math.min(x1, x2) + (Math.abs(x1 - x2) * textOnLinePercentage), y1 - 3);
        addArrow(x1, y1, x2);
    }

    /**
     * Add text to the canvas
     *
     * @param text the text to add
     * @param size the size of the text
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     */
    protected void addText(String text, int size, double x, double y) {
        addText(text, size, x, y, 0);
    }

    /**
     * Add text to the canvas
     *
     * @param text   the text to add
     * @param size   the size of the text
     * @param x      the x-coordinate
     * @param y      the y-coordinate
     * @param rotate the degree to rotate by
     */
    protected void addText(String text, int size, double x, double y, double rotate) {
        var gc = getGraphicsContext2D();

        // Set properties
        gc.setLineWidth(0.7);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Helvetica", size));

        // Add text
        gc.save(); // save current state, so we don't rotate the whole canvas
        gc.translate(x, y);
        gc.rotate(rotate);
        gc.fillText(text, 0, 0);
        gc.restore(); // restore state
    }

    /**
     * Add a horizontal arrow to the canvas
     *
     * @param x1 the start x coordinate
     * @param y1 the start (and finish) y coordinate
     * @param x2 the finish x coordinate
     */
    protected void addArrow(double x1, double y1, double x2) {
        var gc = getGraphicsContext2D();

        // Set the pen properties
        double arrowPointWidth = 3;
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1);
        gc.setLineDashes();

        // Draw the arrow
        if (x2 > x1) {
            // Arrow line
            gc.strokeLine(x1, y1, x2 - arrowPointWidth, y1);
            // Arrow head
            gc.fillPolygon(
                new double[]{x2, x2 - arrowPointWidth, x2 - arrowPointWidth},
                new double[]{y1, y1 - arrowPointWidth, y1 + arrowPointWidth}, 3);
        } else {
            // Arrow line
            gc.strokeLine(x2 + arrowPointWidth, y1, x1, y1);
            // Arrow head
            gc.fillPolygon(
                new double[]{x2, x2 + arrowPointWidth, x2 + arrowPointWidth},
                new double[]{y1, y1 - arrowPointWidth, y1 + arrowPointWidth}, 3);
        }
    }

    /**
     * Add a vertical guideline to the canvas
     *
     * @param x1 the start (and finish) x coordinate
     * @param y1 the start y coordinate
     * @param y2 the finish y coordinate
     */
    protected void addGuideline(double x1, double y1, double y2) {
        var gc = getGraphicsContext2D();

        // Set the pen properties
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(0.5);
        gc.setLineDashes(5);

        // Draw the line
        gc.strokeLine(x1, y1, x1, y2);
    }


    /**
     * Set the initial runway parameters (without the obstacle)
     *
     * @param actualRunwayLength  the actual length of the runway
     * @param toraDistance1       the Take-Off Run Available for logical runway 1
     * @param todaDistance1       the Take-Off Distance Available for logical runway 1
     * @param asdaDistance1       the Accelerate-Stop Distance Available for logical runway 1
     * @param ldaDistance1        the Landing Distance Available for logical runway 1
     * @param toraDistance2       the Take-Off Run Available for logical runway 2
     * @param todaDistance2       the Take-Off Distance Available for logical runway 2
     * @param asdaDistance2       the Accelerate-Stop Distance Available for logical runway 2
     * @param ldaDistance2        the Landing Distance Available for logical runway 2
     * @param displacedThresholdL the displaced threshold for logical runway 1 (on the left)
     * @param displacedThresholdR the displaced threshold for logical runway 2 (on the right)
     * @param leftStopwayLength   the length of the left stopway
     * @param rightStopwayLength  the length of the right stopway
     * @param leftClearwayLength  the length of the left clearway
     * @param rightClearwayLength the length of the right clearway
     */
    public void setInitialParameters(
        double actualRunwayLength,
        double toraDistance1,
        double todaDistance1,
        double asdaDistance1,
        double ldaDistance1,
        double toraDistance2,
        double todaDistance2,
        double asdaDistance2,
        double ldaDistance2,
        double displacedThresholdL,
        double displacedThresholdR,
        double leftStopwayLength,
        double rightStopwayLength,
        double leftClearwayLength,
        double rightClearwayLength) {
        // Set the actual runway length
        this.actualRunwayLength = actualRunwayLength;

        // Set the runway parameter distances
        // Logical Runway 1
        this.toraDistanceActual1 = toraDistance1;
        this.todaDistanceActual1 = todaDistance1;
        this.asdaDistanceActual1 = asdaDistance1;
        this.ldaDistanceActual1 = ldaDistance1;
        this.displacedThresholdLActual = displacedThresholdL;
        this.rightStopwayLengthActual = rightStopwayLength;
        this.rightClearwayLengthActual = rightClearwayLength;

        // Logical Runway 2
        this.toraDistanceActual2 = toraDistance2;
        this.todaDistanceActual2 = todaDistance2;
        this.asdaDistanceActual2 = asdaDistance2;
        this.ldaDistanceActual2 = ldaDistance2;
        this.displacedThresholdRActual = displacedThresholdR;
        this.leftStopwayLengthActual = leftStopwayLength;
        this.leftClearwayLengthActual = leftClearwayLength;

        // Update the canvas
        paint();
    }

    /**
     * Updates all the parameters, respective to the canvas size (for scaling)
     */
    protected void updateValues() {
        // Update all the distances to be respective of the new canvas width
        // Logical Runway 1
        this.toraDistance1 = calculateRatioValue(toraDistanceActual1);
        this.todaDistance1 = calculateRatioValue(todaDistanceActual1);
        this.asdaDistance1 = calculateRatioValue(asdaDistanceActual1);
        this.ldaDistance1 = calculateRatioValue(ldaDistanceActual1);
        this.displacedThresholdL = calculateRatioValue(displacedThresholdLActual);
        this.rightStopwayLength = calculateRatioValue(rightStopwayLengthActual);
        this.rightClearwayLength = calculateRatioValue(rightClearwayLengthActual);

        // Logical Runway 2
        this.toraDistance2 = calculateRatioValue(toraDistanceActual2);
        this.todaDistance2 = calculateRatioValue(todaDistanceActual2);
        this.asdaDistance2 = calculateRatioValue(asdaDistanceActual2);
        this.ldaDistance2 = calculateRatioValue(ldaDistanceActual2);
        this.displacedThresholdR = calculateRatioValue(displacedThresholdRActual);
        this.leftStopwayLength = calculateRatioValue(leftStopwayLengthActual);
        this.leftClearwayLength = calculateRatioValue(leftClearwayLengthActual);

        // Update obstacle distance
        this.obstacleDistanceFromStart = calculateRatioValue(obstacleDistanceFromStartActual);
        this.obstacleDistanceFromCentreLine = calculateRatioValue(
            obstacleDistanceFromCentreLineActual);

        // Update the re-calculated parameters
        this.slope = calculateRatioValue(slopeActual);
        this.stripEnd = calculateRatioValue(stripEndActual);
        this.resa = calculateRatioValue(resaActual);
        this.blastProtection = calculateRatioValue(blastProtectionActual);
    }

    /**
     * Set the new re-calculated parameters
     *
     * @param toraDistance1             the Take-Off Run Available for logical runway 1
     * @param todaDistance1             the Take-Off Distance Available for logical runway 1
     * @param asdaDistance1             the Accelerate-Stop Distance Available for logical runway 1
     * @param ldaDistance1              the Landing Distance Available for logical runway 1
     * @param toraDistance2             the Take-Off Run Available for logical runway 2
     * @param todaDistance2             the Take-Off Distance Available for logical runway 2
     * @param asdaDistance2             the Accelerate-Stop Distance Available for logical runway 2
     * @param ldaDistance2              the Landing Distance Available for logical runway 2
     * @param slope                     the slope calculation
     * @param stripEnd                  the strip end value
     * @param resa                      the RESA value
     * @param blast                     the blast protection value
     * @param obstacleDistanceFromStart the distance of the obstacle from the start of the runway
     *                                  strip
     * @param isObstacleOnLeftSide      indicates whether the obstacle is on the left of the runway
     *                                  (True) or on the right (False)
     */
    public void setRecalculatedParameters(
        double toraDistance1,
        double todaDistance1,
        double asdaDistance1,
        double ldaDistance1,
        double toraDistance2,
        double todaDistance2,
        double asdaDistance2,
        double ldaDistance2,
        double slope,
        double stripEnd,
        double resa,
        double blast,
        double obstacleDistanceFromStart,
        boolean isObstacleOnLeftSide
    ) {
        // Set the new distance parameters
        // Logical Runway 1
        this.toraDistanceActual1 = toraDistance1;
        this.todaDistanceActual1 = todaDistance1;
        this.asdaDistanceActual1 = asdaDistance1;
        this.ldaDistanceActual1 = ldaDistance1;
        // Logical Runway 2
        this.toraDistanceActual2 = toraDistance2;
        this.todaDistanceActual2 = todaDistance2;
        this.asdaDistanceActual2 = asdaDistance2;
        this.ldaDistanceActual2 = ldaDistance2;

        // Set other calculation values
        this.slopeActual = slope;
        this.stripEndActual = stripEnd;
        this.resaActual = resa;
        this.blastProtectionActual = blast;

        // Set the obstacle information
        this.obstacleDistanceFromStartActual = obstacleDistanceFromStart;
        this.isObstacleOnLeftSide = isObstacleOnLeftSide;

        // Update the canvas
        paint();
    }

    /**
     * Calculate a value respective to the canvas (mainly used for lengths/distances)
     *
     * @param value the value
     * @return the new value respective to the canvas
     */
    protected double calculateRatioValue(double value) {
        // The ratio is  actual runway width:canvas runway width
        return (value / actualRunwayLength) * runwayWidth;
    }
}
