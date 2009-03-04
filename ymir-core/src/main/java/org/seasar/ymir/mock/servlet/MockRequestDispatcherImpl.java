package org.seasar.ymir.mock.servlet;

import static org.seasar.ymir.util.ServletUtils.ATTR_FORWARD_CONTEXT_PATH;
import static org.seasar.ymir.util.ServletUtils.ATTR_FORWARD_PATH_INFO;
import static org.seasar.ymir.util.ServletUtils.ATTR_FORWARD_QUERY_STRING;
import static org.seasar.ymir.util.ServletUtils.ATTR_FORWARD_SERVLET_PATH;
import static org.seasar.ymir.util.ServletUtils.ATTR_INCLUDE_CONTEXT_PATH;
import static org.seasar.ymir.util.ServletUtils.ATTR_INCLUDE_PATH_INFO;
import static org.seasar.ymir.util.ServletUtils.ATTR_INCLUDE_QUERY_STRING;
import static org.seasar.ymir.util.ServletUtils.ATTR_INCLUDE_SERVLET_PATH;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.seasar.ymir.Path;
import org.seasar.ymir.util.ServletUtils;

public class MockRequestDispatcherImpl extends
        org.seasar.framework.mock.servlet.MockRequestDispatcherImpl implements
        MockRequestDispatcher {
    private String path_;

    public MockRequestDispatcherImpl(String path, HttpServletRequest request) {
        if (request == null) {
            path_ = path;
        } else {
            Path p = new Path(path);
            String trunk = p.getTrunk();
            if (trunk.startsWith("/")) {
                path_ = path;
            } else {
                path_ = ServletUtils.toAbsolutePath(ServletUtils
                        .getNativePath(request), path);
            }
        }
        // 念のため。
        if (path_.length() == 0) {
            path_ = "/";
        }
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

        if (request instanceof MockHttpServletRequest) {
            MockHttpServletRequest req = (MockHttpServletRequest) request;
            req.setAttribute(ATTR_FORWARD_CONTEXT_PATH, req.getContextPath());
            req.setAttribute(ATTR_FORWARD_PATH_INFO, req.getPathInfo());
            req.setAttribute(ATTR_FORWARD_SERVLET_PATH, req.getServletPath());
            req.setAttribute(ATTR_FORWARD_QUERY_STRING, req.getQueryString());

            Path path = new Path(path_);
            req.setServletPath(path.getTrunk());
            req.setQueryString(path.getQueryString());
        }
    }

    public void include(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        if (request instanceof MockHttpServletRequest) {
            Path path = new Path(path_);
            MockHttpServletRequest req = (MockHttpServletRequest) request;
            req.setAttribute(ATTR_INCLUDE_CONTEXT_PATH, req.getContextPath());
            req.setAttribute(ATTR_INCLUDE_PATH_INFO, null);
            req.setAttribute(ATTR_INCLUDE_SERVLET_PATH, path.getTrunk());
            req.setAttribute(ATTR_INCLUDE_QUERY_STRING, path.getQueryString());
        }
    }
}
