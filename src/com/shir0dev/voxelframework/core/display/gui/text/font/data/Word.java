package com.shir0dev.voxelframework.core.display.gui.text.font.data;

import java.util.ArrayList;
import java.util.List;

public class Word {
    private List<Character> characters = new ArrayList();
    private double width = 0.0;
    private final double fontSize;

    public List<Character> characters() {
        return this.characters;
    }

    public double width() {
        return this.width;
    }

    public Word(double fontSize) {
        this.fontSize = fontSize;
    }

    public void addCharacter(Character character) {
        this.characters.add(character);
        this.width += character.xAdvance() * this.fontSize;
    }
}
