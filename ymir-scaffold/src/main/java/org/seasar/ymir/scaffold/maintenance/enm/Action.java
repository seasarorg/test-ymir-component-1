package org.seasar.ymir.scaffold.maintenance.enm;

public enum Action {
    UNKNOWN(""), INDEX("index"), ADD("add"), EDIT("edit"), DELETE("delete");

    private String name;

    public static Action enumOf(String name) {
        for (Action enm : values()) {
            if (enm.getName().equals(name)) {
                return enm;
            }
        }
        return UNKNOWN;
    }

    private Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
