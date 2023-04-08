package com.team44.runwayredeclarationapp.ui.obstacle;

import com.team44.runwayredeclarationapp.controller.DataController;
import com.team44.runwayredeclarationapp.event.AddObstacleListener;
import com.team44.runwayredeclarationapp.model.ErrorObjectPair;
import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.model.form.ObstacleForm;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorAlert;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorListAlert;
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
     * The form containing the fields to add a new obstacle
     */
    private final ObstacleForm obstacleForm = new ObstacleForm();
    /**
     * The listener called when an obstacle has been added/edited
     */
    private AddObstacleListener addObstacleListener;

    /**
     * Create a new Add Obstacle window
     *
     * @param parent         the parent window
     * @param dataController the data controller
     */
    public AddObstacleWindow(Window parent, DataController dataController) {
        this(parent, dataController, null);
    }

    /**
     * Create a new Add Obstacle window
     *
     * @param parent         the parent window
     * @param dataController the data controller
     * @param obstacle       the existing obstacle, if any (else null)
     */
    public AddObstacleWindow(Window parent, DataController dataController,
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

        // If there is an existing obstacle, set its values
        if (obstacle != null) {
            obstacleForm.setObstacle(obstacle);
        }

        // Add event
        addBtn.setOnAction(event -> {
            if (isInputValid()) {

                ErrorObjectPair<Obstacle> validationErrors;
                if (obstacle == null) {
                    // Add Obstacle
                    validationErrors = dataController.addObstacle(
                        obstacleForm.getObstacleNameInput().getText(),
                        obstacleForm.getObstacleHeightInput().getValue());
                } else {
                    // Edit obstacle
                    validationErrors = dataController.editObstacle(obstacle,
                        obstacleForm.getObstacleNameInput().getText(),
                        obstacleForm.getObstacleHeightInput().getValue());
                }

                // Check if there are any validation errors
                if (!validationErrors.hasErrors()) {
                    // Call the listener
                    if (addObstacleListener != null) {
                        addObstacleListener.addObstacle(validationErrors.getObject());
                    }
                    stage.close();
                } else {
                    // Display errors
                    var errorAlert = new ErrorListAlert();
                    errorAlert.setErrors(validationErrors.getErrorsArray());
                    errorAlert.show();
                }
            }
        });

        // Add rows to grid
        mainPane.addRow(1, new Text("Obstacle Name:"), obstacleForm.getObstacleNameInput());
        mainPane.addRow(2, new Text("Obstacle Height:"), obstacleForm.getObstacleHeightInput());
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
        // Add error messages if needed
        if (!obstacleForm.isValid()) {
            new ErrorAlert("Invalid inputs", "Provided obstacle inputs are invalid!",
                "Ensure that the name field is not empty and under 30 characters.\n"
                    + "Ensure that the height field is not empty and rounded to 2 decimal places.").show();
        }

        return obstacleForm.isValid();
    }

    /**
     * Set the listener to be called when an obstacle is added/edited
     *
     * @param addObstacleListener the listener
     */
    public void setAddObstacleListener(AddObstacleListener addObstacleListener) {
        this.addObstacleListener = addObstacleListener;
    }
}
