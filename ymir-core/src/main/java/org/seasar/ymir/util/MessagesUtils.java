package org.seasar.ymir.util;

import java.util.ArrayList;
import java.util.List;

import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.Request;

public class MessagesUtils {
    private static final String[] EMPTY_STRING = new String[0];

    protected MessagesUtils() {
    }

    /**
     * @deprecated {@link #getPageNames(Request)}を使って下さい。
     */
    public static String getPageName(Request request) {
        String[] pageNames = getPageNames(request);
        if (pageNames.length == 0) {
            return null;
        } else {
            return pageNames[0];
        }
    }

    /**
     * @since 1.0.7
     */
    public static String[] getPageNames(Request request) {
        if (request == null) {
            return EMPTY_STRING;
        } else {
            Dispatch dispatch = request.getCurrentDispatch();
            String pageName = dispatch.getPageComponentName();
            if (dispatch.getDispatcher() == Dispatcher.REQUEST) {
                if (pageName == null) {
                    return EMPTY_STRING;
                } else {
                    return new String[] { pageName };
                }
            } else {
                List<String> pageNames = new ArrayList<String>();
                if (pageName != null) {
                    pageNames.add(pageName);
                }
                pageName = request.getRequestDispatch().getPageComponentName();
                if (pageName != null) {
                    pageNames.add(pageName);
                }
                return pageNames.toArray(EMPTY_STRING);
            }
        }
    }

    public static String[] getMessageNameCandidates(String name,
            String[] pageNameCandidates) {
        List<String> candidates = new ArrayList<String>();
        int dot = name.indexOf('.');
        if (dot < 0) {
            for (String pageNameCandidate : pageNameCandidates) {
                candidates.add(pageNameCandidate + "." + name);
            }
        } else {
            String prefix = name.substring(0, dot) + ".";
            String suffix = name.substring(dot);
            for (String pageNameCandidate : pageNameCandidates) {
                candidates.add(prefix + pageNameCandidate + suffix);
            }
        }
        candidates.add(name);
        return candidates.toArray(new String[0]);
    }
}
