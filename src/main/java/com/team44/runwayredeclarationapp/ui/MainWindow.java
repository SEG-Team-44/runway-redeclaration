package com.team44.runwayredeclarationapp.ui;

import com.team44.runwayredeclarationapp.view.BaseScene;
import com.team44.runwayredeclarationapp.view.MainScene;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The main window that will contain all the UI elements
 */
public class MainWindow {

    private static final Logger logger = LogManager.getLogger(MainWindow.class);

    private final Stage stage;
    private BaseScene currentScene;

    private final double width;
    private final double height;

    /**
     * Create the main window of the program
     *
     * @param stage  the stage of the window
     * @param width  the width of the window
     * @param height the height of the window
     */
    public MainWindow(Stage stage, double width, double height) {
        this.stage = stage;
        this.width = width;
        this.height = height;

        setupStage();

        // Set up default scene
        Scene scene = new Scene(new Pane());
        stage.setScene(scene);

        startMainScene();
    }

    /**
     * Set up the stage
     */
    private void setupStage() {
        stage.setTitle("Runway Redeclaration");
        stage.setMinWidth(width);
        stage.setMinHeight(height);

        // Set icon
        stage.getIcons()
            .add(new Image(String.valueOf(getClass().getResource("/images/logo.png"))));

        // Set max size to max screen size
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        stage.setMaxWidth(screenSize.getWidth());
        stage.setMaxHeight(screenSize.getHeight());
    }

    /**
     * Start and load the main scene
     */
    private void startMainScene() {
        loadScene(new MainScene(this));
    }

    /**
     * Build and initialise a specified scene into the stage
     *
     * @param sceneToLoad the scene to be loaded
     */
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

    /**
     * Get the window stage
     *
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }
}
