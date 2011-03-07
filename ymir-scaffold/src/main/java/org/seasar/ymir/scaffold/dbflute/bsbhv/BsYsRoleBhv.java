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
 * The behavior of (ロール)YS_ROLE as TABLE. <br />
 * <pre>
 * [primary-key]
 *     ID
 * 
 * [column]
 *     ID, NAME, DISPLAY_NAME, CREATED_DATE, MODIFIED_DATE, VERSION_NO
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
 *     YS_ROLE_GROUP_USER
 * 
 * [foreign-property]
 *     
 * 
 * [referrer-property]
 *     ysRoleGroupUserList
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsYsRoleBhv extends AbstractBehaviorWritable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /*df:BehaviorQueryPathBegin*/
    /*df:BehaviorQueryPathEnd*/

    // ===================================================================================
    //                                                                          Table name
    //                                                                          ==========
    /** @return The name on database of table. (NotNull) */
    public String getTableDbName() { return "YS_ROLE"; }

    // ===================================================================================
    //                                                                              DBMeta
    //                                                                              ======
    /** @return The instance of DBMeta. (NotNull) */
    public DBMeta getDBMeta() { return YsRoleDbm.getInstance(); }

    /** @return The instance of DBMeta as my table type. (NotNull) */
    public YsRoleDbm getMyDBMeta() { return YsRoleDbm.getInstance(); }

    // ===================================================================================
    //                                                                        New Instance
    //                                                                        ============
    /** {@inheritDoc} */
    public Entity newEntity() { return newMyEntity(); }

    /** {@inheritDoc} */
    public ConditionBean newConditionBean() { return newMyConditionBean(); }

    /** @return The instance of new entity as my table type. (NotNull) */
    public YsRole newMyEntity() { return new YsRole(); }

    /** @return The instance of new condition-bean as my table type. (NotNull) */
    public YsRoleCB newMyConditionBean() { return new YsRoleCB(); }

    // ===================================================================================
    //                                                                        Count Select
    //                                                                        ============
    /**
     * Select the count by the condition-bean. {IgnorePagingCondition}
     * <pre>
     * YsRoleCB cb = new YsRoleCB();
     * cb.query().setFoo...(value);
     * int count = ysRoleBhv.<span style="color: #FD4747">selectCount</span>(cb);
     * </pre>
     * @param cb The condition-bean of YsRole. (NotNull)
     * @return The selected count.
     */
    public int selectCount(YsRoleCB cb) {
        return doSelectCount(cb);
    }

    protected int doSelectCount(YsRoleCB cb) {
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
     * YsRoleCB cb = new YsRoleCB();
     * cb.query().setFoo...(value);
     * ysRoleBhv.<span style="color: #FD4747">selectCursor</span>(cb, new EntityRowHandler&lt;YsRole&gt;() {
     *     public void handle(YsRole entity) {
     *         ... = entity.getFoo...();
     *     }
     * });
     * </pre>
     * @param cb The condition-bean of YsRole. (NotNull)
     * @param entityRowHandler The handler of entity row of YsRole. (NotNull)
     */
    public void selectCursor(YsRoleCB cb, EntityRowHandler<YsRole> entityRowHandler) {
        doSelectCursor(cb, entityRowHandler, YsRole.class);
    }

    protected <ENTITY extends YsRole> void doSelectCursor(YsRoleCB cb, EntityRowHandler<ENTITY> entityRowHandler, Class<ENTITY> entityType) {
        assertCBNotNull(cb); assertObjectNotNull("entityRowHandler<YsRole>", entityRowHandler); assertObjectNotNull("entityType", entityType);
        assertSpecifyDerivedReferrerEntityProperty(cb, entityType);
        delegateSelectCursor(cb, entityRowHandler, entityType);
    }

    // ===================================================================================
    //                                                                       Entity Select
    //                                                                       =============
    /**
     * Select the entity by the condition-bean.
     * <pre>
     * YsRoleCB cb = new YsRoleCB();
     * cb.query().setFoo...(value);
     * YsRole ysRole = ysRoleBhv.<span style="color: #FD4747">selectEntity</span>(cb);
     * if (ysRole != null) {
     *     ... = ysRole.get...();
     * } else {
     *     ...
     * }
     * </pre>
     * @param cb The condition-bean of YsRole. (NotNull)
     * @return The selected entity. (Nullable: If the condition has no data, it returns null)
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public YsRole selectEntity(YsRoleCB cb) {
        return doSelectEntity(cb, YsRole.class);
    }

    protected <ENTITY extends YsRole> ENTITY doSelectEntity(final YsRoleCB cb, final Class<ENTITY> entityType) {
        return helpSelectEntityInternally(cb, new InternalSelectEntityCallback<ENTITY, YsRoleCB>() {
            public List<ENTITY> callbackSelectList(YsRoleCB cb) { return doSelectList(cb, entityType); } });
    }

    @Override
    protected Entity doReadEntity(ConditionBean cb) {
        return selectEntity(downcast(cb));
    }

    /**
     * Select the entity by the condition-bean with deleted check.
     * <pre>
     * YsRoleCB cb = new YsRoleCB();
     * cb.query().setFoo...(value);
     * YsRole ysRole = ysRoleBhv.<span style="color: #FD4747">selectEntityWithDeletedCheck</span>(cb);
     * ... = ysRole.get...(); <span style="color: #3F7E5E">// the entity always be not null</span>
     * </pre>
     * @param cb The condition-bean of YsRole. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public YsRole selectEntityWithDeletedCheck(YsRoleCB cb) {
        return doSelectEntityWithDeletedCheck(cb, YsRole.class);
    }

    protected <ENTITY extends YsRole> ENTITY doSelectEntityWithDeletedCheck(final YsRoleCB cb, final Class<ENTITY> entityType) {
        return helpSelectEntityWithDeletedCheckInternally(cb, new InternalSelectEntityWithDeletedCheckCallback<ENTITY, YsRoleCB>() {
            public List<ENTITY> callbackSelectList(YsRoleCB cb) { return doSelectList(cb, entityType); } });
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
    public YsRole selectByPKValue(Long id) {
        return doSelectByPKValue(id, YsRole.class);
    }

    protected <ENTITY extends YsRole> ENTITY doSelectByPKValue(Long id, Class<ENTITY> entityType) {
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
    public YsRole selectByPKValueWithDeletedCheck(Long id) {
        return doSelectByPKValueWithDeletedCheck(id, YsRole.class);
    }

    protected <ENTITY extends YsRole> ENTITY doSelectByPKValueWithDeletedCheck(Long id, Class<ENTITY> entityType) {
        return doSelectEntityWithDeletedCheck(buildPKCB(id), entityType);
    }

    private YsRoleCB buildPKCB(Long id) {
        assertObjectNotNull("id", id);
        YsRoleCB cb = newMyConditionBean();
        cb.query().setId_Equal(id);
        return cb;
    }

    // ===================================================================================
    //                                                                         List Select
    //                                                                         ===========
    /**
     * Select the list as result bean.
     * <pre>
     * YsRoleCB cb = new YsRoleCB();
     * cb.query().setFoo...(value);
     * cb.query().addOrderBy_Bar...();
     * ListResultBean&lt;YsRole&gt; ysRoleList = ysRoleBhv.<span style="color: #FD4747">selectList</span>(cb);
     * for (YsRole ysRole : ysRoleList) {
     *     ... = ysRole.get...();
     * }
     * </pre>
     * @param cb The condition-bean of YsRole. (NotNull)
     * @return The result bean of selected list. (NotNull)
     * @exception org.seasar.dbflute.exception.DangerousResultSizeException When the result size is over the specified safety size.
     */
    public ListResultBean<YsRole> selectList(YsRoleCB cb) {
        return doSelectList(cb, YsRole.class);
    }

    protected <ENTITY extends YsRole> ListResultBean<ENTITY> doSelectList(YsRoleCB cb, Class<ENTITY> entityType) {
        assertCBNotNull(cb); assertObjectNotNull("entityType", entityType);
        assertSpecifyDerivedReferrerEntityProperty(cb, entityType);
        return helpSelectListInternally(cb, entityType, new InternalSelectListCallback<ENTITY, YsRoleCB>() {
            public List<ENTITY> callbackSelectList(YsRoleCB cb, Class<ENTITY> entityType) { return delegateSelectList(cb, entityType); } });
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
     * YsRoleCB cb = new YsRoleCB();
     * cb.query().setFoo...(value);
     * cb.query().addOrderBy_Bar...();
     * cb.<span style="color: #FD4747">paging</span>(20, 3); <span style="color: #3F7E5E">// 20 records per a page and current page number is 3</span>
     * PagingResultBean&lt;YsRole&gt; page = ysRoleBhv.<span style="color: #FD4747">selectPage</span>(cb);
     * int allRecordCount = page.getAllRecordCount();
     * int allPageCount = page.getAllPageCount();
     * boolean isExistPrePage = page.isExistPrePage();
     * boolean isExistNextPage = page.isExistNextPage();
     * ...
     * for (YsRole ysRole : page) {
     *     ... = ysRole.get...();
     * }
     * </pre>
     * @param cb The condition-bean of YsRole. (NotNull)
     * @return The result bean of selected page. (NotNull)
     * @exception org.seasar.dbflute.exception.DangerousResultSizeException When the result size is over the specified safety size.
     */
    public PagingResultBean<YsRole> selectPage(YsRoleCB cb) {
        return doSelectPage(cb, YsRole.class);
    }

    protected <ENTITY extends YsRole> PagingResultBean<ENTITY> doSelectPage(YsRoleCB cb, Class<ENTITY> entityType) {
        assertCBNotNull(cb); assertObjectNotNull("entityType", entityType);
        return helpSelectPageInternally(cb, entityType, new InternalSelectPageCallback<ENTITY, YsRoleCB>() {
            public int callbackSelectCount(YsRoleCB cb) { return doSelectCount(cb); }
            public List<ENTITY> callbackSelectList(YsRoleCB cb, Class<ENTITY> entityType) { return doSelectList(cb, entityType); }
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
     * ysRoleBhv.<span style="color: #FD4747">scalarSelect</span>(Date.class).max(new ScalarQuery() {
     *     public void query(YsRoleCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooDatetime()</span>; <span style="color: #3F7E5E">// required for a function</span>
     *         cb.query().setBarName_PrefixSearch("S");
     *     }
     * });
     * </pre>
     * @param <RESULT> The type of result.
     * @param resultType The type of result. (NotNull)
     * @return The scalar value derived by a function. (Nullable)
     */
    public <RESULT> SLFunction<YsRoleCB, RESULT> scalarSelect(Class<RESULT> resultType) {
        return doScalarSelect(resultType, newMyConditionBean());
    }

    protected <RESULT, CB extends YsRoleCB> SLFunction<CB, RESULT> doScalarSelect(Class<RESULT> resultType, CB cb) {
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
     * @param ysRole The entity of ysRole. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean set-upper for registering referrer condition. (NotNull)
     */
    public void loadYsRoleGroupUserList(YsRole ysRole, ConditionBeanSetupper<YsRoleGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysRole, conditionBeanSetupper);
        loadYsRoleGroupUserList(xnewLRLs(ysRole), conditionBeanSetupper);
    }
    /**
     * Load referrer of ysRoleGroupUserList with the set-upper for condition-bean of referrer.
     * <pre>
     * ysRoleBhv.<span style="color: #FD4747">loadYsRoleGroupUserList</span>(ysRoleList, new ConditionBeanSetupper&lt;YsRoleGroupUserCB&gt;() {
     *     public void setup(YsRoleGroupUserCB cb) {
     *         cb.setupSelect...();
     *         cb.query().setFoo...(value);
     *         cb.query().addOrderBy_Bar...(); <span style="color: #3F7E5E">// basically you should order referrer list</span>
     *     }
     * });
     * for (YsRole ysRole : ysRoleList) {
     *     ... = ysRole.<span style="color: #FD4747">getYsRoleGroupUserList()</span>;
     * }
     * </pre>
     * About internal policy, the value of primary key(and others too) is treated as case-insensitive. <br />
     * The condition-bean that the set-upper provides have settings before you touch it. It is as follows:
     * <pre>
     * cb.query().setRoleId_InScope(pkList);
     * cb.query().addOrderBy_RoleId_Asc();
     * </pre>
     * @param ysRoleList The entity list of ysRole. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean set-upper for registering referrer condition. (NotNull)
     */
    public void loadYsRoleGroupUserList(List<YsRole> ysRoleList, ConditionBeanSetupper<YsRoleGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysRoleList, conditionBeanSetupper);
        loadYsRoleGroupUserList(ysRoleList, new LoadReferrerOption<YsRoleGroupUserCB, YsRoleGroupUser>().xinit(conditionBeanSetupper));
    }
    /**
     * {Refer to overload method that has an argument of the list of entity.}
     * @param ysRole The entity of ysRole. (NotNull)
     * @param loadReferrerOption The option of load-referrer. (NotNull)
     */
    public void loadYsRoleGroupUserList(YsRole ysRole, LoadReferrerOption<YsRoleGroupUserCB, YsRoleGroupUser> loadReferrerOption) {
        xassLRArg(ysRole, loadReferrerOption);
        loadYsRoleGroupUserList(xnewLRLs(ysRole), loadReferrerOption);
    }
    /**
     * {Refer to overload method that has an argument of condition-bean setupper.}
     * @param ysRoleList The entity list of ysRole. (NotNull)
     * @param loadReferrerOption The option of load-referrer. (NotNull)
     */
    public void loadYsRoleGroupUserList(List<YsRole> ysRoleList, LoadReferrerOption<YsRoleGroupUserCB, YsRoleGroupUser> loadReferrerOption) {
        xassLRArg(ysRoleList, loadReferrerOption);
        if (ysRoleList.isEmpty()) { return; }
        final YsRoleGroupUserBhv referrerBhv = xgetBSFLR().select(YsRoleGroupUserBhv.class);
        helpLoadReferrerInternally(ysRoleList, loadReferrerOption, new InternalLoadReferrerCallback<YsRole, Long, YsRoleGroupUserCB, YsRoleGroupUser>() {
            public Long getPKVal(YsRole e) { return e.getId(); }
            public void setRfLs(YsRole e, List<YsRoleGroupUser> ls) { e.setYsRoleGroupUserList(ls); }
            public YsRoleGroupUserCB newMyCB() { return referrerBhv.newMyConditionBean(); }
            public void qyFKIn(YsRoleGroupUserCB cb, List<Long> ls) { cb.query().setRoleId_InScope(ls); }
            public void qyOdFKAsc(YsRoleGroupUserCB cb) { cb.query().addOrderBy_RoleId_Asc(); }
            public void spFKCol(YsRoleGroupUserCB cb) { cb.specify().columnRoleId(); }
            public List<YsRoleGroupUser> selRfLs(YsRoleGroupUserCB cb) { return referrerBhv.selectList(cb); }
            public Long getFKVal(YsRoleGroupUser e) { return e.getRoleId(); }
            public void setlcEt(YsRoleGroupUser re, YsRole le) { re.setYsRole(le); }
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
     * YsRole ysRole = new YsRole();
     * <span style="color: #3F7E5E">// if auto-increment, you don't need to set the PK value</span>
     * ysRole.setFoo...(value);
     * ysRole.setBar...(value);
     * ysRoleBhv.<span style="color: #FD4747">insert</span>(ysRole);
     * ... = ysRole.getPK...(); <span style="color: #3F7E5E">// if auto-increment, you can get the value after</span>
     * </pre>
     * @param ysRole The entity of insert target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insert(YsRole ysRole) {
        assertEntityNotNull(ysRole);
        delegateInsert(ysRole);
    }

    @Override
    protected void doCreate(Entity entity) {
        insert(downcast(entity));
    }

    /**
     * Update the entity modified-only. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * YsRole ysRole = new YsRole();
     * ysRole.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysRole.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * ysRole.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     ysRoleBhv.<span style="color: #FD4747">update</span>(ysRole);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * } 
     * </pre>
     * @param ysRole The entity of update target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void update(final YsRole ysRole) {
        helpUpdateInternally(ysRole, new InternalUpdateCallback<YsRole>() {
            public int callbackDelegateUpdate(YsRole entity) { return delegateUpdate(entity); } });
    }

    @Override
    protected void doModify(Entity entity) {
        update(downcast(entity));
    }

    /**
     * Update the entity non-strictly modified-only. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsRole ysRole = new YsRole();
     * ysRole.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysRole.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysRole.setVersionNo(value);</span>
     * ysRoleBhv.<span style="color: #FD4747">updateNonstrict</span>(ysRole);
     * </pre>
     * @param ysRole The entity of update target. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void updateNonstrict(final YsRole ysRole) {
        helpUpdateNonstrictInternally(ysRole, new InternalUpdateNonstrictCallback<YsRole>() {
            public int callbackDelegateUpdateNonstrict(YsRole entity) { return delegateUpdateNonstrict(entity); } });
    }

    @Override
    protected void doModifyNonstrict(Entity entity) {
        updateNonstrict(downcast(entity));
    }

    /**
     * Insert or update the entity modified-only. {ExclusiveControl(when update)}
     * @param ysRole The entity of insert or update target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insertOrUpdate(final YsRole ysRole) {
        helpInsertOrUpdateInternally(ysRole, new InternalInsertOrUpdateCallback<YsRole, YsRoleCB>() {
            public void callbackInsert(YsRole entity) { insert(entity); }
            public void callbackUpdate(YsRole entity) { update(entity); }
            public YsRoleCB callbackNewMyConditionBean() { return newMyConditionBean(); }
            public int callbackSelectCount(YsRoleCB cb) { return selectCount(cb); }
        });
    }

    @Override
    protected void doCreateOrUpdate(Entity entity) {
        insertOrUpdate(downcast(entity));
    }

    /**
     * Insert or update the entity non-strictly modified-only. {NonExclusiveControl(when update)}
     * @param ysRole The entity of insert or update target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insertOrUpdateNonstrict(YsRole ysRole) {
        helpInsertOrUpdateInternally(ysRole, new InternalInsertOrUpdateNonstrictCallback<YsRole>() {
            public void callbackInsert(YsRole entity) { insert(entity); }
            public void callbackUpdateNonstrict(YsRole entity) { updateNonstrict(entity); }
        });
    }

    @Override
    protected void doCreateOrUpdateNonstrict(Entity entity) {
        insertOrUpdateNonstrict(downcast(entity));
    }

    /**
     * Delete the entity. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * YsRole ysRole = new YsRole();
     * ysRole.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * ysRole.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     ysRoleBhv.<span style="color: #FD4747">delete</span>(ysRole);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * } 
     * </pre>
     * @param ysRole The entity of delete target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void delete(YsRole ysRole) {
        helpDeleteInternally(ysRole, new InternalDeleteCallback<YsRole>() {
            public int callbackDelegateDelete(YsRole entity) { return delegateDelete(entity); } });
    }

    @Override
    protected void doRemove(Entity entity) {
        delete(downcast(entity));
    }

    /**
     * Delete the entity non-strictly. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsRole ysRole = new YsRole();
     * ysRole.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysRole.setVersionNo(value);</span>
     * ysRoleBhv.<span style="color: #FD4747">deleteNonstrict</span>(ysRole);
     * </pre>
     * @param ysRole Entity. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void deleteNonstrict(YsRole ysRole) {
        helpDeleteNonstrictInternally(ysRole, new InternalDeleteNonstrictCallback<YsRole>() {
            public int callbackDelegateDeleteNonstrict(YsRole entity) { return delegateDeleteNonstrict(entity); } });
    }

    /**
     * Delete the entity non-strictly ignoring deleted. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsRole ysRole = new YsRole();
     * ysRole.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysRole.setVersionNo(value);</span>
     * ysRoleBhv.<span style="color: #FD4747">deleteNonstrictIgnoreDeleted</span>(ysRole);
     * <span style="color: #3F7E5E">// if the target entity doesn't exist, no exception</span>
     * </pre>
     * @param ysRole Entity. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void deleteNonstrictIgnoreDeleted(YsRole ysRole) {
        helpDeleteNonstrictIgnoreDeletedInternally(ysRole, new InternalDeleteNonstrictIgnoreDeletedCallback<YsRole>() {
            public int callbackDelegateDeleteNonstrict(YsRole entity) { return delegateDeleteNonstrict(entity); } });
    }

    // ===================================================================================
    //                                                                        Batch Update
    //                                                                        ============
    /**
     * Batch-insert the list. This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleList The list of the entity. (NotNull)
     * @return The array of inserted count.
     */
    public int[] batchInsert(List<YsRole> ysRoleList) {
        assertObjectNotNull("ysRoleList", ysRoleList);
        return delegateInsertList(ysRoleList);
    }

    /**
     * Batch-update the list. <br />
     * All columns are update target. {NOT modified only} <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchUpdate(List<YsRole> ysRoleList) {
        assertObjectNotNull("ysRoleList", ysRoleList);
        return delegateUpdateList(ysRoleList);
    }

    /**
     * Batch-update the list non-strictly. <br />
     * All columns are update target. {NOT modified only} <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchUpdateNonstrict(List<YsRole> ysRoleList) {
        assertObjectNotNull("ysRoleList", ysRoleList);
        return delegateUpdateListNonstrict(ysRoleList);
    }

    /**
     * Batch-delete the list. <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchDelete(List<YsRole> ysRoleList) {
        assertObjectNotNull("ysRoleList", ysRoleList);
        return delegateDeleteList(ysRoleList);
    }

    /**
     * Batch-delete the list non-strictly. <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchDeleteNonstrict(List<YsRole> ysRoleList) {
        assertObjectNotNull("ysRoleList", ysRoleList);
        return delegateDeleteListNonstrict(ysRoleList);
    }

    // ===================================================================================
    //                                                                        Query Update
    //                                                                        ============
    /**
     * Query-update the several entities non-strictly modified-only. {NonExclusiveControl}
     * <pre>
     * YsRole ysRole = new YsRole();
     * <span style="color: #3F7E5E">// you don't need to set PK value</span>
     * <span style="color: #3F7E5E">//ysRole.setPK...(value);</span>
     * ysRole.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysRole.setVersionNo(value);</span>
     * YsRoleCB cb = new YsRoleCB();
     * cb.query().setFoo...(value);
     * ysRoleBhv.<span style="color: #FD4747">queryUpdate</span>(ysRole, cb);
     * </pre>
     * @param ysRole The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of YsRole. (NotNull)
     * @return The updated count.
     */
    public int queryUpdate(YsRole ysRole, YsRoleCB cb) {
        return delegateQueryUpdate(ysRole, cb);
    }

    /**
     * Query-delete the several entities. {NonExclusiveControl}
     * <pre>
     * YsRoleCB cb = new YsRoleCB();
     * cb.query().setFoo...(value);
     * ysRoleBhv.<span style="color: #FD4747">queryDelete</span>(ysRole, cb);
     * </pre>
     * @param cb The condition-bean of YsRole. (NotNull)
     * @return The deleted count.
     */
    public int queryDelete(YsRoleCB cb) {
        return delegateQueryDelete(cb);
    }

    /**
     * Varying-update the entity modified-only. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * YsRole ysRole = new YsRole();
     * ysRole.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysRole.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * ysRole.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     UpdateOption&lt;YsRoleCB&gt; option = new UpdateOption&lt;YsRoleCB&gt;();
     *     option.self(new SpecifyQuery&lt;YsRoleCB&gt;() {
     *         public void specify(YsRoleCB cb) {
     *             cb.specify().<span style="color: #FD4747">columnXxxCount()</span>;
     *         }
     *     }).plus(1); <span style="color: #3F7E5E">// XXX_COUNT = XXX_COUNT + 1</span>
     *     ysRoleBhv.<span style="color: #FD4747">varyingUpdate</span>(ysRole, option);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * }
     * </pre>
     * @param ysRole The entity of update target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @param option The option of update for varying values. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void varyingUpdate(YsRole ysRole, final UpdateOption<YsRoleCB> option) {
        processVaryingUpdate(option);
        helpUpdateInternally(ysRole, new InternalUpdateCallback<YsRole>() {
            public int callbackDelegateUpdate(YsRole entity) { return delegateVaryingUpdate(entity, option); } });
    }

    /**
     * Varying-update the entity non-strictly modified-only. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsRole ysRole = new YsRole();
     * ysRole.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysRole.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysRole.setVersionNo(value);</span>
     * UpdateOption&lt;YsRoleCB&gt; option = new UpdateOption&lt;YsRoleCB&gt;();
     * option.self(new SpecifyQuery&lt;YsRoleCB&gt;() {
     *     public void specify(YsRoleCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooCount()</span>;
     *     }
     * }).plus(1); <span style="color: #3F7E5E">// FOO_COUNT = FOO_COUNT + 1</span>
     * ysRoleBhv.<span style="color: #FD4747">varyingUpdateNonstrict</span>(ysRole, option);
     * </pre>
     * @param ysRole The entity of update target. (NotNull) {PrimaryKeyRequired}
     * @param option The option of update for varying values. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void varyingUpdateNonstrict(YsRole ysRole, final UpdateOption<YsRoleCB> option) {
        processVaryingUpdate(option);
        helpUpdateNonstrictInternally(ysRole, new InternalUpdateNonstrictCallback<YsRole>() {
            public int callbackDelegateUpdateNonstrict(YsRole entity) { return delegateVaryingUpdateNonstrict(entity, option); } });
    }

    /**
     * Varying-query-update the several entities non-strictly modified-only. {NonExclusiveControl}
     * <pre>
     * YsRole ysRole = new YsRole();
     * <span style="color: #3F7E5E">// you don't need to set PK value</span>
     * <span style="color: #3F7E5E">//ysRole.setPK...(value);</span>
     * ysRole.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysRole.setVersionNo(value);</span>
     * YsRoleCB cb = new YsRoleCB();
     * cb.query().setFoo...(value);
     * UpdateOption&lt;YsRoleCB&gt; option = new UpdateOption&lt;YsRoleCB&gt;();
     * option.self(new SpecifyQuery&lt;YsRoleCB&gt;() {
     *     public void specify(YsRoleCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooCount()</span>;
     *     }
     * }).plus(1); <span style="color: #3F7E5E">// FOO_COUNT = FOO_COUNT + 1</span>
     * ysRoleBhv.<span style="color: #FD4747">varyingQueryUpdate</span>(ysRole, cb, option);
     * </pre>
     * @param ysRole The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of YsRole. (NotNull)
     * @param option The option of update for varying values. (NotNull)
     * @return The updated count.
     */
    public int varyingQueryUpdate(YsRole ysRole, YsRoleCB cb, final UpdateOption<YsRoleCB> option) {
        processVaryingUpdate(option);
        return delegateVaryingQueryUpdate(ysRole, cb, option);
    }

    protected void processVaryingUpdate(UpdateOption<YsRoleCB> option) {
        assertUpdateOptionNotNull(option);
        YsRoleCB cb = newMyConditionBean();
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
    protected int delegateSelectCount(YsRoleCB cb) { return invoke(createSelectCountCBCommand(cb)); }
    protected <ENTITY extends YsRole> void delegateSelectCursor(YsRoleCB cb, EntityRowHandler<ENTITY> entityRowHandler, Class<ENTITY> entityType)
    { invoke(createSelectCursorCBCommand(cb, entityRowHandler, entityType)); }
    protected <ENTITY extends YsRole> List<ENTITY> delegateSelectList(YsRoleCB cb, Class<ENTITY> entityType)
    { return invoke(createSelectListCBCommand(cb, entityType)); }

    // -----------------------------------------------------
    //                                                Update
    //                                                ------
    protected int delegateInsert(YsRole e)
    { if (!processBeforeInsert(e)) { return 1; } return invoke(createInsertEntityCommand(e)); }
    protected int delegateUpdate(YsRole e)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createUpdateEntityCommand(e)); }
    protected int delegateUpdateNonstrict(YsRole e)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createUpdateNonstrictEntityCommand(e)); }
    protected int delegateDelete(YsRole e)
    { if (!processBeforeDelete(e)) { return 1; } return invoke(createDeleteEntityCommand(e)); }
    protected int delegateDeleteNonstrict(YsRole e)
    { if (!processBeforeDelete(e)) { return 1; } return invoke(createDeleteNonstrictEntityCommand(e)); }

    protected int[] delegateInsertList(List<YsRole> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchInsertEntityCommand(helpFilterBeforeInsertInternally(ls))); }
    @SuppressWarnings("unchecked")
    protected int[] doCreateList(List<Entity> ls) { return delegateInsertList((List)ls); }
    protected int[] delegateUpdateList(List<YsRole> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchUpdateEntityCommand(helpFilterBeforeUpdateInternally(ls))); }
    @SuppressWarnings("unchecked")
    protected int[] doModifyList(List<Entity> ls) { return delegateUpdateList((List)ls); }
    protected int[] delegateUpdateListNonstrict(List<YsRole> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchUpdateNonstrictEntityCommand(helpFilterBeforeUpdateInternally(ls))); }
    protected int[] delegateDeleteList(List<YsRole> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchDeleteEntityCommand(helpFilterBeforeDeleteInternally(ls))); }
    @SuppressWarnings("unchecked")
    protected int[] doRemoveList(List<Entity> ls) { return delegateDeleteList((List)ls); }
    protected int[] delegateDeleteListNonstrict(List<YsRole> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchDeleteNonstrictEntityCommand(helpFilterBeforeDeleteInternally(ls))); }

    protected int delegateQueryUpdate(YsRole e, YsRoleCB cb)
    { if (!processBeforeQueryUpdate(e, cb)) { return 0; } return invoke(createQueryUpdateEntityCBCommand(e, cb));  }
    protected int delegateQueryDelete(YsRoleCB cb)
    { if (!processBeforeQueryDelete(cb)) { return 0; } return invoke(createQueryDeleteCBCommand(cb));  }

    protected int delegateVaryingUpdate(YsRole e, UpdateOption<YsRoleCB> op)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createVaryingUpdateEntityCommand(e, op)); }
    protected int delegateVaryingUpdateNonstrict(YsRole e, UpdateOption<YsRoleCB> op)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createVaryingUpdateNonstrictEntityCommand(e, op)); }
    protected int delegateVaryingQueryUpdate(YsRole e, YsRoleCB cb, UpdateOption<YsRoleCB> op)
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
    protected YsRole downcast(Entity entity) {
        return helpEntityDowncastInternally(entity, YsRole.class);
    }

    protected YsRoleCB downcast(ConditionBean cb) {
        return helpConditionBeanDowncastInternally(cb, YsRoleCB.class);
    }
}
