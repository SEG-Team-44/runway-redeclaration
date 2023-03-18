package com.team44.runwayredeclarationapp.view.component.titlepane;

import com.team44.runwayredeclarationapp.view.MainScene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * The Titled Pane for selecting the airport
 */
public class AirportTitlePane extends TitledPane {

    /**
     * Create the titled pane
     *
     * @param mainScene the main scene
     */
    public AirportTitlePane(MainScene mainScene) {
        // Create titled pane for selecting the airport
        this.setText("Step 1: Select Airport");
        this.setExpanded(true);
        this.setCollapsible(false);

        // Create the horizontal box for selecting airport
        var buttonSelectGridPane = new GridPane();
        buttonSelectGridPane.setHgap(5);
        buttonSelectGridPane.setVgap(5);
        buttonSelectGridPane.getColumnConstraints()
            .addAll(new ColumnConstraints(), new ColumnConstraints(),
                new ColumnConstraints());
        this.setContent(buttonSelectGridPane);
    }
}
