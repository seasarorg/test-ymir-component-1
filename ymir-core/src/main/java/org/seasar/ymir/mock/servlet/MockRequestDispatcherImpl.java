package org.seasar.ymir.mock.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.seasar.ymir.Path;
import org.seasar.ymir.util.ServletUtils;

public class MockRequestDispatcherImpl extends
        org.seasar.framework.mock.servlet.MockRequestDispatcherImpl implements
        MockRequestDispatcher {
    private String path_;

    public MockRequestDispatcherImpl(String path) {
        path_ = path;
    }

    public String getPath() {
        return path_;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void forward(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Path p = new Path(path_, request.getCharacterEncoding());
        for (Map.Entry<String, String[]> entry : p.getParameterMap().entrySet()) {
            for (String value : entry.getValue()) {
                ServletUtils.addParameter(entry.getKey(), value, parameterMap,
                        null);
            }
        }

        // TODO リクエストにFORWARD固有の属性の設定を行なうようにしよう。
    }
}
