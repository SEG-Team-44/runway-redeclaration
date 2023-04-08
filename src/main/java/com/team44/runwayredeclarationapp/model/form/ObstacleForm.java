package com.team44.runwayredeclarationapp.model.form;

import com.team44.runwayredeclarationapp.model.Obstacle;
import com.team44.runwayredeclarationapp.view.component.inputs.DoubleField;
import com.team44.runwayredeclarationapp.view.component.inputs.RegexField;

/**
 * Form for adding a new obstacle
 */
public class ObstacleForm extends BaseForm {

    /**
     * Input field for obstacle name
     */
    private final RegexField obstacleNameInput = new RegexField("^.{0,60}$");
    /**
     * Input field for obstacle height
     */
    private final DoubleField obstacleHeightInput = new DoubleField();

    /**
     * Set an existing obstacle, if any, to modify
     *
     * @param obstacle the existing obstacle
     */
    public void setObstacle(Obstacle obstacle) {
        obstacleNameInput.setText(obstacle.getObstName());
        obstacleHeightInput.setValue(obstacle.getHeight());
    }

    /**
     * Validate the fields in the form
     *
     * @return whether all the fields are valid
     */
    @Override
    public Boolean isValid() {
        return obstacleNameInput.isInputValid() && obstacleHeightInput.isInputValid();
    }

    public RegexField getObstacleNameInput() {
        return obstacleNameInput;
    }

    public DoubleField getObstacleHeightInput() {
        return obstacleHeightInput;
    }
}
