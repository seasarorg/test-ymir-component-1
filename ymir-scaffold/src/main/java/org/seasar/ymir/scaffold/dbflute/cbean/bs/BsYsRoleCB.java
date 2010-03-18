package org.seasar.ymir.scaffold.dbflute.cbean.bs;

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
import org.seasar.ymir.scaffold.dbflute.allcommon.DBFluteConfig;
import org.seasar.ymir.scaffold.dbflute.allcommon.DBMetaInstanceHandler;
import org.seasar.ymir.scaffold.dbflute.allcommon.ImplementedInvokerAssistant;
import org.seasar.ymir.scaffold.dbflute.allcommon.ImplementedSqlClauseCreator;
import org.seasar.ymir.scaffold.dbflute.cbean.*;
import org.seasar.ymir.scaffold.dbflute.cbean.cq.*;

/**
 * The base condition-bean of YS_ROLE.
 * @author DBFlute(AutoGenerator)
 */
public class BsYsRoleCB extends AbstractConditionBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private final DBMetaProvider _dbmetaProvider = new DBMetaInstanceHandler();
    protected YsRoleCQ _conditionQuery;

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
        return "YS_ROLE";
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
    public YsRoleCQ query() {
        return getConditionQuery();
    }

    public YsRoleCQ getConditionQuery() { // public for parameter comment
        if (_conditionQuery == null) {
            _conditionQuery = createLocalCQ();
        }
        return _conditionQuery;
    }

    protected YsRoleCQ createLocalCQ() {
        return xcreateCQ(null, getSqlClause(), getSqlClause().getLocalTableAliasName(), 0);
    }

    protected YsRoleCQ xcreateCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        return new YsRoleCQ(childQuery, sqlClause, aliasName, nestLevel);
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
     * cb.query().union(new UnionQuery&lt;YsRoleCB&gt;() {
     *     public void query(YsRoleCB unionCB) {
     *         unionCB.query().setXxx...
     *     }
     * });
     * </pre>
     * @param unionQuery The query of 'union'. (NotNull)
     */
    public void union(UnionQuery<YsRoleCB> unionQuery) {
        final YsRoleCB cb = new YsRoleCB();
        cb.xsetupForUnion(); xsyncUQ(cb); unionQuery.query(cb);
        final YsRoleCQ cq = cb.query(); query().xsetUnionQuery(cq);
    }

    /**
     * Set up 'union all'.
     * <pre>
     * cb.query().unionAll(new UnionQuery&lt;YsRoleCB&gt;() {
     *     public void query(YsRoleCB unionCB) {
     *         unionCB.query().setXxx...
     *     }
     * });
     * </pre>
     * @param unionQuery The query of 'union'. (NotNull)
     */
    public void unionAll(UnionQuery<YsRoleCB> unionQuery) {
        final YsRoleCB cb = new YsRoleCB();
        cb.xsetupForUnion(); xsyncUQ(cb); unionQuery.query(cb);
        final YsRoleCQ cq = cb.query(); query().xsetUnionAllQuery(cq);
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
            , new HpSpQyCall<YsRoleCQ>() {
                public boolean has() { return true; }
                public YsRoleCQ qy() { return query(); }
            }
            , _forDerivedReferrer, _forScalarSelect, _forScalarSubQuery, getDBMetaProvider()); }
        return _specification;
    }
    protected HpAbstractSpecification<? extends ConditionQuery> localSp() {
        return specify();
    }

    public static class HpSpecification extends HpAbstractSpecification<YsRoleCQ> {
        protected HpSpQyCall<YsRoleCQ> _myQyCall;
        public HpSpecification(ConditionBean baseCB, HpSpQyCall<YsRoleCQ> qyCall
                             , boolean forDeriveReferrer, boolean forScalarSelect, boolean forScalarSubQuery
                             , DBMetaProvider dbmetaProvider)
        { super(baseCB, qyCall, forDeriveReferrer, forScalarSelect, forScalarSubQuery, dbmetaProvider); _myQyCall = qyCall; }
        /** (ID)ID: {PK : ID : NotNull : BIGINT(19)} */
        public void columnId() { doColumn("ID"); }
        /** (ロール名)NAME: {UQ : NotNull : VARCHAR(200)} */
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
        protected String getTableDbName() { return "YS_ROLE"; }
        /**
         * YS_ROLE_GROUP_USER as 'ysRoleGroupUserList'.
         * @return Function. (NotNull)
         */
        public HpSDRFunction<YsRoleGroupUserCB, YsRoleCQ> derivedYsRoleGroupUserList() {
            return new HpSDRFunction<YsRoleGroupUserCB, YsRoleCQ>(_baseCB, _myQyCall.qy(), new HpSDRSetupper<YsRoleGroupUserCB, YsRoleCQ>() {
                public void setup(String function, SubQuery<YsRoleGroupUserCB> subQuery, YsRoleCQ cq, String aliasName) {
                    cq.xsderiveYsRoleGroupUserList(function, subQuery, aliasName); } }, _dbmetaProvider);
        }

        public void xsetupForGeneralOneSpecification(HpSpQyCall<YsRoleCQ> qyCall) {
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
    public HpColQyOperand<YsRoleCB> columnQuery(final SpecifyQuery<YsRoleCB> leftSpecifyQuery) {
        return new HpColQyOperand<YsRoleCB>(new HpColQyHandler<YsRoleCB>() {
            public void handle(SpecifyQuery<YsRoleCB> rightSp, String operand) {
                YsRoleCB cb = new YsRoleCB();
                cb.specify().xsetupForGeneralOneSpecification(new HpSpQyCall<YsRoleCQ>() {
                    public boolean has() { return true; }
                    public YsRoleCQ qy() { return query(); }
                });
                xcolqy(cb, leftSpecifyQuery, rightSp, operand);
            }
        });
    }

    // [DBFlute-0.9.6.3]
    // ===================================================================================
    //                                                                        OrScopeQuery
    //                                                                        ============
    public void orScopeQuery(OrQuery<YsRoleCB> orQuery) {
        xorSQ((YsRoleCB)this, orQuery);
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
    protected String getConditionBeanClassNameInternally() { return YsRoleCB.class.getName(); }
    protected String getConditionQueryClassNameInternally() { return YsRoleCQ.class.getName(); }
    protected String getSubQueryClassNameInternally() { return SubQuery.class.getName(); }
}
