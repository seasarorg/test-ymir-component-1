package org.seasar.ymir.scaffold.dbflute.cbean.cq.bs;

import java.util.Map;

import org.seasar.dbflute.cbean.*;
import org.seasar.dbflute.cbean.cvalue.ConditionValue;
import org.seasar.dbflute.cbean.sqlclause.SqlClause;
import org.seasar.dbflute.exception.IllegalConditionBeanOperationException;
import org.seasar.ymir.scaffold.dbflute.cbean.cq.ciq.*;
import org.seasar.ymir.scaffold.dbflute.cbean.*;
import org.seasar.ymir.scaffold.dbflute.cbean.cq.*;

/**
 * The base condition-query of YS_ROLE_GROUP_USER.
 * @author DBFlute(AutoGenerator)
 */
public class BsYsRoleGroupUserCQ extends AbstractBsYsRoleGroupUserCQ {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected YsRoleGroupUserCIQ _inlineQuery;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public BsYsRoleGroupUserCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        super(childQuery, sqlClause, aliasName, nestLevel);
    }

    // ===================================================================================
    //                                                                              Inline
    //                                                                              ======
    /**
     * Prepare InlineView query. <br />
     * {select ... from ... left outer join (select * from YS_ROLE_GROUP_USER) where FOO = [value] ...}
     * <pre>
     * cb.query().queryMemberStatus().<span style="color: #FD4747">inline()</span>.setFoo...;
     * </pre>
     * @return The condition-query for InlineView query. (NotNull)
     */
    public YsRoleGroupUserCIQ inline() {
        if (_inlineQuery == null) { _inlineQuery = createInlineQuery(); }
        _inlineQuery.xsetOnClause(false); return _inlineQuery;
    }

    protected YsRoleGroupUserCIQ createInlineQuery()
    { return new YsRoleGroupUserCIQ(xgetReferrerQuery(), xgetSqlClause(), xgetAliasName(), xgetNestLevel(), this); }

    /**
     * Prepare OnClause query. <br />
     * {select ... from ... left outer join YS_ROLE_GROUP_USER on ... and FOO = [value] ...}
     * <pre>
     * cb.query().queryMemberStatus().<span style="color: #FD4747">on()</span>.setFoo...;
     * </pre>
     * @return The condition-query for OnClause query. (NotNull)
     * @throws IllegalConditionBeanOperationException When this condition-query is base query.
     */
    public YsRoleGroupUserCIQ on() {
        if (isBaseQuery()) { throw new IllegalConditionBeanOperationException("OnClause for local table is unavailable!"); }
        YsRoleGroupUserCIQ inlineQuery = inline(); inlineQuery.xsetOnClause(true); return inlineQuery;
    }

    // ===================================================================================
    //                                                                               Query
    //                                                                               =====

    protected ConditionValue _id;
    public ConditionValue getId() {
        if (_id == null) { _id = nCV(); }
        return _id;
    }
    protected ConditionValue getCValueId() { return getId(); }

    /** 
     * Add order-by as ascend.
     * @return this. (NotNull)
     */
    public BsYsRoleGroupUserCQ addOrderBy_Id_Asc() { regOBA("ID"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsRoleGroupUserCQ addOrderBy_Id_Desc() { regOBD("ID"); return this; }

    protected ConditionValue _roleId;
    public ConditionValue getRoleId() {
        if (_roleId == null) { _roleId = nCV(); }
        return _roleId;
    }
    protected ConditionValue getCValueRoleId() { return getRoleId(); }

    protected Map<String, YsRoleCQ> _roleId_InScopeRelation_YsRoleMap;
    public Map<String, YsRoleCQ> getRoleId_InScopeRelation_YsRole() { return _roleId_InScopeRelation_YsRoleMap; }
    public String keepRoleId_InScopeRelation_YsRole(YsRoleCQ subQuery) {
        if (_roleId_InScopeRelation_YsRoleMap == null) { _roleId_InScopeRelation_YsRoleMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_roleId_InScopeRelation_YsRoleMap.size() + 1);
        _roleId_InScopeRelation_YsRoleMap.put(key, subQuery); return "roleId_InScopeRelation_YsRole." + key;
    }

    protected Map<String, YsRoleCQ> _roleId_NotInScopeRelation_YsRoleMap;
    public Map<String, YsRoleCQ> getRoleId_NotInScopeRelation_YsRole() { return _roleId_NotInScopeRelation_YsRoleMap; }
    public String keepRoleId_NotInScopeRelation_YsRole(YsRoleCQ subQuery) {
        if (_roleId_NotInScopeRelation_YsRoleMap == null) { _roleId_NotInScopeRelation_YsRoleMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_roleId_NotInScopeRelation_YsRoleMap.size() + 1);
        _roleId_NotInScopeRelation_YsRoleMap.put(key, subQuery); return "roleId_NotInScopeRelation_YsRole." + key;
    }

    /** 
     * Add order-by as ascend.
     * @return this. (NotNull)
     */
    public BsYsRoleGroupUserCQ addOrderBy_RoleId_Asc() { regOBA("ROLE_ID"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsRoleGroupUserCQ addOrderBy_RoleId_Desc() { regOBD("ROLE_ID"); return this; }

    protected ConditionValue _groupId;
    public ConditionValue getGroupId() {
        if (_groupId == null) { _groupId = nCV(); }
        return _groupId;
    }
    protected ConditionValue getCValueGroupId() { return getGroupId(); }

    protected Map<String, YsGroupCQ> _groupId_InScopeRelation_YsGroupMap;
    public Map<String, YsGroupCQ> getGroupId_InScopeRelation_YsGroup() { return _groupId_InScopeRelation_YsGroupMap; }
    public String keepGroupId_InScopeRelation_YsGroup(YsGroupCQ subQuery) {
        if (_groupId_InScopeRelation_YsGroupMap == null) { _groupId_InScopeRelation_YsGroupMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_groupId_InScopeRelation_YsGroupMap.size() + 1);
        _groupId_InScopeRelation_YsGroupMap.put(key, subQuery); return "groupId_InScopeRelation_YsGroup." + key;
    }

    protected Map<String, YsGroupCQ> _groupId_NotInScopeRelation_YsGroupMap;
    public Map<String, YsGroupCQ> getGroupId_NotInScopeRelation_YsGroup() { return _groupId_NotInScopeRelation_YsGroupMap; }
    public String keepGroupId_NotInScopeRelation_YsGroup(YsGroupCQ subQuery) {
        if (_groupId_NotInScopeRelation_YsGroupMap == null) { _groupId_NotInScopeRelation_YsGroupMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_groupId_NotInScopeRelation_YsGroupMap.size() + 1);
        _groupId_NotInScopeRelation_YsGroupMap.put(key, subQuery); return "groupId_NotInScopeRelation_YsGroup." + key;
    }

    /** 
     * Add order-by as ascend.
     * @return this. (NotNull)
     */
    public BsYsRoleGroupUserCQ addOrderBy_GroupId_Asc() { regOBA("GROUP_ID"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsRoleGroupUserCQ addOrderBy_GroupId_Desc() { regOBD("GROUP_ID"); return this; }

    protected ConditionValue _userId;
    public ConditionValue getUserId() {
        if (_userId == null) { _userId = nCV(); }
        return _userId;
    }
    protected ConditionValue getCValueUserId() { return getUserId(); }

    protected Map<String, YsUserCQ> _userId_InScopeRelation_YsUserMap;
    public Map<String, YsUserCQ> getUserId_InScopeRelation_YsUser() { return _userId_InScopeRelation_YsUserMap; }
    public String keepUserId_InScopeRelation_YsUser(YsUserCQ subQuery) {
        if (_userId_InScopeRelation_YsUserMap == null) { _userId_InScopeRelation_YsUserMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_userId_InScopeRelation_YsUserMap.size() + 1);
        _userId_InScopeRelation_YsUserMap.put(key, subQuery); return "userId_InScopeRelation_YsUser." + key;
    }

    protected Map<String, YsUserCQ> _userId_NotInScopeRelation_YsUserMap;
    public Map<String, YsUserCQ> getUserId_NotInScopeRelation_YsUser() { return _userId_NotInScopeRelation_YsUserMap; }
    public String keepUserId_NotInScopeRelation_YsUser(YsUserCQ subQuery) {
        if (_userId_NotInScopeRelation_YsUserMap == null) { _userId_NotInScopeRelation_YsUserMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_userId_NotInScopeRelation_YsUserMap.size() + 1);
        _userId_NotInScopeRelation_YsUserMap.put(key, subQuery); return "userId_NotInScopeRelation_YsUser." + key;
    }

    /** 
     * Add order-by as ascend.
     * @return this. (NotNull)
     */
    public BsYsRoleGroupUserCQ addOrderBy_UserId_Asc() { regOBA("USER_ID"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsRoleGroupUserCQ addOrderBy_UserId_Desc() { regOBD("USER_ID"); return this; }

    protected ConditionValue _createdDate;
    public ConditionValue getCreatedDate() {
        if (_createdDate == null) { _createdDate = nCV(); }
        return _createdDate;
    }
    protected ConditionValue getCValueCreatedDate() { return getCreatedDate(); }

    /** 
     * Add order-by as ascend.
     * @return this. (NotNull)
     */
    public BsYsRoleGroupUserCQ addOrderBy_CreatedDate_Asc() { regOBA("CREATED_DATE"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsRoleGroupUserCQ addOrderBy_CreatedDate_Desc() { regOBD("CREATED_DATE"); return this; }

    protected ConditionValue _modifiedDate;
    public ConditionValue getModifiedDate() {
        if (_modifiedDate == null) { _modifiedDate = nCV(); }
        return _modifiedDate;
    }
    protected ConditionValue getCValueModifiedDate() { return getModifiedDate(); }

    /** 
     * Add order-by as ascend.
     * @return this. (NotNull)
     */
    public BsYsRoleGroupUserCQ addOrderBy_ModifiedDate_Asc() { regOBA("MODIFIED_DATE"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsRoleGroupUserCQ addOrderBy_ModifiedDate_Desc() { regOBD("MODIFIED_DATE"); return this; }

    protected ConditionValue _versionNo;
    public ConditionValue getVersionNo() {
        if (_versionNo == null) { _versionNo = nCV(); }
        return _versionNo;
    }
    protected ConditionValue getCValueVersionNo() { return getVersionNo(); }

    /** 
     * Add order-by as ascend.
     * @return this. (NotNull)
     */
    public BsYsRoleGroupUserCQ addOrderBy_VersionNo_Asc() { regOBA("VERSION_NO"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsRoleGroupUserCQ addOrderBy_VersionNo_Desc() { regOBD("VERSION_NO"); return this; }

    // ===================================================================================
    //                                                           Specified Derived OrderBy
    //                                                           =========================
    /**
     * Add order-by for specified derived column as ascend.
     * <pre>
     * cb.specify().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchaseDatetime();
     *     }
     * }, <span style="color: #FD4747">aliasName</span>);
     * <span style="color: #3F7E5E">// order by [alias-name] asc</span>
     * cb.<span style="color: #FD4747">addSpecifiedDerivedOrderBy_Asc</span>(<span style="color: #FD4747">aliasName</span>);
     * </pre>
     * @param aliasName The alias name specified at (Specify)DerivedReferrer. (NotNull)
     * @return this. (NotNull)
     */
    public BsYsRoleGroupUserCQ addSpecifiedDerivedOrderBy_Asc(String aliasName)
    { registerSpecifiedDerivedOrderBy_Asc(aliasName); return this; }

    /**
     * Add order-by for specified derived column as descend.
     * <pre>
     * cb.specify().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchaseDatetime();
     *     }
     * }, <span style="color: #FD4747">aliasName</span>);
     * <span style="color: #3F7E5E">// order by [alias-name] desc</span>
     * cb.<span style="color: #FD4747">addSpecifiedDerivedOrderBy_Desc</span>(<span style="color: #FD4747">aliasName</span>);
     * </pre>
     * @param aliasName The alias name specified at (Specify)DerivedReferrer. (NotNull)
     * @return this. (NotNull)
     */
    public BsYsRoleGroupUserCQ addSpecifiedDerivedOrderBy_Desc(String aliasName)
    { registerSpecifiedDerivedOrderBy_Desc(aliasName); return this; }

    // ===================================================================================
    //                                                                         Union Query
    //                                                                         ===========
    protected void reflectRelationOnUnionQuery(ConditionQuery baseQueryAsSuper, ConditionQuery unionQueryAsSuper) {
        YsRoleGroupUserCQ baseQuery = (YsRoleGroupUserCQ)baseQueryAsSuper;
        YsRoleGroupUserCQ unionQuery = (YsRoleGroupUserCQ)unionQueryAsSuper;
        if (baseQuery.hasConditionQueryYsGroup()) {
            unionQuery.queryYsGroup().reflectRelationOnUnionQuery(baseQuery.queryYsGroup(), unionQuery.queryYsGroup());
        }
        if (baseQuery.hasConditionQueryYsRole()) {
            unionQuery.queryYsRole().reflectRelationOnUnionQuery(baseQuery.queryYsRole(), unionQuery.queryYsRole());
        }
        if (baseQuery.hasConditionQueryYsUser()) {
            unionQuery.queryYsUser().reflectRelationOnUnionQuery(baseQuery.queryYsUser(), unionQuery.queryYsUser());
        }
    }

    // ===================================================================================
    //                                                                       Foreign Query
    //                                                                       =============
    /**
     * Get the condition-query for relation table. <br />
     * (グループ)YS_GROUP as 'ysGroup'.
     * @return The instance of condition-query. (NotNull)
     */
    public YsGroupCQ queryYsGroup() {
        return getConditionQueryYsGroup();
    }
    protected YsGroupCQ _conditionQueryYsGroup;
    public YsGroupCQ getConditionQueryYsGroup() {
        if (_conditionQueryYsGroup == null) {
            _conditionQueryYsGroup = xcreateQueryYsGroup();
            xsetupOuterJoinYsGroup();
        }
        return _conditionQueryYsGroup;
    }
    protected YsGroupCQ xcreateQueryYsGroup() {
        String nrp = resolveNextRelationPath("YS_ROLE_GROUP_USER", "ysGroup");
        String jan = resolveJoinAliasName(nrp, xgetNextNestLevel());
        YsGroupCQ cq = new YsGroupCQ(this, xgetSqlClause(), jan, xgetNextNestLevel());
        cq.xsetForeignPropertyName("ysGroup");
        cq.xsetRelationPath(nrp); return cq;
    }
    protected void xsetupOuterJoinYsGroup() {
        YsGroupCQ cq = getConditionQueryYsGroup();
        Map<String, String> joinOnMap = newLinkedHashMap();
        joinOnMap.put("GROUP_ID", "ID");
        registerOuterJoin(cq, joinOnMap);
    }
    public boolean hasConditionQueryYsGroup() {
        return _conditionQueryYsGroup != null;
    }

    /**
     * Get the condition-query for relation table. <br />
     * (ロール)YS_ROLE as 'ysRole'.
     * @return The instance of condition-query. (NotNull)
     */
    public YsRoleCQ queryYsRole() {
        return getConditionQueryYsRole();
    }
    protected YsRoleCQ _conditionQueryYsRole;
    public YsRoleCQ getConditionQueryYsRole() {
        if (_conditionQueryYsRole == null) {
            _conditionQueryYsRole = xcreateQueryYsRole();
            xsetupOuterJoinYsRole();
        }
        return _conditionQueryYsRole;
    }
    protected YsRoleCQ xcreateQueryYsRole() {
        String nrp = resolveNextRelationPath("YS_ROLE_GROUP_USER", "ysRole");
        String jan = resolveJoinAliasName(nrp, xgetNextNestLevel());
        YsRoleCQ cq = new YsRoleCQ(this, xgetSqlClause(), jan, xgetNextNestLevel());
        cq.xsetForeignPropertyName("ysRole");
        cq.xsetRelationPath(nrp); return cq;
    }
    protected void xsetupOuterJoinYsRole() {
        YsRoleCQ cq = getConditionQueryYsRole();
        Map<String, String> joinOnMap = newLinkedHashMap();
        joinOnMap.put("ROLE_ID", "ID");
        registerOuterJoin(cq, joinOnMap);
    }
    public boolean hasConditionQueryYsRole() {
        return _conditionQueryYsRole != null;
    }

    /**
     * Get the condition-query for relation table. <br />
     * (ユーザ)YS_USER as 'ysUser'.
     * @return The instance of condition-query. (NotNull)
     */
    public YsUserCQ queryYsUser() {
        return getConditionQueryYsUser();
    }
    protected YsUserCQ _conditionQueryYsUser;
    public YsUserCQ getConditionQueryYsUser() {
        if (_conditionQueryYsUser == null) {
            _conditionQueryYsUser = xcreateQueryYsUser();
            xsetupOuterJoinYsUser();
        }
        return _conditionQueryYsUser;
    }
    protected YsUserCQ xcreateQueryYsUser() {
        String nrp = resolveNextRelationPath("YS_ROLE_GROUP_USER", "ysUser");
        String jan = resolveJoinAliasName(nrp, xgetNextNestLevel());
        YsUserCQ cq = new YsUserCQ(this, xgetSqlClause(), jan, xgetNextNestLevel());
        cq.xsetForeignPropertyName("ysUser");
        cq.xsetRelationPath(nrp); return cq;
    }
    protected void xsetupOuterJoinYsUser() {
        YsUserCQ cq = getConditionQueryYsUser();
        Map<String, String> joinOnMap = newLinkedHashMap();
        joinOnMap.put("USER_ID", "ID");
        registerOuterJoin(cq, joinOnMap);
    }
    public boolean hasConditionQueryYsUser() {
        return _conditionQueryYsUser != null;
    }

    // ===================================================================================
    //                                                                     Scalar SubQuery
    //                                                                     ===============
    protected Map<String, YsRoleGroupUserCQ> _scalarConditionMap;
    public Map<String, YsRoleGroupUserCQ> getScalarCondition() { return _scalarConditionMap; }
    public String keepScalarCondition(YsRoleGroupUserCQ subQuery) {
        if (_scalarConditionMap == null) { _scalarConditionMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_scalarConditionMap.size() + 1);
        _scalarConditionMap.put(key, subQuery); return "scalarCondition." + key;
    }

    // ===================================================================================
    //                                                             MySelf InScope SubQuery
    //                                                             =======================
    protected Map<String, YsRoleGroupUserCQ> _myselfInScopeRelationMap;
    public Map<String, YsRoleGroupUserCQ> getMyselfInScopeRelation() { return _myselfInScopeRelationMap; }
    public String keepMyselfInScopeRelation(YsRoleGroupUserCQ subQuery) {
        if (_myselfInScopeRelationMap == null) { _myselfInScopeRelationMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_myselfInScopeRelationMap.size() + 1);
        _myselfInScopeRelationMap.put(key, subQuery); return "myselfInScopeRelation." + key;
    }

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xCB() { return YsRoleGroupUserCB.class.getName(); }
    protected String xCQ() { return YsRoleGroupUserCQ.class.getName(); }
    protected String xMap() { return Map.class.getName(); }
}
