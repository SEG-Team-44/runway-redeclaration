module com.team44.runwayredeclarationapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.apache.logging.log4j;

    opens com.team44.runwayredeclarationapp to javafx.fxml;
    exports com.team44.runwayredeclarationapp;
}