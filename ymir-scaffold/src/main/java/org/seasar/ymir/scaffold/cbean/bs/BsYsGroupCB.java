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

/**
 * The base condition-bean of YS_GROUP.
 * @author DBFlute(AutoGenerator)
 */
public class BsYsGroupCB extends AbstractConditionBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private final DBMetaProvider _dbmetaProvider = new DBMetaInstanceHandler();
    protected YsGroupCQ _conditionQuery;

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
        return "YS_GROUP";
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
    public YsGroupCQ query() {
        return getConditionQuery();
    }

    public YsGroupCQ getConditionQuery() { // public for parameter comment
        if (_conditionQuery == null) {
            _conditionQuery = createLocalCQ();
        }
        return _conditionQuery;
    }

    protected YsGroupCQ createLocalCQ() {
        return xcreateCQ(null, getSqlClause(), getSqlClause().getLocalTableAliasName(), 0);
    }

    protected YsGroupCQ xcreateCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        return new YsGroupCQ(childQuery, sqlClause, aliasName, nestLevel);
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
     * cb.query().union(new UnionQuery&lt;YsGroupCB&gt;() {
     *     public void query(YsGroupCB unionCB) {
     *         unionCB.query().setXxx...
     *     }
     * });
     * </pre>
     * @param unionQuery The query of 'union'. (NotNull)
     */
    public void union(UnionQuery<YsGroupCB> unionQuery) {
        final YsGroupCB cb = new YsGroupCB();
        cb.xsetupForUnion(); xsyncUQ(cb); unionQuery.query(cb);
        final YsGroupCQ cq = cb.query(); query().xsetUnionQuery(cq);
    }

    /**
     * Set up 'union all'.
     * <pre>
     * cb.query().unionAll(new UnionQuery&lt;YsGroupCB&gt;() {
     *     public void query(YsGroupCB unionCB) {
     *         unionCB.query().setXxx...
     *     }
     * });
     * </pre>
     * @param unionQuery The query of 'union'. (NotNull)
     */
    public void unionAll(UnionQuery<YsGroupCB> unionQuery) {
        final YsGroupCB cb = new YsGroupCB();
        cb.xsetupForUnion(); xsyncUQ(cb); unionQuery.query(cb);
        final YsGroupCQ cq = cb.query(); query().xsetUnionAllQuery(cq);
    }

    // ===================================================================================
    //                                                                         SetupSelect
    //                                                                         ===========

    // [DBFlute-0.7.4]
    // ===================================================================================
    //                                                                             Specify
    //                                                                             =======
    protected HpSpecification _specification;
    public HpSpecification specify() {
        if (_specification == null) { _specification = new HpSpecification(this
            , new HpSpQyCall<YsGroupCQ>() {
                public boolean has() { return true; }
                public YsGroupCQ qy() { return query(); }
            }
            , _forDerivedReferrer, _forScalarSelect, _forScalarSubQuery, getDBMetaProvider()); }
        return _specification;
    }
    protected HpAbstractSpecification<? extends ConditionQuery> localSp() {
        return specify();
    }

    public static class HpSpecification extends HpAbstractSpecification<YsGroupCQ> {
        protected HpSpQyCall<YsGroupCQ> _myQyCall;
        public HpSpecification(ConditionBean baseCB, HpSpQyCall<YsGroupCQ> qyCall
                             , boolean forDeriveReferrer, boolean forScalarSelect, boolean forScalarSubQuery
                             , DBMetaProvider dbmetaProvider)
        { super(baseCB, qyCall, forDeriveReferrer, forScalarSelect, forScalarSubQuery, dbmetaProvider); _myQyCall = qyCall; }
        /** (ID)ID: {PK : ID : NotNull : BIGINT(19)} */
        public void columnId() { doColumn("ID"); }
        /** (グループ名)NAME: {UQ : NotNull : VARCHAR(200)} */
        public void columnName() { doColumn("NAME"); }
        /** (表示名)DISPLAY_NAME: {NotNull : VARCHAR(200)} */
        public void columnDisplayName() { doColumn("DISPLAY_NAME"); }
        /** (作成日時)CREATED_DATE: {NotNull : TIMESTAMP(23, 10)} */
        public void columnCreatedDate() { doColumn("CREATED_DATE"); }
        /** (更新日時)MODIFIED_DATE: {NotNull : TIMESTAMP(23, 10)} */
        public void columnModifiedDate() { doColumn("MODIFIED_DATE"); }
        /** (バージョン番号)VERSION_NO: {NotNull : BIGINT(19) : default=[1]} */
        public void columnVersionNo() { doColumn("VERSION_NO"); }
        protected void doSpecifyRequiredColumn() {
            columnId(); // PK
        }
        protected String getTableDbName() { return "YS_GROUP"; }
        /**
         * YS_GROUP_USER as 'ysGroupUserList'.
         * @return Function. (NotNull)
         */
        public HpSDRFunction<YsGroupUserCB, YsGroupCQ> derivedYsGroupUserList() {
            return new HpSDRFunction<YsGroupUserCB, YsGroupCQ>(_baseCB, _myQyCall.qy(), new HpSDRSetupper<YsGroupUserCB, YsGroupCQ>() {
                public void setup(String function, SubQuery<YsGroupUserCB> subQuery, YsGroupCQ cq, String aliasName) {
                    cq.xsderiveYsGroupUserList(function, subQuery, aliasName); } }, _dbmetaProvider);
        }
        /**
         * YS_ROLE_GROUP_USER as 'ysRoleGroupUserList'.
         * @return Function. (NotNull)
         */
        public HpSDRFunction<YsRoleGroupUserCB, YsGroupCQ> derivedYsRoleGroupUserList() {
            return new HpSDRFunction<YsRoleGroupUserCB, YsGroupCQ>(_baseCB, _myQyCall.qy(), new HpSDRSetupper<YsRoleGroupUserCB, YsGroupCQ>() {
                public void setup(String function, SubQuery<YsRoleGroupUserCB> subQuery, YsGroupCQ cq, String aliasName) {
                    cq.xsderiveYsRoleGroupUserList(function, subQuery, aliasName); } }, _dbmetaProvider);
        }

        public void xsetupForGeneralOneSpecification(HpSpQyCall<YsGroupCQ> qyCall) {
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
    public HpColQyOperand<YsGroupCB> columnQuery(final SpecifyQuery<YsGroupCB> leftSpecifyQuery) {
        return new HpColQyOperand<YsGroupCB>(new HpColQyHandler<YsGroupCB>() {
            public void handle(SpecifyQuery<YsGroupCB> rightSp, String operand) {
                YsGroupCB cb = new YsGroupCB();
                cb.specify().xsetupForGeneralOneSpecification(new HpSpQyCall<YsGroupCQ>() {
                    public boolean has() { return true; }
                    public YsGroupCQ qy() { return query(); }
                });
                xcolqy(cb, leftSpecifyQuery, rightSp, operand);
            }
        });
    }

    // [DBFlute-0.9.6.3]
    // ===================================================================================
    //                                                                        OrScopeQuery
    //                                                                        ============
    public void orScopeQuery(OrQuery<YsGroupCB> orQuery) {
        xorSQ((YsGroupCB)this, orQuery);
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
    protected String getConditionBeanClassNameInternally() { return YsGroupCB.class.getName(); }
    protected String getConditionQueryClassNameInternally() { return YsGroupCQ.class.getName(); }
    protected String getSubQueryClassNameInternally() { return SubQuery.class.getName(); }
}
