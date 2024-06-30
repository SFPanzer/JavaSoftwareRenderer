package com.example.data;

public class Vector3 {
    public float x;
    public float y;
    public float z;

    public static final Vector3 ZERO = new Vector3(0, 0, 0);
    public static final Vector3 ONE = new Vector3(1, 1, 1);
    public static final Vector3 UP = new Vector3(0, 1, 0);
    public static final Vector3 DOWN = new Vector3(0, -1, 0);
    public static final Vector3 LEFT = new Vector3(-1, 0, 0);
    public static final Vector3 RIGHT = new Vector3(1, 0, 0);
    public static final Vector3 FORWARD = new Vector3(0, 0, 1);
    public static final Vector3 BACK = new Vector3(0, 0, -1);

    public static Vector3 transformAsVector(Vector3 vector, Matrix4x4 matrix) {
        Vector4 vector4 = new Vector4(vector.x, vector.y, vector.z, 0);
        Vector4 transformResult = Vector4.transform(vector4, matrix);
        return new Vector3(transformResult.x, transformResult.y, transformResult.z);
    }

    public static Vector3 transformAsCoordinate(Vector3 coordinate, Matrix4x4 matrix) {
        Vector4 homogeneousCoordinate = new Vector4(coordinate.x, coordinate.y, coordinate.z, 1);
        Vector4 transformResult = Vector4.transform(homogeneousCoordinate, matrix);
        return new Vector3(transformResult.x, transformResult.y, transformResult.z);
    }

    public static float dot(Vector3 a, Vector3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static Vector3 cross(Vector3 a, Vector3 b) {
        return new Vector3(
                a.y * b.z - a.z * b.y,
                a.z * b.x - a.x * b.z,
                a.x * b.y - a.y * b.x);
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Vector3 vector = (Vector3) obj;

        return FloatUtils.equals(x, vector.x) &&
                FloatUtils.equals(y, vector.y) &&
                FloatUtils.equals(z, vector.z);
    }

    @Override
    public String toString() {
        return String.format("[%f, %f, %f]", x, y, z);
    }

    public float getSqrLength() {
        return x * x + y * y + z * z;
    }

    public float getLength() {
        return (float) Math.sqrt(getSqrLength());
    }

    public Vector3 getNormalized() {
        float length = getLength();
        if (length == 0) {
            return ZERO;
        }
        return new Vector3(x / length, y / length, z / length);
    }
}
