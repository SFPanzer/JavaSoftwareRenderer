package com.example.Scene;

import java.util.ArrayList;

import com.example.component.Component;

public class Scene {
    public ArrayList<SceneObject> sceneObjects = new ArrayList<>();

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
}
