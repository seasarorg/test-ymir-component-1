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
 * The base condition-query of YS_GROUP_USER.
 * @author DBFlute(AutoGenerator)
 */
public class BsYsGroupUserCQ extends AbstractBsYsGroupUserCQ {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected YsGroupUserCIQ _inlineQuery;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public BsYsGroupUserCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        super(childQuery, sqlClause, aliasName, nestLevel);
    }

    // ===================================================================================
    //                                                                              Inline
    //                                                                              ======
    /**
     * Prepare inline query. <br />
     * {select ... from ... left outer join (select * from YS_GROUP_USER) where abc = [abc] ...}
     * @return Inline query. (NotNull)
     */
    public YsGroupUserCIQ inline() {
        if (_inlineQuery == null) { _inlineQuery = createInlineQuery(); }
        _inlineQuery.xsetOnClauseInline(false); return _inlineQuery;
    }

    protected YsGroupUserCIQ createInlineQuery()
    { return new YsGroupUserCIQ(getReferrerQuery(), getSqlClause(), getAliasName(), getNestLevel(), this); }

    /**
     * Prepare on-clause query. <br />
     * {select ... from ... left outer join YS_GROUP_USER on ... and abc = [abc] ...}
     * @return On-clause query. (NotNull)
     */
    public YsGroupUserCIQ on() {
        if (isBaseQuery(this)) { throw new IllegalConditionBeanOperationException("On-clause for local table is unavailable!"); }
        YsGroupUserCIQ inlineQuery = inline(); inlineQuery.xsetOnClauseInline(true); return inlineQuery;
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

    public BsYsGroupUserCQ addOrderBy_Id_Asc() { regOBA("ID"); return this; }
    public BsYsGroupUserCQ addOrderBy_Id_Desc() { regOBD("ID"); return this; }

    protected ConditionValue _groupId;
    public ConditionValue getGroupId() {
        if (_groupId == null) { _groupId = nCV(); }
        return _groupId;
    }
    protected ConditionValue getCValueGroupId() { return getGroupId(); }

    protected Map<String, YsGroupCQ> _groupId_InScopeSubQuery_YsGroupMap;
    public Map<String, YsGroupCQ> getGroupId_InScopeSubQuery_YsGroup() { return _groupId_InScopeSubQuery_YsGroupMap; }
    public String keepGroupId_InScopeSubQuery_YsGroup(YsGroupCQ subQuery) {
        if (_groupId_InScopeSubQuery_YsGroupMap == null) { _groupId_InScopeSubQuery_YsGroupMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_groupId_InScopeSubQuery_YsGroupMap.size() + 1);
        _groupId_InScopeSubQuery_YsGroupMap.put(key, subQuery); return "groupId_InScopeSubQuery_YsGroup." + key;
    }
      
    public BsYsGroupUserCQ addOrderBy_GroupId_Asc() { regOBA("GROUP_ID"); return this; }
    public BsYsGroupUserCQ addOrderBy_GroupId_Desc() { regOBD("GROUP_ID"); return this; }

    protected ConditionValue _userId;
    public ConditionValue getUserId() {
        if (_userId == null) { _userId = nCV(); }
        return _userId;
    }
    protected ConditionValue getCValueUserId() { return getUserId(); }

    protected Map<String, YsUserCQ> _userId_InScopeSubQuery_YsUserMap;
    public Map<String, YsUserCQ> getUserId_InScopeSubQuery_YsUser() { return _userId_InScopeSubQuery_YsUserMap; }
    public String keepUserId_InScopeSubQuery_YsUser(YsUserCQ subQuery) {
        if (_userId_InScopeSubQuery_YsUserMap == null) { _userId_InScopeSubQuery_YsUserMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_userId_InScopeSubQuery_YsUserMap.size() + 1);
        _userId_InScopeSubQuery_YsUserMap.put(key, subQuery); return "userId_InScopeSubQuery_YsUser." + key;
    }
      
    public BsYsGroupUserCQ addOrderBy_UserId_Asc() { regOBA("USER_ID"); return this; }
    public BsYsGroupUserCQ addOrderBy_UserId_Desc() { regOBD("USER_ID"); return this; }

    protected ConditionValue _createdDate;
    public ConditionValue getCreatedDate() {
        if (_createdDate == null) { _createdDate = nCV(); }
        return _createdDate;
    }
    protected ConditionValue getCValueCreatedDate() { return getCreatedDate(); }

    public BsYsGroupUserCQ addOrderBy_CreatedDate_Asc() { regOBA("CREATED_DATE"); return this; }
    public BsYsGroupUserCQ addOrderBy_CreatedDate_Desc() { regOBD("CREATED_DATE"); return this; }

    protected ConditionValue _modifiedDate;
    public ConditionValue getModifiedDate() {
        if (_modifiedDate == null) { _modifiedDate = nCV(); }
        return _modifiedDate;
    }
    protected ConditionValue getCValueModifiedDate() { return getModifiedDate(); }

    public BsYsGroupUserCQ addOrderBy_ModifiedDate_Asc() { regOBA("MODIFIED_DATE"); return this; }
    public BsYsGroupUserCQ addOrderBy_ModifiedDate_Desc() { regOBD("MODIFIED_DATE"); return this; }

    protected ConditionValue _versionNo;
    public ConditionValue getVersionNo() {
        if (_versionNo == null) { _versionNo = nCV(); }
        return _versionNo;
    }
    protected ConditionValue getCValueVersionNo() { return getVersionNo(); }

    public BsYsGroupUserCQ addOrderBy_VersionNo_Asc() { regOBA("VERSION_NO"); return this; }
    public BsYsGroupUserCQ addOrderBy_VersionNo_Desc() { regOBD("VERSION_NO"); return this; }

    // ===================================================================================
    //                                                           Specified Derived OrderBy
    //                                                           =========================
    public BsYsGroupUserCQ addSpecifiedDerivedOrderBy_Asc(String aliasName) { registerSpecifiedDerivedOrderBy_Asc(aliasName); return this; }
    public BsYsGroupUserCQ addSpecifiedDerivedOrderBy_Desc(String aliasName) { registerSpecifiedDerivedOrderBy_Desc(aliasName); return this; }

    // ===================================================================================
    //                                                                         Union Query
    //                                                                         ===========
    protected void reflectRelationOnUnionQuery(ConditionQuery baseQueryAsSuper, ConditionQuery unionQueryAsSuper) {
        YsGroupUserCQ baseQuery = (YsGroupUserCQ)baseQueryAsSuper;
        YsGroupUserCQ unionQuery = (YsGroupUserCQ)unionQueryAsSuper;
        if (baseQuery.hasConditionQueryYsGroup()) {
            unionQuery.queryYsGroup().reflectRelationOnUnionQuery(baseQuery.queryYsGroup(), unionQuery.queryYsGroup());
        }
        if (baseQuery.hasConditionQueryYsUser()) {
            unionQuery.queryYsUser().reflectRelationOnUnionQuery(baseQuery.queryYsUser(), unionQuery.queryYsUser());
        }
    }

    // ===================================================================================
    //                                                                       Foreign Query
    //                                                                       =============
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
        String nrp = resolveNextRelationPath("YS_GROUP_USER", "ysGroup");
        String jan = resolveJoinAliasName(nrp, getNextNestLevel());
        YsGroupCQ cq = new YsGroupCQ(this, getSqlClause(), jan, getNextNestLevel());
        cq.xsetForeignPropertyName("ysGroup"); cq.xsetRelationPath(nrp); return cq;
    }
    protected void xsetupOuterJoinYsGroup() {
        YsGroupCQ cq = getConditionQueryYsGroup();
        Map<String, String> joinOnMap = newLinkedHashMap();
        joinOnMap.put(getRealColumnName("GROUP_ID"), cq.getRealColumnName("ID"));
        registerOuterJoin(cq, joinOnMap);
    }
    public boolean hasConditionQueryYsGroup() {
        return _conditionQueryYsGroup != null;
    }

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
        String nrp = resolveNextRelationPath("YS_GROUP_USER", "ysUser");
        String jan = resolveJoinAliasName(nrp, getNextNestLevel());
        YsUserCQ cq = new YsUserCQ(this, getSqlClause(), jan, getNextNestLevel());
        cq.xsetForeignPropertyName("ysUser"); cq.xsetRelationPath(nrp); return cq;
    }
    protected void xsetupOuterJoinYsUser() {
        YsUserCQ cq = getConditionQueryYsUser();
        Map<String, String> joinOnMap = newLinkedHashMap();
        joinOnMap.put(getRealColumnName("USER_ID"), cq.getRealColumnName("ID"));
        registerOuterJoin(cq, joinOnMap);
    }
    public boolean hasConditionQueryYsUser() {
        return _conditionQueryYsUser != null;
    }

    // ===================================================================================
    //                                                                     Scalar SubQuery
    //                                                                     ===============
    protected Map<String, YsGroupUserCQ> _scalarSubQueryMap;
    public Map<String, YsGroupUserCQ> getScalarSubQuery() { return _scalarSubQueryMap; }
    public String keepScalarSubQuery(YsGroupUserCQ subQuery) {
        if (_scalarSubQueryMap == null) { _scalarSubQueryMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_scalarSubQueryMap.size() + 1);
        _scalarSubQueryMap.put(key, subQuery); return "scalarSubQuery." + key;
    }

    // ===================================================================================
    //                                                             MySelf InScope SubQuery
    //                                                             =======================
    protected Map<String, YsGroupUserCQ> _myselfInScopeSubQueryMap;
    public Map<String, YsGroupUserCQ> getMyselfInScopeSubQuery() { return _myselfInScopeSubQueryMap; }
    public String keepMyselfInScopeSubQuery(YsGroupUserCQ subQuery) {
        if (_myselfInScopeSubQueryMap == null) { _myselfInScopeSubQueryMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_myselfInScopeSubQueryMap.size() + 1);
        _myselfInScopeSubQueryMap.put(key, subQuery); return "myselfInScopeSubQuery." + key;
    }

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // Very Internal (for Suppressing Warn about 'Not Use Import')
    String xCB() { return YsGroupUserCB.class.getName(); }
    String xCQ() { return YsGroupUserCQ.class.getName(); }
    String xMap() { return Map.class.getName(); }
}
