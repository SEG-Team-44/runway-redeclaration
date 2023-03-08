package com.team44.runwayredeclarationapp.view;

import com.team44.runwayredeclarationapp.ui.InitialiseWindow;
import com.team44.runwayredeclarationapp.ui.ModifyWindow;
import com.team44.runwayredeclarationapp.controller.RecalculationController;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.RunwayObstacle;
import com.team44.runwayredeclarationapp.ui.MainWindow;
import com.team44.runwayredeclarationapp.view.component.SideOnView;
import com.team44.runwayredeclarationapp.view.component.Title;
import com.team44.runwayredeclarationapp.view.component.TopDownView;
import com.team44.runwayredeclarationapp.view.component.ValuesGrid;
import com.team44.runwayredeclarationapp.view.component.VisualisationBase;
import com.team44.runwayredeclarationapp.view.component.VisualisationPane;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TabPane.TabDragPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
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
    private RunwayObstacle selectedObstacle;

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
        infoPane.setAlignment(Pos.TOP_CENTER);
        visualTabPane.getStyleClass().add("visual-tab-pane");

        // Set the visualisation tab size
        visualTabPane.setMinWidth(mainWindow.getWidth() / 2);
        // Always make the visualisation tab half the size of the window
        root.widthProperty().addListener(
            (observableValue, oldWidth, newWidth) -> {
                visualTabPane.setPrefWidth(
                    newWidth.doubleValue() / 2);
                infoPane.setPrefWidth(
                    newWidth.doubleValue() / 2);
            });

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

        // Input Obstacle titledPane
        var inputObstaclePane = new TitledPane();
        inputObstaclePane.setText("Input Obstacle");
        inputObstaclePane.setExpanded(false);
        var nameLabel = new Label("Obstacle name ");
        ComboBox<String> nameField = new ComboBox<String>();
        nameField.getItems().addAll("Aircraft","Other");
        nameField.setEditable(true);
        var heightLabel = new Label("Height of obstacle (m) ");
        var heightField = new TextField();
        var posLLabel = new Label("Position from left end of the runway (m)");
        var posLField = new TextField();
        var posRLabel = new Label("Position from right end of the runway (m)");
        var posRField = new TextField();
        var distLabel = new Label("Distance from centre line (m)");
        var distField = new TextField();

        var obstButton = new Button("Set obstacle");
        obstButton.setOnAction(ActionEvent -> {

            if (selectedRunway == null){
                var alert = new Alert(AlertType.ERROR);
                alert.setContentText("Please select a runway.");
                alert.show();
            } else {
                Obstacle obst = new Obstacle(nameField.getValue(),
                    Double.parseDouble(heightField.getText()));
                selectedObstacle = new RunwayObstacle(obst, selectedRunway,
                    Double.parseDouble(posLField.getText()),
                    Double.parseDouble(posRField.getText()),
                    Double.parseDouble(distField.getText()));
            }
        });

        var obstPane = new GridPane();
        obstPane.setHgap(5);
        obstPane.setVgap(5);
        obstPane.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints(60),
            new ColumnConstraints(), new ColumnConstraints(60));
        obstPane.addRow(1, nameLabel, nameField);
        obstPane.addRow(2, heightLabel, heightField);
        obstPane.addRow(3, posLLabel, posLField);
        obstPane.addRow(4, posRLabel, posRField);
        obstPane.addRow(5, distLabel, distField, obstButton);
        inputObstaclePane.setContent(obstPane);

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
        recalculateBtn.getStyleClass().add("recalculate-btn");
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
        valueGridsBox.setAlignment(Pos.CENTER);
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
        topDownCanvas.setRecalculatedParameters(runway, selectedObstacle, 300);
        sideOnCanvas.setRecalculatedParameters(runway, selectedObstacle, 300);
    }

    /**
     * Select a specific scenario to test the program with
     *
     * @param scenario the scenario number (1-4)
     */
    private void setTestScenario(int scenario) {
        logger.info("Setting scenario - " + scenario);

        Obstacle obstacle;
        Runway runway;
        RunwayObstacle runwayObstacle;

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

        // Create the specified obstacle
        if (scenario == 1) {
            obstacle = new Obstacle("Obstacle Name", 12);
            runwayObstacle = new RunwayObstacle(obstacle, runway, -50.0, 3646.0, 0.0);
        } else if (scenario == 2) {
            obstacle = new Obstacle("Obstacle Name", 25);
            runwayObstacle = new RunwayObstacle(obstacle, runway, 2853.0, 500.0, -20.0);
        } else if (scenario == 3) {
            obstacle = new Obstacle("Obstacle Name", 15);
            runwayObstacle = new RunwayObstacle(obstacle, runway, 150.0, 3203.0, 60.0);
        } else {
            obstacle = new Obstacle("Obstacle Name", 20);
            runwayObstacle = new RunwayObstacle(obstacle, runway, 3546.0, 50.0, 20.0);
        }

        // Select the runway and obstacle to show on the program
        selectedRunway = runway;
        updateInitialRunway(runway);
        selectedObstacle = runwayObstacle;
    }
}
