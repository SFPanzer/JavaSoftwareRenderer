package com.example.data;

public class FloatUtils {
    public static final float EPSILON = 1e-2f;

    public static boolean equals(float a, float b) {
        return Math.abs(a - b) <= EPSILON;
    }
}
