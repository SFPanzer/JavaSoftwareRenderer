package com.example.data;

import java.nio.ByteBuffer;

import com.example.utils.FNV1aHash;

public class VertexAttribute {
    public Vector4 position;
    public Vector3 normal;
    public Vector3 texcoord;

    public VertexAttribute(Vector4 position, Vector3 normal, Vector3 texcoord) {
        this.position = position;
        this.normal = normal;
        this.texcoord = texcoord;
    }

    @Override
    public int hashCode() {
        ByteBuffer buffer = ByteBuffer.allocate(4 * (4 + 3 + 3));

        buffer.putFloat(position.x);
        buffer.putFloat(position.y);
        buffer.putFloat(position.z);
        buffer.putFloat(position.w);
        buffer.putFloat(normal.x);
        buffer.putFloat(normal.y);
        buffer.putFloat(normal.z);
        buffer.putFloat(texcoord.x);
        buffer.putFloat(texcoord.y);
        buffer.putFloat(texcoord.z);

        return FNV1aHash.hash(buffer.array());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        VertexAttribute vertexAttribute = (VertexAttribute) obj;

        return position == vertexAttribute.position &&
                texcoord == vertexAttribute.texcoord &&
                normal == vertexAttribute.normal;
    }
}
