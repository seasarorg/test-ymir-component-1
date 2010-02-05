package org.seasar.ymir.util;

import java.util.ArrayList;
import java.util.List;

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
