package com.team44.runwayredeclarationapp.view;

import com.team44.runwayredeclarationapp.ui.MainWindow;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
        // Add menu bar
        MenuBar menuBar = new MenuBar();
        var fileMenu = new Menu("File");
        var menuItemTest1 = new MenuItem("Test1");
        var menuItemTest2 = new MenuItem("Test2");

        fileMenu.getItems().addAll(menuItemTest1, menuItemTest2);
        menuBar.getMenus().add(fileMenu);

        // Set up initial pane (everything under the title bar)
        root = new StackPane();
        var windowPane = new BorderPane();
        windowPane.setTop(menuBar);
        root.getChildren().add(windowPane);

        // Set up main pane (everything below the menu bar)
        var mainPane = new HBox();
        windowPane.setCenter(mainPane);

        // Create split screen panes
        var infoPane = new VBox();
        var visualTabPane = new TabPane();
        HBox.setHgrow(infoPane, Priority.ALWAYS);
        HBox.setHgrow(visualTabPane, Priority.ALWAYS);
        mainPane.getChildren().addAll(infoPane, visualTabPane);

        // Create tabs for the runway visualisations
        Tab tab1 = new Tab("Top-down");
        Tab tab2 = new Tab("Side-on");
        visualTabPane.getTabs().addAll(tab1, tab2);
        visualTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        // For testing:
        var testText = new Text("Test");
        infoPane.getChildren().add(testText);
        visualTabPane.setStyle("-fx-background-color: beige");
    }
}
