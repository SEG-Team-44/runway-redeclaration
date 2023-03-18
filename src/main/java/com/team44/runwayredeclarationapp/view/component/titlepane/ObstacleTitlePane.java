package com.team44.runwayredeclarationapp.view.component.titlepane;

import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.ui.AddObstacleWindow;
import com.team44.runwayredeclarationapp.ui.MainWindow;
import com.team44.runwayredeclarationapp.view.component.inputs.DoubleField;
import com.team44.runwayredeclarationapp.view.component.inputs.SelectComboBox;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * The Titled Pane for selecting the obstacle
 */
public class ObstacleTitlePane extends TitledPane {

    /**
     * Create the titled pane
     *
     * @param mainWindow     the main window
     * @param observableList the observable list containing the current obstacles
     */
    public ObstacleTitlePane(MainWindow mainWindow, ObservableList<Obstacle> observableList) {
        // Create titled pane for selecting the obstacle
        this.setText("Step 3: Select Obstacle");
        this.setExpanded(true);
        this.setCollapsible(false);

        // Create the horizontal box for selecting obstacle
        var buttonSelectGridPane = new GridPane();
        buttonSelectGridPane.setHgap(5);
        buttonSelectGridPane.setVgap(5);
        buttonSelectGridPane.getColumnConstraints()
            .addAll(new ColumnConstraints(), new ColumnConstraints(100),
                new ColumnConstraints());
        this.setContent(buttonSelectGridPane);

        // Create the obstacle select combobox
        SelectComboBox<Obstacle> obstacleSelectComboBox = new SelectComboBox<>(
            observableList);
        obstacleSelectComboBox.setStringMethod(Obstacle::getObstName);
        obstacleSelectComboBox.setPromptText("Select Obstacle");

        // Obstacle inputs
        var obstacleLeftThresholdInput = new DoubleField();
        var obstacleRightThresholdInput = new DoubleField();
        var obstacleFromCentrelineThresholdInput = new DoubleField();

        // Obstacle tooltips
        var obstacleFromCentrelineTooltip = new Tooltip("Positive for North, negative for South");
        obstacleFromCentrelineTooltip.setShowDelay(Duration.millis(50));
        obstacleFromCentrelineThresholdInput.setTooltip(obstacleFromCentrelineTooltip);

        // Obstacle buttons
        var addObstacleBtn = new Button("Add Obstacle");
        addObstacleBtn.setMaxWidth(Double.MAX_VALUE);
        GridPane.setFillWidth(addObstacleBtn, true);
        var editObstacleBtn = new Button("Edit Obstacle");

        // Button events
        addObstacleBtn.setOnAction(event -> {
            AddObstacleWindow addObstacleWindow = new AddObstacleWindow(mainWindow.getStage(),
                observableList);
        });

        // Add the rows to the grid
        buttonSelectGridPane.addRow(1, obstacleSelectComboBox, addObstacleBtn, editObstacleBtn);
        buttonSelectGridPane.addRow(2); // empty row for spacing
        buttonSelectGridPane.addRow(3, new Text("Distnace from Left Threshold:"),
            obstacleLeftThresholdInput);
        buttonSelectGridPane.addRow(4, new Text("Distnace from Right Threshold:"),
            obstacleRightThresholdInput);
        buttonSelectGridPane.addRow(5, new Text("Distnace from Centreline:"),
            obstacleFromCentrelineThresholdInput);
    }
}
