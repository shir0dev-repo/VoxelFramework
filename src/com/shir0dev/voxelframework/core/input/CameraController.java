package com.shir0dev.voxelframework.core.input;

import com.shir0dev.voxelframework.core.display.render.Camera;
import com.shir0dev.voxelframework.core.transform.Transform;
import com.shir0dev.voxelframework.core.transform.util.QuaternionUtil;
import com.shir0dev.voxelframework.core.transform.util.VectorUtil;
import java.util.Objects;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class CameraController {
    private final Camera camera = Camera.getInstance();
    private final float SPEED_X = 0.03F;
    private final float SPEED_Y = 0.03F;
    private final float MOVE_SPEED = 0.05F;

    public CameraController() {
    }

    public void update() {
        this.doRotation();
        this.doMovement();
    }

    private void doRotation() {
        Vector3f mouseInput = Input.getMouseDelta();
        mouseInput.x *= SPEED_X;
        mouseInput.y *= SPEED_Y;

        Transform camTrans = this.camera.transform();
        Quaternion rotationY = QuaternionUtil.euler(new Vector3f(0.0F, mouseInput.x, 0.0F));
        Vector3f currentRotation = camTrans.getRotationXYZ();
        currentRotation.x = Math.clamp(currentRotation.x, -89.9f, 89.9f);
        float angleX;
        if (Math.abs(currentRotation.x) == 89.9f) {
            currentRotation.x = Math.copySign(89.9f, -1.0F * currentRotation.x);
            currentRotation.z = 0.0F;
            camTrans.setRotationXYZ(currentRotation);
            angleX = 0.0F;
        } else {
            angleX = -mouseInput.y;
        }

        Quaternion rotationX = QuaternionUtil.axisAngle(camTrans.right(), angleX);
        camTrans.rotate(QuaternionUtil.toMatrix4(Quaternion.mul(rotationX, rotationY,null)));
    }

    private void doMovement() {
        Vector3f moveDir = Input.getMovementVector();
        if (!Objects.equals(moveDir, new Vector3f(0, 0, 0))) {
            Transform camTrans = this.camera.transform();

            Vector3f forward = camTrans.forward();
            forward.negate();
            VectorUtil.scale(forward, moveDir.z, forward);

            Vector3f right = camTrans.right();
            VectorUtil.scale(right, moveDir.x, right);

            Vector3f up = camTrans.up();
            VectorUtil.scale(up, moveDir.y, up);

            Vector3f translation = new Vector3f();
            Vector3f.add(forward, right, translation);
            Vector3f.add(translation, up, translation);

            if (translation.lengthSquared() != 0.0F) {
                translation.normalise();
            }

            camTrans.translate(VectorUtil.scale(translation, 0.05f,null));
        }
    }
}
