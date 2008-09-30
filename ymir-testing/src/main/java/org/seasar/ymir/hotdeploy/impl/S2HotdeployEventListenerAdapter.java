package org.seasar.ymir.hotdeploy.impl;

import org.seasar.ymir.hotdeploy.HotdeployEventListener;

public class S2HotdeployEventListenerAdapter implements
        org.seasar.cms.pluggable.hotdeploy.HotdeployEventListener {
    private HotdeployEventListener listener_;

    public S2HotdeployEventListenerAdapter(HotdeployEventListener listener) {
        listener_ = listener;
    }

    public void start() {
        listener_.start();
    }

    public void stop() {
        listener_.stop();
    }
}
