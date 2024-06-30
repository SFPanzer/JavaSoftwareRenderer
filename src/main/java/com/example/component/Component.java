package com.example.component;

import com.example.Scene.SceneObject;

public abstract class Component {
    private SceneObject attachedObject;

    public Component(SceneObject attachedObject) {
        this.attachedObject = attachedObject;
    }

    public SceneObject getAttachedSceneObject() {
        return attachedObject;
    }

    public abstract void start();

    public abstract void update(float delta);

    public abstract void end();
}
