package com.team44.runwayredeclarationapp.ui.runway;

import com.team44.runwayredeclarationapp.controller.ParameterController;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.view.component.alert.ErrorListAlert;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * The class responsible for generate & display the UI for adding a new runway
 */
public class AddRunwayWindow extends ParameterController {

    public AddRunwayWindow(Window parent, Airport airport) {
        Stage stage = new Stage();

        //Components for physical inputs
        Label phyParameter = new Label("Physical parameters:");
        phyParameter.setFont(new Font(17));
        Label runwayL = new Label("Runway Length (m)");
        TextField runwayLTf = new TextField();
        Label runwayW = new Label("Runway Width (m)");
        TextField runwayWTf = new TextField();
        Label stripL = new Label("Distance between runway \n end & strip end (m)");
        TextField stripLTf = new TextField();
        Label stripW = new Label("Distance between runway \n centreline & strip edge (m)");
        TextField stripWTf = new TextField();
        Label clearW = new Label("Clearway Vertical Width (m)");
        TextField clearWTf = new TextField();
        Label resa = new Label("RESA Length (m) ");
        TextField resaTf = new TextField();
        resaTf.setPrefSize(60, 20);

        //Layout the physical input components
        GridPane phyPane = new GridPane();
        phyPane.setHgap(5);
        phyPane.setVgap(5);
        phyPane.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints(60),
            new ColumnConstraints(), new ColumnConstraints(60));
        phyPane.add(phyParameter, 0, 0, 4, 1);
        phyPane.addRow(1, runwayL, runwayLTf, runwayW, runwayWTf);
        phyPane.addRow(2, stripL, stripLTf, stripW, stripWTf);
        phyPane.addRow(3, clearW, clearWTf, resa, resaTf);

        //Components for one logical runway inputs
        String lbl1 = "Parameters For One Logical Runway:";
        TextField degreeTf1 = new TextField();
        CheckBox posCb1 = new CheckBox();
        TextField posTf1 = new TextField();
        TextField toraTf1 = new TextField();
        TextField todaTf1 = new TextField();
        TextField asdaTf1 = new TextField();
        TextField ldaTf1 = new TextField();
        TextField disThreshTf1 = new TextField();

        //Layout of components for logical runway input 1
        GridPane runway1 = getLayout(lbl1, degreeTf1, posCb1, posTf1, toraTf1, todaTf1, asdaTf1,
            ldaTf1, disThreshTf1);

        //Components for the other logical runway inputs
        String lbl2 = "Parameters for corresponding logical runway:";
        TextField degreeTf2 = new TextField();
        CheckBox posCb2 = new CheckBox();
        TextField posTf2 = new TextField();
        TextField toraTf2 = new TextField();
        TextField todaTf2 = new TextField();
        TextField asdaTf2 = new TextField();
        TextField ldaTf2 = new TextField();
        TextField disThreshTf2 = new TextField();

        //'Add runway' button
        Button addBtn = new Button("Add");
        addBtn.setFont(new Font(17));
        addBtn.setOnAction(ActionEvent -> {
            TextField[] textFields = {runwayLTf, runwayWTf, stripLTf, stripWTf, clearWTf, resaTf,
                toraTf1,
                todaTf1, asdaTf1, ldaTf1, toraTf2, todaTf2, asdaTf2, ldaTf2, disThreshTf2,
                disThreshTf1};

            //Close window if runway has been successfully added
            if (addNewRunway(posCb1.isSelected(), posCb2.isSelected(), posTf1.getText(),
                posTf2.getText(),
                degreeTf1.getText(), degreeTf2.getText(), textFields, airport)) {
                printAlert(true);

                stage.close();
            }

            //Display alert if add runway failed
            else {
                var errorAlert = new ErrorListAlert();
                errorAlert.setErrors(getErrors().toArray(new String[]{}));
                getErrors();
                errorAlert.show();
            }
        });

        //Layout of components for logical runway input 2
        GridPane runway2 = getLayout(lbl2, degreeTf2, posCb2, posTf2, toraTf2, todaTf2, asdaTf2,
            ldaTf2, disThreshTf2);

        //Setup main pane
        GridPane mainPane = new GridPane();
        mainPane.setAlignment(Pos.CENTER);

        mainPane.setPadding(new Insets(5, 5, 5, 5));
        mainPane.setVgap(10);

        //Add all layouts & components to main pane
        mainPane.add(phyPane, 0, 0, 2, 1);
        mainPane.add(runway1, 0, 1);
        mainPane.add(runway2, 0, 2);
        mainPane.add(addBtn, 1, 2);
        GridPane.setHalignment(addBtn, HPos.RIGHT);
        GridPane.setValignment(addBtn, VPos.BOTTOM);

        //Set scene
        Scene scene = new Scene(mainPane);
        mainPane.requestFocus();
        scene.setOnMouseClicked(mouseEvent -> {
            mainPane.requestFocus();
        });

        stage.setTitle("Add new runway");
        stage.setScene(scene);
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        stage.show();
    }

    /**
     * Generate the UI containing given components that ask for inputs for logical runways
     *
     * @param lblContent section title
     * @param degree     TextField for degree
     * @param posCb      checkbox of whether to input a position character
     * @param pos        TextField for position character
     * @param tora       TextField for TORA
     * @param toda       TextField for TODA
     * @param asda       TextField for ASDA
     * @param lda        TextField for LDA
     * @param disThresh  TextField for displaced threshold
     * @return a gridPane includes all components
     */
    private GridPane getLayout(String lblContent, TextField degree, CheckBox posCb, TextField pos,
        TextField tora, TextField toda, TextField asda, TextField lda, TextField disThresh) {

        Label lbl = new Label(lblContent);
        lbl.setFont(new Font(17));
        Label degreeLbl = new Label("Degree (01-36)");
        Label posLbl = new Label("Position (L/C/R)");
        Label toraLbl = new Label("TORA (m)");
        Label todaLbl = new Label("TODA (m)");
        Label asdaLbl = new Label("ASDA (m)");
        Label ldaLbl = new Label("LDA (m)");
        Label disThreshLbl = new Label("Displaced Threshold (m)");

        HBox posBox = new HBox();
        posBox.getChildren().addAll(posLbl, posCb);

        //Display the TextField only when checkbox is selected
        pos.visibleProperty().bind(posCb.selectedProperty());

        //Clear the Text in the TextField when deselecting the checkbox
        posCb.setOnAction(ActionEvent -> {
            if (!posCb.isSelected()) {
                pos.clear();
            }
        });

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints(60);

        //Adding all components to the GridPane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.getColumnConstraints().addAll(col1, col2, col1, col2);
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.add(lbl, 0, 0, 4, 1);
        gridPane.addRow(1, degreeLbl, degree, posBox, pos);
        gridPane.addRow(2, toraLbl, tora, asdaLbl, asda);
        gridPane.addRow(3, todaLbl, toda, ldaLbl, lda);
        gridPane.addRow(4, disThreshLbl, disThresh);

        return gridPane;
    }

    /**
     * Print an alert correspond to the boolean
     *
     * @param success if tasks have been successfully performed
     */
    @Override
    protected void printAlert(boolean success) {

        super.printAlert(success);
        Alert a;

        if (success) {
            a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Runway has been logged.");
        } else {
            a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Invalid Input.");
        }

        a.show();
    }
}
