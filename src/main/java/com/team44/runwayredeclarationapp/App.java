package com.team44.runwayredeclarationapp;

import com.team44.runwayredeclarationapp.ui.MainWindow;
import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Starting scene");

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        var mainWindow = new MainWindow(stage, screenSize.getWidth() - 100,
            screenSize.getHeight() - 100);

        stage.show();
    }

    public static void main(String[] args) {
        logger.info("Starting client");
        launch();
    }
}