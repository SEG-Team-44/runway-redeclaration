package com.team44.runwayredeclarationapp.view.component.titlepane;

import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.ui.AddObstacleWindow;
import com.team44.runwayredeclarationapp.ui.ModifyObstacleWindow;
import com.team44.runwayredeclarationapp.view.MainScene;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorAlert;
import com.team44.runwayredeclarationapp.view.component.inputs.DoubleField;
import com.team44.runwayredeclarationapp.view.component.inputs.SelectComboBox;
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

    MainScene mainScene;

    /**
     * The input fields
     */
    private final SelectComboBox<Obstacle> obstacleSelectComboBox;
    private final DoubleField obstacleLeftThresholdInput;
    private final DoubleField obstacleRightThresholdInput;
    private final DoubleField obstacleFromCentrelineThresholdInput;

    /**
     * Create the titled pane
     *
     * @param mainScene the main scene
     */
    public ObstacleTitlePane(MainScene mainScene) {
        this.mainScene = mainScene;

        // Create titled pane for selecting the obstacle
        this.setText("Step 3: Select Obstacle");
        this.setExpanded(true);
        this.setCollapsible(true);

        // Create the horizontal box for selecting obstacle
        var buttonSelectGridPane = new GridPane();
        buttonSelectGridPane.setHgap(5);
        buttonSelectGridPane.setVgap(5);
        buttonSelectGridPane.getColumnConstraints()
            .addAll(new ColumnConstraints(180),
                new ColumnConstraints(100),
                new ColumnConstraints(100));
        this.setContent(buttonSelectGridPane);

        // Create the obstacle select combobox
        obstacleSelectComboBox = new SelectComboBox<>(
            mainScene.getObstacleObservableList());
        obstacleSelectComboBox.setStringMethod(Obstacle::getObstName);
        obstacleSelectComboBox.setPromptText("Select Obstacle");
        obstacleSelectComboBox.setMaxWidth(Double.MAX_VALUE);

        // Obstacle inputs
        obstacleLeftThresholdInput = new DoubleField();
        obstacleLeftThresholdInput.setAllowNegative(true);
        obstacleRightThresholdInput = new DoubleField();
        obstacleRightThresholdInput.setAllowNegative(true);
        obstacleFromCentrelineThresholdInput = new DoubleField();
        obstacleFromCentrelineThresholdInput.setAllowNegative(true);

        // Obstacle tooltips
        var obstacleFromCentrelineTooltip = new Tooltip("Positive for North, negative for South");
        obstacleFromCentrelineTooltip.setShowDelay(Duration.millis(50));
        obstacleFromCentrelineThresholdInput.setTooltip(obstacleFromCentrelineTooltip);

        // Obstacle buttons
        var addObstacleBtn = new Button("Add Obstacle");
        addObstacleBtn.setMaxWidth(Double.MAX_VALUE);
        var editObstacleBtn = new Button("Edit Obstacle");
        editObstacleBtn.setMaxWidth(Double.MAX_VALUE);

        // Button events
        editObstacleBtn.setOnAction(event -> {
            ModifyObstacleWindow modifyObstacleWindow = new ModifyObstacleWindow(
                mainScene.getMainWindow().getStage(),
                mainScene.getObstacleObservableList());
            obstacleSelectComboBox.setValue(null);
        });
        addObstacleBtn.setOnAction(event -> {
            AddObstacleWindow addObstacleWindow = new AddObstacleWindow(
                mainScene.getMainWindow().getStage(),
                mainScene.getObstacleObservableList());
        });

        // Add the rows to the grid
        buttonSelectGridPane.addRow(0, obstacleSelectComboBox, addObstacleBtn, editObstacleBtn);
        buttonSelectGridPane.addRow(1); // empty row for spacing
        buttonSelectGridPane.addRow(2, new Text("Distnace from Left Threshold:"),
            obstacleLeftThresholdInput);
        buttonSelectGridPane.addRow(3, new Text("Distnace from Right Threshold:"),
            obstacleRightThresholdInput);
        buttonSelectGridPane.addRow(4, new Text("Distnace from Centreline:"),
            obstacleFromCentrelineThresholdInput);
    }

    /**
     * Get the obstacle that the user has selected
     *
     * @return the selected obstacle
     */
    public Obstacle getSelectedObstacle() {
        return obstacleSelectComboBox.getValue();
    }

    /**
     * Set an obstacle to be selected in the combobox
     *
     * @param obstacle the obstacle
     */
    public void setSelectedObstacle(Obstacle obstacle) {
        obstacleSelectComboBox.setValue(obstacle);
    }

    /**
     * Set the value of the user inputs
     *
     * @param leftThreshold  the distance from the left threshold
     * @param rightThreshold the distance from the right threshold
     * @param centreline     the distance from the centreline
     */
    public void setInputText(double leftThreshold, double rightThreshold, double centreline) {
        this.obstacleLeftThresholdInput.setText(String.valueOf(leftThreshold));
        this.obstacleRightThresholdInput.setText(String.valueOf(rightThreshold));
        this.obstacleFromCentrelineThresholdInput.setText(String.valueOf(centreline));
    }

    /**
     * Get the distance of the object from the left threshold
     *
     * @return the distance from the left threshold
     */
    public double getObstacleLeftThreshold() {
        return obstacleLeftThresholdInput.getValue();
    }

    /**
     * Get the distance of the object from the right threshold
     *
     * @return the distance from the right threshold
     */
    public double getObstacleRightThreshold() {
        return obstacleRightThresholdInput.getValue();
    }

    /**
     * Get the distance of the object from the centreline
     *
     * @return the distance from the centreline
     */
    public double getObstacleFromCentrelineThreshold() {
        return obstacleFromCentrelineThresholdInput.getValue();
    }

    /**
     * Check if inputs are valid and show necessary errors
     *
     * @return whether the inputs are valid
     */
    public boolean checkInputsValid() {
        // Create an alert
        ErrorAlert errorAlert = new ErrorAlert();

        // Add the corresponding error messages
        if (obstacleSelectComboBox.getValue() == null) {
            errorAlert.addError("Select an obstacle.");
        }
        if (!obstacleLeftThresholdInput.isInputValid()) {
            errorAlert.addError(
                "Left Threshold input cannot be empty and must be a numerical value.");
        }
        if (!obstacleRightThresholdInput.isInputValid()) {
            errorAlert.addError(
                "Right Threshold input cannot be empty and must be a numerical value.");
        }
        if (!obstacleFromCentrelineThresholdInput.isInputValid()) {
            errorAlert.addError(
                "Left Threshold input cannot be empty and must be a numerical value.");
        }
        // todo:: check if threshL and threshR add up to runway length

        // Show the error
        var numberOfErrors = errorAlert.getErrors().size();
        errorAlert.show();

        // Return whether the inputs are valid or not
        return numberOfErrors == 0;
    }

    /**
     * Clear all the inputs
     */
    public void clearInputs() {
        obstacleSelectComboBox.setValue(null);
        obstacleLeftThresholdInput.clear();
        obstacleRightThresholdInput.clear();
        obstacleFromCentrelineThresholdInput.clear();
    }
}