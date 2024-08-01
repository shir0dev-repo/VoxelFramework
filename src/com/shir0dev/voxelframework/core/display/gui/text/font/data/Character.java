package com.shir0dev.voxelframework.core.display.gui.text.font.data;

public class Character {
    int id;
    double texCoordX;
    double texCoordY;
    double maxTexCoordX;
    double maxTexCoordY;
    double xOffset;
    double yOffset;
    double sizeX;
    double sizeY;
    double xAdvance;

    public int id() {
        return this.id;
    }

    public double texCoordX() {
        return this.texCoordX;
    }

    public double texCoordY() {
        return this.texCoordY;
    }

    public double maxTexCoordX() {
        return this.maxTexCoordX;
    }

    public double maxTexCoordY() {
        return this.maxTexCoordY;
    }

    public double xOffset() {
        return this.xOffset;
    }

    public double yOffset() {
        return this.yOffset;
    }

    public double sizeX() {
        return this.sizeX;
    }

    public double sizeY() {
        return this.sizeY;
    }

    public double xAdvance() {
        return this.xAdvance;
    }

    public Character(int id, double texCoordX, double texCoordY, double xTexSize, double yTexSize, double xOffset, double yOffset, double sizeX, double sizeY, double xAdvance) {
        this.id = id;
        this.texCoordX = texCoordX;
        this.texCoordY = texCoordY;
        this.maxTexCoordX = xTexSize + texCoordX;
        this.maxTexCoordY = yTexSize + texCoordY;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.xAdvance = xAdvance;
    }
}
