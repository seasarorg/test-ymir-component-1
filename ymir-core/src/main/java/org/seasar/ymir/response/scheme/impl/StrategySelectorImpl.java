package org.seasar.ymir.response.scheme.impl;

import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.response.scheme.Strategy;
import org.seasar.ymir.response.scheme.StrategySelector;

public class StrategySelectorImpl implements StrategySelector {

    private Map<String, Strategy> strategies_ = new HashMap<String, Strategy>();

    public Strategy getStrategy(String scheme) {

        Strategy strategy = strategies_.get(scheme);
        if (strategy != null) {
            return strategy;
        } else {
            throw new RuntimeException("Strategy does not exist for scheme: "
                    + scheme);
        }
    }

    public void add(Strategy strategy) {

        strategies_.put(strategy.getScheme(), strategy);
    }

    @Binding(value = "@org.seasar.ymir.util.ContainerUtils@findAllComponents(container, @org.seasar.ymir.response.scheme.Strategy@class)", bindingType = BindingType.MUST)
    public void setStrategies(Strategy[] strategies) {

        for (int i = 0; i < strategies.length; i++) {
            add(strategies[i]);
        }
    }
}
