package com.example;

import com.example.Scene.Scene;
import com.example.Scene.SceneObject;
import com.example.application.Application;
import com.example.component.Camera;
import com.example.component.MeshRenderer;
import com.example.component.Camera.Projection;
import com.example.data.Vector3;
import com.example.factory.ObjMeshFactory;
import com.example.renderer.SimpleRenderPipeline;
import com.example.shader.UnlitShader;

public class Main extends Application {
    public static void main(String[] args) {
        Main app = new Main();
        app.renderPipeline = new SimpleRenderPipeline();
        app.launch();
    }

    private SceneObject bunny;
    private SceneObject cameraObject;

    @Override
    protected void initialize() {
        Scene scene = this.getScene();

        cameraObject = new SceneObject("Camera");
        cameraObject.transform.position = new Vector3(0, 0, -5);
        cameraObject.addComponent(Camera.class);
        Camera camera = cameraObject.getComponent(Camera.class);
        Camera.mainCamera = camera;
        camera.projection = Projection.ORTHOGRAPHIC;
        camera.renderTarget = getRenderContext().frameBuffer;
        scene.addSceneObject(cameraObject);

        bunny = new SceneObject("Bunny");
        bunny.transform.position = new Vector3(0, 0.5f, 0);
        bunny.addComponent(MeshRenderer.class);
        MeshRenderer bunnyMeshRenderer = bunny.getComponent(MeshRenderer.class);
        bunnyMeshRenderer.mesh = new ObjMeshFactory().loadMesh("src/main/resources/stanford_bunny_10k.obj");
        bunnyMeshRenderer.shader = new UnlitShader();
        scene.addSceneObject(bunny);
    }

    @Override
    protected void update(float delta) {
        bunny.transform.eulerAngle.y += delta * 50f;
    }
}