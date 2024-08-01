package com.shir0dev.voxelframework.core.display;

import com.shir0dev.voxelframework.core.display.mesh.Mesh;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shir0dev.voxelframework.core.display.mesh.StaticMesh;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public final class GLHandler {
    private static final Map<Integer, List<Integer>> vertexArrayBufferMap = new HashMap<>();
    private static final List<Integer> textures = new ArrayList<>();

    public GLHandler() {
    }

    public int loadTexture(String filePath) {
        Texture texture = null;

        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream(filePath));
        } catch (IOException var4) {
            System.err.println(STR."Could not read texture \{filePath}");
        }

        assert texture != null;

        int textureID = texture.getTextureID();
        textures.add(textureID);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        return textureID;
    }

    public int loadToVAO(float[] vertices, float[] uvs, boolean dynamic) {
        int vaoID = this.bindVAO();
        this.storeAttributeData(vaoID, 0, 2, vertices, dynamic);
        this.storeAttributeData(vaoID, 1, 2, uvs, dynamic);
        GL30.glBindVertexArray(0);
        return vaoID;
    }

    public StaticMesh loadToVAO(float[] vertices, float[] uvs, int[] triangles, float[] normals, boolean dynamic) {
        return new StaticMesh(vertices, triangles, uvs, normals);
    }

    private int bindVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vertexArrayBufferMap.computeIfAbsent(vaoID, _ -> new ArrayList<>());

        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void bindVBO(int currentVAO, int[] indices) {
        int vboID = GL15.glGenBuffers();
        vertexArrayBufferMap.get(currentVAO).add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private void storeAttributeData(int currentVAO, int attributeID, int coordinateSize, float[] attributeData, boolean dynamic) {
        int vboID = GL15.glGenBuffers();
        vertexArrayBufferMap.get(currentVAO).add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(attributeData.length);
        buffer.put(attributeData);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, dynamic ? GL15.GL_DYNAMIC_DRAW : GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeID, coordinateSize, 5126, false, 0, 0L);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void updateAttributeData(int vaoID, int attributeID, int coordinateSize, float[] attributeData) {
        int vboID = vertexArrayBufferMap.get(vaoID).get(attributeID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        int bufferSize = GL15.glGetBufferParameteri(GL15.GL_ARRAY_BUFFER, GL15.GL_BUFFER_SIZE);
        int newBufferSize = attributeData.length * Float.BYTES;
        if (newBufferSize > bufferSize) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            GL15.glDeleteBuffers(vboID);
            int newVboID = GL15.glGenBuffers();
            vertexArrayBufferMap.get(vaoID).set(attributeID, newVboID);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, newVboID);
            FloatBuffer newBuffer = BufferUtils.createFloatBuffer(attributeData.length);
            newBuffer.put(attributeData);
            newBuffer.flip();
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (long)newBufferSize, GL15.GL_DYNAMIC_DRAW);
            GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0L, newBuffer);
            GL20.glVertexAttribPointer(attributeID, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        } else {
            FloatBuffer newBuffer = BufferUtils.createFloatBuffer(attributeData.length);
            newBuffer.put(attributeData).flip();
            GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, newBuffer);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }

    }

    public void dealloc() {
        vertexArrayBufferMap.forEach((key, value) -> {
            value.forEach(GL15::glDeleteBuffers);
            GL30.glDeleteVertexArrays(key);
        });
        vertexArrayBufferMap.clear();
        textures.forEach(GL11::glDeleteTextures);
    }
}
