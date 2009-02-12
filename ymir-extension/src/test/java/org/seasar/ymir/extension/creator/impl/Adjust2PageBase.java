package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.annotation.Meta;

public class Adjust2PageBase {
    @Meta(name = "bornOf", classValue = { Adjust2PageBase.class,
        Adjust2EditPageBase.class })
    protected String param1_;

    @Meta(name = "bornOf", classValue = Adjust2PageBase.class)
    protected String param2_;

    protected String param3_;

    @Meta(name = "bornOf", classValue = Adjust2PageBase.class)
    public String getParam1() {
        return param1_;
    }

    @Meta(name = "bornOf", classValue = Adjust2EditPageBase.class)
    public void setParam1(String param1) {
        param1_ = param1;
    }

    @Meta(name = "bornOf", classValue = Adjust2PageBase.class)
    public String getParam2() {
        return param2_;
    }

    @Meta(name = "bornOf", classValue = Adjust2PageBase.class)
    public void setParam2(String param2) {
        param2_ = param2;
    }

    public String getParam3() {
        return param3_;
    }

    public void setParam3(String param3) {
        param3_ = param3;
    }
}
