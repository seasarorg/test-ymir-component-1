package org.seasar.ymir.scaffold.maintenance.enm;

import java.util.regex.Pattern;

public enum Pane {
    SEARCH(".*search"), LIST(".*list"), DETAIL(".*detail"), EDIT(".*edit"), ADD(
            ".*add");

    private Pattern pattern;

    public static Pane enumOf(String actionName) {
        for (Pane enm : values()) {
            if (enm.pattern.matcher(actionName).matches()) {
                return enm;
            }
        }
        return null;
    }

    private Pane(String patternString) {
        this.pattern = Pattern.compile(patternString);
    }
}
