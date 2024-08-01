package com.shir0dev.voxelframework.core.display.shader.shaders;

import com.shir0dev.voxelframework.core.display.render.Camera;
import com.shir0dev.voxelframework.core.display.shader.ShaderProgram;
import com.shir0dev.voxelframework.core.display.shader.ShaderUtil;
import org.lwjgl.util.vector.Matrix4f;

public class SimpleUnlit extends ShaderProgram {
    private int loc_transformationMatrix;
    private int loc_viewMatrix;

    @Override
    protected void bindAttributes() {
        super.bindAttribute("position", 0);
        super.bindAttribute("uv", 1);
        super.bindAttribute("normal", 2);
    }
    @Override
    protected void getUniformLocations() {
        start();
        ShaderUtil.loadMatrix4f(super.getUniformLocation("projectionMatrix"), Camera.PROJECTION_MATRIX);
        stop();

        loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
        loc_viewMatrix = super.getUniformLocation("viewMatrix");
    }
    @Override
    public void load(Matrix4f transformationMatrix) {
        ShaderUtil.loadMatrix4f(loc_transformationMatrix, transformationMatrix);
        ShaderUtil.loadMatrix4f(loc_viewMatrix, Camera.VIEW_MATRIX);
    }
}
