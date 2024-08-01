package com.shir0dev.voxelframework.core.display.render;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public final class SpaceConversion {
    public SpaceConversion() {
    }

    public static Vector4f screenToClipPoint(Vector3f screenPosition, float depthWS) {
        float ndcX = (screenPosition.x / 1280.0F - 0.5F) * 2.0F;
        float ndcY = (screenPosition.y / 720.0F - 0.5F) * 2.0F;
        if (depthWS < 0.1F) {
            depthWS = 0.1F;
        }

        Vector4f depthPos = new Vector4f(0.0F, 0.0F, depthWS, -1.0F);
        Vector4f projDepth = new Vector4f();
        Matrix4f.transform(Camera.PROJECTION_MATRIX, depthPos, projDepth);
        float ndcZ = projDepth.z / projDepth.w;
        return new Vector4f(ndcX, ndcY, ndcZ, 1.0F);
    }

    public static Vector4f screenToViewPoint(Vector3f screenPosition, float depthWS) {
        Vector4f clipPosition = screenToClipPoint(screenPosition, depthWS);
        Vector4f viewPosition = new Vector4f();
        Matrix4f.transform(Camera.INVERSE_PROJECTION_MATRIX, clipPosition, viewPosition);
        if (viewPosition.w != 0.0F) {
            float n = 1.0F / viewPosition.w;
            viewPosition.x *= n;
            viewPosition.y *= n;
            viewPosition.z *= n;
            viewPosition.w = 1.0F;
        }

        return viewPosition;
    }

    public static Vector3f screenToWorldPoint(Vector3f screenPosition, float depthWS) {
        Vector4f viewPosition = screenToViewPoint(screenPosition, depthWS);
        Vector4f worldPosition4 = new Vector4f();
        Matrix4f.transform(Camera.INVERSE_VIEW_MATRIX, viewPosition, worldPosition4);
        return new Vector3f(worldPosition4.x, worldPosition4.y, worldPosition4.z);
    }

    public static Vector3f clipToScreenPoint(Vector4f clipPosition) {
        return new Vector3f(640.0F * (clipPosition.x + 1.0F), 360.0F * (clipPosition.y + 1.0F), (clipPosition.z + 1.0F) * 0.5F * 999.9F + 0.1F);
    }

    public static Vector4f clipToViewPoint(Vector4f clipPosition) {
        Vector4f viewPosition = new Vector4f();
        Matrix4f.transform(Camera.INVERSE_PROJECTION_MATRIX, clipPosition, viewPosition);
        if (viewPosition.w != 0.0F) {
            float n = 1.0F / viewPosition.w;
            viewPosition.x *= n;
            viewPosition.y *= n;
            viewPosition.z *= n;
            viewPosition.w = 1.0F;
        }

        return viewPosition;
    }

    public static Vector3f clipToWorldPoint(Vector4f clipPosition) {
        Vector4f viewPosition = clipToViewPoint(clipPosition);
        Vector4f worldPosition4 = new Vector4f();
        Matrix4f.transform(Camera.INVERSE_VIEW_MATRIX, viewPosition, worldPosition4);
        return new Vector3f(worldPosition4.x, worldPosition4.y, worldPosition4.z);
    }

    public static Vector3f viewToScreenPoint(Vector4f viewPosition) {
        Vector4f clipPosition = viewToClipPoint(viewPosition);
        return new Vector3f(640.0F * (clipPosition.x + 1.0F), 360.0F * (clipPosition.y + 1.0F), (clipPosition.z + 1.0F) * 0.5F * 999.9F + 0.1F);
    }

    public static Vector4f viewToClipPoint(Vector4f viewPosition) {
        Vector4f clipPosition = new Vector4f();
        Matrix4f.transform(Camera.PROJECTION_MATRIX, viewPosition, clipPosition);
        if (clipPosition.w != 0.0F) {
            float n = 1.0F / clipPosition.w;
            clipPosition.x *= n;
            clipPosition.y *= n;
            clipPosition.z *= n;
            clipPosition.w = 1.0F;
        }

        return clipPosition;
    }

    public static Vector3f viewToWorldPoint(Vector4f viewPosition) {
        Vector4f worldPosition4 = new Vector4f();
        Matrix4f.transform(Camera.INVERSE_VIEW_MATRIX, viewPosition, worldPosition4);
        return new Vector3f(worldPosition4.x, worldPosition4.y, worldPosition4.z);
    }

    public static Vector3f worldToScreenPoint(Vector3f worldPosition) {
        Vector4f clipPosition = worldToClipPoint(worldPosition);
        return new Vector3f(640.0F * (clipPosition.x + 1.0F), 360.0F * (clipPosition.y + 1.0F), (clipPosition.z + 1.0F) * 0.5F * 999.9F + 0.1F);
    }

    public static Vector4f worldToClipPoint(Vector3f worldPosition) {
        Vector4f viewPosition = worldToViewPoint(worldPosition);
        Vector4f clipPosition = new Vector4f();
        Matrix4f.transform(Camera.PROJECTION_MATRIX, viewPosition, clipPosition);
        if (clipPosition.w != 0.0F) {
            float n = 1.0F / clipPosition.w;
            clipPosition.x *= n;
            clipPosition.y *= n;
            clipPosition.z *= n;
            clipPosition.w = 1.0F;
        }

        return clipPosition;
    }

    public static Vector4f worldToViewPoint(Vector3f worldPosition) {
        Vector4f worldPosition4 = new Vector4f(worldPosition.x, worldPosition.y, worldPosition.z, 1.0F);
        Vector4f viewPosition = new Vector4f();
        Matrix4f.transform(Camera.VIEW_MATRIX, worldPosition4, viewPosition);
        if (viewPosition.w != 0.0F) {
            float n = 1.0F / viewPosition.w;
            viewPosition.x *= n;
            viewPosition.y *= n;
            viewPosition.z *= n;
            viewPosition.w = 1.0F;
        }

        return viewPosition;
    }
}
