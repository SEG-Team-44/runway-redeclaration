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
    /*private Color ASDAarrow = Color.valueOf("#FFA500");
    private Color TORAarrow = Color.valueOf("#ADD8E6");
    private Color TODAarrow = Color.valueOf("#FFFF00");
    private Color LDAarrow = Color.valueOf("#FFC6D9");
    private Color Blastarrow = Color.valueOf("#D43149");
    private Color SEarrow = Color.valueOf("#FFDB58");
    private Color RESAarrow = Color.valueOf("#D3D3D3");
    private Color Slopearrow = Color.valueOf("#FFFDD0");
    private Color DTarrow = Color.valueOf("#09FF00");*/
    private Color ASDAarrow = Color.valueOf("#79E1F7");
    private Color TORAarrow = Color.valueOf("#F7CD79");
    private Color TODAarrow = Color.valueOf("#9DE079");
    private Color LDAarrow = Color.valueOf("#FFFEB3");
    private Color Blastarrow = Color.valueOf("#CC9EF0");
    private Color SEarrow = Color.valueOf("#F37735");
    private Color RESAarrow = Color.valueOf("#FFC425");
    private Color Slopearrow = Color.valueOf("#ED748D");
    private Color DTarrow = Color.valueOf("#09FF00");
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
    /*private Color clearedGradedArea = Color.CORNFLOWERBLUE;*/
    private Color clearedGradedArea = Color.valueOf("#6473ED");

    /**
     * Custom colours for side-on view
     */
    //private Color tocsALSLine = Color.WHITE;
    private Color tocsALSLine = Color.valueOf("#FFFEB3");
    private Color sideOnTopBackground = Color.DARKSLATEBLUE;
    private Color sideOnBottomBackground = Color.SADDLEBROWN;

    /**
     * Create the default colour theme
     */
    public ColourTheme() {
    }


    /**
     * Create a custom colour scheme
     *
     * @param loadingScreen          the loading screen background colour
     * @param text                   the text colour
     * @param arrow                  the arrow colour
     * @param ASDAarrow              the ASDA arrow colour
     * @param TODAarrow              the TODA arrow colour
     * @param TORAarrow              the TORA arrow colour
     * @param LDAarrow               the LDA arrow colour
     * @param Blastarrow             the Blast arrow colour
     * @param SEarrow                the SE arrow colour
     * @param RESAarrow              the RESA arrow colour
     * @param Slopearrow             the Slope arrow colour
     * @param DTarrow                the DT arrow colour
     * @param guideline              the guideline colour
     * @param runwayStrip            the runway strip colour
     * @param stopway                the stopway colour
     * @param clearway               the clearway colour
     * @param colourKeyBox           the colour key box colour
     * @param obstacle               the obstacle colour
     * @param thresholdMarkingLines  the threshold marking/lines colour
     * @param topDownBackground      the top-down view background colour
     * @param clearedGradedArea      the cleared and graded area colour
     * @param tocsALSLine            the tocs line colour
     * @param sideOnTopBackground    the side-on view top background colour (sky)
     * @param sideOnBottomBackground the side-on view bottom background colour (under runway)
     */
    public ColourTheme(Color loadingScreen, Color text,
        Color arrow, Color ASDAarrow, Color TODAarrow,
        Color TORAarrow, Color LDAarrow,
        Color Blastarrow, Color SEarrow, Color RESAarrow,
        Color Slopearrow, Color DTarrow,
        Color guideline, Color runwayStrip, Color stopway,
        Color clearway, Color colourKeyBox,
        Color obstacle, Color thresholdMarkingLines,
        Color topDownBackground, Color clearedGradedArea, Color tocsALSLine,
        Color sideOnTopBackground, Color sideOnBottomBackground,
        Color topDownBackgroundCB, Color sideOnTopBackgroundCB, Color sideOnBottomBackgroundCB) {
        this.loadingScreen = loadingScreen;
        this.text = text;
        this.arrow = arrow;
        this.ASDAarrow = ASDAarrow;
        this.TODAarrow = TODAarrow;
        this.TORAarrow = TORAarrow;
        this.LDAarrow = LDAarrow;
        this.Blastarrow = Blastarrow;
        this.SEarrow = SEarrow;
        this.RESAarrow = RESAarrow;
        this.Slopearrow = Slopearrow;
        this.DTarrow = DTarrow;
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

    /**
     * GETTERS
     */
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

    public Color getASDAarrow() {
        return ASDAarrow;
    }

    public Color getTORAarrow() {
        return TORAarrow;
    }

    public Color getTODAarrow() {
        return TODAarrow;
    }

    public Color getLDAarrow() {
        return LDAarrow;
    }

    public Color getSEarrow() {
        return SEarrow;
    }

    public Color getRESAarrow() {
        return RESAarrow;
    }

    public Color getSlopearrow() {
        return Slopearrow;
    }

    public Color getDTarrow() {
        return DTarrow;
    }

    public Color getBlastarrow() {
        return Blastarrow;
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

    /**
     * SETTERS
     */
    public void setTopDownBackground(Color topDownBackground) {
        this.topDownBackground = topDownBackground;
    }

    public void setSideOnTopBackground(Color sideOnTopBackground) {
        this.sideOnTopBackground = sideOnTopBackground;
    }

    public void setSideOnBottomBackground(Color sideOnBottomBackground) {
        this.sideOnBottomBackground = sideOnBottomBackground;
    }

    /**
     * Get the theme for colour-blind mode
     *
     * @return the colour-blind theme
     */
    public static ColourTheme getColourBlindTheme() {
        var theme = new ColourTheme();

        // Change the colours
        theme.setTopDownBackground(Color.valueOf("#90581C"));
        theme.setSideOnTopBackground(Color.valueOf("#006CD1"));
        theme.setSideOnBottomBackground(Color.valueOf("#000000"));

        // Return the theme
        return theme;
    }
}
