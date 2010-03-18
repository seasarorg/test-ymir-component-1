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
    public String keepId_InScopeSubQuery_YsRoleGroupUserList(YsRoleGroupUserCQ sq)
    { return _myCQ.keepId_InScopeSubQuery_YsRoleGroupUserList(sq); }
    public String keepId_NotInScopeSubQuery_YsRoleGroupUserList(YsRoleGroupUserCQ sq)
    { return _myCQ.keepId_NotInScopeSubQuery_YsRoleGroupUserList(sq); }
    public String keepId_ExistsSubQuery_YsRoleGroupUserList(YsRoleGroupUserCQ sq)
    { throwIICBOE("ExistsSubQuery"); return null; }
    public String keepId_NotExistsSubQuery_YsRoleGroupUserList(YsRoleGroupUserCQ sq)
    { throwIICBOE("NotExistsSubQuery"); return null; }
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
    public String keepScalarSubQuery(YsRoleCQ subQuery)
    { throwIICBOE("ScalarSubQuery"); return null; }
    public String keepMyselfInScopeSubQuery(YsRoleCQ subQuery)
    { throwIICBOE("MyselfInScopeSubQuery"); return null;}

    protected void throwIICBOE(String name) { // throwInlineIllegalConditionBeanOperationException()
        throw new IllegalConditionBeanOperationException("Sorry! " + name + " at in-line view is unavailable!");
    }

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // Very Internal (for Suppressing Warn about 'Not Use Import')
    String xiCB() { return YsRoleCB.class.getName(); }
    String xiCQ() { return YsRoleCQ.class.getName(); }
}
