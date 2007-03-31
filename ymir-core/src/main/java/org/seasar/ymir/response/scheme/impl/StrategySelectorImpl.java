package org.seasar.ymir.response.scheme.impl;

import java.util.HashMap;
import java.util.Map;

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

    public void setStrategies(Strategy[] strategies) {

        for (int i = 0; i < strategies.length; i++) {
            add(strategies[i]);
        }
    }
}
