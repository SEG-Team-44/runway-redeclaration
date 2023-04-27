package com.team44.runwayredeclarationapp.ui.userlog;

import com.team44.runwayredeclarationapp.controller.DataController;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * The window for viewing the user log
 */
public class ViewLogWindow extends Stage {

    /**
     * Create a view log window
     *
     * @param parent         the parent window
     * @param dataController the data controller of the program
     */
    public ViewLogWindow(Window parent, DataController dataController) {
        //Setup main pane (log text)
        var mainPane = new StackPane();

        // Create the text field to show the log
        var logTextField = new TextArea();
        logTextField.setEditable(false);

        // Bind the log text
        logTextField.textProperty().bind(
            Bindings.createStringBinding(() -> String.join("\n", dataController.getUserLog()),
                dataController.getUserLog()));

        // Clear button
        var clearBtn = new Button("Clear");
        clearBtn.setOnAction(event -> {
            dataController.getUserLog().clear();
        });

        // Add the nodes
        mainPane.getChildren().addAll(logTextField, clearBtn);
        StackPane.setAlignment(clearBtn, Pos.TOP_RIGHT);

        //Set scene
        Scene scene = new Scene(mainPane);
        logTextField.requestFocus();
        scene.setOnMouseClicked(mouseEvent -> logTextField.requestFocus());

        // Set stage properties and make it a modal window
        this.setTitle("View Log");
        this.setScene(scene);
        this.initOwner(parent);
        this.initModality(Modality.WINDOW_MODAL);

        // Styling
        scene.getStylesheets().addAll(parent.getScene().getStylesheets());
        this.setResizable(false);
        this.setWidth(600);
        this.setHeight(500);

        this.show();
    }
}
