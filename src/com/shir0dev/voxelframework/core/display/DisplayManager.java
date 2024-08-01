package com.shir0dev.voxelframework.core.display;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

public final class DisplayManager {
    public static final int WIDTH = 1280, HEIGHT = 720;
    public static final int TARGET_FPS = 120;

    public static void create() {
        ContextAttribs contextAttributes = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), contextAttributes);
            Display.setTitle("VoxelFramework (VFW) v0.0.1");
        } catch (LWJGLException e) {
            System.err.println("LWJGL ERROR: Could not create display.");
            System.exit(-1);
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }

    public static void prepare() {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClearColor(0, 0, 0, 1);
    }


    public static void update() {
        Display.sync(TARGET_FPS);
        Display.update();
    }

    public static void close() {
        Display.destroy();
    }
}