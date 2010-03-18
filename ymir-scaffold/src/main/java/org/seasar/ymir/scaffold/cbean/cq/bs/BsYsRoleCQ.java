package org.seasar.ymir.scaffold.cbean.cq.bs;

import java.util.Map;

import org.seasar.dbflute.cbean.*;
import org.seasar.dbflute.cbean.cvalue.ConditionValue;
import org.seasar.dbflute.cbean.sqlclause.SqlClause;
import org.seasar.dbflute.exception.IllegalConditionBeanOperationException;
import org.seasar.ymir.scaffold.cbean.cq.ciq.*;
import org.seasar.ymir.scaffold.cbean.*;
import org.seasar.ymir.scaffold.cbean.cq.*;

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
     * Prepare inline query. <br />
     * {select ... from ... left outer join (select * from YS_ROLE) where abc = [abc] ...}
     * @return Inline query. (NotNull)
     */
    public YsRoleCIQ inline() {
        if (_inlineQuery == null) { _inlineQuery = createInlineQuery(); }
        _inlineQuery.xsetOnClauseInline(false); return _inlineQuery;
    }

    protected YsRoleCIQ createInlineQuery()
    { return new YsRoleCIQ(getReferrerQuery(), getSqlClause(), getAliasName(), getNestLevel(), this); }

    /**
     * Prepare on-clause query. <br />
     * {select ... from ... left outer join YS_ROLE on ... and abc = [abc] ...}
     * @return On-clause query. (NotNull)
     */
    public YsRoleCIQ on() {
        if (isBaseQuery(this)) { throw new IllegalConditionBeanOperationException("On-clause for local table is unavailable!"); }
        YsRoleCIQ inlineQuery = inline(); inlineQuery.xsetOnClauseInline(true); return inlineQuery;
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

    protected Map<String, YsRoleGroupUserCQ> _id_InScopeSubQuery_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_InScopeSubQuery_YsRoleGroupUserList() { return _id_InScopeSubQuery_YsRoleGroupUserListMap; }
    public String keepId_InScopeSubQuery_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_InScopeSubQuery_YsRoleGroupUserListMap == null) { _id_InScopeSubQuery_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_InScopeSubQuery_YsRoleGroupUserListMap.size() + 1);
        _id_InScopeSubQuery_YsRoleGroupUserListMap.put(key, subQuery); return "id_InScopeSubQuery_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_NotInScopeSubQuery_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_NotInScopeSubQuery_YsRoleGroupUserList() { return _id_NotInScopeSubQuery_YsRoleGroupUserListMap; }
    public String keepId_NotInScopeSubQuery_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_NotInScopeSubQuery_YsRoleGroupUserListMap == null) { _id_NotInScopeSubQuery_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_NotInScopeSubQuery_YsRoleGroupUserListMap.size() + 1);
        _id_NotInScopeSubQuery_YsRoleGroupUserListMap.put(key, subQuery); return "id_NotInScopeSubQuery_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_ExistsSubQuery_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_ExistsSubQuery_YsRoleGroupUserList() { return _id_ExistsSubQuery_YsRoleGroupUserListMap; }
    public String keepId_ExistsSubQuery_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_ExistsSubQuery_YsRoleGroupUserListMap == null) { _id_ExistsSubQuery_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_ExistsSubQuery_YsRoleGroupUserListMap.size() + 1);
        _id_ExistsSubQuery_YsRoleGroupUserListMap.put(key, subQuery); return "id_ExistsSubQuery_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_NotExistsSubQuery_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_NotExistsSubQuery_YsRoleGroupUserList() { return _id_NotExistsSubQuery_YsRoleGroupUserListMap; }
    public String keepId_NotExistsSubQuery_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_NotExistsSubQuery_YsRoleGroupUserListMap == null) { _id_NotExistsSubQuery_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_NotExistsSubQuery_YsRoleGroupUserListMap.size() + 1);
        _id_NotExistsSubQuery_YsRoleGroupUserListMap.put(key, subQuery); return "id_NotExistsSubQuery_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_SpecifyDerivedReferrer_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_SpecifyDerivedReferrer_YsRoleGroupUserList() { return _id_SpecifyDerivedReferrer_YsRoleGroupUserListMap; }
    public String keepId_SpecifyDerivedReferrer_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_SpecifyDerivedReferrer_YsRoleGroupUserListMap == null) { _id_SpecifyDerivedReferrer_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_SpecifyDerivedReferrer_YsRoleGroupUserListMap.size() + 1);
        _id_SpecifyDerivedReferrer_YsRoleGroupUserListMap.put(key, subQuery); return "id_SpecifyDerivedReferrer_YsRoleGroupUserList." + key;
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

    public BsYsRoleCQ addOrderBy_Id_Asc() { regOBA("ID"); return this; }
    public BsYsRoleCQ addOrderBy_Id_Desc() { regOBD("ID"); return this; }

    protected ConditionValue _name;
    public ConditionValue getName() {
        if (_name == null) { _name = nCV(); }
        return _name;
    }
    protected ConditionValue getCValueName() { return getName(); }

    public BsYsRoleCQ addOrderBy_Name_Asc() { regOBA("NAME"); return this; }
    public BsYsRoleCQ addOrderBy_Name_Desc() { regOBD("NAME"); return this; }

    protected ConditionValue _displayName;
    public ConditionValue getDisplayName() {
        if (_displayName == null) { _displayName = nCV(); }
        return _displayName;
    }
    protected ConditionValue getCValueDisplayName() { return getDisplayName(); }

    public BsYsRoleCQ addOrderBy_DisplayName_Asc() { regOBA("DISPLAY_NAME"); return this; }
    public BsYsRoleCQ addOrderBy_DisplayName_Desc() { regOBD("DISPLAY_NAME"); return this; }

    protected ConditionValue _createdDate;
    public ConditionValue getCreatedDate() {
        if (_createdDate == null) { _createdDate = nCV(); }
        return _createdDate;
    }
    protected ConditionValue getCValueCreatedDate() { return getCreatedDate(); }

    public BsYsRoleCQ addOrderBy_CreatedDate_Asc() { regOBA("CREATED_DATE"); return this; }
    public BsYsRoleCQ addOrderBy_CreatedDate_Desc() { regOBD("CREATED_DATE"); return this; }

    protected ConditionValue _modifiedDate;
    public ConditionValue getModifiedDate() {
        if (_modifiedDate == null) { _modifiedDate = nCV(); }
        return _modifiedDate;
    }
    protected ConditionValue getCValueModifiedDate() { return getModifiedDate(); }

    public BsYsRoleCQ addOrderBy_ModifiedDate_Asc() { regOBA("MODIFIED_DATE"); return this; }
    public BsYsRoleCQ addOrderBy_ModifiedDate_Desc() { regOBD("MODIFIED_DATE"); return this; }

    protected ConditionValue _versionNo;
    public ConditionValue getVersionNo() {
        if (_versionNo == null) { _versionNo = nCV(); }
        return _versionNo;
    }
    protected ConditionValue getCValueVersionNo() { return getVersionNo(); }

    public BsYsRoleCQ addOrderBy_VersionNo_Asc() { regOBA("VERSION_NO"); return this; }
    public BsYsRoleCQ addOrderBy_VersionNo_Desc() { regOBD("VERSION_NO"); return this; }

    // ===================================================================================
    //                                                           Specified Derived OrderBy
    //                                                           =========================
    public BsYsRoleCQ addSpecifiedDerivedOrderBy_Asc(String aliasName) { registerSpecifiedDerivedOrderBy_Asc(aliasName); return this; }
    public BsYsRoleCQ addSpecifiedDerivedOrderBy_Desc(String aliasName) { registerSpecifiedDerivedOrderBy_Desc(aliasName); return this; }

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
    protected Map<String, YsRoleCQ> _scalarSubQueryMap;
    public Map<String, YsRoleCQ> getScalarSubQuery() { return _scalarSubQueryMap; }
    public String keepScalarSubQuery(YsRoleCQ subQuery) {
        if (_scalarSubQueryMap == null) { _scalarSubQueryMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_scalarSubQueryMap.size() + 1);
        _scalarSubQueryMap.put(key, subQuery); return "scalarSubQuery." + key;
    }

    // ===================================================================================
    //                                                             MySelf InScope SubQuery
    //                                                             =======================
    protected Map<String, YsRoleCQ> _myselfInScopeSubQueryMap;
    public Map<String, YsRoleCQ> getMyselfInScopeSubQuery() { return _myselfInScopeSubQueryMap; }
    public String keepMyselfInScopeSubQuery(YsRoleCQ subQuery) {
        if (_myselfInScopeSubQueryMap == null) { _myselfInScopeSubQueryMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_myselfInScopeSubQueryMap.size() + 1);
        _myselfInScopeSubQueryMap.put(key, subQuery); return "myselfInScopeSubQuery." + key;
    }

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // Very Internal (for Suppressing Warn about 'Not Use Import')
    String xCB() { return YsRoleCB.class.getName(); }
    String xCQ() { return YsRoleCQ.class.getName(); }
    String xMap() { return Map.class.getName(); }
}
