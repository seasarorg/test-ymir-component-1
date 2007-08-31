package org.seasar.ymir.redirection;

import java.util.Map;

public interface RedirectionManager {
    boolean isAddScopeIdAsRequestParameter();

    String getScopeIdKey();

    Map<String, Object> getScopeMap();

    Map<String, Object> getScopeMap(boolean create);

    Map<String, Object> getScopeMap(String scopeId);

    Map<String, Object> getScopeMap(String scopeId, boolean create);

    void removeScopeMap();

    void removeScopeMap(String scopeId);

    String getScopeId();

    String getScopeIdFromRequest();

    Object get(String name);
}
