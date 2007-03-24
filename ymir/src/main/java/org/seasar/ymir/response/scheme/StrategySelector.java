/**
 * 
 */
package org.seasar.ymir.response.scheme;


public interface StrategySelector {

    Strategy getStrategy(String scheme);
}