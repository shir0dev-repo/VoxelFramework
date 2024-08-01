package com.shir0dev.voxelframework.core.display.gui.text.font;

import com.shir0dev.voxelframework.core.display.gui.text.GUIText;
import com.shir0dev.voxelframework.core.display.gui.text.mesh.TextMeshCreator;
import com.shir0dev.voxelframework.core.display.gui.text.mesh.TextMeshData;
import java.io.File;

public class FontType {
    private int textureAtlas;
    private TextMeshCreator loader;

    public int textureAtlas() {
        return this.textureAtlas;
    }

    public FontType(int textureAtlas, File fontFile) {
        this.textureAtlas = textureAtlas;
        this.loader = new TextMeshCreator(fontFile);
    }

    public TextMeshData loadText(GUIText text) {
        return this.loader.createTextMesh(text);
    }
}
