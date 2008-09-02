package org.seasar.ymir.creator;

import org.seasar.framework.convention.NamingConvention;

// TODO [YMIR-1.0] PageコンポーネントにはS2以外の機構（Scope）もオブジェクトをDIするため、混乱しないように
// autoBindingをNONEにする。S2からDIしたい場合はBindingを明示的に書くか、@Injectを使うようにする。
public class PageCreator extends
        org.seasar.framework.container.creator.PageCreator {

    public PageCreator(NamingConvention namingConvention) {
        super(namingConvention);
        setExternalBinding(false);
    }
}
