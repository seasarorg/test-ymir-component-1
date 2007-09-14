package com.example.web;

public class ForwardResponseITestPage {
    public String _get() {
        return "/forwardResponseITest2.html?param2=value2";
    }

    public String _post() {
        return "proceed:/forwardResponseITest2.html?param2=value2";
    }
}
