package com.example.Scene;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.example.component.Component;
import com.example.component.Transform;

public class SceneObject {
    public Scene scene = null;
    public String name;
    public Transform transform = new Transform();
    public ArrayList<Component> components = new ArrayList<>();

    public SceneObject(String name) {
        this.name = name;
    }

    public <T extends Component> T getComponent(Class<T> type) {
        for (Component component : components) {
            if (type.isInstance(component)) {
                return type.cast(component);
            }
        }
        return null;
    };

    public <T extends Component> void addComponent(Class<T> type) {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor(SceneObject.class);
            T component = constructor.newInstance(this);
            components.add(component);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
