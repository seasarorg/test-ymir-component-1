package org.seasar.ymir.extension.freemarker;

import org.seasar.ymir.render.Selector;
import org.seasar.ymir.render.html.Select;

public class Hoe9PageBase {
    protected Selector hoeSelector_;

    protected Select fugaSelect_;

    public Selector getHoeSelector() {
        return hoeSelector_;
    }

    public void setHoeSelector(Selector hoeSelector) {
        hoeSelector_ = hoeSelector;
    }

    public Select getFugaSelect() {
        return fugaSelect_;
    }

    public void setFugaSelect(Select fugaSelect) {
        fugaSelect_ = fugaSelect;
    }
}
