package com.shir0dev.voxelframework.core.display.render;

import com.shir0dev.voxelframework.core.display.gui.text.GUIText;
import com.shir0dev.voxelframework.core.display.gui.text.font.FontType;
import com.shir0dev.voxelframework.core.display.shader.shaders.TextShader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class TextRenderer {
    private TextShader textShader = new TextShader();

    public void dealloc() {
    }

    private void prepare() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        this.textShader.start();
    }

    public void render(Map<FontType, List<GUIText>> texts) {
        this.prepare();
        for (FontType font : texts.keySet()) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.textureAtlas());

            for (GUIText text : texts.get(font)) {
                renderText(text);
            }

        }
        this.endRender();
    }

    private void glToggleGUIMesh(boolean enabled, int guiMeshID) {
        if (enabled) {
            GL30.glBindVertexArray(guiMeshID);
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
        } else {
            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL30.glBindVertexArray(0);
        }

    }

    private void renderText(GUIText text) {
        this.glToggleGUIMesh(true, text.textMesh());
        this.textShader.loadColour(text.colour());
        this.textShader.loadTranslation(text.position());
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.vertexCount());
        this.glToggleGUIMesh(false, 0);
    }

    private void endRender() {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
}
