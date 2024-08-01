package com.shir0dev.voxelframework.core.display.mesh;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

public class DynamicMesh extends Mesh {
    protected enum ATTRIB_TARGET { VERTICES, TEXCOORDS };

    public DynamicMesh(float[] vertices, float[] texCoords) {
        super(vertices, texCoords, 2, false);
    }

    protected void updateAttribute(int vboID, int attributeID, float[] newData, ATTRIB_TARGET target) {
        bindVBO(vboID);
        int currentBufferSize = GL15.glGetBufferParameteri(GL15.GL_ARRAY_BUFFER, GL15.GL_BUFFER_SIZE); // get size in bytes
        int newBufferSize = newData.length * Float.BYTES;

        if (newBufferSize <= currentBufferSize) { // clear old buffer and place new data
            FloatBuffer buffer = createFlippedBuffer(newData);
            GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
        } else { // discard old buffer and create new one, adjusting for greater size
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, replaceBuffer(target));

            FloatBuffer buffer = createFlippedBuffer(newData);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_DYNAMIC_DRAW);
            GL20.glVertexAttribPointer(attributeID, 2, GL11.GL_FLOAT, false, 0, 0);
        }

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private int replaceBuffer(ATTRIB_TARGET target) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        int vbo = createVBO();

        switch (target) {
            case VERTICES -> {
                GL15.glDeleteBuffers(this.verticesBufferID);
                this.verticesBufferID = vbo;
            }
            case TEXCOORDS -> {
                GL15.glDeleteBuffers(this.texCoordBufferID);
                this.texCoordBufferID = vbo;
            }
        }
        return vbo;
    }

    @Override
    public void dispose() {
        GL15.glDeleteBuffers(verticesBufferID);
        GL15.glDeleteBuffers(texCoordBufferID);

        GL30.glDeleteVertexArrays(vaoID);
    }
}
