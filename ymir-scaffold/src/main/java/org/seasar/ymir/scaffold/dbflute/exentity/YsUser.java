package org.seasar.ymir.scaffold.dbflute.exentity;

import org.seasar.ymir.scaffold.dbflute.bsentity.BsYsUser;
import org.seasar.ymir.scaffold.util.ScaffoldUtils;

/**
 * The entity of YS_USER.
 * <p>
 * You can implement your original methods here.
 * This class remains when re-generating.
 * </p>
 * @author DBFlute(AutoGenerator)
 */
public class YsUser extends BsYsUser {

    /** Serial version UID. (Default) */
    private static final long serialVersionUID = 1L;

    public boolean passwordEquals(String password) {
        return getPassword().equals(hash(password));
    }

    protected String hash(String password) {
        return ScaffoldUtils.hash(password);
    }
}
