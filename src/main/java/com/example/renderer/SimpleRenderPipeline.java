package com.example.renderer;

import com.example.component.Camera;
import com.example.data.ColorRGBA;

public class SimpleRenderPipeline extends RenderPipeline {

    @Override
    public void Render(Camera camera, RenderContext renderContext) {
        renderContext.clearRenderTarget(true, true, ColorRGBA.MAGENTA);
        renderContext.SetCameraProperties(camera);
        renderContext.drawRenderers();
    }
}
