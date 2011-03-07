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
 * The abstract condition-query of YS_GROUP.
 * @author DBFlute(AutoGenerator)
 */
public abstract class AbstractBsYsGroupCQ extends AbstractConditionQuery {

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public AbstractBsYsGroupCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
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
        return "YS_GROUP";
    }

    // ===================================================================================
    //                                                                               Query
    //                                                                               =====
    
    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br />
     * (ID)ID: {PK, ID, NotNull, BIGINT(19)}
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
     * Set up ExistsReferrer (co-related sub-query). <br />
     * {exists (select GROUP_ID from YS_GROUP_USER where ...)} <br />
     * YS_GROUP_USER as 'ysGroupUserList'.
     * <pre>
     * cb.query().<span style="color: #FD4747">existsYsGroupUserList</span>(new SubQuery&lt;YsGroupUserCB&gt;() {
     *     public void query(YsGroupCB subCB) {
     *         subCB.query().setXxx...
     *     }
     * });
     * </pre>
     * @param subQuery The sub-query of YsGroupUserList for 'exists'. (NotNull)
     */
    public void existsYsGroupUserList(SubQuery<YsGroupUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsGroupUserCB>", subQuery);
        YsGroupUserCB cb = new YsGroupUserCB(); cb.xsetupForExistsReferrer(this); subQuery.query(cb);
        String subQueryPropertyName = keepId_ExistsReferrer_YsGroupUserList(cb.query()); // for saving query-value.
        registerExistsReferrer(cb.query(), "ID", "GROUP_ID", subQueryPropertyName);
    }
    public abstract String keepId_ExistsReferrer_YsGroupUserList(YsGroupUserCQ subQuery);

    /**
     * Set up ExistsReferrer (co-related sub-query). <br />
     * {exists (select GROUP_ID from YS_ROLE_GROUP_USER where ...)} <br />
     * YS_ROLE_GROUP_USER as 'ysRoleGroupUserList'.
     * <pre>
     * cb.query().<span style="color: #FD4747">existsYsRoleGroupUserList</span>(new SubQuery&lt;YsRoleGroupUserCB&gt;() {
     *     public void query(YsGroupCB subCB) {
     *         subCB.query().setXxx...
     *     }
     * });
     * </pre>
     * @param subQuery The sub-query of YsRoleGroupUserList for 'exists'. (NotNull)
     */
    public void existsYsRoleGroupUserList(SubQuery<YsRoleGroupUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsRoleGroupUserCB>", subQuery);
        YsRoleGroupUserCB cb = new YsRoleGroupUserCB(); cb.xsetupForExistsReferrer(this); subQuery.query(cb);
        String subQueryPropertyName = keepId_ExistsReferrer_YsRoleGroupUserList(cb.query()); // for saving query-value.
        registerExistsReferrer(cb.query(), "ID", "GROUP_ID", subQueryPropertyName);
    }
    public abstract String keepId_ExistsReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery);

    /**
     * Set up NotExistsReferrer (co-related sub-query). <br />
     * {not exists (select GROUP_ID from YS_GROUP_USER where ...)} <br />
     * YS_GROUP_USER as 'ysGroupUserList'.
     * <pre>
     * cb.query().<span style="color: #FD4747">notExistsYsGroupUserList</span>(new SubQuery&lt;YsGroupUserCB&gt;() {
     *     public void query(YsGroupCB subCB) {
     *         subCB.query().setXxx...
     *     }
     * });
     * </pre>
     * @param subQuery The sub-query of Id_NotExistsReferrer_YsGroupUserList for 'not exists'. (NotNull)
     */
    public void notExistsYsGroupUserList(SubQuery<YsGroupUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsGroupUserCB>", subQuery);
        YsGroupUserCB cb = new YsGroupUserCB(); cb.xsetupForExistsReferrer(this); subQuery.query(cb);
        String subQueryPropertyName = keepId_NotExistsReferrer_YsGroupUserList(cb.query()); // for saving query-value.
        registerNotExistsReferrer(cb.query(), "ID", "GROUP_ID", subQueryPropertyName);
    }
    public abstract String keepId_NotExistsReferrer_YsGroupUserList(YsGroupUserCQ subQuery);

    /**
     * Set up NotExistsReferrer (co-related sub-query). <br />
     * {not exists (select GROUP_ID from YS_ROLE_GROUP_USER where ...)} <br />
     * YS_ROLE_GROUP_USER as 'ysRoleGroupUserList'.
     * <pre>
     * cb.query().<span style="color: #FD4747">notExistsYsRoleGroupUserList</span>(new SubQuery&lt;YsRoleGroupUserCB&gt;() {
     *     public void query(YsGroupCB subCB) {
     *         subCB.query().setXxx...
     *     }
     * });
     * </pre>
     * @param subQuery The sub-query of Id_NotExistsReferrer_YsRoleGroupUserList for 'not exists'. (NotNull)
     */
    public void notExistsYsRoleGroupUserList(SubQuery<YsRoleGroupUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsRoleGroupUserCB>", subQuery);
        YsRoleGroupUserCB cb = new YsRoleGroupUserCB(); cb.xsetupForExistsReferrer(this); subQuery.query(cb);
        String subQueryPropertyName = keepId_NotExistsReferrer_YsRoleGroupUserList(cb.query()); // for saving query-value.
        registerNotExistsReferrer(cb.query(), "ID", "GROUP_ID", subQueryPropertyName);
    }
    public abstract String keepId_NotExistsReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery);

    /**
     * Set up InScopeRelation (sub-query). <br />
     * {in (select GROUP_ID from YS_GROUP_USER where ...)} <br />
     * YS_GROUP_USER as 'ysGroupUserList'.
     * @param subQuery The sub-query of YsGroupUserList for 'in-scope'. (NotNull)
     */
    public void inScopeYsGroupUserList(SubQuery<YsGroupUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsGroupUserCB>", subQuery);
        YsGroupUserCB cb = new YsGroupUserCB(); cb.xsetupForInScopeRelation(this); subQuery.query(cb);
        String subQueryPropertyName = keepId_InScopeRelation_YsGroupUserList(cb.query()); // for saving query-value.
        registerInScopeRelation(cb.query(), "ID", "GROUP_ID", subQueryPropertyName);
    }
    public abstract String keepId_InScopeRelation_YsGroupUserList(YsGroupUserCQ subQuery);

    /**
     * Set up InScopeRelation (sub-query). <br />
     * {in (select GROUP_ID from YS_ROLE_GROUP_USER where ...)} <br />
     * YS_ROLE_GROUP_USER as 'ysRoleGroupUserList'.
     * @param subQuery The sub-query of YsRoleGroupUserList for 'in-scope'. (NotNull)
     */
    public void inScopeYsRoleGroupUserList(SubQuery<YsRoleGroupUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsRoleGroupUserCB>", subQuery);
        YsRoleGroupUserCB cb = new YsRoleGroupUserCB(); cb.xsetupForInScopeRelation(this); subQuery.query(cb);
        String subQueryPropertyName = keepId_InScopeRelation_YsRoleGroupUserList(cb.query()); // for saving query-value.
        registerInScopeRelation(cb.query(), "ID", "GROUP_ID", subQueryPropertyName);
    }
    public abstract String keepId_InScopeRelation_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery);

    /**
     * Set up NotInScopeRelation (sub-query). <br />
     * {not in (select GROUP_ID from YS_GROUP_USER where ...)} <br />
     * YS_GROUP_USER as 'ysGroupUserList'.
     * @param subQuery The sub-query of YsGroupUserList for 'not in-scope'. (NotNull)
     */
    public void notInScopeYsGroupUserList(SubQuery<YsGroupUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsGroupUserCB>", subQuery);
        YsGroupUserCB cb = new YsGroupUserCB(); cb.xsetupForInScopeRelation(this); subQuery.query(cb);
        String subQueryPropertyName = keepId_NotInScopeRelation_YsGroupUserList(cb.query()); // for saving query-value.
        registerNotInScopeRelation(cb.query(), "ID", "GROUP_ID", subQueryPropertyName);
    }
    public abstract String keepId_NotInScopeRelation_YsGroupUserList(YsGroupUserCQ subQuery);

    /**
     * Set up NotInScopeRelation (sub-query). <br />
     * {not in (select GROUP_ID from YS_ROLE_GROUP_USER where ...)} <br />
     * YS_ROLE_GROUP_USER as 'ysRoleGroupUserList'.
     * @param subQuery The sub-query of YsRoleGroupUserList for 'not in-scope'. (NotNull)
     */
    public void notInScopeYsRoleGroupUserList(SubQuery<YsRoleGroupUserCB> subQuery) {
        assertObjectNotNull("subQuery<YsRoleGroupUserCB>", subQuery);
        YsRoleGroupUserCB cb = new YsRoleGroupUserCB(); cb.xsetupForInScopeRelation(this); subQuery.query(cb);
        String subQueryPropertyName = keepId_NotInScopeRelation_YsRoleGroupUserList(cb.query()); // for saving query-value.
        registerNotInScopeRelation(cb.query(), "ID", "GROUP_ID", subQueryPropertyName);
    }
    public abstract String keepId_NotInScopeRelation_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery);

    public void xsderiveYsGroupUserList(String function, SubQuery<YsGroupUserCB> subQuery, String aliasName, DerivedReferrerOption option) {
        assertObjectNotNull("subQuery<YsGroupUserCB>", subQuery);
        YsGroupUserCB cb = new YsGroupUserCB(); cb.xsetupForDerivedReferrer(this); subQuery.query(cb);
        String subQueryPropertyName = keepId_SpecifyDerivedReferrer_YsGroupUserList(cb.query()); // for saving query-value.
        registerSpecifyDerivedReferrer(function, cb.query(), "ID", "GROUP_ID", subQueryPropertyName, aliasName, option);
    }
    public abstract String keepId_SpecifyDerivedReferrer_YsGroupUserList(YsGroupUserCQ subQuery);

    public void xsderiveYsRoleGroupUserList(String function, SubQuery<YsRoleGroupUserCB> subQuery, String aliasName, DerivedReferrerOption option) {
        assertObjectNotNull("subQuery<YsRoleGroupUserCB>", subQuery);
        YsRoleGroupUserCB cb = new YsRoleGroupUserCB(); cb.xsetupForDerivedReferrer(this); subQuery.query(cb);
        String subQueryPropertyName = keepId_SpecifyDerivedReferrer_YsRoleGroupUserList(cb.query()); // for saving query-value.
        registerSpecifyDerivedReferrer(function, cb.query(), "ID", "GROUP_ID", subQueryPropertyName, aliasName, option);
    }
    public abstract String keepId_SpecifyDerivedReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery);

    /**
     * Prepare for (Query)DerivedReferrer. <br />
     * {FOO &lt;= (select max(BAR) from YS_GROUP_USER where ...)} <br />
     * YS_GROUP_USER as 'ysGroupUserList'.
     * @return The object to set up a function for referrer table. (NotNull)
     */
    public HpQDRFunction<YsGroupUserCB> derivedYsGroupUserList() {
        return xcreateQDRFunctionYsGroupUserList();
    }
    protected HpQDRFunction<YsGroupUserCB> xcreateQDRFunctionYsGroupUserList() {
        return new HpQDRFunction<YsGroupUserCB>(new HpQDRSetupper<YsGroupUserCB>() {
            public void setup(String function, SubQuery<YsGroupUserCB> subQuery, String operand, Object value, DerivedReferrerOption option) {
                xqderiveYsGroupUserList(function, subQuery, operand, value, option);
            }
        });
    }
    public void xqderiveYsGroupUserList(String function, SubQuery<YsGroupUserCB> subQuery, String operand, Object value, DerivedReferrerOption option) {
        assertObjectNotNull("subQuery<YsGroupUserCB>", subQuery);
        YsGroupUserCB cb = new YsGroupUserCB(); cb.xsetupForDerivedReferrer(this); subQuery.query(cb);
        String subQueryPropertyName = keepId_QueryDerivedReferrer_YsGroupUserList(cb.query()); // for saving query-value.
        String parameterPropertyName = keepId_QueryDerivedReferrer_YsGroupUserListParameter(value);
        registerQueryDerivedReferrer(function, cb.query(), "ID", "GROUP_ID", subQueryPropertyName, operand, value, parameterPropertyName, option);
    }
    public abstract String keepId_QueryDerivedReferrer_YsGroupUserList(YsGroupUserCQ subQuery);
    public abstract String keepId_QueryDerivedReferrer_YsGroupUserListParameter(Object parameterValue);

    /**
     * Prepare for (Query)DerivedReferrer. <br />
     * {FOO &lt;= (select max(BAR) from YS_ROLE_GROUP_USER where ...)} <br />
     * YS_ROLE_GROUP_USER as 'ysRoleGroupUserList'.
     * @return The object to set up a function for referrer table. (NotNull)
     */
    public HpQDRFunction<YsRoleGroupUserCB> derivedYsRoleGroupUserList() {
        return xcreateQDRFunctionYsRoleGroupUserList();
    }
    protected HpQDRFunction<YsRoleGroupUserCB> xcreateQDRFunctionYsRoleGroupUserList() {
        return new HpQDRFunction<YsRoleGroupUserCB>(new HpQDRSetupper<YsRoleGroupUserCB>() {
            public void setup(String function, SubQuery<YsRoleGroupUserCB> subQuery, String operand, Object value, DerivedReferrerOption option) {
                xqderiveYsRoleGroupUserList(function, subQuery, operand, value, option);
            }
        });
    }
    public void xqderiveYsRoleGroupUserList(String function, SubQuery<YsRoleGroupUserCB> subQuery, String operand, Object value, DerivedReferrerOption option) {
        assertObjectNotNull("subQuery<YsRoleGroupUserCB>", subQuery);
        YsRoleGroupUserCB cb = new YsRoleGroupUserCB(); cb.xsetupForDerivedReferrer(this); subQuery.query(cb);
        String subQueryPropertyName = keepId_QueryDerivedReferrer_YsRoleGroupUserList(cb.query()); // for saving query-value.
        String parameterPropertyName = keepId_QueryDerivedReferrer_YsRoleGroupUserListParameter(value);
        registerQueryDerivedReferrer(function, cb.query(), "ID", "GROUP_ID", subQueryPropertyName, operand, value, parameterPropertyName, option);
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
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br />
     * (グループ名)NAME: {UQ, NotNull, VARCHAR(200)}
     * @param name The value of name as equal.
     */
    public void setName_Equal(String name) {
        doSetName_Equal(fRES(name));
    }

    protected void doSetName_Equal(String name) {
        regName(CK_EQ, name);
    }

    /**
     * NotEqual(&lt;&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered.
     * @param name The value of name as notEqual.
     */
    public void setName_NotEqual(String name) {
        doSetName_NotEqual(fRES(name));
    }

    protected void doSetName_NotEqual(String name) {
        regName(CK_NES, name);
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
     * InScope(in ('a', 'b')). And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered.
     * @param nameList The collection of name as inScope.
     */
    public void setName_InScope(Collection<String> nameList) {
        doSetName_InScope(nameList);
    }

    public void doSetName_InScope(Collection<String> nameList) {
        regINS(CK_INS, cTL(nameList), getCValueName(), "NAME");
    }

    /**
     * NotInScope(not in ('a', 'b')). And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered.
     * @param nameList The collection of name as notInScope.
     */
    public void setName_NotInScope(Collection<String> nameList) {
        doSetName_NotInScope(nameList);
    }

    public void doSetName_NotInScope(Collection<String> nameList) {
        regINS(CK_NINS, cTL(nameList), getCValueName(), "NAME");
    }

    /**
     * PrefixSearch(like 'xxx%' escape ...). And NullOrEmptyIgnored, SeveralRegistered.
     * @param name The value of name as prefixSearch.
     */
    public void setName_PrefixSearch(String name) {
        setName_LikeSearch(name, cLSOP());
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...} <br />
     * And NullOrEmptyIgnored, SeveralRegistered.
     * @param name The value of name as likeSearch.
     * @param likeSearchOption The option of like-search. (NotNull)
     */
    public void setName_LikeSearch(String name, LikeSearchOption likeSearchOption) {
        regLSQ(CK_LS, fRES(name), getCValueName(), "NAME", likeSearchOption);
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br />
     * And NullOrEmptyIgnored, SeveralRegistered.
     * @param name The value of name as notLikeSearch.
     * @param likeSearchOption The option of not-like-search. (NotNull)
     */
    public void setName_NotLikeSearch(String name, LikeSearchOption likeSearchOption) {
        regLSQ(CK_NLS, fRES(name), getCValueName(), "NAME", likeSearchOption);
    }

    protected void regName(ConditionKey k, Object v) { regQ(k, v, getCValueName(), "NAME"); }
    abstract protected ConditionValue getCValueName();

    /**
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br />
     * (表示名)DISPLAY_NAME: {NotNull, VARCHAR(200)}
     * @param displayName The value of displayName as equal.
     */
    public void setDisplayName_Equal(String displayName) {
        doSetDisplayName_Equal(fRES(displayName));
    }

    protected void doSetDisplayName_Equal(String displayName) {
        regDisplayName(CK_EQ, displayName);
    }

    /**
     * NotEqual(&lt;&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered.
     * @param displayName The value of displayName as notEqual.
     */
    public void setDisplayName_NotEqual(String displayName) {
        doSetDisplayName_NotEqual(fRES(displayName));
    }

    protected void doSetDisplayName_NotEqual(String displayName) {
        regDisplayName(CK_NES, displayName);
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
     * InScope(in ('a', 'b')). And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered.
     * @param displayNameList The collection of displayName as inScope.
     */
    public void setDisplayName_InScope(Collection<String> displayNameList) {
        doSetDisplayName_InScope(displayNameList);
    }

    public void doSetDisplayName_InScope(Collection<String> displayNameList) {
        regINS(CK_INS, cTL(displayNameList), getCValueDisplayName(), "DISPLAY_NAME");
    }

    /**
     * NotInScope(not in ('a', 'b')). And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered.
     * @param displayNameList The collection of displayName as notInScope.
     */
    public void setDisplayName_NotInScope(Collection<String> displayNameList) {
        doSetDisplayName_NotInScope(displayNameList);
    }

    public void doSetDisplayName_NotInScope(Collection<String> displayNameList) {
        regINS(CK_NINS, cTL(displayNameList), getCValueDisplayName(), "DISPLAY_NAME");
    }

    /**
     * PrefixSearch(like 'xxx%' escape ...). And NullOrEmptyIgnored, SeveralRegistered.
     * @param displayName The value of displayName as prefixSearch.
     */
    public void setDisplayName_PrefixSearch(String displayName) {
        setDisplayName_LikeSearch(displayName, cLSOP());
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...} <br />
     * And NullOrEmptyIgnored, SeveralRegistered.
     * @param displayName The value of displayName as likeSearch.
     * @param likeSearchOption The option of like-search. (NotNull)
     */
    public void setDisplayName_LikeSearch(String displayName, LikeSearchOption likeSearchOption) {
        regLSQ(CK_LS, fRES(displayName), getCValueDisplayName(), "DISPLAY_NAME", likeSearchOption);
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br />
     * And NullOrEmptyIgnored, SeveralRegistered.
     * @param displayName The value of displayName as notLikeSearch.
     * @param likeSearchOption The option of not-like-search. (NotNull)
     */
    public void setDisplayName_NotLikeSearch(String displayName, LikeSearchOption likeSearchOption) {
        regLSQ(CK_NLS, fRES(displayName), getCValueDisplayName(), "DISPLAY_NAME", likeSearchOption);
    }

    protected void regDisplayName(ConditionKey k, Object v) { regQ(k, v, getCValueDisplayName(), "DISPLAY_NAME"); }
    abstract protected ConditionValue getCValueDisplayName();
    
    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br />
     * (作成日時)CREATED_DATE: {NotNull, TIMESTAMP(23, 10)}
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
     * (更新日時)MODIFIED_DATE: {NotNull, TIMESTAMP(23, 10)}
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
     * (バージョン番号)VERSION_NO: {NotNull, BIGINT(19), default=[1]}
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
     * cb.query().<span style="color: #FD4747">scalar_Equal()</span>.max(new SubQuery&lt;YsGroupCB&gt;() {
     *     public void query(YsGroupCB subCB) {
     *         subCB.specify().setXxx... <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setYyy...
     *     }
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSSQFunction<YsGroupCB> scalar_Equal() {
        return xcreateSSQFunction(CK_EQ.getOperand());
    }

    /**
     * Prepare ScalarCondition as equal. <br />
     * {where FOO &lt;&gt; (select max(BAR) from ...)
     * <pre>
     * cb.query().<span style="color: #FD4747">scalar_NotEqual()</span>.max(new SubQuery&lt;YsGroupCB&gt;() {
     *     public void query(YsGroupCB subCB) {
     *         subCB.specify().setXxx... <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setYyy...
     *     }
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSSQFunction<YsGroupCB> scalar_NotEqual() {
        return xcreateSSQFunction(CK_NES.getOperand());
    }

    /**
     * Prepare ScalarCondition as greaterThan. <br />
     * {where FOO &gt; (select max(BAR) from ...)
     * <pre>
     * cb.query().<span style="color: #FD4747">scalar_GreaterThan()</span>.max(new SubQuery&lt;YsGroupCB&gt;() {
     *     public void query(YsGroupCB subCB) {
     *         subCB.specify().setFoo... <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setBar...
     *     }
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSSQFunction<YsGroupCB> scalar_GreaterThan() {
        return xcreateSSQFunction(CK_GT.getOperand());
    }

    /**
     * Prepare ScalarCondition as lessThan. <br />
     * {where FOO &lt; (select max(BAR) from ...)
     * <pre>
     * cb.query().<span style="color: #FD4747">scalar_LessThan()</span>.max(new SubQuery&lt;YsGroupCB&gt;() {
     *     public void query(YsGroupCB subCB) {
     *         subCB.specify().setFoo... <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setBar...
     *     }
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSSQFunction<YsGroupCB> scalar_LessThan() {
        return xcreateSSQFunction(CK_LT.getOperand());
    }

    /**
     * Prepare ScalarCondition as greaterEqual. <br />
     * {where FOO &gt;= (select max(BAR) from ...)
     * <pre>
     * cb.query().<span style="color: #FD4747">scalar_GreaterEqual()</span>.max(new SubQuery&lt;YsGroupCB&gt;() {
     *     public void query(YsGroupCB subCB) {
     *         subCB.specify().setFoo... <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setBar...
     *     }
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSSQFunction<YsGroupCB> scalar_GreaterEqual() {
        return xcreateSSQFunction(CK_GE.getOperand());
    }

    /**
     * Prepare ScalarCondition as lessEqual. <br />
     * {where FOO &lt;= (select max(BAR) from ...)
     * <pre>
     * cb.query().<span style="color: #FD4747">scalar_LessEqual()</span>.max(new SubQuery&lt;YsGroupCB&gt;() {
     *     public void query(YsGroupCB subCB) {
     *         subCB.specify().setFoo... <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setBar...
     *     }
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSSQFunction<YsGroupCB> scalar_LessEqual() {
        return xcreateSSQFunction(CK_LE.getOperand());
    }

    protected HpSSQFunction<YsGroupCB> xcreateSSQFunction(final String operand) {
        return new HpSSQFunction<YsGroupCB>(new HpSSQSetupper<YsGroupCB>() {
            public void setup(String function, SubQuery<YsGroupCB> subQuery) {
                xscalarCondition(function, subQuery, operand);
            }
        });
    }

    protected void xscalarCondition(String function, SubQuery<YsGroupCB> subQuery, String operand) {
        assertObjectNotNull("subQuery<YsGroupCB>", subQuery);
        YsGroupCB cb = new YsGroupCB(); cb.xsetupForScalarCondition(this); subQuery.query(cb);
        String subQueryPropertyName = keepScalarCondition(cb.query()); // for saving query-value.
        registerScalarCondition(function, cb.query(), subQueryPropertyName, operand);
    }
    public abstract String keepScalarCondition(YsGroupCQ subQuery);

    // ===================================================================================
    //                                                                      Myself InScope
    //                                                                      ==============
    /**
     * Myself InScope (SubQuery). {mainly for CLOB and Union}
     * @param subQuery The implementation of sub query. (NotNull)
     */
    public void myselfInScope(SubQuery<YsGroupCB> subQuery) {
        assertObjectNotNull("subQuery<YsGroupCB>", subQuery);
        YsGroupCB cb = new YsGroupCB(); cb.xsetupForInScopeRelation(this); subQuery.query(cb);
        String subQueryPropertyName = keepMyselfInScopeRelation(cb.query()); // for saving query-value.
        registerInScopeRelation(cb.query(), "ID", "ID", subQueryPropertyName);
    }
    public abstract String keepMyselfInScopeRelation(YsGroupCQ subQuery);

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xabCB() { return YsGroupCB.class.getName(); }
    protected String xabCQ() { return YsGroupCQ.class.getName(); }
    protected String xabLSO() { return LikeSearchOption.class.getName(); }
    protected String xabSSQS() { return HpSSQSetupper.class.getName(); }
}
