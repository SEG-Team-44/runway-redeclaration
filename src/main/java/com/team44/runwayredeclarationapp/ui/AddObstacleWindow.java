package com.team44.runwayredeclarationapp.ui;

import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorAlert;
import com.team44.runwayredeclarationapp.view.component.inputs.DoubleField;
import com.team44.runwayredeclarationapp.view.component.inputs.RegexField;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * The add obstacle window
 */
public class AddObstacleWindow {

    /**
     * The input taking in the obstacle name
     */
    private final RegexField obstacleNameInput;
    /**
     * The input taking in the obstacle height
     */
    private final DoubleField obstacleHeightInput;
    /**
     * The error alert
     */
    private final ErrorAlert errorAlert = new ErrorAlert();

    /**
     * Create a new Add Obstacle window
     *
     * @param parent                 the parent window
     * @param obstacleObservableList the observable list of current obstacles
     */
    public AddObstacleWindow(Window parent, ObservableList<Obstacle> obstacleObservableList) {
        this(parent, obstacleObservableList, null);
    }

    /**
     * Create a new Add Obstacle window
     *
     * @param parent                 the parent window
     * @param obstacleObservableList the observable list of current obstacles
     * @param obstacle               the existing obstacle, if any (else null)
     */
    public AddObstacleWindow(Window parent, ObservableList<Obstacle> obstacleObservableList,
        Obstacle obstacle) {
        Stage stage = new Stage();

        //Setup main pane
        GridPane mainPane = new GridPane();
        mainPane.setAlignment(Pos.CENTER);

        // Grid properties
        mainPane.setPadding(new Insets(5, 5, 5, 5));
        mainPane.setHgap(15);
        mainPane.setVgap(10);

        // Add button
        Button addBtn = new Button(obstacle == null ? "Add" : "Update");
        addBtn.setFont(new Font(17));
        GridPane.setHalignment(addBtn, HPos.RIGHT);
        GridPane.setValignment(addBtn, VPos.BOTTOM);

        // Inputs
        String obstacleNameRegex = "^.{0,29}$";
        obstacleNameInput = new RegexField(obstacleNameRegex);
        obstacleHeightInput = new DoubleField();

        // If there is an existing obstacle, set its values
        if (obstacle != null) {
            obstacleNameInput.setText(obstacle.getObstName());
            obstacleHeightInput.setText(String.valueOf(obstacle.getHeight()));
        }

        // Add event
        addBtn.setOnAction(event -> {
            if (isInputValid()) {

                if (obstacle == null) {
                    // Create and add the obstacle to the observable list
                    obstacleObservableList.add(
                        new Obstacle(obstacleNameInput.getText(), obstacleHeightInput.getValue())
                    );
                } else {
                    // Update the obstacle in the observable list
                    var newObstacle = new Obstacle(obstacleNameInput.getText(),
                        obstacleHeightInput.getValue());
                    var index = obstacleObservableList.indexOf(obstacle);

                    obstacleObservableList.set(index, newObstacle);
                }

                // Close the window
                stage.close();
            } else {
                // Show alert with errors if input is not valid
                errorAlert.show();
            }
        });

        // Add rows to grid
        mainPane.addRow(1, new Text("Obstacle Name:"), obstacleNameInput);
        mainPane.addRow(2, new Text("Obstacle Height:"), obstacleHeightInput);
        mainPane.add(addBtn, 0, 3, 2, 1);

        //Set scene
        Scene scene = new Scene(mainPane);
        mainPane.requestFocus();
        scene.setOnMouseClicked(mouseEvent -> mainPane.requestFocus());

        // Set stage properties and make it a modal window
        stage.setTitle((obstacle == null ? "Add" : "Update") + " obstacle");
        stage.setScene(scene);
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        stage.show();
    }

    /**
     * Check if the user inputs are valid with the regex and not empty
     *
     * @return whether the inputs are valid
     */
    private Boolean isInputValid() {
        // Check the regex and text empty
        var checkRegex = obstacleNameInput.isRegexMatch() &&
            obstacleHeightInput.isRegexMatch();
        var checkEmpty =
            obstacleNameInput.isEmpty() || obstacleHeightInput.isEmpty();

        // Add error messages if needed
        if (!checkRegex) {
            errorAlert.addError(
                """
                    Input cannot be validated:\s
                     - Name must be under 30 characters.
                     - Height must be under 10,000m and rounded to 2 decimal places.""");
        }
        if (checkEmpty) {
            errorAlert.addError("Inputs cannot be empty.");
        }

        // Return if the inputs are valid or not
        return checkRegex && !checkEmpty;
    }

}
