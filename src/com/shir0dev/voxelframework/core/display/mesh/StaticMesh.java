package com.shir0dev.voxelframework.core.display.mesh;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;

public class StaticMesh extends Mesh {
    protected int triangleBufferID;
    protected int normalBufferID;
    protected int[] triangles;
    public StaticMesh(float[] vertices, int[] triangles, float[] uvs, float[] normals) {
        super(vertices, uvs, 3, false);
        bindVAO(this.vaoID);

        this.triangles = triangles;
        this.triangleBufferID = createIndicesVBO(triangles);

        this.normalBufferID = createVBO();
        storeAttribute(this.normalBufferID, 2, 3, normals, false);

        bindVAO(0);
    }

    public int[] triangles() { return this.triangles; }

    public int triangleVBO() { return this.triangleBufferID; }
    public int normalVBO() { return this.normalBufferID; }

    protected final int createIndicesVBO(int[] indices) {
        int vboID = createVBO();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        bindVBO(0);

        return vboID;
    }

    @Override
    public void dispose() {
        GL15.glDeleteBuffers(verticesBufferID);
        GL15.glDeleteBuffers(texCoordBufferID);
        GL15.glDeleteBuffers(triangleBufferID);
        GL15.glDeleteBuffers(normalBufferID);

        GL30.glDeleteVertexArrays(vaoID);
    }
}
