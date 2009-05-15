package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;

public class Adjust24PageBase {
    @Metas( { @Meta(name = "bornOf", value = "/adjust24.html"),
        @Meta(name = "source", value = "return \"redirect:index.html\";") })
    public String _get() {
        return "redirect:index.html";
    }

    @Meta(name = "bornOf", value = "/adjust24.html")
    public void _get_two() {
    }

    @Metas( { @Meta(name = "bornOf", value = "/adjust24.html"),
        @Meta(name = "source", value = "return \"redirect:index.html\";") })
    public String _post() {
        return "redirect:index.html";
    }
}
