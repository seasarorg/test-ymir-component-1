package com.example.web;

public class YmirTestCaseTestPage {
    private String message;

    public String _get() {
        return "redirect:/ymirTestCaseTest.html?action";
    }

    public void _get_action() {
        message = "_get_action has been called";
    }

    public String getMessage() {
        return message;
    }
}
