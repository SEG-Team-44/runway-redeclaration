package com.team44.runwayredeclarationapp.ui;

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
        var selectWindow = new SelectWindow<Obstacle>(parent, "Obstacle", obstacleObservableList);
        selectWindow.setStringMethod(Obstacle::getObstName);

        // On select event
        selectWindow.setOnSelect((obstacle) -> {
            showModifyScene((Obstacle) obstacle);
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
    }
}
