module com.team44.runwayredeclarationapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires xstream;

    requires com.dlsc.formsfx;
    requires org.apache.logging.log4j;
    requires java.xml;
    requires org.mockito;
    requires javafx.swing;

    opens com.team44.runwayredeclarationapp to javafx.fxml;
    opens com.team44.runwayredeclarationapp.model to xstream;
    opens com.team44.runwayredeclarationapp.utility.xml to xstream;
    exports com.team44.runwayredeclarationapp;
}
