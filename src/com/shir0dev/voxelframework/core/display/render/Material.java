package com.shir0dev.voxelframework.core.display.render;

import com.shir0dev.voxelframework.core.display.shader.ShaderProgram;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Material {
    private final ShaderProgram shader;
    private final int textureID;

    public Material(ShaderProgram shader, int textureID) {
        this.shader = shader;
        this.textureID = textureID;
    }

    public void draw(Renderer renderer) {
        this.shader.start();
        this.shader.load(renderer.transform().getTransformationMatrix());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textureID);
        GL11.glDrawElements(GL11.GL_TRIANGLES, renderer.mesh().triangles().length, GL11.GL_UNSIGNED_INT, 0L);
        this.shader.stop();
    }
}
