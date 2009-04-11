package org.seasar.ymir.checkbox;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;

public class CheckboxInterceptor extends AbstractYmirProcessInterceptor {
    private ApplicationManager applicationManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Override
    public void filterParameterMap(HttpServletRequest httpRequest, String path,
            Dispatcher dispatcher, Map<String, String[]> parameterMap) {
        String key = getKey();
        String[] checkboxes = parameterMap.get(key);
        parameterMap.remove(key);
        if (checkboxes != null) {
            for (String checkbox : new HashSet<String>(Arrays
                    .asList(checkboxes))) {
                if (!parameterMap.containsKey(checkbox)) {
                    parameterMap.put(checkbox, new String[0]);
                }
            }
        }
    }

    String getKey() {
        return applicationManager_.findContextApplication().getProperty(
                Globals.APPKEY_CORE_CHECKBOX_KEY,
                Globals.DEFAULT_CORE_CHECKBOX_KEY);
    }
}
