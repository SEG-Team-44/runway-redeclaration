package com.team44.runwayredeclarationapp.model.theme;

import javafx.scene.paint.Color;

/**
 * The theme specifying colours for each segment of the program
 */
public class ColourTheme {

    /**
     * Loading screen colours
     */
    private Color loadingScreen = Color.BLACK;

    /**
     * Basic text and line colours
     */
    private Color text = Color.WHITE;
    private Color arrow = Color.WHITE;
    private Color guideline = Color.WHITE;

    /**
     * Base colours
     */
    private Color runwayStrip = Color.DIMGRAY;
    private Color stopway = Color.web("2e2e2e");
    private Color clearway = Color.WHITE;
    private Color colourKeyBox = Color.BLACK;

    /**
     * Obstacle colour
     */
    private Color obstacle = Color.RED;

    /**
     * Custom colours for top-down view
     */
    private Color thresholdMarkingLines = Color.WHITE;
    private Color topDownBackground = Color.DARKGREEN;
    private Color clearedGradedArea = Color.CORNFLOWERBLUE;

    /**
     * Custom colours for side-on view
     */
    private Color tocsALSLine = Color.WHITE;
    private Color sideOnTopBackground = Color.DARKSLATEBLUE;
    private Color sideOnBottomBackground = Color.SADDLEBROWN;

    /**
     * Create the default colour theme
     */
    public ColourTheme() {
    }

    /**
     * Create a custom colour theme
     * todo: write documentation
     */
    public ColourTheme(Color loadingScreen, Color text,
        Color arrow, Color guideline, Color runwayStrip, Color stopway,
        Color clearway, Color colourKeyBox,
        Color obstacle, Color thresholdMarkingLines,
        Color topDownBackground, Color clearedGradedArea, Color tocsALSLine,
        Color sideOnTopBackground, Color sideOnBottomBackground) {
        this.loadingScreen = loadingScreen;
        this.text = text;
        this.arrow = arrow;
        this.guideline = guideline;
        this.runwayStrip = runwayStrip;
        this.stopway = stopway;
        this.clearway = clearway;
        this.colourKeyBox = colourKeyBox;
        this.obstacle = obstacle;
        this.thresholdMarkingLines = thresholdMarkingLines;
        this.topDownBackground = topDownBackground;
        this.clearedGradedArea = clearedGradedArea;
        this.tocsALSLine = tocsALSLine;
        this.sideOnTopBackground = sideOnTopBackground;
        this.sideOnBottomBackground = sideOnBottomBackground;
    }

    public ColourTheme getDefault() {
        return new ColourTheme();
    }

    public Color getLoadingScreen() {
        return loadingScreen;
    }

    public Color getText() {
        return text;
    }

    public Color getArrow() {
        return arrow;
    }

    public Color getGuideline() {
        return guideline;
    }

    public Color getRunwayStrip() {
        return runwayStrip;
    }

    public Color getStopway() {
        return stopway;
    }

    public Color getClearway() {
        return clearway;
    }

    public Color getColourKeyBox() {
        return colourKeyBox;
    }

    public Color getObstacle() {
        return obstacle;
    }

    public Color getThresholdMarkingLines() {
        return thresholdMarkingLines;
    }

    public Color getTopDownBackground() {
        return topDownBackground;
    }

    public Color getClearedGradedArea() {
        return clearedGradedArea;
    }

    public Color getTocsALSLine() {
        return tocsALSLine;
    }

    public Color getSideOnTopBackground() {
        return sideOnTopBackground;
    }

    public Color getSideOnBottomBackground() {
        return sideOnBottomBackground;
    }
}
