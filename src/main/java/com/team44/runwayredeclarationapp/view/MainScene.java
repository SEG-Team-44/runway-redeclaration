package com.team44.runwayredeclarationapp.view;

import com.team44.runwayredeclarationapp.controller.RecalculationController;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.RunwayObstacle;
import com.team44.runwayredeclarationapp.ui.MainWindow;
import com.team44.runwayredeclarationapp.view.component.ValuesGrid;
import com.team44.runwayredeclarationapp.view.component.text.Title;
import com.team44.runwayredeclarationapp.view.component.titlepane.AirportTitlePane;
import com.team44.runwayredeclarationapp.view.component.titlepane.ObstacleTitlePane;
import com.team44.runwayredeclarationapp.view.component.titlepane.RunwayTitlePane;
import com.team44.runwayredeclarationapp.view.component.visualisation.SideOnView;
import com.team44.runwayredeclarationapp.view.component.visualisation.TopDownView;
import com.team44.runwayredeclarationapp.view.component.visualisation.VisualisationBase;
import com.team44.runwayredeclarationapp.view.component.visualisation.VisualisationPane;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TabPane.TabDragPolicy;
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
    private SimpleObjectProperty<Airport> airport = new SimpleObjectProperty<>(new Airport());

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
     * Titled Panes
     */
    private AirportTitlePane airportTitlePane;
    private RunwayTitlePane runwayTitlePane;
    private ObstacleTitlePane obstacleTitlePane;

    /**
     * Observable list of obstacles
     */
    private final ObservableList<Obstacle> obstacleObservableList = FXCollections.observableArrayList(
        new Obstacle("Airbus A319", 12),
        new Obstacle("Airbus A330", 16),
        new Obstacle("Airbus A340", 17),
        new Obstacle("Airbus A380", 24),
        new Obstacle("Boeing 737 MAX", 13),
        new Obstacle("Boeing 747", 19),
        new Obstacle("Boeing 757", 14),
        new Obstacle("Boeing 767", 17),
        new Obstacle("Boeing 777", 18),
        new Obstacle("Boeing 787 Dreamliner", 17),
        new Obstacle("Cessna 172", 2),
        new Obstacle("Gulfstream G650", 7),
        new Obstacle("Embraer E145", 6)
    );

    /**
     * Observable list of runways
     */
    private ObservableList<Runway> runwayObservableList = FXCollections.observableArrayList();

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
        // todo remove these runways once airport selection has been added
        airport.get().addRunway(new PRunway(9, 27, 'L', 'R', new double[]{
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
        }));
        airport.get().addRunway(new PRunway(9, 27, 'R', 'L', new double[]{
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
        }));

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

        // Create the titled panes
        airportTitlePane = new AirportTitlePane(this);
        runwayTitlePane = new RunwayTitlePane(this);
        obstacleTitlePane = new ObstacleTitlePane(this);

        // Update the list of runways whenever the airport changes
        airport.addListener((event) -> {
            runwayObservableList.setAll(airport.get().getRunways());
        });
        // Update the list whenever one of the runway changes
        airport.get().getRunwayObservableList().addListener(
            (ListChangeListener<Runway>) observable -> {
                runwayObservableList.setAll(airport.get().getRunways());
            });

        infoPane.getChildren()
            .addAll(inputSectionTitle, airportTitlePane, runwayTitlePane, obstacleTitlePane);

        // Recalculate button
        var recalculateBtn = new Button("Recalculate");
        recalculateBtn.getStyleClass().add("recalculate-btn");
        recalculateBtn.setOnAction(event -> {
            // Check if the user has already selected the runway and obstacle to recalculate
            if (obstacleTitlePane.checkInputsValid() && selectedRunway != null) {
                // Create the obstacle-runway pairing
                selectedObstacle = new RunwayObstacle(
                    obstacleTitlePane.getSelectedObstacle(),
                    selectedRunway, // todo
                    obstacleTitlePane.getObstacleLeftThreshold(),
                    obstacleTitlePane.getObstacleRightThreshold(),
                    obstacleTitlePane.getObstacleFromCentrelineThreshold());

                // Recalculate
                recalculationController.recalculateRunway(selectedRunway, selectedObstacle, 300);
            } else {
                // todo, remove this once "checkInputsValid" for runway and airport
                // var alert = new Alert(AlertType.ERROR);
                // alert.setContentText("Please select a runway and an obstacle.");
                // alert.show();
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
    public void updateInitialRunway(Runway runway) {
        selectedRunway = runway;

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
    public void updateRecalculatedRunway(Runway runway) {
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
            obstacle = new Obstacle("Scenario 1 Obs", 12);
            runwayObstacle = new RunwayObstacle(obstacle, runway, -50.0, 3646.0, 0.0);
        } else if (scenario == 2) {
            obstacle = new Obstacle("Scenario 2 Obs", 25);
            runwayObstacle = new RunwayObstacle(obstacle, runway, 2853.0, 500.0, -20.0);
        } else if (scenario == 3) {
            obstacle = new Obstacle("Scenario 3 Obs", 15);
            runwayObstacle = new RunwayObstacle(obstacle, runway, 150.0, 3203.0, 60.0);
        } else {
            obstacle = new Obstacle("Scenario 4 Obs", 20);
            runwayObstacle = new RunwayObstacle(obstacle, runway, 3546.0, 50.0, 20.0);
        }

        // Select the runway and obstacle to show on the program
        runwayTitlePane.setSelectedRunway(runway);
        updateInitialRunway(runway);
        obstacleTitlePane.setSelectedObstacle(obstacle);
        obstacleTitlePane.setInputText(
            runwayObstacle.getPositionL(),
            runwayObstacle.getPositionR(),
            runwayObstacle.getDistCR()
        );
    }

    /**
     * Get the observable list containing the obstacles
     *
     * @return the observable list
     */
    public ObservableList<Obstacle> getObstacleObservableList() {
        return obstacleObservableList;
    }

    /**
     * Set the selected airport
     *
     * @param airport the airport
     */
    public void setAirport(Airport airport) {
        this.airport.set(airport);
    }

    /**
     * Get the current airport object
     *
     * @return the airport object
     */
    public Airport getAirport() {
        return airport.get();
    }

    /**
     * Get the simple object property for the airport
     *
     * @return airport object property
     */
    public SimpleObjectProperty<Airport> getAirportProperty() {
        return airport;
    }

    /**
     * Get the observable list containing the runways
     *
     * @return the runways
     */
    public ObservableList<Runway> getRunwayObservableList() {
        return runwayObservableList;
    }

    /**
     * Get the current selected runway
     *
     * @return the runway
     */
    public Runway getSelectedRunway() {
        return selectedRunway;
    }
}
