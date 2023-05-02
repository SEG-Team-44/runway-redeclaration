package com.team44.runwayredeclarationapp.ui;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Class that create and display Instructions
 */
public class Instruction {
    /**
     * Build and display instruction window
     */
    public Instruction() {
        TabPane tabPane = new TabPane();
        Tab page1 = new Tab("Airport & Runway");
        Tab page2 = new Tab("Obstacle & Calculation");
        Tab page3 = new Tab("Others");
        page1.closableProperty().setValue(Boolean.FALSE);
        page2.closableProperty().setValue(Boolean.FALSE);
        page3.closableProperty().setValue(Boolean.FALSE);

        //display instructions
        page1.setContent(setUpLayout("/images/airport&runway.png"));
        page2.setContent(setUpLayout("/images/obstacle&calculation.png"));
        page3.setContent(setUpLayout("/images/others.png"));
        tabPane.getTabs().addAll(page1, page2, page3);

        Scene root = new Scene(tabPane);

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(root);
        stage.show();
    }

    /**
     * Create an instruction pane with the given image
     * @param imagePath image path
     * @return instruction pane
     */
    private Pane setUpLayout(String imagePath) {
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(700);
        imageView.setFitWidth(950);

        Pane pane = new Pane();
        pane.getChildren().add(imageView);
        return pane;
    }
}
