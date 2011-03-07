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
    //                                                                         Constructor
    //                                                                         ===========
    public AbstractBsYsRoleGroupUserCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        super(childQuery, sqlClause, aliasName, nestLevel);
    }

    // ===================================================================================
    //                                                                     DBMeta Provider
    //                                                                     ===============
    @Override
    protected DBMetaProvider xgetDBMetaProvider() {
        return DBMetaInstanceHandler.getProvider();
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
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br />
     * ID: {PK, ID, NotNull, BIGINT(19)}
     * @param id The value of id as equal.
     */
    public void setId_Equal(Long id) {
        doSetId_Equal(id);
    }

    protected void doSetId_Equal(Long id) {
        regId(CK_EQ, id);
    }

    /**
     * NotEqual(&lt;&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param id The value of id as notEqual.
     */
    public void setId_NotEqual(Long id) {
        doSetId_NotEqual(id);
    }

    protected void doSetId_NotEqual(Long id) {
        regId(CK_NES, id);
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
        doSetId_InScope(idList);
    }

    protected void doSetId_InScope(Collection<Long> idList) {
        regINS(CK_INS, cTL(idList), getCValueId(), "ID");
    }

    /**
     * NotInScope(not in (1, 2)). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param idList The collection of id as notInScope.
     */
    public void setId_NotInScope(Collection<Long> idList) {
        doSetId_NotInScope(idList);
    }

    protected void doSetId_NotInScope(Collection<Long> idList) {
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
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br />
     * ROLE_ID: {UQ, NotNull, BIGINT(19), FK to YS_ROLE}
     * @param roleId The value of roleId as equal.
     */
    public void setRoleId_Equal(Long roleId) {
        doSetRoleId_Equal(roleId);
    }

    protected void doSetRoleId_Equal(Long roleId) {
        regRoleId(CK_EQ, roleId);
    }

    /**
     * NotEqual(&lt;&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param roleId The value of roleId as notEqual.
     */
    public void setRoleId_NotEqual(Long roleId) {
        doSetRoleId_NotEqual(roleId);
    }

    protected void doSetRoleId_NotEqual(Long roleId) {
        regRoleId(CK_NES, roleId);
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
        doSetRoleId_InScope(roleIdList);
    }

    protected void doSetRoleId_InScope(Collection<Long> roleIdList) {
        regINS(CK_INS, cTL(roleIdList), getCValueRoleId(), "ROLE_ID");
    }

    /**
     * NotInScope(not in (1, 2)). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param roleIdList The collection of roleId as notInScope.
     */
    public void setRoleId_NotInScope(Collection<Long> roleIdList) {
        doSetRoleId_NotInScope(roleIdList);
    }

    protected void doSetRoleId_NotInScope(Collection<Long> roleIdList) {
        regINS(CK_NINS, cTL(roleIdList), getCValueRoleId(), "ROLE_ID");
    }

    /**
     * Set up InScopeRelation (sub-query). <br />
     * {in (select ROLE_ID from YS_ROLE where ...)} <br />
     * (ロール)YS_ROLE as 'ysRole'.
     * @param subQuery The sub-query of YsRole for 'in-scope'. (NotNull)
     */
    public void inScopeYsRole(SubQuery<YsRoleCB> subQuery) {
        assertObjectNotNull("subQuery<YsRoleCB>", subQuery);
        YsRoleCB cb = new YsRoleCB(); cb.xsetupForInScopeRelation(this); subQuery.query(cb);
        String subQueryPropertyName = keepRoleId_InScopeRelation_YsRole(cb.query()); // for saving query-value.
        registerInScopeRelation(cb.query(), "ROLE_ID", "ID", subQueryPropertyName);
    }
    public abstract String keepRoleId_InScopeRelation_YsRole(YsRoleCQ subQuery);

    /**
     * Set up NotInScopeRelation (sub-query). <br />
     * {not in (select ROLE_ID from YS_ROLE where ...)} <br />
     * (ロール)YS_ROLE as 'ysRole'.
     * @param subQuery The sub-query of YsRole for 'not in-scope'. (NotNull)
     */
    public void notInScopeYsRole(SubQuery<YsRoleCB> subQuery) {
        assertObjectNotNull("subQuery<YsRoleCB>", subQuery);
        YsRoleCB cb = new YsRoleCB(); cb.xsetupForInScopeRelation(this); subQuery.query(cb);
        String subQueryPropertyName = keepRoleId_NotInScopeRelation_YsRole(cb.query()); // for saving query-value.
        registerNotInScopeRelation(cb.query(), "ROLE_ID", "ID", subQueryPropertyName);
    }
    public abstract String keepRoleId_NotInScopeRelation_YsRole(YsRoleCQ subQuery);

    protected void regRoleId(ConditionKey k, Object v) { regQ(k, v, getCValueRoleId(), "ROLE_ID"); }
    abstract protected ConditionValue getCValueRoleId();
    
    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br />
     * GROUP_ID: {UQ+, IX, BIGINT(19), FK to YS_GROUP}
     * @param groupId The value of groupId as equal.
     */
    public void setGroupId_Equal(Long groupId) {
        doSetGroupId_Equal(groupId);
    }

    protected void doSetGroupId_Equal(Long groupId) {
        regGroupId(CK_EQ, groupId);
    }

    /**
     * NotEqual(&lt;&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param groupId The value of groupId as notEqual.
     */
    public void setGroupId_NotEqual(Long groupId) {
        doSetGroupId_NotEqual(groupId);
    }

    protected void doSetGroupId_NotEqual(Long groupId) {
        regGroupId(CK_NES, groupId);
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
        doSetGroupId_InScope(groupIdList);
    }

    protected void doSetGroupId_InScope(Collection<Long> groupIdList) {
        regINS(CK_INS, cTL(groupIdList), getCValueGroupId(), "GROUP_ID");
    }

    /**
     * NotInScope(not in (1, 2)). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param groupIdList The collection of groupId as notInScope.
     */
    public void setGroupId_NotInScope(Collection<Long> groupIdList) {
        doSetGroupId_NotInScope(groupIdList);
    }

    protected void doSetGroupId_NotInScope(Collection<Long> groupIdList) {
        regINS(CK_NINS, cTL(groupIdList), getCValueGroupId(), "GROUP_ID");
    }

    /**
     * Set up InScopeRelation (sub-query). <br />
     * {in (select GROUP_ID from YS_GROUP where ...)} <br />
     * (グループ)YS_GROUP as 'ysGroup'.
     * @param subQuery The sub-query of YsGroup for 'in-scope'. (NotNull)
     */
    public void inScopeYsGroup(SubQuery<YsGroupCB> subQuery) {
        assertObjectNotNull("subQuery<YsGroupCB>", subQuery);
        YsGroupCB cb = new YsGroupCB(); cb.xsetupForInScopeRelation(this); subQuery.query(cb);
        String subQueryPropertyName = keepGroupId_InScopeRelation_YsGroup(cb.query()); // for saving query-value.
        registerInScopeRelation(cb.query(), "GROUP_ID", "ID", subQueryPropertyName);
    }
    public abstract String keepGroupId_InScopeRelation_YsGroup(YsGroupCQ subQuery);

    /**
     * Set up NotInScopeRelation (sub-query). <br />
     * {not in (select GROUP_ID from YS_GROUP where ...)} <br />
     * (グループ)YS_GROUP as 'ysGroup'.
     * @param subQuery The sub-query of YsGroup for 'not in-scope'. (NotNull)
     */
    public void notInScopeYsGroup(SubQuery<YsGroupCB> subQuery) {
        assertObjectNotNull("subQuery<YsGroupCB>", subQuery);
        YsGroupCB cb = new YsGroupCB(); cb.xsetupForInScopeRelation(this); subQuery.query(cb);
        String subQueryPropertyName = keepGroupId_NotInScopeRelation_YsGroup(cb.query()); // for saving query-value.
        registerNotInScopeRelation(cb.query(), "GROUP_ID", "ID", subQueryPropertyName);
    }
    public abstract String keepGroupId_NotInScopeRelation_YsGroup(YsGroupCQ subQuery);

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
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br />
     * USER_ID: {UQ+, IX, BIGINT(19), FK to YS_USER}
     * @param userId The value of userId as equal.
     */
    public void setUserId_Equal(Long userId) {
        doSetUserId_Equal(userId);
    }

    protected void doSetUserId_Equal(Long userId) {
        regUserId(CK_EQ, userId);
    }

    /**
     * NotEqual(&lt;&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param userId The value of userId as notEqual.
     */
    public void setUserId_NotEqual(Long userId) {
        doSetUserId_NotEqual(userId);
    }

    protected void doSetUserId_NotEqual(Long userId) {
        regUserId(CK_NES, userId);
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
        doSetUserId_InScope(userIdList);
    }

    protected void doSetUserId_InScope(Collection<Long> userIdList) {
        regINS(CK_INS, cTL(userIdList), getCValueUserId(), "USER_ID");
    }

    /**
     * NotInScope(not in (1, 2)). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param userIdList The collection of userId as notInScope.
     */
    public void setUserId_NotInScope(Collection<Long> userIdList) {
        doSetUserId_NotInScope(userIdList);
    }

    protected void doSetUserId_NotInScope(Collection<Long> userIdList) {
        regINS(CK_NINS, cTL(userIdList), getCValueUserId(), "USER_ID");
    }

    /**
     * Set up InScopeRelation (sub-query). <br />
     * {in (select USER_ID from YS_USER where ...)} <br />
     * (ユーザ)YS_USER as 'ysUser'.
     * @param subQuery The sub-query of YsUser for 'in-scope'. (NotNull)
     */
    public void inScopeYsUser(SubQuery<YsUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsUserCB>", subQuery);
        YsUserCB cb = new YsUserCB(); cb.xsetupForInScopeRelation(this); subQuery.query(cb);
        String subQueryPropertyName = keepUserId_InScopeRelation_YsUser(cb.query()); // for saving query-value.
        registerInScopeRelation(cb.query(), "USER_ID", "ID", subQueryPropertyName);
    }
    public abstract String keepUserId_InScopeRelation_YsUser(YsUserCQ subQuery);

    /**
     * Set up NotInScopeRelation (sub-query). <br />
     * {not in (select USER_ID from YS_USER where ...)} <br />
     * (ユーザ)YS_USER as 'ysUser'.
     * @param subQuery The sub-query of YsUser for 'not in-scope'. (NotNull)
     */
    public void notInScopeYsUser(SubQuery<YsUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsUserCB>", subQuery);
        YsUserCB cb = new YsUserCB(); cb.xsetupForInScopeRelation(this); subQuery.query(cb);
        String subQueryPropertyName = keepUserId_NotInScopeRelation_YsUser(cb.query()); // for saving query-value.
        registerNotInScopeRelation(cb.query(), "USER_ID", "ID", subQueryPropertyName);
    }
    public abstract String keepUserId_NotInScopeRelation_YsUser(YsUserCQ subQuery);

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
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br />
     * CREATED_DATE: {NotNull, TIMESTAMP(23, 10)}
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
     * LessThan(&lt;). And NullIgnored, OnlyOnceRegistered.
     * @param createdDate The value of createdDate as lessThan.
     */
    public void setCreatedDate_LessThan(java.sql.Timestamp createdDate) {
        regCreatedDate(CK_LT,  createdDate);
    }

    /**
     * GreaterEqual(&gt;=). And NullIgnored, OnlyOnceRegistered.
     * @param createdDate The value of createdDate as greaterEqual.
     */
    public void setCreatedDate_GreaterEqual(java.sql.Timestamp createdDate) {
        regCreatedDate(CK_GE,  createdDate);
    }

    /**
     * LessEqual(&lt;=). And NullIgnored, OnlyOnceRegistered.
     * @param createdDate The value of createdDate as lessEqual.
     */
    public void setCreatedDate_LessEqual(java.sql.Timestamp createdDate) {
        regCreatedDate(CK_LE, createdDate);
    }

    /**
     * FromTo with various options. (versatile) <br />
     * {(default) fromDatetime &lt;= column &lt;= toDatetime} <br />
     * And NullIgnored, OnlyOnceRegistered.
     * @param fromDatetime The from-datetime(yyyy/MM/dd HH:mm:ss.SSS) of createdDate. (Nullable)
     * @param toDatetime The to-datetime(yyyy/MM/dd HH:mm:ss.SSS) of createdDate. (Nullable)
     * @param fromToOption The option of from-to. (NotNull)
     */
    public void setCreatedDate_FromTo(java.util.Date fromDatetime, java.util.Date toDatetime, FromToOption fromToOption) {
        regFTQ((fromDatetime != null ? new java.sql.Timestamp(fromDatetime.getTime()) : null), (toDatetime != null ? new java.sql.Timestamp(toDatetime.getTime()) : null), getCValueCreatedDate(), "CREATED_DATE", fromToOption);
    }

    /**
     * DateFromTo. (Date means yyyy/MM/dd) <br />
     * {fromDate &lt;= column &lt; toDate + 1 day} <br />
     * And NullIgnored, OnlyOnceRegistered.
     * <pre>
     * ex) from:{2007/04/10 08:24:53} to:{2007/04/16 14:36:29}
     *     --&gt; column &gt;= '2007/04/10 00:00:00'
     *     and column <span style="color: #FD4747">&lt; '2007/04/17 00:00:00'</span>
     * </pre>
     * @param fromDate The from-date(yyyy/MM/dd) of createdDate. (Nullable)
     * @param toDate The to-date(yyyy/MM/dd) of createdDate. (Nullable)
     */
    public void setCreatedDate_DateFromTo(java.util.Date fromDate, java.util.Date toDate) {
        setCreatedDate_FromTo(fromDate, toDate, new DateFromToOption());
    }

    /**
     * InScope(in ('1965-03-03', '1966-09-15')). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param createdDateList The collection of createdDate as inScope.
     */
    public void setCreatedDate_InScope(Collection<java.sql.Timestamp> createdDateList) {
        doSetCreatedDate_InScope(createdDateList);
    }

    protected void doSetCreatedDate_InScope(Collection<java.sql.Timestamp> createdDateList) {
        regINS(CK_INS, cTL(createdDateList), getCValueCreatedDate(), "CREATED_DATE");
    }

    /**
     * NotInScope(not in ('1965-03-03', '1966-09-15')). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param createdDateList The collection of createdDate as notInScope.
     */
    public void setCreatedDate_NotInScope(Collection<java.sql.Timestamp> createdDateList) {
        doSetCreatedDate_NotInScope(createdDateList);
    }

    protected void doSetCreatedDate_NotInScope(Collection<java.sql.Timestamp> createdDateList) {
        regINS(CK_NINS, cTL(createdDateList), getCValueCreatedDate(), "CREATED_DATE");
    }

    protected void regCreatedDate(ConditionKey k, Object v) { regQ(k, v, getCValueCreatedDate(), "CREATED_DATE"); }
    abstract protected ConditionValue getCValueCreatedDate();
    
    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br />
     * MODIFIED_DATE: {NotNull, TIMESTAMP(23, 10)}
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
     * LessThan(&lt;). And NullIgnored, OnlyOnceRegistered.
     * @param modifiedDate The value of modifiedDate as lessThan.
     */
    public void setModifiedDate_LessThan(java.sql.Timestamp modifiedDate) {
        regModifiedDate(CK_LT,  modifiedDate);
    }

    /**
     * GreaterEqual(&gt;=). And NullIgnored, OnlyOnceRegistered.
     * @param modifiedDate The value of modifiedDate as greaterEqual.
     */
    public void setModifiedDate_GreaterEqual(java.sql.Timestamp modifiedDate) {
        regModifiedDate(CK_GE,  modifiedDate);
    }

    /**
     * LessEqual(&lt;=). And NullIgnored, OnlyOnceRegistered.
     * @param modifiedDate The value of modifiedDate as lessEqual.
     */
    public void setModifiedDate_LessEqual(java.sql.Timestamp modifiedDate) {
        regModifiedDate(CK_LE, modifiedDate);
    }

    /**
     * FromTo with various options. (versatile) <br />
     * {(default) fromDatetime &lt;= column &lt;= toDatetime} <br />
     * And NullIgnored, OnlyOnceRegistered.
     * @param fromDatetime The from-datetime(yyyy/MM/dd HH:mm:ss.SSS) of modifiedDate. (Nullable)
     * @param toDatetime The to-datetime(yyyy/MM/dd HH:mm:ss.SSS) of modifiedDate. (Nullable)
     * @param fromToOption The option of from-to. (NotNull)
     */
    public void setModifiedDate_FromTo(java.util.Date fromDatetime, java.util.Date toDatetime, FromToOption fromToOption) {
        regFTQ((fromDatetime != null ? new java.sql.Timestamp(fromDatetime.getTime()) : null), (toDatetime != null ? new java.sql.Timestamp(toDatetime.getTime()) : null), getCValueModifiedDate(), "MODIFIED_DATE", fromToOption);
    }

    /**
     * DateFromTo. (Date means yyyy/MM/dd) <br />
     * {fromDate &lt;= column &lt; toDate + 1 day} <br />
     * And NullIgnored, OnlyOnceRegistered.
     * <pre>
     * ex) from:{2007/04/10 08:24:53} to:{2007/04/16 14:36:29}
     *     --&gt; column &gt;= '2007/04/10 00:00:00'
     *     and column <span style="color: #FD4747">&lt; '2007/04/17 00:00:00'</span>
     * </pre>
     * @param fromDate The from-date(yyyy/MM/dd) of modifiedDate. (Nullable)
     * @param toDate The to-date(yyyy/MM/dd) of modifiedDate. (Nullable)
     */
    public void setModifiedDate_DateFromTo(java.util.Date fromDate, java.util.Date toDate) {
        setModifiedDate_FromTo(fromDate, toDate, new DateFromToOption());
    }

    /**
     * InScope(in ('1965-03-03', '1966-09-15')). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param modifiedDateList The collection of modifiedDate as inScope.
     */
    public void setModifiedDate_InScope(Collection<java.sql.Timestamp> modifiedDateList) {
        doSetModifiedDate_InScope(modifiedDateList);
    }

    protected void doSetModifiedDate_InScope(Collection<java.sql.Timestamp> modifiedDateList) {
        regINS(CK_INS, cTL(modifiedDateList), getCValueModifiedDate(), "MODIFIED_DATE");
    }

    /**
     * NotInScope(not in ('1965-03-03', '1966-09-15')). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param modifiedDateList The collection of modifiedDate as notInScope.
     */
    public void setModifiedDate_NotInScope(Collection<java.sql.Timestamp> modifiedDateList) {
        doSetModifiedDate_NotInScope(modifiedDateList);
    }

    protected void doSetModifiedDate_NotInScope(Collection<java.sql.Timestamp> modifiedDateList) {
        regINS(CK_NINS, cTL(modifiedDateList), getCValueModifiedDate(), "MODIFIED_DATE");
    }

    protected void regModifiedDate(ConditionKey k, Object v) { regQ(k, v, getCValueModifiedDate(), "MODIFIED_DATE"); }
    abstract protected ConditionValue getCValueModifiedDate();
    
    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br />
     * VERSION_NO: {NotNull, BIGINT(19), default=[1]}
     * @param versionNo The value of versionNo as equal.
     */
    public void setVersionNo_Equal(Long versionNo) {
        doSetVersionNo_Equal(versionNo);
    }

    protected void doSetVersionNo_Equal(Long versionNo) {
        regVersionNo(CK_EQ, versionNo);
    }

    /**
     * NotEqual(&lt;&gt;). And NullIgnored, OnlyOnceRegistered.
     * @param versionNo The value of versionNo as notEqual.
     */
    public void setVersionNo_NotEqual(Long versionNo) {
        doSetVersionNo_NotEqual(versionNo);
    }

    protected void doSetVersionNo_NotEqual(Long versionNo) {
        regVersionNo(CK_NES, versionNo);
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
        doSetVersionNo_InScope(versionNoList);
    }

    protected void doSetVersionNo_InScope(Collection<Long> versionNoList) {
        regINS(CK_INS, cTL(versionNoList), getCValueVersionNo(), "VERSION_NO");
    }

    /**
     * NotInScope(not in (1, 2)). And NullIgnored, NullElementIgnored, SeveralRegistered.
     * @param versionNoList The collection of versionNo as notInScope.
     */
    public void setVersionNo_NotInScope(Collection<Long> versionNoList) {
        doSetVersionNo_NotInScope(versionNoList);
    }

    protected void doSetVersionNo_NotInScope(Collection<Long> versionNoList) {
        regINS(CK_NINS, cTL(versionNoList), getCValueVersionNo(), "VERSION_NO");
    }

    protected void regVersionNo(ConditionKey k, Object v) { regQ(k, v, getCValueVersionNo(), "VERSION_NO"); }
    abstract protected ConditionValue getCValueVersionNo();

    // ===================================================================================
    //                                                                    Scalar Condition
    //                                                                    ================
    /**
     * Prepare ScalarCondition as equal. <br />
     * {where FOO = (select max(BAR) from ...)
     * <pre>
     * cb.query().<span style="color: #FD4747">scalar_Equal()</span>.max(new SubQuery&lt;YsRoleGroupUserCB&gt;() {
     *     public void query(YsRoleGroupUserCB subCB) {
     *         subCB.specify().setXxx... <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setYyy...
     *     }
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSSQFunction<YsRoleGroupUserCB> scalar_Equal() {
        return xcreateSSQFunction(CK_EQ.getOperand());
    }

    /**
     * Prepare ScalarCondition as equal. <br />
     * {where FOO &lt;&gt; (select max(BAR) from ...)
     * <pre>
     * cb.query().<span style="color: #FD4747">scalar_NotEqual()</span>.max(new SubQuery&lt;YsRoleGroupUserCB&gt;() {
     *     public void query(YsRoleGroupUserCB subCB) {
     *         subCB.specify().setXxx... <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setYyy...
     *     }
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSSQFunction<YsRoleGroupUserCB> scalar_NotEqual() {
        return xcreateSSQFunction(CK_NES.getOperand());
    }

    /**
     * Prepare ScalarCondition as greaterThan. <br />
     * {where FOO &gt; (select max(BAR) from ...)
     * <pre>
     * cb.query().<span style="color: #FD4747">scalar_GreaterThan()</span>.max(new SubQuery&lt;YsRoleGroupUserCB&gt;() {
     *     public void query(YsRoleGroupUserCB subCB) {
     *         subCB.specify().setFoo... <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setBar...
     *     }
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSSQFunction<YsRoleGroupUserCB> scalar_GreaterThan() {
        return xcreateSSQFunction(CK_GT.getOperand());
    }

    /**
     * Prepare ScalarCondition as lessThan. <br />
     * {where FOO &lt; (select max(BAR) from ...)
     * <pre>
     * cb.query().<span style="color: #FD4747">scalar_LessThan()</span>.max(new SubQuery&lt;YsRoleGroupUserCB&gt;() {
     *     public void query(YsRoleGroupUserCB subCB) {
     *         subCB.specify().setFoo... <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setBar...
     *     }
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSSQFunction<YsRoleGroupUserCB> scalar_LessThan() {
        return xcreateSSQFunction(CK_LT.getOperand());
    }

    /**
     * Prepare ScalarCondition as greaterEqual. <br />
     * {where FOO &gt;= (select max(BAR) from ...)
     * <pre>
     * cb.query().<span style="color: #FD4747">scalar_GreaterEqual()</span>.max(new SubQuery&lt;YsRoleGroupUserCB&gt;() {
     *     public void query(YsRoleGroupUserCB subCB) {
     *         subCB.specify().setFoo... <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setBar...
     *     }
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSSQFunction<YsRoleGroupUserCB> scalar_GreaterEqual() {
        return xcreateSSQFunction(CK_GE.getOperand());
    }

    /**
     * Prepare ScalarCondition as lessEqual. <br />
     * {where FOO &lt;= (select max(BAR) from ...)
     * <pre>
     * cb.query().<span style="color: #FD4747">scalar_LessEqual()</span>.max(new SubQuery&lt;YsRoleGroupUserCB&gt;() {
     *     public void query(YsRoleGroupUserCB subCB) {
     *         subCB.specify().setFoo... <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setBar...
     *     }
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSSQFunction<YsRoleGroupUserCB> scalar_LessEqual() {
        return xcreateSSQFunction(CK_LE.getOperand());
    }

    protected HpSSQFunction<YsRoleGroupUserCB> xcreateSSQFunction(final String operand) {
        return new HpSSQFunction<YsRoleGroupUserCB>(new HpSSQSetupper<YsRoleGroupUserCB>() {
            public void setup(String function, SubQuery<YsRoleGroupUserCB> subQuery) {
                xscalarCondition(function, subQuery, operand);
            }
        });
    }

    protected void xscalarCondition(String function, SubQuery<YsRoleGroupUserCB> subQuery, String operand) {
        assertObjectNotNull("subQuery<YsRoleGroupUserCB>", subQuery);
        YsRoleGroupUserCB cb = new YsRoleGroupUserCB(); cb.xsetupForScalarCondition(this); subQuery.query(cb);
        String subQueryPropertyName = keepScalarCondition(cb.query()); // for saving query-value.
        registerScalarCondition(function, cb.query(), subQueryPropertyName, operand);
    }
    public abstract String keepScalarCondition(YsRoleGroupUserCQ subQuery);

    // ===================================================================================
    //                                                                      Myself InScope
    //                                                                      ==============
    /**
     * Myself InScope (SubQuery). {mainly for CLOB and Union}
     * @param subQuery The implementation of sub query. (NotNull)
     */
    public void myselfInScope(SubQuery<YsRoleGroupUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsRoleGroupUserCB>", subQuery);
        YsRoleGroupUserCB cb = new YsRoleGroupUserCB(); cb.xsetupForInScopeRelation(this); subQuery.query(cb);
        String subQueryPropertyName = keepMyselfInScopeRelation(cb.query()); // for saving query-value.
        registerInScopeRelation(cb.query(), "ID", "ID", subQueryPropertyName);
    }
    public abstract String keepMyselfInScopeRelation(YsRoleGroupUserCQ subQuery);

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xabCB() { return YsRoleGroupUserCB.class.getName(); }
    protected String xabCQ() { return YsRoleGroupUserCQ.class.getName(); }
    protected String xabLSO() { return LikeSearchOption.class.getName(); }
    protected String xabSSQS() { return HpSSQSetupper.class.getName(); }
}
