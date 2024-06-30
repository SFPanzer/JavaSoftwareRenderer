package com.example.component;

import com.example.data.Matrix4x4;
import com.example.data.Vector3;

public class Transform {
    public Vector3 position;
    public Vector3 eulerAngle;
    public Vector3 scale;

    public Transform parent;

    public Transform(Vector3 position, Vector3 eulerAngle, Vector3 scale) {
        this.position = position;
        this.eulerAngle = eulerAngle;
        this.scale = scale;
        this.parent = null;
    }

    public Transform() {
        position = Vector3.ZERO;
        eulerAngle = Vector3.ZERO;
        scale = Vector3.ONE;
        parent = null;
    }

    public static Matrix4x4 getTranslationMatrix(Vector3 translation) {
        Matrix4x4 translationMatrix = new Matrix4x4(
                1, 0, 0, translation.x,
                0, 1, 0, translation.y,
                0, 0, 1, translation.z,
                0, 0, 0, 1);
        return translationMatrix;
    }

    public static Matrix4x4 getRotationXMatrix(float degree) {
        float sin = (float) Math.sin(Math.toRadians(degree));
        float cos = (float) Math.cos(Math.toRadians(degree));

        Matrix4x4 rotationX = new Matrix4x4(
                1, 0, 0, 0,
                0, cos, -sin, 0,
                0, sin, cos, 0,
                0, 0, 0, 1);

        return rotationX;
    }

    public static Matrix4x4 getRotationYMatrix(float degree) {
        float sin = (float) Math.sin(Math.toRadians(degree));
        float cos = (float) Math.cos(Math.toRadians(degree));

        Matrix4x4 rotationY = new Matrix4x4(
                cos, 0, sin, 0,
                0, 1, 0, 0,
                -sin, 0, cos, 0,
                0, 0, 0, 1);

        return rotationY;
    }

    public static Matrix4x4 getRotationZMatrix(float degree) {
        float sin = (float) Math.sin(Math.toRadians(degree));
        float cos = (float) Math.cos(Math.toRadians(degree));

        Matrix4x4 rotationZ = new Matrix4x4(
                cos, -sin, 0, 0,
                sin, cos, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);

        return rotationZ;
    }

    public Matrix4x4 getLocalToWorldMatrix() {
        Matrix4x4 parentLocalToWorldMatrix;
        if (parent != null) {
            parentLocalToWorldMatrix = parent.getLocalToWorldMatrix();
        } else {
            parentLocalToWorldMatrix = Matrix4x4.getIdentity();
        }

        // Calculate TRS Matrix.
        // Calculate Transpose Matrix.
        Matrix4x4 translationMatrix = getTranslationMatrix(position);
        // Calculate Rotation matrix.
        Matrix4x4 rotationX = getRotationXMatrix(eulerAngle.x);
        Matrix4x4 rotationY = getRotationXMatrix(eulerAngle.y);
        Matrix4x4 rotationZ = getRotationXMatrix(eulerAngle.z);
        Matrix4x4 rotationMatrix = Matrix4x4.mult(rotationY, Matrix4x4.mult(rotationX, rotationZ));

        // Calculate Scale Matrix.
        Matrix4x4 scaleMatrix = new Matrix4x4(
                scale.x, 0, 0, 0,
                0, scale.y, 0, 0,
                0, 0, scale.z, 0,
                0, 0, 0, 1);

        Matrix4x4 TRS = Matrix4x4.mult(parentLocalToWorldMatrix,
                Matrix4x4.mult(translationMatrix, Matrix4x4.mult(rotationMatrix, scaleMatrix)));

        return TRS;
    }
}
