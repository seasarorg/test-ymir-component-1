package org.seasar.cms.ymir.response.constructor.impl;

import org.seasar.cms.ymir.Response;
import org.seasar.cms.ymir.response.VoidResponse;
import org.seasar.cms.ymir.response.constructor.ResponseConstructor;
import org.seasar.cms.ymir.response.scheme.Strategy;
import org.seasar.cms.ymir.response.scheme.StrategySelector;
import org.seasar.cms.ymir.response.scheme.impl.ForwardStrategy;

public class StringResponseConstructor implements ResponseConstructor<String> {

    private StrategySelector strategySelector_;

    public Class<String> getTargetClass() {

        return String.class;
    }

    public Response constructResponse(Object component, String returnValue) {

        if (returnValue == null) {
            return VoidResponse.INSTANCE;
        }

        String scheme;
        String path;
        int colon = returnValue.indexOf(':');
        if (colon < 0) {
            scheme = ForwardStrategy.SCHEME;
            path = returnValue;
        } else {
            scheme = returnValue.substring(0, colon);
            path = returnValue.substring(colon + 1);
        }
        Strategy strategy = strategySelector_.getStrategy(scheme);
        if (strategy == null) {
            throw new RuntimeException("Unknown scheme '" + scheme
                    + "' is specified: " + returnValue);
        }
        return strategy.constructResponse(path, component);
    }

    public void setStrategySelector(StrategySelector strategySelector) {

        strategySelector_ = strategySelector;
    }
}
