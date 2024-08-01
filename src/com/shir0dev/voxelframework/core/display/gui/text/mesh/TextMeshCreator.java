
package com.shir0dev.voxelframework.core.display.gui.text.mesh;

import com.shir0dev.voxelframework.core.display.gui.text.GUIText;
import com.shir0dev.voxelframework.core.display.gui.text.font.data.Character;
import com.shir0dev.voxelframework.core.display.gui.text.font.data.Line;
import com.shir0dev.voxelframework.core.display.gui.text.font.data.MetaFile;
import com.shir0dev.voxelframework.core.display.gui.text.font.data.Word;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TextMeshCreator {
    public static final double LINE_HEIGHT = 0.029999999329447746;
    public static final int SPACE_ASCII = 32;
    public static final int NEW_LINE_ASCII = 10;
    private MetaFile metaData;

    public TextMeshCreator(File metaFile) {
        this.metaData = new MetaFile(metaFile);
    }

    public TextMeshData createTextMesh(GUIText text) {
        List<Line> lines = this.createStructure(text);
        return this.createQuadVertices(text, lines);
    }

    private List<Line> createStructure(GUIText guiText) {
        char[] chars = guiText.text().toCharArray();
        List<Line> lines = new ArrayList();
        Line currentLine = new Line(this.metaData.spaceWidth(), (double)guiText.fontSize(), (double)guiText.maxLineLength());
        Word currentWord = new Word((double)guiText.fontSize());
        char[] var6 = chars;
        int var7 = chars.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            char c = var6[var8];
            if (c == ' ') {
                boolean added = currentLine.attemptToAddWord(currentWord);
                if (!added) {
                    lines.add(currentLine);
                    currentLine = new Line(this.metaData.spaceWidth(), (double)guiText.fontSize(), (double)guiText.maxLineLength());
                    currentLine.attemptToAddWord(currentWord);
                }

                currentWord = new Word((double)guiText.fontSize());
            } else if (c == '\n') {
                currentLine.attemptToAddWord(currentWord);
                lines.add(currentLine);
                currentLine = new Line(this.metaData.spaceWidth(), (double)guiText.fontSize(), (double)guiText.maxLineLength());
                currentWord = new Word((double)guiText.fontSize());
            } else {
                Character character = this.metaData.getCharacter(c);
                currentWord.addCharacter(character);
            }
        }

        this.completeStructure(lines, currentLine, currentWord, guiText);
        return lines;
    }

    private void completeStructure(List<Line> lines, Line currentLine, Word currentWord, GUIText guiText) {
        boolean added = currentLine.attemptToAddWord(currentWord);
        if (!added) {
            lines.add(currentLine);
            currentLine = new Line(this.metaData.spaceWidth(), (double)guiText.fontSize(), (double)guiText.maxLineLength());
            currentLine.attemptToAddWord(currentWord);
        }

        lines.add(currentLine);
    }

    private TextMeshData createQuadVertices(GUIText guiText, List<Line> lines) {
        guiText.setNumLines(lines.size());
        double cursorX = 0.0;
        double cursorY = 0.0;
        List<Float> vertices = new ArrayList();
        List<Float> texCoords = new ArrayList();

        for(Iterator var9 = lines.iterator(); var9.hasNext(); cursorY += 0.029999999329447746 * (double)guiText.fontSize()) {
            Line line = (Line)var9.next();
            if (guiText.isCentered()) {
                cursorX = (line.maxLength() - line.lineLength()) / 2.0;
            }

            for(Iterator var11 = line.words().iterator(); var11.hasNext(); cursorX += this.metaData.spaceWidth() * (double)guiText.fontSize()) {
                Word word = (Word)var11.next();

                Character letter;
                for(Iterator var13 = word.characters().iterator(); var13.hasNext(); cursorX += letter.xAdvance() * (double)guiText.fontSize()) {
                    letter = (Character)var13.next();
                    this.addVerticesForCharacter(cursorX, cursorY, letter, (double)guiText.fontSize(), vertices);
                    addTexCoords(texCoords, letter.texCoordX(), letter.texCoordY(), letter.maxTexCoordX(), letter.maxTexCoordY());
                }
            }

            cursorX = 0.0;
        }

        return new TextMeshData(listToArray(vertices), listToArray(texCoords));
    }

    private void addVerticesForCharacter(double cursorX, double cursorY, Character character, double fontSize, List<Float> vertices) {
        double x = cursorX + character.xOffset() * fontSize;
        double y = cursorY + character.yOffset() * fontSize;
        double xMax = x + character.sizeX() * fontSize;
        double yMax = y + character.sizeY() * fontSize;
        double xFinal = 2.0 * x - 1.0;
        double yFinal = -2.0 * y + 1.0;
        double xMaxFinal = 2.0 * xMax - 1.0;
        double yMaxFinal = -2.0 * yMax + 1.0;
        addVertices(vertices, xFinal, yFinal, xMaxFinal, yMaxFinal);
    }

    private static void addVertices(List<Float> vertices, double x, double y, double xMax, double yMax) {
        vertices.add((float)x);
        vertices.add((float)y);
        vertices.add((float)x);
        vertices.add((float)yMax);
        vertices.add((float)xMax);
        vertices.add((float)yMax);
        vertices.add((float)xMax);
        vertices.add((float)yMax);
        vertices.add((float)xMax);
        vertices.add((float)y);
        vertices.add((float)x);
        vertices.add((float)y);
    }

    private static void addTexCoords(List<Float> texCoords, double x, double y, double xMax, double yMax) {
        texCoords.add((float)x);
        texCoords.add((float)y);
        texCoords.add((float)x);
        texCoords.add((float)yMax);
        texCoords.add((float)xMax);
        texCoords.add((float)yMax);
        texCoords.add((float)xMax);
        texCoords.add((float)yMax);
        texCoords.add((float)xMax);
        texCoords.add((float)y);
        texCoords.add((float)x);
        texCoords.add((float)y);
    }

    private static float[] listToArray(List<Float> listOfFloats) {
        float[] arr = new float[listOfFloats.size()];

        for(int i = 0; i < arr.length; ++i) {
            arr[i] = (Float)listOfFloats.get(i);
        }

        return arr;
    }
}
