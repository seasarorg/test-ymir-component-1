package org.seasar.ymir.scaffold.exbhv;

import org.seasar.ymir.scaffold.bsbhv.BsYsUserBhv;
import org.seasar.ymir.scaffold.cbean.YsUserCB;
import org.seasar.ymir.scaffold.exentity.YsUser;

/**
 * The behavior of YS_USER.
 * <p>
 * You can implement your original methods here.
 * This class remains when re-generating.
 * </p>
 * @author DBFlute(AutoGenerator)
 */
public class YsUserBhv extends BsYsUserBhv {
    public YsUser selectByName(String name) {
        YsUserCB cb = new YsUserCB();
        cb.query().setName_Equal(name);
        return selectEntity(cb);
    }
}
