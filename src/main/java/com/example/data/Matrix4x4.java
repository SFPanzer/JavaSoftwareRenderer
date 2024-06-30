package com.example.data;

public class Matrix4x4 {
    private float data[][] = new float[4][4];

    public static Matrix4x4 getIdentity() {
        return new Matrix4x4(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);
    }

    public static Matrix4x4 mult(Matrix4x4 left, Matrix4x4 right) {
        Matrix4x4 matrix = new Matrix4x4();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                for (int index = 0; index < 4; index++) {
                    matrix.data[row][col] += left.get(row, index) * right.get(index, col);
                }
            }
        }

        return matrix;
    }

    public Matrix4x4() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                data[row][col] = 0;
            }
        }
    }

    public Matrix4x4(
            float a11, float a12, float a13, float a14,
            float a21, float a22, float a23, float a24,
            float a31, float a32, float a33, float a34,
            float a41, float a42, float a43, float a44) {
        data[0][0] = a11;
        data[0][1] = a12;
        data[0][2] = a13;
        data[0][3] = a14;
        data[1][0] = a21;
        data[1][1] = a22;
        data[1][2] = a23;
        data[1][3] = a24;
        data[2][0] = a31;
        data[2][1] = a32;
        data[2][2] = a33;
        data[2][3] = a34;
        data[3][0] = a41;
        data[3][1] = a42;
        data[3][2] = a43;
        data[3][3] = a44;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Matrix4x4 matrix = (Matrix4x4) obj;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (!FloatUtils.equals(data[row][col], matrix.data[row][col])) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return """
                [%.2f,\t%.2f,\t%.2f,\t%.2f]
                [%.2f,\t%.2f,\t%.2f,\t%.2f]
                [%.2f,\t%.2f,\t%.2f,\t%.2f]
                [%.2f,\t%.2f,\t%.2f,\t%.2f]
                """.formatted(
                data[0][0], data[0][1], data[0][2], data[0][3],
                data[1][0], data[1][1], data[1][2], data[1][3],
                data[2][0], data[2][1], data[2][2], data[2][3],
                data[3][0], data[3][1], data[3][2], data[3][3]);
    }

    public float get(int row, int col) {
        return data[row][col];
    }
}
