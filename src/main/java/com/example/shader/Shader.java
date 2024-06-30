package com.example.shader;

import java.util.HashMap;
import java.util.Map;

import com.example.data.ColorRGBA;
import com.example.data.Matrix4x4;
import com.example.data.VertexAttribute;

public abstract class Shader {
    public enum Cull {
        OFF, BACK, FRONT
    }

    protected static Map<String, Matrix4x4> globalMatrix = new HashMap<>();
    public Cull cull = Cull.BACK;

    public static void setGlobalMatrix(String name, Matrix4x4 matrix4x4) {
        globalMatrix.put(name, matrix4x4);
    }

    public static Matrix4x4 getGlobalMatrix(String name) {
        return globalMatrix.get(name);
    }

    public abstract VertexAttribute vertexShader(VertexAttribute input);

    public abstract ColorRGBA fragmentShader(VertexAttribute input);
}
