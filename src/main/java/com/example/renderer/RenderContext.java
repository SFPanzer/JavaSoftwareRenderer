package com.example.renderer;

import java.util.ArrayList;

import com.example.component.Camera;
import com.example.component.MeshRenderer;
import com.example.data.ColorRGBA;
import com.example.data.Image;
import com.example.data.Matrix4x4;
import com.example.data.Vector3;
import com.example.data.VertexAttribute;
import com.example.shader.Shader;
import com.example.shader.Shader.Cull;

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
            // Cull
            if (meshRenderer.shader.cull != Cull.OFF) {
                Vector3 BA = Vector3.sub(primitiveVertices[0].position, primitiveVertices[1].position);
                Vector3 CA = Vector3.sub(primitiveVertices[0].position, primitiveVertices[2].position);
                Vector3 cross = Vector3.cross(BA, CA);
                if (meshRenderer.shader.cull == Cull.BACK) {
                    if (cross.z <= 0) {
                        continue;
                    }
                } else if (meshRenderer.shader.cull == Cull.FRONT) {
                    if (cross.z >= 0) {
                        continue;
                    }
                }
            }

            // Translate to NDC;
            for (int j = 0; j < 3; j++) {
                VertexAttribute v = primitiveVertices[j];
                v.position.x /= v.position.w;
                v.position.y /= v.position.w;
                v.position.z /= v.position.w;
            }
            for (int j = 0; j < 3; j++) {
                VertexAttribute v0 = primitiveVertices[j];
                int x0 = ndcToIndex(v0.position.x, frameBuffer.getWidth());
                int y0 = ndcToIndex(v0.position.y, frameBuffer.getHeight());
                VertexAttribute v1 = primitiveVertices[(j + 1) % 3];
                int x1 = ndcToIndex(v1.position.x, frameBuffer.getWidth());
                int y1 = ndcToIndex(v1.position.y, frameBuffer.getHeight());

                frameBuffer.drawLine(x0, y0, x1, y1, ColorRGBA.RED);
                frameBuffer.drawPixel(x0, y0, ColorRGBA.WHITE);
            }

            /*
             * // Rasterization.
             * {
             * // Calculate Rasterization bound.
             * int minX = ndcToIndex(primitiveVertices[0].position.x,
             * frameBuffer.getWidth());
             * int minY = ndcToIndex(primitiveVertices[0].position.y,
             * frameBuffer.getHeight());
             * int maxX = minX, maxY = minY;
             * 
             * for (int j = 1; j < 3; j++) {
             * int x = ndcToIndex(primitiveVertices[j].position.x, frameBuffer.getWidth());
             * int y = ndcToIndex(primitiveVertices[j].position.y, frameBuffer.getHeight());
             * maxX = x > maxX ? x : maxX;
             * maxY = y > maxY ? y : maxY;
             * minX = x < minX ? x : minX;
             * minY = x < minY ? x : minY;
             * }
             * 
             * minX = minX > 0 ? minX : 0;
             * minY = minY > 0 ? minY : 0;
             * maxX = maxX < frameBuffer.getWidth() - 1 ? maxX : frameBuffer.getWidth() - 1;
             * maxY = maxY < frameBuffer.getHeight() - 1 ? maxY : frameBuffer.getHeight() -
             * 1;
             * 
             * // Traverse the pixels.
             * for (int y = minY; y <= maxY; y++) {
             * for (int x = minX; x <= maxX; x++) {
             * frameBuffer.drawPixel(x, y, ColorRGBA.RED);
             * float ndcX = indexToNdc(x, frameBuffer.getWidth());
             * float ndcY = indexToNdc(y, frameBuffer.getHeight());
             * float ndcZ = 0;
             * Vector4 pixelNDC = new Vector4(ndcX, ndcY, ndcZ, 1);
             * 
             * Vector3 BA = Vector3.sub(primitiveVertices[0].position,
             * primitiveVertices[1].position);
             * Vector3 CB = Vector3.sub(primitiveVertices[1].position,
             * primitiveVertices[2].position);
             * Vector3 AC = Vector3.sub(primitiveVertices[2].position,
             * primitiveVertices[0].position);
             * 
             * Vector3 AP = Vector3.sub(pixelNDC, primitiveVertices[0].position);
             * Vector3 BP = Vector3.sub(pixelNDC, primitiveVertices[1].position);
             * Vector3 CP = Vector3.sub(pixelNDC, primitiveVertices[2].position);
             * 
             * if (Vector3.cross(AC, AP).z > 0 &&
             * Vector3.cross(BA, BP).z > 0 &&
             * Vector3.cross(CB, CP).z > 0) {
             * 
             * }
             * }
             * }
             * }
             */
        }
    }

    int ndcToIndex(float coordinate, int size) {
        return (int) ((size / 2) * (1 + coordinate));
    }

    float indexToNdc(int index, int size) {
        return (index - size / 2) / (size / 2);
    }
}
