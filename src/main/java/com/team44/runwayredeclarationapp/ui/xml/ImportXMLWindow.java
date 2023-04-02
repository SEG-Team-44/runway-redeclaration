package com.team44.runwayredeclarationapp.ui.xml;

import com.team44.runwayredeclarationapp.controller.DataController;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorAlert;
import java.io.File;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * The window for importing xml file
 */
public class ImportXMLWindow extends Stage {

    /**
     * The selected file object to import
     */
    private final SimpleObjectProperty<File> fileSimpleObjectProperty = new SimpleObjectProperty<>();

    /**
     * Create the window for importing xml file
     *
     * @param parent         the parent window
     * @param dataController the data controller
     */
    public ImportXMLWindow(Window parent, DataController dataController) {
        Stage stage = new Stage();

        //Setup main pane
        var mainPane = new VBox();
        mainPane.getStyleClass().add("drag-drop-window");
        HBox.setHgrow(mainPane, Priority.ALWAYS);
        VBox.setVgrow(mainPane, Priority.ALWAYS);

        // Import button
        var importBtn = new Button("Import");
        importBtn.setDisable(true);

        //Set scene
        Scene scene = new Scene(mainPane);
        mainPane.requestFocus();
        scene.setOnMouseClicked(mouseEvent -> mainPane.requestFocus());

        // Drag and drop screen
        var dragDropPane = new VBox();
        dragDropPane.getStyleClass().add("drag-drop-area");
        dragDropPane.setAlignment(Pos.CENTER);
        HBox.setHgrow(dragDropPane, Priority.ALWAYS);
        VBox.setVgrow(dragDropPane, Priority.ALWAYS);
        mainPane.getChildren().addAll(dragDropPane, importBtn);

        // Drag and drop text
        var clickToUploadBtn = new Hyperlink("Click to upload");
        var uploadText = new TextFlow(
            clickToUploadBtn, new Text(" or drag and drop")
        );
        uploadText.getStyleClass().add("text-flow");
        uploadText.setTextAlignment(TextAlignment.CENTER);
        dragDropPane.getChildren().add(uploadText);

        // Click to upload hyperlink
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
            .add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        clickToUploadBtn.setOnAction(event -> {
            fileSimpleObjectProperty.set(fileChooser.showOpenDialog(this));
        });

        // Drag over ui indication
        dragDropPane.setOnDragEntered(dragEvent -> {
            dragDropPane.getStyleClass().add("drag-drop-area-active");
            dragEvent.consume();
        });
        dragDropPane.setOnDragExited(dragEvent -> {
            dragDropPane.getStyleClass().remove("drag-drop-area-active");
            dragEvent.consume();
        });

        // Dropped screen
        var droppedPane = new VBox();
        droppedPane.getStyleClass().add("drag-drop-area");
        droppedPane.setAlignment(Pos.CENTER);
        HBox.setHgrow(droppedPane, Priority.ALWAYS);
        VBox.setVgrow(droppedPane, Priority.ALWAYS);

        // Dropped screen text
        var uploadSuccessfulText = new Text("Upload successful!");
        uploadSuccessfulText.setTextAlignment(TextAlignment.CENTER);

        // Cancel upload button
        var cancelUploadButton = new Button("x");
        cancelUploadButton.setAlignment(Pos.TOP_RIGHT);
        cancelUploadButton.setOnAction(event -> {
            // Remove the uploaded file
            fileSimpleObjectProperty.set(null);
        });

        droppedPane.getChildren().addAll(cancelUploadButton, uploadSuccessfulText);

        // Drag and drop events
        dragDropPane.setOnDragOver(dragEvent -> {
            // Ensure that there is a file
            if (dragEvent.getGestureSource() != dragDropPane && dragEvent.getDragboard()
                .hasFiles()) {
                var files = dragEvent.getDragboard().getFiles();

                if (validateFileDrag(files)) {
                    dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
            }
            dragEvent.consume();
        });

        // Drag dropped file upload
        dragDropPane.setOnDragDropped(dragEvent -> {
            // Ensure that there is a file
            if (dragEvent.getGestureSource() != dragDropPane && dragEvent.getDragboard()
                .hasFiles()) {
                var files = dragEvent.getDragboard().getFiles();

                // Validate file again
                if (!validateFileDrag(files)) {
                    return;
                }
                File fileToUpload = files.get(0);
                fileSimpleObjectProperty.set(fileToUpload);
            }

            dragEvent.consume();
        });

        // File uploaded/removed listener
        fileSimpleObjectProperty.addListener((obsVal, oldFile, newFile) -> {
            if (newFile != null) {
                importBtn.setDisable(false);

                // Switch the panes
                mainPane.getChildren().remove(dragDropPane);
                mainPane.getChildren().add(0, droppedPane);
            } else {
                importBtn.setDisable(true);

                // Switch the panes
                mainPane.getChildren().remove(droppedPane);
                mainPane.getChildren().add(0, dragDropPane);
            }
        });

        // Import event
        importBtn.setOnAction(event -> {
            var fileToUpload = fileSimpleObjectProperty.get();

            // Ensure file still exists
            if (!fileToUpload.exists()) {
                new ErrorAlert("File does not exist", "Uploaded file does not exist!",
                    "Please try upload again.");
                return;
            }

            // Upload XML file
            dataController.uploadXMLFile(fileToUpload);

            stage.close();
        });

        // Set stage properties and make it a modal window
        stage.setTitle("Import XML File");
        stage.setScene(scene);
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);

        // Styling
        scene.getStylesheets().addAll(parent.getScene().getStylesheets());
        stage.setResizable(false);
        stage.setWidth(600);
        stage.setHeight(300);

        stage.show();
    }


    /**
     * Validate the file that is being dragged
     *
     * @param files the list of files that's being dragged
     * @return whether the file is validated
     */
    private boolean validateFileDrag(List<File> files) {
        // Ensure that there is only 1 file
        if (files.size() > 1) {
            return false;
        }

        // Ensure it's an XML file
        var fileName = files.get(0).toString();
        var extensionIndex = fileName.lastIndexOf('.');

        return 0 < extensionIndex && fileName.substring(extensionIndex + 1).equals("xml");
    }
}