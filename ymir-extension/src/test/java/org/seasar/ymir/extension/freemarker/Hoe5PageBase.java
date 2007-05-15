package org.seasar.ymir.extension.freemarker;

import org.seasar.ymir.constraint.PermissionDeniedException;

public class Hoe5PageBase {
    public void _permissionDenied(PermissionDeniedException ex)
            throws PermissionDeniedException {
        throw ex;
    }
}
