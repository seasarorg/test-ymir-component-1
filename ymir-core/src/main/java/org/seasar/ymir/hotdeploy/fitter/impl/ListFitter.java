package org.seasar.ymir.hotdeploy.fitter.impl;

import java.util.List;
import java.util.ListIterator;

@SuppressWarnings("unchecked")
public class ListFitter extends AbstractFitter<List> {
    public Class<List> getTargetClass() {
        return List.class;
    }

    public void fitContent(List value) {
        for (ListIterator itr = value.listIterator(); itr.hasNext();) {
            itr.set(getHotdeployManager().fit(itr.next()));
        }
    }
}
