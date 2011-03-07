package org.seasar.ymir.scaffold.dbflute.bsbhv;

import java.util.List;

import org.seasar.dbflute.*;
import org.seasar.dbflute.bhv.*;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.EntityRowHandler;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.PagingResultBean;
import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.ymir.scaffold.dbflute.exentity.*;
import org.seasar.ymir.scaffold.dbflute.bsentity.dbmeta.*;
import org.seasar.ymir.scaffold.dbflute.cbean.*;

/**
 * The behavior of YS_ROLE_GROUP_USER as TABLE. <br />
 * <pre>
 * [primary-key]
 *     ID
 * 
 * [column]
 *     ID, ROLE_ID, GROUP_ID, USER_ID, CREATED_DATE, MODIFIED_DATE, VERSION_NO
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
 *     YS_GROUP, YS_ROLE, YS_USER
 * 
 * [referrer-table]
 *     
 * 
 * [foreign-property]
 *     ysGroup, ysRole, ysUser
 * 
 * [referrer-property]
 *     
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsYsRoleGroupUserBhv extends AbstractBehaviorWritable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /*df:BehaviorQueryPathBegin*/
    /*df:BehaviorQueryPathEnd*/

    // ===================================================================================
    //                                                                          Table name
    //                                                                          ==========
    /** @return The name on database of table. (NotNull) */
    public String getTableDbName() { return "YS_ROLE_GROUP_USER"; }

    // ===================================================================================
    //                                                                              DBMeta
    //                                                                              ======
    /** @return The instance of DBMeta. (NotNull) */
    public DBMeta getDBMeta() { return YsRoleGroupUserDbm.getInstance(); }

    /** @return The instance of DBMeta as my table type. (NotNull) */
    public YsRoleGroupUserDbm getMyDBMeta() { return YsRoleGroupUserDbm.getInstance(); }

    // ===================================================================================
    //                                                                        New Instance
    //                                                                        ============
    /** {@inheritDoc} */
    public Entity newEntity() { return newMyEntity(); }

    /** {@inheritDoc} */
    public ConditionBean newConditionBean() { return newMyConditionBean(); }

    /** @return The instance of new entity as my table type. (NotNull) */
    public YsRoleGroupUser newMyEntity() { return new YsRoleGroupUser(); }

    /** @return The instance of new condition-bean as my table type. (NotNull) */
    public YsRoleGroupUserCB newMyConditionBean() { return new YsRoleGroupUserCB(); }

    // ===================================================================================
    //                                                                        Count Select
    //                                                                        ============
    /**
     * Select the count by the condition-bean. {IgnorePagingCondition}
     * <pre>
     * YsRoleGroupUserCB cb = new YsRoleGroupUserCB();
     * cb.query().setFoo...(value);
     * int count = ysRoleGroupUserBhv.<span style="color: #FD4747">selectCount</span>(cb);
     * </pre>
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @return The selected count.
     */
    public int selectCount(YsRoleGroupUserCB cb) {
        return doSelectCount(cb);
    }

    protected int doSelectCount(YsRoleGroupUserCB cb) {
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
     * YsRoleGroupUserCB cb = new YsRoleGroupUserCB();
     * cb.query().setFoo...(value);
     * ysRoleGroupUserBhv.<span style="color: #FD4747">selectCursor</span>(cb, new EntityRowHandler&lt;YsRoleGroupUser&gt;() {
     *     public void handle(YsRoleGroupUser entity) {
     *         ... = entity.getFoo...();
     *     }
     * });
     * </pre>
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @param entityRowHandler The handler of entity row of YsRoleGroupUser. (NotNull)
     */
    public void selectCursor(YsRoleGroupUserCB cb, EntityRowHandler<YsRoleGroupUser> entityRowHandler) {
        doSelectCursor(cb, entityRowHandler, YsRoleGroupUser.class);
    }

    protected <ENTITY extends YsRoleGroupUser> void doSelectCursor(YsRoleGroupUserCB cb, EntityRowHandler<ENTITY> entityRowHandler, Class<ENTITY> entityType) {
        assertCBNotNull(cb); assertObjectNotNull("entityRowHandler<YsRoleGroupUser>", entityRowHandler); assertObjectNotNull("entityType", entityType);
        assertSpecifyDerivedReferrerEntityProperty(cb, entityType);
        delegateSelectCursor(cb, entityRowHandler, entityType);
    }

    // ===================================================================================
    //                                                                       Entity Select
    //                                                                       =============
    /**
     * Select the entity by the condition-bean.
     * <pre>
     * YsRoleGroupUserCB cb = new YsRoleGroupUserCB();
     * cb.query().setFoo...(value);
     * YsRoleGroupUser ysRoleGroupUser = ysRoleGroupUserBhv.<span style="color: #FD4747">selectEntity</span>(cb);
     * if (ysRoleGroupUser != null) {
     *     ... = ysRoleGroupUser.get...();
     * } else {
     *     ...
     * }
     * </pre>
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @return The selected entity. (Nullable: If the condition has no data, it returns null)
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public YsRoleGroupUser selectEntity(YsRoleGroupUserCB cb) {
        return doSelectEntity(cb, YsRoleGroupUser.class);
    }

    protected <ENTITY extends YsRoleGroupUser> ENTITY doSelectEntity(final YsRoleGroupUserCB cb, final Class<ENTITY> entityType) {
        return helpSelectEntityInternally(cb, new InternalSelectEntityCallback<ENTITY, YsRoleGroupUserCB>() {
            public List<ENTITY> callbackSelectList(YsRoleGroupUserCB cb) { return doSelectList(cb, entityType); } });
    }

    @Override
    protected Entity doReadEntity(ConditionBean cb) {
        return selectEntity(downcast(cb));
    }

    /**
     * Select the entity by the condition-bean with deleted check.
     * <pre>
     * YsRoleGroupUserCB cb = new YsRoleGroupUserCB();
     * cb.query().setFoo...(value);
     * YsRoleGroupUser ysRoleGroupUser = ysRoleGroupUserBhv.<span style="color: #FD4747">selectEntityWithDeletedCheck</span>(cb);
     * ... = ysRoleGroupUser.get...(); <span style="color: #3F7E5E">// the entity always be not null</span>
     * </pre>
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public YsRoleGroupUser selectEntityWithDeletedCheck(YsRoleGroupUserCB cb) {
        return doSelectEntityWithDeletedCheck(cb, YsRoleGroupUser.class);
    }

    protected <ENTITY extends YsRoleGroupUser> ENTITY doSelectEntityWithDeletedCheck(final YsRoleGroupUserCB cb, final Class<ENTITY> entityType) {
        return helpSelectEntityWithDeletedCheckInternally(cb, new InternalSelectEntityWithDeletedCheckCallback<ENTITY, YsRoleGroupUserCB>() {
            public List<ENTITY> callbackSelectList(YsRoleGroupUserCB cb) { return doSelectList(cb, entityType); } });
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
    public YsRoleGroupUser selectByPKValue(Long id) {
        return doSelectByPKValue(id, YsRoleGroupUser.class);
    }

    protected <ENTITY extends YsRoleGroupUser> ENTITY doSelectByPKValue(Long id, Class<ENTITY> entityType) {
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
    public YsRoleGroupUser selectByPKValueWithDeletedCheck(Long id) {
        return doSelectByPKValueWithDeletedCheck(id, YsRoleGroupUser.class);
    }

    protected <ENTITY extends YsRoleGroupUser> ENTITY doSelectByPKValueWithDeletedCheck(Long id, Class<ENTITY> entityType) {
        return doSelectEntityWithDeletedCheck(buildPKCB(id), entityType);
    }

    private YsRoleGroupUserCB buildPKCB(Long id) {
        assertObjectNotNull("id", id);
        YsRoleGroupUserCB cb = newMyConditionBean();
        cb.query().setId_Equal(id);
        return cb;
    }

    // ===================================================================================
    //                                                                         List Select
    //                                                                         ===========
    /**
     * Select the list as result bean.
     * <pre>
     * YsRoleGroupUserCB cb = new YsRoleGroupUserCB();
     * cb.query().setFoo...(value);
     * cb.query().addOrderBy_Bar...();
     * ListResultBean&lt;YsRoleGroupUser&gt; ysRoleGroupUserList = ysRoleGroupUserBhv.<span style="color: #FD4747">selectList</span>(cb);
     * for (YsRoleGroupUser ysRoleGroupUser : ysRoleGroupUserList) {
     *     ... = ysRoleGroupUser.get...();
     * }
     * </pre>
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @return The result bean of selected list. (NotNull)
     * @exception org.seasar.dbflute.exception.DangerousResultSizeException When the result size is over the specified safety size.
     */
    public ListResultBean<YsRoleGroupUser> selectList(YsRoleGroupUserCB cb) {
        return doSelectList(cb, YsRoleGroupUser.class);
    }

    protected <ENTITY extends YsRoleGroupUser> ListResultBean<ENTITY> doSelectList(YsRoleGroupUserCB cb, Class<ENTITY> entityType) {
        assertCBNotNull(cb); assertObjectNotNull("entityType", entityType);
        assertSpecifyDerivedReferrerEntityProperty(cb, entityType);
        return helpSelectListInternally(cb, entityType, new InternalSelectListCallback<ENTITY, YsRoleGroupUserCB>() {
            public List<ENTITY> callbackSelectList(YsRoleGroupUserCB cb, Class<ENTITY> entityType) { return delegateSelectList(cb, entityType); } });
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
     * YsRoleGroupUserCB cb = new YsRoleGroupUserCB();
     * cb.query().setFoo...(value);
     * cb.query().addOrderBy_Bar...();
     * cb.<span style="color: #FD4747">paging</span>(20, 3); <span style="color: #3F7E5E">// 20 records per a page and current page number is 3</span>
     * PagingResultBean&lt;YsRoleGroupUser&gt; page = ysRoleGroupUserBhv.<span style="color: #FD4747">selectPage</span>(cb);
     * int allRecordCount = page.getAllRecordCount();
     * int allPageCount = page.getAllPageCount();
     * boolean isExistPrePage = page.isExistPrePage();
     * boolean isExistNextPage = page.isExistNextPage();
     * ...
     * for (YsRoleGroupUser ysRoleGroupUser : page) {
     *     ... = ysRoleGroupUser.get...();
     * }
     * </pre>
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @return The result bean of selected page. (NotNull)
     * @exception org.seasar.dbflute.exception.DangerousResultSizeException When the result size is over the specified safety size.
     */
    public PagingResultBean<YsRoleGroupUser> selectPage(YsRoleGroupUserCB cb) {
        return doSelectPage(cb, YsRoleGroupUser.class);
    }

    protected <ENTITY extends YsRoleGroupUser> PagingResultBean<ENTITY> doSelectPage(YsRoleGroupUserCB cb, Class<ENTITY> entityType) {
        assertCBNotNull(cb); assertObjectNotNull("entityType", entityType);
        return helpSelectPageInternally(cb, entityType, new InternalSelectPageCallback<ENTITY, YsRoleGroupUserCB>() {
            public int callbackSelectCount(YsRoleGroupUserCB cb) { return doSelectCount(cb); }
            public List<ENTITY> callbackSelectList(YsRoleGroupUserCB cb, Class<ENTITY> entityType) { return doSelectList(cb, entityType); }
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
     * ysRoleGroupUserBhv.<span style="color: #FD4747">scalarSelect</span>(Date.class).max(new ScalarQuery() {
     *     public void query(YsRoleGroupUserCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooDatetime()</span>; <span style="color: #3F7E5E">// required for a function</span>
     *         cb.query().setBarName_PrefixSearch("S");
     *     }
     * });
     * </pre>
     * @param <RESULT> The type of result.
     * @param resultType The type of result. (NotNull)
     * @return The scalar value derived by a function. (Nullable)
     */
    public <RESULT> SLFunction<YsRoleGroupUserCB, RESULT> scalarSelect(Class<RESULT> resultType) {
        return doScalarSelect(resultType, newMyConditionBean());
    }

    protected <RESULT, CB extends YsRoleGroupUserCB> SLFunction<CB, RESULT> doScalarSelect(Class<RESULT> resultType, CB cb) {
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
    //                                                                    Pull out Foreign
    //                                                                    ================
    /**
     * Pull out the list of foreign table 'YsGroup'.
     * @param ysRoleGroupUserList The list of ysRoleGroupUser. (NotNull)
     * @return The list of foreign table. (NotNull)
     */
    public List<YsGroup> pulloutYsGroup(List<YsRoleGroupUser> ysRoleGroupUserList) {
        return helpPulloutInternally(ysRoleGroupUserList, new InternalPulloutCallback<YsRoleGroupUser, YsGroup>() {
            public YsGroup getFr(YsRoleGroupUser e) { return e.getYsGroup(); }
            public boolean hasRf() { return true; }
            public void setRfLs(YsGroup e, List<YsRoleGroupUser> ls)
            { e.setYsRoleGroupUserList(ls); }
        });
    }
    /**
     * Pull out the list of foreign table 'YsRole'.
     * @param ysRoleGroupUserList The list of ysRoleGroupUser. (NotNull)
     * @return The list of foreign table. (NotNull)
     */
    public List<YsRole> pulloutYsRole(List<YsRoleGroupUser> ysRoleGroupUserList) {
        return helpPulloutInternally(ysRoleGroupUserList, new InternalPulloutCallback<YsRoleGroupUser, YsRole>() {
            public YsRole getFr(YsRoleGroupUser e) { return e.getYsRole(); }
            public boolean hasRf() { return true; }
            public void setRfLs(YsRole e, List<YsRoleGroupUser> ls)
            { e.setYsRoleGroupUserList(ls); }
        });
    }
    /**
     * Pull out the list of foreign table 'YsUser'.
     * @param ysRoleGroupUserList The list of ysRoleGroupUser. (NotNull)
     * @return The list of foreign table. (NotNull)
     */
    public List<YsUser> pulloutYsUser(List<YsRoleGroupUser> ysRoleGroupUserList) {
        return helpPulloutInternally(ysRoleGroupUserList, new InternalPulloutCallback<YsRoleGroupUser, YsUser>() {
            public YsUser getFr(YsRoleGroupUser e) { return e.getYsUser(); }
            public boolean hasRf() { return true; }
            public void setRfLs(YsUser e, List<YsRoleGroupUser> ls)
            { e.setYsRoleGroupUserList(ls); }
        });
    }

    // ===================================================================================
    //                                                                       Entity Update
    //                                                                       =============
    /**
     * Insert the entity.
     * <pre>
     * YsRoleGroupUser ysRoleGroupUser = new YsRoleGroupUser();
     * <span style="color: #3F7E5E">// if auto-increment, you don't need to set the PK value</span>
     * ysRoleGroupUser.setFoo...(value);
     * ysRoleGroupUser.setBar...(value);
     * ysRoleGroupUserBhv.<span style="color: #FD4747">insert</span>(ysRoleGroupUser);
     * ... = ysRoleGroupUser.getPK...(); <span style="color: #3F7E5E">// if auto-increment, you can get the value after</span>
     * </pre>
     * @param ysRoleGroupUser The entity of insert target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insert(YsRoleGroupUser ysRoleGroupUser) {
        assertEntityNotNull(ysRoleGroupUser);
        delegateInsert(ysRoleGroupUser);
    }

    @Override
    protected void doCreate(Entity entity) {
        insert(downcast(entity));
    }

    /**
     * Update the entity modified-only. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * YsRoleGroupUser ysRoleGroupUser = new YsRoleGroupUser();
     * ysRoleGroupUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysRoleGroupUser.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * ysRoleGroupUser.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     ysRoleGroupUserBhv.<span style="color: #FD4747">update</span>(ysRoleGroupUser);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * } 
     * </pre>
     * @param ysRoleGroupUser The entity of update target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void update(final YsRoleGroupUser ysRoleGroupUser) {
        helpUpdateInternally(ysRoleGroupUser, new InternalUpdateCallback<YsRoleGroupUser>() {
            public int callbackDelegateUpdate(YsRoleGroupUser entity) { return delegateUpdate(entity); } });
    }

    @Override
    protected void doModify(Entity entity) {
        update(downcast(entity));
    }

    /**
     * Update the entity non-strictly modified-only. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsRoleGroupUser ysRoleGroupUser = new YsRoleGroupUser();
     * ysRoleGroupUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysRoleGroupUser.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysRoleGroupUser.setVersionNo(value);</span>
     * ysRoleGroupUserBhv.<span style="color: #FD4747">updateNonstrict</span>(ysRoleGroupUser);
     * </pre>
     * @param ysRoleGroupUser The entity of update target. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void updateNonstrict(final YsRoleGroupUser ysRoleGroupUser) {
        helpUpdateNonstrictInternally(ysRoleGroupUser, new InternalUpdateNonstrictCallback<YsRoleGroupUser>() {
            public int callbackDelegateUpdateNonstrict(YsRoleGroupUser entity) { return delegateUpdateNonstrict(entity); } });
    }

    @Override
    protected void doModifyNonstrict(Entity entity) {
        updateNonstrict(downcast(entity));
    }

    /**
     * Insert or update the entity modified-only. {ExclusiveControl(when update)}
     * @param ysRoleGroupUser The entity of insert or update target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insertOrUpdate(final YsRoleGroupUser ysRoleGroupUser) {
        helpInsertOrUpdateInternally(ysRoleGroupUser, new InternalInsertOrUpdateCallback<YsRoleGroupUser, YsRoleGroupUserCB>() {
            public void callbackInsert(YsRoleGroupUser entity) { insert(entity); }
            public void callbackUpdate(YsRoleGroupUser entity) { update(entity); }
            public YsRoleGroupUserCB callbackNewMyConditionBean() { return newMyConditionBean(); }
            public int callbackSelectCount(YsRoleGroupUserCB cb) { return selectCount(cb); }
        });
    }

    @Override
    protected void doCreateOrUpdate(Entity entity) {
        insertOrUpdate(downcast(entity));
    }

    /**
     * Insert or update the entity non-strictly modified-only. {NonExclusiveControl(when update)}
     * @param ysRoleGroupUser The entity of insert or update target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insertOrUpdateNonstrict(YsRoleGroupUser ysRoleGroupUser) {
        helpInsertOrUpdateInternally(ysRoleGroupUser, new InternalInsertOrUpdateNonstrictCallback<YsRoleGroupUser>() {
            public void callbackInsert(YsRoleGroupUser entity) { insert(entity); }
            public void callbackUpdateNonstrict(YsRoleGroupUser entity) { updateNonstrict(entity); }
        });
    }

    @Override
    protected void doCreateOrUpdateNonstrict(Entity entity) {
        insertOrUpdateNonstrict(downcast(entity));
    }

    /**
     * Delete the entity. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * YsRoleGroupUser ysRoleGroupUser = new YsRoleGroupUser();
     * ysRoleGroupUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * ysRoleGroupUser.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     ysRoleGroupUserBhv.<span style="color: #FD4747">delete</span>(ysRoleGroupUser);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * } 
     * </pre>
     * @param ysRoleGroupUser The entity of delete target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void delete(YsRoleGroupUser ysRoleGroupUser) {
        helpDeleteInternally(ysRoleGroupUser, new InternalDeleteCallback<YsRoleGroupUser>() {
            public int callbackDelegateDelete(YsRoleGroupUser entity) { return delegateDelete(entity); } });
    }

    @Override
    protected void doRemove(Entity entity) {
        delete(downcast(entity));
    }

    /**
     * Delete the entity non-strictly. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsRoleGroupUser ysRoleGroupUser = new YsRoleGroupUser();
     * ysRoleGroupUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysRoleGroupUser.setVersionNo(value);</span>
     * ysRoleGroupUserBhv.<span style="color: #FD4747">deleteNonstrict</span>(ysRoleGroupUser);
     * </pre>
     * @param ysRoleGroupUser Entity. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void deleteNonstrict(YsRoleGroupUser ysRoleGroupUser) {
        helpDeleteNonstrictInternally(ysRoleGroupUser, new InternalDeleteNonstrictCallback<YsRoleGroupUser>() {
            public int callbackDelegateDeleteNonstrict(YsRoleGroupUser entity) { return delegateDeleteNonstrict(entity); } });
    }

    /**
     * Delete the entity non-strictly ignoring deleted. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsRoleGroupUser ysRoleGroupUser = new YsRoleGroupUser();
     * ysRoleGroupUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysRoleGroupUser.setVersionNo(value);</span>
     * ysRoleGroupUserBhv.<span style="color: #FD4747">deleteNonstrictIgnoreDeleted</span>(ysRoleGroupUser);
     * <span style="color: #3F7E5E">// if the target entity doesn't exist, no exception</span>
     * </pre>
     * @param ysRoleGroupUser Entity. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void deleteNonstrictIgnoreDeleted(YsRoleGroupUser ysRoleGroupUser) {
        helpDeleteNonstrictIgnoreDeletedInternally(ysRoleGroupUser, new InternalDeleteNonstrictIgnoreDeletedCallback<YsRoleGroupUser>() {
            public int callbackDelegateDeleteNonstrict(YsRoleGroupUser entity) { return delegateDeleteNonstrict(entity); } });
    }

    // ===================================================================================
    //                                                                        Batch Update
    //                                                                        ============
    /**
     * Batch-insert the list. This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleGroupUserList The list of the entity. (NotNull)
     * @return The array of inserted count.
     */
    public int[] batchInsert(List<YsRoleGroupUser> ysRoleGroupUserList) {
        assertObjectNotNull("ysRoleGroupUserList", ysRoleGroupUserList);
        return delegateInsertList(ysRoleGroupUserList);
    }

    /**
     * Batch-update the list. <br />
     * All columns are update target. {NOT modified only} <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleGroupUserList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchUpdate(List<YsRoleGroupUser> ysRoleGroupUserList) {
        assertObjectNotNull("ysRoleGroupUserList", ysRoleGroupUserList);
        return delegateUpdateList(ysRoleGroupUserList);
    }

    /**
     * Batch-update the list non-strictly. <br />
     * All columns are update target. {NOT modified only} <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleGroupUserList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchUpdateNonstrict(List<YsRoleGroupUser> ysRoleGroupUserList) {
        assertObjectNotNull("ysRoleGroupUserList", ysRoleGroupUserList);
        return delegateUpdateListNonstrict(ysRoleGroupUserList);
    }

    /**
     * Batch-delete the list. <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleGroupUserList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchDelete(List<YsRoleGroupUser> ysRoleGroupUserList) {
        assertObjectNotNull("ysRoleGroupUserList", ysRoleGroupUserList);
        return delegateDeleteList(ysRoleGroupUserList);
    }

    /**
     * Batch-delete the list non-strictly. <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleGroupUserList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchDeleteNonstrict(List<YsRoleGroupUser> ysRoleGroupUserList) {
        assertObjectNotNull("ysRoleGroupUserList", ysRoleGroupUserList);
        return delegateDeleteListNonstrict(ysRoleGroupUserList);
    }

    // ===================================================================================
    //                                                                        Query Update
    //                                                                        ============
    /**
     * Query-update the several entities non-strictly modified-only. {NonExclusiveControl}
     * <pre>
     * YsRoleGroupUser ysRoleGroupUser = new YsRoleGroupUser();
     * <span style="color: #3F7E5E">// you don't need to set PK value</span>
     * <span style="color: #3F7E5E">//ysRoleGroupUser.setPK...(value);</span>
     * ysRoleGroupUser.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysRoleGroupUser.setVersionNo(value);</span>
     * YsRoleGroupUserCB cb = new YsRoleGroupUserCB();
     * cb.query().setFoo...(value);
     * ysRoleGroupUserBhv.<span style="color: #FD4747">queryUpdate</span>(ysRoleGroupUser, cb);
     * </pre>
     * @param ysRoleGroupUser The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @return The updated count.
     */
    public int queryUpdate(YsRoleGroupUser ysRoleGroupUser, YsRoleGroupUserCB cb) {
        return delegateQueryUpdate(ysRoleGroupUser, cb);
    }

    /**
     * Query-delete the several entities. {NonExclusiveControl}
     * <pre>
     * YsRoleGroupUserCB cb = new YsRoleGroupUserCB();
     * cb.query().setFoo...(value);
     * ysRoleGroupUserBhv.<span style="color: #FD4747">queryDelete</span>(ysRoleGroupUser, cb);
     * </pre>
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @return The deleted count.
     */
    public int queryDelete(YsRoleGroupUserCB cb) {
        return delegateQueryDelete(cb);
    }

    /**
     * Varying-update the entity modified-only. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * YsRoleGroupUser ysRoleGroupUser = new YsRoleGroupUser();
     * ysRoleGroupUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysRoleGroupUser.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * ysRoleGroupUser.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     UpdateOption&lt;YsRoleGroupUserCB&gt; option = new UpdateOption&lt;YsRoleGroupUserCB&gt;();
     *     option.self(new SpecifyQuery&lt;YsRoleGroupUserCB&gt;() {
     *         public void specify(YsRoleGroupUserCB cb) {
     *             cb.specify().<span style="color: #FD4747">columnXxxCount()</span>;
     *         }
     *     }).plus(1); <span style="color: #3F7E5E">// XXX_COUNT = XXX_COUNT + 1</span>
     *     ysRoleGroupUserBhv.<span style="color: #FD4747">varyingUpdate</span>(ysRoleGroupUser, option);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * }
     * </pre>
     * @param ysRoleGroupUser The entity of update target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @param option The option of update for varying values. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void varyingUpdate(YsRoleGroupUser ysRoleGroupUser, final UpdateOption<YsRoleGroupUserCB> option) {
        processVaryingUpdate(option);
        helpUpdateInternally(ysRoleGroupUser, new InternalUpdateCallback<YsRoleGroupUser>() {
            public int callbackDelegateUpdate(YsRoleGroupUser entity) { return delegateVaryingUpdate(entity, option); } });
    }

    /**
     * Varying-update the entity non-strictly modified-only. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsRoleGroupUser ysRoleGroupUser = new YsRoleGroupUser();
     * ysRoleGroupUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysRoleGroupUser.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysRoleGroupUser.setVersionNo(value);</span>
     * UpdateOption&lt;YsRoleGroupUserCB&gt; option = new UpdateOption&lt;YsRoleGroupUserCB&gt;();
     * option.self(new SpecifyQuery&lt;YsRoleGroupUserCB&gt;() {
     *     public void specify(YsRoleGroupUserCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooCount()</span>;
     *     }
     * }).plus(1); <span style="color: #3F7E5E">// FOO_COUNT = FOO_COUNT + 1</span>
     * ysRoleGroupUserBhv.<span style="color: #FD4747">varyingUpdateNonstrict</span>(ysRoleGroupUser, option);
     * </pre>
     * @param ysRoleGroupUser The entity of update target. (NotNull) {PrimaryKeyRequired}
     * @param option The option of update for varying values. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void varyingUpdateNonstrict(YsRoleGroupUser ysRoleGroupUser, final UpdateOption<YsRoleGroupUserCB> option) {
        processVaryingUpdate(option);
        helpUpdateNonstrictInternally(ysRoleGroupUser, new InternalUpdateNonstrictCallback<YsRoleGroupUser>() {
            public int callbackDelegateUpdateNonstrict(YsRoleGroupUser entity) { return delegateVaryingUpdateNonstrict(entity, option); } });
    }

    /**
     * Varying-query-update the several entities non-strictly modified-only. {NonExclusiveControl}
     * <pre>
     * YsRoleGroupUser ysRoleGroupUser = new YsRoleGroupUser();
     * <span style="color: #3F7E5E">// you don't need to set PK value</span>
     * <span style="color: #3F7E5E">//ysRoleGroupUser.setPK...(value);</span>
     * ysRoleGroupUser.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysRoleGroupUser.setVersionNo(value);</span>
     * YsRoleGroupUserCB cb = new YsRoleGroupUserCB();
     * cb.query().setFoo...(value);
     * UpdateOption&lt;YsRoleGroupUserCB&gt; option = new UpdateOption&lt;YsRoleGroupUserCB&gt;();
     * option.self(new SpecifyQuery&lt;YsRoleGroupUserCB&gt;() {
     *     public void specify(YsRoleGroupUserCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooCount()</span>;
     *     }
     * }).plus(1); <span style="color: #3F7E5E">// FOO_COUNT = FOO_COUNT + 1</span>
     * ysRoleGroupUserBhv.<span style="color: #FD4747">varyingQueryUpdate</span>(ysRoleGroupUser, cb, option);
     * </pre>
     * @param ysRoleGroupUser The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @param option The option of update for varying values. (NotNull)
     * @return The updated count.
     */
    public int varyingQueryUpdate(YsRoleGroupUser ysRoleGroupUser, YsRoleGroupUserCB cb, final UpdateOption<YsRoleGroupUserCB> option) {
        processVaryingUpdate(option);
        return delegateVaryingQueryUpdate(ysRoleGroupUser, cb, option);
    }

    protected void processVaryingUpdate(UpdateOption<YsRoleGroupUserCB> option) {
        assertUpdateOptionNotNull(option);
        YsRoleGroupUserCB cb = newMyConditionBean();
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
    protected int delegateSelectCount(YsRoleGroupUserCB cb) { return invoke(createSelectCountCBCommand(cb)); }
    protected <ENTITY extends YsRoleGroupUser> void delegateSelectCursor(YsRoleGroupUserCB cb, EntityRowHandler<ENTITY> entityRowHandler, Class<ENTITY> entityType)
    { invoke(createSelectCursorCBCommand(cb, entityRowHandler, entityType)); }
    protected <ENTITY extends YsRoleGroupUser> List<ENTITY> delegateSelectList(YsRoleGroupUserCB cb, Class<ENTITY> entityType)
    { return invoke(createSelectListCBCommand(cb, entityType)); }

    // -----------------------------------------------------
    //                                                Update
    //                                                ------
    protected int delegateInsert(YsRoleGroupUser e)
    { if (!processBeforeInsert(e)) { return 1; } return invoke(createInsertEntityCommand(e)); }
    protected int delegateUpdate(YsRoleGroupUser e)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createUpdateEntityCommand(e)); }
    protected int delegateUpdateNonstrict(YsRoleGroupUser e)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createUpdateNonstrictEntityCommand(e)); }
    protected int delegateDelete(YsRoleGroupUser e)
    { if (!processBeforeDelete(e)) { return 1; } return invoke(createDeleteEntityCommand(e)); }
    protected int delegateDeleteNonstrict(YsRoleGroupUser e)
    { if (!processBeforeDelete(e)) { return 1; } return invoke(createDeleteNonstrictEntityCommand(e)); }

    protected int[] delegateInsertList(List<YsRoleGroupUser> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchInsertEntityCommand(helpFilterBeforeInsertInternally(ls))); }
    @SuppressWarnings("unchecked")
    protected int[] doCreateList(List<Entity> ls) { return delegateInsertList((List)ls); }
    protected int[] delegateUpdateList(List<YsRoleGroupUser> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchUpdateEntityCommand(helpFilterBeforeUpdateInternally(ls))); }
    @SuppressWarnings("unchecked")
    protected int[] doModifyList(List<Entity> ls) { return delegateUpdateList((List)ls); }
    protected int[] delegateUpdateListNonstrict(List<YsRoleGroupUser> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchUpdateNonstrictEntityCommand(helpFilterBeforeUpdateInternally(ls))); }
    protected int[] delegateDeleteList(List<YsRoleGroupUser> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchDeleteEntityCommand(helpFilterBeforeDeleteInternally(ls))); }
    @SuppressWarnings("unchecked")
    protected int[] doRemoveList(List<Entity> ls) { return delegateDeleteList((List)ls); }
    protected int[] delegateDeleteListNonstrict(List<YsRoleGroupUser> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchDeleteNonstrictEntityCommand(helpFilterBeforeDeleteInternally(ls))); }

    protected int delegateQueryUpdate(YsRoleGroupUser e, YsRoleGroupUserCB cb)
    { if (!processBeforeQueryUpdate(e, cb)) { return 0; } return invoke(createQueryUpdateEntityCBCommand(e, cb));  }
    protected int delegateQueryDelete(YsRoleGroupUserCB cb)
    { if (!processBeforeQueryDelete(cb)) { return 0; } return invoke(createQueryDeleteCBCommand(cb));  }

    protected int delegateVaryingUpdate(YsRoleGroupUser e, UpdateOption<YsRoleGroupUserCB> op)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createVaryingUpdateEntityCommand(e, op)); }
    protected int delegateVaryingUpdateNonstrict(YsRoleGroupUser e, UpdateOption<YsRoleGroupUserCB> op)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createVaryingUpdateNonstrictEntityCommand(e, op)); }
    protected int delegateVaryingQueryUpdate(YsRoleGroupUser e, YsRoleGroupUserCB cb, UpdateOption<YsRoleGroupUserCB> op)
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
    protected YsRoleGroupUser downcast(Entity entity) {
        return helpEntityDowncastInternally(entity, YsRoleGroupUser.class);
    }

    protected YsRoleGroupUserCB downcast(ConditionBean cb) {
        return helpConditionBeanDowncastInternally(cb, YsRoleGroupUserCB.class);
    }
}
