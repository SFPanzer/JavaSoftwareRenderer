package com.example.application;

import com.example.Scene.Scene;
import com.example.renderer.RenderContext;
import com.example.renderer.Screen;

public abstract class Application {
    protected int width;
    protected int height;
    protected String title;

    private Screen screen;
    private RenderContext renderContext;
    private boolean isRunning;

    private int framePerSecond;
    private final static int FRAME_DELTA_QUEUE_LENGTH = 32;
    private float[] frameDeltaQueue = new float[FRAME_DELTA_QUEUE_LENGTH];

    private Scene scene;

    public Application() {
        width = 800;
        height = 600;
        title = "Software Renderer";
        isRunning = true;
    }

    public void launch() {
        long startTime = System.nanoTime();
        long previousTime = System.nanoTime();
        long deltaTime;
        float delta;

        screen = new Screen(width, height, title);
        renderContext = new RenderContext(width, height);

        scene = new Scene();

        initialize();
        scene.start();

        while (isRunning) {
            deltaTime = System.nanoTime() - previousTime;
            previousTime = System.nanoTime();
            delta = deltaTime / 1000000000.0f;

            updateFramePerSecond(delta);
            update(delta);
            scene.update(delta);
            render(delta);
        }

        long totalTime = System.nanoTime() - startTime;
        System.out.printf("运行总时间：" + totalTime / 1000000000.0f);
    }

    public void stop() {
        isRunning = false;
    }

    protected abstract void initialize();

    protected abstract void update(float delta);

    protected void render(float delta) {
        screen.swapBuffer(renderContext.frameBuffer, framePerSecond);
    }

    private void updateFramePerSecond(float delta) {
        for (int i = 0; i < FRAME_DELTA_QUEUE_LENGTH - 1; i++) {
            frameDeltaQueue[i] = frameDeltaQueue[i + 1];
        }
        frameDeltaQueue[FRAME_DELTA_QUEUE_LENGTH - 1] = delta;

        int count = 0;
        float sum = 0;
        for (int i = 0; i < FRAME_DELTA_QUEUE_LENGTH; i++) {
            if (frameDeltaQueue[i] > 0) {
                count++;
                sum += frameDeltaQueue[i];
            }
        }

        framePerSecond = (int) (count / sum);
    }

    public Scene getScene() {
        return scene;
    }

    public RenderContext getRenderContext() {
        return renderContext;
    }
}
