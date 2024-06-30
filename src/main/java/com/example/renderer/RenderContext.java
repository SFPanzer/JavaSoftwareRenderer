package com.example.renderer;

import java.util.ArrayList;

import com.example.component.Camera;
import com.example.component.MeshRenderer;
import com.example.data.ColorRGBA;
import com.example.data.Image;
import com.example.data.Matrix4x4;
import com.example.data.VertexAttribute;
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
        Matrix4x4 matrixM = meshRenderer.getAttachedSceneObject().transform.getLocalToWorldMatrix();
        Shader.setGlobalMatrix("MATRIX_MVP", Matrix4x4.mult(Shader.getGlobalMatrix("MATRIX_VP"), matrixM));

        // Execute VertexShader;
        VertexAttribute[] vertexShaderOutput = new VertexAttribute[meshRenderer.mesh.vertexBuffer.length];
        for (int i = 0; i < vertexShaderOutput.length; i++) {
            vertexShaderOutput[i] = meshRenderer.shader.vertexShader(meshRenderer.mesh.vertexBuffer[i]);
        }

        // Primitive Assembly;
        for (int i = 0; i < meshRenderer.mesh.elementBuffer.length / 3; i++) {
            boolean isDiscarded = true;
            VertexAttribute[] primitiveVertices = new VertexAttribute[3];
            for (int j = 0; j < 3; j++) {
                VertexAttribute v = vertexShaderOutput[meshRenderer.mesh.elementBuffer[3 * i + j]];
                // Check if inside View Frustum.
                if (v.position.x > -1 && v.position.x < 1 &&
                        v.position.y > -1 && v.position.y < 1 &&
                        v.position.z > 0 && v.position.z < 1) {
                    isDiscarded = false;
                }
                primitiveVertices[j] = v;
            }
            // Skip when all vertices of attribute out of View Frustum.
            if (isDiscarded) {
                continue;
            }
            // Translate to NDC;
            for (int j = 0; j < 3; j++) {
                VertexAttribute v = primitiveVertices[j];
                v.position.x /= v.position.w;
                v.position.y /= v.position.w;
                v.position.z /= v.position.w;

                int x = (int) ((frameBuffer.getWidth() / 2) * (1 + v.position.x));
                int y = (int) ((frameBuffer.getHeight() / 2) * (1 + v.position.y));
                frameBuffer.drawPixel(x, y, ColorRGBA.WHITE);
            }
        }

    }
}
