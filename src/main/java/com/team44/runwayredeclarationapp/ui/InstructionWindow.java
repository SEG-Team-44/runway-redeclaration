package com.team44.runwayredeclarationapp.ui;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Class that create and display Instructions
 */
public class InstructionWindow extends Stage{
    /**
     * Build and display instruction window
     * @param parent parent window
     */
    public InstructionWindow(Window parent) {
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

        Scene mainScene = new Scene(tabPane);

        this.setTitle("Instruction");
        this.setResizable(false);
        this.initOwner(parent);
        this.initModality(Modality.WINDOW_MODAL);
        this.setScene(mainScene);
        this.show();
    }

    /**
     * Create an instruction pane with the given image
     * @param imagePath image path
     * @return instruction pane
     */
    private Pane setUpLayout(String imagePath) {
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(600);
        imageView.setFitWidth(800);

        Pane pane = new Pane();
        pane.getChildren().add(imageView);
        return pane;
    }
}
