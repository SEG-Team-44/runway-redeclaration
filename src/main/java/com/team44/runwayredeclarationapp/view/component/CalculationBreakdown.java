package com.team44.runwayredeclarationapp.view.component;

import com.team44.runwayredeclarationapp.controller.RecalculationController;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.RunwayObstacle;
import com.team44.runwayredeclarationapp.view.component.text.Title;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Class responsible for displaying calculation breakdown
 */
public class CalculationBreakdown extends VBox {

    /**
     * logical ID of the logical runway where aircraft is taking off towards/landing towards the
     * obstacle
     */
    String runway1;
    /**
     * logical ID of the logical runway where aircraft is taking off away/landing over the obstacle
     */
    String runway2;
    RecalculationController controller;
    /**
     * runway that have been recalculated
     */
    Runway recalRunway;

    Title title;
    /**
     * TabPane included all calculations
     */
    TabPane tabPane;

    /**
     * Initialise calculation breakdown component
     */
    public CalculationBreakdown() {
        title = new Title("Calculation Breakdown:");
        title.getStyleClass().add("title");

        controller = new RecalculationController();
        getChildren().add(title);
        setAlignment(Pos.CENTER);
        setSpacing(15);
    }

    /**
     * Generate calculation breakdown for TORA
     *
     * @param runway current runway
     * @param rwObst current runway and obstacle appeared
     * @return VBox containing calculation breakdown of TORA
     */
    private VBox initTORA(Runway runway, RunwayObstacle rwObst) {
        // get all parameters used in recalculation for runway 1 & 2
        HashMap values1 = controller.recalculateTORA(runway, runway1, rwObst, rwObst.getBlastPro());
        HashMap values2 = controller.recalculateTORA(runway, runway2, rwObst, rwObst.getBlastPro());

        //calculation formula of runway 1
        String f1 = "= Original TORA - Obstacle From Threshold - ";
        //calculation breakdown of runway 1
        String c1 = "= " + values1.get("ogTORA") + " - " +
            values1.get("distanceFromThreshold");

        //add blast protection or strip end & RESA into the formula & calculation for runway 1
        if (values1.get("blastProtection") == null) {
            f1 += "Strip End - RESA ";
            c1 += " - " + values1.get("stripEnd") + " - " + values1.get("resa");
        } else {
            f1 += "Blast Protection";
            c1 += " - " + values1.get("blastProtection");
        }

        //add displaced threshold into the formula & calculation if required
        if (values1.get("displacedThreshold") != null) {
            f1 += " - Displaced Threshold";
            c1 += " - " + values1.get("displacedThreshold");
        }
        Text result1 = new Text("= " + values1.get("recalTORA"));

        //formula & calculation breakdown for runway 2
        String f2 = "= Obstacle From Threshold - Slope Calculation - Strip End";
        String c2 = "= " + values2.get("distanceFromThreshold") + " - " +
            rwObst.getObst().getHeight() + "*50" + " - " + values2.get("stripEnd");

        //add displaced threshold if required
        if (values2.get("displacedThreshold") != null) {
            f2 += " - Displaced Threshold";
            c2 += " - " + values2.get("displacedThreshold");
        }
        Text result2 = new Text("= " + values2.get("recalTORA"));

        Text formula1 = new Text(f1);
        Text calculation1 = new Text(c1);
        Text formula2 = new Text(f2);
        Text calculation2 = new Text(c2);

        return combineComponents(new Text("TORA "), formula1, calculation1, result1,
            new Text("TORA "), formula2, calculation2, result2, false);
    }

    /**
     * Generate calculation breakdown for TODA
     *
     * @param runway current runway
     * @param rwObst current runway & obstacle appeared
     * @return VBox containing calculation breakdown of TODA
     */
    private VBox initTODA(Runway runway, RunwayObstacle rwObst) {
        // get all parameters used in recalculation for runway1 & 2
        HashMap values1 = controller.recalculateTODA(runway, runway1, rwObst,
            recalRunway.getTora(runway1));
        HashMap values2 = controller.recalculateTODA(runway, runway2, rwObst,
            recalRunway.getTora(runway2));

        //formula & calculation of runway 1
        Text formula1 = new Text("= Recalculated TORA + Clearway");
        Text calculation1 = new Text("= " + values1.get("tora") + " + " + values1.get("clearway"));
        Text result1 = new Text("= " + values1.get("recalTODA"));

        //formula & calculation of runway 2
        Text formula2 = new Text("= Recalculated TORA");
        Text result2 = new Text("= " + values2.get("recalTODA"));

        return combineComponents(new Text("TODA "), formula1, calculation1, result1,
            new Text("TODA "), formula2, result2, result2, true);
    }

    /**
     * Generate calculation breakdown for ASDA
     *
     * @param runway current runway
     * @param rwObst current runway & obstacle appeared
     * @return VBox containing calculation breakdown of ASDA
     */
    private VBox initASDA(Runway runway, RunwayObstacle rwObst) {
        // get all parameters used in recalculation for runway1 & 2
        HashMap values1 = controller.recalculateASDA(runway, runway1, rwObst,
            recalRunway.getTora(runway1));
        HashMap values2 = controller.recalculateASDA(runway, runway2, rwObst,
            recalRunway.getTora(runway2));

        Text formula1 = new Text("= Recalculated TORA + Stopway");
        Text calculation1 = new Text("= " + values1.get("tora") + " + " + values1.get("stopway"));
        Text result1 = new Text("= " + values1.get("recalASDA"));

        Text formula2 = new Text("= Recalculated TORA");
        Text result2 = new Text("= " + values2.get("recalASDA"));

        return combineComponents(new Text("ASDA "), formula1, calculation1, result1,
            new Text("ASDA "), formula2, result2, result2, true);
    }

    /**
     * Generate calculation breakdown for LDA
     *
     * @param runway current runway
     * @param rwObst current runway * obstacle appeared
     * @return VBox containing calculation breakdown of LDA
     */
    private VBox initLDA(Runway runway, RunwayObstacle rwObst) {
        // get all parameters used in recalculation for runway1 & 2
        HashMap values1 = controller.recalculateLDA(runway, runway1, rwObst);
        HashMap values2 = controller.recalculateLDA(runway, runway2, rwObst);

        Text formula1 = new Text("= Original LDA - " +
            "Obstacle From Threshold - Slope Calculation - Strip End");
        Text calculation1 = new Text("= " + values1.get("ogLDA") + " - " +
            values1.get("distanceFromThreshold") + " - " +
            rwObst.getObst().getHeight() + "*50" + " - " + values1.get("stripEnd"));
        Text result1 = new Text("= " + values1.get("recalLDA"));

        Text formula2 = new Text("= Obstacle From Threshold - Strip End - RESA");
        Text calculation2 = new Text("= " + values2.get("distanceFromThreshold") + " - " +
            values2.get("stripEnd") + " - " + values2.get("resa"));
        Text result2 = new Text("= " + values2.get("recalLDA"));

        return combineComponents(new Text("LDA "), formula1, calculation1, result1,
            new Text("LDA "), formula2, calculation2, result2, false);
    }

    /**
     * Generate all calculation breakdowns for the given formulas & calculations
     *
     * @param title1       title for runway 1
     * @param formula1     formula for runway 1
     * @param calculation1 calculation breakdown for runway 1
     * @param result1      recalculated parameter of runway 1
     * @param title2       title for runway 2
     * @param formula2     formula for runway 2
     * @param calculation2 calculation breakdown for runway 2
     * @param result2      recalculated parameter of runway 2
     * @param noCalForRw2  boolean indicates whether calculation breakdown is needed for runway 2
     * @return VBox containing all components passed
     */
    private VBox combineComponents(Text title1, Text formula1, Text calculation1, Text result1,
        Text title2, Text formula2, Text calculation2, Text result2, boolean noCalForRw2) {
        //initialise each subsection
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(5));
        GridPane runway1Grid = new GridPane();
        runway1Grid.setVgap(5);
        GridPane runway2Grid = new GridPane();
        runway2Grid.setVgap(5);

        //calculation title indicating the calculation's corresponding runway
        Text calTitle1 = new Text(runway1 + " (Taking Off Away/Landing Over):");
        Text calTitle2 = new Text(runway2 + " (Taking Off Towards/Landing Towards):");

        //combine all components
        runway1Grid.add(calTitle1, 0, 0, 2, 1);
        runway1Grid.add(title1, 0, 1);
        runway1Grid.addColumn(1, formula1, calculation1, result1);

        runway2Grid.add(calTitle2, 0, 0, 2, 1);
        runway2Grid.add(title2, 0, 1);

        //print the result only if no calculation is needed for runway 2
        if (noCalForRw2) {
            runway2Grid.addColumn(1, formula2, result2);
        } else {
            runway2Grid.addColumn(1, formula2, calculation2, result2);
        }

        vBox.getChildren().addAll(runway1Grid, runway2Grid);
        return vBox;
    }

    /**
     * Initialise logicalID 1 & 2
     *
     * @param runway current runway
     * @param rwObst current runway & obstacle appeared
     */
    private void setRunways(Runway runway, RunwayObstacle rwObst) {
        //set runway1 to be the logical runway ID of which the aircraft is operating in the direction of take off away/land over the obstacle
        //runway2 to be the logical runway ID for the opposite (take off towards/land towards)
        if (controller.isTakeOffTowards(runway.getLogicId1(), runway.getLogicId1(), rwObst)) {
            runway1 = runway.getLogicId1();
            runway2 = runway.getLogicId2();
        } else {
            runway1 = runway.getLogicId2();
            runway2 = runway.getLogicId1();
        }
    }

    /**
     * Generate the TabPane displaying the whole calculation breakdown
     *
     * @param originRunway original runway
     * @param rwObst       obstacle appeared on the runway
     */
    public void displayCalculations(Runway originRunway, RunwayObstacle rwObst) {
        //remove previous calculation breakdown before generating a new one
        reset();

        //initialise the tabPane
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setTabDragPolicy(TabPane.TabDragPolicy.FIXED);

        //reset the logical IDs
        setRunways(originRunway, rwObst);

        //store the recalculated runway
        recalRunway = rwObst.getRecalculatedRw();

        //initialise the tabs displaying the calculations
        Tab tora = new Tab("TORA");
        tora.setContent(initTORA(originRunway, rwObst));
        Tab toda = new Tab("TODA");
        toda.setContent(initTODA(originRunway, rwObst));
        Tab asda = new Tab("ASDA");
        asda.setContent(initASDA(originRunway, rwObst));
        Tab lda = new Tab("LDA");
        lda.setContent(initLDA(originRunway, rwObst));

        tabPane.getTabs().addAll(tora, toda, asda, lda);
        getChildren().add(tabPane);
    }

    /**
     * Remove the previous calculation breakdown from the section
     */
    public void reset() {
        getChildren().remove(tabPane);
    }
}
