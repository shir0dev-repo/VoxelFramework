package com.shir0dev.voxelframework.core.transform.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

public final class MatrixUtil {
    @Contract(pure = true)
    public static @NotNull Vector3f getPosition(@NotNull Matrix4f src) {
        return new Vector3f(src.m30, src.m31, src.m32);
    }

    public static void setPosition(@NotNull Matrix4f src, @NotNull Vector3f position) {
        Matrix4f m = new Matrix4f(src);
        m.m30 = position.x;
        m.m31 = position.y;
        m.m32 = position.z;
        src.load(m);
    }

    @Contract(pure = true)
    public static @NotNull Quaternion getRotation(@NotNull Matrix4f src) {
        Quaternion ret = new Quaternion();
        float trace;

        if (src.m22 < 0.0F) {
            if (src.m00 > src.m11) {
                trace = 1.0F + src.m00 - src.m11 - src.m22;
                ret.set(trace, src.m10 + src.m01, src.m02 + src.m20, src.m12 - src.m21);
            } else {
                trace = 1.0F - src.m00 + src.m11 - src.m22;
                ret.set(src.m10 + src.m01, trace, src.m21 + src.m12, src.m20 - src.m02);
            }
        } else if (src.m00 < -src.m11) {
            trace = 1.0F - src.m00 - src.m11 + src.m22;
            ret.set(src.m02 + src.m20, src.m21 + src.m12, trace, src.m10 - src.m01);
        } else {
            trace = 1.0F + src.m00 + src.m11 + src.m22;
            ret.set(src.m12 - src.m21, src.m20 - src.m02, src.m01 - src.m10, trace);
        }

        float n = 0.5F / (float)Math.sqrt(trace);
        ret.w *= n;
        ret.x *= n;
        ret.y *= n;
        ret.z *= n;

        ret.normalise();
        return ret;
    }

    public static void setRotation(@NotNull Matrix4f src, @NotNull Quaternion rotation) {
        Vector3f position = getPosition(src);

        src.setIdentity();
        rotate(src, rotation, src);
        orthonormalize(src, src);

        translate(src, position, src);
    }

    @Contract(pure = true)
    public static @NotNull Vector3f getScale(@NotNull Matrix4f src) {
        return new Vector3f(
                (float) Math.sqrt(src.m00 * src.m00 + src.m10 * src.m10 + src.m20 * src.m20),
                (float) Math.sqrt(src.m01 * src.m01 + src.m11 * src.m11 + src.m21 * src.m21),
                (float) Math.sqrt(src.m02 * src.m02 + src.m12 * src.m12 + src.m22 * src.m22)
        );
    }

    public static void setScale(@NotNull Matrix4f src, @NotNull Vector3f scale) {
        Vector3f position = getPosition(src);
        Quaternion rotation = getRotation(src);
        src.setIdentity();
        src.scale(scale);
        rotate(src, rotation, src);
        translate(src, position, src);
    }

    public static @NotNull Matrix4f translate(@NotNull Matrix4f src, @NotNull Vector3f translation, @Nullable Matrix4f dest) {
        if (dest == null) dest = new Matrix4f(src);

        dest.m30 += translation.x;
        dest.m31 += translation.y;
        dest.m32 += translation.z;

        return dest;
    }

    public static @NotNull Matrix4f rotate(@NotNull Matrix4f src, @NotNull Matrix4f rotationMatrix, @Nullable Matrix4f dest) {
        Vector3f position = getPosition(src);
        Matrix4f rotationSrc = new Matrix4f(src);
        setPosition(rotationSrc, new Vector3f(0.0F, 0.0F, 0.0F));

        if (dest == null) dest = new Matrix4f();

        Matrix4f.mul(rotationMatrix, rotationSrc, dest);
        setPosition(dest, position);
        orthonormalize(dest, dest);

        return dest;
    }

    public static @NotNull Matrix4f rotate(@NotNull Matrix4f src, @NotNull Quaternion rotation, @Nullable Matrix4f dest) {
        Quaternion currentRotation = getRotation(src);
        if (Math.abs(rotation.lengthSquared() - 1.0F) > 0.001F) {
            rotation.normalise();
        }

        Quaternion result = new Quaternion();
        Quaternion.mul(rotation, currentRotation, result);
        result.normalise();
        Matrix4f rotationMatrix = QuaternionUtil.toMatrix4(result);
        orthonormalize(rotationMatrix, rotationMatrix);

        if (dest == null) dest = new Matrix4f();


        dest.load(rotationMatrix);
        setPosition(dest, getPosition(src));

        return dest;
    }

    public static @NotNull Matrix4f scale(@NotNull Matrix4f src, @NotNull Vector3f scale, @Nullable Matrix4f dest) {
        return src;
    }

    @Contract(pure = true)
    public static @NotNull Matrix4f orthonormalize(@NotNull Matrix4f src, @Nullable Matrix4f dest) {
        Vector3f position = getPosition(src);
        Matrix3f rm = getRotationMatrix(src, true);

        Vector3f basisX = new Vector3f(rm.m00, rm.m01, rm.m02);
        Vector3f basisY = new Vector3f(rm.m10, rm.m11, rm.m12);
        Vector3f basisZ = new Vector3f(rm.m20, rm.m21, rm.m22);

        Vector3f u1 = basisX.normalise(null);

        Vector3f w2 = Vector3f.sub(basisY, VectorUtil.project(basisY, basisX),null);
        Vector3f u2 = w2.normalise(null);

        Vector3f w3 = Vector3f.sub(basisZ, VectorUtil.project(basisZ, basisX),null);
        w3 = Vector3f.sub(w3, VectorUtil.project(basisZ, w2),null);
        Vector3f u3 = w3.normalise(null);

        if (dest == null)  dest = new Matrix4f();

        dest.m00 = u1.x; dest.m10 = u2.x; dest.m20 = u3.x; dest.m30 = position.x;
        dest.m01 = u1.y; dest.m11 = u2.y; dest.m21 = u3.y; dest.m31 = position.y;
        dest.m02 = u1.z; dest.m12 = u2.z; dest.m22 = u3.z; dest.m32 = position.z;
        dest.m03 = 0.0F; dest.m13 = 0.0F; dest.m23 = 0.0F; dest.m33 = 1.0F;

        return dest;
    }

    @Contract(pure = true)
    public static @NotNull Matrix3f getRotationMatrix(@NotNull Matrix4f src, boolean removeScale) {
        Matrix3f m = new Matrix3f();
        if (removeScale) {
            Vector3f scale = getScale(src);
            m.m00 = src.m00 / scale.x;
            m.m10 = src.m10 / scale.y;
            m.m20 = src.m20 / scale.z;
            m.m01 = src.m01 / scale.x;
            m.m11 = src.m11 / scale.y;
            m.m21 = src.m21 / scale.z;
            m.m02 = src.m02 / scale.x;
            m.m12 = src.m12 / scale.y;
            m.m22 = src.m22 / scale.z;
        } else {
            m.m00 = src.m00;
            m.m01 = src.m01;
            m.m02 = src.m02;
            m.m10 = src.m10;
            m.m11 = src.m11;
            m.m12 = src.m12;
            m.m20 = src.m20;
            m.m21 = src.m21;
            m.m22 = src.m22;
        }
        return m;
    }
}
