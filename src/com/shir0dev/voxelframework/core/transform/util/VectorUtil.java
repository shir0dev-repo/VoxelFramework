package com.shir0dev.voxelframework.core.transform.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public final class VectorUtil {
    @Contract(pure = true)
    public static Vector3f project(@NotNull Vector3f a, @NotNull Vector3f b) {
        float dot = Vector3f.dot(a, b);
        float lSqr = b.lengthSquared();
        float scalar = dot / lSqr;
        return scale(b, scalar, null);
    }

    @Contract(pure = true)
    public static Vector4f project(@NotNull Vector4f a, Vector4f b) {
        float dot = Vector4f.dot(a, b);
        float lSqr = b.lengthSquared();
        float scalar = dot / lSqr;
        return scale(b, scalar, null);
    }

    @Contract(pure = true)
    public static Vector3f scale(@NotNull Vector3f v, float scalar, @Nullable Vector3f dest) {
        if (dest == null) dest = new Vector3f();

        dest.set(v.x * scalar, v.y * scalar, v.z * scalar);
        return dest;
    }

    @Contract(pure = true)
    public static Vector4f scale(@NotNull Vector4f v, float scalar, @Nullable Vector4f dest) {
        if (dest == null) dest = new Vector4f();

        dest.set(v.x * scalar, v.y * scalar, v.z * scalar, v.w * scalar);
        return dest;
    }

    @Contract(pure = true)
    public static Vector4f homogenize(@NotNull Vector3f v, @Nullable Vector4f dest) {
        if (dest == null) dest = new Vector4f();


        dest.set(v.x, v.y, v.z, 1.0F);
        return dest;
    }

    @Contract(pure = true)
    public static Vector3f dehomogenize(@NotNull Vector4f v, @Nullable Vector3f dest, boolean normalise) {
        if (normalise && Math.abs(v.lengthSquared()) - 1.0F > 0.001F)
            v.normalise();

        if (dest == null) dest = new Vector3f();

        dest.set(v.x, v.y, v.z);
        return dest;
    }

    public static Vector3f lerp(@NotNull Vector3f a, @NotNull Vector3f b, float value01, @Nullable Vector3f dest) {
        if (dest == null) dest = new Vector3f();


        value01 = Math.clamp(value01, 0.0F, 1.0F);
        Vector3f.add(scale(a, value01, null), scale(b, 1.0F - value01, null), dest);
        return dest;
    }

    public static final class Direction {
        public static final Vector3f RIGHT = new Vector3f(1.0F, 0.0F, 0.0F);
        public static final Vector3f UP = new Vector3f(0.0F, 1.0F, 0.0F);
        public static final Vector3f FORWARD = new Vector3f(0.0F, 0.0F, 1.0F);
        public static final Vector3f ZERO = new Vector3f(0.0F, 0.0F, 0.0F);
        public static final Vector3f ONE = new Vector3f(1.0F, 1.0F, 1.0F);
    }
}
