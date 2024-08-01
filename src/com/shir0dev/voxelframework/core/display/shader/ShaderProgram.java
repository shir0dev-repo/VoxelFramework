package com.shir0dev.voxelframework.core.display.shader;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

public abstract class ShaderProgram {
    private final int programID;
    private final int vertexID;
    private final int fragmentID;

    protected ShaderProgram() {
        String name = getClass().getSimpleName();
        this.vertexID = ShaderUtil.loadShader(name, GL20.GL_VERTEX_SHADER);
        this.fragmentID = ShaderUtil.loadShader(name, GL20.GL_FRAGMENT_SHADER);
        this.programID = GL20.glCreateProgram();

        GL20.glAttachShader(this.programID, this.vertexID);
        GL20.glAttachShader(this.programID, this.fragmentID);
        this.bindAttributes();

        GL20.glLinkProgram(this.programID);
        GL20.glValidateProgram(this.programID);
        this.getUniformLocations();
    }

    protected final void bindAttribute(String attributeName, int attribute) {
        GL20.glBindAttribLocation(this.programID, attribute, attributeName);
    }

    protected abstract void bindAttributes();

    protected final int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(this.programID, uniformName);
    }

    protected abstract void getUniformLocations();

    public final void start() {
        GL20.glUseProgram(programID);
    }
    public abstract void load(Matrix4f transformationMatrix);
    public final void stop() {
        GL20.glUseProgram(0);
    }
}
