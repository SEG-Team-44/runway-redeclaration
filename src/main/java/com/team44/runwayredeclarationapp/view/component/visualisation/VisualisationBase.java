package com.team44.runwayredeclarationapp.view.component.visualisation;

import com.team44.runwayredeclarationapp.model.Coord;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.RunwayObstacle;
import com.team44.runwayredeclarationapp.model.theme.ColourTheme;
import java.util.HashMap;
import java.util.HashSet;
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
     * Indicating what screen the user is on
     */
    protected boolean isLoadingScreen = true;
    protected boolean isObstacleScreen = false;
    protected boolean isShowValues = false;
    protected boolean isWhiteArrow = false;
    protected boolean isShowKey = true;
    protected boolean isRotateCompass = false;
    protected boolean isThresholdSwitched = false;

    /**
     * The runway and obstacle shown on the visualisation
     */
    protected Runway runway, originalRunway, switchedRunway;
    protected RunwayObstacle runwayObstacle;

    /**
     * Runway width and height
     */
    protected double runwayWidth, runwayLength;

    /**
     * Runway Threshold Designators and degrees
     */
    protected String tDesignator1, degree1, tDesignator2, degree2;

    /**
     * Runway strip coordinates
     */
    protected double runwayX1, runwayX2, runwayY1, runwayY2;

    /**
     * Runway centre-line coordinate
     */
    protected double centreLineY1;

    /**
     * The actual runway length and width
     */
    protected double actualRunwayLength, actualRunwayWidth;


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
    protected double obstacleHeight, obstacleHeightActual;


    /**
     * Indicating whether the obstacle is on the left (True) or the right (False)
     */
    protected Boolean isObstacleOnLeftSide, isObstacleOnLeftSideActual;


    /**
     * The canvas value and actual values of important safety constraints that may be needed when an
     * obstacle is present
     */
    protected double resa, resaActual;
    protected double stripEnd, stripEndActual;
    protected double blastProtection, blastProtectionActual;
    protected double slope, slopeActual;

    /**
     * The TOCS coordinates
     */
    protected double tocsStartX, tocsStartY, tocsEndX, tocsEndY;

    /**
     * The canvas distance from the runway strip to the first arrow annotation, for both logical
     * runways
     */
    protected double arrowsFromRunwayOffset = 50;

    /**
     * The gaps between each arrow on the visualisation
     */
    protected double arrowsGapBetween = 25;

    /**
     * The coordinates for the guidelines for both logical runways
     */
    protected HashSet<Coord> guideLineCoordsUp = new HashSet<>();
    protected HashSet<Coord> guideLineCoordsDown = new HashSet<>();

    /**
     * The colour theme of the visualisation
     */
    protected ColourTheme colourTheme = new ColourTheme();

    /**
     * Create a visualisation canvas for the runway
     *
     * @param width  the width of the canvas
     * @param height the height of the canvas
     */
    public VisualisationBase(double width, double height) {
        setWidth(width);
        setHeight(height);

        // Redraw the canvas whenever the window is resized
        widthProperty().addListener(evt -> paint());
        heightProperty().addListener(evt -> paint());
    }

    /**
     * Paint the canvas to visualise the runway
     */
    protected void paint() {
        // Reset previous coordinates
        guideLineCoordsUp.clear();
        guideLineCoordsDown.clear();

        // Load loading screen if necessary
        if (isLoadingScreen) {
            paintLoadingScreen();
            return;
        }

        // Set new size
        double width = getWidth();
        double height = getHeight();
        var gc = getGraphicsContext2D();

        // Clear the current canvas
        gc.clearRect(0, 0, width, height);

        gc.save();
        gc.translate(getWidth() / 2, getHeight() / 2);
        gc.rotate(isRotateCompass ? (Double.parseDouble(degree1) * 10 - 90) : 0);
        gc.translate(-getWidth() / 2, -getHeight() / 2);

        // Draw the new canvas
        gc.setFill(colourTheme.getTopDownBackground());
        gc.fillRect(-width, -height, width * 3, height * 3);

        // Runway cords and info:
        this.runwayX1 = width * 0.1;
        this.runwayY1 = (height / 2) - (runwayWidth / 2);
        this.runwayLength = width - (runwayX1 * 2);
        this.runwayX2 = runwayX1 + runwayLength;
        this.runwayY2 = runwayY1 + runwayWidth;
        updateValues();

        // Paint the canvas background (for separate subclasses)
        paintCanvasBackground();

        // Draw the runway strip
        gc.setFill(colourTheme.getRunwayStrip());
        gc.fillRect(runwayX1, runwayY1, runwayLength, runwayWidth);

        // Left side stopway
        gc.setFill(colourTheme.getStopway());
        gc.fillRect(runwayX1 - leftStopwayLength, runwayY1, leftStopwayLength, runwayWidth);

        // Right side stopway
        gc.fillRect(runwayX2, runwayY1, rightStopwayLength, runwayWidth);

        // Paint custom canvas features (for separate subclasses)
        paintCanvasCustom();

        // Draw initial arrows and the obstacle if necessary
        if (!isObstacleScreen) {
            drawAllArrowsInitial();
        } else if (isObstacleOnLeftSide) {
            drawAllArrowsRecalculatedLeft();
            drawObstacle();
            drawTOCSandALS();
        } else {
            drawAllArrowsRecalculatedRight();
            drawObstacle();
            drawTOCSandALS();
        }

        // Draw the guidelines
        drawGuidelines();

        gc.restore();

        // Draw the take-off/landing direction and text
        drawTakeOffLandingDirection();

        // Draw key
        if (isShowKey) {
            drawColourKey();
        }
    }

    protected void drawTOCSandALS() {
    }

    /**
     * Paint custom canvas features for different visualisation views
     */
    protected abstract void paintCanvasCustom();

    /**
     * Paint custom canvas background for different visualisation views
     */
    protected abstract void paintCanvasBackground();

    /**
     * Draw the obstacle
     */
    protected abstract void drawObstacle();

    /**
     * Draw the loading screen to the canvas
     */
    protected void paintLoadingScreen() {
        // Set new size
        double width = getWidth();
        double height = getHeight();
        var gc = getGraphicsContext2D();

        // Clear the current canvas
        gc.clearRect(0, 0, width, height);

        // Draw the new canvas
        gc.setFill(colourTheme.getLoadingScreen());
        gc.fillRect(0, 0, width, height);

        // Add the loading message
        addText("Load a runway to see the visualisation.", 16, (width / 2) - 120, (height / 2));
    }

    /**
     * Reset the canvas back to the loading screen
     */
    public void reset() {
        isLoadingScreen = true;
        paintLoadingScreen();
    }

    /**
     * Draw guidelines on the canvas, relative to the runway and parameters
     */
    protected void drawGuidelines() {
        // Dict for the coordinate x-values
        var mapUp = new HashMap<Double, Coord>();
        var mapDown = new HashMap<Double, Coord>();

        // Add the top guidelines to the map
        guideLineCoordsUp.forEach((crd) -> {
            // Only add the lowest y-value coordinate
            if (!mapUp.containsKey(crd.getX()) || crd.getY() < mapUp.get(crd.getX()).getY()) {
                mapUp.put(crd.getX(), crd);
            }
        });

        // Add the bottom guidelines to the map
        guideLineCoordsDown.forEach((crd) -> {
            // Only add the highest y-value coordinate
            if (!mapDown.containsKey(crd.getX()) || crd.getY() > mapDown.get(crd.getX()).getY()) {
                mapDown.put(crd.getX(), crd);
            }
        });

        // Draw the guideline for each of the coordinates
        mapUp.values().forEach((crd) -> addGuideline(crd.getX(), crd.getY(), runwayY1));
        mapDown.values().forEach((crd) -> addGuideline(crd.getX(), crd.getY(), runwayY2));
    }

    /**
     * Draw the colour key on the visualisation
     */
    protected void drawColourKey() {
        var gc = getGraphicsContext2D();
        gc.setGlobalAlpha(0.25);

        // Specify colour key box properties
        var boxHeight = 55;
        var boxWidth = getWidth() / 3;
        var boxX = getHeight() - boxHeight;
        var keyBoxOffset = 6;

        // Draw the box
        gc.setFill(colourTheme.getColourKeyBox());
        gc.fillRect(0, boxX, boxWidth, boxHeight);

        // Reset properties
        gc.setGlobalAlpha(1);

        // Draw key boxes
        // Stopway
        gc.setFill(colourTheme.getStopway());
        gc.fillRect(10, boxX + keyBoxOffset, 15, 10);
        // Clearway
        gc.setStroke(colourTheme.getClearway());
        gc.strokeRect(10, boxX + keyBoxOffset + 15, 15, 10);

        // Add the key texts
        addText("Stopway", 13, 40, boxX + 15);
        addText("Clearway", 13, 40, boxX + 30);

        // Add the custom keys for the visualisation
        paintKeyCustom(boxX + 45, boxX + keyBoxOffset + 30);
    }


    /**
     * Paint custom additional colour keys
     *
     * @param nextTextX   the x coordinate of the next key text
     * @param nextKeyBoxX the x coordinate of the next key box
     */
    protected void paintKeyCustom(double nextTextX, double nextKeyBoxX) {
    }

    /**
     * Draw the take-off/landing direction for both logical runways
     */
    protected void drawTakeOffLandingDirection() {
        // Top arrow
        addArrow(5, 30, 70, 10, colourTheme.getArrow());
        addText("Landing and Take-off in this direction", 13, 5, 15);

        // Bottom arrow
        addArrow(getWidth() - 25, getHeight() - 30, getWidth() - 90, 10, colourTheme.getArrow());
        addText("Landing and Take-off in this direction", 13, getWidth() - 230,
            getHeight() - 8);
    }

    /**
     * Set whether to show the distance values on the canvas
     *
     * @param showValues whether to show the values
     */
    public void setShowValues(boolean showValues) {
        isShowValues = showValues;
        paint();
    }

    /**
     * Set whether to change arrow colours to white
     *
     * @param arrowColour whether to change arrow colours to white
     */
    public void setWhiteArrow(boolean arrowColour) {
        isWhiteArrow = arrowColour;
        paint();
    }

    /**
     * Create a string with the text and value side by side
     *
     * @param text  the text
     * @param value the value
     * @return the full string together
     */
    protected String createValueText(String text, double value) {
        // return with value if necessary
        return isShowValues ? text + " (" + value + "m)" : text;
    }

    /**
     * Draw the initial annotations (arrows) when the obstacle is not present (when the runway
     * parameters has not been recalculated)
     */
    protected void drawAllArrowsInitial() {
        // Landing/takeoff to the right (logical runway 1)
        addTextArrow(createValueText("TODA", todaDistanceActual1),
            runwayX1,
            runwayY1 - arrowsFromRunwayOffset - (arrowsGapBetween * 4),
            runwayX2 + rightClearwayLength, colourTheme.getTODAarrow());
        addTextArrow(createValueText("ASDA", asdaDistanceActual1),
            runwayX1,
            runwayY1 - arrowsFromRunwayOffset - (arrowsGapBetween * 3),
            runwayX2 + rightStopwayLength, colourTheme.getASDAarrow());
        addTextArrow(createValueText("TORA", toraDistanceActual1),
            runwayX1,
            runwayY1 - arrowsFromRunwayOffset - (arrowsGapBetween * 2),
            runwayX2, colourTheme.getTORAarrow());
        addTextArrow(createValueText("LDA", ldaDistanceActual1),
            runwayX1 + displacedThresholdL,
            runwayY1 - arrowsFromRunwayOffset - arrowsGapBetween,
            runwayX2, colourTheme.getLDAarrow());
        if (displacedThresholdL > 0) {
            addTextArrow(createValueText("DT", displacedThresholdLActual),
                runwayX1,
                runwayY1 - arrowsFromRunwayOffset,
                runwayX1 + displacedThresholdL, colourTheme.getDTarrow());
        }

        // Landing/takeoff to the left (logical runway 2)
        addTextArrow(createValueText("TODA", todaDistanceActual2),
            runwayX2,
            runwayY2 + arrowsFromRunwayOffset + (arrowsGapBetween * 4),
            runwayX1 - leftClearwayLength, colourTheme.getTODAarrow());
        addTextArrow(createValueText("ASDA", asdaDistanceActual2),
            runwayX2,
            runwayY2 + arrowsFromRunwayOffset + (arrowsGapBetween * 3),
            runwayX1 - leftStopwayLength, colourTheme.getASDAarrow());
        addTextArrow(createValueText("TORA", toraDistanceActual2),
            runwayX2,
            runwayY2 + arrowsFromRunwayOffset + (arrowsGapBetween * 2),
            runwayX1, colourTheme.getTORAarrow());
        addTextArrow(createValueText("LDA", ldaDistanceActual2),
            runwayX2 - displacedThresholdR,
            runwayY2 + arrowsFromRunwayOffset + arrowsGapBetween,
            runwayX1, colourTheme.getLDAarrow());
        if (displacedThresholdR > 0) {
            addTextArrow(createValueText("DT", displacedThresholdRActual),
                runwayX2,
                runwayY2 + arrowsFromRunwayOffset,
                runwayX2 - displacedThresholdR, colourTheme.getDTarrow()); // Displaced Right
        }
    }


    /**
     * Draw the annotations (arrows) when an obstacle is present (when the runway parameters has
     * been recalculated) and when the obstacle is placed on the left of the runway
     */
    protected void drawAllArrowsRecalculatedLeft() {
        // The x coordinate value of the obstacle
        var obstacleCoord = runwayX1 + obstacleDistanceFromStart;

        //Set TOCS/ALS start and end X & Y coordinates
        tocsStartX = runwayX1 + obstacleDistanceFromStart + slope;
        tocsStartY = runwayY1;
        tocsEndX = runwayX1 + obstacleDistanceFromStart;
        tocsEndY = runwayY1 - obstacleHeight;

        // Landing/takeoff to the right (logical runway 1)
        // Show blast if it's greater than RESA + SE
        double blastEndCoord;
        if (blastProtectionActual < (resaActual + stripEndActual)) {
            blastEndCoord = obstacleCoord + resa + stripEnd;

            addTextArrow(createValueText("SE", stripEndActual),
                obstacleCoord + resa,
                runwayY1 - arrowsFromRunwayOffset,
                obstacleCoord + resa + stripEnd, colourTheme.getSEarrow());
            addTextArrow(createValueText("RESA", resaActual),
                obstacleCoord,
                runwayY1 - arrowsFromRunwayOffset - (arrowsGapBetween * 2),
                obstacleCoord + resa, colourTheme.getRESAarrow());
        } else {
            blastEndCoord = obstacleCoord + blastProtection;

            addTextArrow(createValueText("Blast", blastProtectionActual),
                obstacleCoord,
                runwayY1 - arrowsFromRunwayOffset - (arrowsGapBetween * 2),
                blastEndCoord, colourTheme.getBlastarrow());
        }

        var slopeEndCoord = obstacleCoord + slope;

        var stripEndCoord1 = slopeEndCoord + stripEnd;
        addTextArrow(createValueText("Slope", slopeActual),
            obstacleCoord,
            runwayY1 - arrowsFromRunwayOffset - arrowsGapBetween,
            slopeEndCoord, colourTheme.getSlopearrow());
        addTextArrow(createValueText("SE", stripEndActual),
            slopeEndCoord,
            runwayY1 - arrowsFromRunwayOffset,
            stripEndCoord1, colourTheme.getSEarrow());

        addTextArrow(createValueText("TODA", todaDistanceActual1),
            blastEndCoord,
            runwayY1 - arrowsFromRunwayOffset - (arrowsGapBetween * 4),
            blastEndCoord + todaDistance1, colourTheme.getTODAarrow());
        addTextArrow(createValueText("ASDA", asdaDistanceActual1),
            blastEndCoord,
            runwayY1 - arrowsFromRunwayOffset - (arrowsGapBetween * 3),
            blastEndCoord + asdaDistance1, colourTheme.getASDAarrow());
        addTextArrow(createValueText("TORA", toraDistanceActual1),
            blastEndCoord,
            runwayY1 - arrowsFromRunwayOffset - (arrowsGapBetween * 2),
            blastEndCoord + toraDistance1, colourTheme.getTORAarrow());
        addTextArrow(createValueText("LDA", ldaDistanceActual1),
            stripEndCoord1,
            runwayY1 - arrowsFromRunwayOffset - arrowsGapBetween,
            stripEndCoord1 + ldaDistance1, colourTheme.getLDAarrow());
        if (displacedThresholdL > 0) {
            addTextArrow(createValueText("DT", displacedThresholdLActual),
                runwayX1,
                runwayY1 - arrowsFromRunwayOffset,
                runwayX1 + displacedThresholdL, colourTheme.getDTarrow()); // Displaced Left
        }

        // Landing/takeoff to the left (logical runway 2)
        var lda2EndCoord = runwayX2 - displacedThresholdR - ldaDistance2;
        var tora2EndCoord = runwayX2 - toraDistance2;

        addTextArrow(createValueText("SE", stripEndActual),
            lda2EndCoord,
            runwayY2 + arrowsFromRunwayOffset,
            lda2EndCoord - stripEnd, colourTheme.getSEarrow());
        addTextArrow(createValueText("RESA", resaActual),
            lda2EndCoord - stripEnd,
            runwayY2 + arrowsFromRunwayOffset + arrowsGapBetween,
            lda2EndCoord - stripEnd - resa, colourTheme.getRESAarrow());

        addTextArrow(createValueText("SE", stripEndActual),
            tora2EndCoord,
            runwayY2 + arrowsFromRunwayOffset,
            tora2EndCoord - stripEnd, colourTheme.getSEarrow());
        addTextArrow(createValueText("Slope", slopeActual),
            tora2EndCoord - stripEnd,
            runwayY2 + arrowsFromRunwayOffset + (arrowsGapBetween * 2),
            tora2EndCoord - stripEnd - slope, colourTheme.getSlopearrow());

        addTextArrow(createValueText("TODA", todaDistanceActual2),
            runwayX2,
            runwayY2 + arrowsFromRunwayOffset + (arrowsGapBetween * 4),
            runwayX2 - todaDistance2, colourTheme.getTODAarrow());
        addTextArrow(createValueText("ASDA", asdaDistanceActual2),
            runwayX2,
            runwayY2 + arrowsFromRunwayOffset + (arrowsGapBetween * 3),
            runwayX2 - asdaDistance2, colourTheme.getASDAarrow());
        addTextArrow(createValueText("TORA", toraDistanceActual2),
            runwayX2,
            runwayY2 + arrowsFromRunwayOffset + (arrowsGapBetween * 2),
            tora2EndCoord, colourTheme.getTORAarrow());
        addTextArrow(createValueText("LDA", ldaDistanceActual2),
            runwayX2 - displacedThresholdR,
            runwayY2 + arrowsFromRunwayOffset + arrowsGapBetween,
            lda2EndCoord, colourTheme.getLDAarrow());
        if (displacedThresholdR > 0) {
            addTextArrow(createValueText("DT", displacedThresholdRActual),
                runwayX2,
                runwayY2 + arrowsFromRunwayOffset,
                runwayX2 - displacedThresholdR, colourTheme.getDTarrow()); // Displaced Right
        }
    }

    /**
     * Draw the annotations (arrows) when an obstacle is present (when the runway parameters has
     * been recalculated) and when the obstacle is placed on the right of the runway
     */
    protected void drawAllArrowsRecalculatedRight() {
        // The x coordinate value of the obstacle
        var obstacleCoord = runwayX1 + obstacleDistanceFromStart;

        //Set TOCS/ALS start and end X & Y coordinates
        tocsStartX = runwayX1 + obstacleDistanceFromStart - slope;
        tocsStartY = runwayY1;
        tocsEndX = runwayX1 + obstacleDistanceFromStart;
        tocsEndY = runwayY1 - obstacleHeight;

        // Landing/takeoff to the right (logical runway 1)
        var toraEndCoord = runwayX1 + toraDistance1;
        var stripEndCoord1 = toraEndCoord + stripEnd;
        addTextArrow(createValueText("SE", stripEndActual),
            toraEndCoord,
            runwayY1 - arrowsFromRunwayOffset - (arrowsGapBetween * 2),
            stripEndCoord1, colourTheme.getSEarrow());
        addTextArrow(createValueText("Slope", slopeActual),
            stripEndCoord1,
            runwayY1 - arrowsFromRunwayOffset - (arrowsGapBetween * 3),
            stripEndCoord1 + slope, colourTheme.getSlopearrow());

        var lda1EndCoord = runwayX1 + displacedThresholdL + ldaDistance1;
        addTextArrow(createValueText("SE", stripEndActual),
            lda1EndCoord,
            runwayY1 - arrowsFromRunwayOffset - (arrowsGapBetween * 2),
            lda1EndCoord + stripEnd, colourTheme.getSEarrow());
        addTextArrow(createValueText("RESA", resaActual),
            lda1EndCoord + stripEnd,
            runwayY1 - arrowsFromRunwayOffset - arrowsGapBetween,
            lda1EndCoord + stripEnd + resa, colourTheme.getRESAarrow());

        addTextArrow(createValueText("TODA", todaDistanceActual1),
            runwayX1,
            runwayY1 - arrowsFromRunwayOffset - (arrowsGapBetween * 4),
            runwayX1 + todaDistance1, colourTheme.getTODAarrow());
        addTextArrow(createValueText("ASDA", asdaDistanceActual1),
            runwayX1,
            runwayY1 - arrowsFromRunwayOffset - (arrowsGapBetween * 3),
            runwayX1 + asdaDistance1, colourTheme.getASDAarrow());
        addTextArrow(createValueText("TORA", toraDistanceActual1),
            runwayX1,
            runwayY1 - arrowsFromRunwayOffset - (arrowsGapBetween * 2),
            toraEndCoord, colourTheme.getTORAarrow());
        addTextArrow(createValueText("LDA", ldaDistanceActual1),
            runwayX1 + displacedThresholdL,
            runwayY1 - arrowsFromRunwayOffset - arrowsGapBetween,
            lda1EndCoord, colourTheme.getLDAarrow());
        if (displacedThresholdL > 0) {
            addTextArrow(createValueText("DT", displacedThresholdLActual),
                runwayX1,
                runwayY1 - arrowsFromRunwayOffset,
                runwayX1 + displacedThresholdL, colourTheme.getDTarrow()); // Displaced Left
        }

        // Landing/takeoff to the left (logical runway 2)
        var stripEndStartCoord1 = obstacleCoord - slope - stripEnd;
        addTextArrow(createValueText("Slope", slopeActual),
            obstacleCoord,
            runwayY2 + arrowsFromRunwayOffset + arrowsGapBetween,
            obstacleCoord - slope, colourTheme.getSlopearrow());
        addTextArrow(createValueText("SE", stripEndActual),
            obstacleCoord - slope,
            runwayY2 + arrowsFromRunwayOffset,
            stripEndStartCoord1, colourTheme.getSEarrow());

        double stripEndStartCoord2;
        // Show blast if it's greater than RESA + SE
        if (blastProtectionActual < (resaActual + stripEndActual)) {
            stripEndStartCoord2 = obstacleCoord - resa - stripEnd;

            addTextArrow(createValueText("RESA", resaActual),
                obstacleCoord,
                runwayY2 + arrowsFromRunwayOffset + (arrowsGapBetween * 2),
                obstacleCoord - resa, colourTheme.getRESAarrow());
            addTextArrow(createValueText("SE", stripEndActual),
                obstacleCoord - resa,
                runwayY2 + arrowsFromRunwayOffset,
                stripEndStartCoord2, colourTheme.getSEarrow());
        } else {
            stripEndStartCoord2 = obstacleCoord - blastProtection;

            addTextArrow(createValueText("Blast", blastProtectionActual),
                obstacleCoord,
                runwayY2 + arrowsFromRunwayOffset + (arrowsGapBetween * 2),
                obstacleCoord - blastProtection, colourTheme.getBlastarrow());
        }

        addTextArrow(createValueText("TODA", todaDistanceActual1),
            stripEndStartCoord2,
            runwayY2 + arrowsFromRunwayOffset + (arrowsGapBetween * 4),
            runwayX1 - leftClearwayLength, colourTheme.getTODAarrow());
        addTextArrow(createValueText("ASDA", asdaDistanceActual2),
            stripEndStartCoord2,
            runwayY2 + arrowsFromRunwayOffset + (arrowsGapBetween * 3),
            runwayX1 - leftStopwayLength, colourTheme.getASDAarrow());
        addTextArrow(createValueText("TORA", toraDistanceActual2),
            stripEndStartCoord2,
            runwayY2 + arrowsFromRunwayOffset + (arrowsGapBetween * 2),
            runwayX1, colourTheme.getTORAarrow());
        addTextArrow(createValueText("LDA", ldaDistanceActual2),
            stripEndStartCoord1,
            runwayY2 + arrowsFromRunwayOffset + arrowsGapBetween,
            stripEndStartCoord1 - ldaDistance2, colourTheme.getLDAarrow());
        if (displacedThresholdR > 0) {
            addTextArrow(createValueText("DT", displacedThresholdRActual),
                runwayX2,
                runwayY2 + arrowsFromRunwayOffset,
                runwayX2 - displacedThresholdR, colourTheme.getDTarrow()); // Displaced Right
        }
    }


    /**
     * Add an arrow with an attached text
     *
     * @param text  the text for the arrow
     * @param x1    the start x coordinate
     * @param y1    the start (and finish) y coordinate
     * @param x2    the finish x coordinate
     * @param color the colour of the text and arrow
     */
    protected void addTextArrow(String text, double x1, double y1, double x2, Color color) {
        // Set how far into the arrow the text should be
        double textOnLinePercentage = x1 > x2 ? 0.5 : 0.1;

        // Draw the text and arrow separately
        addText(text, 12, Math.min(x1, x2) + (Math.abs(x1 - x2) * textOnLinePercentage), y1 - 3,
            color);
        addArrow(x1, y1, x2, color);

        // Add the coordinates to the array of guideline coords
        if (x2 > x1) {
            guideLineCoordsUp.add(new Coord(x1, y1));
            guideLineCoordsUp.add(new Coord(x2, y1));
        } else {
            guideLineCoordsDown.add(new Coord(x1, y1));
            guideLineCoordsDown.add(new Coord(x2, y1));
        }
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
        addText(text, size, x, y, 0, colourTheme.getText());
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
        addText(text, size, x, y, rotate, colourTheme.getText());
    }

    /**
     * Add text to the canvas
     *
     * @param text  the text to add
     * @param size  the size of the text
     * @param x     the x-coordinate
     * @param y     the y-coordinate
     * @param color the colour of the text
     */
    protected void addText(String text, int size, double x, double y, Color color) {
        addText(text, size, x, y, 0, color);
    }

    /**
     * Add text to the canvas
     *
     * @param text   the text to add
     * @param size   the size of the text
     * @param x      the x-coordinate
     * @param y      the y-coordinate
     * @param rotate the degree to rotate by
     * @param color  the colour of the text
     */
    protected void addText(String text, int size, double x, double y, double rotate, Color color) {
        var gc = getGraphicsContext2D();

        // Set properties
        gc.setLineWidth(0.7);
        if (isWhiteArrow) {
            gc.setFill(Color.WHITE);
        } else {
            gc.setFill(color);
        }
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
        addArrow(x1, y1, x2, 3, colourTheme.getArrow());
    }

    /**
     * Add a horizontal arrow to the canvas
     *
     * @param x1    the start x coordinate
     * @param y1    the start (and finish) y coordinate
     * @param x2    the finish x coordinate
     * @param color
     */
    protected void addArrow(double x1, double y1, double x2, Color color) {
        addArrow(x1, y1, x2, 3, color);
    }

    /**
     * Add a horizontal arrow to the canvas
     *
     * @param x1              the start x coordinate
     * @param y1              the start (and finish) y coordinate
     * @param x2              the finish x coordinate
     * @param arrowPointWidth the width of the arrowhead
     * @param color           the colour of the arrow
     */
    protected void addArrow(double x1, double y1, double x2, double arrowPointWidth,
        Color color) {
        var gc = getGraphicsContext2D();

        // Set the pen properties
        if (isWhiteArrow) {
            gc.setFill(Color.WHITE);
            gc.setStroke(Color.WHITE);
        } else {
            gc.setFill(color);
            gc.setStroke(color);
        }
        gc.setLineWidth(arrowPointWidth / 3);
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
        gc.setStroke(colourTheme.getGuideline());
        gc.setLineWidth(0.5);
        gc.setLineDashes(5);

        // Draw the line
        gc.strokeLine(x1, y1, x1, y2);
    }

    /**
     * Set the initial runway parameters (without the obstacle)
     *
     * @param runway the runway object
     */
    public void setInitialParameters(Runway runway) {
        this.runway = runway;
        this.originalRunway = runway;
        this.switchedRunway = runway.getSwitchedThresholdRunway();

        // Switch the runway if necessary
        if (isThresholdSwitched) {
            runway = runway.getSwitchedThresholdRunway();
        }

        // Get the 2 logical runway ID's
        var runway1ID = runway.getLogicId1();
        var runway2ID = runway.getLogicId2();

        // Send the data to the other overloaded method
        setInitialParameters(
            runway.getRunwayL(),
            runway.getRunwayW(),
            runway1ID,
            Runway.getDegreeInString(runway.getDegree1()),
            runway.getTora(runway1ID),
            runway.getToda(runway1ID),
            runway.getAsda(runway1ID),
            runway.getLda(runway1ID),
            runway2ID,
            Runway.getDegreeInString(runway.getDegree2()),
            runway.getTora(runway2ID),
            runway.getToda(runway2ID),
            runway.getAsda(runway2ID),
            runway.getLda(runway2ID),
            runway.getDisThresh(runway2ID),
            runway.getDisThresh(runway1ID),
            runway.getStopwayL(runway2ID),
            runway.getStopwayL(runway1ID),
            runway.getClearwayL(runway2ID),
            runway.getClearwayL(runway1ID)
        );
    }

    /**
     * Set the initial runway parameters (without the obstacle)
     *
     * @param actualRunwayLength  the actual length of the runway
     * @param actualRunwayWidth   the actual width of the runway
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
    protected void setInitialParameters(
        double actualRunwayLength,
        double actualRunwayWidth,
        String tDesignator1,
        String degree1,
        double toraDistance1,
        double todaDistance1,
        double asdaDistance1,
        double ldaDistance1,
        String tDesignator2,
        String degree2,
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
        this.actualRunwayWidth = actualRunwayWidth;

        // Set the runway parameter distances
        // Logical Runway 1
        this.tDesignator1 = tDesignator1;
        this.degree1 = degree1;
        this.toraDistanceActual1 = toraDistance1;
        this.todaDistanceActual1 = todaDistance1;
        this.asdaDistanceActual1 = asdaDistance1;
        this.ldaDistanceActual1 = ldaDistance1;
        this.displacedThresholdLActual = displacedThresholdL;
        this.rightStopwayLengthActual = rightStopwayLength;
        this.rightClearwayLengthActual = rightClearwayLength;

        // Logical Runway 2
        this.tDesignator2 = tDesignator2;
        this.degree2 = degree2;
        this.toraDistanceActual2 = toraDistance2;
        this.todaDistanceActual2 = todaDistance2;
        this.asdaDistanceActual2 = asdaDistance2;
        this.ldaDistanceActual2 = ldaDistance2;
        this.displacedThresholdRActual = displacedThresholdR;
        this.leftStopwayLengthActual = leftStopwayLength;
        this.leftClearwayLengthActual = leftClearwayLength;

        // Stop the loading screen
        this.isLoadingScreen = false;

        // Remove any obstacle
        this.isObstacleScreen = false;

        // Update the canvas
        paint();
    }

    /**
     * Updates all the parameters, respective to the canvas size (for scaling)
     */
    protected void updateValues() {
        // Update all the distances to be respective of the new canvas width
        // Logical Runway 1
        this.toraDistance1 = calculateRatioValueLength(toraDistanceActual1);
        this.todaDistance1 = calculateRatioValueLength(todaDistanceActual1);
        this.asdaDistance1 = calculateRatioValueLength(asdaDistanceActual1);
        this.ldaDistance1 = calculateRatioValueLength(ldaDistanceActual1);
        this.displacedThresholdL = calculateRatioValueLength(displacedThresholdLActual);
        this.rightStopwayLength = calculateRatioValueLength(rightStopwayLengthActual);
        this.rightClearwayLength = calculateRatioValueLength(rightClearwayLengthActual);

        // Logical Runway 2
        this.toraDistance2 = calculateRatioValueLength(toraDistanceActual2);
        this.todaDistance2 = calculateRatioValueLength(todaDistanceActual2);
        this.asdaDistance2 = calculateRatioValueLength(asdaDistanceActual2);
        this.ldaDistance2 = calculateRatioValueLength(ldaDistanceActual2);
        this.displacedThresholdR = calculateRatioValueLength(displacedThresholdRActual);
        this.leftStopwayLength = calculateRatioValueLength(leftStopwayLengthActual);
        this.leftClearwayLength = calculateRatioValueLength(leftClearwayLengthActual);

        // Update obstacle distance
        this.obstacleDistanceFromStart = calculateRatioValueLength(obstacleDistanceFromStartActual);
        this.obstacleDistanceFromCentreLine = calculateRatioValueWidth(
            obstacleDistanceFromCentreLineActual);
        this.obstacleHeight = calculateRatioValueLength(obstacleHeightActual) + 10;

        // Update the re-calculated parameters
        this.slope = calculateRatioValueLength(slopeActual);
        this.stripEnd = calculateRatioValueLength(stripEndActual);
        this.resa = calculateRatioValueLength(resaActual);
        this.blastProtection = calculateRatioValueLength(blastProtectionActual);
    }


    /**
     * Set the new re-calculated parameters
     *
     * @param runwayObstacle the runway obstacle object with the recalculated values
     * @param blast          the blast protection
     */
    public void setRecalculatedParameters(RunwayObstacle runwayObstacle, double blast) {
        this.runway = runwayObstacle.getRecalculatedRw();
        this.runwayObstacle = runwayObstacle;

        // Find distances
        var isLeftSide = runwayObstacle.getPositionL() < runwayObstacle.getPositionR();
        var distanceFromStart =
            runway.getDisThresh(runway.getLogicId2()) + runwayObstacle.getPositionL();

        // Switch the runway if necessary
        if (isThresholdSwitched) {
            this.runway = runway.getSwitchedThresholdRunway();
            isLeftSide = !isLeftSide;
            distanceFromStart = actualRunwayLength - distanceFromStart;
        }

        // Get the 2 logical runway ID's
        var runway1ID = runway.getLogicId1();
        var runway2ID = runway.getLogicId2();

        // Send the data to the other overloaded method
        setRecalculatedParameters(
            runway.getTora(runway1ID),
            runway.getToda(runway1ID),
            runway.getAsda(runway1ID),
            runway.getLda(runway1ID),
            runway.getTora(runway2ID),
            runway.getToda(runway2ID),
            runway.getAsda(runway2ID),
            runway.getLda(runway2ID),
            runwayObstacle.getObst().getSlope(),
            runway.getStripL(),
            runway.getResaL(),
            blast,
            distanceFromStart,
            runwayObstacle.getDistCR() * (isThresholdSwitched ? -1 : 1),
            runwayObstacle.getObst().getHeight(),
            isLeftSide
        );
    }

    /**
     * Refresh the visualisation
     */
    protected void refresh() {
        if (isLoadingScreen) {
            return;
        }

        // Store the screen state temporarily
        var tempScreenStore = isObstacleScreen;

        // Select which runway to display
        var runwayToDisplay = isThresholdSwitched ? switchedRunway : originalRunway;

        // Get logical ids
        var runway1ID = runwayToDisplay.getLogicId1();
        var runway2ID = runwayToDisplay.getLogicId2();

        // Update the parameters
        setInitialParameters(
            runwayToDisplay.getRunwayL(),
            runwayToDisplay.getRunwayW(),
            runway1ID,
            Runway.getDegreeInString(runwayToDisplay.getDegree1()),
            runwayToDisplay.getTora(runway1ID),
            runwayToDisplay.getToda(runway1ID),
            runwayToDisplay.getAsda(runway1ID),
            runwayToDisplay.getLda(runway1ID),
            runway2ID,
            Runway.getDegreeInString(runwayToDisplay.getDegree2()),
            runwayToDisplay.getTora(runway2ID),
            runwayToDisplay.getToda(runway2ID),
            runwayToDisplay.getAsda(runway2ID),
            runwayToDisplay.getLda(runway2ID),
            runwayToDisplay.getDisThresh(runway2ID),
            runwayToDisplay.getDisThresh(runway1ID),
            runwayToDisplay.getStopwayL(runway2ID),
            runwayToDisplay.getStopwayL(runway1ID),
            runwayToDisplay.getClearwayL(runway2ID),
            runwayToDisplay.getClearwayL(runway1ID)
        );

        // If obstacle screen
        if (tempScreenStore) {
            setRecalculatedParameters(this.runwayObstacle, this.blastProtectionActual);
        }
    }

    /**
     * Set the new re-calculated parameters
     *
     * @param toraDistance1                  the Take-Off Run Available for logical runway 1
     * @param todaDistance1                  the Take-Off Distance Available for logical runway 1
     * @param asdaDistance1                  the Accelerate-Stop Distance Available for logical
     *                                       runway 1
     * @param ldaDistance1                   the Landing Distance Available for logical runway 1
     * @param toraDistance2                  the Take-Off Run Available for logical runway 2
     * @param todaDistance2                  the Take-Off Distance Available for logical runway 2
     * @param asdaDistance2                  the Accelerate-Stop Distance Available for logical
     *                                       runway 2
     * @param ldaDistance2                   the Landing Distance Available for logical runway 2
     * @param slope                          the slope calculation
     * @param stripEnd                       the strip end value
     * @param resa                           the RESA value
     * @param blast                          the blast protection value
     * @param obstacleDistanceFromStart      the distance of the obstacle from the start of the
     *                                       runway strip
     * @param obstacleDistanceFromCentreLine the obstacle distance from the centreline (+ for
     *                                       north)
     * @param obstacleHeight                 the height of the obstacle
     * @param isObstacleOnLeftSide           indicates whether the obstacle is on the left of the
     *                                       runway (True) or on the right (False)
     */
    protected void setRecalculatedParameters(
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
        double obstacleDistanceFromCentreLine,
        double obstacleHeight,
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
        this.obstacleDistanceFromCentreLineActual = obstacleDistanceFromCentreLine;
        this.obstacleHeightActual = obstacleHeight;
        this.isObstacleOnLeftSide = isObstacleOnLeftSide;

        // Set obstacle screen
        this.isObstacleScreen = true;

        // Update the canvas
        paint();
    }

    /**
     * Calculate a value respective to the canvas runway length (mainly used for east/west
     * distances)
     *
     * @param value the value
     * @return the new value respective to the canvas runway length
     */
    protected double calculateRatioValueLength(double value) {
        // The ratio is  actual runway width:canvas runway width
        return (value / actualRunwayLength) * runwayLength;
    }

    /**
     * Calculate a value respective to the canvas runway width (mainly used for north/south
     * distances)
     *
     * @param value the value
     * @return the new value respective to the canvas runway width
     */
    protected double calculateRatioValueWidth(double value) {
        // The ratio is  actual runway width:canvas runway width
        return (value / actualRunwayWidth) * runwayWidth;
    }

    /**
     * Set the colour theme of the visualisation
     *
     * @param colourTheme the colour theme object
     */
    public void setColourTheme(ColourTheme colourTheme) {
        this.colourTheme = colourTheme;
        paint();
    }

    /**
     * Set the distance/offset from the runway strip to the first arrow annotation
     *
     * @param offset the offset value
     */
    public void setArrowsFromRunwayOffset(double offset) {
        this.arrowsFromRunwayOffset = offset;
    }

    /**
     * Set the gap between the arrows on the visualisation
     *
     * @param gap the gap value
     */
    public void setArrowsGapBetween(double gap) {
        this.arrowsGapBetween = gap;
    }

    /**
     * Set whether to show the colour key on the visualisation
     *
     * @param showKey whether to show the key
     */
    public void setShowKey(boolean showKey) {
        this.isShowKey = showKey;
    }

    /**
     * Set whether to rotate the runway strip to match its compass heading
     *
     * @param rotateCompass whether to rotate the runway strip
     */
    public void setRotateCompass(boolean rotateCompass) {
        this.isRotateCompass = rotateCompass;

        // Update canvas
        paint();
    }

    /**
     * Set whether to switch the thresholds
     *
     * @param thresholdSwitched whether to switch threshold
     */
    public void setThresholdSwitched(boolean thresholdSwitched) {
        this.isThresholdSwitched = thresholdSwitched;

        // Refresh the canvas
        refresh();
    }
}
