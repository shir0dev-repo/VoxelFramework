package com.shir0dev.voxelframework.core.display.gui.text.font.data;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private double maxLength;
    private double spacing;
    private List<Word> words = new ArrayList();
    private double currentLineLength = 0.0;

    public double maxLength() {
        return this.maxLength;
    }

    public double spacing() {
        return this.spacing;
    }

    public List<Word> words() {
        return this.words;
    }

    public double lineLength() {
        return this.currentLineLength;
    }

    public Line(double spaceWidth, double fontSize, double maxLength) {
        this.spacing = spaceWidth * fontSize;
        this.maxLength = maxLength;
    }

    public boolean attemptToAddWord(Word word) {
        double additionalLength = word.width();
        additionalLength += !this.words.isEmpty() ? this.spacing : 0.0;
        if (this.currentLineLength + additionalLength <= this.maxLength) {
            this.words.add(word);
            this.currentLineLength += additionalLength;
            return true;
        } else {
            return false;
        }
    }
}
