package com.team44.runwayredeclarationapp.view;

import com.team44.runwayredeclarationapp.ui.MainWindow;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BaseScene {

    protected static final Logger logger = LogManager.getLogger(BaseScene.class);

    protected final MainWindow mainWindow;
    protected Scene scene;
    protected Pane root;

    /**
     * Create scene within the main window
     *
     * @param mainWindow the main window
     */
    protected BaseScene(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * Initialise the scene
     */
    public abstract void initialise();

    /**
     * Create the scene ui layout
     */
    public abstract void build();

    /**
     * Create a scene with the root StackPane as its parent
     *
     * @return the scene
     */
    public Scene setScene() {
        this.scene = new Scene(root);
        loadStyleSheet("main.css");
        return scene;
    }

    /**
     * Add a stylesheet to the scene
     *
     * @param fileName the filename of the stylesheet in the /resources/style folder
     */
    protected void loadStyleSheet(String fileName) {
        scene.getStylesheets().add(getClass().getResource("/style/" + fileName).toExternalForm());
    }

    /**
     * Get the main window
     *
     * @return the main window
     */
    public MainWindow getMainWindow() {
        return mainWindow;
    }
}
