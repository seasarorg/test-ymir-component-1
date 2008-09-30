package org.seasar.ymir.mock.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.seasar.ymir.util.ServletUtils;

public class MockRequestDispatcherImpl extends
        org.seasar.framework.mock.servlet.MockRequestDispatcherImpl implements
        MockRequestDispatcher {
    private String path_;

    public MockRequestDispatcherImpl(String path) {
        path_ = path;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void forward(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        int question = path_.indexOf('?');
        if (question >= 0) {
            MockHttpServletRequestImpl req = (MockHttpServletRequestImpl) request;
            Map<String, String[]> parameterMap = req.getParameterMap();
            Map<String, String[]> originalParameterMap = new HashMap<String, String[]>(
                    parameterMap);
            parameterMap.clear();
            parameterMap.putAll(ServletUtils.parseParameters(path_
                    .substring(question + 1), req.getCharacterEncoding()));
            for (Iterator<Map.Entry<String, String[]>> itr = originalParameterMap
                    .entrySet().iterator(); itr.hasNext();) {
                Entry<String, String[]> entry = itr.next();
                String key = entry.getKey();
                String[] originalValue = entry.getValue();
                String[] curValue = parameterMap.get(key);
                if (curValue == null) {
                    parameterMap.put(key, originalValue);
                } else {
                    String[] newValue = new String[curValue.length
                            + originalValue.length];
                    System.arraycopy(curValue, 0, newValue, 0, curValue.length);
                    System.arraycopy(originalValue, 0, newValue,
                            curValue.length, originalValue.length);
                    parameterMap.put(key, newValue);
                }
            }
        }
    }
}
