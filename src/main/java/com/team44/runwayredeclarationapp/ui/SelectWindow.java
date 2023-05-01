package com.team44.runwayredeclarationapp.ui;

import com.team44.runwayredeclarationapp.event.OnDeleteListener;
import com.team44.runwayredeclarationapp.event.OnSelectListener;
import java.util.function.Function;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * A selection window with a list view
 *
 * @param <T> the type of objects in the list view
 */
public class SelectWindow<T> extends Stage {

    /**
     * The list view
     */
    private final ListView<T> options;
    /**
     * The listener called when an item has been selected
     */
    private OnSelectListener onSelectListener;

    private OnDeleteListener onDeleteListener;

    /**
     * Create a select window with a list view
     *
     * @param parent         the parent window
     * @param title          the title of the list
     * @param observableList the observable list that will go in the list view
     */
    public SelectWindow(Window parent, String title, ObservableList<T> observableList) {
        // Add the list view in the scroll pane
        ScrollPane scroll = new ScrollPane();
        options = new ListView<>(observableList);
        scroll.setContent(options);

        // Create the select button
        Button selectBtn = new Button("Select");
        selectBtn.setFont(new Font(17));

        //create the delete button
        Button deleteBtn = new Button("Delete");
        deleteBtn.setFont(new Font(17));

        //disable delete btn when nothing is selected
        deleteBtn.setDisable(true);
        options.getSelectionModel().selectedItemProperty().addListener(
            (observableValue, s, t1) -> deleteBtn.setDisable(false));

        //call the delete listener when button pressed
        deleteBtn.setOnAction(ActionEvent -> {
            var selectedItem = options.getSelectionModel().getSelectedItem();
            if ((onDeleteListener != null) && (selectedItem != null)) {
                // Call the listener and pass the selected obstacle
                onDeleteListener.delete(options.getSelectionModel().getSelectedItem());
            }
        });

        // List view properties
        scroll.setFitToWidth(true);
        options.setOnKeyPressed(event -> {
            // Enter key
            if (event.getCode() == KeyCode.ENTER) {
                selectBtn.fire();
            }
        });
        options.setOnMouseClicked(event -> {
            // Double click
            if (event.getClickCount() == 2) {
                selectBtn.fire();
            }
        });

        // Set the button event
        selectBtn.setOnAction(ActionEvent -> {
            var selectedItem = options.getSelectionModel().getSelectedItem();
            if ((onSelectListener != null) && (selectedItem != null)) {
                // Call the listener and pass the selected obstacle
                onSelectListener.select(options.getSelectionModel().getSelectedItem());
            }
        });

        // Enable button only when a user selected an option
        selectBtn.setDisable(true);
        options.getSelectionModel().selectedItemProperty().addListener(
            (observableValue, s, t1) -> selectBtn.setDisable(false));

        // Create a title
        Label lbl = new Label("Select " + title.toLowerCase() + ":");
        lbl.setFont(new Font(18));

        //combine the 2 buttons
        HBox buttons = new HBox();
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(deleteBtn, selectBtn);

        //combine the scroll pane & button
        VBox optionBox = new VBox();
        optionBox.getChildren().addAll(lbl, scroll, buttons);
        optionBox.setAlignment(Pos.CENTER);
        optionBox.setSpacing(5);
        optionBox.setPadding(new Insets(5));

        // Create the scene
        Scene scene = new Scene(optionBox);
        this.setTitle("Select " + title);
        this.initOwner(parent);
        this.initModality(Modality.WINDOW_MODAL);
        this.setResizable(false);
        this.setScene(scene);
        this.show();
    }

    /**
     * Set the listener to be called when an item has been successfully selected
     *
     * @param onSelectListener the listener
     */
    public void setOnSelect(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    /**
     * Set the listener to be called when an item is to be deleted
     *
     * @param onDeleteListener the listener
     */
    public void setOnDelete(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    /**
     * Set the method that will return the string to display for each object
     *
     * @param stringMethod the method of the class
     */
    public void setStringMethod(Function<T, String> stringMethod) {
        // Set the list view cell factory
        options.setCellFactory(event -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                // if none, leave it empty
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(stringMethod.apply(item));
                }
            }
        });
    }

    /**
     * Refresh the list of options
     */
    public void refresh() {
        options.refresh();
    }
}
