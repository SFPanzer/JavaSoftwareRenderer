package com.example.factory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.data.Mesh;
import com.example.data.Vector3;
import com.example.data.Vector4;
import com.example.data.VertexAttribute;

public class ObjMeshFactory {
    private ArrayList<Vector4> vertices = new ArrayList<>();
    private ArrayList<Vector3> texcoords = new ArrayList<>();
    private ArrayList<Vector3> normals = new ArrayList<>();
    private HashMap<VertexAttribute, Integer> vertexAttributeIndexMap = new HashMap<>();
    private ArrayList<VertexAttribute> vertexBuffer = new ArrayList<>();
    private ArrayList<Integer> elementBuffer = new ArrayList<>();

    public Mesh loadMesh(String filePath) {
        Mesh mesh = new Mesh();

        // Add zero texcoord and normal to revent out-of-bounds errors.
        texcoords.add(new Vector3(0, 0, 0));
        normals.add(new Vector3(0, 0, 0));

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("v ")) {
                    parseVertex(line);
                } else if (line.startsWith("vt ")) {
                    parseTexcoord(line);
                } else if (line.startsWith("vn ")) {
                    parseNormal(line);
                } else if (line.startsWith("f ")) {
                    parseFace(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mesh.vertexBuffer = vertexBuffer.toArray(new VertexAttribute[0]);
        mesh.elementBuffer = elementBuffer.toArray(new Integer[0]);

        return mesh;
    }

    private void parseVertex(String line) {
        String[] tokens = line.split(" ");

        float x = Float.parseFloat(tokens[1]);
        float y = -Float.parseFloat(tokens[2]);
        float z = Float.parseFloat(tokens[3]);
        float w = 1f;

        if (tokens.length > 4) {
            w = Float.parseFloat(tokens[4]);
        }

        Vector4 vertex = new Vector4(x, y, z, w);
        vertices.add(vertex);
    }

    private void parseTexcoord(String line) {
        String[] tokens = line.split(" ");

        float u = Float.parseFloat(tokens[1]);
        float v = 0f;
        float w = 0f;

        if (tokens.length > 2) {
            v = Float.parseFloat(tokens[2]);
        }
        if (tokens.length > 3) {
            w = Float.parseFloat(tokens[3]);
        }

        Vector3 texcoord = new Vector3(u, v, w);
        texcoords.add(texcoord);
    }

    private void parseNormal(String line) {
        String[] tokens = line.split(" ");

        float x = Float.parseFloat(tokens[1]);
        float y = -Float.parseFloat(tokens[2]);
        float z = Float.parseFloat(tokens[3]);

        Vector3 normal = new Vector3(x, y, z);
        normals.add(normal);
    }

    private void parseFace(String line) {
        String[] tokens = line.split(" ");

        for (int i = 0; i < 3; i++) {
            // Note: Wavefont Obj file's indices start from 1;
            Vector4 vertex;
            Vector3 texcoord = texcoords.get(0);
            Vector3 normal = normals.get(0);

            // Parse indices;
            String token = tokens[i + 1];
            String[] elements = token.split("/");

            vertex = vertices.get(Integer.parseInt(elements[0]) - 1);

            if (elements.length > 1) {
                if (!elements[1].equals("")) {
                    texcoord = texcoords.get(Integer.parseInt(elements[1]));
                }
            }
            if (elements.length > 2) {
                if (!elements[2].equals("")) {
                    normal = normals.get(Integer.parseInt(elements[2]));
                }
            }

            VertexAttribute vertexAttribute = new VertexAttribute(vertex, normal, texcoord);

            // Remove duplicates.
            if (!vertexAttributeIndexMap.containsKey(vertexAttribute)) {
                vertexBuffer.add(vertexAttribute);
                vertexAttributeIndexMap.put(vertexAttribute, vertexBuffer.size() - 1);
            }

            elementBuffer.add(vertexAttributeIndexMap.get(vertexAttribute));
        }

    }
}
