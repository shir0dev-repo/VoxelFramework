package com.shir0dev.voxelframework.core.display.mesh;

import com.shir0dev.voxelframework.core.display.GLHandler;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class OBJLoader {
    public static StaticMesh loadObjModel(String fileName, GLHandler loader) {
        FileReader fr = null;

        try {
            fr = new FileReader(STR."res/obj/\{fileName}.obj");
        } catch (FileNotFoundException e) {
            System.err.println(STR."Could not load OBJ file at res/\{fileName}.obj\n\{e}");
        }

        assert fr != null;

        BufferedReader reader = new BufferedReader(fr);
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        float[] normalsArray = null;
        float[] uvsArray = null;

        try {
            String line;
            String[] currentLine;
            label80:
            do {
                while(true) {
                    while(true) {
                        while(true) {
                            line = reader.readLine();
                            currentLine = line.split(" ");
                            if (!line.startsWith("v ")) {
                                if (!line.startsWith("vt ")) {
                                    if (!line.startsWith("vn ")) {
                                        continue label80;
                                    }

                                    addNormal(currentLine, normals);
                                } else {
                                    addUV(currentLine, textures);
                                }
                            } else {
                                addVertex(currentLine, vertices);
                            }
                        }
                    }
                }
            } while(!line.startsWith("f "));

            uvsArray = new float[vertices.size() * 2];
            normalsArray = new float[vertices.size() * 3];

            while(line != null) {
                if (!line.startsWith("f ")) {
                    line = reader.readLine();
                } else {
                    currentLine = line.split(" ");
                    processVertices(currentLine, indices, textures, normals, uvsArray, normalsArray);
                    line = reader.readLine();
                }
            }

            reader.close();
        } catch (Exception var17) {
            var17.printStackTrace();
        }

        float[] verticesArray = new float[vertices.size() * 3];
        int[] indicesArray = new int[indices.size()];
        int vertexIndex = 0;

        Vector3f v;
        for(Iterator var14 = vertices.iterator(); var14.hasNext(); verticesArray[vertexIndex++] = v.z) {
            v = (Vector3f)var14.next();
            verticesArray[vertexIndex++] = v.x;
            verticesArray[vertexIndex++] = v.y;
        }

        for(int i = 0; i < indices.size(); ++i) {
            indicesArray[i] = (Integer)indices.get(i);
        }

        return loader.loadToVAO(verticesArray, uvsArray, indicesArray, normalsArray, false);
    }

    private static void addVertex(String[] currentLine, List<Vector3f> vertexList) {
        float x = Float.parseFloat(currentLine[1]);
        float y = Float.parseFloat(currentLine[2]);
        float z = Float.parseFloat(currentLine[3]);
        vertexList.add(new Vector3f(x, y, z));
    }

    private static void addUV(String[] currentLine, List<Vector2f> uvList) {
        float x = Float.parseFloat(currentLine[1]);
        float y = Float.parseFloat(currentLine[2]);
        uvList.add(new Vector2f(x, y));
    }

    private static void addNormal(String[] currentLine, List<Vector3f> normalsList) {
        float x = Float.parseFloat(currentLine[1]);
        float y = Float.parseFloat(currentLine[2]);
        float z = Float.parseFloat(currentLine[3]);
        normalsList.add(new Vector3f(x, y, z));
    }

    private static void processVertices(String[] currentLine, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalArray) {
        String[][] vertices = new String[][]{currentLine[1].split("/"), currentLine[2].split("/"), currentLine[3].split("/")};
        String[][] var7 = vertices;
        int var8 = vertices.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            String[] vertex = var7[var9];
            if (!Arrays.asList(vertex).contains("")) {
                int currentVertexIndex = Integer.parseInt(vertex[0]) - 1;
                indices.add(currentVertexIndex);
                Vector2f currentTexture = (Vector2f)textures.get(Integer.parseInt(vertex[1]) - 1);
                textureArray[currentVertexIndex * 2] = currentTexture.x;
                textureArray[currentVertexIndex * 2 + 1] = 1.0F - currentTexture.y;
                Vector3f currentNormal = (Vector3f)normals.get(Integer.parseInt(vertex[2]) - 1);
                normalArray[currentVertexIndex * 3] = currentNormal.x;
                normalArray[currentVertexIndex * 3 + 1] = currentNormal.y;
                normalArray[currentVertexIndex * 3 + 2] = currentNormal.z;
            }
        }

    }
}
