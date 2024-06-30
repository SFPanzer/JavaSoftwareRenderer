package com.example.component;

import com.example.Scene.SceneObject;
import com.example.data.Matrix4x4;
import com.example.data.Mesh;
import com.example.renderer.RenderContext;
import com.example.shader.Shader;

public class MeshRenderer extends Component {
    public Mesh mesh;
    public Shader shader;
    public Matrix4x4 MATRIX_M;

    public MeshRenderer(SceneObject attachedObject) {
        super(attachedObject);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float delta) {
        MATRIX_M = getAttachedSceneObject().transform.getLocalToWorldMatrix();
        RenderContext renderContext = getAttachedSceneObject().scene.application.getRenderContext();
        renderContext.meshRenderers.add(this);
    }

    @Override
    public void end() {

    }

}
