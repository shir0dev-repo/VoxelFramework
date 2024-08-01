package com.shir0dev.voxelframework.core.display.debug;

import com.shir0dev.voxelframework.core.display.GLHandler;
import com.shir0dev.voxelframework.core.display.gui.text.GUIText;
import com.shir0dev.voxelframework.core.display.gui.text.font.FontType;
import com.shir0dev.voxelframework.core.display.gui.text.mesh.TextMeshData;
import com.shir0dev.voxelframework.core.display.render.Camera;
import com.shir0dev.voxelframework.core.display.render.TextRenderer;
import com.shir0dev.voxelframework.core.transform.Transform;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;

public class DebugScreen {
    private static boolean isActive = false;
    private static GLHandler glHandler;
    private static TextRenderer renderer;
    private static FontType font;
    private static final Map<GUIText, Supplier<String>> supplierMap = new HashMap<>();
    private static final Map<FontType, List<GUIText>> entries = new HashMap<>();

    public static void toggle() {
        isActive = !isActive;
    }

    public static void init(GLHandler glHandler) {
        DebugScreen.glHandler = glHandler;
        renderer = new TextRenderer();
        font = new FontType(DebugScreen.glHandler.loadTexture("res/lang/font/Voxl.png"), new File("res/lang/font/Voxl.fnt"));
        Transform cameraTransform = Camera.getInstance().transform();
        Supplier<String> supp = cameraTransform::toString;
        GUIText camText = new GUIText(supp.get(), font, 1.0F, new Vector2f(0.01F, 0.005F), 0.5F, false);
        camText.setColour(1.0F, 0.0F, 1.0F);
        addEntry(camText, supp);
    }

    public static void render() {
        if (isActive) {
            updateEntries();
            renderer.render(entries);
        }
    }

    public static void addEntry(GUIText text, Supplier<String> supplier) {
        supplierMap.put(text, supplier);
        entries.computeIfAbsent(font, _ -> new ArrayList<>());
        entries.get(font).add(text);
    }

    public static void updateEntries() {
        for (var kvp : supplierMap.entrySet()) {
            Supplier<String> str = kvp.getValue();
            GUIText text = kvp.getKey();

            entries.get(font)
                    .stream()
                    .filter(t -> Objects.equals(t, text))
                    .findFirst().ifPresent(t -> {
                        t.setText(str.get());
                        TextMeshData data = font.loadText(t);
                        t.setMeshInfo(text.textMesh(), data.vertexCount());
                        GL30.glBindVertexArray(text.textMesh());
                        glHandler.updateAttributeData(text.textMesh(), 0, 2, data.vertices());
                        glHandler.updateAttributeData(text.textMesh(), 1, 2, data.uvs());
                        GL30.glBindVertexArray(0);
                    });
        }
    }
}
