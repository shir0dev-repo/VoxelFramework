package com.shir0dev.voxelframework.core.display.gui.text.font.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.Display;

public class MetaFile {
    private static final int IDX_PAD_TOP = 0;
    private static final int IDX_PAD_LEFT = 1;
    private static final int IDX_PAD_BOTTOM = 2;
    private static final int IDX_PAD_RIGHT = 3;
    private static final int DESIRED_PADDING = 3;
    private static final String SPLITTER = " ";
    private static final String NUMBER_SEPARATOR = ",";
    private double aspectRatio = (double)Display.getWidth() / (double)Display.getHeight();
    private double horizontalPerPixelSize;
    private double verticalPerPixelSize;
    private double spaceWidth;
    private int[] padding;
    private int paddingWidth;
    private int paddingHeight;
    private Map<Integer, Character> metaData = new HashMap();
    private BufferedReader reader;
    private Map<String, String> values = new HashMap();

    public double spaceWidth() {
        return this.spaceWidth;
    }

    public Character getCharacter(int ascii) {
        return (Character)this.metaData.get(ascii);
    }

    public MetaFile(File file) {
        this.openFile(file);
        this.loadPaddingData();
        this.loadLineSizes();
        int imageWidth = this.getValueOfVariable("scaleW");
        this.loadCharacterData(imageWidth);
        this.closeFile();
    }

    private boolean processNextLine() {
        this.values.clear();
        String line = null;

        try {
            line = this.reader.readLine();
        } catch (IOException var7) {
        }

        if (line == null) {
            return false;
        } else {
            String[] var2 = line.split(" ");
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String part = var2[var4];
                String[] valuePairs = part.split("=");
                if (valuePairs.length == 2) {
                    this.values.put(valuePairs[0], valuePairs[1]);
                }
            }

            return true;
        }
    }

    private int getValueOfVariable(String variable) {
        return Integer.parseInt((String)this.values.get(variable));
    }

    private int[] getValuesOfVariable(String variable) {
        String[] numbers = ((String)this.values.get(variable)).split(",");
        int[] values = new int[numbers.length];

        for(int i = 0; i < values.length; ++i) {
            values[i] = Integer.parseInt(numbers[i]);
        }

        return values;
    }

    private void openFile(File file) {
        try {
            this.reader = new BufferedReader(new FileReader(file));
        } catch (Exception var3) {
            System.err.println("Could not read font meta file!\n" + String.valueOf(var3));
        }

    }

    private void closeFile() {
        try {
            this.reader.close();
        } catch (IOException var2) {
            System.err.println("Failed to close file reader!\n" + String.valueOf(var2));
        }

    }

    private void loadPaddingData() {
        this.processNextLine();
        this.padding = this.getValuesOfVariable("padding");
        this.paddingWidth = this.padding[1] + this.padding[3];
        this.paddingHeight = this.padding[0] + this.padding[2];
    }

    private void loadLineSizes() {
        this.processNextLine();
        int lineHeightPixels = this.getValueOfVariable("lineHeight") - this.paddingHeight;
        this.verticalPerPixelSize = 0.029999999329447746 / (double)lineHeightPixels;
        this.horizontalPerPixelSize = this.verticalPerPixelSize / this.aspectRatio;
    }

    private void loadCharacterData(int imageWidth) {
        this.processNextLine();
        this.processNextLine();

        while(this.processNextLine()) {
            Character c = this.loadCharacter(imageWidth);
            if (c != null) {
                this.metaData.put(c.id(), c);
            }
        }

    }

    private Character loadCharacter(int imageSize) {
        int id = this.getValueOfVariable("id");
        if (id == 32) {
            this.spaceWidth = (double)(this.getValueOfVariable("xadvance") - this.paddingWidth) * this.horizontalPerPixelSize;
            return null;
        } else {
            double xTex = ((double)this.getValueOfVariable("x") + (double)(this.padding[1] - 3)) / (double)imageSize;
            double yTex = ((double)this.getValueOfVariable("y") + (double)(this.padding[0] - 3)) / (double)imageSize;
            int width = this.getValueOfVariable("width") - (this.paddingWidth - 6);
            int height = this.getValueOfVariable("height") - (this.paddingHeight - 6);
            double quadWidth = (double)width * this.horizontalPerPixelSize;
            double quadHeight = (double)height * this.verticalPerPixelSize;
            double xTexSize = (double)width / (double)imageSize;
            double yTexSize = (double)height / (double)imageSize;
            double xOff = (double)(this.getValueOfVariable("xoffset") + this.padding[1] - 3) * this.horizontalPerPixelSize;
            double yOff = (double)(this.getValueOfVariable("yoffset") + this.padding[0] - 3) * this.verticalPerPixelSize;
            double xAdv = (double)(this.getValueOfVariable("xadvance") - this.paddingWidth) * this.horizontalPerPixelSize;
            return new Character(id, xTex, yTex, xTexSize, yTexSize, xOff, yOff, quadWidth, quadHeight, xAdv);
        }
    }
}

