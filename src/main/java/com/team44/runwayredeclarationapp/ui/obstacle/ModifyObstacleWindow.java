package com.team44.runwayredeclarationapp.ui.obstacle;

import com.team44.runwayredeclarationapp.controller.DataController;
import com.team44.runwayredeclarationapp.event.EditObstacleListener;
import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.ui.SelectWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Modify Obstacle modal window
 */
public class ModifyObstacleWindow {

    private Stage stage;
    private final Window parent;
    /**
     * The data controller for editing an obstacle
     */
    private final DataController dataController;

    /**
     * The listener called when an obstacle has been successfully edited
     */
    private EditObstacleListener editObstacleListener;

    /**
     * Initialising the stage
     *
     * @param parent         the parent window
     * @param dataController the data controller
     */
    public ModifyObstacleWindow(Window parent, DataController dataController) {
        this.parent = parent;
        this.dataController = dataController;

        // Pop up option window for user to choose an obstacle
        showOptionScene();
    }

    /**
     * Setup & display the option window
     */
    private void showOptionScene() {
        // Create the select window
        var selectWindow = new SelectWindow<Obstacle>(parent, "Obstacle To Edit",
            dataController.getObstacleObservableList());
        selectWindow.setStringMethod(Obstacle::getObstName);

        // On select event
        selectWindow.setOnSelect((obstacle) -> {
            showModifyScene((Obstacle) obstacle);
        });

        //On delete event
        selectWindow.setOnDelete((obstacle) -> {
            deleteIsPressed((Obstacle) obstacle);
        });

        this.stage = selectWindow;
    }

    /**
     * Setup & display the UI for user to update the values for the selected obstacle
     *
     * @param obstacle the obstacle selected
     */
    private void showModifyScene(Obstacle obstacle) {
        var initialObstacle = obstacle.clone();
        var modifyWindow = new AddObstacleWindow(stage, dataController, obstacle);

        // Call listener
        modifyWindow.setAddObstacleListener(modifiedObstacle -> {
            if (editObstacleListener != null) {
                editObstacleListener.editObstacle(initialObstacle, modifiedObstacle);
            }
        });
    }

    /**
     * Set the listener to be called when an obstacle has been successfully edited
     *
     * @param editObstacleListener the listener
     */
    public void setEditObstacleListener(EditObstacleListener editObstacleListener) {
        this.editObstacleListener = editObstacleListener;
    }

    /**
     * Handle when an obstacle is to be deleted
     *
     * @param obstacle obstacle to be deleted
     */
    private void deleteIsPressed(Obstacle obstacle) {
        dataController.deleteObstacle(obstacle);

    }
}
