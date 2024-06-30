package com.example.renderer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JFrame;

import com.example.data.Image;

public class Screen {
    private JFrame frame;
    private Canvas canvas;
    private BufferedImage bufferedImage;
    private BufferStrategy bufferStrategy;
    private byte[] bufferedImageData;

    public Screen(int width, int height, String title) {
        canvas = new Canvas();

        Dimension size = new Dimension(width, height);
        canvas.setPreferredSize(size);
        canvas.setMaximumSize(size);
        canvas.setMinimumSize(size);
        canvas.setFocusable(true);

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(width, height);
        frame.setTitle(title);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
        centerScreen();

        canvas.requestFocus();

        // 双缓冲
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        bufferedImageData = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
    }

    public void swapBuffer(Image frameBuffer, int fps) {
        // 复制frameBuffer内容到BufferedImage中
        int width = frameBuffer.getWidth();
        int height = frameBuffer.getHeight();
        byte[] frameBufferData = frameBuffer.getData();
        int length = width * height;

        for (int i = 0; i < length; i++) {
            int displayDataIndex = i * 3;
            int imageDataIndex = i * 4;
            // DisplayData 的图片格式为BGR
            bufferedImageData[displayDataIndex + 0] = frameBufferData[imageDataIndex + 2]; // Blue
            bufferedImageData[displayDataIndex + 1] = frameBufferData[imageDataIndex + 1]; // Green
            bufferedImageData[displayDataIndex + 2] = frameBufferData[imageDataIndex + 0]; // Red
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
        graphics.setColor(Color.WHITE);
        graphics.drawString("FPS: " + fps, 2, 16);
        graphics.dispose();

        bufferStrategy.show();
    }

    private void centerScreen() {
        Dimension size = frame.getSize();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - size.width) / 2;
        int y = (screen.height - size.height) / 2;
        frame.setLocation(x, y);
    }
}
