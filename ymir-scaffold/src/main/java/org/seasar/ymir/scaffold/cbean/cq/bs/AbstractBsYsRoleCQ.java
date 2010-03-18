package org.seasar.ymir.scaffold.cbean.cq.bs;

import java.util.Collection;

import org.seasar.dbflute.cbean.*;
import org.seasar.dbflute.cbean.chelper.*;
import org.seasar.dbflute.cbean.ckey.*;
import org.seasar.dbflute.cbean.coption.*;
import org.seasar.dbflute.cbean.cvalue.ConditionValue;
import org.seasar.dbflute.cbean.sqlclause.SqlClause;
import org.seasar.dbflute.dbmeta.DBMetaProvider;
import org.seasar.ymir.scaffold.allcommon.*;
import org.seasar.ymir.scaffold.cbean.*;
import org.seasar.ymir.scaffold.cbean.cq.*;

/**
 * The abstract condition-query of YS_ROLE.
 * @author DBFlute(AutoGenerator)
 */
public abstract class AbstractBsYsRoleCQ extends AbstractConditionQuery {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final DBMetaProvider _dbmetaProvider = new DBMetaInstanceHandler();

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public AbstractBsYsRoleCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
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
        return "YS_ROLE";
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

    public void inScopeYsRoleGroupUserList(SubQuery<YsRoleGroupUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsRoleGroupUserCB>", subQuery);
        YsRoleGroupUserCB cb = new YsRoleGroupUserCB(); cb.xsetupForInScopeSubQuery(); subQuery.query(cb);
        String subQueryPropertyName = keepId_InScopeSubQuery_YsRoleGroupUserList(cb.query()); // for saving query-value.
        registerInScopeSubQuery(cb.query(), "ID", "ROLE_ID", subQueryPropertyName);
    }
    public abstract String keepId_InScopeSubQuery_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery);

    public void notInScopeYsRoleGroupUserList(SubQuery<YsRoleGroupUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsRoleGroupUserCB>", subQuery);
        YsRoleGroupUserCB cb = new YsRoleGroupUserCB(); cb.xsetupForInScopeSubQuery(); subQuery.query(cb);
        String subQueryPropertyName = keepId_NotInScopeSubQuery_YsRoleGroupUserList(cb.query()); // for saving query-value.
        registerNotInScopeSubQuery(cb.query(), "ID", "ROLE_ID", subQueryPropertyName);
    }
    public abstract String keepId_NotInScopeSubQuery_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery);

    /**
     * Set up 'exists' sub-query. {exists (select ROLE_ID from YS_ROLE_GROUP_USER where ...)}
     * @param subQuery The sub-query of Id_ExistsSubQuery_YsRoleGroupUserList for 'exists'. (NotNull)
     */
    public void existsYsRoleGroupUserList(SubQuery<YsRoleGroupUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsRoleGroupUserCB>", subQuery);
        YsRoleGroupUserCB cb = new YsRoleGroupUserCB(); cb.xsetupForExistsSubQuery(); subQuery.query(cb);
        String subQueryPropertyName = keepId_ExistsSubQuery_YsRoleGroupUserList(cb.query()); // for saving query-value.
        registerExistsSubQuery(cb.query(), "ID", "ROLE_ID", subQueryPropertyName);
    }
    public abstract String keepId_ExistsSubQuery_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery);

    /**
     * Set up 'not exists' sub-query. {not exists (select ROLE_ID from YS_ROLE_GROUP_USER where ...)}
     * @param subQuery The sub-query of Id_NotExistsSubQuery_YsRoleGroupUserList for 'not exists'. (NotNull)
     */
    public void notExistsYsRoleGroupUserList(SubQuery<YsRoleGroupUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsRoleGroupUserCB>", subQuery);
        YsRoleGroupUserCB cb = new YsRoleGroupUserCB(); cb.xsetupForExistsSubQuery(); subQuery.query(cb);
        String subQueryPropertyName = keepId_NotExistsSubQuery_YsRoleGroupUserList(cb.query()); // for saving query-value.
        registerNotExistsSubQuery(cb.query(), "ID", "ROLE_ID", subQueryPropertyName);
    }
    public abstract String keepId_NotExistsSubQuery_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery);

    public void xsderiveYsRoleGroupUserList(String function, SubQuery<YsRoleGroupUserCB> subQuery, String aliasName) {
        assertObjectNotNull("subQuery<YsRoleGroupUserCB>", subQuery);
        YsRoleGroupUserCB cb = new YsRoleGroupUserCB(); cb.xsetupForDerivedReferrer(); subQuery.query(cb);
        String subQueryPropertyName = keepId_SpecifyDerivedReferrer_YsRoleGroupUserList(cb.query()); // for saving query-value.
        registerSpecifyDerivedReferrer(function, cb.query(), "ID", "ROLE_ID", subQueryPropertyName, aliasName);
    }
    public abstract String keepId_SpecifyDerivedReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery);

    public HpQDRFunction<YsRoleGroupUserCB> derivedYsRoleGroupUserList() {
        return xcreateQDRFunctionYsRoleGroupUserList();
    }
    protected HpQDRFunction<YsRoleGroupUserCB> xcreateQDRFunctionYsRoleGroupUserList() {
        return new HpQDRFunction<YsRoleGroupUserCB>(new HpQDRSetupper<YsRoleGroupUserCB>() {
            public void setup(String function, SubQuery<YsRoleGroupUserCB> subQuery, String operand, Object value) {
                xqderiveYsRoleGroupUserList(function, subQuery, operand, value);
            }
        });
    }
    public void xqderiveYsRoleGroupUserList(String function, SubQuery<YsRoleGroupUserCB> subQuery, String operand, Object value) {
        assertObjectNotNull("subQuery<YsRoleGroupUserCB>", subQuery);
        YsRoleGroupUserCB cb = new YsRoleGroupUserCB(); cb.xsetupForDerivedReferrer(); subQuery.query(cb);
        String subQueryPropertyName = keepId_QueryDerivedReferrer_YsRoleGroupUserList(cb.query()); // for saving query-value.
        String parameterPropertyName = keepId_QueryDerivedReferrer_YsRoleGroupUserListParameter(value);
        registerQueryDerivedReferrer(function, cb.query(), "ID", "ROLE_ID", subQueryPropertyName, operand, value, parameterPropertyName);
    }
    public abstract String keepId_QueryDerivedReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery);
    public abstract String keepId_QueryDerivedReferrer_YsRoleGroupUserListParameter(Object parameterValue);

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
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. {UQ : NotNull : VARCHAR(200)}
     * @param name The value of name as equal.
     */
    public void setName_Equal(String name) {
        doSetName_Equal(fRES(name));
    }

    protected void doSetName_Equal(String name) {
        regName(CK_EQ, name);
    }

    /**
     * NotEqual(!=). And NullOrEmptyIgnored, OnlyOnceRegistered.
     * @param name The value of name as notEqual.
     */
    public void setName_NotEqual(String name) {
        doSetName_NotEqual(fRES(name));
    }

    protected void doSetName_NotEqual(String name) {
        regName(CK_NE, name);
    }

    /**
     * GreaterThan(&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered.
     * @param name The value of name as greaterThan.
     */
    public void setName_GreaterThan(String name) {
        regName(CK_GT, fRES(name));
    }

    /**
     * LessThan(&lt;). And NullOrEmptyIgnored, OnlyOnceRegistered.
     * @param name The value of name as lessThan.
     */
    public void setName_LessThan(String name) {
        regName(CK_LT, fRES(name));
    }

    /**
     * GreaterEqual(&gt;=). And NullOrEmptyIgnored, OnlyOnceRegistered.
     * @param name The value of name as greaterEqual.
     */
    public void setName_GreaterEqual(String name) {
        regName(CK_GE, fRES(name));
    }

    /**
     * LessEqual(&lt;=). And NullOrEmptyIgnored, OnlyOnceRegistered.
     * @param name The value of name as lessEqual.
     */
    public void setName_LessEqual(String name) {
        regName(CK_LE, fRES(name));
    }

    /**
     * PrefixSearch(like 'xxx%' escape ...). And NullOrEmptyIgnored, SeveralRegistered.
     * @param name The value of name as prefixSearch.
     */
    public void setName_PrefixSearch(String name) {
        setName_LikeSearch(name, cLSOP());
    }

    /**
     * InScope(in ('a', 'b')). And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered.
     * @param nameList The collection of name as inScope.
     */
    public void setName_InScope(Collection<String> nameList) {
        regINS(CK_INS, cTL(nameList), getCValueName(), "NAME");
    }

    /**
     * NotInScope(not in ('a', 'b')). And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered.
     * @param nameList The collection of name as notInScope.
     */
    public void setName_NotInScope(Collection<String> nameList) {
        regINS(CK_NINS, cTL(nameList), getCValueName(), "NAME");
    }

    /**
     * LikeSearch(like 'xxx%' escape ...). And NullOrEmptyIgnored, SeveralRegistered.
     * @param name The value of name as likeSearch.
     * @param likeSearchOption The option of like-search. (NotNull)
     */
    public void setName_LikeSearch(String name, LikeSearchOption likeSearchOption) {
        regLSQ(CK_LS, fRES(name), getCValueName(), "NAME", likeSearchOption);
    }

    /**
     * NotLikeSearch(not like 'xxx%' escape ...). And NullOrEmptyIgnored, SeveralRegistered.
     * @param name The value of name as notLikeSearch.
     * @param likeSearchOption The option of not-like-search. (NotNull)
     */
    public void setName_NotLikeSearch(String name, LikeSearchOption likeSearchOption) {
        regLSQ(CK_NLS, fRES(name), getCValueName(), "NAME", likeSearchOption);
    }

    protected void regName(ConditionKey k, Object v) { regQ(k, v, getCValueName(), "NAME"); }
    abstract protected ConditionValue getCValueName();

    /**
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. {NotNull : VARCHAR(200)}
     * @param displayName The value of displayName as equal.
     */
    public void setDisplayName_Equal(String displayName) {
        doSetDisplayName_Equal(fRES(displayName));
    }

    protected void doSetDisplayName_Equal(String displayName) {
        regDisplayName(CK_EQ, displayName);
    }

    /**
     * NotEqual(!=). And NullOrEmptyIgnored, OnlyOnceRegistered.
     * @param displayName The value of displayName as notEqual.
     */
    public void setDisplayName_NotEqual(String displayName) {
        doSetDisplayName_NotEqual(fRES(displayName));
    }

    protected void doSetDisplayName_NotEqual(String displayName) {
        regDisplayName(CK_NE, displayName);
    }

    /**
     * GreaterThan(&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered.
     * @param displayName The value of displayName as greaterThan.
     */
    public void setDisplayName_GreaterThan(String displayName) {
        regDisplayName(CK_GT, fRES(displayName));
    }

    /**
     * LessThan(&lt;). And NullOrEmptyIgnored, OnlyOnceRegistered.
     * @param displayName The value of displayName as lessThan.
     */
    public void setDisplayName_LessThan(String displayName) {
        regDisplayName(CK_LT, fRES(displayName));
    }

    /**
     * GreaterEqual(&gt;=). And NullOrEmptyIgnored, OnlyOnceRegistered.
     * @param displayName The value of displayName as greaterEqual.
     */
    public void setDisplayName_GreaterEqual(String displayName) {
        regDisplayName(CK_GE, fRES(displayName));
    }

    /**
     * LessEqual(&lt;=). And NullOrEmptyIgnored, OnlyOnceRegistered.
     * @param displayName The value of displayName as lessEqual.
     */
    public void setDisplayName_LessEqual(String displayName) {
        regDisplayName(CK_LE, fRES(displayName));
    }

    /**
     * PrefixSearch(like 'xxx%' escape ...). And NullOrEmptyIgnored, SeveralRegistered.
     * @param displayName The value of displayName as prefixSearch.
     */
    public void setDisplayName_PrefixSearch(String displayName) {
        setDisplayName_LikeSearch(displayName, cLSOP());
    }

    /**
     * InScope(in ('a', 'b')). And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered.
     * @param displayNameList The collection of displayName as inScope.
     */
    public void setDisplayName_InScope(Collection<String> displayNameList) {
        regINS(CK_INS, cTL(displayNameList), getCValueDisplayName(), "DISPLAY_NAME");
    }

    /**
     * NotInScope(not in ('a', 'b')). And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered.
     * @param displayNameList The collection of displayName as notInScope.
     */
    public void setDisplayName_NotInScope(Collection<String> displayNameList) {
        regINS(CK_NINS, cTL(displayNameList), getCValueDisplayName(), "DISPLAY_NAME");
    }

    /**
     * LikeSearch(like 'xxx%' escape ...). And NullOrEmptyIgnored, SeveralRegistered.
     * @param displayName The value of displayName as likeSearch.
     * @param likeSearchOption The option of like-search. (NotNull)
     */
    public void setDisplayName_LikeSearch(String displayName, LikeSearchOption likeSearchOption) {
        regLSQ(CK_LS, fRES(displayName), getCValueDisplayName(), "DISPLAY_NAME", likeSearchOption);
    }

    /**
     * NotLikeSearch(not like 'xxx%' escape ...). And NullOrEmptyIgnored, SeveralRegistered.
     * @param displayName The value of displayName as notLikeSearch.
     * @param likeSearchOption The option of not-like-search. (NotNull)
     */
    public void setDisplayName_NotLikeSearch(String displayName, LikeSearchOption likeSearchOption) {
        regLSQ(CK_NLS, fRES(displayName), getCValueDisplayName(), "DISPLAY_NAME", likeSearchOption);
    }

    protected void regDisplayName(ConditionKey k, Object v) { regQ(k, v, getCValueDisplayName(), "DISPLAY_NAME"); }
    abstract protected ConditionValue getCValueDisplayName();
    
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
    public HpSSQFunction<YsRoleCB> scalar_Equal() {
        return xcreateSSQFunction("=");
    }

    public HpSSQFunction<YsRoleCB> scalar_GreaterEqual() {
        return xcreateSSQFunction(">=");
    }

    public HpSSQFunction<YsRoleCB> scalar_GreaterThan() {
        return xcreateSSQFunction(">");
    }

    public HpSSQFunction<YsRoleCB> scalar_LessEqual() {
        return xcreateSSQFunction("<=");
    }
    
    public HpSSQFunction<YsRoleCB> scalar_LessThan() {
        return xcreateSSQFunction("<");
    }
    
    protected HpSSQFunction<YsRoleCB> xcreateSSQFunction(final String operand) {
        return new HpSSQFunction<YsRoleCB>(new HpSSQSetupper<YsRoleCB>() {
            public void setup(String function, SubQuery<YsRoleCB> subQuery) {
                xscalarSubQuery(function, subQuery, operand);
            }
        });
    }

    protected void xscalarSubQuery(String function, SubQuery<YsRoleCB> subQuery, String operand) {
        assertObjectNotNull("subQuery<YsRoleCB>", subQuery);
        YsRoleCB cb = new YsRoleCB(); cb.xsetupForScalarSubQuery(); subQuery.query(cb);
        String subQueryPropertyName = keepScalarSubQuery(cb.query()); // for saving query-value.
        registerScalarSubQuery(function, cb.query(), subQueryPropertyName, operand);
    }
    public abstract String keepScalarSubQuery(YsRoleCQ subQuery);

    // ===================================================================================
    //                                                             MySelf InScope SubQuery
    //                                                             =======================
    /**
     * Myself InScope SubQuery. {mainly for CLOB and Union}
     * @param subQuery The implementation of sub query. (NotNull)
     */
    public void myselfInScope(SubQuery<YsRoleCB> subQuery) {
        assertObjectNotNull("subQuery<YsRoleCB>", subQuery);
        YsRoleCB cb = new YsRoleCB(); cb.xsetupForInScopeSubQuery(); subQuery.query(cb);
        String subQueryPropertyName = keepMyselfInScopeSubQuery(cb.query()); // for saving query-value.
        registerInScopeSubQuery(cb.query(), "ID", "ID", subQueryPropertyName);
    }
    public abstract String keepMyselfInScopeSubQuery(YsRoleCQ subQuery);

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // Very Internal (for Suppressing Warn about 'Not Use Import')
    String xCB() { return YsRoleCB.class.getName(); }
    String xCQ() { return YsRoleCQ.class.getName(); }
    String xLSO() { return LikeSearchOption.class.getName(); }
    String xSSQS() { return HpSSQSetupper.class.getName(); }
}
