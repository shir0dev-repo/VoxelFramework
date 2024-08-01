package com.shir0dev.voxelframework.core.input;

import com.shir0dev.voxelframework.core.display.debug.DebugScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Input {
    private static final int CENTER_X = 640;
    private static final int CENTER_Y = 360;
    private static Vector3f mouseDelta;
    private static final Vector2f movementVector;

    public Input() {
    }

    public static Vector3f getMouseDelta() {
        return mouseDelta;
    }

    public static Vector2f getMovementVector() {
        return movementVector;
    }

    public static void poll() {
        mouseDelta.set((float)Mouse.getDX(), (float)Mouse.getDY());
        checkMovement();
        Keyboard.poll();

        while(Keyboard.next()) {
            if (Keyboard.isKeyDown(1)) {
                Mouse.setGrabbed(!Mouse.isGrabbed());
            } else if (Keyboard.isKeyDown(61)) {
                DebugScreen.toggle();
            }
        }

    }

    private static void checkMovement() {
        if (Keyboard.isKeyDown(30)) {
            movementVector.x = -1.0F;
        } else if (Keyboard.isKeyDown(32)) {
            movementVector.x = 1.0F;
        } else {
            movementVector.x = 0.0F;
        }

        if (Keyboard.isKeyDown(31)) {
            movementVector.y = -1.0F;
        } else if (Keyboard.isKeyDown(17)) {
            movementVector.y = 1.0F;
        } else {
            movementVector.y = 0.0F;
        }

    }

    public static void lateUpdate() {
        if (Display.isActive() && Mouse.isGrabbed()) {
            Mouse.setCursorPosition(CENTER_X, CENTER_Y);
        }

        movementVector.set(0.0F, 0.0F);
    }

    static {
        Mouse.setGrabbed(true);
        mouseDelta = new Vector3f();
        movementVector = new Vector2f(0.0F, 0.0F);
    }
}
