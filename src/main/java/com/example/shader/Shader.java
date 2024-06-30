package com.example.shader;

import java.util.HashMap;
import java.util.Map;

import com.example.data.ColorRGBA;
import com.example.data.Matrix4x4;
import com.example.data.VertexAttribute;

public abstract class Shader {
    protected static Map<String, Matrix4x4> globalMatrix = new HashMap<>();

    public static void setGlobalMatrix(String name, Matrix4x4 matrix4x4) {
        globalMatrix.put(name, matrix4x4);
    }

    public abstract VertexAttribute vertexShader(VertexAttribute input);

    public abstract ColorRGBA fragmentShader(VertexAttribute input);
}
