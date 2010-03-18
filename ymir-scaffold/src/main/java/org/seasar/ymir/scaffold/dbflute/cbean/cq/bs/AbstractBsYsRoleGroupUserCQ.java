package org.seasar.ymir.scaffold.dbflute.cbean.cq.bs;

import java.util.Collection;

import org.seasar.dbflute.cbean.*;
import org.seasar.dbflute.cbean.chelper.*;
import org.seasar.dbflute.cbean.ckey.*;
import org.seasar.dbflute.cbean.coption.*;
import org.seasar.dbflute.cbean.cvalue.ConditionValue;
import org.seasar.dbflute.cbean.sqlclause.SqlClause;
import org.seasar.dbflute.dbmeta.DBMetaProvider;
import org.seasar.ymir.scaffold.dbflute.allcommon.*;
import org.seasar.ymir.scaffold.dbflute.cbean.*;
import org.seasar.ymir.scaffold.dbflute.cbean.cq.*;

/**
 * The abstract condition-query of YS_ROLE_GROUP_USER.
 * @author DBFlute(AutoGenerator)
 */
public abstract class AbstractBsYsRoleGroupUserCQ extends AbstractConditionQuery {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final DBMetaProvider _dbmetaProvider = new DBMetaInstanceHandler();

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public AbstractBsYsRoleGroupUserCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        super(childQuery, sqlClause, aliasName, nestLevel);
    }

    // ===================================================================================
    //                                                                     DBMeta Provider
    //                                                                     ===============
    @Override
    protected DBMetaProvider getDBMetaProvider() {
        return _dbmetaProvider;
    }

    // ===================================================================================
    //                                                                          Table Name
    //                                                                          ==========
    public String getTableDbName() {
        return "YS_ROLE_GROUP_USER";
    }

    // ===================================================================================
    //                                                                               Query
    //                                                                               =====
    
    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. {PK : ID : NotNull : BIGINT(19)}
     * @param id The value of id as equal.
     */
    public void setId_Equal(Long id) {
        regId(CK_EQ, id);
    }

    /**
     * NotEqual(!=). And NullIgnored, OnlyOnceRegistered.
     * @param id The value of id as notEqual.
     */
    public void setId_NotEqual(Long id) {
        regId(CK_NE, id);
    }

    /**
     * GreaterThan(&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param id The value of id as greaterThan.
     */
    public void setId_GreaterThan(Long id) {
        regId(CK_GT, id);
    }

    /**
     * LessThan(&lt;). And NullIgnored, OnlyOnceRegistered.
     * @param id The value of id as lessThan.
     */
    public void setId_LessThan(Long id) {
        regId(CK_LT, id);
    }

    /**
     * GreaterEqual(&gt;=). And NullIgnored, OnlyOnceRegistered.
     * @param id The value of id as greaterEqual.
     */
    public void setId_GreaterEqual(Long id) {
        regId(CK_GE, id);
    }

    /**
     * LessEqual(&lt;=). And NullIgnored, OnlyOnceRegistered.
     * @param id The value of id as lessEqual.
     */
    public void setId_LessEqual(Long id) {
        regId(CK_LE, id);
    }

    /**
     * InScope(in (1, 2)). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param idList The collection of id as inScope.
     */
    public void setId_InScope(Collection<Long> idList) {
        regINS(CK_INS, cTL(idList), getCValueId(), "ID");
    }

    /**
     * NotInScope(not in (1, 2)). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param idList The collection of id as notInScope.
     */
    public void setId_NotInScope(Collection<Long> idList) {
        regINS(CK_NINS, cTL(idList), getCValueId(), "ID");
    }

    /**
     * IsNull(is null). And OnlyOnceRegistered.
     */
    public void setId_IsNull() { regId(CK_ISN, DOBJ); }

    /**
     * IsNotNull(is not null). And OnlyOnceRegistered.
     */
    public void setId_IsNotNull() { regId(CK_ISNN, DOBJ); }

    protected void regId(ConditionKey k, Object v) { regQ(k, v, getCValueId(), "ID"); }
    abstract protected ConditionValue getCValueId();
    
    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. {UQ : NotNull : BIGINT(19) : FK to YS_ROLE}
     * @param roleId The value of roleId as equal.
     */
    public void setRoleId_Equal(Long roleId) {
        regRoleId(CK_EQ, roleId);
    }

    /**
     * NotEqual(!=). And NullIgnored, OnlyOnceRegistered.
     * @param roleId The value of roleId as notEqual.
     */
    public void setRoleId_NotEqual(Long roleId) {
        regRoleId(CK_NE, roleId);
    }

    /**
     * GreaterThan(&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param roleId The value of roleId as greaterThan.
     */
    public void setRoleId_GreaterThan(Long roleId) {
        regRoleId(CK_GT, roleId);
    }

    /**
     * LessThan(&lt;). And NullIgnored, OnlyOnceRegistered.
     * @param roleId The value of roleId as lessThan.
     */
    public void setRoleId_LessThan(Long roleId) {
        regRoleId(CK_LT, roleId);
    }

    /**
     * GreaterEqual(&gt;=). And NullIgnored, OnlyOnceRegistered.
     * @param roleId The value of roleId as greaterEqual.
     */
    public void setRoleId_GreaterEqual(Long roleId) {
        regRoleId(CK_GE, roleId);
    }

    /**
     * LessEqual(&lt;=). And NullIgnored, OnlyOnceRegistered.
     * @param roleId The value of roleId as lessEqual.
     */
    public void setRoleId_LessEqual(Long roleId) {
        regRoleId(CK_LE, roleId);
    }

    /**
     * InScope(in (1, 2)). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param roleIdList The collection of roleId as inScope.
     */
    public void setRoleId_InScope(Collection<Long> roleIdList) {
        regINS(CK_INS, cTL(roleIdList), getCValueRoleId(), "ROLE_ID");
    }

    /**
     * NotInScope(not in (1, 2)). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param roleIdList The collection of roleId as notInScope.
     */
    public void setRoleId_NotInScope(Collection<Long> roleIdList) {
        regINS(CK_NINS, cTL(roleIdList), getCValueRoleId(), "ROLE_ID");
    }

    public void inScopeYsRole(SubQuery<YsRoleCB> subQuery) {
        assertObjectNotNull("subQuery<YsRoleCB>", subQuery);
        YsRoleCB cb = new YsRoleCB(); cb.xsetupForInScopeSubQuery(); subQuery.query(cb);
        String subQueryPropertyName = keepRoleId_InScopeSubQuery_YsRole(cb.query()); // for saving query-value.
        registerInScopeSubQuery(cb.query(), "ROLE_ID", "ID", subQueryPropertyName);
    }
    public abstract String keepRoleId_InScopeSubQuery_YsRole(YsRoleCQ subQuery);

    protected void regRoleId(ConditionKey k, Object v) { regQ(k, v, getCValueRoleId(), "ROLE_ID"); }
    abstract protected ConditionValue getCValueRoleId();
    
    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. {UQ : BIGINT(19) : FK to YS_GROUP}
     * @param groupId The value of groupId as equal.
     */
    public void setGroupId_Equal(Long groupId) {
        regGroupId(CK_EQ, groupId);
    }

    /**
     * NotEqual(!=). And NullIgnored, OnlyOnceRegistered.
     * @param groupId The value of groupId as notEqual.
     */
    public void setGroupId_NotEqual(Long groupId) {
        regGroupId(CK_NE, groupId);
    }

    /**
     * GreaterThan(&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param groupId The value of groupId as greaterThan.
     */
    public void setGroupId_GreaterThan(Long groupId) {
        regGroupId(CK_GT, groupId);
    }

    /**
     * LessThan(&lt;). And NullIgnored, OnlyOnceRegistered.
     * @param groupId The value of groupId as lessThan.
     */
    public void setGroupId_LessThan(Long groupId) {
        regGroupId(CK_LT, groupId);
    }

    /**
     * GreaterEqual(&gt;=). And NullIgnored, OnlyOnceRegistered.
     * @param groupId The value of groupId as greaterEqual.
     */
    public void setGroupId_GreaterEqual(Long groupId) {
        regGroupId(CK_GE, groupId);
    }

    /**
     * LessEqual(&lt;=). And NullIgnored, OnlyOnceRegistered.
     * @param groupId The value of groupId as lessEqual.
     */
    public void setGroupId_LessEqual(Long groupId) {
        regGroupId(CK_LE, groupId);
    }

    /**
     * InScope(in (1, 2)). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param groupIdList The collection of groupId as inScope.
     */
    public void setGroupId_InScope(Collection<Long> groupIdList) {
        regINS(CK_INS, cTL(groupIdList), getCValueGroupId(), "GROUP_ID");
    }

    /**
     * NotInScope(not in (1, 2)). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param groupIdList The collection of groupId as notInScope.
     */
    public void setGroupId_NotInScope(Collection<Long> groupIdList) {
        regINS(CK_NINS, cTL(groupIdList), getCValueGroupId(), "GROUP_ID");
    }

    public void inScopeYsGroup(SubQuery<YsGroupCB> subQuery) {
        assertObjectNotNull("subQuery<YsGroupCB>", subQuery);
        YsGroupCB cb = new YsGroupCB(); cb.xsetupForInScopeSubQuery(); subQuery.query(cb);
        String subQueryPropertyName = keepGroupId_InScopeSubQuery_YsGroup(cb.query()); // for saving query-value.
        registerInScopeSubQuery(cb.query(), "GROUP_ID", "ID", subQueryPropertyName);
    }
    public abstract String keepGroupId_InScopeSubQuery_YsGroup(YsGroupCQ subQuery);

    /**
     * IsNull(is null). And OnlyOnceRegistered.
     */
    public void setGroupId_IsNull() { regGroupId(CK_ISN, DOBJ); }

    /**
     * IsNotNull(is not null). And OnlyOnceRegistered.
     */
    public void setGroupId_IsNotNull() { regGroupId(CK_ISNN, DOBJ); }

    protected void regGroupId(ConditionKey k, Object v) { regQ(k, v, getCValueGroupId(), "GROUP_ID"); }
    abstract protected ConditionValue getCValueGroupId();
    
    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. {UQ : BIGINT(19) : FK to YS_USER}
     * @param userId The value of userId as equal.
     */
    public void setUserId_Equal(Long userId) {
        regUserId(CK_EQ, userId);
    }

    /**
     * NotEqual(!=). And NullIgnored, OnlyOnceRegistered.
     * @param userId The value of userId as notEqual.
     */
    public void setUserId_NotEqual(Long userId) {
        regUserId(CK_NE, userId);
    }

    /**
     * GreaterThan(&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param userId The value of userId as greaterThan.
     */
    public void setUserId_GreaterThan(Long userId) {
        regUserId(CK_GT, userId);
    }

    /**
     * LessThan(&lt;). And NullIgnored, OnlyOnceRegistered.
     * @param userId The value of userId as lessThan.
     */
    public void setUserId_LessThan(Long userId) {
        regUserId(CK_LT, userId);
    }

    /**
     * GreaterEqual(&gt;=). And NullIgnored, OnlyOnceRegistered.
     * @param userId The value of userId as greaterEqual.
     */
    public void setUserId_GreaterEqual(Long userId) {
        regUserId(CK_GE, userId);
    }

    /**
     * LessEqual(&lt;=). And NullIgnored, OnlyOnceRegistered.
     * @param userId The value of userId as lessEqual.
     */
    public void setUserId_LessEqual(Long userId) {
        regUserId(CK_LE, userId);
    }

    /**
     * InScope(in (1, 2)). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param userIdList The collection of userId as inScope.
     */
    public void setUserId_InScope(Collection<Long> userIdList) {
        regINS(CK_INS, cTL(userIdList), getCValueUserId(), "USER_ID");
    }

    /**
     * NotInScope(not in (1, 2)). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param userIdList The collection of userId as notInScope.
     */
    public void setUserId_NotInScope(Collection<Long> userIdList) {
        regINS(CK_NINS, cTL(userIdList), getCValueUserId(), "USER_ID");
    }

    public void inScopeYsUser(SubQuery<YsUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsUserCB>", subQuery);
        YsUserCB cb = new YsUserCB(); cb.xsetupForInScopeSubQuery(); subQuery.query(cb);
        String subQueryPropertyName = keepUserId_InScopeSubQuery_YsUser(cb.query()); // for saving query-value.
        registerInScopeSubQuery(cb.query(), "USER_ID", "ID", subQueryPropertyName);
    }
    public abstract String keepUserId_InScopeSubQuery_YsUser(YsUserCQ subQuery);

    /**
     * IsNull(is null). And OnlyOnceRegistered.
     */
    public void setUserId_IsNull() { regUserId(CK_ISN, DOBJ); }

    /**
     * IsNotNull(is not null). And OnlyOnceRegistered.
     */
    public void setUserId_IsNotNull() { regUserId(CK_ISNN, DOBJ); }

    protected void regUserId(ConditionKey k, Object v) { regQ(k, v, getCValueUserId(), "USER_ID"); }
    abstract protected ConditionValue getCValueUserId();
    
    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. {NotNull : TIMESTAMP(23, 10)}
     * @param createdDate The value of createdDate as equal.
     */
    public void setCreatedDate_Equal(java.sql.Timestamp createdDate) {
        regCreatedDate(CK_EQ,  createdDate);
    }

    /**
     * GreaterThan(&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param createdDate The value of createdDate as greaterThan.
     */
    public void setCreatedDate_GreaterThan(java.sql.Timestamp createdDate) {
        regCreatedDate(CK_GT,  createdDate);
    }

    /**
     * LessThan(&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param createdDate The value of createdDate as lessThan.
     */
    public void setCreatedDate_LessThan(java.sql.Timestamp createdDate) {
        regCreatedDate(CK_LT,  createdDate);
    }

    /**
     * GreaterEqual(&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param createdDate The value of createdDate as greaterEqual.
     */
    public void setCreatedDate_GreaterEqual(java.sql.Timestamp createdDate) {
        regCreatedDate(CK_GE,  createdDate);
    }

    /**
     * LessEqual(&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param createdDate The value of createdDate as lessEqual.
     */
    public void setCreatedDate_LessEqual(java.sql.Timestamp createdDate) {
        regCreatedDate(CK_LE, createdDate);
    }

    /**
     * FromTo(fromDate &lt;= COLUMN_NAME &lt;= toDate). And NullIgnored, OnlyOnceRegistered. {NotNull : TIMESTAMP(23, 10)}
     * @param fromDate The from-date of createdDate. (Nullable)
     * @param toDate The to-date of createdDate. (Nullable)
     * @param fromToOption The option of from-to. (NotNull)
     */
    public void setCreatedDate_FromTo(java.util.Date fromDate, java.util.Date toDate, FromToOption fromToOption) {
        regFTQ((fromDate != null ? new java.sql.Timestamp(fromDate.getTime()) : null), (toDate != null ? new java.sql.Timestamp(toDate.getTime()) : null), getCValueCreatedDate(), "CREATED_DATE", fromToOption);
    }

    /**
     * FromTo(fromDate &lt;= COLUMN_NAME &lt; toDate + 1). And NullIgnored, OnlyOnceRegistered. {NotNull : TIMESTAMP(23, 10)}
     * @param fromDate The from-date of createdDate. (Nullable)
     * @param toDate The to-date of createdDate. (Nullable)
     */
    public void setCreatedDate_DateFromTo(java.util.Date fromDate, java.util.Date toDate) {
        setCreatedDate_FromTo(fromDate, toDate, new DateFromToOption());
    }

    protected void regCreatedDate(ConditionKey k, Object v) { regQ(k, v, getCValueCreatedDate(), "CREATED_DATE"); }
    abstract protected ConditionValue getCValueCreatedDate();
    
    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. {NotNull : TIMESTAMP(23, 10)}
     * @param modifiedDate The value of modifiedDate as equal.
     */
    public void setModifiedDate_Equal(java.sql.Timestamp modifiedDate) {
        regModifiedDate(CK_EQ,  modifiedDate);
    }

    /**
     * GreaterThan(&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param modifiedDate The value of modifiedDate as greaterThan.
     */
    public void setModifiedDate_GreaterThan(java.sql.Timestamp modifiedDate) {
        regModifiedDate(CK_GT,  modifiedDate);
    }

    /**
     * LessThan(&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param modifiedDate The value of modifiedDate as lessThan.
     */
    public void setModifiedDate_LessThan(java.sql.Timestamp modifiedDate) {
        regModifiedDate(CK_LT,  modifiedDate);
    }

    /**
     * GreaterEqual(&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param modifiedDate The value of modifiedDate as greaterEqual.
     */
    public void setModifiedDate_GreaterEqual(java.sql.Timestamp modifiedDate) {
        regModifiedDate(CK_GE,  modifiedDate);
    }

    /**
     * LessEqual(&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param modifiedDate The value of modifiedDate as lessEqual.
     */
    public void setModifiedDate_LessEqual(java.sql.Timestamp modifiedDate) {
        regModifiedDate(CK_LE, modifiedDate);
    }

    /**
     * FromTo(fromDate &lt;= COLUMN_NAME &lt;= toDate). And NullIgnored, OnlyOnceRegistered. {NotNull : TIMESTAMP(23, 10)}
     * @param fromDate The from-date of modifiedDate. (Nullable)
     * @param toDate The to-date of modifiedDate. (Nullable)
     * @param fromToOption The option of from-to. (NotNull)
     */
    public void setModifiedDate_FromTo(java.util.Date fromDate, java.util.Date toDate, FromToOption fromToOption) {
        regFTQ((fromDate != null ? new java.sql.Timestamp(fromDate.getTime()) : null), (toDate != null ? new java.sql.Timestamp(toDate.getTime()) : null), getCValueModifiedDate(), "MODIFIED_DATE", fromToOption);
    }

    /**
     * FromTo(fromDate &lt;= COLUMN_NAME &lt; toDate + 1). And NullIgnored, OnlyOnceRegistered. {NotNull : TIMESTAMP(23, 10)}
     * @param fromDate The from-date of modifiedDate. (Nullable)
     * @param toDate The to-date of modifiedDate. (Nullable)
     */
    public void setModifiedDate_DateFromTo(java.util.Date fromDate, java.util.Date toDate) {
        setModifiedDate_FromTo(fromDate, toDate, new DateFromToOption());
    }

    protected void regModifiedDate(ConditionKey k, Object v) { regQ(k, v, getCValueModifiedDate(), "MODIFIED_DATE"); }
    abstract protected ConditionValue getCValueModifiedDate();
    
    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. {NotNull : BIGINT(19) : default=[1]}
     * @param versionNo The value of versionNo as equal.
     */
    public void setVersionNo_Equal(Long versionNo) {
        regVersionNo(CK_EQ, versionNo);
    }

    /**
     * NotEqual(!=). And NullIgnored, OnlyOnceRegistered.
     * @param versionNo The value of versionNo as notEqual.
     */
    public void setVersionNo_NotEqual(Long versionNo) {
        regVersionNo(CK_NE, versionNo);
    }

    /**
     * GreaterThan(&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param versionNo The value of versionNo as greaterThan.
     */
    public void setVersionNo_GreaterThan(Long versionNo) {
        regVersionNo(CK_GT, versionNo);
    }

    /**
     * LessThan(&lt;). And NullIgnored, OnlyOnceRegistered.
     * @param versionNo The value of versionNo as lessThan.
     */
    public void setVersionNo_LessThan(Long versionNo) {
        regVersionNo(CK_LT, versionNo);
    }

    /**
     * GreaterEqual(&gt;=). And NullIgnored, OnlyOnceRegistered.
     * @param versionNo The value of versionNo as greaterEqual.
     */
    public void setVersionNo_GreaterEqual(Long versionNo) {
        regVersionNo(CK_GE, versionNo);
    }

    /**
     * LessEqual(&lt;=). And NullIgnored, OnlyOnceRegistered.
     * @param versionNo The value of versionNo as lessEqual.
     */
    public void setVersionNo_LessEqual(Long versionNo) {
        regVersionNo(CK_LE, versionNo);
    }

    /**
     * InScope(in (1, 2)). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param versionNoList The collection of versionNo as inScope.
     */
    public void setVersionNo_InScope(Collection<Long> versionNoList) {
        regINS(CK_INS, cTL(versionNoList), getCValueVersionNo(), "VERSION_NO");
    }

    /**
     * NotInScope(not in (1, 2)). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param versionNoList The collection of versionNo as notInScope.
     */
    public void setVersionNo_NotInScope(Collection<Long> versionNoList) {
        regINS(CK_NINS, cTL(versionNoList), getCValueVersionNo(), "VERSION_NO");
    }

    protected void regVersionNo(ConditionKey k, Object v) { regQ(k, v, getCValueVersionNo(), "VERSION_NO"); }
    abstract protected ConditionValue getCValueVersionNo();

    // ===================================================================================
    //                                                                     Scalar SubQuery
    //                                                                     ===============
    public HpSSQFunction<YsRoleGroupUserCB> scalar_Equal() {
        return xcreateSSQFunction("=");
    }

    public HpSSQFunction<YsRoleGroupUserCB> scalar_GreaterEqual() {
        return xcreateSSQFunction(">=");
    }

    public HpSSQFunction<YsRoleGroupUserCB> scalar_GreaterThan() {
        return xcreateSSQFunction(">");
    }

    public HpSSQFunction<YsRoleGroupUserCB> scalar_LessEqual() {
        return xcreateSSQFunction("<=");
    }
    
    public HpSSQFunction<YsRoleGroupUserCB> scalar_LessThan() {
        return xcreateSSQFunction("<");
    }
    
    protected HpSSQFunction<YsRoleGroupUserCB> xcreateSSQFunction(final String operand) {
        return new HpSSQFunction<YsRoleGroupUserCB>(new HpSSQSetupper<YsRoleGroupUserCB>() {
            public void setup(String function, SubQuery<YsRoleGroupUserCB> subQuery) {
                xscalarSubQuery(function, subQuery, operand);
            }
        });
    }

    protected void xscalarSubQuery(String function, SubQuery<YsRoleGroupUserCB> subQuery, String operand) {
        assertObjectNotNull("subQuery<YsRoleGroupUserCB>", subQuery);
        YsRoleGroupUserCB cb = new YsRoleGroupUserCB(); cb.xsetupForScalarSubQuery(); subQuery.query(cb);
        String subQueryPropertyName = keepScalarSubQuery(cb.query()); // for saving query-value.
        registerScalarSubQuery(function, cb.query(), subQueryPropertyName, operand);
    }
    public abstract String keepScalarSubQuery(YsRoleGroupUserCQ subQuery);

    // ===================================================================================
    //                                                             MySelf InScope SubQuery
    //                                                             =======================
    /**
     * Myself InScope SubQuery. {mainly for CLOB and Union}
     * @param subQuery The implementation of sub query. (NotNull)
     */
    public void myselfInScope(SubQuery<YsRoleGroupUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsRoleGroupUserCB>", subQuery);
        YsRoleGroupUserCB cb = new YsRoleGroupUserCB(); cb.xsetupForInScopeSubQuery(); subQuery.query(cb);
        String subQueryPropertyName = keepMyselfInScopeSubQuery(cb.query()); // for saving query-value.
        registerInScopeSubQuery(cb.query(), "ID", "ID", subQueryPropertyName);
    }
    public abstract String keepMyselfInScopeSubQuery(YsRoleGroupUserCQ subQuery);

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // Very Internal (for Suppressing Warn about 'Not Use Import')
    String xCB() { return YsRoleGroupUserCB.class.getName(); }
    String xCQ() { return YsRoleGroupUserCQ.class.getName(); }
    String xLSO() { return LikeSearchOption.class.getName(); }
    String xSSQS() { return HpSSQSetupper.class.getName(); }
}
