package org.seasar.ymir.skeleton;

import java.util.HashMap;
import java.util.Map;

import org.seasar.ymir.vili.Configurator;
import org.seasar.ymir.vili.ViliBehavior;
import org.seasar.ymir.vili.ViliProjectPreferences;

public class ConfiguratorImpl implements Configurator {
    public Map<String, Object> createAdditionalParameters(
            ViliBehavior behavior, ViliProjectPreferences preferences) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        return parameters;
    }
}
