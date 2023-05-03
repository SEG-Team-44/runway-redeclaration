package com.team44.runwayredeclarationapp.view.component.titlepane;

import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.ui.obstacle.AddObstacleWindow;
import com.team44.runwayredeclarationapp.ui.obstacle.ModifyObstacleWindow;
import com.team44.runwayredeclarationapp.view.MainScene;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorListAlert;
import com.team44.runwayredeclarationapp.view.component.inputs.DoubleField;
import com.team44.runwayredeclarationapp.view.component.inputs.SelectComboBox;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.StringConverter;

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
    private final DoubleField blastProtection;

    /**
     * Store the previously selected obstacle
     */
    private Obstacle previouslySelectedObstacle = null;

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

        // Combobox selection event
        obstacleSelectComboBox.valueProperty().addListener((obs, oldObstacle, newObstacle) -> {
            if (newObstacle != null) {
                this.previouslySelectedObstacle = newObstacle;
            }
        });

        // Obstacle inputs
        obstacleLeftThresholdInput = new DoubleField();
        obstacleLeftThresholdInput.setAllowNegative(true);
        obstacleLeftThresholdInput.setDisable(true);

        obstacleRightThresholdInput = new DoubleField();
        obstacleRightThresholdInput.setAllowNegative(true);
        obstacleRightThresholdInput.setDisable(true);

        obstacleFromCentrelineThresholdInput = new DoubleField();
        obstacleFromCentrelineThresholdInput.setAllowNegative(true);
        blastProtection = new DoubleField();

        // Obstacle tooltips
        var obstacleFromCentrelineTooltip = new Tooltip("Positive for North, negative for South");
        obstacleFromCentrelineTooltip.setShowDelay(Duration.millis(50));
        obstacleFromCentrelineThresholdInput.setTooltip(obstacleFromCentrelineTooltip);

        // Obstacle buttons
        var addObstacleBtn = new Button("Add Obstacle");
        addObstacleBtn.setMaxWidth(Double.MAX_VALUE);
        var editObstacleBtn = new Button("Edit Obstacle");
        editObstacleBtn.setMaxWidth(Double.MAX_VALUE);

        // Runway selection event for left/right distance inputs
        mainScene.getSelectedRunwayProperty().addListener((obs, oldRunway, newRunway) -> {
            if (oldRunway != null && oldRunway.equals(newRunway)) {
                return;
            } else {
                // Clear input if runway changed
                obstacleLeftThresholdInput.clear();
                obstacleRightThresholdInput.clear();
            }

            // Disable button if no runway selected
            if (newRunway == null) {
                obstacleLeftThresholdInput.setDisable(true);
                obstacleRightThresholdInput.setDisable(true);
            } else {
                // A runway has been selected
                obstacleLeftThresholdInput.setDisable(false);
                obstacleRightThresholdInput.setDisable(false);

                // Get the logical ids
                var runway1 = newRunway.getLogicId1();
                var runway2 = newRunway.getLogicId2();

                // Unbind any previous bindings
                obstacleLeftThresholdInput.textProperty()
                    .unbindBidirectional(obstacleRightThresholdInput.textProperty());

                // Create the converter for automatically setting left/right distance inputs
                var converter = new StringConverter<String>() {
                    /**
                     * Input left
                     */
                    @Override
                    public String toString(String value) {
                        if (value.isEmpty() || value.equals("-")) {
                            return "";
                        } else {
                            var doubleValue = Double.parseDouble(value);
                            var newValue =
                                newRunway.getRunwayL() - newRunway.getDisThresh(runway1)
                                    - newRunway.getDisThresh(runway2)
                                    - doubleValue;
                            return Double.toString(newValue);
                        }
                    }

                    /**
                     * Input right
                     */
                    @Override
                    public String fromString(String value) {
                        if (value.isEmpty() || value.equals("-")) {
                            return "";
                        } else {
                            double doubleValue = Double.parseDouble(value);
                            var newValue =
                                newRunway.getRunwayL() - newRunway.getDisThresh(runway1)
                                    - newRunway.getDisThresh(runway2)
                                    - doubleValue;
                            return Double.toString(newValue);
                        }
                    }
                };

                // Create binding for the inputs
                Bindings.bindBidirectional(obstacleLeftThresholdInput.textProperty(),
                    obstacleRightThresholdInput.textProperty(), converter);
            }
        });

        // Button events
        editObstacleBtn.setOnAction(event -> {
            // Open modify window
            ModifyObstacleWindow modifyObstacleWindow = new ModifyObstacleWindow(
                mainScene.getMainWindow().getStage(),
                mainScene.getDataController());

            // Set the listener
            modifyObstacleWindow.setEditObstacleListener((initialObstacle, modifiedObstacle) -> {
                if (initialObstacle.equals(previouslySelectedObstacle)) {
                    // Set the modified obstacle to the combobox
                    obstacleSelectComboBox.setValue(modifiedObstacle);
                }
            });
        });

        addObstacleBtn.setOnAction(event -> {
            AddObstacleWindow addObstacleWindow = new AddObstacleWindow(
                mainScene.getMainWindow().getStage(),
                mainScene.getDataController());

            // Set the listener
            addObstacleWindow.setAddObstacleListener(obstacleSelectComboBox::setValue);
        });

        // Add the rows to the grid
        buttonSelectGridPane.addRow(0, obstacleSelectComboBox, addObstacleBtn, editObstacleBtn);
        buttonSelectGridPane.addRow(1); // empty row for spacing
        buttonSelectGridPane.addRow(2, new Text("Distance from Left Threshold:"),
            obstacleLeftThresholdInput);
        buttonSelectGridPane.addRow(3, new Text("Distance from Right Threshold:"),
            obstacleRightThresholdInput);
        buttonSelectGridPane.addRow(4, new Text("Distance from Centreline:"),
            obstacleFromCentrelineThresholdInput);
        buttonSelectGridPane.addRow(5, new Text("Blast Protection:"), blastProtection);
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
     * @param blastPro       current blast protection
     */
    public void setInputText(double leftThreshold, double rightThreshold, double centreline,
        double blastPro) {
        this.obstacleLeftThresholdInput.setText(String.valueOf(leftThreshold));
        this.obstacleRightThresholdInput.setText(String.valueOf(rightThreshold));
        this.obstacleFromCentrelineThresholdInput.setText(String.valueOf(centreline));
        this.blastProtection.setText(String.valueOf(blastPro));
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
     * Get the current blast protection
     *
     * @return blast protection value
     */
    public double getBlastProtection() {
        return blastProtection.getValue();
    }

    /**
     * Check if inputs are valid and show necessary errors
     *
     * @return whether the inputs are valid
     */
    public boolean checkInputsValid() {
        // Create an alert
        ErrorListAlert errorListAlert = new ErrorListAlert();

        // Add the corresponding error messages
        if (obstacleSelectComboBox.getValue() == null) {
            errorListAlert.addError("Select an obstacle.");
        }
        if (!obstacleLeftThresholdInput.isInputValid()) {
            errorListAlert.addError(
                "Left Threshold input cannot be empty and must be a numerical value.");
        }
        if (!obstacleRightThresholdInput.isInputValid()) {
            errorListAlert.addError(
                "Right Threshold input cannot be empty and must be a numerical value.");
        }
        if (!obstacleFromCentrelineThresholdInput.isInputValid()) {
            errorListAlert.addError(
                "Left Threshold input cannot be empty and must be a numerical value.");
        }
        if (!blastProtection.isInputValid()) {
            errorListAlert.addError(
                "Blast protection cannot be empty and must be a non-negative numerical value.");
        }

        // Show the error
        var numberOfErrors = errorListAlert.getErrors().size();
        errorListAlert.show();

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
        blastProtection.clear();
    }
}
