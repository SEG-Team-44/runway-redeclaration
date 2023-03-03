package com.team44.runwayredeclarationapp.controller;

import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Runway;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ModifyParameters {

    Stage stage;

    public ModifyParameters(Window parent, Airport airport) {

        stage = new Stage();
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        optionScene(airport);
    }

    private void optionScene(Airport airport) {

        ScrollPane scroll = new ScrollPane();
        ListView<String> options = new ListView<>();

        for (Runway runway : airport.getRunways()) {
            options.getItems().add(runway.getId());
        }
         scroll.setContent(options);

        Button modifyBtn = new Button("Modify");
        modifyBtn.setDisable(true);

        options.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, t1) -> modifyBtn.setDisable(false));

        modifyBtn.setOnAction(ActionEvent -> {
            modifyScene(options.getSelectionModel().getSelectedItem());
        });
        VBox optionBox = new VBox();
        optionBox.getChildren().addAll(scroll, modifyBtn);
    }

    private void modifyScene(String id) {
        
    }
}
