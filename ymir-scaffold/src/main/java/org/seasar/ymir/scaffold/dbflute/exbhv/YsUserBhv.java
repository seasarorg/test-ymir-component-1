package org.seasar.ymir.scaffold.dbflute.exbhv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.seasar.dbflute.bhv.ConditionBeanSetupper;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.scaffold.dbflute.bsbhv.BsYsUserBhv;
import org.seasar.ymir.scaffold.dbflute.cbean.YsGroupUserCB;
import org.seasar.ymir.scaffold.dbflute.cbean.YsRoleGroupUserCB;
import org.seasar.ymir.scaffold.dbflute.cbean.YsUserCB;
import org.seasar.ymir.scaffold.dbflute.exentity.YsGroup;
import org.seasar.ymir.scaffold.dbflute.exentity.YsGroupUser;
import org.seasar.ymir.scaffold.dbflute.exentity.YsRole;
import org.seasar.ymir.scaffold.dbflute.exentity.YsRoleGroupUser;
import org.seasar.ymir.scaffold.dbflute.exentity.YsUser;

/**
 * The behavior of YS_USER.
 * <p>
 * You can implement your original methods here.
 * This class remains when re-generating.
 * </p>
 * @author DBFlute(AutoGenerator)
 */
public class YsUserBhv extends BsYsUserBhv {
    @Binding(bindingType = BindingType.MUST)
    protected YsGroupBhv ysGroupBhv;

    public YsUser selectByName(String name) {
        YsUserCB cb = new YsUserCB();
        cb.query().setName_Equal(name);
        return selectEntity(cb);
    }

    public YsUser selectForLogin(String name) {
        YsUserCB cb = new YsUserCB();
        cb.query().setName_Equal(name);
        YsUser user = selectEntity(cb);
        if (user != null) {
            Map<Long, YsRole> roleMap = new TreeMap<Long, YsRole>();

            // ユーザが属しているグループに割り当てられているロールを取得する。
            loadYsGroupUserList(user,
                    new ConditionBeanSetupper<YsGroupUserCB>() {
                        public void setup(YsGroupUserCB cb) {
                            cb.setupSelect_YsGroup();
                        }
                    });
            List<YsGroup> groups = new ArrayList<YsGroup>();
            for (YsGroupUser groupUser : user.getYsGroupUserList()) {
                groups.add(groupUser.getYsGroup());
            }
            ysGroupBhv.loadYsRoleGroupUserList(groups,
                    new ConditionBeanSetupper<YsRoleGroupUserCB>() {
                        public void setup(YsRoleGroupUserCB cb) {
                            cb.setupSelect_YsRole();
                        }
                    });
            for (YsGroup group : groups) {
                for (YsRoleGroupUser roleGroupUser : group
                        .getYsRoleGroupUserList()) {
                    YsRole role = roleGroupUser.getYsRole();
                    roleMap.put(role.getId(), role);
                }
            }

            // ユーザに直接割り当てられているロールを取得する。
            loadYsRoleGroupUserList(user,
                    new ConditionBeanSetupper<YsRoleGroupUserCB>() {
                        public void setup(YsRoleGroupUserCB cb) {
                            cb.setupSelect_YsRole();
                        }
                    });
            for (YsRoleGroupUser roleGroupUser : user.getYsRoleGroupUserList()) {
                YsRole role = roleGroupUser.getYsRole();
                roleMap.put(role.getId(), role);
            }

            // ロールをユーザにセットしておく。
            user.setRoles(roleMap.values());
        }
        return user;
    }
}
