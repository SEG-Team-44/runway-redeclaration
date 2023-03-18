package com.team44.runwayredeclarationapp.view.component.inputs;

import java.util.function.Function;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * A combobox with custom object support
 *
 * @param <T> class of the objects which will be in the combobox
 */
public class SelectComboBox<T> extends ComboBox<T> {

    /**
     * The observable list that is bound to the combobox
     */
    private ObservableList<T> observableList;

    /**
     * Create a combobox
     */
    public SelectComboBox() {
        super();
    }

    /**
     * Create a combobox attached to an observable list
     *
     * @param observableListToSet the observable list
     */
    public SelectComboBox(ObservableList<T> observableListToSet) {
        super();

        this.observableList = observableListToSet;
        this.setItems(observableList);
    }

    /**
     * Set the method that will return the string to display for each object
     *
     * @param stringMethod the method of the class
     */
    public void setStringMethod(Function<T, String> stringMethod) {
        // Set the combobox converter
        setConverter(new StringConverter<T>() {
            @Override
            public String toString(T item) {
                if (item == null) {
                    return null;
                } else {
                    // call the method on the object
                    return stringMethod.apply(item);
                }
            }

            @Override
            public T fromString(String s) {
                return null;
            }
        });
    }

    /**
     * Get the observable list bound to the combobox
     *
     * @return the observable list
     */
    public ObservableList<T> getObservableList() {
        return observableList;
    }

    /**
     * Set the observable list that will be bound the combobox
     *
     * @param observableList the observable list
     */
    public void setObservableList(ObservableList<T> observableList) {
        this.observableList = observableList;
    }
}
