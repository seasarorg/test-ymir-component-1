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
 * The condition-query for in-line of YS_ROLE.
 * @author DBFlute(AutoGenerator)
 */
public class YsRoleCIQ extends AbstractBsYsRoleCQ {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected BsYsRoleCQ _myCQ;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public YsRoleCIQ(ConditionQuery childQuery, SqlClause sqlClause
                        , String aliasName, int nestLevel, BsYsRoleCQ myCQ) {
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
    public String keepId_ExistsReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ sq)
    { throwIICBOE("ExistsReferrer"); return null; }
    public String keepId_NotExistsReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ sq)
    { throwIICBOE("NotExistsReferrer"); return null; }
    public String keepId_InScopeRelation_YsRoleGroupUserList(YsRoleGroupUserCQ sq)
    { return _myCQ.keepId_InScopeRelation_YsRoleGroupUserList(sq); }
    public String keepId_NotInScopeRelation_YsRoleGroupUserList(YsRoleGroupUserCQ sq)
    { return _myCQ.keepId_NotInScopeRelation_YsRoleGroupUserList(sq); }
    public String keepId_SpecifyDerivedReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ sq)
    { throwIICBOE("(Specify)DerivedReferrer"); return null; }
    public String keepId_QueryDerivedReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ sq)
    { throwIICBOE("(Query)DerivedReferrer"); return null; }
    public String keepId_QueryDerivedReferrer_YsRoleGroupUserListParameter(Object pv)
    { throwIICBOE("(Query)DerivedReferrer"); return null; }
    protected ConditionValue getCValueName() { return _myCQ.getName(); }
    protected ConditionValue getCValueDisplayName() { return _myCQ.getDisplayName(); }
    protected ConditionValue getCValueCreatedDate() { return _myCQ.getCreatedDate(); }
    protected ConditionValue getCValueModifiedDate() { return _myCQ.getModifiedDate(); }
    protected ConditionValue getCValueVersionNo() { return _myCQ.getVersionNo(); }
    public String keepScalarCondition(YsRoleCQ subQuery)
    { throwIICBOE("ScalarCondition"); return null; }
    public String keepMyselfInScopeRelation(YsRoleCQ subQuery)
    { throwIICBOE("MyselfInScopeRelation"); return null;}

    protected void throwIICBOE(String name) { // throwInlineIllegalConditionBeanOperationException()
        throw new IllegalConditionBeanOperationException(name + " at InlineView is unsupported.");
    }

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xinCB() { return YsRoleCB.class.getName(); }
    protected String xinCQ() { return YsRoleCQ.class.getName(); }
}
