package com.shir0dev.voxelframework.core.transform;

import com.shir0dev.voxelframework.core.transform.util.MatrixUtil;
import com.shir0dev.voxelframework.core.transform.util.QuaternionUtil;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

public class Transform {
    private final Matrix4f transformationMatrix = new Matrix4f();
    private final Vector3f localRight = new Vector3f();
    private final Vector3f localUp = new Vector3f();
    private final Vector3f localForward = new Vector3f();

    public @NotNull Matrix4f getTransformationMatrix() { return this.transformationMatrix; }

    public @NotNull Vector3f right() {
        localRight.set(transformationMatrix.m00, transformationMatrix.m01, transformationMatrix.m02);
        return localRight;
    }

    public @NotNull Vector3f up() {
        localUp.set(transformationMatrix.m01, transformationMatrix.m11, transformationMatrix.m21);
        return localUp;
    }

    public @NotNull Vector3f forward() {
        localForward.set(transformationMatrix.m20, transformationMatrix.m21, transformationMatrix.m22);
        return localForward;
    }

    public @NotNull Vector3f getPosition() {
        return MatrixUtil.getPosition(this.transformationMatrix);
    }

    public void setPosition(@NotNull Vector3f position) {
        MatrixUtil.setPosition(this.transformationMatrix, position);
    }

    public @NotNull Quaternion getRotation() {
        Quaternion var10000 = MatrixUtil.getRotation(this.transformationMatrix);

        return var10000;
    }

    public void setRotation(@NotNull Quaternion rotation) {


        MatrixUtil.setRotation(this.transformationMatrix, rotation);
    }

    public @NotNull Vector3f getRotationXYZ() {
        Vector3f var10000 = QuaternionUtil.eulerAngles(this.getRotation());


        return var10000;
    }

    public void setRotationXYZ(@NotNull Vector3f rotation) {


        this.setRotation(QuaternionUtil.euler(rotation));
    }

    public @NotNull Vector3f getScale() {
        Vector3f var10000 = MatrixUtil.getScale(this.transformationMatrix);


        return var10000;
    }

    public void setScale(@NotNull Vector3f scale) {


        MatrixUtil.setScale(this.transformationMatrix, scale);
    }

    public Transform() {
        this.setIdentity();
    }

    public void setIdentity() {
        this.transformationMatrix.setIdentity();
    }

    public void translate(Vector3f translation) {
        MatrixUtil.translate(this.transformationMatrix, translation, this.transformationMatrix);
    }

    public void rotate(@NotNull Matrix4f rotationMatrix) {


        MatrixUtil.rotate(this.transformationMatrix, rotationMatrix, this.transformationMatrix);
    }

    public void rotate(@NotNull Quaternion rotation) {


        MatrixUtil.rotate(this.transformationMatrix, rotation, this.transformationMatrix);
    }

    public void rotate(@NotNull Vector3f eulers) {


        this.rotate(QuaternionUtil.euler(eulers));
    }

    public void rotate(float xDegrees, float yDegrees, float zDegrees) {
        this.rotate(new Vector3f(xDegrees, yDegrees, zDegrees));
    }

    public void scale(@NotNull Vector3f scale) {


        MatrixUtil.scale(this.transformationMatrix, scale, this.transformationMatrix);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        DecimalFormat format = new DecimalFormat("0.0");
        format.setRoundingMode(RoundingMode.UP);
        Vector3f position = this.getPosition();
        sb.append("POS: [ ");
        sb.append(STR."X: \{format.format(position.x)}, ");
        sb.append(STR."Y: \{format.format(position.y)}, ");
        sb.append(STR."Z: \{format.format(position.z)} ");
        sb.append(" ]\n");

        Vector3f eulers = QuaternionUtil.eulerAngles(this.getRotation());
        sb.append("ROT: [ ");
        sb.append(STR."X: \{format.format(eulers.x)}, ");
        sb.append(STR."Y: \{format.format(eulers.y)}, ");
        sb.append(STR."Z: \{format.format(eulers.z)} ");
        sb.append(" ]\n");

        Vector3f scale = this.getScale();
        sb.append("SCA: [ ");
        sb.append(STR."X: \{format.format(scale.x)}, ");
        sb.append(STR."Y: \{format.format(scale.y)}, ");
        sb.append(STR."Z: \{format.format(scale.z)} ");
        sb.append(" ]");

        return sb.toString();
    }
}
