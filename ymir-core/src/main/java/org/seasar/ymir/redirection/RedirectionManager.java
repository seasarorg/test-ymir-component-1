package org.seasar.ymir.redirection;

import java.util.Map;

import org.seasar.ymir.Globals;

public interface RedirectionManager {
    String KEY_SCOPE = Globals.IDPREFIX + "redirection";

    Map<String, Object> getScopeMap();

    Map<String, Object> getScopeMap(boolean create);

    Map<String, Object> getScopeMap(String scopeKey);

    Map<String, Object> getScopeMap(String scopeKey, boolean create);

    void removeScopeMap();

    void removeScopeMap(String scopeKey);

    String getScopeKey();

    Object get(String name);
}
