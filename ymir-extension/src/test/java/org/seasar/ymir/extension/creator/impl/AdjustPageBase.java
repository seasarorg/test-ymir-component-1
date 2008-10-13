package org.seasar.ymir.extension.creator.impl;

public class AdjustPageBase {
    @org.seasar.ymir.annotation.Meta(name = "source", value = "return \"redirect:/login.html\";")
    public String _get() {
        return "redirect:/login.html";
    }
}
