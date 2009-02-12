package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.annotation.Meta;

public class Adjust3PageBase {
    @Meta(name = "bornOf", classValue = { Adjust3PageBase.class,
        Adjust3EditPageBase.class })
    public void _get() {
    }

    @Meta(name = "bornOf", classValue = Adjust3PageBase.class)
    public void _get_output() {
    }

    public void _get_list() {
    }
}
