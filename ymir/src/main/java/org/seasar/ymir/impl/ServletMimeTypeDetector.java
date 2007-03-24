package org.seasar.ymir.impl;

import javax.servlet.ServletContext;

import org.seasar.ymir.MimeTypeDetector;
import org.seasar.framework.container.S2Container;

public class ServletMimeTypeDetector implements MimeTypeDetector {

    private S2Container container_;

    public void setContainer(S2Container container) {

        container_ = container;
    }

    public String getMimeType(String name) {

        Object application = container_.getRoot().getExternalContext()
                .getApplication();
        if (application instanceof ServletContext) {
            return ((ServletContext) application).getMimeType(name);
        } else {
            return null;
        }
    }
}
