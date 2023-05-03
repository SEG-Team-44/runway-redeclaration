package com.team44.runwayredeclarationapp.view;

import com.team44.runwayredeclarationapp.controller.DataController;
import com.team44.runwayredeclarationapp.controller.FileController;
import com.team44.runwayredeclarationapp.controller.RecalculationController;
import com.team44.runwayredeclarationapp.controller.ValidationController;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.model.RunwayObstacle;
import com.team44.runwayredeclarationapp.model.theme.ColourTheme;
import com.team44.runwayredeclarationapp.ui.InstructionWindow;
import com.team44.runwayredeclarationapp.ui.MainWindow;
import com.team44.runwayredeclarationapp.ui.userlog.ViewLogWindow;
import com.team44.runwayredeclarationapp.ui.xml.ExportXMLWindow;
import com.team44.runwayredeclarationapp.ui.xml.ImportXMLWindow;
import com.team44.runwayredeclarationapp.view.component.CalculationBreakdown;
import com.team44.runwayredeclarationapp.view.component.RunwayParametersGrid;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorAlert;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorListAlert;
import com.team44.runwayredeclarationapp.view.component.alert.InfoAlert;
import com.team44.runwayredeclarationapp.view.component.text.Title;
import com.team44.runwayredeclarationapp.view.component.titlepane.AirportTitlePane;
import com.team44.runwayredeclarationapp.view.component.titlepane.ObstacleTitlePane;
import com.team44.runwayredeclarationapp.view.component.titlepane.RunwayTitlePane;
import com.team44.runwayredeclarationapp.view.component.visualisation.MapView;
import com.team44.runwayredeclarationapp.view.component.visualisation.SideOnView;
import com.team44.runwayredeclarationapp.view.component.visualisation.TopDownView;
import com.team44.runwayredeclarationapp.view.component.visualisation.VisualisationBase;
import com.team44.runwayredeclarationapp.view.component.visualisation.VisualisationPane;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.function.Function;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TabPane.TabDragPolicy;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 * The main scene that will be shown when the user opens the program
 */
public class MainScene extends BaseScene {

    /**
     * The selected airport
     */
    private final SimpleObjectProperty<Airport> airport = new SimpleObjectProperty<>();

    /**
     * The data controller responsible for handling the list of airports, runways and obstacles
     */
    private final DataController dataController = new DataController();

    /**
     * The controller responsible for setting the recalculated values
     */
    private final RecalculationController recalculationController = new RecalculationController(
        dataController);

    /**
     * The controller responsible for reading/writing data in the file system
     */
    private final FileController fileController = new FileController();

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
    private VisualisationBase topDownCanvas, sideOnCanvas, simultBottomCanvas, simultTopCanvas, mapCanvas;

    /**
     * Titled Panes
     */
    private AirportTitlePane airportTitlePane;
    private RunwayTitlePane runwayTitlePane;
    private ObstacleTitlePane obstacleTitlePane;

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
            dataController.getRunwayObservableList().setAll(airport.get().getRunways());

            // Update the list of runways whenever one of the runway changes
            airport.get().getRunwayObservableList().addListener(
                (ListChangeListener<Runway>) observable -> {
                    dataController.getRunwayObservableList().setAll(airport.get().getRunways());

                    //reset related gui if a selected runway is removed
                    if (runwayTitlePane.getSelectedRunway() != null &&
                        !dataController.getRunwayObservableList()
                            .contains(runwayTitlePane.getSelectedRunway())) {
                        runwayTitlePane.clearInputs();
                        ogValuesGrid.reset();
                        newValuesGrid.reset();

                        callAllCanvasMethod((canvas) -> {
                            canvas.reset();
                            return null;
                        });
                        obstacleTitlePane.clearInputs();

                    }
                });
        });

        // Set the data loaded set listener
        fileController.setDataSetListener((airports, obstacles) -> {
            dataController.getAirportObservableList().setAll(airports);
            dataController.getObstacleObservableList().setAll(obstacles);
        });

        // Set the data loaded add listener
        fileController.setDataAddListener((airports, obstacles) -> {
            dataController.getAirportObservableList().addAll(airports);
            dataController.getObstacleObservableList().addAll(obstacles);
        });

        //Update the obstacle gui when a selected obstacle is removed
        dataController.getObstacleObservableList().addListener(
            (ListChangeListener<Obstacle>) observable -> {
                if (obstacleTitlePane.getSelectedObstacle() != null &&
                    !dataController.getObstacleObservableList()
                        .contains(obstacleTitlePane.getSelectedObstacle())) {
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
        dataController.getAirportObservableList().addListener(
            (ListChangeListener<Airport>) observable -> {
                if (airportTitlePane.getSelectedAirport() != null &&
                    !dataController.getAirportObservableList()
                        .contains(airportTitlePane.getSelectedAirport())) {
                    reset();
                }
            });

        // Set the show error listener
        fileController.setErrorListener(this::showError);
        fileController.setMultipleErrorsListener(this::showErrorList);

        // Load the initial state
        fileController.loadInitialState();
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
        var fileMenuSeparator = new SeparatorMenuItem();
        var menuItemViewLog = new MenuItem("View Log");

        // Save keyboard shortcut
        var saveKeyShortcut = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        menuItemSave.setAccelerator(saveKeyShortcut);

        // Save event
        menuItemSave.setOnAction(event -> {
            fileController.setState(
                dataController.getAirportObservableList().toArray(new Airport[0]),
                dataController.getObstacleObservableList().toArray(new Obstacle[0]));

            // Show alert
            var alert = new InfoAlert("Save successful", "Your data has been saved!",
                "Data has been saved locally and will automatically load when the program is opened.");
            alert.show();
        });

        // Reset event
        menuItemResetState.setOnAction(event -> {
            this.reset();
            fileController.resetState();

            // Show alert
            var alert = new InfoAlert("Reset successful", "Your data has been reset!",
                "Local data has been reset back to preset.");
            alert.show();
        });

        // View log event
        menuItemViewLog.setOnAction(event -> {
            new ViewLogWindow(getMainWindow().getStage(), dataController);
        });

        fileMenu.getItems()
            .addAll(menuItemSave, menuItemResetState, fileMenuSeparator, menuItemViewLog);

        // Menu for view
        var viewMenu = new Menu("View");
        var themeMenu = new Menu("Themes");
        var toggleShowValueMenuItem = new CheckMenuItem("Show Values");
        var toggleMatchCompassHeading = new CheckMenuItem("Match Compass");
        var toggleWhiteArrows = new CheckMenuItem("Set White Arrows");
        var toggleSwitchThresholds = new CheckMenuItem("Switch Thresholds");
        toggleShowValueMenuItem.setSelected(false);
        toggleMatchCompassHeading.setSelected(false);
        toggleWhiteArrows.setSelected(false);
        toggleSwitchThresholds.setSelected(false);
        viewMenu.getItems().addAll(themeMenu, toggleShowValueMenuItem, toggleMatchCompassHeading,
            toggleWhiteArrows, toggleSwitchThresholds);

        // Themes toggle
        // List of themes available
        var themes = new ColourTheme[]{
            ColourTheme.getColourBlindTheme(),
            ColourTheme.getDarkModeTheme(),
            ColourTheme.getHighContrastTheme()};
        ToggleGroup themesToggleGroup = new ToggleGroup();

        // Default theme button
        var defaultColourThemeButton = new RadioMenuItem(new ColourTheme().getThemeName());
        defaultColourThemeButton.setToggleGroup(themesToggleGroup);
        defaultColourThemeButton.setSelected(true);
        themeMenu.getItems().add(defaultColourThemeButton);
        defaultColourThemeButton.setOnAction(event -> callAllCanvasMethod((canvas) -> {
            canvas.setColourTheme(new ColourTheme());
            return null;
        }));

        // Add the rest of the themes
        for (ColourTheme theme : themes) {
            var radioButton = new RadioMenuItem(theme.getThemeName());
            radioButton.setToggleGroup(themesToggleGroup);
            themeMenu.getItems().add(radioButton);

            // Event handler for the button to change the theme
            radioButton.setOnAction(event -> callAllCanvasMethod((canvas) -> {
                canvas.setColourTheme(theme);
                return null;
            }));
        }

        // Event handler for toggling the show value state
        toggleShowValueMenuItem.setOnAction(event -> {
            if (toggleShowValueMenuItem.isSelected()) {
                callAllCanvasMethod((canvas) -> {
                    canvas.setShowValues(true);
                    return null;
                });
            } else {
                callAllCanvasMethod((canvas) -> {
                    canvas.setShowValues(false);
                    return null;
                });
            }
        });

        // Event handler for toggling whether to match the compass heading
        toggleMatchCompassHeading.setOnAction(event -> {
            if (toggleMatchCompassHeading.isSelected()) {
                topDownCanvas.setRotateCompass(true);
                mapCanvas.setRotateCompass(true);
            } else {
                topDownCanvas.setRotateCompass(false);
                mapCanvas.setRotateCompass(false);
            }
        });

        // Event handler for toggling white arrows
        toggleWhiteArrows.setOnAction(event -> {
            if (toggleWhiteArrows.isSelected()) {
                callAllCanvasMethod((canvas) -> {
                    canvas.setWhiteArrow(true);
                    return null;
                });
            } else {
                callAllCanvasMethod((canvas) -> {
                    canvas.setWhiteArrow(false);
                    return null;
                });
            }
        });

        // Event handler for toggling switching thresholds
        toggleSwitchThresholds.setOnAction(event -> {
            if (toggleSwitchThresholds.isSelected()) {
                callAllCanvasMethod((canvas) -> {
                    canvas.setThresholdSwitched(true);
                    return null;
                });
            } else {
                callAllCanvasMethod((canvas) -> {
                    canvas.setThresholdSwitched(false);
                    return null;
                });
            }
        });

        // Menu for XML
        var xmlMenu = new Menu("XML");
        var menuItemImportXML = new MenuItem("Import XML");
        var menuItemExportXML = new MenuItem("Export XML");
        xmlMenu.getItems().addAll(menuItemImportXML, menuItemExportXML);

        // Event handlers for XML menu items
        menuItemImportXML.setOnAction(event -> {
            new ImportXMLWindow(getMainWindow().getStage(), fileController);
        });
        menuItemExportXML.setOnAction(event -> {
            new ExportXMLWindow(getMainWindow().getStage(), fileController,
                dataController.getAirportObservableList(),
                dataController.getObstacleObservableList());
        });

        // Menu for exporting canvas images
        var exportMenu = new Menu("Export");
        var menuItemExportTopDown = new MenuItem("Export Top-Down View");
        var menuItemExportSideOn = new MenuItem("Export Side-On View");
        var menuItemExportMap = new MenuItem("Export Map View");

        exportMenu.getItems()
            .addAll(menuItemExportTopDown, menuItemExportSideOn, menuItemExportMap);

        // Canvas image exporting events
        menuItemExportTopDown.setOnAction(event -> exportCanvas(topDownCanvas));
        menuItemExportSideOn.setOnAction(event -> exportCanvas(sideOnCanvas));
        menuItemExportMap.setOnAction(event -> exportCanvas(mapCanvas));

        //Create a menu for reading instructions
        var helpMenu = new Menu("Help");
        var instructionMenuItem = new MenuItem("Instructions");
        instructionMenuItem.setOnAction(event -> {
            InstructionWindow instructionWindow = new InstructionWindow(getMainWindow().getStage());
        });
        helpMenu.getItems().add(instructionMenuItem);

        // Create a menu for selecting scenarios to test the program with
        var scenario1MenuItem = new MenuItem("Select Scenario 1");
        var scenario2MenuItem = new MenuItem("Select Scenario 2");
        var scenario3MenuItem = new MenuItem("Select Scenario 3");
        var scenario4MenuItem = new MenuItem("Select Scenario 4");

        // Set the actions for the menu item buttons
        scenario1MenuItem.setOnAction(event -> setTestScenario(1));
        scenario2MenuItem.setOnAction(event -> setTestScenario(2));
        scenario3MenuItem.setOnAction(event -> setTestScenario(3));
        scenario4MenuItem.setOnAction(event -> setTestScenario(4));

        menuBar.getMenus().addAll(fileMenu, viewMenu, xmlMenu, exportMenu, helpMenu);

        // Scenarios to test with (FOR DEVS)
        var testDevMenu = new Menu("Test (for devs)"); // For testing
        testDevMenu.getItems()
            .addAll(scenario1MenuItem, scenario2MenuItem, scenario3MenuItem, scenario4MenuItem);
        // menuBar.getMenus().add(testDevMenu);

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
        Tab simultTab = new Tab("Simultaneous view");
        Tab mapTab = new Tab("Map view");
        visualTabPane.getTabs().addAll(topDownTab, sideOnTab, simultTab, mapTab);

        // Add the visualisations to the tabs
        topDownCanvas = new TopDownView(0, 0);
        sideOnCanvas = new SideOnView(0, 0);
        mapCanvas = new MapView(0, 0);
        var topDownPane = new VisualisationPane(topDownCanvas);
        var sideOnPane = new VisualisationPane(sideOnCanvas);
        var mapPane = new VisualisationPane(mapCanvas);
        topDownTab.setContent(topDownPane);
        sideOnTab.setContent(sideOnPane);
        mapTab.setContent(mapPane);

        // Create the simultaneous view canvas and panes
        simultBottomCanvas = new TopDownView(0, 0);
        simultTopCanvas = new SideOnView(0, 0);
        var simultBottomPane = new VisualisationPane(simultBottomCanvas);
        var simultTopPane = new VisualisationPane(simultTopCanvas);

        // Split the simultaneous view into half
        var simultVBox = new VBox();
        simultTab.setContent(simultVBox);
        VBox.setVgrow(simultVBox, Priority.ALWAYS);
        simultVBox.getChildren().addAll(simultBottomPane, simultTopPane);
        simultTopPane.prefHeightProperty().bind(simultBottomPane.prefHeightProperty());
        simultBottomPane.prefHeightProperty().bind(Bindings.divide(simultVBox.heightProperty(), 2));

        // Set the simultaneous view canvas properties
        simultTopCanvas.setArrowsFromRunwayOffset(10);
        simultTopCanvas.setArrowsGapBetween(14);
        simultTopCanvas.setShowKey(false);
        simultBottomCanvas.setArrowsFromRunwayOffset(10);
        simultBottomCanvas.setArrowsGapBetween(14);
        simultBottomCanvas.setShowKey(false);

        // Set theme listener
        colourThemeProperty.addListener((observableValue, oldColourTheme, newColourTheme) -> {
            // Set theme to default if colour theme is empty
            if (newColourTheme == null) {
                callAllCanvasMethod((canvas) -> {
                    canvas.setColourTheme(new ColourTheme());
                    return null;
                });
                return;
            }
            callAllCanvasMethod((canvas) -> {
                canvas.setColourTheme(newColourTheme);
                return null;
            });
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
            // Validate obstacle info inputs
            var validateObstacleInputs = ValidationController.validateObstacleInformationInputs(
                runwayTitlePane.getSelectedRunway(), obstacleTitlePane.getObstacleLeftThreshold(),
                obstacleTitlePane.getObstacleRightThreshold(),
                obstacleTitlePane.getObstacleFromCentrelineThreshold(),
                obstacleTitlePane.getBlastProtection());

            // Show error if validation error
            if (validateObstacleInputs.size() > 0) {
                showErrorList(validateObstacleInputs.toArray(String[]::new));
                return;
            }

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

        // Update all canvas
        callAllCanvasMethod((canvas) -> {
            canvas.setInitialParameters(runway);
            return null;
        });
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

        // Update all canvas
        callAllCanvasMethod((canvas) -> {
            canvas.setRecalculatedParameters(runwayObstacle,
                runwayObstacle.getBlastPro());
            return null;
        });
    }

    /**
     * Clear all runway information from the gui
     */
    public void clearRunway() {
        ogValuesGrid.reset();
        newValuesGrid.reset();
        calculations.reset();

        callAllCanvasMethod((canvas) -> {
            canvas.reset();
            return null;
        });
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
        return dataController.getObstacleObservableList();
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
        return dataController.getAirportObservableList();
    }

    /**
     * Get the observable list containing the runways of the specified airport
     *
     * @return the runways
     */
    public ObservableList<Runway> getRunwayObservableList() {
        return dataController.getRunwayObservableList();
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

    /**
     * Show an error alert
     *
     * @param title   the error title
     * @param header  the error header
     * @param content the error content
     */
    private void showError(String title, String header, String content) {
        new ErrorAlert(title, header, content).show();
    }

    /**
     * Show an error list alert
     *
     * @param errors the list of errors
     */
    private void showErrorList(String[] errors) {
        var errorList = new ErrorListAlert();
        errorList.setErrors(errors);
        errorList.show();
    }

    /**
     * Get the data controller of the program
     *
     * @return the data controller
     */
    public DataController getDataController() {
        return dataController;
    }

    /**
     * Get the observable property for the selected runway (combobox)
     *
     * @return the observable property
     */
    public ObjectProperty<Runway> getSelectedRunwayProperty() {
        return runwayTitlePane.getRunwaySelectComboBox().valueProperty();
    }

    /**
     * Open file chooser to save a canvas as an image
     *
     * @param canvas the canvas to save
     */
    public void exportCanvas(Canvas canvas) {
        var canvasHeight = (int) Math.round(canvas.getHeight());
        var canvasWidth = (int) Math.round(canvas.getWidth());

        // Valid file extensions
        var jpgExtension = new FileChooser.ExtensionFilter("JPEG files", "*.jpg", "*.jpeg");
        var pngExtension = new FileChooser.ExtensionFilter("PNG files", "*.png");
        var gifExtension = new FileChooser.ExtensionFilter("GIF files", "*.gif");

        // File choosing with export button
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(jpgExtension, pngExtension, gifExtension);
        var saveTo = fileChooser.showSaveDialog(this.getMainWindow().getStage());

        // Don't do anything if the file is null
        if (saveTo == null) {
            return;
        }

        // Get the file name and extension
        var fileName = saveTo.getName();
        var lastIndex = fileName.lastIndexOf(".");
        var fileExtension = lastIndex > 0 ? fileName.substring(lastIndex + 1) : "";

        // Write the canvas snapshot to the image
        WritableImage image = new WritableImage(canvasHeight, canvasWidth);
        canvas.snapshot(null, image);

        // Get the rendered image and save it to the location
        BufferedImage bufferedImage = new BufferedImage((int) image.getWidth(),
            (int) image.getHeight(), BufferedImage.TYPE_INT_RGB);
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, bufferedImage);

        try {
            // Save image
            var writerFound = ImageIO.write(renderedImage, fileExtension, saveTo);

            // Write could not be found error
            if (!writerFound) {
                showError("File Save Error", "Could not save the image to the desired location.",
                    "File format (" + fileExtension + ") appropriate writer could not be found");
            }
        } catch (IOException e) {
            showError("File Save Error", "Could not save the image to the desired location.",
                "An IO error occurred :" + e.getMessage());
        }
    }

    /**
     * Call a specific method on all the visualisation canvas'
     *
     * @param canvasMethod the method to call
     */
    private void callAllCanvasMethod(Function<VisualisationBase, Void> canvasMethod) {
        canvasMethod.apply(topDownCanvas);
        canvasMethod.apply(sideOnCanvas);
        canvasMethod.apply(simultTopCanvas);
        canvasMethod.apply(simultBottomCanvas);
        canvasMethod.apply(mapCanvas);
    }
}
