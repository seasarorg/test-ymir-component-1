package org.seasar.ymir.scaffold.dbflute.bsbhv;

import java.util.List;

import org.seasar.dbflute.*;
import org.seasar.dbflute.bhv.*;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.EntityRowHandler;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.PagingBean;
import org.seasar.dbflute.cbean.PagingHandler;
import org.seasar.dbflute.cbean.PagingInvoker;
import org.seasar.dbflute.cbean.PagingResultBean;
import org.seasar.dbflute.cbean.ResultBeanBuilder;
import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.ymir.scaffold.dbflute.exentity.*;
import org.seasar.ymir.scaffold.dbflute.bsentity.dbmeta.*;
import org.seasar.ymir.scaffold.dbflute.cbean.*;

/**
 * The behavior of YS_GROUP_USER that is TABLE. <br />
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
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @return The selected count.
     */
    public int selectCount(YsGroupUserCB cb) {
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
     * Select the cursor by the condition-bean. <br />
     * Attention: It has a mapping cost from result set to entity.
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @param entityRowHandler The handler of entity row of YsGroupUser. (NotNull)
     */
    public void selectCursor(YsGroupUserCB cb, EntityRowHandler<YsGroupUser> entityRowHandler) {
        assertCBNotNull(cb); assertObjectNotNull("entityRowHandler<YsGroupUser>", entityRowHandler);
        delegateSelectCursor(cb, entityRowHandler);
    }

    // ===================================================================================
    //                                                                       Entity Select
    //                                                                       =============
    /**
     * Select the entity by the condition-bean.
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @return The selected entity. (Nullable: If the condition has no data, it returns null)
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public YsGroupUser selectEntity(final YsGroupUserCB cb) {
        return helpSelectEntityInternally(cb, new InternalSelectEntityCallback<YsGroupUser, YsGroupUserCB>() {
            public List<YsGroupUser> callbackSelectList(YsGroupUserCB cb) { return selectList(cb); } });
    }

    @Override
    protected Entity doReadEntity(ConditionBean cb) {
        return selectEntity(downcast(cb));
    }

    /**
     * Select the entity by the condition-bean with deleted check.
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public YsGroupUser selectEntityWithDeletedCheck(final YsGroupUserCB cb) {
        return helpSelectEntityWithDeletedCheckInternally(cb, new InternalSelectEntityWithDeletedCheckCallback<YsGroupUser, YsGroupUserCB>() {
            public List<YsGroupUser> callbackSelectList(YsGroupUserCB cb) { return selectList(cb); } });
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
     */
    public YsGroupUser selectByPKValue(Long id) {
        return selectEntity(buildPKCB(id));
    }

    /**
     * Select the entity by the primary-key value with deleted check.
     * @param id The one of primary key. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public YsGroupUser selectByPKValueWithDeletedCheck(Long id) {
        return selectEntityWithDeletedCheck(buildPKCB(id));
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
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @return The result bean of selected list. (NotNull)
     */
    public ListResultBean<YsGroupUser> selectList(YsGroupUserCB cb) {
        assertCBNotNull(cb);
        return new ResultBeanBuilder<YsGroupUser>(getTableDbName()).buildListResultBean(cb, delegateSelectList(cb));
    }

    @Override
    protected ListResultBean<? extends Entity> doReadList(ConditionBean cb) {
        return selectList(downcast(cb));
    }

    // ===================================================================================
    //                                                                         Page Select
    //                                                                         ===========
    /**
     * Select the page as result bean.
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @return The result bean of selected page. (NotNull)
     */
    public PagingResultBean<YsGroupUser> selectPage(final YsGroupUserCB cb) {
        assertCBNotNull(cb);
        final PagingInvoker<YsGroupUser> invoker = new PagingInvoker<YsGroupUser>(getTableDbName());
        final PagingHandler<YsGroupUser> handler = new PagingHandler<YsGroupUser>() {
            public PagingBean getPagingBean() { return cb; }
            public int count() { return selectCount(cb); }
            public List<YsGroupUser> paging() { return selectList(cb); }
        };
        return invoker.invokePaging(handler);
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
     * Call a function method after this method called like as follows:
     * <pre>
     * ysGroupUserBhv.scalarSelect(Date.class).max(new ScalarQuery(YsGroupUserCB cb) {
     *     cb.specify().columnXxxDatetime(); // the required specification of target column
     *     cb.query().setXxxName_PrefixSearch("S"); // query as you like it
     * });
     * </pre>
     * @param <RESULT> The type of result.
     * @param resultType The type of result. (NotNull)
     * @return The scalar value derived by a function. (Nullable)
     */
    public <RESULT> SLFunction<YsGroupUserCB, RESULT> scalarSelect(Class<RESULT> resultType) {
        YsGroupUserCB cb = newMyConditionBean();
        cb.xsetupForScalarSelect();
        cb.getSqlClause().disableSelectIndex(); // for when you use union
        return new SLFunction<YsGroupUserCB, RESULT>(cb, resultType);
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
     * Update the entity modified-only. {UpdateCountZeroException, ConcurrencyControl}
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
     * Update the entity non-strictly modified-only. {UpdateCountZeroException, NonConcurrencyControl}
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
     * Insert or update the entity modified-only. {ConcurrencyControl(when update)}
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
     * Insert or update the entity non-strictly modified-only. {NonConcurrencyControl(when update)}
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
     * Delete the entity. {UpdateCountZeroException, ConcurrencyControl}
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
     * Delete the entity non-strictly. {UpdateCountZeroException, NonConcurrencyControl}
     * @param ysGroupUser Entity. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void deleteNonstrict(YsGroupUser ysGroupUser) {
        helpDeleteNonstrictInternally(ysGroupUser, new InternalDeleteNonstrictCallback<YsGroupUser>() {
            public int callbackDelegateDeleteNonstrict(YsGroupUser entity) { return delegateDeleteNonstrict(entity); } });
    }

    /**
     * Delete the entity non-strictly ignoring deleted. {UpdateCountZeroException, NonConcurrencyControl}
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
     * Batch insert the list. This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupUserList The list of the entity. (NotNull)
     * @return The array of inserted count.
     */
    public int[] batchInsert(List<YsGroupUser> ysGroupUserList) {
        assertObjectNotNull("ysGroupUserList", ysGroupUserList);
        return delegateInsertList(ysGroupUserList);
    }

    /**
     * Batch update the list. All columns are update target. {NOT modified only} <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupUserList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchUpdate(List<YsGroupUser> ysGroupUserList) {
        assertObjectNotNull("ysGroupUserList", ysGroupUserList);
        return delegateUpdateList(ysGroupUserList);
    }

    /**
     * Batch update the list non-strictly. All columns are update target. {NOT modified only} <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupUserList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchUpdateNonstrict(List<YsGroupUser> ysGroupUserList) {
        assertObjectNotNull("ysGroupUserList", ysGroupUserList);
        return delegateUpdateListNonstrict(ysGroupUserList);
    }

    /**
     * Batch delete the list. <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupUserList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchDelete(List<YsGroupUser> ysGroupUserList) {
        assertObjectNotNull("ysGroupUserList", ysGroupUserList);
        return delegateDeleteList(ysGroupUserList);
    }

    /**
     * Batch delete the list non-strictly. <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
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
     * Query update the several entities. {NoConcurrencyControl}
     * @param ysGroupUser The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @return The updated count.
     */
    public int queryUpdate(YsGroupUser ysGroupUser, YsGroupUserCB cb) {
        assertObjectNotNull("ysGroupUser", ysGroupUser); assertCBNotNull(cb);
        setupCommonColumnOfUpdateIfNeeds(ysGroupUser);
        filterEntityOfUpdate(ysGroupUser); assertEntityOfUpdate(ysGroupUser);
        return invoke(createQueryUpdateEntityCBCommand(ysGroupUser, cb));
    }

    /**
     * Query delete the several entities. {NoConcurrencyControl}
     * @param cb The condition-bean of YsGroupUser. (NotNull)
     * @return The deleted count.
     */
    public int queryDelete(YsGroupUserCB cb) {
        assertCBNotNull(cb);
        return invoke(createQueryDeleteCBCommand(cb));
    }
    
    // ===================================================================================
    //                                                                     Delegate Method
    //                                                                     ===============
    // [Behavior Command]
    // -----------------------------------------------------
    //                                                Select
    //                                                ------
    protected int delegateSelectCount(YsGroupUserCB cb) { return invoke(createSelectCountCBCommand(cb)); }
    protected void delegateSelectCursor(YsGroupUserCB cb, EntityRowHandler<YsGroupUser> entityRowHandler)
    { invoke(createSelectCursorCBCommand(cb, entityRowHandler, YsGroupUser.class)); }
    protected List<YsGroupUser> delegateSelectList(YsGroupUserCB cb)
    { return invoke(createSelectListCBCommand(cb, YsGroupUser.class)); }

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
