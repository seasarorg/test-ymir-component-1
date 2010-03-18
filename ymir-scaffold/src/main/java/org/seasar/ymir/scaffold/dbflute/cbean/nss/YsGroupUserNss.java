package org.seasar.ymir.scaffold.dbflute.cbean.nss;

import org.seasar.dbflute.cbean.ConditionQuery;
import org.seasar.ymir.scaffold.dbflute.cbean.cq.YsGroupUserCQ;

/**
 * The nest select set-upper of YS_GROUP_USER.
 * @author DBFlute(AutoGenerator)
 */
public class YsGroupUserNss {

    protected YsGroupUserCQ _query;
    public YsGroupUserNss(YsGroupUserCQ query) { _query = query; }
    public boolean hasConditionQuery() { return _query != null; }

    // ===================================================================================
    //                                                           With Nested Foreign Table
    //                                                           =========================
    public YsGroupNss withYsGroup() {
        _query.doNss(new YsGroupUserCQ.NssCall() { public ConditionQuery qf() { return _query.queryYsGroup(); }});
		return new YsGroupNss(_query.queryYsGroup());
    }
    public YsUserNss withYsUser() {
        _query.doNss(new YsGroupUserCQ.NssCall() { public ConditionQuery qf() { return _query.queryYsUser(); }});
		return new YsUserNss(_query.queryYsUser());
    }

    // ===================================================================================
    //                                                          With Nested Referrer Table
    //                                                          ==========================
}
