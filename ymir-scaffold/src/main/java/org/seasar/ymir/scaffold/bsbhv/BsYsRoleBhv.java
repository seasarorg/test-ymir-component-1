package org.seasar.ymir.scaffold.bsbhv;

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
import org.seasar.ymir.scaffold.exbhv.*;
import org.seasar.ymir.scaffold.exentity.*;
import org.seasar.ymir.scaffold.bsentity.dbmeta.*;
import org.seasar.ymir.scaffold.cbean.*;

/**
 * The behavior of (ロール)YS_ROLE that is TABLE. <br />
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
     * @param cb The condition-bean of YsRole. (NotNull)
     * @return The selected count.
     */
    public int selectCount(YsRoleCB cb) {
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
     * @param cb The condition-bean of YsRole. (NotNull)
     * @param entityRowHandler The handler of entity row of YsRole. (NotNull)
     */
    public void selectCursor(YsRoleCB cb, EntityRowHandler<YsRole> entityRowHandler) {
        assertCBNotNull(cb); assertObjectNotNull("entityRowHandler<YsRole>", entityRowHandler);
        delegateSelectCursor(cb, entityRowHandler);
    }

    // ===================================================================================
    //                                                                       Entity Select
    //                                                                       =============
    /**
     * Select the entity by the condition-bean.
     * @param cb The condition-bean of YsRole. (NotNull)
     * @return The selected entity. (Nullable: If the condition has no data, it returns null)
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public YsRole selectEntity(final YsRoleCB cb) {
        return helpSelectEntityInternally(cb, new InternalSelectEntityCallback<YsRole, YsRoleCB>() {
            public List<YsRole> callbackSelectList(YsRoleCB cb) { return selectList(cb); } });
    }

    @Override
    protected Entity doReadEntity(ConditionBean cb) {
        return selectEntity(downcast(cb));
    }

    /**
     * Select the entity by the condition-bean with deleted check.
     * @param cb The condition-bean of YsRole. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public YsRole selectEntityWithDeletedCheck(final YsRoleCB cb) {
        return helpSelectEntityWithDeletedCheckInternally(cb, new InternalSelectEntityWithDeletedCheckCallback<YsRole, YsRoleCB>() {
            public List<YsRole> callbackSelectList(YsRoleCB cb) { return selectList(cb); } });
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
    public YsRole selectByPKValue(Long id) {
        return selectEntity(buildPKCB(id));
    }

    /**
     * Select the entity by the primary-key value with deleted check.
     * @param id The one of primary key. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public YsRole selectByPKValueWithDeletedCheck(Long id) {
        return selectEntityWithDeletedCheck(buildPKCB(id));
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
     * @param cb The condition-bean of YsRole. (NotNull)
     * @return The result bean of selected list. (NotNull)
     */
    public ListResultBean<YsRole> selectList(YsRoleCB cb) {
        assertCBNotNull(cb);
        return new ResultBeanBuilder<YsRole>(getTableDbName()).buildListResultBean(cb, delegateSelectList(cb));
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
     * @param cb The condition-bean of YsRole. (NotNull)
     * @return The result bean of selected page. (NotNull)
     */
    public PagingResultBean<YsRole> selectPage(final YsRoleCB cb) {
        assertCBNotNull(cb);
        final PagingInvoker<YsRole> invoker = new PagingInvoker<YsRole>(getTableDbName());
        final PagingHandler<YsRole> handler = new PagingHandler<YsRole>() {
            public PagingBean getPagingBean() { return cb; }
            public int count() { return selectCount(cb); }
            public List<YsRole> paging() { return selectList(cb); }
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
     * ysRoleBhv.scalarSelect(Date.class).max(new ScalarQuery(YsRoleCB cb) {
     *     cb.specify().columnXxxDatetime(); // the required specification of target column
     *     cb.query().setXxxName_PrefixSearch("S"); // query as you like it
     * });
     * </pre>
     * @param <RESULT> The type of result.
     * @param resultType The type of result. (NotNull)
     * @return The scalar value derived by a function. (Nullable)
     */
    public <RESULT> SLFunction<YsRoleCB, RESULT> scalarSelect(Class<RESULT> resultType) {
        YsRoleCB cb = newMyConditionBean();
        cb.xsetupForScalarSelect();
        cb.getSqlClause().disableSelectIndex(); // for when you use union
        return new SLFunction<YsRoleCB, RESULT>(cb, resultType);
    }

    // ===================================================================================
    //                                                                       Load Referrer
    //                                                                       =============
    /**
     * {Refer to overload method that has an argument of the list of entity.}
     * @param ysRole The entity of ysRole. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean setupper for registering referrer condition. (NotNull)
     */
    public void loadYsRoleGroupUserList(YsRole ysRole, ConditionBeanSetupper<YsRoleGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysRole, conditionBeanSetupper);
        loadYsRoleGroupUserList(xnewLRLs(ysRole), conditionBeanSetupper);
    }
    /**
     * Load referrer of ysRoleGroupUserList with the setupper for condition-bean of referrer. <br />
     * About internal policy, the value of primary key(and others too) is treated as case-insensitive. <br />
     * The condition-bean that the setupper provides have settings before you touch it. It is as follows:
     * <pre>
     * cb.query().setRoleId_InScope(pkList);
     * cb.query().addOrderBy_RoleId_Asc();
     * </pre>
     * @param ysRoleList The entity list of ysRole. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean setupper for registering referrer condition. (NotNull)
     */
    public void loadYsRoleGroupUserList(List<YsRole> ysRoleList, ConditionBeanSetupper<YsRoleGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysRoleList, conditionBeanSetupper);
        loadYsRoleGroupUserList(ysRoleList, new LoadReferrerOption<YsRoleGroupUserCB, YsRoleGroupUser>(conditionBeanSetupper));
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
     * Update the entity modified-only. {UpdateCountZeroException, ConcurrencyControl}
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
     * Update the entity non-strictly modified-only. {UpdateCountZeroException, NonConcurrencyControl}
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
     * Insert or update the entity modified-only. {ConcurrencyControl(when update)}
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
     * Insert or update the entity non-strictly modified-only. {NonConcurrencyControl(when update)}
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
     * Delete the entity. {UpdateCountZeroException, ConcurrencyControl}
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
     * Delete the entity non-strictly. {UpdateCountZeroException, NonConcurrencyControl}
     * @param ysRole Entity. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void deleteNonstrict(YsRole ysRole) {
        helpDeleteNonstrictInternally(ysRole, new InternalDeleteNonstrictCallback<YsRole>() {
            public int callbackDelegateDeleteNonstrict(YsRole entity) { return delegateDeleteNonstrict(entity); } });
    }

    /**
     * Delete the entity non-strictly ignoring deleted. {UpdateCountZeroException, NonConcurrencyControl}
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
     * Batch insert the list. This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleList The list of the entity. (NotNull)
     * @return The array of inserted count.
     */
    public int[] batchInsert(List<YsRole> ysRoleList) {
        assertObjectNotNull("ysRoleList", ysRoleList);
        return delegateInsertList(ysRoleList);
    }

    /**
     * Batch update the list. All columns are update target. {NOT modified only} <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchUpdate(List<YsRole> ysRoleList) {
        assertObjectNotNull("ysRoleList", ysRoleList);
        return delegateUpdateList(ysRoleList);
    }

    /**
     * Batch update the list non-strictly. All columns are update target. {NOT modified only} <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchUpdateNonstrict(List<YsRole> ysRoleList) {
        assertObjectNotNull("ysRoleList", ysRoleList);
        return delegateUpdateListNonstrict(ysRoleList);
    }

    /**
     * Batch delete the list. <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysRoleList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchDelete(List<YsRole> ysRoleList) {
        assertObjectNotNull("ysRoleList", ysRoleList);
        return delegateDeleteList(ysRoleList);
    }

    /**
     * Batch delete the list non-strictly. <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
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
     * Query update the several entities. {NoConcurrencyControl}
     * @param ysRole The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of YsRole. (NotNull)
     * @return The updated count.
     */
    public int queryUpdate(YsRole ysRole, YsRoleCB cb) {
        assertObjectNotNull("ysRole", ysRole); assertCBNotNull(cb);
        setupCommonColumnOfUpdateIfNeeds(ysRole);
        filterEntityOfUpdate(ysRole); assertEntityOfUpdate(ysRole);
        return invoke(createQueryUpdateEntityCBCommand(ysRole, cb));
    }

    /**
     * Query delete the several entities. {NoConcurrencyControl}
     * @param cb The condition-bean of YsRole. (NotNull)
     * @return The deleted count.
     */
    public int queryDelete(YsRoleCB cb) {
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
    protected int delegateSelectCount(YsRoleCB cb) { return invoke(createSelectCountCBCommand(cb)); }
    protected void delegateSelectCursor(YsRoleCB cb, EntityRowHandler<YsRole> entityRowHandler)
    { invoke(createSelectCursorCBCommand(cb, entityRowHandler, YsRole.class)); }
    protected List<YsRole> delegateSelectList(YsRoleCB cb)
    { return invoke(createSelectListCBCommand(cb, YsRole.class)); }

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
