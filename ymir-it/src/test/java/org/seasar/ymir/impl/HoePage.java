package org.seasar.ymir.impl;

import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.annotation.In;
import org.seasar.ymir.annotation.Out;
import org.seasar.ymir.annotation.Protected;
import org.seasar.ymir.scope.impl.SessionScope;

public class HoePage {
    public void setMap(Map map) {
    }

    public void setMaps(Map[] maps) {
    }

    public void setString(String string) {
    }

    public void setStrings(String[] strings) {
    }

    @Protected
    public void setProtected(String protecteds) {
    }

    @In(SessionScope.class)
    public void setIn(String in) {
    }

    @Binding("component")
    public void setComponent(String component) {
    }

    @Out(SessionScope.class)
    public String getOut() {
        return null;
    }

    public void setOut(String out) {
    }

    public void setFile(FormFile file) {
    }

    public void setFiles(FormFile[] files) {
    }
}
