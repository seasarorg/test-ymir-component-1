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
 * The behavior of (ユーザ)YS_USER that is TABLE. <br />
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
     * @param cb The condition-bean of YsUser. (NotNull)
     * @return The selected count.
     */
    public int selectCount(YsUserCB cb) {
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
     * @param cb The condition-bean of YsUser. (NotNull)
     * @param entityRowHandler The handler of entity row of YsUser. (NotNull)
     */
    public void selectCursor(YsUserCB cb, EntityRowHandler<YsUser> entityRowHandler) {
        assertCBNotNull(cb); assertObjectNotNull("entityRowHandler<YsUser>", entityRowHandler);
        delegateSelectCursor(cb, entityRowHandler);
    }

    // ===================================================================================
    //                                                                       Entity Select
    //                                                                       =============
    /**
     * Select the entity by the condition-bean.
     * @param cb The condition-bean of YsUser. (NotNull)
     * @return The selected entity. (Nullable: If the condition has no data, it returns null)
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public YsUser selectEntity(final YsUserCB cb) {
        return helpSelectEntityInternally(cb, new InternalSelectEntityCallback<YsUser, YsUserCB>() {
            public List<YsUser> callbackSelectList(YsUserCB cb) { return selectList(cb); } });
    }

    @Override
    protected Entity doReadEntity(ConditionBean cb) {
        return selectEntity(downcast(cb));
    }

    /**
     * Select the entity by the condition-bean with deleted check.
     * @param cb The condition-bean of YsUser. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public YsUser selectEntityWithDeletedCheck(final YsUserCB cb) {
        return helpSelectEntityWithDeletedCheckInternally(cb, new InternalSelectEntityWithDeletedCheckCallback<YsUser, YsUserCB>() {
            public List<YsUser> callbackSelectList(YsUserCB cb) { return selectList(cb); } });
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
    public YsUser selectByPKValue(Long id) {
        return selectEntity(buildPKCB(id));
    }

    /**
     * Select the entity by the primary-key value with deleted check.
     * @param id The one of primary key. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public YsUser selectByPKValueWithDeletedCheck(Long id) {
        return selectEntityWithDeletedCheck(buildPKCB(id));
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
     * @param cb The condition-bean of YsUser. (NotNull)
     * @return The result bean of selected list. (NotNull)
     */
    public ListResultBean<YsUser> selectList(YsUserCB cb) {
        assertCBNotNull(cb);
        return new ResultBeanBuilder<YsUser>(getTableDbName()).buildListResultBean(cb, delegateSelectList(cb));
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
     * @param cb The condition-bean of YsUser. (NotNull)
     * @return The result bean of selected page. (NotNull)
     */
    public PagingResultBean<YsUser> selectPage(final YsUserCB cb) {
        assertCBNotNull(cb);
        final PagingInvoker<YsUser> invoker = new PagingInvoker<YsUser>(getTableDbName());
        final PagingHandler<YsUser> handler = new PagingHandler<YsUser>() {
            public PagingBean getPagingBean() { return cb; }
            public int count() { return selectCount(cb); }
            public List<YsUser> paging() { return selectList(cb); }
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
     * ysUserBhv.scalarSelect(Date.class).max(new ScalarQuery(YsUserCB cb) {
     *     cb.specify().columnXxxDatetime(); // the required specification of target column
     *     cb.query().setXxxName_PrefixSearch("S"); // query as you like it
     * });
     * </pre>
     * @param <RESULT> The type of result.
     * @param resultType The type of result. (NotNull)
     * @return The scalar value derived by a function. (Nullable)
     */
    public <RESULT> SLFunction<YsUserCB, RESULT> scalarSelect(Class<RESULT> resultType) {
        YsUserCB cb = newMyConditionBean();
        cb.xsetupForScalarSelect();
        cb.getSqlClause().disableSelectIndex(); // for when you use union
        return new SLFunction<YsUserCB, RESULT>(cb, resultType);
    }

    // ===================================================================================
    //                                                                       Load Referrer
    //                                                                       =============
    /**
     * {Refer to overload method that has an argument of the list of entity.}
     * @param ysUser The entity of ysUser. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean setupper for registering referrer condition. (NotNull)
     */
    public void loadYsGroupUserList(YsUser ysUser, ConditionBeanSetupper<YsGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysUser, conditionBeanSetupper);
        loadYsGroupUserList(xnewLRLs(ysUser), conditionBeanSetupper);
    }
    /**
     * Load referrer of ysGroupUserList with the setupper for condition-bean of referrer. <br />
     * About internal policy, the value of primary key(and others too) is treated as case-insensitive. <br />
     * The condition-bean that the setupper provides have settings before you touch it. It is as follows:
     * <pre>
     * cb.query().setUserId_InScope(pkList);
     * cb.query().addOrderBy_UserId_Asc();
     * </pre>
     * @param ysUserList The entity list of ysUser. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean setupper for registering referrer condition. (NotNull)
     */
    public void loadYsGroupUserList(List<YsUser> ysUserList, ConditionBeanSetupper<YsGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysUserList, conditionBeanSetupper);
        loadYsGroupUserList(ysUserList, new LoadReferrerOption<YsGroupUserCB, YsGroupUser>(conditionBeanSetupper));
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
            public List<YsGroupUser> selRfLs(YsGroupUserCB cb) { return referrerBhv.selectList(cb); }
            public Long getFKVal(YsGroupUser e) { return e.getUserId(); }
            public void setlcEt(YsGroupUser re, YsUser le) { re.setYsUser(le); }
        });
    }

    /**
     * {Refer to overload method that has an argument of the list of entity.}
     * @param ysUser The entity of ysUser. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean setupper for registering referrer condition. (NotNull)
     */
    public void loadYsRoleGroupUserList(YsUser ysUser, ConditionBeanSetupper<YsRoleGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysUser, conditionBeanSetupper);
        loadYsRoleGroupUserList(xnewLRLs(ysUser), conditionBeanSetupper);
    }
    /**
     * Load referrer of ysRoleGroupUserList with the setupper for condition-bean of referrer. <br />
     * About internal policy, the value of primary key(and others too) is treated as case-insensitive. <br />
     * The condition-bean that the setupper provides have settings before you touch it. It is as follows:
     * <pre>
     * cb.query().setUserId_InScope(pkList);
     * cb.query().addOrderBy_UserId_Asc();
     * </pre>
     * @param ysUserList The entity list of ysUser. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean setupper for registering referrer condition. (NotNull)
     */
    public void loadYsRoleGroupUserList(List<YsUser> ysUserList, ConditionBeanSetupper<YsRoleGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysUserList, conditionBeanSetupper);
        loadYsRoleGroupUserList(ysUserList, new LoadReferrerOption<YsRoleGroupUserCB, YsRoleGroupUser>(conditionBeanSetupper));
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
     * Update the entity modified-only. {UpdateCountZeroException, ConcurrencyControl}
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
     * Update the entity non-strictly modified-only. {UpdateCountZeroException, NonConcurrencyControl}
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
     * Insert or update the entity modified-only. {ConcurrencyControl(when update)}
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
     * Insert or update the entity non-strictly modified-only. {NonConcurrencyControl(when update)}
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
     * Delete the entity. {UpdateCountZeroException, ConcurrencyControl}
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
     * Delete the entity non-strictly. {UpdateCountZeroException, NonConcurrencyControl}
     * @param ysUser Entity. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void deleteNonstrict(YsUser ysUser) {
        helpDeleteNonstrictInternally(ysUser, new InternalDeleteNonstrictCallback<YsUser>() {
            public int callbackDelegateDeleteNonstrict(YsUser entity) { return delegateDeleteNonstrict(entity); } });
    }

    /**
     * Delete the entity non-strictly ignoring deleted. {UpdateCountZeroException, NonConcurrencyControl}
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
     * Batch insert the list. This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysUserList The list of the entity. (NotNull)
     * @return The array of inserted count.
     */
    public int[] batchInsert(List<YsUser> ysUserList) {
        assertObjectNotNull("ysUserList", ysUserList);
        return delegateInsertList(ysUserList);
    }

    /**
     * Batch update the list. All columns are update target. {NOT modified only} <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysUserList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchUpdate(List<YsUser> ysUserList) {
        assertObjectNotNull("ysUserList", ysUserList);
        return delegateUpdateList(ysUserList);
    }

    /**
     * Batch update the list non-strictly. All columns are update target. {NOT modified only} <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysUserList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchUpdateNonstrict(List<YsUser> ysUserList) {
        assertObjectNotNull("ysUserList", ysUserList);
        return delegateUpdateListNonstrict(ysUserList);
    }

    /**
     * Batch delete the list. <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
     * @param ysUserList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchDelete(List<YsUser> ysUserList) {
        assertObjectNotNull("ysUserList", ysUserList);
        return delegateDeleteList(ysUserList);
    }

    /**
     * Batch delete the list non-strictly. <br />
     * This method use 'Batch Update' of java.sql.PreparedStatement.
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
     * Query update the several entities. {NoConcurrencyControl}
     * @param ysUser The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of YsUser. (NotNull)
     * @return The updated count.
     */
    public int queryUpdate(YsUser ysUser, YsUserCB cb) {
        assertObjectNotNull("ysUser", ysUser); assertCBNotNull(cb);
        setupCommonColumnOfUpdateIfNeeds(ysUser);
        filterEntityOfUpdate(ysUser); assertEntityOfUpdate(ysUser);
        return invoke(createQueryUpdateEntityCBCommand(ysUser, cb));
    }

    /**
     * Query delete the several entities. {NoConcurrencyControl}
     * @param cb The condition-bean of YsUser. (NotNull)
     * @return The deleted count.
     */
    public int queryDelete(YsUserCB cb) {
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
    protected int delegateSelectCount(YsUserCB cb) { return invoke(createSelectCountCBCommand(cb)); }
    protected void delegateSelectCursor(YsUserCB cb, EntityRowHandler<YsUser> entityRowHandler)
    { invoke(createSelectCursorCBCommand(cb, entityRowHandler, YsUser.class)); }
    protected List<YsUser> delegateSelectList(YsUserCB cb)
    { return invoke(createSelectListCBCommand(cb, YsUser.class)); }

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
