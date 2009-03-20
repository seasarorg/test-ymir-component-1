package org.seasar.ymir.history.impl;

import java.util.Map;

import org.seasar.ymir.Path;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.history.HistoryElement;

public class HistoryElementImpl implements HistoryElement {
    private static final long serialVersionUID = -4130580172801001752L;

    private Path path_;

    public Path getPath() {
        return path_;
    }

    public void setPath(Path path) {
        path_ = path;
    }

    public Class<?> getPageClass() {
        return YmirContext.getYmir().getPageClassOfPath(path_.getTrunk());
    }
}
