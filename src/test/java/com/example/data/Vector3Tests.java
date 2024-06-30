package com.example.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class Vector3Tests {
    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/data/Vector3/Cross.csv", numLinesToSkip = 1)
    public void testCross(float a_x, float a_y, float a_z,
            float b_x, float b_y, float b_z,
            float expect_x, float expect_y, float expect_z) {
        Vector3 vectorA = new Vector3(a_x, a_y, a_z);
        Vector3 vectorB = new Vector3(b_x, b_y, b_z);
        Vector3 expect = new Vector3(expect_x, expect_y, expect_z);

        Vector3 actual = Vector3.cross(vectorA, vectorB);

        assertEquals(expect, actual);
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/data/Vector3/Dot.csv", numLinesToSkip = 1)
    public void testDot(float a_x, float a_y, float a_z,
            float b_x, float b_y, float b_z,
            float expect) {
        Vector3 vectorA = new Vector3(a_x, a_y, a_z);
        Vector3 vectorB = new Vector3(b_x, b_y, b_z);

        float actual = Vector3.dot(vectorA, vectorB);

        assertEquals(expect, actual);
    }
}
