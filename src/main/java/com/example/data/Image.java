package com.example.data;

public class Image {
    protected final int width;
    protected final int height;
    protected final byte[] data;

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        this.data = new byte[width * height * 4];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public byte[] getData() {
        return data;
    }

    public void fill(ColorRGBA color) {
        for (int i = 0; i < width * height; i++) {
            int index = i * 4;
            data[index] = color.r;
            data[index + 1] = color.g;
            data[index + 2] = color.b;
            data[index + 3] = color.a;
        }
    }

    public void drawPixel(int x, int y, ColorRGBA color) {
        if (x >= width || x < 0 || y >= height || y < 0) {
            return;
        }

        int index = (y * width + x) * 4;
        data[index] = color.r;
        data[index + 1] = color.g;
        data[index + 2] = color.b;
        data[index + 3] = color.a;
    }
}
