package com.shir0dev.voxelframework.core.display.gui.text;

import com.shir0dev.voxelframework.core.display.gui.text.font.FontType;
import com.shir0dev.voxelframework.core.transform.util.VectorUtil.Direction;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class GUIText {
    private String text;
    private float fontSize;
    private int textMeshVAO;
    private int vertexCount;
    private Vector3f colour;
    private Vector2f position;
    private float maxLineLength;
    private int numLines;
    private FontType font;
    private boolean centered;

    public String text() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float fontSize() {
        return this.fontSize;
    }

    public int textMesh() {
        return this.textMeshVAO;
    }

    public int vertexCount() {
        return this.vertexCount;
    }

    public void setMeshInfo(int vao, int vertexCount) {
        this.textMeshVAO = vao;
        this.vertexCount = vertexCount;
    }

    public Vector3f colour() {
        return this.colour;
    }

    public void setColour(float r, float g, float b) {
        this.colour.set(r, g, b);
    }

    public Vector2f position() {
        return this.position;
    }

    public float maxLineLength() {
        return this.maxLineLength;
    }

    public int numLines() {
        return this.numLines;
    }

    public void setNumLines(int numLines) {
        this.numLines = numLines;
    }

    public FontType font() {
        return this.font;
    }

    public boolean isCentered() {
        return this.centered;
    }

    public GUIText(String text, FontType font, float fontSize, Vector2f position, float maxLineLength, boolean centered) {
        this.colour = Direction.ONE;
        this.centered = false;
        this.text = text;
        this.fontSize = fontSize;
        this.font = font;
        this.position = position;
        this.maxLineLength = maxLineLength;
        this.centered = centered;
        GUIManager.loadText(this);
    }

    public void update(String newStr) {
        GUIManager.updateText(this, newStr);
    }

    public void remove() {
        GUIManager.removeText(this);
    }
}
