package com.team44.runwayredeclarationapp.view;

import com.team44.runwayredeclarationapp.ui.InitialiseWindow;
import com.team44.runwayredeclarationapp.ui.ModifyWindow;
import com.team44.runwayredeclarationapp.controller.RecalculationController;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.ui.MainWindow;
import com.team44.runwayredeclarationapp.view.component.SideOnView;
import com.team44.runwayredeclarationapp.view.component.TopDownView;
import com.team44.runwayredeclarationapp.view.component.ValuesGrid;
import com.team44.runwayredeclarationapp.view.component.VisualisationBase;
import com.team44.runwayredeclarationapp.view.component.VisualisationPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TabPane.TabDragPolicy;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * The main scene that will be shown when the user opens the program
 */
public class MainScene extends BaseScene {

    /**
     * The selected airport
     */
    private Airport airport = new Airport();

    /**
     * The controller responsible for setting the recalculated values
     */
    private RecalculationController recalculationController = new RecalculationController();

    /**
     * The grid displaying the original and recalculated values
     */
    private ValuesGrid ogValuesGrid, newValuesGrid;

    /**
     * The canvas displaying the top down and side on view
     */
    private VisualisationBase topDownCanvas, sideOnCanvas;

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

        // Set up the main pane
        root = new StackPane();
        var mainPane = new BorderPane();
        mainPane.setTop(menuBar);
        root.getChildren().add(mainPane);

        // Create split screen panes
        var infoPane = new VBox();
        var visualTabPane = new TabPane();
        mainPane.setLeft(infoPane);
        mainPane.setRight(visualTabPane);
        infoPane.getStyleClass().add("info-pane");
        infoPane.setSpacing(10);
        visualTabPane.getStyleClass().add("visual-tab-pane");

        // Set the visualisation tab size
        visualTabPane.setMinWidth(mainWindow.getWidth() / 2);
        // Always make the visualisation tab half the size of the window
        root.widthProperty().addListener(
            (observableValue, oldWidth, newWidth) -> visualTabPane.setPrefWidth(
                newWidth.doubleValue() / 2));

        // Set tab properties
        visualTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        visualTabPane.setTabDragPolicy(TabDragPolicy.REORDER);

        // Create tabs for the runway visualisations
        Tab tab1 = new Tab("Top-down view");
        Tab tab2 = new Tab("Side-on view");
        visualTabPane.getTabs().addAll(tab1, tab2);

        // Add the visualisations to the tabs
        topDownCanvas = new TopDownView(0, 0);
        sideOnCanvas = new SideOnView(0, 0);
        var topDownPane = new VisualisationPane(topDownCanvas);
        var sideOnPane = new VisualisationPane(sideOnCanvas);
        tab1.setContent(topDownPane);
        tab2.setContent(sideOnPane);

        // For testing:
        var testText = new Text("Test");
        var titledPane = new TitledPane();
        titledPane.setText("Input Obstacle");
        var testBox = new VBox();
        titledPane.setContent(testBox);
        VBox.setVgrow(titledPane, Priority.ALWAYS);
        infoPane.getChildren().addAll(testText, titledPane);

        //Add runway button
        Button addRunwayBtn = new Button("Log in New Runway");
        //Generate init window when button clicked
        addRunwayBtn.setOnAction(ActionEvent -> {
            InitialiseWindow initPage = new InitialiseWindow(mainWindow.getStage(),
                airport);

            // Set the new runway listener
            initPage.setNewRunwayListener(this::setNewRunway);
        });
        infoPane.getChildren().add(addRunwayBtn);

        Button modifyBtn = new Button("Modify Runway");

        modifyBtn.setOnAction(ActionEvent -> {
            if (airport.getRunways().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("There are no runways recorded on the system.");
                a.show();
            } else {
                ModifyWindow modifyPage = new ModifyWindow(mainWindow.getStage(), airport);

                // Set the runway listener to update
                modifyPage.setNewRunwayListener(this::setNewRunway);
            }
        });
        infoPane.getChildren().add(modifyBtn);

        // Testing -
        var loadRunwayBtn = new Button("Load Runway (testing)");
        var recalculateBtn = new Button("Recalculate (testing)");
        recalculateBtn.setDisable(true);
        infoPane.getChildren().addAll(loadRunwayBtn, recalculateBtn);

        loadRunwayBtn.setOnAction((e) -> {
            logger.info("Load runway button pressed (testing)");
            recalculateBtn.setDisable(false);

            // 09R
            topDownCanvas.setInitialParameters(3660, 3660, 3660, 3660, 3353, 3660, 3660, 3660, 3660,
                307, 0, 0, 0, 0, 0);
            sideOnCanvas.setInitialParameters(3660, 3660, 3660, 3660, 3353, 3660, 3660, 3660, 3660,
                307, 0, 0, 0, 0, 0);

            // 09L
//            topDownCanvas.setInitialParameters(3902, 3902, 3902, 3902, 3595, 3884, 3962, 3884, 3884,
//                306, 0, 0, 0, 78,
//                0);
//            sideOnCanvas.setInitialParameters(3902, 3902, 3902, 3902, 3595, 3884, 3962, 3884, 3884,
//                306, 0, 0, 0, 78,
//                0);
        });

        recalculateBtn.setOnAction((e) -> {
            logger.info("Recalculate button pressed (testing)");

            // 09R
            topDownCanvas.setRecalculatedParameters(1850, 1850, 1850, 2553, 2860, 2860, 2860, 1850,
                25 * 50, 60, 240, 300, 2853 + 307, 25, false);
            sideOnCanvas.setRecalculatedParameters(1850, 1850, 1850, 2553, 2860, 2860, 2860, 1850,
                25 * 50, 60, 240, 300, 2853 + 307, 25, false);
            // Parameters of obstacle on the left
            topDownCanvas.setRecalculatedParameters(2903, 2903, 2903, 2393, 2393, 2393, 2393, 2903,
                15 * 50, 60, 240, 300, 150 + 307, 15, true);
            sideOnCanvas.setRecalculatedParameters(2903, 2903, 2903, 2393, 2393, 2393, 2393, 2903,
                15 * 50, 60, 240, 300, 150 + 307, 15, true);

            // 09L
//            topDownCanvas.setRecalculatedParameters(3346, 3346, 3346, 2985, 2986, 2986, 2986, 3346,
//                12 * 50, 60, 240, 300, 306 - 50, 12, true);
//            sideOnCanvas.setRecalculatedParameters(3346, 3346, 3346, 2985, 2986, 2986, 2986, 3346,
//                12 * 50, 60, 240, 300, 306 - 50, 12, true);
            // Parameters of obstacle on right below
//            topDownView.setRecalculatedParameters(2792, 2792, 2792, 3246, 3534, 3612, 3534, 2774,
//                20 * 50, 60, 240, 300, 3546 + 306, 20, false);
//            sideOnCanvas.setRecalculatedParameters(2792, 2792, 2792, 3246, 3534, 3612, 3534, 2774,
//                20 * 50, 60, 240, 300, 3546 + 306, 20, false);

        });

        // Set the listener to handle when parameters have been recalculated
        recalculationController.setRecalculatedViewListener(() -> {
        });

        // Create horizontal box for both value grid
        var valueGridsBox = new HBox();
        valueGridsBox.setSpacing(30);

        // Create the value grid
        ogValuesGrid = new ValuesGrid("Original Values:");
        newValuesGrid = new ValuesGrid("Recalculated Values:");

        // Add the grids to the information pane
        valueGridsBox.getChildren().addAll(ogValuesGrid, newValuesGrid);
        infoPane.getChildren().add(valueGridsBox);
    }

    /**
     * Set the runway that the user has selected/updated
     *
     * @param runway the runway object
     */
    private void setNewRunway(Runway runway) {
        ogValuesGrid.setRunway(runway);
    }

    /**
     * Set the initial runway to show on the visualisation tabs
     *
     * @param runway the runway object
     */
    private void setVisualisationInitialRunway(Runway runway) {
    }

    private void setVisualisationReCalculatedRunway() {
    }
}
