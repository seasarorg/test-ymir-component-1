package org.seasar.ymir.util;

import org.seasar.ymir.Request;

public class MessagesUtils {
    protected MessagesUtils() {
    }

    public static String getPageName(Request request) {
        if (request == null) {
            return null;
        } else {
            return request.getCurrentDispatch().getPageComponentName();
        }
    }

    public static String getPageSpecificName(String name, String pageName) {
        if (pageName == null) {
            return null;
        }

        int dot = name.indexOf('.');
        if (dot < 0) {
            return pageName + "." + name;
        } else {
            return name.substring(0, dot) + "." + pageName
                    + name.substring(dot);
        }
    }
}
