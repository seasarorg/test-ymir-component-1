package org.seasar.ymir;

public interface Globals {
    String IDPREFIX = "org.seasar.ymir.";

    String LANDMARK_CLASSNAME = IDPREFIX + "landmark.Landmark";

    String ATTR_LOCALE = IDPREFIX + "locale";

    String MESSAGES = "messages.xproperties";

    String NAME_MESSAGES = "messages";

    String APPKEYPREFIX_CORE = "core.";

    String APPKEY_CORE_REQUESTPARAMETER_STRICTINJECTION = APPKEYPREFIX_CORE
            + "requestParameter.strictInjection";
}
