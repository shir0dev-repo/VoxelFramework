package com.shir0dev.voxelframework.core.display.shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class ShaderUtil {
    private static final String SHADER_RESOURCE_PATH = "res/shader/";
    private static final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public static String getShaderFilePath(@NotNull String name, int type) {
        String path = SHADER_RESOURCE_PATH + name;
        if (type == GL20.GL_VERTEX_SHADER) {
            path = path.concat(".vert");
        } else if (type == GL20.GL_FRAGMENT_SHADER) {
            path = path.concat(".frag");
        } else {
            System.err.println(STR."Invalid shader type provided: \{type}");
            System.exit(-1);
        }

        return path;
    }

    public static int loadShader(String shaderName, int type) {
        StringBuilder sourceFile = new StringBuilder();
        String shaderPath = getShaderFilePath(shaderName, type);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(shaderPath));

            String line;
            while((line = reader.readLine()) != null) {
                sourceFile.append(line).append('\n');
            }

            reader.close();
        } catch (IOException var6) {
            System.err.println(STR."Could not reader shader file at: \{shaderPath}.");
            System.exit(-1);
        }

        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, sourceFile);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, 35713) == 0) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println(STR."Failed to compile shader \{shaderName} at \{shaderPath}.");
            System.exit(-1);
        }

        return shaderID;
    }

    public static void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    public static void loadBoolean(int location, boolean value) {
        GL20.glUniform1f(location, value ? 1.0F : 0.0F);
    }

    public static void loadVector2f(int location, Vector2f value) {
        GL20.glUniform2f(location, value.x, value.y);
    }

    public static void loadVector3f(int location, Vector3f value) {
        GL20.glUniform3f(location, value.x, value.y, value.z);
    }

    public static void loadMatrix4f(int location, Matrix4f value) {
        value.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4(location, false, matrixBuffer);
    }
}
