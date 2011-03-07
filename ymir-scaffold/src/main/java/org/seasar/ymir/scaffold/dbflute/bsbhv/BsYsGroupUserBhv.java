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
 * The behavior of YS_GROUP_USER as TABLE. <br />
 * <pre>
 * [primary-key]
 *     ID
 * 
 * [column]
 *     ID, GROUP_ID, USER_ID, CREATED_DATE, MODIFIED_DATE, VERSION_NO
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
 *     YS_GROUP, YS_USER
 * 
 * [referrer-table]
 *     
 * 
 * [foreign-property]
 *     ysGroup, ysUser
 * 
 * [referrer-property]
 *     
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsYsGroupUserBhv extends AbstractBehaviorWritable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /*df:BehaviorQueryPathBegin*/
    /*df:BehaviorQueryPathEnd*/

    // ===================================================================================
    //                                                                          Table name
    //                                                                          ==========
    /** @return The name on database of table. (NotNull) */
    public String getTableDbName() { return "YS_GROUP_USER"; }

    // ===================================================================================
    //                                                                              DBMeta
    //                                                                              ======
    /** @return The instance of DBMeta. (NotNull) */
    public DBMeta getDBMeta() { return YsGroupUserDbm.getInstance(); }

    /** @return The instance of DBMeta as my table type. (NotNull) */
    public YsGroupUserDbm getMyDBMeta() { return YsGroupUserDbm.getInstance(); }

    // ===================================================================================
    //                                                                        New Instance
    //                                                                        ============
    /** {@inheritDoc} */
    public Entity newEntity() { return newMyEntity(); }

    /** {@inheritDoc} */
    public ConditionBean newConditionBean() { return newMyConditionBean(); }

    /** @return The instance of new entity as my table type. (NotNull) */
    public YsGroupUser newMyEntity() { return new YsGroupUser(); }

    /** @return The instance of new condition-bean as my table type. (NotNull) */
    public YsGroupUserCB newMyConditionBean() { return new YsGroupUserCB(); }

    // ===================================================================================
    //                                                                        Count Select
    //                                                                        ============
    /**
     * Select the count by the condition-bean. {IgnorePagingCondition}
     * <pre>
     * YsGroupUserCB cb = new YsGroupUserCB();
     * cb.query().setFoo...(value);
     * int count = ysGroupUserBhv.<span style="color: #FD4747">selectCount</span>(cb);
     * </pre>
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @return The selected count.
     */
    public int selectCount(YsGroupUserCB cb) {
        return doSelectCount(cb);
    }

    protected int doSelectCount(YsGroupUserCB cb) {
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
     * YsGroupUserCB cb = new YsGroupUserCB();
     * cb.query().setFoo...(value);
     * ysGroupUserBhv.<span style="color: #FD4747">selectCursor</span>(cb, new EntityRowHandler&lt;YsGroupUser&gt;() {
     *     public void handle(YsGroupUser entity) {
     *         ... = entity.getFoo...();
     *     }
     * });
     * </pre>
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @param entityRowHandler The handler of entity row of YsGroupUser. (NotNull)
     */
    public void selectCursor(YsGroupUserCB cb, EntityRowHandler<YsGroupUser> entityRowHandler) {
        doSelectCursor(cb, entityRowHandler, YsGroupUser.class);
    }

    protected <ENTITY extends YsGroupUser> void doSelectCursor(YsGroupUserCB cb, EntityRowHandler<ENTITY> entityRowHandler, Class<ENTITY> entityType) {
        assertCBNotNull(cb); assertObjectNotNull("entityRowHandler<YsGroupUser>", entityRowHandler); assertObjectNotNull("entityType", entityType);
        assertSpecifyDerivedReferrerEntityProperty(cb, entityType);
        delegateSelectCursor(cb, entityRowHandler, entityType);
    }

    // ===================================================================================
    //                                                                       Entity Select
    //                                                                       =============
    /**
     * Select the entity by the condition-bean.
     * <pre>
     * YsGroupUserCB cb = new YsGroupUserCB();
     * cb.query().setFoo...(value);
     * YsGroupUser ysGroupUser = ysGroupUserBhv.<span style="color: #FD4747">selectEntity</span>(cb);
     * if (ysGroupUser != null) {
     *     ... = ysGroupUser.get...();
     * } else {
     *     ...
     * }
     * </pre>
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @return The selected entity. (Nullable: If the condition has no data, it returns null)
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public YsGroupUser selectEntity(YsGroupUserCB cb) {
        return doSelectEntity(cb, YsGroupUser.class);
    }

    protected <ENTITY extends YsGroupUser> ENTITY doSelectEntity(final YsGroupUserCB cb, final Class<ENTITY> entityType) {
        return helpSelectEntityInternally(cb, new InternalSelectEntityCallback<ENTITY, YsGroupUserCB>() {
            public List<ENTITY> callbackSelectList(YsGroupUserCB cb) { return doSelectList(cb, entityType); } });
    }

    @Override
    protected Entity doReadEntity(ConditionBean cb) {
        return selectEntity(downcast(cb));
    }

    /**
     * Select the entity by the condition-bean with deleted check.
     * <pre>
     * YsGroupUserCB cb = new YsGroupUserCB();
     * cb.query().setFoo...(value);
     * YsGroupUser ysGroupUser = ysGroupUserBhv.<span style="color: #FD4747">selectEntityWithDeletedCheck</span>(cb);
     * ... = ysGroupUser.get...(); <span style="color: #3F7E5E">// the entity always be not null</span>
     * </pre>
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public YsGroupUser selectEntityWithDeletedCheck(YsGroupUserCB cb) {
        return doSelectEntityWithDeletedCheck(cb, YsGroupUser.class);
    }

    protected <ENTITY extends YsGroupUser> ENTITY doSelectEntityWithDeletedCheck(final YsGroupUserCB cb, final Class<ENTITY> entityType) {
        return helpSelectEntityWithDeletedCheckInternally(cb, new InternalSelectEntityWithDeletedCheckCallback<ENTITY, YsGroupUserCB>() {
            public List<ENTITY> callbackSelectList(YsGroupUserCB cb) { return doSelectList(cb, entityType); } });
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
    public YsGroupUser selectByPKValue(Long id) {
        return doSelectByPKValue(id, YsGroupUser.class);
    }

    protected <ENTITY extends YsGroupUser> ENTITY doSelectByPKValue(Long id, Class<ENTITY> entityType) {
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
    public YsGroupUser selectByPKValueWithDeletedCheck(Long id) {
        return doSelectByPKValueWithDeletedCheck(id, YsGroupUser.class);
    }

    protected <ENTITY extends YsGroupUser> ENTITY doSelectByPKValueWithDeletedCheck(Long id, Class<ENTITY> entityType) {
        return doSelectEntityWithDeletedCheck(buildPKCB(id), entityType);
    }

    private YsGroupUserCB buildPKCB(Long id) {
        assertObjectNotNull("id", id);
        YsGroupUserCB cb = newMyConditionBean();
        cb.query().setId_Equal(id);
        return cb;
    }

    // ===================================================================================
    //                                                                         List Select
    //                                                                         ===========
    /**
     * Select the list as result bean.
     * <pre>
     * YsGroupUserCB cb = new YsGroupUserCB();
     * cb.query().setFoo...(value);
     * cb.query().addOrderBy_Bar...();
     * ListResultBean&lt;YsGroupUser&gt; ysGroupUserList = ysGroupUserBhv.<span style="color: #FD4747">selectList</span>(cb);
     * for (YsGroupUser ysGroupUser : ysGroupUserList) {
     *     ... = ysGroupUser.get...();
     * }
     * </pre>
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @return The result bean of selected list. (NotNull)
     * @exception org.seasar.dbflute.exception.DangerousResultSizeException When the result size is over the specified safety size.
     */
    public ListResultBean<YsGroupUser> selectList(YsGroupUserCB cb) {
        return doSelectList(cb, YsGroupUser.class);
    }

    protected <ENTITY extends YsGroupUser> ListResultBean<ENTITY> doSelectList(YsGroupUserCB cb, Class<ENTITY> entityType) {
        assertCBNotNull(cb); assertObjectNotNull("entityType", entityType);
        assertSpecifyDerivedReferrerEntityProperty(cb, entityType);
        return helpSelectListInternally(cb, entityType, new InternalSelectListCallback<ENTITY, YsGroupUserCB>() {
            public List<ENTITY> callbackSelectList(YsGroupUserCB cb, Class<ENTITY> entityType) { return delegateSelectList(cb, entityType); } });
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
     * YsGroupUserCB cb = new YsGroupUserCB();
     * cb.query().setFoo...(value);
     * cb.query().addOrderBy_Bar...();
     * cb.<span style="color: #FD4747">paging</span>(20, 3); <span style="color: #3F7E5E">// 20 records per a page and current page number is 3</span>
     * PagingResultBean&lt;YsGroupUser&gt; page = ysGroupUserBhv.<span style="color: #FD4747">selectPage</span>(cb);
     * int allRecordCount = page.getAllRecordCount();
     * int allPageCount = page.getAllPageCount();
     * boolean isExistPrePage = page.isExistPrePage();
     * boolean isExistNextPage = page.isExistNextPage();
     * ...
     * for (YsGroupUser ysGroupUser : page) {
     *     ... = ysGroupUser.get...();
     * }
     * </pre>
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @return The result bean of selected page. (NotNull)
     * @exception org.seasar.dbflute.exception.DangerousResultSizeException When the result size is over the specified safety size.
     */
    public PagingResultBean<YsGroupUser> selectPage(YsGroupUserCB cb) {
        return doSelectPage(cb, YsGroupUser.class);
    }

    protected <ENTITY extends YsGroupUser> PagingResultBean<ENTITY> doSelectPage(YsGroupUserCB cb, Class<ENTITY> entityType) {
        assertCBNotNull(cb); assertObjectNotNull("entityType", entityType);
        return helpSelectPageInternally(cb, entityType, new InternalSelectPageCallback<ENTITY, YsGroupUserCB>() {
            public int callbackSelectCount(YsGroupUserCB cb) { return doSelectCount(cb); }
            public List<ENTITY> callbackSelectList(YsGroupUserCB cb, Class<ENTITY> entityType) { return doSelectList(cb, entityType); }
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
     * ysGroupUserBhv.<span style="color: #FD4747">scalarSelect</span>(Date.class).max(new ScalarQuery() {
     *     public void query(YsGroupUserCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooDatetime()</span>; <span style="color: #3F7E5E">// required for a function</span>
     *         cb.query().setBarName_PrefixSearch("S");
     *     }
     * });
     * </pre>
     * @param <RESULT> The type of result.
     * @param resultType The type of result. (NotNull)
     * @return The scalar value derived by a function. (Nullable)
     */
    public <RESULT> SLFunction<YsGroupUserCB, RESULT> scalarSelect(Class<RESULT> resultType) {
        return doScalarSelect(resultType, newMyConditionBean());
    }

    protected <RESULT, CB extends YsGroupUserCB> SLFunction<CB, RESULT> doScalarSelect(Class<RESULT> resultType, CB cb) {
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
     * @param ysGroupUserList The list of ysGroupUser. (NotNull)
     * @return The list of foreign table. (NotNull)
     */
    public List<YsGroup> pulloutYsGroup(List<YsGroupUser> ysGroupUserList) {
        return helpPulloutInternally(ysGroupUserList, new InternalPulloutCallback<YsGroupUser, YsGroup>() {
            public YsGroup getFr(YsGroupUser e) { return e.getYsGroup(); }
            public boolean hasRf() { return true; }
            public void setRfLs(YsGroup e, List<YsGroupUser> ls)
            { e.setYsGroupUserList(ls); }
        });
    }
    /**
     * Pull out the list of foreign table 'YsUser'.
     * @param ysGroupUserList The list of ysGroupUser. (NotNull)
     * @return The list of foreign table. (NotNull)
     */
    public List<YsUser> pulloutYsUser(List<YsGroupUser> ysGroupUserList) {
        return helpPulloutInternally(ysGroupUserList, new InternalPulloutCallback<YsGroupUser, YsUser>() {
            public YsUser getFr(YsGroupUser e) { return e.getYsUser(); }
            public boolean hasRf() { return true; }
            public void setRfLs(YsUser e, List<YsGroupUser> ls)
            { e.setYsGroupUserList(ls); }
        });
    }

    // ===================================================================================
    //                                                                       Entity Update
    //                                                                       =============
    /**
     * Insert the entity.
     * <pre>
     * YsGroupUser ysGroupUser = new YsGroupUser();
     * <span style="color: #3F7E5E">// if auto-increment, you don't need to set the PK value</span>
     * ysGroupUser.setFoo...(value);
     * ysGroupUser.setBar...(value);
     * ysGroupUserBhv.<span style="color: #FD4747">insert</span>(ysGroupUser);
     * ... = ysGroupUser.getPK...(); <span style="color: #3F7E5E">// if auto-increment, you can get the value after</span>
     * </pre>
     * @param ysGroupUser The entity of insert target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insert(YsGroupUser ysGroupUser) {
        assertEntityNotNull(ysGroupUser);
        delegateInsert(ysGroupUser);
    }

    @Override
    protected void doCreate(Entity entity) {
        insert(downcast(entity));
    }

    /**
     * Update the entity modified-only. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * YsGroupUser ysGroupUser = new YsGroupUser();
     * ysGroupUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysGroupUser.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * ysGroupUser.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     ysGroupUserBhv.<span style="color: #FD4747">update</span>(ysGroupUser);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * } 
     * </pre>
     * @param ysGroupUser The entity of update target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void update(final YsGroupUser ysGroupUser) {
        helpUpdateInternally(ysGroupUser, new InternalUpdateCallback<YsGroupUser>() {
            public int callbackDelegateUpdate(YsGroupUser entity) { return delegateUpdate(entity); } });
    }

    @Override
    protected void doModify(Entity entity) {
        update(downcast(entity));
    }

    /**
     * Update the entity non-strictly modified-only. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsGroupUser ysGroupUser = new YsGroupUser();
     * ysGroupUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysGroupUser.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysGroupUser.setVersionNo(value);</span>
     * ysGroupUserBhv.<span style="color: #FD4747">updateNonstrict</span>(ysGroupUser);
     * </pre>
     * @param ysGroupUser The entity of update target. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void updateNonstrict(final YsGroupUser ysGroupUser) {
        helpUpdateNonstrictInternally(ysGroupUser, new InternalUpdateNonstrictCallback<YsGroupUser>() {
            public int callbackDelegateUpdateNonstrict(YsGroupUser entity) { return delegateUpdateNonstrict(entity); } });
    }

    @Override
    protected void doModifyNonstrict(Entity entity) {
        updateNonstrict(downcast(entity));
    }

    /**
     * Insert or update the entity modified-only. {ExclusiveControl(when update)}
     * @param ysGroupUser The entity of insert or update target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insertOrUpdate(final YsGroupUser ysGroupUser) {
        helpInsertOrUpdateInternally(ysGroupUser, new InternalInsertOrUpdateCallback<YsGroupUser, YsGroupUserCB>() {
            public void callbackInsert(YsGroupUser entity) { insert(entity); }
            public void callbackUpdate(YsGroupUser entity) { update(entity); }
            public YsGroupUserCB callbackNewMyConditionBean() { return newMyConditionBean(); }
            public int callbackSelectCount(YsGroupUserCB cb) { return selectCount(cb); }
        });
    }

    @Override
    protected void doCreateOrUpdate(Entity entity) {
        insertOrUpdate(downcast(entity));
    }

    /**
     * Insert or update the entity non-strictly modified-only. {NonExclusiveControl(when update)}
     * @param ysGroupUser The entity of insert or update target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insertOrUpdateNonstrict(YsGroupUser ysGroupUser) {
        helpInsertOrUpdateInternally(ysGroupUser, new InternalInsertOrUpdateNonstrictCallback<YsGroupUser>() {
            public void callbackInsert(YsGroupUser entity) { insert(entity); }
            public void callbackUpdateNonstrict(YsGroupUser entity) { updateNonstrict(entity); }
        });
    }

    @Override
    protected void doCreateOrUpdateNonstrict(Entity entity) {
        insertOrUpdateNonstrict(downcast(entity));
    }

    /**
     * Delete the entity. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * YsGroupUser ysGroupUser = new YsGroupUser();
     * ysGroupUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * ysGroupUser.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     ysGroupUserBhv.<span style="color: #FD4747">delete</span>(ysGroupUser);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * } 
     * </pre>
     * @param ysGroupUser The entity of delete target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void delete(YsGroupUser ysGroupUser) {
        helpDeleteInternally(ysGroupUser, new InternalDeleteCallback<YsGroupUser>() {
            public int callbackDelegateDelete(YsGroupUser entity) { return delegateDelete(entity); } });
    }

    @Override
    protected void doRemove(Entity entity) {
        delete(downcast(entity));
    }

    /**
     * Delete the entity non-strictly. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsGroupUser ysGroupUser = new YsGroupUser();
     * ysGroupUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysGroupUser.setVersionNo(value);</span>
     * ysGroupUserBhv.<span style="color: #FD4747">deleteNonstrict</span>(ysGroupUser);
     * </pre>
     * @param ysGroupUser Entity. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void deleteNonstrict(YsGroupUser ysGroupUser) {
        helpDeleteNonstrictInternally(ysGroupUser, new InternalDeleteNonstrictCallback<YsGroupUser>() {
            public int callbackDelegateDeleteNonstrict(YsGroupUser entity) { return delegateDeleteNonstrict(entity); } });
    }

    /**
     * Delete the entity non-strictly ignoring deleted. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsGroupUser ysGroupUser = new YsGroupUser();
     * ysGroupUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysGroupUser.setVersionNo(value);</span>
     * ysGroupUserBhv.<span style="color: #FD4747">deleteNonstrictIgnoreDeleted</span>(ysGroupUser);
     * <span style="color: #3F7E5E">// if the target entity doesn't exist, no exception</span>
     * </pre>
     * @param ysGroupUser Entity. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void deleteNonstrictIgnoreDeleted(YsGroupUser ysGroupUser) {
        helpDeleteNonstrictIgnoreDeletedInternally(ysGroupUser, new InternalDeleteNonstrictIgnoreDeletedCallback<YsGroupUser>() {
            public int callbackDelegateDeleteNonstrict(YsGroupUser entity) { return delegateDeleteNonstrict(entity); } });
    }

    // ===================================================================================
    //                                                                        Batch Update
    //                                                                        ============
    /**
     * Batch-insert the list. This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupUserList The list of the entity. (NotNull)
     * @return The array of inserted count.
     */
    public int[] batchInsert(List<YsGroupUser> ysGroupUserList) {
        assertObjectNotNull("ysGroupUserList", ysGroupUserList);
        return delegateInsertList(ysGroupUserList);
    }

    /**
     * Batch-update the list. <br />
     * All columns are update target. {NOT modified only} <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupUserList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchUpdate(List<YsGroupUser> ysGroupUserList) {
        assertObjectNotNull("ysGroupUserList", ysGroupUserList);
        return delegateUpdateList(ysGroupUserList);
    }

    /**
     * Batch-update the list non-strictly. <br />
     * All columns are update target. {NOT modified only} <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupUserList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchUpdateNonstrict(List<YsGroupUser> ysGroupUserList) {
        assertObjectNotNull("ysGroupUserList", ysGroupUserList);
        return delegateUpdateListNonstrict(ysGroupUserList);
    }

    /**
     * Batch-delete the list. <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupUserList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchDelete(List<YsGroupUser> ysGroupUserList) {
        assertObjectNotNull("ysGroupUserList", ysGroupUserList);
        return delegateDeleteList(ysGroupUserList);
    }

    /**
     * Batch-delete the list non-strictly. <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupUserList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchDeleteNonstrict(List<YsGroupUser> ysGroupUserList) {
        assertObjectNotNull("ysGroupUserList", ysGroupUserList);
        return delegateDeleteListNonstrict(ysGroupUserList);
    }

    // ===================================================================================
    //                                                                        Query Update
    //                                                                        ============
    /**
     * Query-update the several entities non-strictly modified-only. {NonExclusiveControl}
     * <pre>
     * YsGroupUser ysGroupUser = new YsGroupUser();
     * <span style="color: #3F7E5E">// you don't need to set PK value</span>
     * <span style="color: #3F7E5E">//ysGroupUser.setPK...(value);</span>
     * ysGroupUser.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysGroupUser.setVersionNo(value);</span>
     * YsGroupUserCB cb = new YsGroupUserCB();
     * cb.query().setFoo...(value);
     * ysGroupUserBhv.<span style="color: #FD4747">queryUpdate</span>(ysGroupUser, cb);
     * </pre>
     * @param ysGroupUser The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @return The updated count.
     */
    public int queryUpdate(YsGroupUser ysGroupUser, YsGroupUserCB cb) {
        return delegateQueryUpdate(ysGroupUser, cb);
    }

    /**
     * Query-delete the several entities. {NonExclusiveControl}
     * <pre>
     * YsGroupUserCB cb = new YsGroupUserCB();
     * cb.query().setFoo...(value);
     * ysGroupUserBhv.<span style="color: #FD4747">queryDelete</span>(ysGroupUser, cb);
     * </pre>
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @return The deleted count.
     */
    public int queryDelete(YsGroupUserCB cb) {
        return delegateQueryDelete(cb);
    }

    /**
     * Varying-update the entity modified-only. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * YsGroupUser ysGroupUser = new YsGroupUser();
     * ysGroupUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysGroupUser.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * ysGroupUser.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     UpdateOption&lt;YsGroupUserCB&gt; option = new UpdateOption&lt;YsGroupUserCB&gt;();
     *     option.self(new SpecifyQuery&lt;YsGroupUserCB&gt;() {
     *         public void specify(YsGroupUserCB cb) {
     *             cb.specify().<span style="color: #FD4747">columnXxxCount()</span>;
     *         }
     *     }).plus(1); <span style="color: #3F7E5E">// XXX_COUNT = XXX_COUNT + 1</span>
     *     ysGroupUserBhv.<span style="color: #FD4747">varyingUpdate</span>(ysGroupUser, option);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * }
     * </pre>
     * @param ysGroupUser The entity of update target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @param option The option of update for varying values. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void varyingUpdate(YsGroupUser ysGroupUser, final UpdateOption<YsGroupUserCB> option) {
        processVaryingUpdate(option);
        helpUpdateInternally(ysGroupUser, new InternalUpdateCallback<YsGroupUser>() {
            public int callbackDelegateUpdate(YsGroupUser entity) { return delegateVaryingUpdate(entity, option); } });
    }

    /**
     * Varying-update the entity non-strictly modified-only. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsGroupUser ysGroupUser = new YsGroupUser();
     * ysGroupUser.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysGroupUser.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysGroupUser.setVersionNo(value);</span>
     * UpdateOption&lt;YsGroupUserCB&gt; option = new UpdateOption&lt;YsGroupUserCB&gt;();
     * option.self(new SpecifyQuery&lt;YsGroupUserCB&gt;() {
     *     public void specify(YsGroupUserCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooCount()</span>;
     *     }
     * }).plus(1); <span style="color: #3F7E5E">// FOO_COUNT = FOO_COUNT + 1</span>
     * ysGroupUserBhv.<span style="color: #FD4747">varyingUpdateNonstrict</span>(ysGroupUser, option);
     * </pre>
     * @param ysGroupUser The entity of update target. (NotNull) {PrimaryKeyRequired}
     * @param option The option of update for varying values. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void varyingUpdateNonstrict(YsGroupUser ysGroupUser, final UpdateOption<YsGroupUserCB> option) {
        processVaryingUpdate(option);
        helpUpdateNonstrictInternally(ysGroupUser, new InternalUpdateNonstrictCallback<YsGroupUser>() {
            public int callbackDelegateUpdateNonstrict(YsGroupUser entity) { return delegateVaryingUpdateNonstrict(entity, option); } });
    }

    /**
     * Varying-query-update the several entities non-strictly modified-only. {NonExclusiveControl}
     * <pre>
     * YsGroupUser ysGroupUser = new YsGroupUser();
     * <span style="color: #3F7E5E">// you don't need to set PK value</span>
     * <span style="color: #3F7E5E">//ysGroupUser.setPK...(value);</span>
     * ysGroupUser.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysGroupUser.setVersionNo(value);</span>
     * YsGroupUserCB cb = new YsGroupUserCB();
     * cb.query().setFoo...(value);
     * UpdateOption&lt;YsGroupUserCB&gt; option = new UpdateOption&lt;YsGroupUserCB&gt;();
     * option.self(new SpecifyQuery&lt;YsGroupUserCB&gt;() {
     *     public void specify(YsGroupUserCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooCount()</span>;
     *     }
     * }).plus(1); <span style="color: #3F7E5E">// FOO_COUNT = FOO_COUNT + 1</span>
     * ysGroupUserBhv.<span style="color: #FD4747">varyingQueryUpdate</span>(ysGroupUser, cb, option);
     * </pre>
     * @param ysGroupUser The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @param option The option of update for varying values. (NotNull)
     * @return The updated count.
     */
    public int varyingQueryUpdate(YsGroupUser ysGroupUser, YsGroupUserCB cb, final UpdateOption<YsGroupUserCB> option) {
        processVaryingUpdate(option);
        return delegateVaryingQueryUpdate(ysGroupUser, cb, option);
    }

    protected void processVaryingUpdate(UpdateOption<YsGroupUserCB> option) {
        assertUpdateOptionNotNull(option);
        YsGroupUserCB cb = newMyConditionBean();
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
    protected int delegateSelectCount(YsGroupUserCB cb) { return invoke(createSelectCountCBCommand(cb)); }
    protected <ENTITY extends YsGroupUser> void delegateSelectCursor(YsGroupUserCB cb, EntityRowHandler<ENTITY> entityRowHandler, Class<ENTITY> entityType)
    { invoke(createSelectCursorCBCommand(cb, entityRowHandler, entityType)); }
    protected <ENTITY extends YsGroupUser> List<ENTITY> delegateSelectList(YsGroupUserCB cb, Class<ENTITY> entityType)
    { return invoke(createSelectListCBCommand(cb, entityType)); }

    // -----------------------------------------------------
    //                                                Update
    //                                                ------
    protected int delegateInsert(YsGroupUser e)
    { if (!processBeforeInsert(e)) { return 1; } return invoke(createInsertEntityCommand(e)); }
    protected int delegateUpdate(YsGroupUser e)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createUpdateEntityCommand(e)); }
    protected int delegateUpdateNonstrict(YsGroupUser e)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createUpdateNonstrictEntityCommand(e)); }
    protected int delegateDelete(YsGroupUser e)
    { if (!processBeforeDelete(e)) { return 1; } return invoke(createDeleteEntityCommand(e)); }
    protected int delegateDeleteNonstrict(YsGroupUser e)
    { if (!processBeforeDelete(e)) { return 1; } return invoke(createDeleteNonstrictEntityCommand(e)); }

    protected int[] delegateInsertList(List<YsGroupUser> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchInsertEntityCommand(helpFilterBeforeInsertInternally(ls))); }
    @SuppressWarnings("unchecked")
    protected int[] doCreateList(List<Entity> ls) { return delegateInsertList((List)ls); }
    protected int[] delegateUpdateList(List<YsGroupUser> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchUpdateEntityCommand(helpFilterBeforeUpdateInternally(ls))); }
    @SuppressWarnings("unchecked")
    protected int[] doModifyList(List<Entity> ls) { return delegateUpdateList((List)ls); }
    protected int[] delegateUpdateListNonstrict(List<YsGroupUser> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchUpdateNonstrictEntityCommand(helpFilterBeforeUpdateInternally(ls))); }
    protected int[] delegateDeleteList(List<YsGroupUser> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchDeleteEntityCommand(helpFilterBeforeDeleteInternally(ls))); }
    @SuppressWarnings("unchecked")
    protected int[] doRemoveList(List<Entity> ls) { return delegateDeleteList((List)ls); }
    protected int[] delegateDeleteListNonstrict(List<YsGroupUser> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchDeleteNonstrictEntityCommand(helpFilterBeforeDeleteInternally(ls))); }

    protected int delegateQueryUpdate(YsGroupUser e, YsGroupUserCB cb)
    { if (!processBeforeQueryUpdate(e, cb)) { return 0; } return invoke(createQueryUpdateEntityCBCommand(e, cb));  }
    protected int delegateQueryDelete(YsGroupUserCB cb)
    { if (!processBeforeQueryDelete(cb)) { return 0; } return invoke(createQueryDeleteCBCommand(cb));  }

    protected int delegateVaryingUpdate(YsGroupUser e, UpdateOption<YsGroupUserCB> op)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createVaryingUpdateEntityCommand(e, op)); }
    protected int delegateVaryingUpdateNonstrict(YsGroupUser e, UpdateOption<YsGroupUserCB> op)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createVaryingUpdateNonstrictEntityCommand(e, op)); }
    protected int delegateVaryingQueryUpdate(YsGroupUser e, YsGroupUserCB cb, UpdateOption<YsGroupUserCB> op)
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
    protected YsGroupUser downcast(Entity entity) {
        return helpEntityDowncastInternally(entity, YsGroupUser.class);
    }

    protected YsGroupUserCB downcast(ConditionBean cb) {
        return helpConditionBeanDowncastInternally(cb, YsGroupUserCB.class);
    }
}
