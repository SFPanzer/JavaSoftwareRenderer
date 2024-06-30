package com.example.renderer;

import java.util.ArrayList;

import com.example.component.Camera;
import com.example.component.MeshRenderer;
import com.example.data.ColorRGBA;
import com.example.data.Image;
import com.example.data.Matrix4x4;
import com.example.shader.Shader;

public class RenderContext {
    public class DepthBuffer {
        protected final int width;
        protected final int height;
        protected final float[] data;

        public DepthBuffer(int width, int height) {
            this.width = width;
            this.height = height;
            this.data = new float[width * height];
        }

        public float getDepth(int x, int y) {
            if (x > width || x < 0 || y > height || y < 0) {
                return 0;
            }

            int index = y * width + x;
            return data[index];
        }

        public void setDepth(int x, int y, float depth) {
            if (x > width || x < 0 || y > height || y < 0) {
                return;
            }

            int index = y * width + x;
            data[index] = depth;
        }

        public void fill(float depth) {
            for (int i = 0; i < width * height; i++) {
                data[i] = depth;
            }
        }
    }

    public final Image frameBuffer;
    public final DepthBuffer depthBuffer;
    public final ArrayList<MeshRenderer> meshRenderers = new ArrayList<>();

    public RenderContext(int width, int height) {
        frameBuffer = new Image(width, height);
        depthBuffer = new DepthBuffer(width, height);
    }

    public void SetCameraProperties(Camera camera) {
        Shader.setGlobalMatrix("MATRIX_VP",
                Matrix4x4.mult(camera.getProjectionMatrix(), camera.getWorldToCameraMatrix()));
    }

    public void clearRenderTarget(boolean clearDepth, boolean clearColor, ColorRGBA backgroundColor,
            float depth) {
        if (clearDepth) {
            depthBuffer.fill(depth);
        }
        if (clearColor) {
            frameBuffer.fill(backgroundColor);
        }
    }

    public void clearRenderTarget(boolean clearDepth, boolean clearColor, ColorRGBA backgroundColor) {
        clearRenderTarget(clearDepth, clearColor, backgroundColor, 1f);
    }

    public void drawRenderers() {
        for (var renderer : meshRenderers) {
            drawRenderer(renderer);
        }
        meshRenderers.clear();
    }

    private void drawRenderer(MeshRenderer meshRenderer) {

    }
}
