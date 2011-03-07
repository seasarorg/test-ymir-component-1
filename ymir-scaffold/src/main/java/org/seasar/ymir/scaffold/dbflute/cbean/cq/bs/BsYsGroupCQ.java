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
 * The base condition-query of YS_GROUP.
 * @author DBFlute(AutoGenerator)
 */
public class BsYsGroupCQ extends AbstractBsYsGroupCQ {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected YsGroupCIQ _inlineQuery;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public BsYsGroupCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        super(childQuery, sqlClause, aliasName, nestLevel);
    }

    // ===================================================================================
    //                                                                              Inline
    //                                                                              ======
    /**
     * Prepare InlineView query. <br />
     * {select ... from ... left outer join (select * from YS_GROUP) where FOO = [value] ...}
     * <pre>
     * cb.query().queryMemberStatus().<span style="color: #FD4747">inline()</span>.setFoo...;
     * </pre>
     * @return The condition-query for InlineView query. (NotNull)
     */
    public YsGroupCIQ inline() {
        if (_inlineQuery == null) { _inlineQuery = createInlineQuery(); }
        _inlineQuery.xsetOnClause(false); return _inlineQuery;
    }

    protected YsGroupCIQ createInlineQuery()
    { return new YsGroupCIQ(xgetReferrerQuery(), xgetSqlClause(), xgetAliasName(), xgetNestLevel(), this); }

    /**
     * Prepare OnClause query. <br />
     * {select ... from ... left outer join YS_GROUP on ... and FOO = [value] ...}
     * <pre>
     * cb.query().queryMemberStatus().<span style="color: #FD4747">on()</span>.setFoo...;
     * </pre>
     * @return The condition-query for OnClause query. (NotNull)
     * @throws IllegalConditionBeanOperationException When this condition-query is base query.
     */
    public YsGroupCIQ on() {
        if (isBaseQuery()) { throw new IllegalConditionBeanOperationException("OnClause for local table is unavailable!"); }
        YsGroupCIQ inlineQuery = inline(); inlineQuery.xsetOnClause(true); return inlineQuery;
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

    protected Map<String, YsGroupUserCQ> _id_ExistsReferrer_YsGroupUserListMap;
    public Map<String, YsGroupUserCQ> getId_ExistsReferrer_YsGroupUserList() { return _id_ExistsReferrer_YsGroupUserListMap; }
    public String keepId_ExistsReferrer_YsGroupUserList(YsGroupUserCQ subQuery) {
        if (_id_ExistsReferrer_YsGroupUserListMap == null) { _id_ExistsReferrer_YsGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_ExistsReferrer_YsGroupUserListMap.size() + 1);
        _id_ExistsReferrer_YsGroupUserListMap.put(key, subQuery); return "id_ExistsReferrer_YsGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_ExistsReferrer_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_ExistsReferrer_YsRoleGroupUserList() { return _id_ExistsReferrer_YsRoleGroupUserListMap; }
    public String keepId_ExistsReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_ExistsReferrer_YsRoleGroupUserListMap == null) { _id_ExistsReferrer_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_ExistsReferrer_YsRoleGroupUserListMap.size() + 1);
        _id_ExistsReferrer_YsRoleGroupUserListMap.put(key, subQuery); return "id_ExistsReferrer_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsGroupUserCQ> _id_NotExistsReferrer_YsGroupUserListMap;
    public Map<String, YsGroupUserCQ> getId_NotExistsReferrer_YsGroupUserList() { return _id_NotExistsReferrer_YsGroupUserListMap; }
    public String keepId_NotExistsReferrer_YsGroupUserList(YsGroupUserCQ subQuery) {
        if (_id_NotExistsReferrer_YsGroupUserListMap == null) { _id_NotExistsReferrer_YsGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_NotExistsReferrer_YsGroupUserListMap.size() + 1);
        _id_NotExistsReferrer_YsGroupUserListMap.put(key, subQuery); return "id_NotExistsReferrer_YsGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_NotExistsReferrer_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_NotExistsReferrer_YsRoleGroupUserList() { return _id_NotExistsReferrer_YsRoleGroupUserListMap; }
    public String keepId_NotExistsReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_NotExistsReferrer_YsRoleGroupUserListMap == null) { _id_NotExistsReferrer_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_NotExistsReferrer_YsRoleGroupUserListMap.size() + 1);
        _id_NotExistsReferrer_YsRoleGroupUserListMap.put(key, subQuery); return "id_NotExistsReferrer_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsGroupUserCQ> _id_SpecifyDerivedReferrer_YsGroupUserListMap;
    public Map<String, YsGroupUserCQ> getId_SpecifyDerivedReferrer_YsGroupUserList() { return _id_SpecifyDerivedReferrer_YsGroupUserListMap; }
    public String keepId_SpecifyDerivedReferrer_YsGroupUserList(YsGroupUserCQ subQuery) {
        if (_id_SpecifyDerivedReferrer_YsGroupUserListMap == null) { _id_SpecifyDerivedReferrer_YsGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_SpecifyDerivedReferrer_YsGroupUserListMap.size() + 1);
        _id_SpecifyDerivedReferrer_YsGroupUserListMap.put(key, subQuery); return "id_SpecifyDerivedReferrer_YsGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_SpecifyDerivedReferrer_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_SpecifyDerivedReferrer_YsRoleGroupUserList() { return _id_SpecifyDerivedReferrer_YsRoleGroupUserListMap; }
    public String keepId_SpecifyDerivedReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_SpecifyDerivedReferrer_YsRoleGroupUserListMap == null) { _id_SpecifyDerivedReferrer_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_SpecifyDerivedReferrer_YsRoleGroupUserListMap.size() + 1);
        _id_SpecifyDerivedReferrer_YsRoleGroupUserListMap.put(key, subQuery); return "id_SpecifyDerivedReferrer_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsGroupUserCQ> _id_InScopeRelation_YsGroupUserListMap;
    public Map<String, YsGroupUserCQ> getId_InScopeRelation_YsGroupUserList() { return _id_InScopeRelation_YsGroupUserListMap; }
    public String keepId_InScopeRelation_YsGroupUserList(YsGroupUserCQ subQuery) {
        if (_id_InScopeRelation_YsGroupUserListMap == null) { _id_InScopeRelation_YsGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_InScopeRelation_YsGroupUserListMap.size() + 1);
        _id_InScopeRelation_YsGroupUserListMap.put(key, subQuery); return "id_InScopeRelation_YsGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_InScopeRelation_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_InScopeRelation_YsRoleGroupUserList() { return _id_InScopeRelation_YsRoleGroupUserListMap; }
    public String keepId_InScopeRelation_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_InScopeRelation_YsRoleGroupUserListMap == null) { _id_InScopeRelation_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_InScopeRelation_YsRoleGroupUserListMap.size() + 1);
        _id_InScopeRelation_YsRoleGroupUserListMap.put(key, subQuery); return "id_InScopeRelation_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsGroupUserCQ> _id_NotInScopeRelation_YsGroupUserListMap;
    public Map<String, YsGroupUserCQ> getId_NotInScopeRelation_YsGroupUserList() { return _id_NotInScopeRelation_YsGroupUserListMap; }
    public String keepId_NotInScopeRelation_YsGroupUserList(YsGroupUserCQ subQuery) {
        if (_id_NotInScopeRelation_YsGroupUserListMap == null) { _id_NotInScopeRelation_YsGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_NotInScopeRelation_YsGroupUserListMap.size() + 1);
        _id_NotInScopeRelation_YsGroupUserListMap.put(key, subQuery); return "id_NotInScopeRelation_YsGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_NotInScopeRelation_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_NotInScopeRelation_YsRoleGroupUserList() { return _id_NotInScopeRelation_YsRoleGroupUserListMap; }
    public String keepId_NotInScopeRelation_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_NotInScopeRelation_YsRoleGroupUserListMap == null) { _id_NotInScopeRelation_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_NotInScopeRelation_YsRoleGroupUserListMap.size() + 1);
        _id_NotInScopeRelation_YsRoleGroupUserListMap.put(key, subQuery); return "id_NotInScopeRelation_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsGroupUserCQ> _id_QueryDerivedReferrer_YsGroupUserListMap;
    public Map<String, YsGroupUserCQ> getId_QueryDerivedReferrer_YsGroupUserList() { return _id_QueryDerivedReferrer_YsGroupUserListMap; }
    public String keepId_QueryDerivedReferrer_YsGroupUserList(YsGroupUserCQ subQuery) {
        if (_id_QueryDerivedReferrer_YsGroupUserListMap == null) { _id_QueryDerivedReferrer_YsGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_QueryDerivedReferrer_YsGroupUserListMap.size() + 1);
        _id_QueryDerivedReferrer_YsGroupUserListMap.put(key, subQuery); return "id_QueryDerivedReferrer_YsGroupUserList." + key;
    }
    protected Map<String, Object> _id_QueryDerivedReferrer_YsGroupUserListParameterMap;
    public Map<String, Object> getId_QueryDerivedReferrer_YsGroupUserListParameter() { return _id_QueryDerivedReferrer_YsGroupUserListParameterMap; }
    public String keepId_QueryDerivedReferrer_YsGroupUserListParameter(Object parameterValue) {
        if (_id_QueryDerivedReferrer_YsGroupUserListParameterMap == null) { _id_QueryDerivedReferrer_YsGroupUserListParameterMap = newLinkedHashMap(); }
        String key = "subQueryParameterKey" + (_id_QueryDerivedReferrer_YsGroupUserListParameterMap.size() + 1);
        _id_QueryDerivedReferrer_YsGroupUserListParameterMap.put(key, parameterValue); return "id_QueryDerivedReferrer_YsGroupUserListParameter." + key;
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
    public BsYsGroupCQ addOrderBy_Id_Asc() { regOBA("ID"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsGroupCQ addOrderBy_Id_Desc() { regOBD("ID"); return this; }

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
    public BsYsGroupCQ addOrderBy_Name_Asc() { regOBA("NAME"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsGroupCQ addOrderBy_Name_Desc() { regOBD("NAME"); return this; }

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
    public BsYsGroupCQ addOrderBy_DisplayName_Asc() { regOBA("DISPLAY_NAME"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsGroupCQ addOrderBy_DisplayName_Desc() { regOBD("DISPLAY_NAME"); return this; }

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
    public BsYsGroupCQ addOrderBy_CreatedDate_Asc() { regOBA("CREATED_DATE"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsGroupCQ addOrderBy_CreatedDate_Desc() { regOBD("CREATED_DATE"); return this; }

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
    public BsYsGroupCQ addOrderBy_ModifiedDate_Asc() { regOBA("MODIFIED_DATE"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsGroupCQ addOrderBy_ModifiedDate_Desc() { regOBD("MODIFIED_DATE"); return this; }

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
    public BsYsGroupCQ addOrderBy_VersionNo_Asc() { regOBA("VERSION_NO"); return this; }
    /**
     * Add order-by as descend.
     * @return this. (NotNull)
     */
    public BsYsGroupCQ addOrderBy_VersionNo_Desc() { regOBD("VERSION_NO"); return this; }

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
    public BsYsGroupCQ addSpecifiedDerivedOrderBy_Asc(String aliasName)
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
    public BsYsGroupCQ addSpecifiedDerivedOrderBy_Desc(String aliasName)
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
    protected Map<String, YsGroupCQ> _scalarConditionMap;
    public Map<String, YsGroupCQ> getScalarCondition() { return _scalarConditionMap; }
    public String keepScalarCondition(YsGroupCQ subQuery) {
        if (_scalarConditionMap == null) { _scalarConditionMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_scalarConditionMap.size() + 1);
        _scalarConditionMap.put(key, subQuery); return "scalarCondition." + key;
    }

    // ===================================================================================
    //                                                             MySelf InScope SubQuery
    //                                                             =======================
    protected Map<String, YsGroupCQ> _myselfInScopeRelationMap;
    public Map<String, YsGroupCQ> getMyselfInScopeRelation() { return _myselfInScopeRelationMap; }
    public String keepMyselfInScopeRelation(YsGroupCQ subQuery) {
        if (_myselfInScopeRelationMap == null) { _myselfInScopeRelationMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_myselfInScopeRelationMap.size() + 1);
        _myselfInScopeRelationMap.put(key, subQuery); return "myselfInScopeRelation." + key;
    }

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xCB() { return YsGroupCB.class.getName(); }
    protected String xCQ() { return YsGroupCQ.class.getName(); }
    protected String xMap() { return Map.class.getName(); }
}
