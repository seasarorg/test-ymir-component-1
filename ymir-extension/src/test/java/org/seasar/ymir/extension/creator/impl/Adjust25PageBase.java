package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.scope.annotation.RequestParameter;

public class Adjust25PageBase {
    @Meta(name = "bornOf", value = "/adjust25.html")
    public static final String P_param1$value1 = "param1.value1";

    @Meta(name = "bornOf", value = "/fuga.html")
    public static final String P_param1$value2 = "param1.value2";

    @Meta(name = "bornOf", value = "/fuga.html")
    public static final String P_param1$value3 = "param1.value3";

    protected String param1_;

    @RequestParameter
    public void setParam1(String param1) {
        param1_ = param1;
    }
}
