package org.seasar.ymir.extension.freemarker;

import org.seasar.ymir.annotation.Meta;

public class AbstractHoe12Page {
    @Meta(name = "property", value = "hoe12Form")
    protected Hoe12FormDto hoe12Form_;

    public Hoe12FormDto getHoe12Form() {
        return hoe12Form_;
    }

    public void setHoe12orm(Hoe12FormDto hoe12Form) {
        this.hoe12Form_ = hoe12Form;
    }
}
