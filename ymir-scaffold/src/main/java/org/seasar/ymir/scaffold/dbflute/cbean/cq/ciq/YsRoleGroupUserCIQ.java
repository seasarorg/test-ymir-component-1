package org.seasar.ymir.scaffold.dbflute.cbean.cq.ciq;

import org.seasar.dbflute.cbean.*;
import org.seasar.dbflute.cbean.ckey.*;
import org.seasar.dbflute.cbean.coption.ConditionOption;
import org.seasar.dbflute.cbean.cvalue.ConditionValue;
import org.seasar.dbflute.cbean.sqlclause.SqlClause;
import org.seasar.dbflute.exception.IllegalConditionBeanOperationException;
import org.seasar.ymir.scaffold.dbflute.cbean.*;
import org.seasar.ymir.scaffold.dbflute.cbean.cq.bs.*;
import org.seasar.ymir.scaffold.dbflute.cbean.cq.*;

/**
 * The condition-query for in-line of YS_ROLE_GROUP_USER.
 * @author DBFlute(AutoGenerator)
 */
public class YsRoleGroupUserCIQ extends AbstractBsYsRoleGroupUserCQ {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected BsYsRoleGroupUserCQ _myCQ;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public YsRoleGroupUserCIQ(ConditionQuery childQuery, SqlClause sqlClause
                        , String aliasName, int nestLevel, BsYsRoleGroupUserCQ myCQ) {
        super(childQuery, sqlClause, aliasName, nestLevel);
        _myCQ = myCQ;
        _foreignPropertyName = _myCQ.xgetForeignPropertyName(); // accept foreign property name
        _relationPath = _myCQ.xgetRelationPath(); // accept relation path
        _inline = true;
    }

    // ===================================================================================
    //                                                             Override about Register
    //                                                             =======================
    @Override
    protected void reflectRelationOnUnionQuery(ConditionQuery bq, ConditionQuery uq) {
        String msg = "InlineView must not need UNION method: " + bq + " : " + uq;
        throw new IllegalConditionBeanOperationException(msg);
    }

    @Override
    protected void setupConditionValueAndRegisterWhereClause(ConditionKey k, Object v, ConditionValue cv, String col) {
        regIQ(k, v, cv, col);
    }

    @Override
    protected void setupConditionValueAndRegisterWhereClause(ConditionKey k, Object v, ConditionValue cv, String col, ConditionOption op) {
        regIQ(k, v, cv, col, op);
    }

    @Override
    protected void registerWhereClause(String wc) {
        registerInlineWhereClause(wc);
    }

    @Override
    protected boolean isInScopeRelationSuppressLocalAliasName() {
        if (_onClause) {
            throw new IllegalConditionBeanOperationException("InScopeRelation on OnClause is unsupported.");
        }
        return true;
    }

    // ===================================================================================
    //                                                                Override about Query
    //                                                                ====================
    protected ConditionValue getCValueId() { return _myCQ.getId(); }
    protected ConditionValue getCValueRoleId() { return _myCQ.getRoleId(); }
    public String keepRoleId_InScopeRelation_YsRole(YsRoleCQ sq)
    { return _myCQ.keepRoleId_InScopeRelation_YsRole(sq); }
    public String keepRoleId_NotInScopeRelation_YsRole(YsRoleCQ sq)
    { return _myCQ.keepRoleId_NotInScopeRelation_YsRole(sq); }
    protected ConditionValue getCValueGroupId() { return _myCQ.getGroupId(); }
    public String keepGroupId_InScopeRelation_YsGroup(YsGroupCQ sq)
    { return _myCQ.keepGroupId_InScopeRelation_YsGroup(sq); }
    public String keepGroupId_NotInScopeRelation_YsGroup(YsGroupCQ sq)
    { return _myCQ.keepGroupId_NotInScopeRelation_YsGroup(sq); }
    protected ConditionValue getCValueUserId() { return _myCQ.getUserId(); }
    public String keepUserId_InScopeRelation_YsUser(YsUserCQ sq)
    { return _myCQ.keepUserId_InScopeRelation_YsUser(sq); }
    public String keepUserId_NotInScopeRelation_YsUser(YsUserCQ sq)
    { return _myCQ.keepUserId_NotInScopeRelation_YsUser(sq); }
    protected ConditionValue getCValueCreatedDate() { return _myCQ.getCreatedDate(); }
    protected ConditionValue getCValueModifiedDate() { return _myCQ.getModifiedDate(); }
    protected ConditionValue getCValueVersionNo() { return _myCQ.getVersionNo(); }
    public String keepScalarCondition(YsRoleGroupUserCQ subQuery)
    { throwIICBOE("ScalarCondition"); return null; }
    public String keepMyselfInScopeRelation(YsRoleGroupUserCQ subQuery)
    { throwIICBOE("MyselfInScopeRelation"); return null;}

    protected void throwIICBOE(String name) { // throwInlineIllegalConditionBeanOperationException()
        throw new IllegalConditionBeanOperationException(name + " at InlineView is unsupported.");
    }

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xinCB() { return YsRoleGroupUserCB.class.getName(); }
    protected String xinCQ() { return YsRoleGroupUserCQ.class.getName(); }
}
