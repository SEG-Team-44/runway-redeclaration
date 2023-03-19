package com.team44.runwayredeclarationapp.view.component.visualisation;

import com.team44.runwayredeclarationapp.model.Coord;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.RunwayObstacle;
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

    /**
     * Runway width and height
     */
    protected double runwayWidth, runwayLength;

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
    protected double arrowsFromRunwayOffset = 110;

    /**
     * The coordinates for the guidelines for both logical runways
     */
    protected HashSet<Coord> guideLineCoordsUp = new HashSet<>();
    protected HashSet<Coord> guideLineCoordsDown = new HashSet<>();

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

        // Draw the new canvas
        gc.setFill(Color.DARKGREEN);
        gc.fillRect(0, 0, width, height);

        // Runway cords and info:
        this.runwayX1 = width * 0.15;
        this.runwayY1 = (height / 2) - (runwayWidth / 2);
        this.runwayLength = width - (runwayX1 * 2);
        this.runwayX2 = runwayX1 + runwayLength;
        this.runwayY2 = runwayY1 + runwayWidth;
        updateValues();

        // Paint the canvas background (for separate subclasses)
        paintCanvasBackground();

        // Draw the runway strip
        gc.setFill(Color.DIMGRAY);
        gc.fillRect(runwayX1, runwayY1, runwayLength, runwayWidth);

        // Left side stopway
        gc.setFill(Color.web("2e2e2e"));
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
        } else {
            drawAllArrowsRecalculatedRight();
            drawObstacle();
        }

        // Draw the guidelines
        drawGuidelines();

        // Draw the take-off/landing direction and text
        drawTakeOffLandingDirection();
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
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        // Add the loading message
        addText("Load a runway to see the visualisation.", 16, (width / 2) - 120, (height / 2));
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
     * Draw the take-off/landing direction for both logical runways
     */
    protected void drawTakeOffLandingDirection() {
        // Top arrow
        addArrow(20, 35, 70, 10);
        addText("Landing and Take-off in this direction", 13, 20, 20);

        // Bottom arrow
        addArrow(getWidth() - 20, getHeight() - 35, getWidth() - 70, 10);
        addText("Landing and Take-off in this direction", 13, getWidth() - 230, getHeight() - 13);
    }

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
        addArrow(x1, y1, x2, 3);
    }

    /**
     * Add a horizontal arrow to the canvas
     *
     * @param x1              the start x coordinate
     * @param y1              the start (and finish) y coordinate
     * @param x2              the finish x coordinate
     * @param arrowPointWidth the width of the arrowhead
     */
    protected void addArrow(double x1, double y1, double x2, double arrowPointWidth) {
        var gc = getGraphicsContext2D();

        // Set the pen properties
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
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
        gc.setStroke(Color.WHITE);
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
        // Get the 2 logical runway ID's
        var runway1ID = runway.getLogicId1();
        var runway2ID = runway.getLogicId2();

        // Send the data to the other overloaded method
        setInitialParameters(
            runway.getRunwayL(),
            runway.getRunwayW(),
            runway.getTora(runway1ID),
            runway.getToda(runway1ID),
            runway.getAsda(runway1ID),
            runway.getLda(runway1ID),
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
    public void setInitialParameters(
        double actualRunwayLength,
        double actualRunwayWidth,
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
        this.actualRunwayWidth = actualRunwayWidth;

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
     * @param runway   the runway object
     * @param obstacle the obstacle
     * @param blast    the blast protection
     */
    public void setRecalculatedParameters(Runway runway, RunwayObstacle obstacle, double blast) {
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
            obstacle.getObst().getSlope(),
            runway.getStripL(),
            runway.getResaL(),
            blast,
            runway.getDisThresh(runway2ID) + obstacle.getPositionL(),
            obstacle.getDistCR(),
            obstacle.getObst().getHeight(),
            obstacle.getPositionL() < obstacle.getPositionR()
        );
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
        // todo:: blast protect > resa + se

        // The ratio is  actual runway width:canvas runway width
        return (value / actualRunwayWidth) * runwayWidth;
    }
}
