package org.seasar.ymir.json;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.seasar.ymir.Request;
import org.seasar.ymir.RequestWrapper;
import org.seasar.ymir.TypeConversionManager;

import net.sf.json.JSONObject;

public class JSONRequest extends RequestWrapper {
    private Map<String, String[]> parameterMap_;

    public JSONRequest(Request request, JSONObject jsonObject,
            TypeConversionManager typeConversionManager) {
        super(request);
        Map<String, String[]> map = new LinkedHashMap<String, String[]>(request
                .getParameterMap());
        for (Iterator<?> itr = jsonObject.keys(); itr.hasNext();) {
            String name = (String) itr.next();
            Object value = jsonObject.get(name);
            map.put(name, typeConversionManager.convert(value, String[].class));
        }
        parameterMap_ = Collections.unmodifiableMap(map);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameterMap_;
    }

    @Override
    public Iterator<String> getParameterNames() {
        return parameterMap_.keySet().iterator();
    }

    @Override
    public String getParameter(String name) {
        return getParameter(name, null);
    }

    @Override
    public String getParameter(String name, String defaultValue) {
        String[] values = (String[]) parameterMap_.get(name);
        if (values != null && values.length > 0) {
            return values[0];
        } else {
            return defaultValue;
        }
    }

    @Override
    public String[] getParameterValues(String name) {
        return getParameterValues(name, null);
    }

    @Override
    public String[] getParameterValues(String name, String[] defaultValues) {
        String[] values = (String[]) parameterMap_.get(name);
        if (values != null) {
            return values;
        } else {
            return defaultValues;
        }
    }
}
