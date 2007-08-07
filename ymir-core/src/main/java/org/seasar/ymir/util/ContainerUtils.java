package org.seasar.ymir.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.seasar.cms.pluggable.util.PluggableUtils;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;

public class ContainerUtils {
    protected ContainerUtils() {
    }

    public static Object[] findAllAndAscendantComponents(S2Container container,
            Object componentKey) {
        ComponentDef[] componentDefs = findAllAndAscendantComponentDefs(
                container, componentKey);

        Class clazz;
        if (componentKey instanceof Class) {
            clazz = (Class) componentKey;
        } else {
            clazz = Object.class;
        }
        Object[] objs = (Object[]) Array.newInstance(clazz,
                componentDefs.length);
        for (int i = 0; i < objs.length; i++) {
            objs[i] = componentDefs[i].getComponent();
        }
        return objs;
    }

    public static ComponentDef[] findAllAndAscendantComponentDefs(
            S2Container container, Object componentKey) {
        synchronized (container.getRoot()) {
            Set<ComponentDef> componentDefSet = new LinkedHashSet<ComponentDef>();
            componentDefSet.addAll(Arrays.asList(container
                    .findAllComponentDefs(componentKey)));
            componentDefSet.addAll(Arrays.asList(PluggableUtils
                    .findAscendantComponentDefs(container, componentKey)));
            return componentDefSet.toArray(new ComponentDef[0]);
        }
    }
}
