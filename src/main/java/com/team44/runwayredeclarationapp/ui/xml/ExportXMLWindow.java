package com.team44.runwayredeclarationapp.ui.xml;

import com.team44.runwayredeclarationapp.controller.FileController;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Obstacle;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * The window for exporting xml file
 */
public class ExportXMLWindow extends Stage {


    /**
     * Create the window for exporting xml file
     *
     * @param parent                 the parent window
     * @param fileController         the data controller
     * @param airportObservableList  the observable list of airports
     * @param obstacleObservableList the observable list of obstacles
     */
    public ExportXMLWindow(Window parent, FileController fileController,
        ObservableList<Airport> airportObservableList,
        ObservableList<Obstacle> obstacleObservableList) {

        //Setup main pane
        var mainPane = new VBox();
        mainPane.getStyleClass().add("export-window");
        mainPane.setSpacing(15);
        mainPane.setAlignment(Pos.CENTER);
        HBox.setHgrow(mainPane, Priority.ALWAYS);
        VBox.setVgrow(mainPane, Priority.ALWAYS);

        //Set scene
        Scene scene = new Scene(mainPane);
        mainPane.requestFocus();
        scene.setOnMouseClicked(mouseEvent -> mainPane.requestFocus());

        // Export text
        var infoText = new Text("Export " + airportObservableList.size() + " airport(s) and "
            + obstacleObservableList.size() + " obstacle(s).");

        // Export button
        var exportButton = new Button("Export");

        // File choosing with export button
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
            .add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        exportButton.setOnAction(event -> {
            fileController.exportXMLFile(airportObservableList.toArray(Airport[]::new),
                obstacleObservableList.toArray(Obstacle[]::new), fileChooser.showSaveDialog(this));
        });

        mainPane.getChildren().addAll(infoText, exportButton);

        // Set stage properties and make it a modal window
        this.setTitle("Export XML File");
        this.setScene(scene);
        this.initOwner(parent);
        this.initModality(Modality.WINDOW_MODAL);

        // Styling
        scene.getStylesheets().addAll(parent.getScene().getStylesheets());
        this.setResizable(false);
        this.setWidth(600);
        this.setHeight(300);

        this.show();
    }
}
