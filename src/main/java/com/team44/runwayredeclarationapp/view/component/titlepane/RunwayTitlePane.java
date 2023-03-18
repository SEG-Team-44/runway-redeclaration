package com.team44.runwayredeclarationapp.view.component.titlepane;

import com.team44.runwayredeclarationapp.ui.InitialiseWindow;
import com.team44.runwayredeclarationapp.ui.ModifyWindow;
import com.team44.runwayredeclarationapp.ui.SelectWindow;
import com.team44.runwayredeclarationapp.view.MainScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * The Titled Pane for selecting the runway
 */
public class RunwayTitlePane extends TitledPane {

    /**
     * Create the titled pane
     *
     * @param mainScene the main scene
     */
    public RunwayTitlePane(MainScene mainScene) {
        // Create titled pane for selecting the runway
        this.setText("Step 2: Select Runway");
        this.setExpanded(true);
        this.setCollapsible(false);

        // Create the horizontal box for selecting runway
        var buttonSelectGridPane = new GridPane();
        buttonSelectGridPane.setHgap(5);
        buttonSelectGridPane.setVgap(5);
        buttonSelectGridPane.getColumnConstraints()
            .addAll(new ColumnConstraints(180), new ColumnConstraints(100),
                new ColumnConstraints(100));
        this.setContent(buttonSelectGridPane);

        //Add runway button
        Button addRunwayBtn = new Button("Add Runway");
        addRunwayBtn.setMaxWidth(Double.MAX_VALUE);
        //Generate init window when button clicked
        addRunwayBtn.setOnAction(ActionEvent -> {
            InitialiseWindow initPage = new InitialiseWindow(mainScene.getMainWindow().getStage(),
                mainScene.getAirport());

            // Set the new runway listener
            initPage.setNewRunwayListener(mainScene::updateInitialRunway);
        });

        Button modifyBtn = new Button("Edit Runway");
        modifyBtn.setMaxWidth(Double.MAX_VALUE);

        modifyBtn.setOnAction(ActionEvent -> {
            if (mainScene.getAirport().getRunways().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("There are no runways recorded on the system.");
                a.show();
            } else {
                ModifyWindow modifyPage = new ModifyWindow(mainScene.getMainWindow().getStage(),
                    mainScene.getAirport());

                // Set the runway listener to update
                modifyPage.setNewRunwayListener(mainScene::updateInitialRunway);
            }
        });

        Button selectBtn = new Button("Select current Runway");

        selectBtn.setOnAction(ActionEvent -> {
            if (mainScene.getAirport().getRunways().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("There are no runways recorded on the system.");
                a.show();
            } else {
                SelectWindow selectPage = new SelectWindow(mainScene.getMainWindow().getStage(),
                    mainScene.getAirport());

                // Set the runway listener to update
                selectPage.setNewRunwayListener(mainScene::updateInitialRunway);
            }
        });

        // Add rows
        buttonSelectGridPane.addRow(0, selectBtn, addRunwayBtn, modifyBtn);
    }
}
