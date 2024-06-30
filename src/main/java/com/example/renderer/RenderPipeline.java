package com.example.renderer;

import com.example.component.Camera;

public abstract class RenderPipeline {
    public abstract void Render(Camera camera, RenderContext renderContext);
}
