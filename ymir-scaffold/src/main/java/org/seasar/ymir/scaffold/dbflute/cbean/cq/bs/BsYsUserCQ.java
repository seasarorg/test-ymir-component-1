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
 * The base condition-query of YS_USER.
 * @author DBFlute(AutoGenerator)
 */
public class BsYsUserCQ extends AbstractBsYsUserCQ {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected YsUserCIQ _inlineQuery;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public BsYsUserCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        super(childQuery, sqlClause, aliasName, nestLevel);
    }

    // ===================================================================================
    //                                                                              Inline
    //                                                                              ======
    /**
     * Prepare inline query. <br />
     * {select ... from ... left outer join (select * from YS_USER) where abc = [abc] ...}
     * @return Inline query. (NotNull)
     */
    public YsUserCIQ inline() {
        if (_inlineQuery == null) { _inlineQuery = createInlineQuery(); }
        _inlineQuery.xsetOnClauseInline(false); return _inlineQuery;
    }

    protected YsUserCIQ createInlineQuery()
    { return new YsUserCIQ(getReferrerQuery(), getSqlClause(), getAliasName(), getNestLevel(), this); }

    /**
     * Prepare on-clause query. <br />
     * {select ... from ... left outer join YS_USER on ... and abc = [abc] ...}
     * @return On-clause query. (NotNull)
     */
    public YsUserCIQ on() {
        if (isBaseQuery(this)) { throw new IllegalConditionBeanOperationException("On-clause for local table is unavailable!"); }
        YsUserCIQ inlineQuery = inline(); inlineQuery.xsetOnClauseInline(true); return inlineQuery;
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

    protected Map<String, YsGroupUserCQ> _id_InScopeSubQuery_YsGroupUserListMap;
    public Map<String, YsGroupUserCQ> getId_InScopeSubQuery_YsGroupUserList() { return _id_InScopeSubQuery_YsGroupUserListMap; }
    public String keepId_InScopeSubQuery_YsGroupUserList(YsGroupUserCQ subQuery) {
        if (_id_InScopeSubQuery_YsGroupUserListMap == null) { _id_InScopeSubQuery_YsGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_InScopeSubQuery_YsGroupUserListMap.size() + 1);
        _id_InScopeSubQuery_YsGroupUserListMap.put(key, subQuery); return "id_InScopeSubQuery_YsGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_InScopeSubQuery_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_InScopeSubQuery_YsRoleGroupUserList() { return _id_InScopeSubQuery_YsRoleGroupUserListMap; }
    public String keepId_InScopeSubQuery_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_InScopeSubQuery_YsRoleGroupUserListMap == null) { _id_InScopeSubQuery_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_InScopeSubQuery_YsRoleGroupUserListMap.size() + 1);
        _id_InScopeSubQuery_YsRoleGroupUserListMap.put(key, subQuery); return "id_InScopeSubQuery_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsGroupUserCQ> _id_NotInScopeSubQuery_YsGroupUserListMap;
    public Map<String, YsGroupUserCQ> getId_NotInScopeSubQuery_YsGroupUserList() { return _id_NotInScopeSubQuery_YsGroupUserListMap; }
    public String keepId_NotInScopeSubQuery_YsGroupUserList(YsGroupUserCQ subQuery) {
        if (_id_NotInScopeSubQuery_YsGroupUserListMap == null) { _id_NotInScopeSubQuery_YsGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_NotInScopeSubQuery_YsGroupUserListMap.size() + 1);
        _id_NotInScopeSubQuery_YsGroupUserListMap.put(key, subQuery); return "id_NotInScopeSubQuery_YsGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_NotInScopeSubQuery_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_NotInScopeSubQuery_YsRoleGroupUserList() { return _id_NotInScopeSubQuery_YsRoleGroupUserListMap; }
    public String keepId_NotInScopeSubQuery_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_NotInScopeSubQuery_YsRoleGroupUserListMap == null) { _id_NotInScopeSubQuery_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_NotInScopeSubQuery_YsRoleGroupUserListMap.size() + 1);
        _id_NotInScopeSubQuery_YsRoleGroupUserListMap.put(key, subQuery); return "id_NotInScopeSubQuery_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsGroupUserCQ> _id_ExistsSubQuery_YsGroupUserListMap;
    public Map<String, YsGroupUserCQ> getId_ExistsSubQuery_YsGroupUserList() { return _id_ExistsSubQuery_YsGroupUserListMap; }
    public String keepId_ExistsSubQuery_YsGroupUserList(YsGroupUserCQ subQuery) {
        if (_id_ExistsSubQuery_YsGroupUserListMap == null) { _id_ExistsSubQuery_YsGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_ExistsSubQuery_YsGroupUserListMap.size() + 1);
        _id_ExistsSubQuery_YsGroupUserListMap.put(key, subQuery); return "id_ExistsSubQuery_YsGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_ExistsSubQuery_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_ExistsSubQuery_YsRoleGroupUserList() { return _id_ExistsSubQuery_YsRoleGroupUserListMap; }
    public String keepId_ExistsSubQuery_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_ExistsSubQuery_YsRoleGroupUserListMap == null) { _id_ExistsSubQuery_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_ExistsSubQuery_YsRoleGroupUserListMap.size() + 1);
        _id_ExistsSubQuery_YsRoleGroupUserListMap.put(key, subQuery); return "id_ExistsSubQuery_YsRoleGroupUserList." + key;
    }

    protected Map<String, YsGroupUserCQ> _id_NotExistsSubQuery_YsGroupUserListMap;
    public Map<String, YsGroupUserCQ> getId_NotExistsSubQuery_YsGroupUserList() { return _id_NotExistsSubQuery_YsGroupUserListMap; }
    public String keepId_NotExistsSubQuery_YsGroupUserList(YsGroupUserCQ subQuery) {
        if (_id_NotExistsSubQuery_YsGroupUserListMap == null) { _id_NotExistsSubQuery_YsGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_NotExistsSubQuery_YsGroupUserListMap.size() + 1);
        _id_NotExistsSubQuery_YsGroupUserListMap.put(key, subQuery); return "id_NotExistsSubQuery_YsGroupUserList." + key;
    }

    protected Map<String, YsRoleGroupUserCQ> _id_NotExistsSubQuery_YsRoleGroupUserListMap;
    public Map<String, YsRoleGroupUserCQ> getId_NotExistsSubQuery_YsRoleGroupUserList() { return _id_NotExistsSubQuery_YsRoleGroupUserListMap; }
    public String keepId_NotExistsSubQuery_YsRoleGroupUserList(YsRoleGroupUserCQ subQuery) {
        if (_id_NotExistsSubQuery_YsRoleGroupUserListMap == null) { _id_NotExistsSubQuery_YsRoleGroupUserListMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_id_NotExistsSubQuery_YsRoleGroupUserListMap.size() + 1);
        _id_NotExistsSubQuery_YsRoleGroupUserListMap.put(key, subQuery); return "id_NotExistsSubQuery_YsRoleGroupUserList." + key;
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

    public BsYsUserCQ addOrderBy_Id_Asc() { regOBA("ID"); return this; }
    public BsYsUserCQ addOrderBy_Id_Desc() { regOBD("ID"); return this; }

    protected ConditionValue _name;
    public ConditionValue getName() {
        if (_name == null) { _name = nCV(); }
        return _name;
    }
    protected ConditionValue getCValueName() { return getName(); }

    public BsYsUserCQ addOrderBy_Name_Asc() { regOBA("NAME"); return this; }
    public BsYsUserCQ addOrderBy_Name_Desc() { regOBD("NAME"); return this; }

    protected ConditionValue _displayName;
    public ConditionValue getDisplayName() {
        if (_displayName == null) { _displayName = nCV(); }
        return _displayName;
    }
    protected ConditionValue getCValueDisplayName() { return getDisplayName(); }

    public BsYsUserCQ addOrderBy_DisplayName_Asc() { regOBA("DISPLAY_NAME"); return this; }
    public BsYsUserCQ addOrderBy_DisplayName_Desc() { regOBD("DISPLAY_NAME"); return this; }

    protected ConditionValue _password;
    public ConditionValue getPassword() {
        if (_password == null) { _password = nCV(); }
        return _password;
    }
    protected ConditionValue getCValuePassword() { return getPassword(); }

    public BsYsUserCQ addOrderBy_Password_Asc() { regOBA("PASSWORD"); return this; }
    public BsYsUserCQ addOrderBy_Password_Desc() { regOBD("PASSWORD"); return this; }

    protected ConditionValue _mailAddress;
    public ConditionValue getMailAddress() {
        if (_mailAddress == null) { _mailAddress = nCV(); }
        return _mailAddress;
    }
    protected ConditionValue getCValueMailAddress() { return getMailAddress(); }

    public BsYsUserCQ addOrderBy_MailAddress_Asc() { regOBA("MAIL_ADDRESS"); return this; }
    public BsYsUserCQ addOrderBy_MailAddress_Desc() { regOBD("MAIL_ADDRESS"); return this; }

    protected ConditionValue _createdDate;
    public ConditionValue getCreatedDate() {
        if (_createdDate == null) { _createdDate = nCV(); }
        return _createdDate;
    }
    protected ConditionValue getCValueCreatedDate() { return getCreatedDate(); }

    public BsYsUserCQ addOrderBy_CreatedDate_Asc() { regOBA("CREATED_DATE"); return this; }
    public BsYsUserCQ addOrderBy_CreatedDate_Desc() { regOBD("CREATED_DATE"); return this; }

    protected ConditionValue _modifiedDate;
    public ConditionValue getModifiedDate() {
        if (_modifiedDate == null) { _modifiedDate = nCV(); }
        return _modifiedDate;
    }
    protected ConditionValue getCValueModifiedDate() { return getModifiedDate(); }

    public BsYsUserCQ addOrderBy_ModifiedDate_Asc() { regOBA("MODIFIED_DATE"); return this; }
    public BsYsUserCQ addOrderBy_ModifiedDate_Desc() { regOBD("MODIFIED_DATE"); return this; }

    protected ConditionValue _versionNo;
    public ConditionValue getVersionNo() {
        if (_versionNo == null) { _versionNo = nCV(); }
        return _versionNo;
    }
    protected ConditionValue getCValueVersionNo() { return getVersionNo(); }

    public BsYsUserCQ addOrderBy_VersionNo_Asc() { regOBA("VERSION_NO"); return this; }
    public BsYsUserCQ addOrderBy_VersionNo_Desc() { regOBD("VERSION_NO"); return this; }

    // ===================================================================================
    //                                                           Specified Derived OrderBy
    //                                                           =========================
    public BsYsUserCQ addSpecifiedDerivedOrderBy_Asc(String aliasName) { registerSpecifiedDerivedOrderBy_Asc(aliasName); return this; }
    public BsYsUserCQ addSpecifiedDerivedOrderBy_Desc(String aliasName) { registerSpecifiedDerivedOrderBy_Desc(aliasName); return this; }

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
    protected Map<String, YsUserCQ> _scalarSubQueryMap;
    public Map<String, YsUserCQ> getScalarSubQuery() { return _scalarSubQueryMap; }
    public String keepScalarSubQuery(YsUserCQ subQuery) {
        if (_scalarSubQueryMap == null) { _scalarSubQueryMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_scalarSubQueryMap.size() + 1);
        _scalarSubQueryMap.put(key, subQuery); return "scalarSubQuery." + key;
    }

    // ===================================================================================
    //                                                             MySelf InScope SubQuery
    //                                                             =======================
    protected Map<String, YsUserCQ> _myselfInScopeSubQueryMap;
    public Map<String, YsUserCQ> getMyselfInScopeSubQuery() { return _myselfInScopeSubQueryMap; }
    public String keepMyselfInScopeSubQuery(YsUserCQ subQuery) {
        if (_myselfInScopeSubQueryMap == null) { _myselfInScopeSubQueryMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_myselfInScopeSubQueryMap.size() + 1);
        _myselfInScopeSubQueryMap.put(key, subQuery); return "myselfInScopeSubQuery." + key;
    }

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // Very Internal (for Suppressing Warn about 'Not Use Import')
    String xCB() { return YsUserCB.class.getName(); }
    String xCQ() { return YsUserCQ.class.getName(); }
    String xMap() { return Map.class.getName(); }
}
