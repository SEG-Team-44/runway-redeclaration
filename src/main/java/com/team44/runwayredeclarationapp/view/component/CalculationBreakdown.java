package com.team44.runwayredeclarationapp.view.component;

import com.team44.runwayredeclarationapp.controller.RecalculationController;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.RunwayObstacle;
import com.team44.runwayredeclarationapp.view.component.text.Title;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Objects;

public class CalculationBreakdown extends VBox {
    String runway1;
    String runway2;
    RecalculationController controller;
    Runway recalRunway;
    Title title;
    TabPane tabPane;

    public CalculationBreakdown() {
        title = new Title("Calculation Breakdown");
        title.setFont(new Font(15));

        controller = new RecalculationController();
        getChildren().add(title);
        setAlignment(Pos.CENTER);
    }

    private VBox initTORA(Runway runway, RunwayObstacle rwObst, int blastProtection) {
        VBox toraBox = new VBox();
        toraBox.setSpacing(15);
        toraBox.setPadding(new Insets(5));
        GridPane runway1Grid = new GridPane();
        runway1Grid.setVgap(5);
        GridPane runway2Grid = new GridPane();
        runway2Grid.setVgap(5);

        HashMap values1 = controller.recalculateTORA(runway, runway1, rwObst, blastProtection);
        HashMap values2 = controller.recalculateTORA(runway, runway2, rwObst, blastProtection);

        String r1 = runway1 + " (Taking Off Towards/Landing Over):";
        String r2 = runway2 + " (Taking Off Away/Landing Towards):";

        Text title1 = new Text();
        title1.setText(r1);
        Text title2 = new Text();
        title2.setText(r2);

        String f1 = "= Original TORA" + getSymbol(convertToDouble(values1.get("distanceFromThreshold"))) + "Obstacle From Threshold - ";
        String c1 = "= " + values1.get("ogTORA").toString() + getSymbol(convertToDouble(values1.get("distanceFromThreshold"))) +
                Math.abs(convertToDouble(values1.get("distanceFromThreshold")));

        if (values1.get("blastProtection") == null) {
            f1 += "Strip End - RESA ";
            c1 += " - " + values1.get("stripEnd") + values1.get("resa");
        }
        else {
            f1 += "Blast Protection";
            c1 += " - " + values1.get("blastProtection");
        }

        if (values1.get("displacedThreshold") != null) {
            f1 += " - Displaced Threshold";
            c1 += " - " + values1.get("displacedThreshold");
        }
        Text result1 = new Text("= " + values1.get("recalTORA"));

        //work out calculation breakdown for runway 2
        String f2 = "= Obstacle From Threshold - Slope Calculation - Strip End";
        String c2 = "= " + values2.get("distanceFromThreshold") + " - " +
                values2.get("slopeCalculation") + " - " + values2.get("stripEnd");
        if (values2.get("displacedThreshold") != null) {
            f2 += getSymbol(convertToDouble(values2.get("displacedThreshold"))) + "Displaced Threshold";
            c2 += getSymbol(convertToDouble(values2.get("displacedThreshold"))) + Math.abs(convertToDouble(values2.get("displacedThreshold")));
        }
        Text result2 = new Text("= " + values2.get("recalTORA"));

        Text formula1 = new Text(f1);
        Text calculation1 = new Text(c1);
        Text formula2 = new Text(f2);
        Text calculation2 = new Text(c2);
        Text tora1 = new Text("TORA ");
        Text tora2 = new Text("TORA ");

        runway1Grid.add(title1,0,0, 2,1);
        runway1Grid.add(tora1, 0,1);
        runway1Grid.addColumn(1, formula1, calculation1, result1);

        runway2Grid.add(title2, 0,0,2,1);
        runway2Grid.add(tora2, 0,1);
        runway2Grid.addColumn(1, formula2, calculation2, result2);
        toraBox.getChildren().addAll(runway1Grid, runway2Grid);

        return toraBox;
    }

    private VBox initTODA(Runway runway, RunwayObstacle rwObst) {
        VBox todaBox = new VBox();
        todaBox.setSpacing(15);
        todaBox.setSpacing(5);
        todaBox.setPadding(new Insets(5));
        GridPane runway1Grid = new GridPane();
        runway1Grid.setVgap(5);
        GridPane runway2Grid = new GridPane();
        runway2Grid.setVgap(5);

        HashMap values1 = controller.recalculateTODA(runway, runway1, rwObst, recalRunway.getTora(runway1));
        HashMap values2 = controller.recalculateTODA(runway, runway2, rwObst, recalRunway.getTora(runway2));

        String r1 = runway1 + " (Taking Off Towards/Landing Over):";
        String r2 = runway2 + " (Taking Off Away/Landing Towards):";

        Text title1 = new Text();
        title1.setText(r1);
        Text title2 = new Text();
        title2.setText(r2);

        Text toda1 = new Text("TODA ");
        Text formula1 = new Text("= Recalculated TORA + Clearway");
        Text calculation1 = new Text("= " + values1.get("tora") + " + " + values1.get("clearway"));
        Text result1 = new Text("= " + values1.get("recalTODA"));

        Text toda2 = new Text("TODA ");
        Text formula2 = new Text("= Recalculated TORA");
        Text result2 = new Text("= " + values2.get("recalTODA"));

        runway1Grid.add(title1,0,0, 2,1);
        runway1Grid.add(toda1, 0,1);
        runway1Grid.addColumn(1, formula1, calculation1, result1);

        runway2Grid.add(title2, 0,0,2,1);
        runway2Grid.add(toda2, 0,1);
        runway2Grid.addColumn(1, formula2, result2);
        todaBox.getChildren().addAll(runway1Grid, runway2Grid);

        return todaBox;
    }

    private VBox initASDA(Runway runway, RunwayObstacle rwObst) {
        VBox asdaBox = new VBox();
        asdaBox.setSpacing(15);
        asdaBox.setSpacing(5);
        asdaBox.setPadding(new Insets(5));
        GridPane runway1Grid = new GridPane();
        runway1Grid.setVgap(5);
        GridPane runway2Grid = new GridPane();
        runway2Grid.setVgap(5);

        HashMap values1 = controller.recalculateASDA(runway, runway1, rwObst, recalRunway.getTora(runway1));
        HashMap values2 = controller.recalculateASDA(runway, runway2, rwObst, recalRunway.getTora(runway2));

        String r1 = runway1 + " (Taking Off Towards/Landing Over):";
        String r2 = runway2 + " (Taking Off Away/Landing Towards):";

        Text title1 = new Text();
        title1.setText(r1);
        Text title2 = new Text();
        title2.setText(r2);

        Text asda1 = new Text("ASDA ");
        Text formula1 = new Text ("= Recalculated TORA + Stopway");
        Text calculation1 = new Text ("= " + values1.get("tora") + " + " + values1.get("stopway"));
        Text result1 = new Text("= " + values1.get("recalASDA"));

        Text formula2 = new Text ("= Recalculated TORA");
        Text result2 = new Text("= " + values2.get("recalASDA"));
        Text asda2 = new Text("ASDA ");

        runway1Grid.add(title1,0,0, 2,1);
        runway1Grid.add(asda1, 0,1);
        runway1Grid.addColumn(1, formula1, calculation1, result1);

        runway2Grid.add(title2, 0,0,2,1);
        runway2Grid.add(asda2, 0,1);
        runway2Grid.addColumn(1, formula2, result2);
        asdaBox.getChildren().addAll(runway1Grid, runway2Grid);

        return asdaBox;
    }

    private VBox initLDA(Runway runway, RunwayObstacle rwObst) {
        VBox ldaBox = new VBox();
        ldaBox.setSpacing(15);
        ldaBox.setSpacing(5);
        ldaBox.setPadding(new Insets(5));
        GridPane runway1Grid = new GridPane();
        runway1Grid.setVgap(5);
        GridPane runway2Grid = new GridPane();
        runway2Grid.setVgap(5);

        HashMap values1 = controller.recalculateLDA(runway, runway1, rwObst);
        HashMap values2 = controller.recalculateLDA(runway, runway2, rwObst);

        String r1 = runway1 + " (Taking Off Towards/Landing Over):";
        String r2 = runway2 + " (Taking Off Away/Landing Towards):";

        Text title1 = new Text();
        title1.setText(r1);
        Text title2 = new Text();
        title2.setText(r2);

        Text lda1 = new Text("LDA ");
        Text formula1 = new Text("= Original LDA" + getSymbol(convertToDouble(values1.get("distanceFromThreshold"))) +
                "Obstacle From Threshold - Slope Calculation - Strip End");
        Text calculation1 = new Text("= " + values1.get("ogLDA") + getSymbol(convertToDouble(values1.get("distanceFromThreshold"))) +
                Math.abs(convertToDouble(values1.get("distanceFromThreshold"))) + " - " +
                values1.get("slopeCalculation") + " - " + values1.get("stripEnd"));
        Text result1 = new Text("= " + values1.get("recalLDA"));

        Text lda2 = new Text("LDA ");
        Text formula2 = new Text("= Obstacle From Threshold - Strip End - RESA");
        Text calculation2 = new Text("= " + values2.get("distanceFromThreshold") + " - " +
                values2.get("stripEnd") + " - " + values2.get("resa"));
        Text result2 = new Text("= " + values2.get("recalLDA"));

        runway1Grid.add(title1,0,0, 2,1);
        runway1Grid.add(lda1, 0,1);
        runway1Grid.addColumn(1, formula1, calculation1, result1);

        runway2Grid.add(title2, 0,0,2,1);
        runway2Grid.add(lda2, 0,1);
        runway2Grid.addColumn(1, formula2, calculation2, result2);
        ldaBox.getChildren().addAll(runway1Grid, runway2Grid);

        return ldaBox;
    }

    private void setRunways(Runway runway, RunwayObstacle rwObst) {
        //if taking off towards logical runway 1
        if (controller.isTakeOffTowards(runway.getLogicId1(), runway.getLogicId1(), rwObst)) {
            runway1 = runway.getLogicId1();
            runway2 = runway.getLogicId2();
        }

        else {
            runway1 = runway.getLogicId2();
            runway2 = runway.getLogicId1();
        }
    }

    public void displayCalculations(Runway originRunway, RunwayObstacle rwObst, int blastProtection) {
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setTabDragPolicy(TabPane.TabDragPolicy.FIXED);

        setRunways(originRunway, rwObst);
        recalRunway = controller.recalculateRunway(rwObst, blastProtection);

        Tab tora = new Tab("TORA");
        tora.setContent(initTORA(originRunway, rwObst, blastProtection));
        Tab toda = new Tab("TODA");
        toda.setContent(initTODA(originRunway, rwObst));
        Tab asda = new Tab("ASDA");
        asda.setContent(initASDA(originRunway, rwObst));
        Tab lda = new Tab("LDA");
        lda.setContent(initLDA(originRunway, rwObst));

        tabPane.getTabs().addAll(tora, toda, asda, lda);
        getChildren().add(tabPane);
    }

    public void reset() {
        getChildren().remove(tabPane);
    }

    private double convertToDouble (Object value) {
        return Double.parseDouble(value.toString());
    }

    private String getSymbol(double value) {
        if (value < 0) {
            return " + ";
        }

        else return " - ";
    }
}
