package com.example.component;

import com.example.Scene.SceneObject;
import com.example.data.Image;
import com.example.data.Matrix4x4;
import com.example.data.Vector3;

public class Camera extends Component {
    public enum Projection {
        ORTHOGRAPHIC,
        PERSPECTIVE
    }

    public static Camera mainCamera;

    public Image renderTarget;
    public Projection projection = Projection.PERSPECTIVE;
    public float fov = 60f;
    public float near = 0.1f;
    public float far = 100f;

    public Camera(SceneObject attachedObject) {
        super(attachedObject);
    }

    public Matrix4x4 getWorldToCameraMatrix() {
        Transform cameraTransform = getAttachedSceneObject().transform;

        Matrix4x4 TranslationMatrix = Transform.getTranslationMatrix(
                new Vector3(
                        -cameraTransform.position.x,
                        -cameraTransform.position.y,
                        -cameraTransform.position.z));

        Matrix4x4 RotationXMatrix = Transform.getRotationXMatrix(-cameraTransform.eulerAngle.x);
        Matrix4x4 RotationYMatrix = Transform.getRotationXMatrix(-cameraTransform.eulerAngle.y);
        Matrix4x4 RotationZMatrix = Transform.getRotationXMatrix(-cameraTransform.eulerAngle.z);
        Matrix4x4 RotationMatrix = Matrix4x4.mult(RotationZMatrix, Matrix4x4.mult(RotationXMatrix, RotationYMatrix));

        Matrix4x4 WorldToCameraMatrix = Matrix4x4.mult(RotationMatrix, TranslationMatrix);

        return WorldToCameraMatrix;
    }

    private Matrix4x4 getOrthographicMatrix() {
        float aspect = (float) renderTarget.getWidth() / renderTarget.getHeight();
        float width = (float) Math.tan(Math.toRadians(fov));
        float height = width / aspect;

        Matrix4x4 orthographicMatrix = new Matrix4x4(
                2 / width, 0, 0, 0,
                0, 2 / height, 0, 0,
                0, 0, 1 / (far - near), -near / (far - near),
                0, 0, 0, 0);

        return orthographicMatrix;
    }

    private Matrix4x4 getPerspectiveMatrix() {
        float aspect = (float) renderTarget.getWidth() / renderTarget.getHeight();
        float radFov = (float) Math.toRadians(fov) / 2;

        Matrix4x4 perspectiveMatrix = new Matrix4x4(
                1 / (float) Math.tan(radFov), 0, 0, 0,
                0, 1 / ((float) Math.tan(radFov) * aspect), 0, 0,
                0, 0, -(far + near) / (far - near), -(2 * far * near) / (far - near),
                0, 0, -1, 0);

        return perspectiveMatrix;
    }

    public Matrix4x4 getProjectionMatrix() {
        switch (projection) {
            case ORTHOGRAPHIC:
                return getOrthographicMatrix();
            case PERSPECTIVE:
                return getPerspectiveMatrix();

            default:
                return getOrthographicMatrix();
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void end() {
    }
}
