package com.shir0dev.voxelframework.core.display.shader.shaders;

import com.shir0dev.voxelframework.core.display.shader.ShaderProgram;
import com.shir0dev.voxelframework.core.display.shader.ShaderUtil;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class TextShader extends ShaderProgram {
    private int loc_colour;
    private int loc_translation;

    public TextShader() {
    }

    protected void bindAttributes() {
        super.bindAttribute("position", 0);
        super.bindAttribute("textureCoords", 1);
    }

    protected void getUniformLocations() {
        this.loc_colour = super.getUniformLocation("colour");
        this.loc_translation = super.getUniformLocation("translation");
    }

    @Override
    public void load(Matrix4f transformationMatrix) { }
    public void loadColour(Vector3f colour) {
        ShaderUtil.loadVector3f(this.loc_colour, colour);
    }

    public void loadTranslation(Vector2f translation) {
        ShaderUtil.loadVector2f(this.loc_translation, translation);
    }
}
