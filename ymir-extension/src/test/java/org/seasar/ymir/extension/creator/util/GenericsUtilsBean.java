package org.seasar.ymir.extension.creator.util;

import java.util.List;

public class GenericsUtilsBean {
    public List<String>[] getList() {
        return null;
    }

    public String getValue1() {
        return null;
    }

    public String[] getValue2() {
        return null;
    }

    public List<String> getValue3() {
        return null;
    }

    public <T> T getValue4() {
        return null;
    }

    public <T extends List<String>> T getValue5() {
        return null;
    }

    public <T extends List<String>> T[] getValue6() {
        return null;
    }

    public Class<?> getValue7() {
        return null;
    }

    public Class<? extends String> getValue8() {
        return null;
    }
}
