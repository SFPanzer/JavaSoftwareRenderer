package com.example.Scene;

import java.util.ArrayList;

import com.example.application.Application;
import com.example.component.Component;

public class Scene {
    public final Application application;

    private ArrayList<SceneObject> sceneObjects = new ArrayList<>();

    public Scene(Application application) {
        this.application = application;
    }

    public void start() {
        for (SceneObject sceneObject : sceneObjects) {
            for (Component component : sceneObject.components) {
                component.start();
            }
        }
    }

    public void update(float delta) {
        for (SceneObject sceneObject : sceneObjects) {
            for (Component component : sceneObject.components) {
                component.update(delta);
            }
        }
    }

    public void end() {
        for (SceneObject sceneObject : sceneObjects) {
            for (Component component : sceneObject.components) {
                component.end();
            }
        }
    }

    public void addSceneObject(SceneObject sceneObject) {
        sceneObject.scene = this;
        sceneObjects.add(sceneObject);
    }
}
