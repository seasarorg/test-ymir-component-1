package org.seasar.ymir.zpt;

import net.skirnir.freyja.zpt.tales.JavaTypePrefixHandler;

public class YmirJavaTypePrefixHandler extends JavaTypePrefixHandler {
    public YmirJavaTypePrefixHandler() {
        super();
        addFunction(YmirFunction.class);
    }
}
