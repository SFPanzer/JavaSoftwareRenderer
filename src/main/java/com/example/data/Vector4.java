package com.example.data;

public class Vector4 {
    public float x;
    public float y;
    public float z;
    public float w;

    public static Vector4 transform(Vector4 vector, Matrix4x4 matrix) {
        float input[] = new float[4];
        float output[] = new float[4];

        input[0] = vector.x;
        input[1] = vector.y;
        input[2] = vector.z;
        input[3] = vector.w;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                output[row] += matrix.get(row, col) * input[col];
            }
        }

        return new Vector4(output[0], output[1], output[2], output[3]);
    }

    public static Vector4 plus(Vector4 a, Vector4 b) {
        return new Vector4(a.x + b.x,
                a.y + b.y,
                a.z + b.z,
                a.w + b.w);
    }

    public static Vector4 sub(Vector4 a, Vector4 b) {
        return new Vector4(a.x - b.x,
                a.y - b.y,
                a.z - b.z,
                a.w - b.w);
    }

    public Vector4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Vector4 vector = (Vector4) obj;

        return FloatUtils.equals(x, vector.x) &&
                FloatUtils.equals(y, vector.y) &&
                FloatUtils.equals(z, vector.z) &&
                FloatUtils.equals(w, vector.w);
    }

    @Override
    public String toString() {
        return String.format("[%f, %f, %f, %f]", x, y, z, w);
    }
}
