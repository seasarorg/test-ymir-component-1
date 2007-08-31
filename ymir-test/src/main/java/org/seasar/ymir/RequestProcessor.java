package org.seasar.ymir;

import org.seasar.ymir.constraint.PermissionDeniedException;

public interface RequestProcessor {

    /**
     * 代わりにConstraintInterceptor#ACTION_VALIDATIONFAILEDを使って下さい。
     */
    @Deprecated
    String ACTION_VALIDATIONFAILED = "_validationFailed";

    /**
     * 代わりにConstraintInterceptor#ACTION_PERMISSIONDENIEDを使って下さい。
     */
    @Deprecated
    String ACTION_PERMISSIONDENIED = "_permissionDenied";

    String ACTION_DEFAULT = "_default";

    String METHOD_RENDER = "_render";

    String ATTR_SELF = "self";

    String ATTR_NOTES = "notes";

    String ATTR_PAGECOMPONENT = "pageComponent";

    Response process(Request request) throws PageNotFoundException,
            PermissionDeniedException;

    Object backupForInclusion(AttributeContainer attributeContainer);

    void restoreForInclusion(AttributeContainer attributeContainer,
            Object backupped);
}
