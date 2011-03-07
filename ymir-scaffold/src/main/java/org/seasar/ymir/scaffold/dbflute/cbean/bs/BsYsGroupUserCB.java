package org.seasar.ymir.scaffold.dbflute.cbean.bs;

import org.seasar.dbflute.cbean.AbstractConditionBean;
import org.seasar.dbflute.cbean.AndQuery;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.ConditionQuery;
import org.seasar.dbflute.cbean.OrQuery;
import org.seasar.dbflute.cbean.SpecifyQuery;
import org.seasar.dbflute.cbean.SubQuery;
import org.seasar.dbflute.cbean.UnionQuery;
import org.seasar.dbflute.cbean.chelper.*;
import org.seasar.dbflute.cbean.coption.*;
import org.seasar.dbflute.cbean.sqlclause.SqlClause;
import org.seasar.dbflute.cbean.sqlclause.SqlClauseCreator;
import org.seasar.dbflute.dbmeta.DBMetaProvider;
import org.seasar.dbflute.twowaysql.factory.SqlAnalyzerFactory;
import org.seasar.ymir.scaffold.dbflute.allcommon.DBFluteConfig;
import org.seasar.ymir.scaffold.dbflute.allcommon.DBMetaInstanceHandler;
import org.seasar.ymir.scaffold.dbflute.allcommon.ImplementedInvokerAssistant;
import org.seasar.ymir.scaffold.dbflute.allcommon.ImplementedSqlClauseCreator;
import org.seasar.ymir.scaffold.dbflute.cbean.*;
import org.seasar.ymir.scaffold.dbflute.cbean.cq.*;
import org.seasar.ymir.scaffold.dbflute.cbean.nss.*;

/**
 * The base condition-bean of YS_GROUP_USER.
 * @author DBFlute(AutoGenerator)
 */
public class BsYsGroupUserCB extends AbstractConditionBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected YsGroupUserCQ _conditionQuery;

    // ===================================================================================
    //                                                                           SqlClause
    //                                                                           =========
    @Override
    protected SqlClause createSqlClause() {
        SqlClauseCreator creator = DBFluteConfig.getInstance().getSqlClauseCreator();
        if (creator != null) {
            return creator.createSqlClause(this);
        }
        return new ImplementedSqlClauseCreator().createSqlClause(this); // as default
    }

    // ===================================================================================
    //                                                                     DBMeta Provider
    //                                                                     ===============
    @Override
    protected DBMetaProvider getDBMetaProvider() {
        return DBMetaInstanceHandler.getProvider(); // as default
    }

    // ===================================================================================
    //                                                                          Table Name
    //                                                                          ==========
    public String getTableDbName() {
        return "YS_GROUP_USER";
    }

    // ===================================================================================
    //                                                                 PrimaryKey Handling
    //                                                                 ===================
    public void acceptPrimaryKey(Long id) {
        assertObjectNotNull("id", id);
        BsYsGroupUserCB cb = this;
        cb.query().setId_Equal(id);
    }

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
    /**
     * Prepare for various queries. <br />
     * Examples of main functions are following:
     * <pre>
     * <span style="color: #3F7E5E">// Basic Queries</span>
     * cb.query().setMemberId_Equal(value);        <span style="color: #3F7E5E">// =</span>
     * cb.query().setMemberId_NotEqual(value);     <span style="color: #3F7E5E">// !=</span>
     * cb.query().setMemberId_GreaterThan(value);  <span style="color: #3F7E5E">// &gt;</span>
     * cb.query().setMemberId_LessThan(value);     <span style="color: #3F7E5E">// &lt;</span>
     * cb.query().setMemberId_GreaterEqual(value); <span style="color: #3F7E5E">// &gt;=</span>
     * cb.query().setMemberId_LessEqual(value);    <span style="color: #3F7E5E">// &lt;=</span>
     * cb.query().setMemberName_InScope(valueList);    <span style="color: #3F7E5E">// in ('a', 'b')</span>
     * cb.query().setMemberName_NotInScope(valueList); <span style="color: #3F7E5E">// not in ('a', 'b')</span>
     * cb.query().setMemberName_PrefixSearch(value);   <span style="color: #3F7E5E">// like 'a%' escape '|'</span>
     * <span style="color: #3F7E5E">// LikeSearch with various options: (versatile)</span>
     * <span style="color: #3F7E5E">// {like ... [options]}</span>
     * cb.query().setMemberName_LikeSearch(value, option);
     * cb.query().setMemberName_NotLikeSearch(value, option); <span style="color: #3F7E5E">// not like ...</span>
     * <span style="color: #3F7E5E">// FromTo with various options: (versatile)</span>
     * <span style="color: #3F7E5E">// {(default) fromDatetime &lt;= BIRTHDATE &lt;= toDatetime}</span>
     * cb.query().setBirthdate_FromTo(fromDatetime, toDatetime, option);
     * <span style="color: #3F7E5E">// DateFromTo: (Date means yyyy/MM/dd)</span>
     * <span style="color: #3F7E5E">// {fromDate &lt;= BIRTHDATE &lt; toDate + 1 day}</span>
     * cb.query().setBirthdate_DateFromTo(fromDate, toDate);
     * cb.query().setBirthdate_IsNull();    <span style="color: #3F7E5E">// is null</span>
     * cb.query().setBirthdate_IsNotNull(); <span style="color: #3F7E5E">// is not null</span>
     * 
     * <span style="color: #3F7E5E">// ExistsReferrer: (co-related sub-query)</span>
     * <span style="color: #3F7E5E">// {where exists (select PURCHASE_ID from PURCHASE where ...)}</span>
     * cb.query().existsPurchaseList(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.query().setXxx... <span style="color: #3F7E5E">// referrer sub-query condition</span>
     *     }
     * });
     * cb.query().notExistsPurchaseList...
     * 
     * <span style="color: #3F7E5E">// InScopeRelation: (sub-query)</span>
     * <span style="color: #3F7E5E">// {where MEMBER_STATUS_CODE in (select MEMBER_STATUS_CODE from MEMBER_STATUS where ...)}</span>
     * cb.query().inScopeMemberStatus(new SubQuery&lt;MemberStatusCB&gt;() {
     *     public void query(MemberStatusCB subCB) {
     *         subCB.query().setXxx... <span style="color: #3F7E5E">// relation sub-query condition</span>
     *     }
     * });
     * cb.query().notInScopeMemberStatus...
     * 
     * <span style="color: #3F7E5E">// (Query)DerivedReferrer: (co-related sub-query)</span>
     * cb.query().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchasePrice(); <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setXxx... <span style="color: #3F7E5E">// referrer sub-query condition</span>
     *     }
     * }).greaterEqual(value);
     * 
     * <span style="color: #3F7E5E">// ScalarCondition: (self-table sub-query)</span>
     * cb.query().scalar_Equal().max(new SubQuery&lt;MemberCB&gt;() {
     *     public void query(MemberCB subCB) {
     *         subCB.specify().columnBirthdate(); <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setXxx... <span style="color: #3F7E5E">// scalar sub-query condition</span>
     *     }
     * });
     * 
     * <span style="color: #3F7E5E">// OrderBy</span>
     * cb.query().addOrderBy_MemberName_Asc();
     * cb.query().addOrderBy_MemberName_Desc().withManualOrder(valueList);
     * cb.query().addOrderBy_MemberName_Desc().withNullsFirst();
     * cb.query().addOrderBy_MemberName_Desc().withNullsLast();
     * cb.query().addSpecifiedDerivedOrderBy_Desc(aliasName);
     * 
     * <span style="color: #3F7E5E">// Query(Relation)</span>
     * cb.query().queryMemberStatus()...;
     * cb.query().queryMemberAddressAsValid(targetDate)...;
     * </pre>
     * @return The instance of condition-query for base-point table to set up query. (NotNull)
     */
    public YsGroupUserCQ query() {
        assertQueryPurpose(); // assert only when user-public query 
        return getConditionQuery();
    }

    public YsGroupUserCQ getConditionQuery() { // public for parameter comment and internal
        if (_conditionQuery == null) {
            _conditionQuery = createLocalCQ();
        }
        return _conditionQuery;
    }

    protected YsGroupUserCQ createLocalCQ() {
        return xcreateCQ(null, getSqlClause(), getSqlClause().getBasePointAliasName(), 0);
    }

    protected YsGroupUserCQ xcreateCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        return new YsGroupUserCQ(childQuery, sqlClause, aliasName, nestLevel);
    }

    public ConditionQuery localCQ() {
        return getConditionQuery();
    }

    // ===================================================================================
    //                                                                               Union
    //                                                                               =====
    /**
     * Set up 'union' for base-point table. <br />
     * You don't need to call SetupSelect in union-query,
     * because it inherits calls before. (Don't call SetupSelect after here)
     * <pre>
     * cb.query().<span style="color: #FD4747">union</span>(new UnionQuery&lt;YsGroupUserCB&gt;() {
     *     public void query(YsGroupUserCB unionCB) {
     *         unionCB.query().setXxx...
     *     }
     * });
     * </pre>
     * @param unionQuery The query of 'union'. (NotNull)
     */
    public void union(UnionQuery<YsGroupUserCB> unionQuery) {
        final YsGroupUserCB cb = new YsGroupUserCB();
        cb.xsetupForUnion(this); xsyncUQ(cb); unionQuery.query(cb);
        final YsGroupUserCQ cq = cb.query(); query().xsetUnionQuery(cq);
    }

    /**
     * Set up 'union all' for base-point table. <br />
     * You don't need to call SetupSelect in union-query,
     * because it inherits calls before. (Don't call SetupSelect after here)
     * <pre>
     * cb.query().<span style="color: #FD4747">unionAll</span>(new UnionQuery&lt;YsGroupUserCB&gt;() {
     *     public void query(YsGroupUserCB unionCB) {
     *         unionCB.query().setXxx...
     *     }
     * });
     * </pre>
     * @param unionQuery The query of 'union all'. (NotNull)
     */
    public void unionAll(UnionQuery<YsGroupUserCB> unionQuery) {
        final YsGroupUserCB cb = new YsGroupUserCB();
        cb.xsetupForUnion(this); xsyncUQ(cb); unionQuery.query(cb);
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
    /**
     * Set up relation columns to select clause. <br />
     * (グループ)YS_GROUP as 'ysGroup'.
     * <pre>
     * YsGroupUserCB cb = new YsGroupUserCB();
     * cb.<span style="color: #FD4747">setupSelect_YsGroup()</span>; <span style="color: #3F7E5E">// ...().with[nested-relation]()</span>
     * cb.query().setFoo...(value);
     * YsGroupUser ysGroupUser = ysGroupUserBhv.selectEntityWithDeletedCheck(cb);
     * ... = ysGroupUser.<span style="color: #FD4747">getYsGroup()</span>; <span style="color: #3F7E5E">// you can get by using SetupSelect</span>
     * </pre>
     * @return The set-upper of nested relation. {setupSelect...().with[nested-relation]} (NotNull)
     */
    public YsGroupNss setupSelect_YsGroup() {
        if (hasSpecifiedColumn()) { // if reverse call
            specify().columnGroupId();
        }
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
    /**
     * Set up relation columns to select clause. <br />
     * (ユーザ)YS_USER as 'ysUser'.
     * <pre>
     * YsGroupUserCB cb = new YsGroupUserCB();
     * cb.<span style="color: #FD4747">setupSelect_YsUser()</span>; <span style="color: #3F7E5E">// ...().with[nested-relation]()</span>
     * cb.query().setFoo...(value);
     * YsGroupUser ysGroupUser = ysGroupUserBhv.selectEntityWithDeletedCheck(cb);
     * ... = ysGroupUser.<span style="color: #FD4747">getYsUser()</span>; <span style="color: #3F7E5E">// you can get by using SetupSelect</span>
     * </pre>
     * @return The set-upper of nested relation. {setupSelect...().with[nested-relation]} (NotNull)
     */
    public YsUserNss setupSelect_YsUser() {
        if (hasSpecifiedColumn()) { // if reverse call
            specify().columnUserId();
        }
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

    /**
     * Prepare for SpecifyColumn, (Specify)DerivedReferrer. <br />
     * This method should be called after SetupSelect.
     * <pre>
     * cb.setupSelect_MemberStatus(); <span style="color: #3F7E5E">// should be called before specify()</span>
     * cb.specify().columnMemberName();
     * cb.specify().specifyMemberStatus().columnMemberStatusName();
     * cb.specify().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchaseDatetime();
     *         subCB.query().set...
     *     }
     * }, aliasName);
     * </pre>
     * @return The instance of specification. (NotNull)
     */
    public HpSpecification specify() {
        assertSpecifyPurpose();
        if (_specification == null) { _specification = new HpSpecification(this
            , new HpSpQyCall<YsGroupUserCQ>() {
                public boolean has() { return true; }
                public YsGroupUserCQ qy() { return getConditionQuery(); }
            }
            , _purpose, getDBMetaProvider()); }
        return _specification;
    }

    protected boolean hasSpecifiedColumn() {
        return _specification != null && _specification.isAlreadySpecifiedRequiredColumn();
    }

    protected HpAbstractSpecification<? extends ConditionQuery> localSp() {
        return specify();
    }

    public static class HpSpecification extends HpAbstractSpecification<YsGroupUserCQ> {
        protected YsGroupCB.HpSpecification _ysGroup;
        protected YsUserCB.HpSpecification _ysUser;
        public HpSpecification(ConditionBean baseCB, HpSpQyCall<YsGroupUserCQ> qyCall
                             , HpCBPurpose purpose, DBMetaProvider dbmetaProvider)
        { super(baseCB, qyCall, purpose, dbmetaProvider); }
        /** ID: {PK, ID, NotNull, BIGINT(19)} */
        public void columnId() { doColumn("ID"); }
        /** GROUP_ID: {UQ, NotNull, BIGINT(19), FK to YS_GROUP} */
        public void columnGroupId() { doColumn("GROUP_ID"); }
        /** USER_ID: {UQ+, IX, NotNull, BIGINT(19), FK to YS_USER} */
        public void columnUserId() { doColumn("USER_ID"); }
        /** CREATED_DATE: {NotNull, TIMESTAMP(23, 10)} */
        public void columnCreatedDate() { doColumn("CREATED_DATE"); }
        /** MODIFIED_DATE: {NotNull, TIMESTAMP(23, 10)} */
        public void columnModifiedDate() { doColumn("MODIFIED_DATE"); }
        /** VERSION_NO: {NotNull, BIGINT(19), default=[1]} */
        public void columnVersionNo() { doColumn("VERSION_NO"); }
        @Override
        protected void doSpecifyRequiredColumn() {
            columnId(); // PK
            if (qyCall().qy().hasConditionQueryYsGroup()
                    || qyCall().qy().xgetReferrerQuery() instanceof YsGroupCQ) {
                columnGroupId(); // FK or one-to-one referrer
            }
            if (qyCall().qy().hasConditionQueryYsUser()
                    || qyCall().qy().xgetReferrerQuery() instanceof YsUserCQ) {
                columnUserId(); // FK or one-to-one referrer
            }
        }
        @Override
        protected String getTableDbName() { return "YS_GROUP_USER"; }
        /**
         * Prepare to specify functions about relation table. <br />
         * (グループ)YS_GROUP as 'ysGroup'.
         * @return The instance for specification for relation table to specify. (NotNull)
         */
        public YsGroupCB.HpSpecification specifyYsGroup() {
            assertRelation("ysGroup");
            if (_ysGroup == null) {
                _ysGroup = new YsGroupCB.HpSpecification(_baseCB, new HpSpQyCall<YsGroupCQ>() {
                    public boolean has() { return _qyCall.has() && _qyCall.qy().hasConditionQueryYsGroup(); }
                    public YsGroupCQ qy() { return _qyCall.qy().queryYsGroup(); } }
                    , _purpose, _dbmetaProvider);
                if (xhasSyncQyCall()) { // inherits it
                    _ysGroup.xsetSyncQyCall(new HpSpQyCall<YsGroupCQ>() {
                        public boolean has() { return xsyncQyCall().has() && xsyncQyCall().qy().hasConditionQueryYsGroup(); }
                        public YsGroupCQ qy() { return xsyncQyCall().qy().queryYsGroup(); }
                    });
                }
            }
            return _ysGroup;
        }
        /**
         * Prepare to specify functions about relation table. <br />
         * (ユーザ)YS_USER as 'ysUser'.
         * @return The instance for specification for relation table to specify. (NotNull)
         */
        public YsUserCB.HpSpecification specifyYsUser() {
            assertRelation("ysUser");
            if (_ysUser == null) {
                _ysUser = new YsUserCB.HpSpecification(_baseCB, new HpSpQyCall<YsUserCQ>() {
                    public boolean has() { return _qyCall.has() && _qyCall.qy().hasConditionQueryYsUser(); }
                    public YsUserCQ qy() { return _qyCall.qy().queryYsUser(); } }
                    , _purpose, _dbmetaProvider);
                if (xhasSyncQyCall()) { // inherits it
                    _ysUser.xsetSyncQyCall(new HpSpQyCall<YsUserCQ>() {
                        public boolean has() { return xsyncQyCall().has() && xsyncQyCall().qy().hasConditionQueryYsUser(); }
                        public YsUserCQ qy() { return xsyncQyCall().qy().queryYsUser(); }
                    });
                }
            }
            return _ysUser;
        }
    }

    // [DBFlute-0.9.5.3]
    // ===================================================================================
    //                                                                         ColumnQuery
    //                                                                         ===========
    /**
     * Set up column-query. {column1 = column2}
     * <pre>
     * <span style="color: #3F7E5E">// where FOO &lt; BAR</span>
     * cb.<span style="color: #FD4747">columnQuery</span>(new SpecifyQuery&lt;YsGroupUserCB&gt;() {
     *     public void query(YsGroupUserCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFoo()</span>; <span style="color: #3F7E5E">// left column</span>
     *     }
     * }).lessThan(new SpecifyQuery&lt;YsGroupUserCB&gt;() {
     *     public void query(YsGroupUserCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnBar()</span>; <span style="color: #3F7E5E">// right column</span>
     *     }
     * }); <span style="color: #3F7E5E">// you can calculate for right column like '}).plus(3);'</span>
     * </pre>
     * @param leftSpecifyQuery The specify-query for left column. (NotNull)
     * @return The object for setting up operand and right column. (NotNull)
     */
    public HpColQyOperand<YsGroupUserCB> columnQuery(final SpecifyQuery<YsGroupUserCB> leftSpecifyQuery) {
        return new HpColQyOperand<YsGroupUserCB>(new HpColQyHandler<YsGroupUserCB>() {
            public HpCalculator handle(SpecifyQuery<YsGroupUserCB> rightSp, String operand) {
                return xcolqy(xcreateColumnQueryCB(), xcreateColumnQueryCB(), leftSpecifyQuery, rightSp, operand);
            }
        });
    }

    protected YsGroupUserCB xcreateColumnQueryCB() {
        YsGroupUserCB cb = new YsGroupUserCB();
        cb.xsetupForColumnQuery((YsGroupUserCB)this);
        return cb;
    }

    // [DBFlute-0.9.6.3]
    // ===================================================================================
    //                                                                        OrScopeQuery
    //                                                                        ============
    /**
     * Set up the query for or-scope. <br />
     * (Same-column-and-same-condition-key conditions are allowed in or-scope)
     * <pre>
     * <span style="color: #3F7E5E">// where (FOO = '...' or BAR = '...')</span>
     * cb.<span style="color: #FD4747">orScopeQuery</span>(new OrQuery&lt;YsGroupUserCB&gt;() {
     *     public void query(YsGroupUserCB orCB) {
     *         orCB.query().setFOO_Equal...
     *         orCB.query().setBAR_Equal...
     *     }
     * });
     * </pre>
     * @param orQuery The query for or-condition. (NotNull)
     */
    public void orScopeQuery(OrQuery<YsGroupUserCB> orQuery) {
        xorSQ((YsGroupUserCB)this, orQuery);
    }

    /**
     * Set up the and-part of or-scope. <br />
     * (However nested or-scope query and as-or-split of like-search in and-part are unsupported)
     * <pre>
     * <span style="color: #3F7E5E">// where (FOO = '...' or (BAR = '...' and QUX = '...'))</span>
     * cb.<span style="color: #FD4747">orScopeQuery</span>(new OrQuery&lt;YsGroupUserCB&gt;() {
     *     public void query(YsGroupUserCB orCB) {
     *         orCB.query().setFOO_Equal...
     *         orCB.<span style="color: #FD4747">orScopeQueryAndPart</span>(new AndQuery&lt;YsGroupUserCB&gt;() {
     *             public void query(YsGroupUserCB andCB) {
     *                 andCB.query().setBar_...
     *                 andCB.query().setQux_...
     *             }
     *         });
     *     }
     * });
     * </pre>
     * @param andQuery The query for and-condition. (NotNull)
     */
    public void orScopeQueryAndPart(AndQuery<YsGroupUserCB> andQuery) {
        xorSQAP((YsGroupUserCB)this, andQuery);
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

    // [DBFlute-0.7.4]
    // ===================================================================================
    //                                                                        Purpose Type
    //                                                                        ============
    public void xsetupForColumnQuery(final YsGroupUserCB mainCB) {
        xinheritSubQueryInfo(mainCB.localCQ());
        xchangePurposeSqlClause(HpCBPurpose.COLUMN_QUERY);

        // inherits a parent query to synchronize real name
        // (and also for suppressing query check) 
        specify().xsetSyncQyCall(new HpSpQyCall<YsGroupUserCQ>() {
            public boolean has() { return true; }
            public YsGroupUserCQ qy() { return mainCB.query(); }
        });
    }

    public void xsetupForVaryingUpdate() {
        xchangePurposeSqlClause(HpCBPurpose.VARYING_UPDATE);

        // for suppressing query check
        final YsGroupUserCB nonCheckCB = new YsGroupUserCB();
        specify().xsetSyncQyCall(new HpSpQyCall<YsGroupUserCQ>() {
            public boolean has() { return true; }
            public YsGroupUserCQ qy() { return nonCheckCB.query(); }
        });
    }

    // ===================================================================================
    //                                                                            Internal
    //                                                                            ========
    // very internal (for suppressing warn about 'Not Use Import')
    protected String getConditionBeanClassNameInternally() { return YsGroupUserCB.class.getName(); }
    protected String getConditionQueryClassNameInternally() { return YsGroupUserCQ.class.getName(); }
    protected String getSubQueryClassNameInternally() { return SubQuery.class.getName(); }
    protected String getConditionOptionClassNameInternally() { return ConditionOption.class.getName(); }
}
