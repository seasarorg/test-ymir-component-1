package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;

public class AdjustPageBase {
    @Metas({@Meta(name = "bornOf", value = "/login.html"), @Meta(name = "source", value = "return \"/login.html\";")})
    public String _get() {
        return "redirect:/login.html";
    }
}
