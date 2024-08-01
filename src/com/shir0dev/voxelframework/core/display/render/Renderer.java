package com.shir0dev.voxelframework.core.display.render;

import com.shir0dev.voxelframework.core.display.mesh.Mesh;
import com.shir0dev.voxelframework.core.display.mesh.StaticMesh;
import com.shir0dev.voxelframework.core.transform.Transform;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {
    private final Transform transform;
    private final StaticMesh mesh;
    private final Material material;

    public Transform transform() {
        return this.transform;
    }

    public StaticMesh mesh() {
        return this.mesh;
    }

    public Material material() {
        return this.material;
    }

    public Renderer(Transform transform, StaticMesh mesh, Material material) {
        this.transform = transform;
        this.mesh = mesh;
        this.material = material;
    }

    private void glToggleMesh(boolean enabled) {
        if (enabled) {
            GL30.glBindVertexArray(this.mesh.vaoID());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL20.glEnableVertexAttribArray(2);
        } else {
            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL20.glDisableVertexAttribArray(2);
            GL30.glBindVertexArray(0);
        }

    }

    public void render() {
        this.glToggleMesh(true);
        this.material.draw(this);
        this.glToggleMesh(false);
    }
}
