package com.team44.runwayredeclarationapp.model.form;

import com.team44.runwayredeclarationapp.model.PRunway;
import com.team44.runwayredeclarationapp.model.Runway;
import com.team44.runwayredeclarationapp.view.component.inputs.DoubleField;
import com.team44.runwayredeclarationapp.view.component.inputs.IntegerField;
import com.team44.runwayredeclarationapp.view.component.inputs.RegexField;
import java.util.Arrays;
import java.util.stream.Stream;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * Form for adding a new runway
 */
public class RunwayForm extends BaseForm {

    /**
     * The physical parameters of the runway
     */
    private final DoubleField runwayLTf = new DoubleField();
    private final DoubleField runwayWTf = new DoubleField();
    private final DoubleField stripLTf = new DoubleField();
    private final DoubleField stripWTf = new DoubleField();
    private final DoubleField clearWTf = new DoubleField();
    private final DoubleField resaTf = new DoubleField();

    /**
     * The degree and position of the logical runways
     */
    private final IntegerField degreeTf1 = new IntegerField();
    private final IntegerField degreeTf2 = new IntegerField();
    private final ComboBox<Character> posCb1 = createPosComboBox();
    private final ComboBox<Character> posCb2 = createPosComboBox();

    /**
     * Parameters for logical runway 1
     */
    private final DoubleField toraTf1 = new DoubleField();
    private final DoubleField todaTf1 = new DoubleField();
    private final DoubleField asdaTf1 = new DoubleField();
    private final DoubleField ldaTf1 = new DoubleField();
    private final DoubleField disThreshTf1 = new DoubleField();

    /**
     * Parameters for logical runway 2
     */
    private final DoubleField toraTf2 = new DoubleField();
    private final DoubleField todaTf2 = new DoubleField();
    private final DoubleField asdaTf2 = new DoubleField();
    private final DoubleField ldaTf2 = new DoubleField();
    private final DoubleField disThreshTf2 = new DoubleField();

    /**
     * Create the form for adding a new runway
     */
    public RunwayForm() {
        // Add listeners to make the combobox dependent on each other
        posCb1.valueProperty().addListener(createPosComboBoxListener(posCb2));
        posCb2.valueProperty().addListener(createPosComboBoxListener(posCb1));

        // Bind disable property
        posCb1.disableProperty().bindBidirectional(posCb2.disableProperty());

        // Degree binding
        var converter = new StringConverter<String>() {
            /**
             * Logical runway 1
             */
            @Override
            public String toString(String value) {
                if (value.isEmpty() || value.equals("-")) {
                    return "";
                } else {
                    var intValue = Integer.parseInt(value);
                    var newValue = intValue + (intValue > 18 ? -18 : 18);
                    return Integer.toString(newValue);
                }
            }

            /**
             * Input right
             */
            @Override
            public String fromString(String value) {
                return toString(value);
            }
        };

        // Set the bindings for the degrees
        Bindings.bindBidirectional(degreeTf1.textProperty(),
            degreeTf2.textProperty(), converter);

        degreeTf1.setDigits(2);
        degreeTf2.setDigits(2);
        stripWTf.setValue(100);
    }

    /**
     * Create the change listener to make a position combobox dependent on another position
     * combobox
     *
     * @param otherPosComboBox the position combobox
     * @return the change listener
     */
    private ChangeListener<Character> createPosComboBoxListener(
        ComboBox<Character> otherPosComboBox) {
        return (observable, oldValue, newValue) -> {
            if (newValue == null) {
                otherPosComboBox.setValue(null);
            } else if (newValue.equals('L')) {
                otherPosComboBox.setValue('R');
            } else if (newValue.equals('C')) {
                otherPosComboBox.setValue('C');
            } else if (newValue.equals('R')) {
                otherPosComboBox.setValue('L');
            }
        };
    }

    /**
     * Create a position combobox with the preset characters
     *
     * @return the position combobox
     */
    private ComboBox<Character> createPosComboBox() {
        return new ComboBox<>(FXCollections.observableArrayList(null, 'L', 'C', 'R'));
    }

    /**
     * Set the parameters of a specified runway to the fields in the form
     *
     * @param runway the specified runway
     */
    public void setRunway(Runway runway) {
        // Get the logical ids
        String runway1 = runway.getLogicId1();
        String runway2 = runway.getLogicId2();

        // Don't let user change the pos when modifying
        posCb1.setDisable(true);

        // Set the values of the fields
        if (runway instanceof PRunway) {
            posCb1.setValue(((PRunway) runway).getPos1());
        }
        degreeTf1.setValue(runway.getDegree1());
        degreeTf2.setValue(runway.getDegree2());
        runwayLTf.setValue(runway.getRunwayL());
        runwayWTf.setValue(runway.getRunwayW());
        stripLTf.setValue(runway.getStripL());
        stripWTf.setValue(runway.getStripW());
        clearWTf.setValue(runway.getClearwayW());
        resaTf.setValue(runway.getResaL());
        toraTf1.setValue(runway.getTora(runway1));
        todaTf1.setValue(runway.getToda(runway1));
        asdaTf1.setValue(runway.getAsda(runway1));
        ldaTf1.setValue(runway.getLda(runway1));
        disThreshTf1.setValue(runway.getDisThresh(runway2));
        toraTf2.setValue(runway.getTora(runway2));
        todaTf2.setValue(runway.getToda(runway2));
        asdaTf2.setValue(runway.getAsda(runway2));
        ldaTf2.setValue(runway.getLda(runway2));
        disThreshTf2.setValue(runway.getDisThresh(runway1));
    }

    /**
     * Get the array of runway parameters that can be used to create/modify a runway object
     *
     * @return the array of runway parameters
     */
    public double[] getParameters() {
        DoubleField[] parameterFields = {runwayLTf, runwayWTf, stripLTf, stripWTf, clearWTf, resaTf,
            toraTf1, todaTf1, asdaTf1, ldaTf1,
            toraTf2, todaTf2, asdaTf2, ldaTf2, disThreshTf2,
            disThreshTf1};

        return Stream.of(parameterFields).mapToDouble(DoubleField::getValue).toArray();
    }

    /**
     * Validate the fields in the form
     *
     * @return whether all the fields are valid
     */
    @Override
    public Boolean isValid() {
        // Get all the parameter fields that need to be validated
        RegexField[] parameterFields = {runwayLTf, runwayWTf, stripLTf, stripWTf,
            clearWTf, resaTf,
            degreeTf1, degreeTf2,
            toraTf1, todaTf1, asdaTf1, ldaTf1, disThreshTf1,
            toraTf2, todaTf2, asdaTf2, ldaTf2, disThreshTf2};

        // Validate each of the parameter text fields in an array
        var validateRegex = Stream.of(parameterFields).map(RegexField::isInputValid)
            .toArray(Boolean[]::new);

        // Return true if all validations are true
        return Arrays.stream(validateRegex).allMatch(Boolean::booleanValue);
    }

    public DoubleField getRunwayLTf() {
        return runwayLTf;
    }

    public DoubleField getRunwayWTf() {
        return runwayWTf;
    }

    public DoubleField getStripLTf() {
        return stripLTf;
    }

    public DoubleField getStripWTf() {
        return stripWTf;
    }

    public DoubleField getClearWTf() {
        return clearWTf;
    }

    public DoubleField getResaTf() {
        return resaTf;
    }

    public IntegerField getDegreeTf1() {
        return degreeTf1;
    }

    public ComboBox<Character> getPosCb1() {
        return posCb1;
    }

    public IntegerField getDegreeTf2() {
        return degreeTf2;
    }

    public ComboBox<Character> getPosCb2() {
        return posCb2;
    }

    public DoubleField getToraTf1() {
        return toraTf1;
    }

    public DoubleField getTodaTf1() {
        return todaTf1;
    }

    public DoubleField getAsdaTf1() {
        return asdaTf1;
    }

    public DoubleField getLdaTf1() {
        return ldaTf1;
    }

    public DoubleField getDisThreshTf1() {
        return disThreshTf1;
    }

    public DoubleField getToraTf2() {
        return toraTf2;
    }

    public DoubleField getTodaTf2() {
        return todaTf2;
    }

    public DoubleField getAsdaTf2() {
        return asdaTf2;
    }

    public DoubleField getLdaTf2() {
        return ldaTf2;
    }

    public DoubleField getDisThreshTf2() {
        return disThreshTf2;
    }
}
