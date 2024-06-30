package com.example.component;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.example.data.Matrix4x4;
import com.example.data.Vector3;

public class TransformTests {
    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/component/Transform/GetLocalToWorldMatrix.csv", numLinesToSkip = 1)
    public void testGetLocalToWorldMatrix(float positionX, float positionY, float positionZ,
            float rotationX, float rotationY, float rotationZ,
            float scaleX, float scaleY, float scaleZ,
            float trs11, float trs12, float trs13, float trs14,
            float trs21, float trs22, float trs23, float trs24,
            float trs31, float trs32, float trs33, float trs34,
            float trs41, float trs42, float trs43, float trs44) {
        Transform transform = new Transform(
                new Vector3(positionX, positionY, positionZ),
                new Vector3(rotationX, rotationY, rotationZ),
                new Vector3(scaleX, scaleY, scaleZ));

        var expect = new Matrix4x4(trs11, trs12, trs13, trs14,
                trs21, trs22, trs23, trs24,
                trs31, trs32, trs33, trs34,
                trs41, trs42, trs43, trs44);

        var actual = transform.getLocalToWorldMatrix();

        assertEquals(expect, actual);
    }
}
