package com.team44.runwayredeclarationapp.view;

import com.team44.runwayredeclarationapp.ui.MainWindow;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class MainScene extends BaseScene {

    /**
     * Create scene within the main window
     *
     * @param mainWindow the main window
     */
    public MainScene(MainWindow mainWindow) {
        super(mainWindow);
    }

    /**
     * Initialise the scene
     */
    @Override
    public void initialise() {

    }

    /**
     * Create the scene ui layout
     */
    @Override
    public void build() {
        root = new StackPane();

        var testText = new Text("Test");
        root.getChildren().add(testText);
    }
}
