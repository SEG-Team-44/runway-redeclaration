package com.team44.runwayredeclarationapp.model;

import java.util.List;

/**
 * The data structure for pairing an object with a list of errors
 *
 * @param <T> the type of the object in the pairing
 */
public class ErrorObjectPair<T> {

    /**
     * The list of errors
     */
    private final List<String> errors;
    /**
     * The object in the pairing
     */
    private final T object;

    /**
     * Create an error object pair
     *
     * @param object the object
     * @param errors the list of errors
     */
    public ErrorObjectPair(T object, List<String> errors) {
        this.errors = errors;
        this.object = object;
    }

    /**
     * Check if there are any errors
     *
     * @return whether there are any errors
     */
    public boolean hasErrors() {
        return errors.size() > 0;
    }

    /**
     * Get the list of errors
     *
     * @return the list of errors
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Get the list of errors as a string array
     *
     * @return the list of errors in a string array
     */
    public String[] getErrorsArray() {
        return errors.toArray(new String[]{});
    }

    /**
     * Get the object in the pairing
     *
     * @return the object
     */
    public T getObject() {
        return object;
    }
}
