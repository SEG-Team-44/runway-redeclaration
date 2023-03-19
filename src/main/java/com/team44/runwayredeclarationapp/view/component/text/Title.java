package com.team44.runwayredeclarationapp.view.component.text;

import javafx.scene.text.Text;

/**
 * Text node styled as a title
 */
public class Title extends Text {

    /**
     * Create a title widget
     *
     * @param text the title text
     */
    public Title(String text) {
        setText(text);
        getStyleClass().add("title-text");
    }
}
