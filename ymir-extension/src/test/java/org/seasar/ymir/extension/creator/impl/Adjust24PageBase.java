package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;

public class Adjust24PageBase {
    @Meta(name = "bornOf", value = "/adjust24.html")
    public String _get() {
        return null;
    }

    @Metas( { @Meta(name = "bornOf", value = "/adjust24.html"),
        @Meta(name = "source", value = "return \"redirect:index.html\";") })
    public String _get_string() {
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
