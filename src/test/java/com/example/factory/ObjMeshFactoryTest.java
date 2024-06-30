package com.example.factory;

import org.junit.jupiter.api.Test;

import com.example.data.Mesh;

public class ObjMeshFactoryTest {
    @Test
    public void testLoadMesh() {
        Mesh mesh = new ObjMeshFactory().loadMesh("src/main/resources/stanford_bunny_10k.obj");
        System.out.printf("VertexBufferLength: %d, ElementBufferLength: %d;\n",
                mesh.vertexBuffer.length,
                mesh.elementBuffer.length);
    }
}
