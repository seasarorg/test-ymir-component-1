package org.seasar.ymir.hotdeploy.fitter.impl;

import java.util.Map;
import java.util.Iterator;

public class MapFitter extends AbstractFitter<Map> {
    public Class<Map> getTargetClass() {
        return Map.class;
    }

    @SuppressWarnings("unchecked")
    public Map copy(Map value) {
        Map destination = newInstance(value.getClass());
        for (Iterator<Map.Entry> itr = value.entrySet().iterator(); itr
                .hasNext();) {
            Map.Entry entry = itr.next();
            destination.put(getHotdeployManager().fit(entry.getKey()),
                    getHotdeployManager().fit(entry.getValue()));
        }
        return destination;
    }
}
