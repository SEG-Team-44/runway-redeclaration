package com.team44.runwayredeclarationapp.ui;

import com.team44.runwayredeclarationapp.controller.DeleteController;
import com.team44.runwayredeclarationapp.event.AddObstacleListener;
import com.team44.runwayredeclarationapp.model.Obstacle;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Modify Obstacle modal window
 */
public class ModifyObstacleWindow {

    private Stage stage;
    private final Window parent;
    /**
     * The observable list containing the obstacles
     */
    private final ObservableList<Obstacle> obstacleObservableList;

    private final DeleteController deleteController = new DeleteController();

    /**
     * The listener called when an obstacle has been successfully edited
     */
    private AddObstacleListener editObstacleListener;

    /**
     * Initialising the stage
     *
     * @param parent                 the parent window
     * @param obstacleObservableList the observable list containing the obstacles
     */
    public ModifyObstacleWindow(Window parent, ObservableList<Obstacle> obstacleObservableList) {
        this.parent = parent;
        this.obstacleObservableList = obstacleObservableList;

        // Pop up option window for user to choose an obstacle
        showOptionScene();
    }

    /**
     * Setup & display the option window
     */
    private void showOptionScene() {
        // Create the select window
        var selectWindow = new SelectWindow<Obstacle>(parent, "Obstacle To Edit",
            obstacleObservableList);
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
        var modifyWindow = new AddObstacleWindow(stage, obstacleObservableList, obstacle);

        // Call listener
        modifyWindow.setAddObstacleListener(modifiedObstacle -> {
            if (editObstacleListener != null) {
                editObstacleListener.addObstacle(modifiedObstacle);
            }
        });
    }

    /**
     * Set the listener to be called when an obstacle has been successfully edited
     *
     * @param editObstacleListener the listener
     */
    public void setEditAirportListener(AddObstacleListener editObstacleListener) {
        this.editObstacleListener = editObstacleListener;
    }

    /**
     * Handle when an obstacle is to be deleted
     *
     * @param obstacle obstacle to be deleted
     */
    private void deleteIsPressed(Obstacle obstacle) {
        deleteController.deleteObstacle(obstacle, obstacleObservableList);

    }
}
