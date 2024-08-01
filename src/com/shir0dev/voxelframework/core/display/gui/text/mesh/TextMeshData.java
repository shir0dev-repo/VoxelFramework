package com.shir0dev.voxelframework.core.display.gui.text.mesh;

public record TextMeshData(float[] vertices, float[] uvs) {
    public TextMeshData(float[] vertices, float[] uvs) {
        this.vertices = vertices;
        this.uvs = uvs;
    }

    public int vertexCount() {
        return this.vertices.length / 2;
    }

    public float[] vertices() {
        return this.vertices;
    }

    public float[] uvs() {
        return this.uvs;
    }
}
