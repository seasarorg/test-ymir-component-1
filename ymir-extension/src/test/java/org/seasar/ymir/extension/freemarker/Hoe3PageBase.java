package org.seasar.ymir.extension.freemarker;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.message.Notes;

public class Hoe3PageBase {
    public void _validationFailed(Notes notes) {
    }

    @Meta(name = "source", value = { "throw ex;", "ex" })
    public void _permissionDenied(PermissionDeniedException ex)
            throws PermissionDeniedException {
        throw ex;
    }
}
