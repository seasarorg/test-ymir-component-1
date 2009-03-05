package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;

public class Adjust2PageBase {
    protected String param1_;

    protected String param2_;

    protected String param3_;

    @Metas( { @Meta(name = "bornOf", value = "/adjust2.html"),
        @Meta(name = "property", value = "param5") })
    protected String param5_;

    protected String param6_;

    @Meta(name = "bornOf", value = "/adjust2.html")
    public String getParam1() {
        return param1_;
    }

    @Meta(name = "bornOf", value = "/adjust2Edit.html")
    public void setParam1(String param1) {
        param1_ = param1;
    }

    @Meta(name = "bornOf", value = "/adjust2.html")
    public String getParam2() {
        return param2_;
    }

    @Meta(name = "bornOf", value = "/adjust2.html")
    public void setParam2(String param2) {
        param2_ = param2;
    }

    public String getParam3() {
        return param3_;
    }

    public void setParam3(String param3) {
        param3_ = param3;
    }

    @Meta(name = "bornOf", value = "/adjust2.html")
    public String getParam5() {
        return param5_;
    }

    @Meta(name = "bornOf", value = "/adjust2.html")
    public void setParam5(String param5) {
        param5_ = param5;
    }

    @Meta(name = "bornOf", value = { "/adjust2.html", "/adjust2Edit.html" })
    public String getParam6() {
        return param6_;
    }
}
