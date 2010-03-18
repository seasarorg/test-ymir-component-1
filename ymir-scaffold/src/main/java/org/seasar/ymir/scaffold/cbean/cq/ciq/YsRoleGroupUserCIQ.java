package org.seasar.ymir.scaffold.cbean.cq.ciq;

import org.seasar.dbflute.cbean.*;
import org.seasar.dbflute.cbean.ckey.*;
import org.seasar.dbflute.cbean.coption.ConditionOption;
import org.seasar.dbflute.cbean.cvalue.ConditionValue;
import org.seasar.dbflute.cbean.sqlclause.SqlClause;
import org.seasar.dbflute.exception.IllegalConditionBeanOperationException;
import org.seasar.ymir.scaffold.cbean.*;
import org.seasar.ymir.scaffold.cbean.cq.bs.*;
import org.seasar.ymir.scaffold.cbean.cq.*;

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
        _foreignPropertyName = _myCQ.getForeignPropertyName(); // accept foreign property name
        _relationPath = _myCQ.getRelationPath(); // accept relation path
    }

    // ===================================================================================
    //                                                             Override about Register
    //                                                             =======================
    @Override
    protected void reflectRelationOnUnionQuery(ConditionQuery baseQueryAsSuper, ConditionQuery unionQueryAsSuper) {
        String msg = "InlineQuery must not need UNION method: " + baseQueryAsSuper + " : " + unionQueryAsSuper;
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
    protected void registerWhereClause(String whereClause) {
        registerInlineWhereClause(whereClause);
    }

    @Override
    protected String getInScopeSubQueryRealColumnName(String columnName) {
        if (_onClauseInline) {
            String msg = "Sorry! InScopeSubQuery of on-clause is unavailable";
            throw new IllegalConditionBeanOperationException(msg);
        }
        return _onClauseInline ? getRealAliasName() + "." + columnName : columnName;
    }

    @Override
    protected void registerExistsSubQuery(ConditionQuery subQuery, String columnName, String relatedColumnName, String propertyName) {
        String msg = "Sorry! ExistsSubQuery at in-line view is unavailable. So please use InScopeSubQyery.";
        throw new IllegalConditionBeanOperationException(msg);
    }

    // ===================================================================================
    //                                                                Override about Query
    //                                                                ====================
    protected ConditionValue getCValueId() { return _myCQ.getId(); }
    protected ConditionValue getCValueRoleId() { return _myCQ.getRoleId(); }
    public String keepRoleId_InScopeSubQuery_YsRole(YsRoleCQ sq)
    { return _myCQ.keepRoleId_InScopeSubQuery_YsRole(sq); }
    protected ConditionValue getCValueGroupId() { return _myCQ.getGroupId(); }
    public String keepGroupId_InScopeSubQuery_YsGroup(YsGroupCQ sq)
    { return _myCQ.keepGroupId_InScopeSubQuery_YsGroup(sq); }
    protected ConditionValue getCValueUserId() { return _myCQ.getUserId(); }
    public String keepUserId_InScopeSubQuery_YsUser(YsUserCQ sq)
    { return _myCQ.keepUserId_InScopeSubQuery_YsUser(sq); }
    protected ConditionValue getCValueCreatedDate() { return _myCQ.getCreatedDate(); }
    protected ConditionValue getCValueModifiedDate() { return _myCQ.getModifiedDate(); }
    protected ConditionValue getCValueVersionNo() { return _myCQ.getVersionNo(); }
    public String keepScalarSubQuery(YsRoleGroupUserCQ subQuery)
    { throwIICBOE("ScalarSubQuery"); return null; }
    public String keepMyselfInScopeSubQuery(YsRoleGroupUserCQ subQuery)
    { throwIICBOE("MyselfInScopeSubQuery"); return null;}

    protected void throwIICBOE(String name) { // throwInlineIllegalConditionBeanOperationException()
        throw new IllegalConditionBeanOperationException("Sorry! " + name + " at in-line view is unavailable!");
    }

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // Very Internal (for Suppressing Warn about 'Not Use Import')
    String xiCB() { return YsRoleGroupUserCB.class.getName(); }
    String xiCQ() { return YsRoleGroupUserCQ.class.getName(); }
}
