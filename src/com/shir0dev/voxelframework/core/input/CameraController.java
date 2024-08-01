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
        mouseInput.x *= 0.03F;
        mouseInput.y *= 0.03F;
        Transform camTrans = this.camera.transform();
        Quaternion rotationY = QuaternionUtil.euler(new Vector3f(0.0F, mouseInput.x, 0.0F));
        Vector3f currentRotation = camTrans.getRotationXYZ();
        currentRotation.x = Math.clamp(currentRotation.x, -70.0F, 70.0F);
        float angleX;
        if (Math.abs(currentRotation.x) == 70.0F) {
            currentRotation.x = Math.copySign(69.9F, -1.0F * currentRotation.x);
            currentRotation.z = 0.0F;
            camTrans.setRotationXYZ(currentRotation);
            angleX = 0.0F;
        } else {
            angleX = -mouseInput.y;
        }

        Quaternion rotationX = QuaternionUtil.axisAngle(camTrans.right(), angleX);
        camTrans.rotate(QuaternionUtil.toMatrix4(Quaternion.mul(rotationX, rotationY, (Quaternion)null)));
    }

    private void doMovement() {
        Vector2f moveDir = Input.getMovementVector();
        if (!Objects.equals(moveDir, new Vector2f(0.0F, 0.0F))) {
            Transform camTrans = this.camera.transform();
            Vector3f forward = camTrans.forward();
            forward.negate();
            VectorUtil.scale(forward, moveDir.y, forward);
            Vector3f right = camTrans.right();
            VectorUtil.scale(right, moveDir.x, right);
            Vector3f translation = new Vector3f();
            Vector3f.add(forward, right, translation);
            if (translation.lengthSquared() != 0.0F) {
                translation.normalise();
            }

            camTrans.translate(VectorUtil.scale(translation, 0.05F, (Vector3f)null));
        }
    }
}
