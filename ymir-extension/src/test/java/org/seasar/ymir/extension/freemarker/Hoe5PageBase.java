package org.seasar.ymir.extension.freemarker;

import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.conversation.annotation.Begin;

public class Hoe5PageBase {
    @Begin
    public void _permissionDenied(PermissionDeniedException ex)
            throws PermissionDeniedException {
        throw ex;
    }
}
