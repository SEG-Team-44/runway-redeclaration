package com.team44.runwayredeclarationapp.ui;

import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.view.component.alert.ConfirmAlert;
import com.team44.runwayredeclarationapp.view.component.alert.InfoAlert;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Optional;

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
    }

    /**
     * Handle when an obstacle is to be deleted
     * @param obstacle obstacle to be deleted
     */
    private void deleteIsPressed(Obstacle obstacle) {
        Alert confirmAlert = new ConfirmAlert("Confirmation", "Do you want to remove " +
                obstacle.getObstName() + " from the system?");

        //delete the obstacle if OK btn pressed
        Optional<ButtonType> btnType = confirmAlert.showAndWait();
        if (btnType.get() == ButtonType.OK) {
            obstacleObservableList.remove(obstacle);

            //inform user that deletion is successful
            InfoAlert infoAlert = new InfoAlert("Delete successful", "Delete successful",
                    obstacle.getObstName() + " has been removed");
            infoAlert.show();
        }
    }
}
