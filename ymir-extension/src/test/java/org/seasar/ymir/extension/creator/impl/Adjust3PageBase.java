package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.annotation.Meta;

public class Adjust3PageBase {
    @Meta(name = "bornOf", value = { "/adjust3.html", "/adjust3Edit.html" })
    public void _get() {
    }

    @Meta(name = "bornOf", value = "/adjust3.html")
    public void _get_output() {
    }

    public void _get_list() {
    }
}
