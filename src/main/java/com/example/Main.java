package com.example;

import com.example.Scene.Scene;
import com.example.Scene.SceneObject;
import com.example.application.Application;
import com.example.component.Camera;
import com.example.component.MeshRenderer;
import com.example.factory.ObjMeshFactory;
import com.example.renderer.SimpleRenderPipeline;

public class Main extends Application {
    public static void main(String[] args) {
        Main app = new Main();
        app.renderPipeline = new SimpleRenderPipeline();
        app.launch();
    }

    @Override
    protected void initialize() {
        Scene scene = this.getScene();

        SceneObject cameraObject = new SceneObject();
        cameraObject.addComponent(Camera.class);
        Camera camera = cameraObject.getComponent(Camera.class);
        camera.renderTarget = getRenderContext().frameBuffer;
        Camera.mainCamera = camera;
        scene.addSceneObject(cameraObject);

        SceneObject bunny = new SceneObject();
        bunny.addComponent(MeshRenderer.class);
        bunny.getComponent(MeshRenderer.class).mesh = new ObjMeshFactory()
                .loadMesh("src/main/resources/stanford_bunny_10k.obj");
        scene.addSceneObject(bunny);
    }

    @Override
    protected void update(float delta) {

    }
}