package com.team44.runwayredeclarationapp.controller;


import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class RunwayInitialisation {



   public RunwayInitialisation() {
       Stage stage = new Stage();

       Label degree = new Label("Degree");
       TextField degreeTf = new TextField();

       Label pos = new Label("Position (L/C/R)");
       CheckBox posCb = new CheckBox();

       TextField posTf = new TextField();
       //Display the TextField only when checkbox is selected
       posTf.visibleProperty().bind(posCb.selectedProperty());

       //Clear the Text in the TextField when unselecting the checkbox
       posCb.setOnAction(ActionEvent -> {
           if (!posCb.isSelected()) {
               posTf.clear();
           }
       });

       HBox posBox = new HBox();
       posBox.setSpacing(5);
       posBox.getChildren().addAll(pos, posCb);

       Label runwayL = new Label("Runway Length");
       TextField runwayLTf = new TextField();

       Label runwayW = new Label("Runway Width");
       TextField runwayWTf = new TextField();

       Label stripL = new Label("Strip Length");
       TextField stripLTf = new TextField();

       Label stripW = new Label("Strip Width");
       TextField stripWTf = new TextField();

       Label stopL = new Label("Stopway Length");
       TextField stopLTf = new TextField();

       Label stopW = new Label("Stopway Width");
       TextField stopWTf = new TextField();

       Label clearL = new Label("Clearway Length");
       TextField clearLTf = new TextField();

       Label clearW = new Label("Clearway Width");
       TextField clearWTf = new TextField();

       Label tora = new Label("TORA");
       TextField toraTf = new TextField();

       Label toda = new Label("TODA");
       TextField todaTf = new TextField();

       Label asda = new Label("ASDA");
       TextField asdaTf = new TextField();

       Label lda = new Label("LDA");
       TextField ldaTf = new TextField();

       Label disThresh = new Label("Displaced Threshold");
       TextField disThreshTf = new TextField();

       Button addBtn = new Button("Log in");
       addBtn.setAlignment(Pos.CENTER_RIGHT);

       GridPane getDataPage = new GridPane();
       getDataPage.setAlignment(Pos.CENTER_LEFT);

       getDataPage.setPadding(new Insets(10,10,10,10));
       getDataPage.setVgap(7);
       getDataPage.setHgap(5);

       getDataPage.addColumn(0, degree, runwayL, runwayW, stopL, stopW, tora, asda, disThresh);
       getDataPage.addColumn(1, degreeTf,runwayLTf, runwayWTf, stopLTf, stopWTf, toraTf, asdaTf, disThreshTf);
       getDataPage.addColumn(2, posBox, stripL, stripW, clearL, clearW, toda, lda);
       getDataPage.addColumn(3, posTf, stripWTf, stripLTf, clearLTf, clearWTf, todaTf, ldaTf, addBtn);
       GridPane.setHalignment(addBtn, HPos.RIGHT);

       //TextField[] textFields = {degreeTf, posTf, runwayLTf, runwayWTf, stripLTf, stripWTf, stopLTf, stopWTf, clearLTf, clearWTf,toraTf, todaTf, asdaTf, ldaTf, disThreshTf};


       Scene scene = new Scene(getDataPage);
       stage.setTitle("Log in runway");
       stage.setScene(scene);
       stage.show();
   }

/*   private Boolean validInput(TextField[] textFields, CheckBox posCb) {


       return false;
   }*/
}
