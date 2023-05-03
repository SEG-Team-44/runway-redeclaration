package com.team44.runwayredeclarationapp.ui.xml;

import com.team44.runwayredeclarationapp.controller.FileController;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorAlert;
import com.team44.runwayredeclarationapp.view.component.alert.InfoAlert;
import java.io.File;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tooltip;
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
import javafx.util.Duration;

/**
 * The window for importing xml file
 */
public class ImportXMLWindow extends Stage {

    /**
     * The selected file object to import
     */
    private final SimpleObjectProperty<File> fileSimpleObjectProperty = new SimpleObjectProperty<>();

    /**
     * The data controller to import/export XML files
     */
    private final FileController fileController;

    /**
     * Create the window for importing xml file
     *
     * @param parent         the parent window
     * @param fileController the data controller
     */
    public ImportXMLWindow(Window parent, FileController fileController) {
        this.fileController = fileController;

        // Set successful import event
        fileController.setFileUploadSuccessfulListener(this::uploadSuccessful);

        //Setup main pane
        var mainPane = new VBox();
        mainPane.getStyleClass().add("drag-drop-window");
        mainPane.setSpacing(15);
        mainPane.setAlignment(Pos.CENTER);
        HBox.setHgrow(mainPane, Priority.ALWAYS);
        VBox.setVgrow(mainPane, Priority.ALWAYS);

        // Import button pane
        var importBtnsPane = new HBox();
        importBtnsPane.setAlignment(Pos.CENTER_RIGHT);
        importBtnsPane.setSpacing(10);
        // Import buttons
        var importBtn = new Button("Import");
        importBtn.setDisable(true);
        var importResetBtn = new Button("Reset and Import");
        importResetBtn.disableProperty().bindBidirectional(importBtn.disableProperty());

        // Tooltips
        var importTooltip = new Tooltip("Import the data from the XML file.");
        importTooltip.setShowDelay(Duration.millis(50));
        importBtn.setTooltip(importTooltip);
        var importResetTooltip = new Tooltip(
            "Overwrite all existing program data with data in XML file.");
        importResetTooltip.setShowDelay(Duration.millis(50));
        importResetBtn.setTooltip(importResetTooltip);

        importBtnsPane.getChildren().addAll(importResetBtn, importBtn);

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
        mainPane.getChildren().addAll(dragDropPane, importBtnsPane);

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
        droppedPane.setSpacing(15);
        HBox.setHgrow(droppedPane, Priority.ALWAYS);
        VBox.setVgrow(droppedPane, Priority.ALWAYS);

        // Dropped screen text
        var uploadSuccessfulText = new Text("Upload successful!");
        var fileNameTextProperty = new SimpleStringProperty();
        var fileNameText = new Text();
        fileNameText.setWrappingWidth(150);
        fileNameText.getStyleClass().add("small-text");
        fileNameText.textProperty().bind(fileNameTextProperty);
        fileNameText.setTextAlignment(TextAlignment.CENTER);
        uploadSuccessfulText.setTextAlignment(TextAlignment.CENTER);

        // Cancel upload button
        var cancelUploadButton = new Button("Cancel");
        cancelUploadButton.setAlignment(Pos.TOP_RIGHT);
        cancelUploadButton.setOnAction(event -> {
            // Remove the uploaded file
            fileSimpleObjectProperty.set(null);
        });

        droppedPane.getChildren().addAll(uploadSuccessfulText, fileNameText, cancelUploadButton);

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
                // File successfully uploaded
                fileNameTextProperty.set(fileSimpleObjectProperty.get().getName());

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
            importFile(false);
        });
        importResetBtn.setOnAction(event -> {
            importFile(true);
        });

        // Set stage properties and make it a modal window
        this.setTitle("Import XML File");
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

    /**
     * Import the xml file stored in the file object property
     *
     * @param reset whether to reset the existing data
     */
    private void importFile(boolean reset) {
        var fileToUpload = fileSimpleObjectProperty.get();

        // Ensure file still exists
        if (!fileToUpload.exists()) {
            new ErrorAlert("File does not exist", "Uploaded file does not exist!",
                "Please try upload again.").show();

            fileSimpleObjectProperty.set(null);
            return;
        }

        // Upload XML file
        fileController.uploadXMLFile(fileToUpload, reset);
    }

    /**
     * Called when xml upload has been successful
     */
    public void uploadSuccessful() {
        // Show alert
        new InfoAlert("Upload successful", "XML file has been successfully uploaded!",
            null).show();

        // Close the window
        this.close();
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
