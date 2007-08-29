package org.seasar.ymir.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.seasar.cms.pluggable.util.PluggableUtils;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.ymir.YmirContext;

public class ContainerUtils {
    private static S2Container container_;

    protected ContainerUtils() {
    }

    // for test
    static void setS2Container(S2Container container) {
        container_ = container;
    }

    public static S2Container getS2Container() {
        if (container_ != null) {
            return container_;
        } else {
            return YmirContext.getYmir().getApplication().getS2Container();
        }
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

    @SuppressWarnings("unchecked")
    public static <T> T[] merge(T[] a, T[] b) {
        if (b == null || b.length == 0) {
            return a;
        } else if (a == null || a.length == 0) {
            return b;
        }

        Set<T> set = new LinkedHashSet<T>();
        for (int i = 0; i < a.length; i++) {
            set.add(a[i]);
        }
        for (int i = 0; i < b.length; i++) {
            set.add(b[i]);
        }
        return set.toArray((T[]) Array.newInstance(a.getClass()
                .getComponentType(), 0));
    }
}
