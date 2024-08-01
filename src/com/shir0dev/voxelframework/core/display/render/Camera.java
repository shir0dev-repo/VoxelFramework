package com.shir0dev.voxelframework.core.display.render;

import com.shir0dev.voxelframework.core.transform.Transform;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public final class Camera {
    public static final float FOV = 60.0F;
    public static final float NEAR_PLANE = 0.1F;
    public static final float FAR_PLANE = 1000.0F;
    public static final Matrix4f PROJECTION_MATRIX = constructProjectionMatrix();
    public static final Matrix4f INVERSE_PROJECTION_MATRIX = constructInverseProjectionMatrix();
    public static Matrix4f VIEW_MATRIX = new Matrix4f();
    public static Matrix4f INVERSE_VIEW_MATRIX = new Matrix4f();
    private static Camera INSTANCE;
    private final Transform transform = new Transform();

    public static synchronized Camera getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Camera();
        }

        return INSTANCE;
    }

    public Transform transform() {
        return this.transform;
    }

    private Camera() {
    }

    public static void update() {
        VIEW_MATRIX = updateViewMatrix();
        INVERSE_VIEW_MATRIX = updateInverseViewMatrix();
    }

    private static Matrix4f updateViewMatrix() {
        Transform t = getInstance().transform();
        Matrix4f viewMatrix = new Matrix4f(t.getTransformationMatrix());
        viewMatrix.invert();
        return viewMatrix;
    }

    private static Matrix4f updateInverseViewMatrix() {
        return Matrix4f.invert(VIEW_MATRIX, (Matrix4f)null);
    }

    private static Matrix4f constructProjectionMatrix() {
        float aspect = 1.7777778F;
        float scaleY = 1.0F / (float)Math.tan(Math.toRadians(30.0));
        float scaleX = scaleY / aspect;
        float frustumLength = 999.9F;
        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = scaleX;
        projectionMatrix.m11 = scaleY;
        projectionMatrix.m22 = -(1000.1F / frustumLength);
        projectionMatrix.m23 = -1.0F;
        projectionMatrix.m32 = -(200.0F / frustumLength);
        projectionMatrix.m33 = 0.0F;
        return projectionMatrix;
    }

    private static Matrix4f constructInverseProjectionMatrix() {
        assert PROJECTION_MATRIX != null;

        Matrix4f invProjection = new Matrix4f();
        Matrix4f.invert(PROJECTION_MATRIX, invProjection);
        return invProjection;
    }

    public static Vector3f getMouseScreenPosition() {
        float x = (float)Mouse.getX();
        float y = (float)Mouse.getY();
        return new Vector3f(x, y, -1.0F);
    }

    public static Vector3f getMouseViewPosition() {
        float x = 2.0F * (float)Mouse.getX() / 1280.0F - 1.0F;
        float y = 2.0F * (float)Mouse.getY() / 720.0F - 1.0F;
        return new Vector3f(x, y, -1.0F);
    }

    public static Vector3f getMouseWorldPosition() {
        return getMouseWorldPosition(0.0F);
    }

    public static Vector3f getMouseWorldPosition(float depthWS) {
        return SpaceConversion.screenToWorldPoint(getMouseScreenPosition(), depthWS);
    }

    static {
        update();
        INSTANCE = null;
    }
}
