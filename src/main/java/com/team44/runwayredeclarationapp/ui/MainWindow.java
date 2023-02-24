package com.team44.runwayredeclarationapp.ui;

import com.team44.runwayredeclarationapp.view.BaseScene;
import com.team44.runwayredeclarationapp.view.MainScene;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainWindow {

    private static final Logger logger = LogManager.getLogger(MainWindow.class);

    private final Stage stage;
    private BaseScene currentScene;

    private final double width;
    private final double height;

    public MainWindow(Stage stage, double width, double height) {
        this.stage = stage;
        this.width = width;
        this.height = height;

        stage.setTitle("Runway Redeclaration");
        stage.setWidth(width);
        stage.setHeight(height);
        // todo:: set minimum size

        // set up default scene
        Scene scene = new Scene(new Pane());
        stage.setScene(scene);

        startMainScene();
    }

    private void startMainScene() {
        loadScene(new MainScene(this));
    }

    private void loadScene(BaseScene sceneToLoad) {
        logger.info("Loading scene - " + sceneToLoad.getClass().getName());
        currentScene = sceneToLoad;

        sceneToLoad.build();
        Scene scene = sceneToLoad.setScene();
        stage.setScene(scene);

        Platform.runLater(() -> currentScene.initialise());
    }


    /**
     * Get the window width
     *
     * @return the window width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Get the window height
     *
     * @return the window height
     */
    public double getHeight() {
        return height;
    }
}
