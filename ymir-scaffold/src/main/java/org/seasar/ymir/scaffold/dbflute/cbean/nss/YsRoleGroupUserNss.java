package org.seasar.ymir.scaffold.dbflute.cbean.nss;

import org.seasar.dbflute.cbean.ConditionQuery;
import org.seasar.ymir.scaffold.dbflute.cbean.cq.YsRoleGroupUserCQ;

/**
 * The nest select set-upper of YS_ROLE_GROUP_USER.
 * @author DBFlute(AutoGenerator)
 */
public class YsRoleGroupUserNss {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected YsRoleGroupUserCQ _query;
    public YsRoleGroupUserNss(YsRoleGroupUserCQ query) { _query = query; }
    public boolean hasConditionQuery() { return _query != null; }

    // ===================================================================================
    //                                                                     Nested Relation
    //                                                                     ===============
    public YsGroupNss withYsGroup() {
        _query.doNss(new YsRoleGroupUserCQ.NssCall() { public ConditionQuery qf() { return _query.queryYsGroup(); }});
		return new YsGroupNss(_query.queryYsGroup());
    }
    public YsRoleNss withYsRole() {
        _query.doNss(new YsRoleGroupUserCQ.NssCall() { public ConditionQuery qf() { return _query.queryYsRole(); }});
		return new YsRoleNss(_query.queryYsRole());
    }
    public YsUserNss withYsUser() {
        _query.doNss(new YsRoleGroupUserCQ.NssCall() { public ConditionQuery qf() { return _query.queryYsUser(); }});
		return new YsUserNss(_query.queryYsUser());
    }

}
