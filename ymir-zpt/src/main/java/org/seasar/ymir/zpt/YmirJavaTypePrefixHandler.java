package org.seasar.ymir.zpt;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.util.ContainerUtils;

import net.skirnir.freyja.zpt.tales.JavaTypePrefixHandler;

public class YmirJavaTypePrefixHandler extends JavaTypePrefixHandler {
    public YmirJavaTypePrefixHandler() {
        addFunction(YmirFunction.class);
        addUserDefinedFunctions();
    }

    void addUserDefinedFunctions() {
        for (ComponentDef componentDef : ContainerUtils.findAllComponentDefs(
                getS2Container(), Functions.class)) {
            addFunction(componentDef.getComponentClass());
        }
    }

    S2Container getS2Container() {
        return YmirContext.getYmir().getApplication().getS2Container();
    }
}
