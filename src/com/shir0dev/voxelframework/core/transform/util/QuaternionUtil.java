package com.shir0dev.voxelframework.core.transform.util;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

public class QuaternionUtil {
    public static Matrix3f toMatrix3(@NotNull Quaternion q) {
        if (Math.abs(q.lengthSquared() - 1.0F) > 0.001F) {
            q.normalise(q);
        }

        float xx = q.x * q.x;
        float xy = q.x * q.y;
        float xz = q.x * q.z;
        float xw = q.x * q.w;
        float yy = q.y * q.y;
        float yz = q.y * q.z;
        float yw = q.y * q.w;
        float zz = q.z * q.z;
        float zw = q.z * q.w;

        Matrix3f m = new Matrix3f();

        m.m00 = 1.0F - 2.0F * (yy + zz);
        m.m01 = 2.0F * (xy - zw);
        m.m02 = 2.0F * (xz + yw);
        m.m10 = 2.0F * (xy + zw);
        m.m11 = 1.0F - 2.0F * (xx + zz);
        m.m12 = 2.0F * (yz - xw);
        m.m20 = 2.0F * (xz - yw);
        m.m21 = 2.0F * (yz + xw);
        m.m22 = 1.0F - 2.0F * (xx + yy);
        return m;
    }

    public static @NotNull Matrix4f toMatrix4(@NotNull Quaternion q) {
        if (Math.abs(q.lengthSquared() - 1.0F) > 0.001F) {
            q.normalise();
        }

        float xx = q.x * q.x;
        float xy = q.x * q.y;
        float xz = q.x * q.z;
        float xw = q.x * q.w;
        float yy = q.y * q.y;
        float yz = q.y * q.z;
        float yw = q.y * q.w;
        float zz = q.z * q.z;
        float zw = q.z * q.w;

        Matrix4f m = new Matrix4f();

        m.m00 = 1.0F - 2.0F * (yy - zz);
        m.m10 = 2.0F * (xy + zw);
        m.m20 = 2.0F * (xz - yw);
        m.m30 = 0.0F;

        m.m01 = 2.0F * (xy - zw);
        m.m11 = 1.0F - 2.0F * (xx - zz);
        m.m21 = 2.0F * (yz + xw);
        m.m31 = 0.0F;

        m.m02 = 2.0F * (xz + yw);
        m.m12 = 2.0F * (yz - xw);
        m.m22 = 1.0F - 2.0F * (xx - yy);
        m.m32 = 0.0F;

        m.m03 = 0.0F;
        m.m13 = 0.0F;
        m.m23 = 0.0F;
        m.m33 = 1.0F;

        return m;
    }

    public static Quaternion axisAngle(@NotNull Vector3f axis, float angleDegrees) {
        Quaternion q = new Quaternion();
        q.setIdentity();

        float angleRadians = (float)Math.toRadians(angleDegrees);
        q.x = axis.x * (float)Math.sin(angleRadians / 2.0F);
        q.y = axis.y * (float)Math.sin(angleRadians / 2.0F);
        q.z = axis.z * (float)Math.sin(angleRadians / 2.0F);
        q.w = (float)Math.cos(angleRadians / 2.0F);
        q.normalise();
        return q;
    }

    public static Quaternion euler(@NotNull Vector3f eulers) {
        double cx = Math.cos(Math.toRadians(eulers.x) / 2.0);
        double cy = Math.cos(Math.toRadians(eulers.y) / 2.0);
        double cz = Math.cos(Math.toRadians(eulers.z) / 2.0);
        double sx = Math.sin(Math.toRadians(eulers.x) / 2.0);
        double sy = Math.sin(Math.toRadians(eulers.y) / 2.0);
        double sz = Math.sin(Math.toRadians(eulers.z) / 2.0);

        double w = cx * cy * cz - sx * sy * sz;
        double x = sx * cy * cz + cx * sy * sz;
        double y = cx * sy * cz - sx * cy * sz;
        double z = cx * cy * sz + sx * sy * cz;
        Quaternion q = new Quaternion((float)x, (float)y, (float)z, (float)w);
        q.normalise();
        return q;
    }

    public static Vector3f eulerAngles(@NotNull Quaternion q) {
        Vector3f eulers = new Vector3f();
        if (Math.abs(q.lengthSquared() - 1.0F) > 0.001F) {
            q.normalise();
        }

        eulers.z = (float)Math.toDegrees(Math.atan2(2.0 * (double)(q.w * q.z + q.x * q.y), 1.0 - 2.0 * (double)(q.z * q.z + q.x * q.x)));
        eulers.x = (float)Math.toDegrees(Math.asin(2.0 * (double)(q.w * q.x - q.z * q.y)));
        eulers.y = (float)Math.toDegrees(Math.atan2(2.0 * (double)(q.w * q.y + q.z * q.x), 1.0 - 2.0 * (double)(q.x * q.x + q.y * q.y)));
        return eulers;
    }
}
