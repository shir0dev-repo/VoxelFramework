package com.shir0dev.voxelframework.core.display.gui.text;

import com.shir0dev.voxelframework.core.display.GLHandler;
import com.shir0dev.voxelframework.core.display.gui.text.font.FontType;
import com.shir0dev.voxelframework.core.display.gui.text.mesh.TextMeshData;
import com.shir0dev.voxelframework.core.display.render.TextRenderer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.lwjgl.opengl.GL30;

public final class GUIManager {
    private static GLHandler glHandler;
    private static Map<FontType, List<GUIText>> texts = new HashMap();
    private static TextRenderer renderer;

    public GUIManager() {
    }

    public static void init(GLHandler glHandler) {
        GUIManager.glHandler = glHandler;
        renderer = new TextRenderer();
    }

    public static void render() {
        renderer.render(texts);
    }

    public static void loadText(GUIText text) {
        FontType font = text.font();
        TextMeshData data = font.loadText(text);
        int vao = glHandler.loadToVAO(data.vertices(), data.uvs(), true);
        text.setMeshInfo(vao, data.vertexCount());
        List<GUIText> textBatch = texts.computeIfAbsent(font, k -> new ArrayList<>());
        textBatch.add(text);
    }

    public static void updateText(GUIText text, String newStr) {
        (texts.get(text.font())).stream().filter(t -> Objects.equals(t, text))
            .findFirst().ifPresent((t) -> {
                t.setText(newStr);
                FontType font = text.font();
                TextMeshData data = font.loadText(t);
                t.setMeshInfo(text.textMesh(), data.vertexCount());
                GL30.glBindVertexArray(text.textMesh());
                glHandler.updateAttributeData(text.textMesh(), 0, 2, data.vertices());
                glHandler.updateAttributeData(text.textMesh(), 1, 2, data.uvs());
                GL30.glBindVertexArray(0);
            });
    }

    public static void removeText(GUIText text) {
        List<GUIText> textBatch = texts.get(text.font());
        textBatch.remove(text);
        if (textBatch.isEmpty()) {
            texts.remove(textBatch);
        }

    }
}
