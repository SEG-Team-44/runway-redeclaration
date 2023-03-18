package com.team44.runwayredeclarationapp.view.component.visualisation;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

/**
 * The layout pane for specified visualisation canvas
 */
public class VisualisationPane extends Pane {

    /**
     * Create a new layout pane
     *
     * @param canvas the visualisation canvas
     */
    public VisualisationPane(Canvas canvas) {
        // Bind canvas size properties to the pane properties
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());

        // to rotate:
        // canvas.setRotate(45);

        this.setStyle("-fx-background-color: black");
        this.getChildren().add(canvas);
    }
}
