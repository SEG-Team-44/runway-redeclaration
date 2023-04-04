package com.team44.runwayredeclarationapp.view;

import com.team44.runwayredeclarationapp.controller.DataController;
import com.team44.runwayredeclarationapp.controller.RecalculationController;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.RunwayObstacle;
import com.team44.runwayredeclarationapp.model.theme.ColourTheme;
import com.team44.runwayredeclarationapp.ui.MainWindow;
import com.team44.runwayredeclarationapp.ui.xml.ExportXMLWindow;
import com.team44.runwayredeclarationapp.ui.xml.ImportXMLWindow;
import com.team44.runwayredeclarationapp.view.component.CalculationBreakdown;
import com.team44.runwayredeclarationapp.view.component.RunwayParametersGrid;
import com.team44.runwayredeclarationapp.view.component.alert.InfoAlert;
import com.team44.runwayredeclarationapp.view.component.text.Title;
import com.team44.runwayredeclarationapp.view.component.titlepane.AirportTitlePane;
import com.team44.runwayredeclarationapp.view.component.titlepane.ObstacleTitlePane;
import com.team44.runwayredeclarationapp.view.component.titlepane.RunwayTitlePane;
import com.team44.runwayredeclarationapp.view.component.visualisation.SideOnView;
import com.team44.runwayredeclarationapp.view.component.visualisation.TopDownView;
import com.team44.runwayredeclarationapp.view.component.visualisation.VisualisationBase;
import com.team44.runwayredeclarationapp.view.component.visualisation.VisualisationPane;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TabPane.TabDragPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
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
    private final SimpleObjectProperty<Airport> airport = new SimpleObjectProperty<>();

    /**
     * The controller responsible for setting the recalculated values
     */
    private final RecalculationController recalculationController = new RecalculationController();

    /**
     * The controller responsible for reading/writing data in the file system
     */
    private final DataController dataController = new DataController();

    /**
     * The grid displaying the original and recalculated values
     */
    private RunwayParametersGrid ogValuesGrid, newValuesGrid;

    /**
     * The pane showing the calculation breakdown
     */
    private final CalculationBreakdown calculations = new CalculationBreakdown();

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
    private final ObservableList<Obstacle> obstacleObservableList = FXCollections.observableArrayList();

    /**
     * Observable list of airports
     */
    private final ObservableList<Airport> airportObservableList = FXCollections.observableArrayList();
    /**
     * Observable list of runways
     */
    private final ObservableList<Runway> runwayObservableList = FXCollections.observableArrayList();

    /**
     * The colour theme of the program
     */
    private final ObjectProperty<ColourTheme> colourThemeProperty = new SimpleObjectProperty<>(
        new ColourTheme());

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
        airport.addListener((event) -> {
            // Don't do anything if airport is empty
            if (airport.get() == null) {
                return;
            }

            // Update the list of runways whenever the airport changes
            runwayObservableList.setAll(airport.get().getRunways());

            // Update the list of runways whenever one of the runway changes
            airport.get().getRunwayObservableList().addListener(
                (ListChangeListener<Runway>) observable -> {
                    runwayObservableList.setAll(airport.get().getRunways());

                    //reset related gui if a selected runway is removed
                    if (runwayTitlePane.getSelectedRunway() != null &&
                        !runwayObservableList.contains(runwayTitlePane.getSelectedRunway())) {
                        runwayTitlePane.clearInputs();
                        ogValuesGrid.reset();
                        newValuesGrid.reset();

                        topDownCanvas.reset();
                        sideOnCanvas.reset();
                        obstacleTitlePane.clearInputs();

                    }
                });
        });

        // Set the data loaded set listener
        dataController.setDataSetListener((airports, obstacles) -> {
            airportObservableList.setAll(airports);
            obstacleObservableList.setAll(obstacles);
        });

        // Set the data loaded add listener
        dataController.setDataAddListener((airports, obstacles) -> {
            airportObservableList.addAll(airports);
            obstacleObservableList.addAll(obstacles);
        });

        //Update the obstacle gui when a selected obstacle is removed
        obstacleObservableList.addListener(
            (ListChangeListener<Obstacle>) observable -> {
                if (obstacleTitlePane.getSelectedObstacle() != null &&
                    !obstacleObservableList.contains(obstacleTitlePane.getSelectedObstacle())) {
                    obstacleTitlePane.clearInputs();

                    // Update the gui
                    var selectedRunway = runwayTitlePane.getSelectedRunway();
                    if (selectedRunway != null) {
                        updateInitialRunway(selectedRunway);
                    }

                    // Reset the calculations view
                    calculations.reset();
                }
            });

        //reset the whole main scene if current selected airport is deleted
        airportObservableList.addListener(
            (ListChangeListener<Airport>) observable -> {
                if (airportTitlePane.getSelectedAirport() != null &&
                    !airportObservableList.contains(airportTitlePane.getSelectedAirport())) {
                    reset();
                }
            });

        // Load the initial state
        dataController.loadInitialState();
    }

    /**
     * Create the scene ui layout
     */
    @Override
    public void build() {
        // Add menu bar
        MenuBar menuBar = new MenuBar();
        var fileMenu = new Menu("File");
        var menuItemSave = new MenuItem("Save");
        var menuItemResetState = new MenuItem("Reset");

        // Save keyboard shortcut
        var saveKeyShortcut = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        menuItemSave.setAccelerator(saveKeyShortcut);

        // Save event
        menuItemSave.setOnAction(event -> {
            dataController.setState(airportObservableList.toArray(new Airport[0]),
                obstacleObservableList.toArray(new Obstacle[0]));

            // Show alert
            var alert = new InfoAlert("Save successful", "Your data has been saved!",
                "Data has been saved locally and will automatically load when the program is opened.");
            alert.show();
        });

        // Reset event
        menuItemResetState.setOnAction(event -> {
            this.reset();
            dataController.resetState();

            // Show alert
            var alert = new InfoAlert("Reset successful", "Your data has been reset!",
                "Local data has been reset back to preset.");
            alert.show();
        });

        fileMenu.getItems().addAll(menuItemSave, menuItemResetState);

        // Menu for view
        var viewMenu = new Menu("View");
        var toggleShowValueMenuItem = new CheckMenuItem("Show values on visualisation");
        toggleShowValueMenuItem.setSelected(false);
        viewMenu.getItems().add(toggleShowValueMenuItem);

        // Event handler for toggling the show value state
        toggleShowValueMenuItem.setOnAction(event -> {
            if (toggleShowValueMenuItem.isSelected()) {
                topDownCanvas.setShowValues(true);
                sideOnCanvas.setShowValues(true);
            } else {
                topDownCanvas.setShowValues(false);
                sideOnCanvas.setShowValues(false);

            }
        });

        // Menu for XML
        var xmlMenu = new Menu("XML");
        var menuItemImportXML = new MenuItem("Import XML");
        var menuItemExportXML = new MenuItem("Export XML");
        xmlMenu.getItems().addAll(menuItemImportXML, menuItemExportXML);

        // Event handlers for XML menu items
        menuItemImportXML.setOnAction(event -> {
            new ImportXMLWindow(getMainWindow().getStage(), dataController);
        });
        menuItemExportXML.setOnAction(event -> {
            new ExportXMLWindow(getMainWindow().getStage(), dataController, airportObservableList,
                obstacleObservableList);
        });

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
        menuBar.getMenus().addAll(fileMenu, viewMenu, xmlMenu, testDevMenu);

        // Set up the main pane
        root = new StackPane();
        var mainPane = new BorderPane();
        mainPane.setTop(menuBar);
        root.getChildren().add(mainPane);

        // Create the tab pane for the info section
        var infoTabPane = new TabPane();
        Tab inputTab = new Tab("Input");
        Tab outputTab = new Tab("Output");
        infoTabPane.getTabs().addAll(inputTab, outputTab);

        // Info pane (Left side)
        var infoScrollPane = new ScrollPane();
        infoScrollPane.setFitToWidth(true);
        var inputPane = new VBox();
        var outputPane = new VBox();
        infoScrollPane.setContent(inputPane);

        // Info pane properties
        inputPane.setSpacing(10);
        outputPane.setSpacing(20);
        inputPane.getStyleClass().add("info-pane");
        outputPane.getStyleClass().add("info-pane");
        inputPane.setAlignment(Pos.TOP_CENTER);
        outputPane.setAlignment(Pos.TOP_CENTER);
        inputTab.setContent(infoScrollPane);
        outputTab.setContent(outputPane);

        // Visual pane (Right side)
        var visualTabPane = new TabPane();
        visualTabPane.getStyleClass().add("visual-tab-pane");

        // Set the split screen
        mainPane.setRight(visualTabPane);
        mainPane.setLeft(infoTabPane);

        // Set the visualisation tab size
        visualTabPane.setMinWidth(mainWindow.getWidth() / 2);
        infoTabPane.setMinWidth(mainWindow.getWidth() / 2);
        // Always make the visualisation tab half the size of the window
        root.widthProperty().addListener(
            (observableValue, oldWidth, newWidth) -> {
                visualTabPane.setPrefWidth(
                    newWidth.doubleValue() / 2);
                inputPane.setPrefWidth(
                    newWidth.doubleValue() / 2);
            });

        // Set tab properties
        visualTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        infoTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        visualTabPane.setTabDragPolicy(TabDragPolicy.REORDER);
        infoTabPane.setTabDragPolicy(TabDragPolicy.REORDER);

        // Create tabs for the runway visualisations
        Tab topDownTab = new Tab("Top-down view");
        Tab sideOnTab = new Tab("Side-on view");
        visualTabPane.getTabs().addAll(topDownTab, sideOnTab);

        // Add the visualisations to the tabs
        topDownCanvas = new TopDownView(0, 0);
        sideOnCanvas = new SideOnView(0, 0);
        var topDownPane = new VisualisationPane(topDownCanvas);
        var sideOnPane = new VisualisationPane(sideOnCanvas);
        topDownTab.setContent(topDownPane);
        sideOnTab.setContent(sideOnPane);

        // Set theme listener
        colourThemeProperty.addListener((observableValue, oldColourTheme, newColourTheme) -> {
            // Set theme to default if colour theme is empty
            if (newColourTheme == null) {
                topDownCanvas.setColourTheme(new ColourTheme());
                sideOnCanvas.setColourTheme(new ColourTheme());
                return;
            }
            topDownCanvas.setColourTheme(newColourTheme);
            sideOnCanvas.setColourTheme(newColourTheme);
        });

        // Input section
        var inputSectionTitle = new Title("Input:");

        // Create the titled panes
        airportTitlePane = new AirportTitlePane(this);
        runwayTitlePane = new RunwayTitlePane(this);
        obstacleTitlePane = new ObstacleTitlePane(this);

        inputPane.getChildren()
            .addAll(inputSectionTitle, airportTitlePane, runwayTitlePane, obstacleTitlePane);

        // Recalculate button
        var recalculateBtn = new Button("Recalculate");
        recalculateBtn.getStyleClass().add("recalculate-btn");
        recalculateBtn.setOnAction(event -> {
            // Check if the user has already selected the runway and obstacle to recalculate
            if (runwayTitlePane.checkInputsValid() && obstacleTitlePane.checkInputsValid()) {
                // Create the obstacle-runway pairing
                var selectedRunwayObstacle = new RunwayObstacle(
                    obstacleTitlePane.getSelectedObstacle(),
                    runwayTitlePane.getSelectedRunway(),
                    obstacleTitlePane.getObstacleLeftThreshold(),
                    obstacleTitlePane.getObstacleRightThreshold(),
                    obstacleTitlePane.getObstacleFromCentrelineThreshold(),
                    obstacleTitlePane.getBlastProtection());

                // Recalculate
                recalculationController.recalculateRunway(selectedRunwayObstacle);
            }
        });
        inputPane.getChildren().add(recalculateBtn);

        // Set the listener to handle when parameters have been recalculated
        recalculationController.setRecalculatedRunwayListener(this::updateRecalculatedRunway);

        // Input section
        var outputSectionTitle = new Title("Runway Values:");
        outputPane.getChildren().add(outputSectionTitle);

        // Create horizontal box for both value grid
        var valueGridsBox = new HBox();
        valueGridsBox.setSpacing(30);

        // Create the value grid
        ogValuesGrid = new RunwayParametersGrid("Original Values:");
        newValuesGrid = new RunwayParametersGrid("Recalculated Values:");

        // Add the grids to the information pane
        valueGridsBox.getChildren().addAll(ogValuesGrid, newValuesGrid);
        valueGridsBox.setAlignment(Pos.CENTER);
        outputPane.getChildren().addAll(valueGridsBox, calculations);
    }

    /**
     * Set the runway that the user has selected/updated to the gui
     *
     * @param runway the runway object
     */
    public void updateInitialRunway(Runway runway) {
        // Set the initial runway to the original values grid
        ogValuesGrid.setRunway(runway);
        newValuesGrid.reset();
        calculations.reset();

        // Update both canvas
        topDownCanvas.setInitialParameters(runway);
        sideOnCanvas.setInitialParameters(runway);
    }

    /**
     * Set the recalculated runway to the gui
     *
     * @param runwayObstacle the runway-obstacle pairing
     */
    public void updateRecalculatedRunway(RunwayObstacle runwayObstacle) {
        var runway = runwayObstacle.getRecalculatedRw();

        // Set the recalculated runway parameters to the recalculated values grid
        newValuesGrid.setRunway(runway);
        calculations.displayCalculations(runwayTitlePane.getSelectedRunway(), runwayObstacle);

        // Update both canvas
        topDownCanvas.setRecalculatedParameters(runway, runwayObstacle,
            runwayObstacle.getBlastPro());
        sideOnCanvas.setRecalculatedParameters(runway, runwayObstacle,
            runwayObstacle.getBlastPro());
    }

    /**
     * Clear all runway information from the gui
     */
    public void clearRunway() {
        ogValuesGrid.reset();
        newValuesGrid.reset();
        calculations.reset();

        topDownCanvas.reset();
        sideOnCanvas.reset();
    }

    /**
     * Reset the gui back to it's initial state
     */
    private void reset() {
        clearRunway();

        airportTitlePane.clearInputs();
        runwayTitlePane.clearInputs();
        obstacleTitlePane.clearInputs();
    }

    /*public void airportDeleted(Airport airport) {
        if (airport == airportTitlePane.getSelectedAirport()) {
            reset();
        }
    }

    public void runwayDeleted(Runway runway) {
         if (runway == runwayTitlePane.getSelectedRunway()) {
             runwayTitlePane.clearInputs();
             sideOnCanvas.reset();
             topDownCanvas.reset();
             ogValuesGrid.reset();
             newValuesGrid.reset();
         }
    }

    public void obstacleDeleted(Obstacle obstacle) {
        if (obstacle == obstacleTitlePane.getSelectedObstacle()) {
            obstacleTitlePane.clearInputs();
        }
    }*/

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
            runwayObstacle = new RunwayObstacle(obstacle, runway, -50.0, 3646.0, 0.0, 300);
        } else if (scenario == 2) {
            obstacle = new Obstacle("Scenario 2 Obs", 25);
            runwayObstacle = new RunwayObstacle(obstacle, runway, 2853.0, 500.0, -20.0, 300);
        } else if (scenario == 3) {
            obstacle = new Obstacle("Scenario 3 Obs", 15);
            runwayObstacle = new RunwayObstacle(obstacle, runway, 150.0, 3203.0, 60.0, 300);
        } else {
            obstacle = new Obstacle("Scenario 4 Obs", 20);
            runwayObstacle = new RunwayObstacle(obstacle, runway, 3546.0, 50.0, 20.0, 300);
        }

        // Select the runway and obstacle to show on the program
        runwayTitlePane.setSelectedRunway(runway);
        updateInitialRunway(runway);
        obstacleTitlePane.setSelectedObstacle(obstacle);
        obstacleTitlePane.setInputText(
            runwayObstacle.getPositionL(),
            runwayObstacle.getPositionR(),
            runwayObstacle.getDistCR(),
            runwayObstacle.getBlastPro()
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
     * Get the observable list containing the airports
     *
     * @return the airports
     */
    public ObservableList<Airport> getAirportObservableList() {
        return airportObservableList;
    }

    /**
     * Get the observable list containing the runways of the specified airport
     *
     * @return the runways
     */
    public ObservableList<Runway> getRunwayObservableList() {
        return runwayObservableList;
    }

    /**
     * Get the airportPane
     *
     * @return the airport Pane
     */
    public AirportTitlePane getAirportTitlePane() {
        return airportTitlePane;
    }

    /**
     * Get the runwayPane
     *
     * @return the runway Pane
     */
    public RunwayTitlePane getRunwayTitlePane() {
        return runwayTitlePane;
    }

    /**
     * Get the obstaclePane
     *
     * @return the obstacle Pane
     */
    public ObstacleTitlePane getObstacleTitlePane() {
        return obstacleTitlePane;
    }
}
