package org.seasar.ymir.hotdeploy.fitter.impl;

import java.util.Collection;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public class CollectionFitter extends AbstractFitter<Collection> {
    public Class<Collection> getTargetClass() {
        return Collection.class;
    }

    @SuppressWarnings("unchecked")
    public Collection copy(Collection value) {
        Collection destination = newInstance(value.getClass());
        for (Iterator itr = value.iterator(); itr.hasNext();) {
            destination.add(getHotdeployManager().fit(itr.next()));
        }
        return destination;
    }
}
