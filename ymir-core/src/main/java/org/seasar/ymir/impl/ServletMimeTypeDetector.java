package org.seasar.ymir.impl;

import javax.servlet.ServletContext;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.MimeTypeDetector;
import org.seasar.ymir.util.ContainerUtils;

public class ServletMimeTypeDetector implements MimeTypeDetector {

    private S2Container container_;

    public void setContainer(S2Container container) {

        container_ = container;
    }

    public String getMimeType(String name) {

        ServletContext application = ContainerUtils
                .getServletContext(container_.getRoot());
        if (application != null) {
            return application.getMimeType(name);
        } else {
            return null;
        }
    }
}
