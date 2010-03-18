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
import org.seasar.ymir.scaffold.dbflute.exbhv.*;
import org.seasar.ymir.scaffold.dbflute.exentity.*;
import org.seasar.ymir.scaffold.dbflute.bsentity.dbmeta.*;
import org.seasar.ymir.scaffold.dbflute.cbean.*;

/**
 * The behavior of (グループ)YS_GROUP that is TABLE. <br />
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
public abstract class BsYsGroupBhv extends AbstractBehaviorWritable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /*df:BehaviorQueryPathBegin*/
    /*df:BehaviorQueryPathEnd*/

    // ===================================================================================
    //                                                                          Table name
    //                                                                          ==========
    /** @return The name on database of table. (NotNull) */
    public String getTableDbName() { return "YS_GROUP"; }

    // ===================================================================================
    //                                                                              DBMeta
    //                                                                              ======
    /** @return The instance of DBMeta. (NotNull) */
    public DBMeta getDBMeta() { return YsGroupDbm.getInstance(); }

    /** @return The instance of DBMeta as my table type. (NotNull) */
    public YsGroupDbm getMyDBMeta() { return YsGroupDbm.getInstance(); }

    // ===================================================================================
    //                                                                        New Instance
    //                                                                        ============
    /** {@inheritDoc} */
    public Entity newEntity() { return newMyEntity(); }

    /** {@inheritDoc} */
    public ConditionBean newConditionBean() { return newMyConditionBean(); }

    /** @return The instance of new entity as my table type. (NotNull) */
    public YsGroup newMyEntity() { return new YsGroup(); }

    /** @return The instance of new condition-bean as my table type. (NotNull) */
    public YsGroupCB newMyConditionBean() { return new YsGroupCB(); }

    // ===================================================================================
    //                                                                        Count Select
    //                                                                        ============
    /**
     * Select the count by the condition-bean. {IgnorePagingCondition}
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @return The selected count.
     */
    public int selectCount(YsGroupCB cb) {
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
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @param entityRowHandler The handler of entity row of YsGroup. (NotNull)
     */
    public void selectCursor(YsGroupCB cb, EntityRowHandler<YsGroup> entityRowHandler) {
        assertCBNotNull(cb); assertObjectNotNull("entityRowHandler<YsGroup>", entityRowHandler);
        delegateSelectCursor(cb, entityRowHandler);
    }

    // ===================================================================================
    //                                                                       Entity Select
    //                                                                       =============
    /**
     * Select the entity by the condition-bean.
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @return The selected entity. (Nullable: If the condition has no data, it returns null)
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public YsGroup selectEntity(final YsGroupCB cb) {
        return helpSelectEntityInternally(cb, new InternalSelectEntityCallback<YsGroup, YsGroupCB>() {
            public List<YsGroup> callbackSelectList(YsGroupCB cb) { return selectList(cb); } });
    }

    @Override
    protected Entity doReadEntity(ConditionBean cb) {
        return selectEntity(downcast(cb));
    }

    /**
     * Select the entity by the condition-bean with deleted check.
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public YsGroup selectEntityWithDeletedCheck(final YsGroupCB cb) {
        return helpSelectEntityWithDeletedCheckInternally(cb, new InternalSelectEntityWithDeletedCheckCallback<YsGroup, YsGroupCB>() {
            public List<YsGroup> callbackSelectList(YsGroupCB cb) { return selectList(cb); } });
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
    public YsGroup selectByPKValue(Long id) {
        return selectEntity(buildPKCB(id));
    }

    /**
     * Select the entity by the primary-key value with deleted check.
     * @param id The one of primary key. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public YsGroup selectByPKValueWithDeletedCheck(Long id) {
        return selectEntityWithDeletedCheck(buildPKCB(id));
    }

    private YsGroupCB buildPKCB(Long id) {
        assertObjectNotNull("id", id);
        YsGroupCB cb = newMyConditionBean();
        cb.query().setId_Equal(id);
        return cb;
    }

    // ===================================================================================
    //                                                                         List Select
    //                                                                         ===========
    /**
     * Select the list as result bean.
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @return The result bean of selected list. (NotNull)
     */
    public ListResultBean<YsGroup> selectList(YsGroupCB cb) {
        assertCBNotNull(cb);
        return new ResultBeanBuilder<YsGroup>(getTableDbName()).buildListResultBean(cb, delegateSelectList(cb));
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
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @return The result bean of selected page. (NotNull)
     */
    public PagingResultBean<YsGroup> selectPage(final YsGroupCB cb) {
        assertCBNotNull(cb);
        final PagingInvoker<YsGroup> invoker = new PagingInvoker<YsGroup>(getTableDbName());
        final PagingHandler<YsGroup> handler = new PagingHandler<YsGroup>() {
            public PagingBean getPagingBean() { return cb; }
            public int count() { return selectCount(cb); }
            public List<YsGroup> paging() { return selectList(cb); }
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
     * ysGroupBhv.scalarSelect(Date.class).max(new ScalarQuery(YsGroupCB cb) {
     *     cb.specify().columnXxxDatetime(); // the required specification of target column
     *     cb.query().setXxxName_PrefixSearch("S"); // query as you like it
     * });
     * </pre>
     * @param <RESULT> The type of result.
     * @param resultType The type of result. (NotNull)
     * @return The scalar value derived by a function. (Nullable)
     */
    public <RESULT> SLFunction<YsGroupCB, RESULT> scalarSelect(Class<RESULT> resultType) {
        YsGroupCB cb = newMyConditionBean();
        cb.xsetupForScalarSelect();
        cb.getSqlClause().disableSelectIndex(); // for when you use union
        return new SLFunction<YsGroupCB, RESULT>(cb, resultType);
    }

    // ===================================================================================
    //                                                                       Load Referrer
    //                                                                       =============
    /**
     * {Refer to overload method that has an argument of the list of entity.}
     * @param ysGroup The entity of ysGroup. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean setupper for registering referrer condition. (NotNull)
     */
    public void loadYsGroupUserList(YsGroup ysGroup, ConditionBeanSetupper<YsGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysGroup, conditionBeanSetupper);
        loadYsGroupUserList(xnewLRLs(ysGroup), conditionBeanSetupper);
    }
    /**
     * Load referrer of ysGroupUserList with the setupper for condition-bean of referrer. <br />
     * About internal policy, the value of primary key(and others too) is treated as case-insensitive. <br />
     * The condition-bean that the setupper provides have settings before you touch it. It is as follows:
     * <pre>
     * cb.query().setGroupId_InScope(pkList);
     * cb.query().addOrderBy_GroupId_Asc();
     * </pre>
     * @param ysGroupList The entity list of ysGroup. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean setupper for registering referrer condition. (NotNull)
     */
    public void loadYsGroupUserList(List<YsGroup> ysGroupList, ConditionBeanSetupper<YsGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysGroupList, conditionBeanSetupper);
        loadYsGroupUserList(ysGroupList, new LoadReferrerOption<YsGroupUserCB, YsGroupUser>(conditionBeanSetupper));
    }
    /**
     * {Refer to overload method that has an argument of the list of entity.}
     * @param ysGroup The entity of ysGroup. (NotNull)
     * @param loadReferrerOption The option of load-referrer. (NotNull)
     */
    public void loadYsGroupUserList(YsGroup ysGroup, LoadReferrerOption<YsGroupUserCB, YsGroupUser> loadReferrerOption) {
        xassLRArg(ysGroup, loadReferrerOption);
        loadYsGroupUserList(xnewLRLs(ysGroup), loadReferrerOption);
    }
    /**
     * {Refer to overload method that has an argument of condition-bean setupper.}
     * @param ysGroupList The entity list of ysGroup. (NotNull)
     * @param loadReferrerOption The option of load-referrer. (NotNull)
     */
    public void loadYsGroupUserList(List<YsGroup> ysGroupList, LoadReferrerOption<YsGroupUserCB, YsGroupUser> loadReferrerOption) {
        xassLRArg(ysGroupList, loadReferrerOption);
        if (ysGroupList.isEmpty()) { return; }
        final YsGroupUserBhv referrerBhv = xgetBSFLR().select(YsGroupUserBhv.class);
        helpLoadReferrerInternally(ysGroupList, loadReferrerOption, new InternalLoadReferrerCallback<YsGroup, Long, YsGroupUserCB, YsGroupUser>() {
            public Long getPKVal(YsGroup e) { return e.getId(); }
            public void setRfLs(YsGroup e, List<YsGroupUser> ls) { e.setYsGroupUserList(ls); }
            public YsGroupUserCB newMyCB() { return referrerBhv.newMyConditionBean(); }
            public void qyFKIn(YsGroupUserCB cb, List<Long> ls) { cb.query().setGroupId_InScope(ls); }
            public void qyOdFKAsc(YsGroupUserCB cb) { cb.query().addOrderBy_GroupId_Asc(); }
            public List<YsGroupUser> selRfLs(YsGroupUserCB cb) { return referrerBhv.selectList(cb); }
            public Long getFKVal(YsGroupUser e) { return e.getGroupId(); }
            public void setlcEt(YsGroupUser re, YsGroup le) { re.setYsGroup(le); }
        });
    }

    /**
     * {Refer to overload method that has an argument of the list of entity.}
     * @param ysGroup The entity of ysGroup. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean setupper for registering referrer condition. (NotNull)
     */
    public void loadYsRoleGroupUserList(YsGroup ysGroup, ConditionBeanSetupper<YsRoleGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysGroup, conditionBeanSetupper);
        loadYsRoleGroupUserList(xnewLRLs(ysGroup), conditionBeanSetupper);
    }
    /**
     * Load referrer of ysRoleGroupUserList with the setupper for condition-bean of referrer. <br />
     * About internal policy, the value of primary key(and others too) is treated as case-insensitive. <br />
     * The condition-bean that the setupper provides have settings before you touch it. It is as follows:
     * <pre>
     * cb.query().setGroupId_InScope(pkList);
     * cb.query().addOrderBy_GroupId_Asc();
     * </pre>
     * @param ysGroupList The entity list of ysGroup. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean setupper for registering referrer condition. (NotNull)
     */
    public void loadYsRoleGroupUserList(List<YsGroup> ysGroupList, ConditionBeanSetupper<YsRoleGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysGroupList, conditionBeanSetupper);
        loadYsRoleGroupUserList(ysGroupList, new LoadReferrerOption<YsRoleGroupUserCB, YsRoleGroupUser>(conditionBeanSetupper));
    }
    /**
     * {Refer to overload method that has an argument of the list of entity.}
     * @param ysGroup The entity of ysGroup. (NotNull)
     * @param loadReferrerOption The option of load-referrer. (NotNull)
     */
    public void loadYsRoleGroupUserList(YsGroup ysGroup, LoadReferrerOption<YsRoleGroupUserCB, YsRoleGroupUser> loadReferrerOption) {
        xassLRArg(ysGroup, loadReferrerOption);
        loadYsRoleGroupUserList(xnewLRLs(ysGroup), loadReferrerOption);
    }
    /**
     * {Refer to overload method that has an argument of condition-bean setupper.}
     * @param ysGroupList The entity list of ysGroup. (NotNull)
     * @param loadReferrerOption The option of load-referrer. (NotNull)
     */
    public void loadYsRoleGroupUserList(List<YsGroup> ysGroupList, LoadReferrerOption<YsRoleGroupUserCB, YsRoleGroupUser> loadReferrerOption) {
        xassLRArg(ysGroupList, loadReferrerOption);
        if (ysGroupList.isEmpty()) { return; }
        final YsRoleGroupUserBhv referrerBhv = xgetBSFLR().select(YsRoleGroupUserBhv.class);
        helpLoadReferrerInternally(ysGroupList, loadReferrerOption, new InternalLoadReferrerCallback<YsGroup, Long, YsRoleGroupUserCB, YsRoleGroupUser>() {
            public Long getPKVal(YsGroup e) { return e.getId(); }
            public void setRfLs(YsGroup e, List<YsRoleGroupUser> ls) { e.setYsRoleGroupUserList(ls); }
            public YsRoleGroupUserCB newMyCB() { return referrerBhv.newMyConditionBean(); }
            public void qyFKIn(YsRoleGroupUserCB cb, List<Long> ls) { cb.query().setGroupId_InScope(ls); }
            public void qyOdFKAsc(YsRoleGroupUserCB cb) { cb.query().addOrderBy_GroupId_Asc(); }
            public List<YsRoleGroupUser> selRfLs(YsRoleGroupUserCB cb) { return referrerBhv.selectList(cb); }
            public Long getFKVal(YsRoleGroupUser e) { return e.getGroupId(); }
            public void setlcEt(YsRoleGroupUser re, YsGroup le) { re.setYsGroup(le); }
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
     * @param ysGroup The entity of insert target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insert(YsGroup ysGroup) {
        assertEntityNotNull(ysGroup);
        delegateInsert(ysGroup);
    }

    @Override
    protected void doCreate(Entity entity) {
        insert(downcast(entity));
    }

    /**
     * Update the entity modified-only. {UpdateCountZeroException, ConcurrencyControl}
     * @param ysGroup The entity of update target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void update(final YsGroup ysGroup) {
        helpUpdateInternally(ysGroup, new InternalUpdateCallback<YsGroup>() {
            public int callbackDelegateUpdate(YsGroup entity) { return delegateUpdate(entity); } });
    }

    @Override
    protected void doModify(Entity entity) {
        update(downcast(entity));
    }
    
    /**
     * Update the entity non-strictly modified-only. {UpdateCountZeroException, NonConcurrencyControl}
     * @param ysGroup The entity of update target. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void updateNonstrict(final YsGroup ysGroup) {
        helpUpdateNonstrictInternally(ysGroup, new InternalUpdateNonstrictCallback<YsGroup>() {
            public int callbackDelegateUpdateNonstrict(YsGroup entity) { return delegateUpdateNonstrict(entity); } });
    }

    @Override
    protected void doModifyNonstrict(Entity entity) {
        updateNonstrict(downcast(entity));
    }

    /**
     * Insert or update the entity modified-only. {ConcurrencyControl(when update)}
     * @param ysGroup The entity of insert or update target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insertOrUpdate(final YsGroup ysGroup) {
        helpInsertOrUpdateInternally(ysGroup, new InternalInsertOrUpdateCallback<YsGroup, YsGroupCB>() {
            public void callbackInsert(YsGroup entity) { insert(entity); }
            public void callbackUpdate(YsGroup entity) { update(entity); }
            public YsGroupCB callbackNewMyConditionBean() { return newMyConditionBean(); }
            public int callbackSelectCount(YsGroupCB cb) { return selectCount(cb); }
        });
    }

    @Override
    protected void doCreateOrUpdate(Entity entity) {
        insertOrUpdate(downcast(entity));
    }

    /**
     * Insert or update the entity non-strictly modified-only. {NonConcurrencyControl(when update)}
     * @param ysGroup The entity of insert or update target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insertOrUpdateNonstrict(YsGroup ysGroup) {
        helpInsertOrUpdateInternally(ysGroup, new InternalInsertOrUpdateNonstrictCallback<YsGroup>() {
            public void callbackInsert(YsGroup entity) { insert(entity); }
            public void callbackUpdateNonstrict(YsGroup entity) { updateNonstrict(entity); }
        });
    }

    @Override
    protected void doCreateOrUpdateNonstrict(Entity entity) {
        insertOrUpdateNonstrict(downcast(entity));
    }

    /**
     * Delete the entity. {UpdateCountZeroException, ConcurrencyControl}
     * @param ysGroup The entity of delete target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void delete(YsGroup ysGroup) {
        helpDeleteInternally(ysGroup, new InternalDeleteCallback<YsGroup>() {
            public int callbackDelegateDelete(YsGroup entity) { return delegateDelete(entity); } });
    }

    @Override
    protected void doRemove(Entity entity) {
        delete(downcast(entity));
    }

    /**
     * Delete the entity non-strictly. {UpdateCountZeroException, NonConcurrencyControl}
     * @param ysGroup Entity. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void deleteNonstrict(YsGroup ysGroup) {
        helpDeleteNonstrictInternally(ysGroup, new InternalDeleteNonstrictCallback<YsGroup>() {
            public int callbackDelegateDeleteNonstrict(YsGroup entity) { return delegateDeleteNonstrict(entity); } });
    }

    /**
     * Delete the entity non-strictly ignoring deleted. {UpdateCountZeroException, NonConcurrencyControl}
     * @param ysGroup Entity. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void deleteNonstrictIgnoreDeleted(YsGroup ysGroup) {
        helpDeleteNonstrictIgnoreDeletedInternally(ysGroup, new InternalDeleteNonstrictIgnoreDeletedCallback<YsGroup>() {
            public int callbackDelegateDeleteNonstrict(YsGroup entity) { return delegateDeleteNonstrict(entity); } });
    }

    // ===================================================================================
    //                                                                        Batch Update
    //                                                                        ============
    /**
     * Batch insert the list. This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupList The list of the entity. (NotNull)
     * @return The array of inserted count.
     */
    public int[] batchInsert(List<YsGroup> ysGroupList) {
        assertObjectNotNull("ysGroupList", ysGroupList);
        return delegateInsertList(ysGroupList);
    }

    /**
     * Batch update the list. All columns are update target. {NOT modified only} <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchUpdate(List<YsGroup> ysGroupList) {
        assertObjectNotNull("ysGroupList", ysGroupList);
        return delegateUpdateList(ysGroupList);
    }

    /**
     * Batch update the list non-strictly. All columns are update target. {NOT modified only} <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchUpdateNonstrict(List<YsGroup> ysGroupList) {
        assertObjectNotNull("ysGroupList", ysGroupList);
        return delegateUpdateListNonstrict(ysGroupList);
    }

    /**
     * Batch delete the list. <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchDelete(List<YsGroup> ysGroupList) {
        assertObjectNotNull("ysGroupList", ysGroupList);
        return delegateDeleteList(ysGroupList);
    }

    /**
     * Batch delete the list non-strictly. <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchDeleteNonstrict(List<YsGroup> ysGroupList) {
        assertObjectNotNull("ysGroupList", ysGroupList);
        return delegateDeleteListNonstrict(ysGroupList);
    }

    // ===================================================================================
    //                                                                        Query Update
    //                                                                        ============
    /**
     * Query update the several entities. {NoConcurrencyControl}
     * @param ysGroup The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @return The updated count.
     */
    public int queryUpdate(YsGroup ysGroup, YsGroupCB cb) {
        assertObjectNotNull("ysGroup", ysGroup); assertCBNotNull(cb);
        setupCommonColumnOfUpdateIfNeeds(ysGroup);
        filterEntityOfUpdate(ysGroup); assertEntityOfUpdate(ysGroup);
        return invoke(createQueryUpdateEntityCBCommand(ysGroup, cb));
    }

    /**
     * Query delete the several entities. {NoConcurrencyControl}
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @return The deleted count.
     */
    public int queryDelete(YsGroupCB cb) {
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
    protected int delegateSelectCount(YsGroupCB cb) { return invoke(createSelectCountCBCommand(cb)); }
    protected void delegateSelectCursor(YsGroupCB cb, EntityRowHandler<YsGroup> entityRowHandler)
    { invoke(createSelectCursorCBCommand(cb, entityRowHandler, YsGroup.class)); }
    protected List<YsGroup> delegateSelectList(YsGroupCB cb)
    { return invoke(createSelectListCBCommand(cb, YsGroup.class)); }

    // -----------------------------------------------------
    //                                                Update
    //                                                ------
    protected int delegateInsert(YsGroup e)
    { if (!processBeforeInsert(e)) { return 1; } return invoke(createInsertEntityCommand(e)); }
    protected int delegateUpdate(YsGroup e)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createUpdateEntityCommand(e)); }
    protected int delegateUpdateNonstrict(YsGroup e)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createUpdateNonstrictEntityCommand(e)); }
    protected int delegateDelete(YsGroup e)
    { if (!processBeforeDelete(e)) { return 1; } return invoke(createDeleteEntityCommand(e)); }
    protected int delegateDeleteNonstrict(YsGroup e)
    { if (!processBeforeDelete(e)) { return 1; } return invoke(createDeleteNonstrictEntityCommand(e)); }

    protected int[] delegateInsertList(List<YsGroup> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchInsertEntityCommand(helpFilterBeforeInsertInternally(ls))); }
    @SuppressWarnings("unchecked")
    protected int[] doCreateList(List<Entity> ls) { return delegateInsertList((List)ls); }
    protected int[] delegateUpdateList(List<YsGroup> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchUpdateEntityCommand(helpFilterBeforeUpdateInternally(ls))); }
    @SuppressWarnings("unchecked")
    protected int[] doModifyList(List<Entity> ls) { return delegateUpdateList((List)ls); }
    protected int[] delegateUpdateListNonstrict(List<YsGroup> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchUpdateNonstrictEntityCommand(helpFilterBeforeUpdateInternally(ls))); }
    protected int[] delegateDeleteList(List<YsGroup> ls)
    { if (ls.isEmpty()) { return new int[]{}; } return invoke(createBatchDeleteEntityCommand(helpFilterBeforeDeleteInternally(ls))); }
    @SuppressWarnings("unchecked")
    protected int[] doRemoveList(List<Entity> ls) { return delegateDeleteList((List)ls); }
    protected int[] delegateDeleteListNonstrict(List<YsGroup> ls)
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
    protected YsGroup downcast(Entity entity) {
        return helpEntityDowncastInternally(entity, YsGroup.class);
    }

    protected YsGroupCB downcast(ConditionBean cb) {
        return helpConditionBeanDowncastInternally(cb, YsGroupCB.class);
    }
}
