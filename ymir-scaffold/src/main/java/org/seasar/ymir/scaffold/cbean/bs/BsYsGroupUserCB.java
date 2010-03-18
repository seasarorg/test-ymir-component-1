package org.seasar.ymir.scaffold.cbean.bs;

import java.util.Map;

import org.seasar.dbflute.cbean.AbstractConditionBean;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.ConditionQuery;
import org.seasar.dbflute.cbean.OrQuery;
import org.seasar.dbflute.cbean.SpecifyQuery;
import org.seasar.dbflute.cbean.SubQuery;
import org.seasar.dbflute.cbean.UnionQuery;
import org.seasar.dbflute.cbean.chelper.*;
import org.seasar.dbflute.cbean.sqlclause.SqlClause;
import org.seasar.dbflute.dbmeta.DBMetaProvider;
import org.seasar.dbflute.twowaysql.factory.SqlAnalyzerFactory;
import org.seasar.ymir.scaffold.allcommon.DBFluteConfig;
import org.seasar.ymir.scaffold.allcommon.DBMetaInstanceHandler;
import org.seasar.ymir.scaffold.allcommon.ImplementedInvokerAssistant;
import org.seasar.ymir.scaffold.allcommon.ImplementedSqlClauseCreator;
import org.seasar.ymir.scaffold.cbean.*;
import org.seasar.ymir.scaffold.cbean.cq.*;
import org.seasar.ymir.scaffold.cbean.nss.*;

/**
 * The base condition-bean of YS_GROUP_USER.
 * @author DBFlute(AutoGenerator)
 */
public class BsYsGroupUserCB extends AbstractConditionBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private final DBMetaProvider _dbmetaProvider = new DBMetaInstanceHandler();
    protected YsGroupUserCQ _conditionQuery;

    // ===================================================================================
    //                                                                           SqlClause
    //                                                                           =========
    @Override
    protected SqlClause createSqlClause() {
        return new ImplementedSqlClauseCreator().createSqlClause(this);
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
        return "YS_GROUP_USER";
    }

    // ===================================================================================
    //                                                                      PrimaryKey Map
    //                                                                      ==============
    public void acceptPrimaryKeyMap(Map<String, ? extends Object> primaryKeyMap) {
        assertPrimaryKeyMap(primaryKeyMap);
        {
            Object obj = primaryKeyMap.get("ID");
            if (obj instanceof Long) {
                query().setId_Equal((Long)obj);
            } else {
                query().setId_Equal(new Long((String)obj));
            }
        }

    }

    // ===================================================================================
    //                                                                     OrderBy Setting
    //                                                                     ===============
    public ConditionBean addOrderBy_PK_Asc() {
        query().addOrderBy_Id_Asc();
        return this;
    }

    public ConditionBean addOrderBy_PK_Desc() {
        query().addOrderBy_Id_Desc();
        return this;
    }

    // ===================================================================================
    //                                                                               Query
    //                                                                               =====
    public YsGroupUserCQ query() {
        return getConditionQuery();
    }

    public YsGroupUserCQ getConditionQuery() { // public for parameter comment
        if (_conditionQuery == null) {
            _conditionQuery = createLocalCQ();
        }
        return _conditionQuery;
    }

    protected YsGroupUserCQ createLocalCQ() {
        return xcreateCQ(null, getSqlClause(), getSqlClause().getLocalTableAliasName(), 0);
    }

    protected YsGroupUserCQ xcreateCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        return new YsGroupUserCQ(childQuery, sqlClause, aliasName, nestLevel);
    }

    /**
     * {@inheritDoc}
     */
    public ConditionQuery localCQ() {
        return getConditionQuery();
    }

    // ===================================================================================
    //                                                                               Union
    //                                                                               =====
    /**
     * Set up 'union'.
     * <pre>
     * cb.query().union(new UnionQuery&lt;YsGroupUserCB&gt;() {
     *     public void query(YsGroupUserCB unionCB) {
     *         unionCB.query().setXxx...
     *     }
     * });
     * </pre>
     * @param unionQuery The query of 'union'. (NotNull)
     */
    public void union(UnionQuery<YsGroupUserCB> unionQuery) {
        final YsGroupUserCB cb = new YsGroupUserCB();
        cb.xsetupForUnion(); xsyncUQ(cb); unionQuery.query(cb);
        final YsGroupUserCQ cq = cb.query(); query().xsetUnionQuery(cq);
    }

    /**
     * Set up 'union all'.
     * <pre>
     * cb.query().unionAll(new UnionQuery&lt;YsGroupUserCB&gt;() {
     *     public void query(YsGroupUserCB unionCB) {
     *         unionCB.query().setXxx...
     *     }
     * });
     * </pre>
     * @param unionQuery The query of 'union'. (NotNull)
     */
    public void unionAll(UnionQuery<YsGroupUserCB> unionQuery) {
        final YsGroupUserCB cb = new YsGroupUserCB();
        cb.xsetupForUnion(); xsyncUQ(cb); unionQuery.query(cb);
        final YsGroupUserCQ cq = cb.query(); query().xsetUnionAllQuery(cq);
    }

    // ===================================================================================
    //                                                                         SetupSelect
    //                                                                         ===========
    protected YsGroupNss _nssYsGroup;
    public YsGroupNss getNssYsGroup() {
        if (_nssYsGroup == null) { _nssYsGroup = new YsGroupNss(null); }
        return _nssYsGroup;
    }
    public YsGroupNss setupSelect_YsGroup() {
        doSetupSelect(new SsCall() { public ConditionQuery qf() { return query().queryYsGroup(); } });
        if (_nssYsGroup == null || !_nssYsGroup.hasConditionQuery())
        { _nssYsGroup = new YsGroupNss(query().queryYsGroup()); }
        return _nssYsGroup;
    }
    protected YsUserNss _nssYsUser;
    public YsUserNss getNssYsUser() {
        if (_nssYsUser == null) { _nssYsUser = new YsUserNss(null); }
        return _nssYsUser;
    }
    public YsUserNss setupSelect_YsUser() {
        doSetupSelect(new SsCall() { public ConditionQuery qf() { return query().queryYsUser(); } });
        if (_nssYsUser == null || !_nssYsUser.hasConditionQuery())
        { _nssYsUser = new YsUserNss(query().queryYsUser()); }
        return _nssYsUser;
    }

    // [DBFlute-0.7.4]
    // ===================================================================================
    //                                                                             Specify
    //                                                                             =======
    protected HpSpecification _specification;
    public HpSpecification specify() {
        if (_specification == null) { _specification = new HpSpecification(this
            , new HpSpQyCall<YsGroupUserCQ>() {
                public boolean has() { return true; }
                public YsGroupUserCQ qy() { return query(); }
            }
            , _forDerivedReferrer, _forScalarSelect, _forScalarSubQuery, getDBMetaProvider()); }
        return _specification;
    }
    protected HpAbstractSpecification<? extends ConditionQuery> localSp() {
        return specify();
    }

    public static class HpSpecification extends HpAbstractSpecification<YsGroupUserCQ> {
        protected HpSpQyCall<YsGroupUserCQ> _myQyCall;
        protected YsGroupCB.HpSpecification _ysGroup;
        protected YsUserCB.HpSpecification _ysUser;
        public HpSpecification(ConditionBean baseCB, HpSpQyCall<YsGroupUserCQ> qyCall
                             , boolean forDeriveReferrer, boolean forScalarSelect, boolean forScalarSubQuery
                             , DBMetaProvider dbmetaProvider)
        { super(baseCB, qyCall, forDeriveReferrer, forScalarSelect, forScalarSubQuery, dbmetaProvider); _myQyCall = qyCall; }
        /** ID: {PK : ID : NotNull : BIGINT(19)} */
        public void columnId() { doColumn("ID"); }
        /** GROUP_ID: {UQ : NotNull : BIGINT(19) : FK to YS_GROUP} */
        public void columnGroupId() { doColumn("GROUP_ID"); }
        /** USER_ID: {UQ : NotNull : BIGINT(19) : FK to YS_USER} */
        public void columnUserId() { doColumn("USER_ID"); }
        /** CREATED_DATE: {NotNull : TIMESTAMP(23, 10)} */
        public void columnCreatedDate() { doColumn("CREATED_DATE"); }
        /** MODIFIED_DATE: {NotNull : TIMESTAMP(23, 10)} */
        public void columnModifiedDate() { doColumn("MODIFIED_DATE"); }
        /** VERSION_NO: {NotNull : BIGINT(19) : default=[1]} */
        public void columnVersionNo() { doColumn("VERSION_NO"); }
        protected void doSpecifyRequiredColumn() {
            columnId(); // PK
            if (_myQyCall.qy().hasConditionQueryYsGroup()) {
                columnGroupId(); // FK
            }
            if (_myQyCall.qy().hasConditionQueryYsUser()) {
                columnUserId(); // FK
            }
        }
        protected String getTableDbName() { return "YS_GROUP_USER"; }
        /**
         * (グループ)YS_GROUP as 'ysGroup'.
         * @return Next specification. (NotNull)
         */
        public YsGroupCB.HpSpecification specifyYsGroup() {
            assertForeign("ysGroup");
            if (_ysGroup == null) {
                _ysGroup = new YsGroupCB.HpSpecification(_baseCB, new HpSpQyCall<YsGroupCQ>() {
                    public boolean has() { return _myQyCall.has() && _myQyCall.qy().hasConditionQueryYsGroup(); }
                    public YsGroupCQ qy() { return _myQyCall.qy().queryYsGroup(); } }
                    , _forDerivedReferrer, _forScalarSelect, _forScalarSubQuery, _dbmetaProvider);
                    if (_forGeneralOneSpecificaion) { _ysGroup.xsetupForGeneralOneSpecification(null); }
            }
            return _ysGroup;
        }
        /**
         * (ユーザ)YS_USER as 'ysUser'.
         * @return Next specification. (NotNull)
         */
        public YsUserCB.HpSpecification specifyYsUser() {
            assertForeign("ysUser");
            if (_ysUser == null) {
                _ysUser = new YsUserCB.HpSpecification(_baseCB, new HpSpQyCall<YsUserCQ>() {
                    public boolean has() { return _myQyCall.has() && _myQyCall.qy().hasConditionQueryYsUser(); }
                    public YsUserCQ qy() { return _myQyCall.qy().queryYsUser(); } }
                    , _forDerivedReferrer, _forScalarSelect, _forScalarSubQuery, _dbmetaProvider);
                    if (_forGeneralOneSpecificaion) { _ysUser.xsetupForGeneralOneSpecification(null); }
            }
            return _ysUser;
        }

        public void xsetupForGeneralOneSpecification(HpSpQyCall<YsGroupUserCQ> qyCall) {
            if (qyCall != null) { _myQyCall = qyCall; _qyCall = qyCall; } _forGeneralOneSpecificaion = true;
        }
    }

    // [DBFlute-0.9.5.3]
    // ===================================================================================
    //                                                                         ColumnQuery
    //                                                                         ===========
    /**
     * @param leftSpecifyQuery The specify-query for left column. (NotNull)
     * @return The object for setting up operand and right column. (NotNull)
     */
    public HpColQyOperand<YsGroupUserCB> columnQuery(final SpecifyQuery<YsGroupUserCB> leftSpecifyQuery) {
        return new HpColQyOperand<YsGroupUserCB>(new HpColQyHandler<YsGroupUserCB>() {
            public void handle(SpecifyQuery<YsGroupUserCB> rightSp, String operand) {
                YsGroupUserCB cb = new YsGroupUserCB();
                cb.specify().xsetupForGeneralOneSpecification(new HpSpQyCall<YsGroupUserCQ>() {
                    public boolean has() { return true; }
                    public YsGroupUserCQ qy() { return query(); }
                });
                xcolqy(cb, leftSpecifyQuery, rightSp, operand);
            }
        });
    }

    // [DBFlute-0.9.6.3]
    // ===================================================================================
    //                                                                        OrScopeQuery
    //                                                                        ============
    public void orScopeQuery(OrQuery<YsGroupUserCB> orQuery) {
        xorSQ((YsGroupUserCB)this, orQuery);
    }

    // ===================================================================================
    //                                                                          DisplaySQL
    //                                                                          ==========
    @Override
    protected SqlAnalyzerFactory getSqlAnalyzerFactory()
    { return new ImplementedInvokerAssistant().assistSqlAnalyzerFactory(); }
    @Override
    protected String getLogDateFormat() { return DBFluteConfig.getInstance().getLogDateFormat(); }
    @Override
    protected String getLogTimestampFormat() { return DBFluteConfig.getInstance().getLogTimestampFormat(); }

    // ===================================================================================
    //                                                          Basic Status Determination
    //                                                          ==========================
    public boolean hasUnionQueryOrUnionAllQuery() {
        return query().hasUnionQueryOrUnionAllQuery();
    }

    // ===================================================================================
    //                                                                            Internal
    //                                                                            ========
    // Very Internal (for Suppressing Warn about 'Not Use Import')
    protected String getConditionBeanClassNameInternally() { return YsGroupUserCB.class.getName(); }
    protected String getConditionQueryClassNameInternally() { return YsGroupUserCQ.class.getName(); }
    protected String getSubQueryClassNameInternally() { return SubQuery.class.getName(); }
}
