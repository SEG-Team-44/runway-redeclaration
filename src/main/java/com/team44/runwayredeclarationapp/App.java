package com.team44.runwayredeclarationapp;

import com.team44.runwayredeclarationapp.ui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The main application to be executed
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);

    /**
     * Load the main window on the specified stage
     *
     * @param stage the stage
     */
    @Override
    public void start(Stage stage) {
        logger.info("Starting scene");

        var mainWindow = new MainWindow(stage, 1200, 690);
        stage.show();
    }

    /**
     * Launch the program
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        logger.info("Starting client");
        launch();
    }
}
