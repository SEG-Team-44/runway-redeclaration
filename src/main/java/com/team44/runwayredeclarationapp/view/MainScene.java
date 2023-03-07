package com.team44.runwayredeclarationapp.view;

import com.team44.runwayredeclarationapp.controller.InitialiseWindow;
import com.team44.runwayredeclarationapp.controller.ModifyWindow;
import com.team44.runwayredeclarationapp.controller.RecalculationController;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Obstacles;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.ui.MainWindow;
import com.team44.runwayredeclarationapp.view.component.SideOnView;
import com.team44.runwayredeclarationapp.view.component.Title;
import com.team44.runwayredeclarationapp.view.component.TopDownView;
import com.team44.runwayredeclarationapp.view.component.ValuesGrid;
import com.team44.runwayredeclarationapp.view.component.VisualisationBase;
import com.team44.runwayredeclarationapp.view.component.VisualisationPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * The main scene that will be shown when the user opens the program
 */
public class MainScene extends BaseScene {

    /**
     * The selected airport
     */
    private Airport airport = new Airport();

    /**
     * The selected runway
     */
    private Runway selectedRunway;

    /**
     * The selected obstacle
     */
    private Obstacles selectedObstacle;

    /**
     * The controller responsible for setting the recalculated values
     */
    private final RecalculationController recalculationController = new RecalculationController();

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

        // Create a menu for selecting scenarios to test the program with
        var testDevMenu = new Menu("Test (for devs)");
        var scenario1MenuItem = new MenuItem("Select Scenario 1");
        var scenario2MenuItem = new MenuItem("Select Scenario 2");
        var scenario3MenuItem = new MenuItem("Select Scenario 3");
        var scenario4MenuItem = new MenuItem("Select Scenario 4");

        // Set the actions for the menu item buttons
        scenario1MenuItem.setOnAction(event -> setTestScenario(1));
        scenario2MenuItem.setOnAction(event -> setTestScenario(2));
        scenario3MenuItem.setOnAction(event -> setTestScenario(3));
        scenario4MenuItem.setOnAction(event -> setTestScenario(4));

        // Add all the scenario buttons to the menu
        testDevMenu.getItems()
            .addAll(scenario1MenuItem, scenario2MenuItem, scenario3MenuItem, scenario4MenuItem);
        menuBar.getMenus().addAll(fileMenu, testDevMenu);

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

        // Input section
        var inputSectionTitle = new Title("Input:");

        // Input runway pane
        var inputRunwayPane = new TitledPane();
        inputRunwayPane.setText("Select Runway");
        var inputRunwayPaneBox = new VBox();

        // Input runway pane (button layout)
        var inputRunwayPaneButtonBox = new HBox();
        inputRunwayPaneButtonBox.setSpacing(10);
        inputRunwayPane.setContent(inputRunwayPaneBox);
        inputRunwayPaneBox.getChildren().add(inputRunwayPaneButtonBox);

        // Input obstacle pane
        var inputObstaclePane = new TitledPane();
        inputObstaclePane.setText("Select Obstacle");
        var inputObstaclePaneBox = new VBox();
        inputObstaclePane.setContent(inputObstaclePaneBox);

        infoPane.getChildren().addAll(inputSectionTitle, inputRunwayPane, inputObstaclePane);

        //Add runway button
        Button addRunwayBtn = new Button("Add New Runway");
        //Generate init window when button clicked
        addRunwayBtn.setOnAction(ActionEvent -> {
            InitialiseWindow initPage = new InitialiseWindow(mainWindow.getStage(),
                airport);

            // Set the new runway listener
            initPage.setNewRunwayListener(this::updateInitialRunway);
        });
        inputRunwayPaneButtonBox.getChildren().add(addRunwayBtn);

        Button modifyBtn = new Button("Modify Runway");

        modifyBtn.setOnAction(ActionEvent -> {
            if (airport.getRunways().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("There are no runways recorded on the system.");
                a.show();
            } else {
                ModifyWindow modifyPage = new ModifyWindow(mainWindow.getStage(), airport);

                // Set the runway listener to update
                modifyPage.setNewRunwayListener(this::updateInitialRunway);
            }
        });
        inputRunwayPaneButtonBox.getChildren().add(modifyBtn);

        // Recalculate button
        var recalculateBtn = new Button("Recalculate");
        recalculateBtn.setOnAction(event -> {
            // Check if the user has already selected the runway and obstacle to recalculate
            if (selectedRunway == null || selectedObstacle == null) {
                var alert = new Alert(AlertType.ERROR);
                alert.setContentText("Please select a runway and an obstacle.");
                alert.show();

            } else {
                recalculationController.recalculateRunway(selectedRunway, selectedObstacle, 300);
            }
        });
        infoPane.getChildren().add(recalculateBtn);

        // Set the listener to handle when parameters have been recalculated
        recalculationController.setSetRunwayListener(this::updateRecalculatedRunway);

        // Input section
        var outputSectionTitle = new Title("Output:");
        infoPane.getChildren().add(outputSectionTitle);

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
     * Set the runway that the user has selected/updated to the gui
     *
     * @param runway the runway object
     */
    private void updateInitialRunway(Runway runway) {
        // Set the initial runway to the original values grid
        ogValuesGrid.setRunway(runway);
        newValuesGrid.reset();

        // Update both canvas
        topDownCanvas.setInitialParameters(runway);
        sideOnCanvas.setInitialParameters(runway);
    }

    /**
     * Set the recalculated runway to the gui
     *
     * @param runway the recalculated runway object
     */
    private void updateRecalculatedRunway(Runway runway) {
        // Set the recalculated runway parameters to the recalculated values grid
        newValuesGrid.setRunway(runway);

        // Update both canvas
        topDownCanvas.setRecalculatedParameters(runway, selectedObstacle,
            selectedObstacle.getSlope(),
            300, selectedObstacle.getPositionL(), selectedObstacle.getPositionR());
        sideOnCanvas.setRecalculatedParameters(runway, selectedObstacle,
            selectedObstacle.getSlope(),
            300, selectedObstacle.getPositionL(), selectedObstacle.getPositionR());
    }

    /**
     * Select a specific scenario to test the program with
     *
     * @param scenario the scenario number (1-4)
     */
    private void setTestScenario(int scenario) {
        logger.info("Setting scenario - " + scenario);

        Obstacles obstacle;
        Runway runway;

        // Create the specified obstacle
        if (scenario == 1) {
            obstacle = new Obstacles("Obstacle Name", 12, -50, 3646);
        } else if (scenario == 2) {
            obstacle = new Obstacles("Obstacle Name", 25, 2853, 500);
        } else if (scenario == 3) {
            obstacle = new Obstacles("Obstacle Name", 15, 150, 3203);
        } else {
            obstacle = new Obstacles("Obstacle Name", 20, 3546, 50);
        }

        // Create and set the runway corresponding to the scenario
        if (scenario == 1 || scenario == 4) {
            runway = new PRunway(9, 27, 'L', 'R', new double[]{
                3902,//runwayL
                100,//runwayW
                60,//stripL
                100,//stripW
                100,//clearwayW
                240, //resaL
                3902,//tora1
                3902,//toda1
                3902,//asda1
                3595,//lda1
                3884,//tora2
                3962,//toda2
                3884,//asda2
                3884,//lda2
                0,//disThresh1
                306//disThresh2
            });
        } else {
            runway = new PRunway(9, 27, 'R', 'L', new double[]{
                3660,//runwayL
                100,//runwayW
                60,//stripL
                100,//stripW
                100,//clearwayW
                240, //resaL
                3660,//tora1
                3660,//toda1
                3660,//asda1
                3353,//lda1
                3660,//tora2
                3660,//toda2
                3660,//asda2
                3660,//lda2
                0,//disThresh1
                307//disThresh2
            });
        }

        // Select the runway and obstacle to show on the program
        selectedRunway = runway;
        updateInitialRunway(runway);
        selectedObstacle = obstacle;
    }
}
