package org.seasar.ymir.scaffold.dbflute.exentity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

    public static final int ID_ADMINISTRATOR = 1;

    private final Set<Long> roleIdSet = new HashSet<Long>();

    private final Set<String> roleNameSet = new HashSet<String>();

    public boolean passwordEquals(String password) {
        if (password.length() == 0) {
            return getPassword().length() == 0;
        } else {
            return getPassword().equals(hash(password));
        }
    }

    protected String hash(String password) {
        return ScaffoldUtils.hash(password);
    }

    public boolean isInRole(YsRole... roles) {
        Long[] roleIds = new Long[roles.length];
        for (int i = 0; i < roles.length; i++) {
            roleIds[i] = roles[i].getId();
        }
        return isInRole(roleIds);
    }

    public boolean isInRole(String... roleNames) {
        if (getId() == ID_ADMINISTRATOR) {
            return true;
        }

        for (String roleName : roleNames) {
            if (roleNameSet.contains(roleName)) {
                return true;
            }
        }

        return false;
    }

    public boolean isInRole(Long... roleIds) {
        if (getId() == ID_ADMINISTRATOR) {
            return true;
        }

        for (Long roleId : roleIds) {
            if (roleIdSet.contains(roleId)) {
                return true;
            }
        }

        return false;
    }

    public void setRoles(Collection<YsRole> roles) {
        roleIdSet.clear();
        roleNameSet.clear();
        for (YsRole role : roles) {
            roleIdSet.add(role.getId());
            roleNameSet.add(role.getName());
        }
    }
}
