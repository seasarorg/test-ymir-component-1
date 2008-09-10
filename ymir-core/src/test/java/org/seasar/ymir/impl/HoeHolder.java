package org.seasar.ymir.impl;

import org.seasar.ymir.constraint.annotation.Required;
import org.seasar.ymir.constraint.annotation.Requireds;

@Hoe("1")
@HoeAlias("2")
@HoeAliass( { @HoeAlias("3"), @HoeAlias("4") })
@HoeAliasAlias("5")
@Required
@Requireds(@Required)
@RequiredAlias
public class HoeHolder {
    public void hoe(@Hoe("1")
    @HoeAlias("2")
    @HoeAliass( { @HoeAlias("3"), @HoeAlias("4") })
    @HoeAliasAlias("5")
    @RequiredAlias
    String param) {
    }
}
