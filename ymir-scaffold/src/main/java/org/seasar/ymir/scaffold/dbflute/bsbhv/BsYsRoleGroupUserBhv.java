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
 * The behavior of YS_ROLE_GROUP_USER that is TABLE. <br />
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
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @return The selected count.
     */
    public int selectCount(YsRoleGroupUserCB cb) {
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
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @param entityRowHandler The handler of entity row of YsRoleGroupUser. (NotNull)
     */
    public void selectCursor(YsRoleGroupUserCB cb, EntityRowHandler<YsRoleGroupUser> entityRowHandler) {
        assertCBNotNull(cb); assertObjectNotNull("entityRowHandler<YsRoleGroupUser>", entityRowHandler);
        delegateSelectCursor(cb, entityRowHandler);
    }

    // ===================================================================================
    //                                                                       Entity Select
    //                                                                       =============
    /**
     * Select the entity by the condition-bean.
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @return The selected entity. (Nullable: If the condition has no data, it returns null)
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public YsRoleGroupUser selectEntity(final YsRoleGroupUserCB cb) {
        return helpSelectEntityInternally(cb, new InternalSelectEntityCallback<YsRoleGroupUser, YsRoleGroupUserCB>() {
            public List<YsRoleGroupUser> callbackSelectList(YsRoleGroupUserCB cb) { return selectList(cb); } });
    }

    @Override
    protected Entity doReadEntity(ConditionBean cb) {
        return selectEntity(downcast(cb));
    }

    /**
     * Select the entity by the condition-bean with deleted check.
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public YsRoleGroupUser selectEntityWithDeletedCheck(final YsRoleGroupUserCB cb) {
        return helpSelectEntityWithDeletedCheckInternally(cb, new InternalSelectEntityWithDeletedCheckCallback<YsRoleGroupUser, YsRoleGroupUserCB>() {
            public List<YsRoleGroupUser> callbackSelectList(YsRoleGroupUserCB cb) { return selectList(cb); } });
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
    public YsRoleGroupUser selectByPKValue(Long id) {
        return selectEntity(buildPKCB(id));
    }

    /**
     * Select the entity by the primary-key value with deleted check.
     * @param id The one of primary key. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public YsRoleGroupUser selectByPKValueWithDeletedCheck(Long id) {
        return selectEntityWithDeletedCheck(buildPKCB(id));
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
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @return The result bean of selected list. (NotNull)
     */
    public ListResultBean<YsRoleGroupUser> selectList(YsRoleGroupUserCB cb) {
        assertCBNotNull(cb);
        return new ResultBeanBuilder<YsRoleGroupUser>(getTableDbName()).buildListResultBean(cb, delegateSelectList(cb));
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
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @return The result bean of selected page. (NotNull)
     */
    public PagingResultBean<YsRoleGroupUser> selectPage(final YsRoleGroupUserCB cb) {
        assertCBNotNull(cb);
        final PagingInvoker<YsRoleGroupUser> invoker = new PagingInvoker<YsRoleGroupUser>(getTableDbName());
        final PagingHandler<YsRoleGroupUser> handler = new PagingHandler<YsRoleGroupUser>() {
            public PagingBean getPagingBean() { return cb; }
            public int count() { return selectCount(cb); }
            public List<YsRoleGroupUser> paging() { return selectList(cb); }
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
     * ysRoleGroupUserBhv.scalarSelect(Date.class).max(new ScalarQuery(YsRoleGroupUserCB cb) {
     *     cb.specify().columnXxxDatetime(); // the required specification of target column
     *     cb.query().setXxxName_PrefixSearch("S"); // query as you like it
     * });
     * </pre>
     * @param <RESULT> The type of result.
     * @param resultType The type of result. (NotNull)
     * @return The scalar value derived by a function. (Nullable)
     */
    public <RESULT> SLFunction<YsRoleGroupUserCB, RESULT> scalarSelect(Class<RESULT> resultType) {
        YsRoleGroupUserCB cb = newMyConditionBean();
        cb.xsetupForScalarSelect();
        cb.getSqlClause().disableSelectIndex(); // for when you use union
        return new SLFunction<YsRoleGroupUserCB, RESULT>(cb, resultType);
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
     * Update the entity modified-only. {UpdateCountZeroException, ConcurrencyControl}
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
     * Update the entity non-strictly modified-only. {UpdateCountZeroException, NonConcurrencyControl}
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
     * Insert or update the entity modified-only. {ConcurrencyControl(when update)}
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
     * Insert or update the entity non-strictly modified-only. {NonConcurrencyControl(when update)}
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
     * Delete the entity. {UpdateCountZeroException, ConcurrencyControl}
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
     * Delete the entity non-strictly. {UpdateCountZeroException, NonConcurrencyControl}
     * @param ysRoleGroupUser Entity. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void deleteNonstrict(YsRoleGroupUser ysRoleGroupUser) {
        helpDeleteNonstrictInternally(ysRoleGroupUser, new InternalDeleteNonstrictCallback<YsRoleGroupUser>() {
            public int callbackDelegateDeleteNonstrict(YsRoleGroupUser entity) { return delegateDeleteNonstrict(entity); } });
    }

    /**
     * Delete the entity non-strictly ignoring deleted. {UpdateCountZeroException, NonConcurrencyControl}
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
     * Batch insert the list. This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleGroupUserList The list of the entity. (NotNull)
     * @return The array of inserted count.
     */
    public int[] batchInsert(List<YsRoleGroupUser> ysRoleGroupUserList) {
        assertObjectNotNull("ysRoleGroupUserList", ysRoleGroupUserList);
        return delegateInsertList(ysRoleGroupUserList);
    }

    /**
     * Batch update the list. All columns are update target. {NOT modified only} <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleGroupUserList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchUpdate(List<YsRoleGroupUser> ysRoleGroupUserList) {
        assertObjectNotNull("ysRoleGroupUserList", ysRoleGroupUserList);
        return delegateUpdateList(ysRoleGroupUserList);
    }

    /**
     * Batch update the list non-strictly. All columns are update target. {NOT modified only} <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleGroupUserList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchUpdateNonstrict(List<YsRoleGroupUser> ysRoleGroupUserList) {
        assertObjectNotNull("ysRoleGroupUserList", ysRoleGroupUserList);
        return delegateUpdateListNonstrict(ysRoleGroupUserList);
    }

    /**
     * Batch delete the list. <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleGroupUserList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchDelete(List<YsRoleGroupUser> ysRoleGroupUserList) {
        assertObjectNotNull("ysRoleGroupUserList", ysRoleGroupUserList);
        return delegateDeleteList(ysRoleGroupUserList);
    }

    /**
     * Batch delete the list non-strictly. <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
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
     * Query update the several entities. {NoConcurrencyControl}
     * @param ysRoleGroupUser The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @return The updated count.
     */
    public int queryUpdate(YsRoleGroupUser ysRoleGroupUser, YsRoleGroupUserCB cb) {
        assertObjectNotNull("ysRoleGroupUser", ysRoleGroupUser); assertCBNotNull(cb);
        setupCommonColumnOfUpdateIfNeeds(ysRoleGroupUser);
        filterEntityOfUpdate(ysRoleGroupUser); assertEntityOfUpdate(ysRoleGroupUser);
        return invoke(createQueryUpdateEntityCBCommand(ysRoleGroupUser, cb));
    }

    /**
     * Query delete the several entities. {NoConcurrencyControl}
     * @param cb The condition-bean of YsRoleGroupUser. (NotNull)
     * @return The deleted count.
     */
    public int queryDelete(YsRoleGroupUserCB cb) {
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
    protected int delegateSelectCount(YsRoleGroupUserCB cb) { return invoke(createSelectCountCBCommand(cb)); }
    protected void delegateSelectCursor(YsRoleGroupUserCB cb, EntityRowHandler<YsRoleGroupUser> entityRowHandler)
    { invoke(createSelectCursorCBCommand(cb, entityRowHandler, YsRoleGroupUser.class)); }
    protected List<YsRoleGroupUser> delegateSelectList(YsRoleGroupUserCB cb)
    { return invoke(createSelectListCBCommand(cb, YsRoleGroupUser.class)); }

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
