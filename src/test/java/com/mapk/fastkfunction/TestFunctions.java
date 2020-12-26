package com.mapk.fastkfunction;

public class TestFunctions {
    private final int value;

    public TestFunctions(int value) {
        this.value = value;
    }

    public static TestFunctions of(int value) {
        return new TestFunctions(value);
    }

    public int getValue() {
        return value;
    }
}
