package org.seasar.ymir.hotdeploy.fitter.impl;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class MapFitter extends AbstractFitter<Map> {
    public Class<Map> getTargetClass() {
        return Map.class;
    }

    public void fitContent(Map value) {
        Map map = new LinkedHashMap();
        for (Iterator<Map.Entry> itr = value.entrySet().iterator(); itr
                .hasNext();) {
            Map.Entry entry = itr.next();
            map.put(getHotdeployManager().fit(entry.getKey()),
                    getHotdeployManager().fit(entry.getValue()));
        }
        value.clear();
        for (Iterator<Map.Entry> itr = map.entrySet().iterator(); itr.hasNext();) {
            Map.Entry entry = itr.next();
            value.put(entry.getKey(), entry.getValue());
        }
    }
}
