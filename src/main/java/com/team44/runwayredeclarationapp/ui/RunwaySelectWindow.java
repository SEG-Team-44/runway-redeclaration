package com.team44.runwayredeclarationapp.ui;

import com.team44.runwayredeclarationapp.controller.ParameterController;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Runway;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class RunwaySelectWindow extends ParameterController {

    Stage stage;

    /**
     * Initialising the stage
     *
     * @param parent
     * @param airport current airport
     */
    public RunwaySelectWindow(Window parent, Airport airport) {
        stage = new Stage();
        stage.setTitle("Runway Selection");
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        //Pop up option window for user to select current runway
        showSelectScene(airport);
    }

    /**
     * Setup & display the option window
     *
     * @param airport current airport
     */
    private void showSelectScene(Airport airport) {
        //listing all runways recorded in the system
        ScrollPane scroll = new ScrollPane();
        ListView<String> options = new ListView<>();

        for (Runway runway : airport.getRunways()) {
            options.getItems().add(runway.getPhyId());
        }
        scroll.setContent(options);

        Button selectBtn = new Button("Select");
        selectBtn.setFont(new Font(17));
        //enable button only when a user selected a runway
        selectBtn.setDisable(true);

        options.getSelectionModel().selectedItemProperty().addListener(
            (observableValue, s, t1) -> selectBtn.setDisable(false));

        selectBtn.setOnAction(ActionEvent -> {
            printAlert(true);
            stage.close();
        });

        Label lbl = new Label("Select a Runway:");
        lbl.setFont(new Font(18));

        //combine the scroll pane & button
        VBox optionBox = new VBox();
        optionBox.getChildren().addAll(lbl, scroll, selectBtn);
        optionBox.setAlignment(Pos.CENTER);
        optionBox.setSpacing(5);
        optionBox.setPadding(new Insets(5));

        Scene scene = new Scene(optionBox);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    protected void printAlert(boolean success) {

        super.printAlert(success);
        Alert a;

        if (success) {
            a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Runway has been selected successfully.");
        } else {
            a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Select a runway.");
        }

        a.show();
    }
}
