package com.example.utils;

public class FNV1aHash {
    private static final int FNV_PRIME = 0xFFF1;
    private static final int FNV_OFFSET_BASIS = 0x10001;

    public static int hash(byte[] input) {
        int hash = FNV_OFFSET_BASIS;
        for (byte b : input) {
            hash ^= b;
            hash *= FNV_PRIME;
        }
        return hash;
    }
}
