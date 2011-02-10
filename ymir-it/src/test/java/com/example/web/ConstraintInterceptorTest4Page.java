package com.example.web;

public class ConstraintInterceptorTest4Page {
    private boolean called = false;

    public void _get() {
        called = true;
    }

    public boolean isCalled() {
        return called;
    }
}
