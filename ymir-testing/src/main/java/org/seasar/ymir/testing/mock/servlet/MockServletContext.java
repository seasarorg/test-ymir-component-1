package org.seasar.ymir.testing.mock.servlet;

import org.seasar.kvasir.util.io.Resource;

public interface MockServletContext extends
        org.seasar.framework.mock.servlet.MockServletContext {
    void setRoot(Resource root);
}
