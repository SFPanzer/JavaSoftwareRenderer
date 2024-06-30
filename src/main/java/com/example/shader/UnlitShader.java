package com.example.shader;

import com.example.data.ColorRGBA;
import com.example.data.Vector3;
import com.example.data.Vector4;
import com.example.data.VertexAttribute;

public class UnlitShader extends Shader {

    @Override
    public VertexAttribute vertexShader(VertexAttribute input) {
        VertexAttribute o = new VertexAttribute(
                Vector4.transform(input.position, Shader.getGlobalMatrix("MATRIX_MVP")),
                Vector3.transformAsVector(input.normal, Shader.getGlobalMatrix("MATRIX_MVP")),
                input.texcoord);
        return o;
    }

    @Override
    public ColorRGBA fragmentShader(VertexAttribute input) {
        return ColorRGBA.WHITE;
    }
}
