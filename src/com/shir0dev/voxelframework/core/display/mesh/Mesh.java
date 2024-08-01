package com.shir0dev.voxelframework.core.display.mesh;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

public abstract class Mesh {
        protected int vaoID;
        protected int verticesBufferID;
        protected int texCoordBufferID;

    public Mesh(float[] vertices, float[] texCoords, int vertexCoordinateSize, boolean isDynamic) {
        this.vaoID = createVAO();
        bindVAO(this.vaoID);

        this.verticesBufferID = createVBO();
        this.texCoordBufferID = createVBO();

        storeAttribute(verticesBufferID, 0, vertexCoordinateSize, vertices, isDynamic);
        storeAttribute(texCoordBufferID, 1, 2, texCoords, isDynamic);

        bindVAO(0);
    }

    public int vaoID() { return this.vaoID; }
    public int verticesBuffer() { return this.verticesBufferID; }
    public int texCoordBuffer() { return this.texCoordBufferID; }

    protected final int createVAO() { return GL30.glGenVertexArrays(); }
    protected final void bindVAO(int vaoID) { GL30.glBindVertexArray(vaoID); }
    protected final int createVBO() { return GL15.glGenBuffers(); }
    protected final void bindVBO(int vboID) { GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); }

    protected final void storeAttribute(int vboID, int attributeID, int coordinateSize, float[] attributeData, boolean isDynamicDraw) {
        bindVBO(vboID);

        FloatBuffer buffer = createFlippedBuffer(attributeData);

        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, isDynamicDraw ? GL15.GL_DYNAMIC_DRAW : GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeID, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        bindVBO(0);
    }

    protected final FloatBuffer createFlippedBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        return buffer.put(data).flip();
    }

    public abstract void dispose();
}
