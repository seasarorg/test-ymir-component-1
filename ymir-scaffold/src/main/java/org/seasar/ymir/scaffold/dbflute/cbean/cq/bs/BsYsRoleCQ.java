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
 * The base condition-query of YS_ROLE.
 * @author DBFlute(AutoGenerator)
 */
public class BsYsRoleCQ extends AbstractBsYsRoleCQ {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected YsRoleCIQ _inlineQuery;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public BsYsRoleCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        super(childQuery, sqlClause, aliasName, nestLevel);
    }

    // ===================================================================================
    //                                                                              Inline
    //                                                                              ======
    /**
     * Prepare InlineView query. <br />
     * {select ... from ... left outer join (select * from YS_ROLE) where FOO = [value] ...}
     * <pre>
     * cb.query().queryMemberStatus().<span style="color: #FD4747">inline()</span>.setFoo...;
     * </pre>
     * @return The condition-query for InlineView query. (NotNull)
     */
    public YsRoleCIQ inline() {
        if (_inlineQuery == null) { _inlineQuery = createInlineQuery(); }
        _inlineQuery.xsetOnClause(false); return _inlineQuery;
    }

    protected YsRoleCIQ createInlineQuery()
    { return new YsRoleCIQ(xgetReferrerQuery(), xgetSqlClause(), xgetAliasName(), xgetNestLevel(), this); }

    /**
     * Prepare OnClause query. <br />
     * {select ... from ... left outer join YS_ROLE on ... and FOO = [value] ...}
     * <pre>
     * cb.query().queryMemberStatus().<span style="color: #FD4747">on()</span>.setFoo...;
     * </pre>
     * @return The condition-query for OnClause query. (NotNull)
     * @throws IllegalConditionBeanOperationException When this condition-query is base query.
     */
    public YsRoleCIQ on() {
        if (isBaseQuery()) { throw new IllegalConditionBeanOperationException("OnClause for local table is unavailable!"); }
        YsRoleCIQ inlineQuery = inline(); inlineQuery.xsetOnClause(true); return inlineQuery;
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

    protected Map<String, YsRoleGroupUserCQ> _id_ExistsReferrer_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_ExistsReferrer_YsRoleGroupUserList() { return _id_ExistsReferrer_YsRoleGroupUserListMap; }
    public String keepId_ExistsReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_ExistsReferrer_YsRoleGroupUserListMap == null) { _id_ExistsReferrer_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_ExistsReferrer_YsRoleGroupUserListMap.size() + 1);
        _id_ExistsReferrer_YsRoleGroupUserListMap.put(key, subQuery); return "id_ExistsReferrer_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_NotExistsReferrer_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_NotExistsReferrer_YsRoleGroupUserList() { return _id_NotExistsReferrer_YsRoleGroupUserListMap; }
    public String keepId_NotExistsReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_NotExistsReferrer_YsRoleGroupUserListMap == null) { _id_NotExistsReferrer_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_NotExistsReferrer_YsRoleGroupUserListMap.size() + 1);
        _id_NotExistsReferrer_YsRoleGroupUserListMap.put(key, subQuery); return "id_NotExistsReferrer_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_SpecifyDerivedReferrer_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_SpecifyDerivedReferrer_YsRoleGroupUserList() { return _id_SpecifyDerivedReferrer_YsRoleGroupUserListMap; }
    public String keepId_SpecifyDerivedReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_SpecifyDerivedReferrer_YsRoleGroupUserListMap == null) { _id_SpecifyDerivedReferrer_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_SpecifyDerivedReferrer_YsRoleGroupUserListMap.size() + 1);
        _id_SpecifyDerivedReferrer_YsRoleGroupUserListMap.put(key, subQuery); return "id_SpecifyDerivedReferrer_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_InScopeRelation_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_InScopeRelation_YsRoleGroupUserList() { return _id_InScopeRelation_YsRoleGroupUserListMap; }
    public String keepId_InScopeRelation_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_InScopeRelation_YsRoleGroupUserListMap == null) { _id_InScopeRelation_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_InScopeRelation_YsRoleGroupUserListMap.size() + 1);
        _id_InScopeRelation_YsRoleGroupUserListMap.put(key, subQuery); return "id_InScopeRelation_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_NotInScopeRelation_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_NotInScopeRelation_YsRoleGroupUserList() { return _id_NotInScopeRelation_YsRoleGroupUserListMap; }
    public String keepId_NotInScopeRelation_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_NotInScopeRelation_YsRoleGroupUserListMap == null) { _id_NotInScopeRelation_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_NotInScopeRelation_YsRoleGroupUserListMap.size() + 1);
        _id_NotInScopeRelation_YsRoleGroupUserListMap.put(key, subQuery); return "id_NotInScopeRelation_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_QueryDerivedReferrer_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_QueryDerivedReferrer_YsRoleGroupUserList() { return _id_QueryDerivedReferrer_YsRoleGroupUserListMap; }
    public String keepId_QueryDerivedReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_QueryDerivedReferrer_YsRoleGroupUserListMap == null) { _id_QueryDerivedReferrer_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_QueryDerivedReferrer_YsRoleGroupUserListMap.size() + 1);
        _id_QueryDerivedReferrer_YsRoleGroupUserListMap.put(key, subQuery); return "id_QueryDerivedReferrer_YsRoleGroupUserList." + key;
    }
    protected Map<String, Object> _id_QueryDerivedReferrer_YsRoleGroupUserListParameterMap;
    public Map<String, Object> getId_QueryDerivedReferrer_YsRoleGroupUserListParameter() { return _id_QueryDerivedReferrer_YsRoleGroupUserListParameterMap; }
    public String keepId_QueryDerivedReferrer_YsRoleGroupUserListParameter(Object parameterValue) {
        if (_id_QueryDerivedReferrer_YsRoleGroupUserListParameterMap == null) { _id_QueryDerivedReferrer_YsRoleGroupUserListParameterMap = newLinkedHashMap(); }
        String key = "subQueryParameterKey" + (_id_QueryDerivedReferrer_YsRoleGroupUserListParameterMap.size() + 1);
        _id_QueryDerivedReferrer_YsRoleGroupUserListParameterMap.put(key, parameterValue); return "id_QueryDerivedReferrer_YsRoleGroupUserListParameter." + key;
    }

    /** 
     * Add order-by as ascend.
     * @return this. (NotNull)
     */
    public BsYsRoleCQ addOrderBy_Id_Asc() { regOBA("ID"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsRoleCQ addOrderBy_Id_Desc() { regOBD("ID"); return this; }

    protected ConditionValue _name;
    public ConditionValue getName() {
        if (_name == null) { _name = nCV(); }
        return _name;
    }
    protected ConditionValue getCValueName() { return getName(); }

    /** 
     * Add order-by as ascend.
     * @return this. (NotNull)
     */
    public BsYsRoleCQ addOrderBy_Name_Asc() { regOBA("NAME"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsRoleCQ addOrderBy_Name_Desc() { regOBD("NAME"); return this; }

    protected ConditionValue _displayName;
    public ConditionValue getDisplayName() {
        if (_displayName == null) { _displayName = nCV(); }
        return _displayName;
    }
    protected ConditionValue getCValueDisplayName() { return getDisplayName(); }

    /** 
     * Add order-by as ascend.
     * @return this. (NotNull)
     */
    public BsYsRoleCQ addOrderBy_DisplayName_Asc() { regOBA("DISPLAY_NAME"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsRoleCQ addOrderBy_DisplayName_Desc() { regOBD("DISPLAY_NAME"); return this; }

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
    public BsYsRoleCQ addOrderBy_CreatedDate_Asc() { regOBA("CREATED_DATE"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsRoleCQ addOrderBy_CreatedDate_Desc() { regOBD("CREATED_DATE"); return this; }

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
    public BsYsRoleCQ addOrderBy_ModifiedDate_Asc() { regOBA("MODIFIED_DATE"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsRoleCQ addOrderBy_ModifiedDate_Desc() { regOBD("MODIFIED_DATE"); return this; }

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
    public BsYsRoleCQ addOrderBy_VersionNo_Asc() { regOBA("VERSION_NO"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsRoleCQ addOrderBy_VersionNo_Desc() { regOBD("VERSION_NO"); return this; }

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
    public BsYsRoleCQ addSpecifiedDerivedOrderBy_Asc(String aliasName)
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
    public BsYsRoleCQ addSpecifiedDerivedOrderBy_Desc(String aliasName)
    { registerSpecifiedDerivedOrderBy_Desc(aliasName); return this; }

    // ===================================================================================
    //                                                                         Union Query
    //                                                                         ===========
    protected void reflectRelationOnUnionQuery(ConditionQuery baseQueryAsSuper, ConditionQuery unionQueryAsSuper) {
    }

    // ===================================================================================
    //                                                                       Foreign Query
    //                                                                       =============
    // ===================================================================================
    //                                                                     Scalar SubQuery
    //                                                                     ===============
    protected Map<String, YsRoleCQ> _scalarConditionMap;
    public Map<String, YsRoleCQ> getScalarCondition() { return _scalarConditionMap; }
    public String keepScalarCondition(YsRoleCQ subQuery) {
        if (_scalarConditionMap == null) { _scalarConditionMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_scalarConditionMap.size() + 1);
        _scalarConditionMap.put(key, subQuery); return "scalarCondition." + key;
    }

    // ===================================================================================
    //                                                             MySelf InScope SubQuery
    //                                                             =======================
    protected Map<String, YsRoleCQ> _myselfInScopeRelationMap;
    public Map<String, YsRoleCQ> getMyselfInScopeRelation() { return _myselfInScopeRelationMap; }
    public String keepMyselfInScopeRelation(YsRoleCQ subQuery) {
        if (_myselfInScopeRelationMap == null) { _myselfInScopeRelationMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_myselfInScopeRelationMap.size() + 1);
        _myselfInScopeRelationMap.put(key, subQuery); return "myselfInScopeRelation." + key;
    }

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xCB() { return YsRoleCB.class.getName(); }
    protected String xCQ() { return YsRoleCQ.class.getName(); }
    protected String xMap() { return Map.class.getName(); }
}
