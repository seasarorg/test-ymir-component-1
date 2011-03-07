package org.seasar.ymir.scaffold.dbflute.bsbhv;

import java.util.List;

import org.seasar.dbflute.*;
import org.seasar.dbflute.bhv.*;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.EntityRowHandler;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.PagingResultBean;
import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.ymir.scaffold.dbflute.exbhv.*;
import org.seasar.ymir.scaffold.dbflute.exentity.*;
import org.seasar.ymir.scaffold.dbflute.bsentity.dbmeta.*;
import org.seasar.ymir.scaffold.dbflute.cbean.*;

/**
 * The behavior of (ユーザ)YS_USER as TABLE. <br />
 * <pre>
 * [primary-key]
 *     ID
 * 
 * [column]
 *     ID, NAME, DISPLAY_NAME, PASSWORD, MAIL_ADDRESS, CREATED_DATE, MODIFIED_DATE, VERSION_NO
 * 
 * [sequence]
 *     
 * 
 * [identity]
 *     ID
 * 
 * [version-no]
 *     VERSION_NO
 * 
 * [foreign-table]
 *     
 * 
 * [referrer-table]
 *     YS_GROUP_USER, YS_ROLE_GROUP_USER
 * 
 * [foreign-property]
 *     
 * 
 * [referrer-property]
 *     ysGroupUserList, ysRoleGroupUserList
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsYsUserBhv extends AbstractBehaviorWritable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /*df:BehaviorQueryPathBegin*/
    /*df:BehaviorQueryPathEnd*/

    // ===================================================================================
    //                                                                          Table name
    //                                                                          ==========
    /** @return The name on database of table. (NotNull) */
    public String getTableDbName() { return "YS_USER"; }

    // ===================================================================================
    //                                                                              DBMeta
    //                                                                              ======
    /** @return The instance of DBMeta. (NotNull) */
    public DBMeta getDBMeta() { return YsUserDbm.getInstance(); }

    /** @return The instance of DBMeta as my table type. (NotNull) */
    public YsUserDbm getMyDBMeta() { return YsUserDbm.getInstance(); }

    // ===================================================================================
    //                                                                        New Instance
    //                                                                        ============
    /** {@inheritDoc} */
    public Entity newEntity() { return newMyEntity(); }

    /** {@inheritDoc} */
    public ConditionBean newConditionBean() { return newMyConditionBean(); }

    /** @return The instance of new entity as my table type. (NotNull) */
    public YsUser newMyEntity() { return new YsUser(); }

    /** @return The instance of new condition-bean as my table type. (NotNull) */
    public YsUserCB newMyConditionBean() { return new YsUserCB(); }

    // ===================================================================================
    //                                                                        Count Select
    //                                                                        ============
    /**
     * Select the count by the condition-bean. {IgnorePagingCondition}
     * <pre>
     * YsUserCB cb = new YsUserCB();
     * cb.query().setFoo...(value);
     * int count = ysUserBhv.<span style="color: #FD4747">selectCount</span>(cb);
     * </pre>
     * @param cb The condition-bean of YsUser. (NotNull)
     * @return The selected count.
     */
    public int selectCount(YsUserCB cb) {
        return doSelectCount(cb);
    }

    protected int doSelectCount(YsUserCB cb) {
        assertCBNotNull(cb);
        return delegateSelectCount(cb);
    }

    @Override
    protected int doReadCount(ConditionBean cb) {
        return selectCount(downcast(cb));
    }

    // ===================================================================================
    //                                                                       Cursor Select
    //                                                                       =============
    /**
     * Select the cursor by the condition-bean.
     * <pre>
     * YsUserCB cb = new YsUserCB();
     * cb.query().setFoo...(value);
     * ysUserBhv.<span style="color: #FD4747">selectCursor</span>(cb, new EntityRowHandler&lt;YsUser&gt;() {
     *     public void handle(YsUser entity) {
     *         ... = entity.getFoo...();
     *     }
     * });
     * </pre>
     * @param cb The condition-bean of YsUser. (NotNull)
     * @param entityRowHandler The handler of entity row of YsUser. (NotNull)
     */
    public void selectCursor(YsUserCB cb, EntityRowHandler<YsUser> entityRowHandler) {
        doSelectCursor(cb, entityRowHandler, YsUser.class);
    }

    protected <ENTITY extends YsUser> void doSelectCursor(YsUserCB cb, EntityRowHandler<ENTITY> entityRowHandler, Class<ENTITY> entityType) {
        assertCBNotNull(cb); assertObjectNotNull("entityRowHandler<YsUser>", entityRowHandler); assertObjectNotNull("entityType", entityType);
        assertSpecifyDerivedReferrerEntityProperty(cb, entityType);
        delegateSelectCursor(cb, entityRowHandler, entityType);
    }

    // ===================================================================================
    //                                                                       Entity Select
    //                                                                       =============
    /**
     * Select the entity by the condition-bean.
     * <pre>
     * YsUserCB cb = new YsUserCB();
     * cb.query().setFoo...(value);
     * YsUser ysUser = ysUserBhv.<span style="color: #FD4747">selectEntity</span>(cb);
     * if (ysUser != null) {
     *     ... = ysUser.get...();
     * } else {
     *     ...
     * }
     * </pre>
     * @param cb The condition-bean of YsUser. (NotNull)
     * @return The selected entity. (Nullable: If the condition has no data, it returns null)
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public YsUser selectEntity(YsUserCB cb) {
        return doSelectEntity(cb, YsUser.class);
    }

    protected <ENTITY extends YsUser> ENTITY doSelectEntity(final YsUserCB cb, final Class<ENTITY> entityType) {
        return helpSelectEntityInternally(cb, new InternalSelectEntityCallback<ENTITY, YsUserCB>() {
            public List<ENTITY> callbackSelectList(YsUserCB cb) { return doSelectList(cb, entityType); } });
    }

    @Override
    protected Entity doReadEntity(ConditionBean cb) {
        return selectEntity(downcast(cb));
    }

    /**
     * Select the entity by the condition-bean with deleted check.
     * <pre>
     * YsUserCB cb = new YsUserCB();
     * cb.query().setFoo...(value);
     * YsUser ysUser = ysUserBhv.<span style="color: #FD4747">selectEntityWithDeletedCheck</span>(cb);
     * ... = ysUser.get...(); <span style="color: #3F7E5E">// the entity always be not null</span>
     * </pre>
     * @param cb The condition-bean of YsUser. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public YsUser selectEntityWithDeletedCheck(YsUserCB cb) {
        return doSelectEntityWithDeletedCheck(cb, YsUser.class);
    }

    protected <ENTITY extends YsUser> ENTITY doSelectEntityWithDeletedCheck(final YsUserCB cb, final Class<ENTITY> entityType) {
        return helpSelectEntityWithDeletedCheckInternally(cb, new InternalSelectEntityWithDeletedCheckCallback<ENTITY, YsUserCB>() {
            public List<ENTITY> callbackSelectList(YsUserCB cb) { return doSelectList(cb, entityType); } });
    }

    @Override
    protected Entity doReadEntityWithDeletedCheck(ConditionBean cb) {
        return selectEntityWithDeletedCheck(downcast(cb));
    }

    /**
     * Select the entity by the primary-key value.
     * @param id The one of primary key. (NotNull)
     * @return The selected entity. (Nullable: If the primary-key value has no data, it returns null)
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public YsUser selectByPKValue(Long id) {
        return doSelectByPKValue(id, YsUser.class);
    }

    protected <ENTITY extends YsUser> ENTITY doSelectByPKValue(Long id, Class<ENTITY> entityType) {
        return doSelectEntity(buildPKCB(id), entityType);
    }

    /**
     * Select the entity by the primary-key value with deleted check.
     * @param id The one of primary key. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public YsUser selectByPKValueWithDeletedCheck(Long id) {
        return doSelectByPKValueWithDeletedCheck(id, YsUser.class);
    }

    protected <ENTITY extends YsUser> ENTITY doSelectByPKValueWithDeletedCheck(Long id, Class<ENTITY> entityType) {
        return doSelectEntityWithDeletedCheck(buildPKCB(id), entityType);
    }

    private YsUserCB buildPKCB(Long id) {
        assertObjectNotNull("id", id);
        YsUserCB cb = newMyConditionBean();
        cb.query().setId_Equal(id);
        return cb;
    }

    // ===================================================================================
    //                                                                         List Select
    //                                                                         ===========
    /**
     * Select the list as result bean.
     * <pre>
     * YsUserCB cb = new YsUserCB();
     * cb.query().setFoo...(value);
     * cb.query().addOrderBy_Bar...();
     * ListResultBean&lt;YsUser&gt; ysUserList = ysUserBhv.<span style="color: #FD4747">selectList</span>(cb);
     * for (YsUser ysUser : ysUserList) {
     *     ... = ysUser.get...();
     * }
     * </pre>
     * @param cb The condition-bean of YsUser. (NotNull)
     * @return The result bean of selected list. (NotNull)
     * @exception org.seasar.dbflute.exception.DangerousResultSizeException When the result size is over the specified safety size.
     */
    public ListResultBean<YsUser> selectList(YsUserCB cb) {
        return doSelectList(cb, YsUser.class);
    }

    protected <ENTITY extends YsUser> ListResultBean<ENTITY> doSelectList(YsUserCB cb, Class<ENTITY> entityType) {
        assertCBNotNull(cb); assertObjectNotNull("entityType", entityType);
        assertSpecifyDerivedReferrerEntityProperty(cb, entityType);
        return helpSelectListInternally(cb, entityType, new InternalSelectListCallback<ENTITY, YsUserCB>() {
            public List<ENTITY> callbackSelectList(YsUserCB cb, Class<ENTITY> entityType) { return delegateSelectList(cb, entityType); } });
    }

    @Override
    protected ListResultBean<? extends Entity> doReadList(ConditionBean cb) {
        return selectList(downcast(cb));
    }

    // ===================================================================================
    //                                                                         Page Select
    //                                                                         ===========
    /**
     * Select the page as result bean. <br />
     * (both count-select and paging-select are executed)
     * <pre>
     * YsUserCB cb = new YsUserCB();
     * cb.query().setFoo...(value);
     * cb.query().addOrderBy_Bar...();
     * cb.<span style="color: #FD4747">paging</span>(20, 3); <span style="color: #3F7E5E">// 20 records per a page and current page number is 3</span>
     * PagingResultBean&lt;YsUser&gt; page = ysUserBhv.<span style="color: #FD4747">selectPage</span>(cb);
     * int allRecordCount = page.getAllRecordCount();
     * int allPageCount = page.getAllPageCount();
     * boolean isExistPrePage = page.isExistPrePage();
     * boolean isExistNextPage = page.isExistNextPage();
     * ...
     * for (YsUser ysUser : page) {
     *     ... = ysUser.get...();
     * }
     * </pre>
     * @param cb The condition-bean of YsUser. (NotNull)
     * @return The result bean of selected page. (NotNull)
     * @exception org.seasar.dbflute.exception.DangerousResultSizeException When the result size is over the specified safety size.
     */
    public PagingResultBean<YsUser> selectPage(YsUserCB cb) {
        return doSelectPage(cb, YsUser.class);
    }

    protected <ENTITY extends YsUser> PagingResultBean<ENTITY> doSelectPage(YsUserCB cb, Class<ENTITY> entityType) {
        assertCBNotNull(cb); assertObjectNotNull("entityType", entityType);
        return helpSelectPageInternally(cb, entityType, new InternalSelectPageCallback<ENTITY, YsUserCB>() {
            public int callbackSelectCount(YsUserCB cb) { return doSelectCount(cb); }
            public List<ENTITY> callbackSelectList(YsUserCB cb, Class<ENTITY> entityType) { return doSelectList(cb, entityType); }
        });
    }

    @Override
    protected PagingResultBean<? extends Entity> doReadPage(ConditionBean cb) {
        return selectPage(downcast(cb));
    }

    // ===================================================================================
    //                                                                       Scalar Select
    //                                                                       =============
    /**
     * Select the scalar value derived by a function. <br />
     * You should call a function method after this method called like as follows:
     * <pre>
     * ysUserBhv.<span style="color: #FD4747">scalarSelect</span>(Date.class).max(new ScalarQuery() {
     *     public void query(YsUserCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooDatetime()</span>; <span style="color: #3F7E5E">// required for a function</span>
     *         cb.query().setBarName_PrefixSearch("S");
     *     }
     * });
     * </pre>
     * @param <RESULT> The type of result.
     * @param resultType The type of result. (NotNull)
     * @return The scalar value derived by a function. (Nullable)
     */
    public <RESULT> SLFunction<YsUserCB, RESULT> scalarSelect(Class<RESULT> resultType) {
        return doScalarSelect(resultType, newMyConditionBean());
    }

    protected <RESULT, CB extends YsUserCB> SLFunction<CB, RESULT> doScalarSelect(Class<RESULT> resultType, CB cb) {
        assertObjectNotNull("resultType", resultType); assertCBNotNull(cb);
        cb.xsetupForScalarSelect(); cb.getSqlClause().disableSelectIndex(); // for when you use union
        return new SLFunction<CB, RESULT>(cb, resultType);
    }

    // ===================================================================================
    //                                                                            Sequence
    //                                                                            ========
    @Override
    protected Number doReadNextVal() {
        String msg = "This table is NOT related to sequence: " + getTableDbName();
        throw new UnsupportedOperationException(msg);
    }

    // ===================================================================================
    //                                                                       Load Referrer
    //                                                                       =============
    /**
     * {Refer to overload method that has an argument of the list of entity.}
     * @param ysUser The entity of ysUser. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean set-upper for registering referrer condition. (NotNull)
     */
    public void loadYsGroupUserList(YsUser ysUser, ConditionBeanSetupper<YsGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysUser, conditionBeanSetupper);
        loadYsGroupUserList(xnewLRLs(ysUser), conditionBeanSetupper);
    }
    /**
     * Load referrer of ysGroupUserList with the set-upper for condition-bean of referrer.
     * <pre>
     * ysUserBhv.<span style="color: #FD4747">loadYsGroupUserList</span>(ysUserList, new ConditionBeanSetupper&lt;YsGroupUserCB&gt;() {
     *     public void setup(YsGroupUserCB cb) {
     *         cb.setupSelect...();
     *         cb.query().setFoo...(value);
     *         cb.query().addOrderBy_Bar...(); <span style="color: #3F7E5E">// basically you should order referrer list</span>
     *     }
     * });
     * for (YsUser ysUser : ysUserList) {
     *     ... = ysUser.<span style="color: #FD4747">getYsGroupUserList()</span>;
     * }
     * </pre>
     * About internal policy, the value of primary key(and others too) is treated as case-insensitive. <br />
     * The condition-bean that the set-upper provides have settings before you touch it. It is as follows:
     * <pre>
     * cb.query().setUserId_InScope(pkList);
     * cb.query().addOrderBy_UserId_Asc();
     * </pre>
     * @param ysUserList The entity list of ysUser. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean set-upper for registering referrer condition. (NotNull)
     */
    public void loadYsGroupUserList(List<YsUser> ysUserList, ConditionBeanSetupper<YsGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysUserList, conditionBeanSetupper);
        loadYsGroupUserList(ysUserList, new LoadReferrerOption<YsGroupUserCB, YsGroupUser>().xinit(conditionBeanSetupper));
    }
    /**
     * {Refer to overload method that has an argument of the list of entity.}
     * @param ysUser The entity of ysUser. (NotNull)
     * @param loadReferrerOption The option of load-referrer. (NotNull)
     */
    public void loadYsGroupUserList(YsUser ysUser, LoadReferrerOption<YsGroupUserCB, YsGroupUser> loadReferrerOption) {
        xassLRArg(ysUser, loadReferrerOption);
        loadYsGroupUserList(xnewLRLs(ysUser), loadReferrerOption);
    }
    /**
     * {Refer to overload method that has an argument of condition-bean setupper.}
     * @param ysUserList The entity list of ysUser. (NotNull)
     * @param loadReferrerOption The option of load-referrer. (NotNull)
     */
    public void loadYsGroupUserList(List<YsUser> ysUserList, LoadReferrerOption<YsGroupUserCB, YsGroupUser> loadReferrerOption) {
        xassLRArg(ysUserList, loadReferrerOption);
        if (ysUserList.isEmpty()) { return; }
        final YsGroupUserBhv referrerBhv = xgetBSFLR().select(YsGroupUserBhv.class);
        helpLoadReferrerInternally(ysUserList, loadReferrerOption, new InternalLoadReferrerCallback<YsUser, Long, YsGroupUserCB, YsGroupUser>() {
            public Long getPKVal(YsUser e) { return e.getId(); }
            public void setRfLs(YsUser e, List<YsGroupUser> ls) { e.setYsGroupUserList(ls); }
            public YsGroupUserCB newMyCB() { return referrerBhv.newMyConditionBean(); }
            public void qyFKIn(YsGroupUserCB cb, List<Long> ls) { cb.query().setUserId_InScope(ls); }
            public void qyOdFKAsc(YsGroupUserCB cb) { cb.query().addOrderBy_UserId_Asc(); }
            public void spFKCol(YsGroupUserCB cb) { cb.specify().columnUserId(); }
            public List<YsGroupUser> selRfLs(YsGroupUserCB cb) { return referrerBhv.selectList(cb); }
            public Long getFKVal(YsGroupUser e) { return e.getUserId(); }
            public void setlcEt(YsGroupUser re, YsUser le) { re.setYsUser(le); }
        });
    }

    /**
     * {Refer to overload method that has an argument of the list of entity.}
     * @param ysUser The entity of ysUser. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean set-upper for registering referrer condition. (NotNull)
     */
    public void loadYsRoleGroupUserList(YsUser ysUser, ConditionBeanSetupper<YsRoleGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysUser, conditionBeanSetupper);
        loadYsRoleGroupUserList(xnewLRLs(ysUser), conditionBeanSetupper);
    }
    /**
     * Load referrer of ysRoleGroupUserList with the set-upper for condition-bean of referrer.
     * <pre>
     * ysUserBhv.<span style="color: #FD4747">loadYsRoleGroupUserList</span>(ysUserList, new ConditionBeanSetupper&lt;YsRoleGroupUserCB&gt;() {
     *     public void setup(YsRoleGroupUserCB cb) {
     *         cb.setupSelect...();
     *         cb.query().setFoo...(value);
     *         cb.query().addOrderBy_Bar...(); <span style="color: #3F7E5E">// basically you should order referrer list</span>
     *     }
     * });
     * for (YsUser ysUser : ysUserList) {
     *     ... = ysUser.<span style="color: #FD4747">getYsRoleGroupUserList()</span>;
     * }
     * </pre>
     * About internal policy, the value of primary key(and others too) is treated as case-insensitive. <br />
     * The condition-bean that the set-upper provides have settings before you touch it. It is as follows:
     * <pre>
     * cb.query().setUserId_InScope(pkList);
     * cb.query().addOrderBy_UserId_Asc();
     * </pre>
     * @param ysUserList The entity list of ysUser. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean set-upper for registering referrer condition. (NotNull)
     */
    public void loadYsRoleGroupUserList(List<YsUser> ysUserList, ConditionBeanSetupper<YsRoleGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysUserList, conditionBeanSetupper);
        loadYsRoleGroupUserList(ysUserList, new LoadReferrerOption<YsRoleGroupUserCB, YsRoleGroupUser>().xinit(conditionBeanSetupper));
    }
    /**
     * {Refer to overload method that has an argument of the list of entity.}
     * @param ysUser The entity of ysUser. (NotNull)
     * @param loadReferrerOption The option of load-referrer. (NotNull)
     */
    public void loadYsRoleGroupUserList(YsUser ysUser, LoadReferrerOption<YsRoleGroupUserCB, YsRoleGroupUser> loadReferrerOption) {
        xassLRArg(ysUser, loadReferrerOption);
        loadYsRoleGroupUserList(xnewLRLs(ysUser), loadReferrerOption);
    }
    /**
     * {Refer to overload method that has an argument of condition-bean setupper.}
     * @param ysUserList The entity list of ysUser. (NotNull)
     * @param loadReferrerOption The option of load-referrer. (NotNull)
     */
    public void loadYsRoleGroupUserList(List<YsUser> ysUserList, LoadReferrerOption<YsRoleGroupUserCB, YsRoleGroupUser> loadReferrerOption) {
        xassLRArg(ysUserList, loadReferrerOption);
        if (ysUserList.isEmpty()) { return; }
        final YsRoleGroupUserBhv referrerBhv = xgetBSFLR().select(YsRoleGroupUserBhv.class);
        helpLoadReferrerInternally(ysUserList, loadReferrerOption, new InternalLoadReferrerCallback<YsUser, Long, YsRoleGroupUserCB, YsRoleGroupUser>() {
            public Long getPKVal(YsUser e) { return e.getId(); }
            public void setRfLs(YsUser e, List<YsRoleGroupUser> ls) { e.setYsRoleGroupUserList(ls); }
            public YsRoleGroupUserCB newMyCB() { return referrerBhv.newMyConditionBean(); }
            public void qyFKIn(YsRoleGroupUserCB cb, List<Long> ls) { cb.query().setUserId_InScope(ls); }
            public void qyOdFKAsc(YsRoleGroupUserCB cb) { cb.query().addOrderBy_UserId_Asc(); }
            public void spFKCol(YsRoleGroupUserCB cb) { cb.specify().columnUserId(); }
            public List<YsRoleGroupUser> selRfLs(YsRoleGroupUserCB cb) { return referrerBhv.selectList(cb); }
            public Long getFKVal(YsRoleGroupUser e) { return e.getUserId(); }
            public void setlcEt(YsRoleGroupUser re, YsUser le) { re.setYsUser(le); }
        });
    }

    // ===================================================================================
    //                                                                    Pull out Foreign
    //                                                                    ================

    // ===================================================================================
    //                                                                       Entity Update
    //                                                                       =============
    /**
     * Insert the entity.
     * <pre>
     * YsUser ysUser = new YsUser();
     * <span style="color: #3F7E5E">// if auto-increment, you don't need to set the PK value</span>
     * ysUser.setFoo...(value);
     * ysUser.setBar...(value);
     * ysUserBhv.<span style="color: #FD4747">insert</span>(ysUser);
     * ... = ysUser.getPK...(); <span style="color: #3F7E5E">// if auto-increment, you can get the value after</span>
     * </pre>
     * @param ysUser The entity of insert target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insert(YsUser ysUser) {
        assertEntityNotNull(ysUser);
        delegateInsert(ysUser);
    }

    @Override
    protected void doCreate(Entity entity) {
        insert(downcast(entity));
    }

    /**
     * Update the entity modified-only. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * YsUser ysUser = new YsUser();
     * ysUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysUser.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * ysUser.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     ysUserBhv.<span style="color: #FD4747">update</span>(ysUser);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * } 
     * </pre>
     * @param ysUser The entity of update target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void update(final YsUser ysUser) {
        helpUpdateInternally(ysUser, new InternalUpdateCallback<YsUser>() {
            public int callbackDelegateUpdate(YsUser entity) { return delegateUpdate(entity); } });
    }

    @Override
    protected void doModify(Entity entity) {
        update(downcast(entity));
    }

    /**
     * Update the entity non-strictly modified-only. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsUser ysUser = new YsUser();
     * ysUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysUser.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysUser.setVersionNo(value);</span>
     * ysUserBhv.<span style="color: #FD4747">updateNonstrict</span>(ysUser);
     * </pre>
     * @param ysUser The entity of update target. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void updateNonstrict(final YsUser ysUser) {
        helpUpdateNonstrictInternally(ysUser, new InternalUpdateNonstrictCallback<YsUser>() {
            public int callbackDelegateUpdateNonstrict(YsUser entity) { return delegateUpdateNonstrict(entity); } });
    }

    @Override
    protected void doModifyNonstrict(Entity entity) {
        updateNonstrict(downcast(entity));
    }

    /**
     * Insert or update the entity modified-only. {ExclusiveControl(when update)}
     * @param ysUser The entity of insert or update target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insertOrUpdate(final YsUser ysUser) {
        helpInsertOrUpdateInternally(ysUser, new InternalInsertOrUpdateCallback<YsUser, YsUserCB>() {
            public void callbackInsert(YsUser entity) { insert(entity); }
            public void callbackUpdate(YsUser entity) { update(entity); }
            public YsUserCB callbackNewMyConditionBean() { return newMyConditionBean(); }
            public int callbackSelectCount(YsUserCB cb) { return selectCount(cb); }
        });
    }

    @Override
    protected void doCreateOrUpdate(Entity entity) {
        insertOrUpdate(downcast(entity));
    }

    /**
     * Insert or update the entity non-strictly modified-only. {NonExclusiveControl(when update)}
     * @param ysUser The entity of insert or update target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insertOrUpdateNonstrict(YsUser ysUser) {
        helpInsertOrUpdateInternally(ysUser, new InternalInsertOrUpdateNonstrictCallback<YsUser>() {
            public void callbackInsert(YsUser entity) { insert(entity); }
            public void callbackUpdateNonstrict(YsUser entity) { updateNonstrict(entity); }
        });
    }

    @Override
    protected void doCreateOrUpdateNonstrict(Entity entity) {
        insertOrUpdateNonstrict(downcast(entity));
    }

    /**
     * Delete the entity. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * YsUser ysUser = new YsUser();
     * ysUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * ysUser.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     ysUserBhv.<span style="color: #FD4747">delete</span>(ysUser);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * } 
     * </pre>
     * @param ysUser The entity of delete target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void delete(YsUser ysUser) {
        helpDeleteInternally(ysUser, new InternalDeleteCallback<YsUser>() {
            public int callbackDelegateDelete(YsUser entity) { return delegateDelete(entity); } });
    }

    @Override
    protected void doRemove(Entity entity) {
        delete(downcast(entity));
    }

    /**
     * Delete the entity non-strictly. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsUser ysUser = new YsUser();
     * ysUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysUser.setVersionNo(value);</span>
     * ysUserBhv.<span style="color: #FD4747">deleteNonstrict</span>(ysUser);
     * </pre>
     * @param ysUser Entity. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void deleteNonstrict(YsUser ysUser) {
        helpDeleteNonstrictInternally(ysUser, new InternalDeleteNonstrictCallback<YsUser>() {
            public int callbackDelegateDeleteNonstrict(YsUser entity) { return delegateDeleteNonstrict(entity); } });
    }

    /**
     * Delete the entity non-strictly ignoring deleted. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsUser ysUser = new YsUser();
     * ysUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysUser.setVersionNo(value);</span>
     * ysUserBhv.<span style="color: #FD4747">deleteNonstrictIgnoreDeleted</span>(ysUser);
     * <span style="color: #3F7E5E">// if the target entity doesn't exist, no exception</span>
     * </pre>
     * @param ysUser Entity. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void deleteNonstrictIgnoreDeleted(YsUser ysUser) {
        helpDeleteNonstrictIgnoreDeletedInternally(ysUser, new InternalDeleteNonstrictIgnoreDeletedCallback<YsUser>() {
            public int callbackDelegateDeleteNonstrict(YsUser entity) { return delegateDeleteNonstrict(entity); } });
    }

    // ===================================================================================
    //                                                                        Batch Update
    //                                                                        ============
    /**
     * Batch-insert the list. This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysUserList The list of the entity. (NotNull)
     * @return The array of inserted count.
     */
    public int[] batchInsert(List<YsUser> ysUserList) {
        assertObjectNotNull("ysUserList", ysUserList);
        return delegateInsertList(ysUserList);
    }

    /**
     * Batch-update the list. <br />
     * All columns are update target. {NOT modified only} <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysUserList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchUpdate(List<YsUser> ysUserList) {
        assertObjectNotNull("ysUserList", ysUserList);
        return delegateUpdateList(ysUserList);
    }

    /**
     * Batch-update the list non-strictly. <br />
     * All columns are update target. {NOT modified only} <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysUserList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchUpdateNonstrict(List<YsUser> ysUserList) {
        assertObjectNotNull("ysUserList", ysUserList);
        return delegateUpdateListNonstrict(ysUserList);
    }

    /**
     * Batch-delete the list. <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysUserList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchDelete(List<YsUser> ysUserList) {
        assertObjectNotNull("ysUserList", ysUserList);
        return delegateDeleteList(ysUserList);
    }

    /**
     * Batch-delete the list non-strictly. <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysUserList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchDeleteNonstrict(List<YsUser> ysUserList) {
        assertObjectNotNull("ysUserList", ysUserList);
        return delegateDeleteListNonstrict(ysUserList);
    }

    // ===================================================================================
    //                                                                        Query Update
    //                                                                        ============
    /**
     * Query-update the several entities non-strictly modified-only. {NonExclusiveControl}
     * <pre>
     * YsUser ysUser = new YsUser();
     * <span style="color: #3F7E5E">// you don't need to set PK value</span>
     * <span style="color: #3F7E5E">//ysUser.setPK...(value);</span>
     * ysUser.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysUser.setVersionNo(value);</span>
     * YsUserCB cb = new YsUserCB();
     * cb.query().setFoo...(value);
     * ysUserBhv.<span style="color: #FD4747">queryUpdate</span>(ysUser, cb);
     * </pre>
     * @param ysUser The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of YsUser. (NotNull)
     * @return The updated count.
     */
    public int queryUpdate(YsUser ysUser, YsUserCB cb) {
        return delegateQueryUpdate(ysUser, cb);
    }

    /**
     * Query-delete the several entities. {NonExclusiveControl}
     * <pre>
     * YsUserCB cb = new YsUserCB();
     * cb.query().setFoo...(value);
     * ysUserBhv.<span style="color: #FD4747">queryDelete</span>(ysUser, cb);
     * </pre>
     * @param cb The condition-bean of YsUser. (NotNull)
     * @return The deleted count.
     */
    public int queryDelete(YsUserCB cb) {
        return delegateQueryDelete(cb);
    }

    /**
     * Varying-update the entity modified-only. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * YsUser ysUser = new YsUser();
     * ysUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysUser.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * ysUser.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     UpdateOption&lt;YsUserCB&gt; option = new UpdateOption&lt;YsUserCB&gt;();
     *     option.self(new SpecifyQuery&lt;YsUserCB&gt;() {
     *         public void specify(YsUserCB cb) {
     *             cb.specify().<span style="color: #FD4747">columnXxxCount()</span>;
     *         }
     *     }).plus(1); <span style="color: #3F7E5E">// XXX_COUNT = XXX_COUNT + 1</span>
     *     ysUserBhv.<span style="color: #FD4747">varyingUpdate</span>(ysUser, option);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * }
     * </pre>
     * @param ysUser The entity of update target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @param option The option of update for varying values. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void varyingUpdate(YsUser ysUser, final UpdateOption<YsUserCB> option) {
        processVaryingUpdate(option);
        helpUpdateInternally(ysUser, new InternalUpdateCallback<YsUser>() {
            public int callbackDelegateUpdate(YsUser entity) { return delegateVaryingUpdate(entity, option); } });
    }

    /**
     * Varying-update the entity non-strictly modified-only. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsUser ysUser = new YsUser();
     * ysUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysUser.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysUser.setVersionNo(value);</span>
     * UpdateOption&lt;YsUserCB&gt; option = new UpdateOption&lt;YsUserCB&gt;();
     * option.self(new SpecifyQuery&lt;YsUserCB&gt;() {
     *     public void specify(YsUserCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooCount()</span>;
     *     }
     * }).plus(1); <span style="color: #3F7E5E">// FOO_COUNT = FOO_COUNT + 1</span>
     * ysUserBhv.<span style="color: #FD4747">varyingUpdateNonstrict</span>(ysUser, option);
     * </pre>
     * @param ysUser The entity of update target. (NotNull) {PrimaryKeyRequired}
     * @param option The option of update for varying values. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void varyingUpdateNonstrict(YsUser ysUser, final UpdateOption<YsUserCB> option) {
        processVaryingUpdate(option);
        helpUpdateNonstrictInternally(ysUser, new InternalUpdateNonstrictCallback<YsUser>() {
            public int callbackDelegateUpdateNonstrict(YsUser entity) { return delegateVaryingUpdateNonstrict(entity, option); } });
    }

    /**
     * Varying-query-update the several entities non-strictly modified-only. {NonExclusiveControl}
     * <pre>
     * YsUser ysUser = new YsUser();
     * <span style="color: #3F7E5E">// you don't need to set PK value</span>
     * <span style="color: #3F7E5E">//ysUser.setPK...(value);</span>
     * ysUser.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysUser.setVersionNo(value);</span>
     * YsUserCB cb = new YsUserCB();
     * cb.query().setFoo...(value);
     * UpdateOption&lt;YsUserCB&gt; option = new UpdateOption&lt;YsUserCB&gt;();
     * option.self(new SpecifyQuery&lt;YsUserCB&gt;() {
     *     public void specify(YsUserCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooCount()</span>;
     *     }
     * }).plus(1); <span style="color: #3F7E5E">// FOO_COUNT = FOO_COUNT + 1</span>
     * ysUserBhv.<span style="color: #FD4747">varyingQueryUpdate</span>(ysUser, cb, option);
     * </pre>
     * @param ysUser The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of YsUser. (NotNull)
     * @param option The option of update for varying values. (NotNull)
     * @return The updated count.
     */
    public int varyingQueryUpdate(YsUser ysUser, YsUserCB cb, final UpdateOption<YsUserCB> option) {
        processVaryingUpdate(option);
        return delegateVaryingQueryUpdate(ysUser, cb, option);
    }

    protected void processVaryingUpdate(UpdateOption<YsUserCB> option) {
        assertUpdateOptionNotNull(option);
        YsUserCB cb = newMyConditionBean();
        cb.xsetupForVaryingUpdate();
        option.resolveSpecification(cb);
    }
    
    // ===================================================================================
    //                                                                     Delegate Method
    //                                                                     ===============
    // [Behavior Command]
    // -----------------------------------------------------
    //                                                Select
    //                                                ------
    protected int delegateSelectCount(YsUserCB cb) { return invoke(createSelectCountCBCommand(cb)); }
    protected <ENTITY extends YsUser> void delegateSelectCursor(YsUserCB cb, EntityRowHandler<ENTITY> entityRowHandler, Class<ENTITY> entityType)
    { invoke(createSelectCursorCBCommand(cb, entityRowHandler, entityType)); }
    protected <ENTITY extends YsUser> List<ENTITY> delegateSelectList(YsUserCB cb, Class<ENTITY> entityType)
    { return invoke(createSelectListCBCommand(cb, entityType)); }

    // -----------------------------------------------------
    //                                                Update
    //                                                ------
    protected int delegateInsert(YsUser e)
    { if (!processBeforeInsert(e)) { return 1; } return invoke(createInsertEntityCommand(e)); }
    protected int delegateUpdate(YsUser e)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createUpdateEntityCommand(e)); }
    protected int delegateUpdateNonstrict(YsUser e)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createUpdateNonstrictEntityCommand(e)); }
    protected int delegateDelete(YsUser e)
    { if (!processBeforeDelete(e)) { return 1; } return invoke(createDeleteEntityCommand(e)); }
    protected int delegateDeleteNonstrict(YsUser e)
    { if (!processBeforeDelete(e)) { return 1; } return invoke(createDeleteNonstrictEntityCommand(e)); }

    protected int[] delegateInsertList(List<YsUser> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchInsertEntityCommand(helpFilterBeforeInsertInternally(ls))); }
    @SuppressWarnings("unchecked")
    protected int[] doCreateList(List<Entity> ls) { return delegateInsertList((List)ls); }
    protected int[] delegateUpdateList(List<YsUser> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchUpdateEntityCommand(helpFilterBeforeUpdateInternally(ls))); }
    @SuppressWarnings("unchecked")
    protected int[] doModifyList(List<Entity> ls) { return delegateUpdateList((List)ls); }
    protected int[] delegateUpdateListNonstrict(List<YsUser> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchUpdateNonstrictEntityCommand(helpFilterBeforeUpdateInternally(ls))); }
    protected int[] delegateDeleteList(List<YsUser> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchDeleteEntityCommand(helpFilterBeforeDeleteInternally(ls))); }
    @SuppressWarnings("unchecked")
    protected int[] doRemoveList(List<Entity> ls) { return delegateDeleteList((List)ls); }
    protected int[] delegateDeleteListNonstrict(List<YsUser> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchDeleteNonstrictEntityCommand(helpFilterBeforeDeleteInternally(ls))); }

    protected int delegateQueryUpdate(YsUser e, YsUserCB cb)
    { if (!processBeforeQueryUpdate(e, cb)) { return 0; } return invoke(createQueryUpdateEntityCBCommand(e, cb));  }
    protected int delegateQueryDelete(YsUserCB cb)
    { if (!processBeforeQueryDelete(cb)) { return 0; } return invoke(createQueryDeleteCBCommand(cb));  }

    protected int delegateVaryingUpdate(YsUser e, UpdateOption<YsUserCB> op)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createVaryingUpdateEntityCommand(e, op)); }
    protected int delegateVaryingUpdateNonstrict(YsUser e, UpdateOption<YsUserCB> op)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createVaryingUpdateNonstrictEntityCommand(e, op)); }
    protected int delegateVaryingQueryUpdate(YsUser e, YsUserCB cb, UpdateOption<YsUserCB> op)
    { if (!processBeforeQueryUpdate(e, cb)) { return 0; } return invoke(createVaryingQueryUpdateEntityCBCommand(e, cb, op));  }

    // ===================================================================================
    //                                                                Optimistic Lock Info
    //                                                                ====================
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasVersionNoValue(Entity entity) {
        return !(downcast(entity).getVersionNo() + "").equals("null");// For primitive type
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasUpdateDateValue(Entity entity) {
        return false;
    }

    // ===================================================================================
    //                                                                     Downcast Helper
    //                                                                     ===============
    protected YsUser downcast(Entity entity) {
        return helpEntityDowncastInternally(entity, YsUser.class);
    }

    protected YsUserCB downcast(ConditionBean cb) {
        return helpConditionBeanDowncastInternally(cb, YsUserCB.class);
    }
}
