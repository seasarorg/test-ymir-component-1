package org.seasar.ymir.extension.freemarker;

import org.seasar.ymir.Notes;
import org.seasar.ymir.constraint.PermissionDeniedException;

public class Hoe3PageBase {
    public void _validationFailed(Notes notes) {
    }

    public void _permissionDenied(PermissionDeniedException ex)
            throws PermissionDeniedException {
        throw ex;
    }
}
