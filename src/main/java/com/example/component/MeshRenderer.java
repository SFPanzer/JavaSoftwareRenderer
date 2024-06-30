package com.example.component;

import java.util.ArrayList;

import com.example.Scene.SceneObject;
import com.example.data.Matrix4x4;
import com.example.data.Mesh;
import com.example.shader.Shader;

public class MeshRenderer extends Component {
    public Mesh mesh;
    public Shader shader;
    public Matrix4x4 MATRIX_M;

    public static ArrayList<MeshRenderer> meshRenderers;

    public MeshRenderer(SceneObject attachedObject) {
        super(attachedObject);
        meshRenderers.add(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float delta) {
        MATRIX_M = getAttachedSceneObject().transform.getLocalToWorldMatrix();
    }

    @Override
    public void end() {
        meshRenderers.remove(this);
    }

}
