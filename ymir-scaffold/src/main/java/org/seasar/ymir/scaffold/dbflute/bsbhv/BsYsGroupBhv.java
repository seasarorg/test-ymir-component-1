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
 * The behavior of (グループ)YS_GROUP as TABLE. <br />
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
     * <pre>
     * YsGroupCB cb = new YsGroupCB();
     * cb.query().setFoo...(value);
     * int count = ysGroupBhv.<span style="color: #FD4747">selectCount</span>(cb);
     * </pre>
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @return The selected count.
     */
    public int selectCount(YsGroupCB cb) {
        return doSelectCount(cb);
    }

    protected int doSelectCount(YsGroupCB cb) {
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
     * YsGroupCB cb = new YsGroupCB();
     * cb.query().setFoo...(value);
     * ysGroupBhv.<span style="color: #FD4747">selectCursor</span>(cb, new EntityRowHandler&lt;YsGroup&gt;() {
     *     public void handle(YsGroup entity) {
     *         ... = entity.getFoo...();
     *     }
     * });
     * </pre>
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @param entityRowHandler The handler of entity row of YsGroup. (NotNull)
     */
    public void selectCursor(YsGroupCB cb, EntityRowHandler<YsGroup> entityRowHandler) {
        doSelectCursor(cb, entityRowHandler, YsGroup.class);
    }

    protected <ENTITY extends YsGroup> void doSelectCursor(YsGroupCB cb, EntityRowHandler<ENTITY> entityRowHandler, Class<ENTITY> entityType) {
        assertCBNotNull(cb); assertObjectNotNull("entityRowHandler<YsGroup>", entityRowHandler); assertObjectNotNull("entityType", entityType);
        assertSpecifyDerivedReferrerEntityProperty(cb, entityType);
        delegateSelectCursor(cb, entityRowHandler, entityType);
    }

    // ===================================================================================
    //                                                                       Entity Select
    //                                                                       =============
    /**
     * Select the entity by the condition-bean.
     * <pre>
     * YsGroupCB cb = new YsGroupCB();
     * cb.query().setFoo...(value);
     * YsGroup ysGroup = ysGroupBhv.<span style="color: #FD4747">selectEntity</span>(cb);
     * if (ysGroup != null) {
     *     ... = ysGroup.get...();
     * } else {
     *     ...
     * }
     * </pre>
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @return The selected entity. (Nullable: If the condition has no data, it returns null)
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public YsGroup selectEntity(YsGroupCB cb) {
        return doSelectEntity(cb, YsGroup.class);
    }

    protected <ENTITY extends YsGroup> ENTITY doSelectEntity(final YsGroupCB cb, final Class<ENTITY> entityType) {
        return helpSelectEntityInternally(cb, new InternalSelectEntityCallback<ENTITY, YsGroupCB>() {
            public List<ENTITY> callbackSelectList(YsGroupCB cb) { return doSelectList(cb, entityType); } });
    }

    @Override
    protected Entity doReadEntity(ConditionBean cb) {
        return selectEntity(downcast(cb));
    }

    /**
     * Select the entity by the condition-bean with deleted check.
     * <pre>
     * YsGroupCB cb = new YsGroupCB();
     * cb.query().setFoo...(value);
     * YsGroup ysGroup = ysGroupBhv.<span style="color: #FD4747">selectEntityWithDeletedCheck</span>(cb);
     * ... = ysGroup.get...(); <span style="color: #3F7E5E">// the entity always be not null</span>
     * </pre>
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public YsGroup selectEntityWithDeletedCheck(YsGroupCB cb) {
        return doSelectEntityWithDeletedCheck(cb, YsGroup.class);
    }

    protected <ENTITY extends YsGroup> ENTITY doSelectEntityWithDeletedCheck(final YsGroupCB cb, final Class<ENTITY> entityType) {
        return helpSelectEntityWithDeletedCheckInternally(cb, new InternalSelectEntityWithDeletedCheckCallback<ENTITY, YsGroupCB>() {
            public List<ENTITY> callbackSelectList(YsGroupCB cb) { return doSelectList(cb, entityType); } });
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
    public YsGroup selectByPKValue(Long id) {
        return doSelectByPKValue(id, YsGroup.class);
    }

    protected <ENTITY extends YsGroup> ENTITY doSelectByPKValue(Long id, Class<ENTITY> entityType) {
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
    public YsGroup selectByPKValueWithDeletedCheck(Long id) {
        return doSelectByPKValueWithDeletedCheck(id, YsGroup.class);
    }

    protected <ENTITY extends YsGroup> ENTITY doSelectByPKValueWithDeletedCheck(Long id, Class<ENTITY> entityType) {
        return doSelectEntityWithDeletedCheck(buildPKCB(id), entityType);
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
     * <pre>
     * YsGroupCB cb = new YsGroupCB();
     * cb.query().setFoo...(value);
     * cb.query().addOrderBy_Bar...();
     * ListResultBean&lt;YsGroup&gt; ysGroupList = ysGroupBhv.<span style="color: #FD4747">selectList</span>(cb);
     * for (YsGroup ysGroup : ysGroupList) {
     *     ... = ysGroup.get...();
     * }
     * </pre>
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @return The result bean of selected list. (NotNull)
     * @exception org.seasar.dbflute.exception.DangerousResultSizeException When the result size is over the specified safety size.
     */
    public ListResultBean<YsGroup> selectList(YsGroupCB cb) {
        return doSelectList(cb, YsGroup.class);
    }

    protected <ENTITY extends YsGroup> ListResultBean<ENTITY> doSelectList(YsGroupCB cb, Class<ENTITY> entityType) {
        assertCBNotNull(cb); assertObjectNotNull("entityType", entityType);
        assertSpecifyDerivedReferrerEntityProperty(cb, entityType);
        return helpSelectListInternally(cb, entityType, new InternalSelectListCallback<ENTITY, YsGroupCB>() {
            public List<ENTITY> callbackSelectList(YsGroupCB cb, Class<ENTITY> entityType) { return delegateSelectList(cb, entityType); } });
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
     * YsGroupCB cb = new YsGroupCB();
     * cb.query().setFoo...(value);
     * cb.query().addOrderBy_Bar...();
     * cb.<span style="color: #FD4747">paging</span>(20, 3); <span style="color: #3F7E5E">// 20 records per a page and current page number is 3</span>
     * PagingResultBean&lt;YsGroup&gt; page = ysGroupBhv.<span style="color: #FD4747">selectPage</span>(cb);
     * int allRecordCount = page.getAllRecordCount();
     * int allPageCount = page.getAllPageCount();
     * boolean isExistPrePage = page.isExistPrePage();
     * boolean isExistNextPage = page.isExistNextPage();
     * ...
     * for (YsGroup ysGroup : page) {
     *     ... = ysGroup.get...();
     * }
     * </pre>
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @return The result bean of selected page. (NotNull)
     * @exception org.seasar.dbflute.exception.DangerousResultSizeException When the result size is over the specified safety size.
     */
    public PagingResultBean<YsGroup> selectPage(YsGroupCB cb) {
        return doSelectPage(cb, YsGroup.class);
    }

    protected <ENTITY extends YsGroup> PagingResultBean<ENTITY> doSelectPage(YsGroupCB cb, Class<ENTITY> entityType) {
        assertCBNotNull(cb); assertObjectNotNull("entityType", entityType);
        return helpSelectPageInternally(cb, entityType, new InternalSelectPageCallback<ENTITY, YsGroupCB>() {
            public int callbackSelectCount(YsGroupCB cb) { return doSelectCount(cb); }
            public List<ENTITY> callbackSelectList(YsGroupCB cb, Class<ENTITY> entityType) { return doSelectList(cb, entityType); }
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
     * ysGroupBhv.<span style="color: #FD4747">scalarSelect</span>(Date.class).max(new ScalarQuery() {
     *     public void query(YsGroupCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooDatetime()</span>; <span style="color: #3F7E5E">// required for a function</span>
     *         cb.query().setBarName_PrefixSearch("S");
     *     }
     * });
     * </pre>
     * @param <RESULT> The type of result.
     * @param resultType The type of result. (NotNull)
     * @return The scalar value derived by a function. (Nullable)
     */
    public <RESULT> SLFunction<YsGroupCB, RESULT> scalarSelect(Class<RESULT> resultType) {
        return doScalarSelect(resultType, newMyConditionBean());
    }

    protected <RESULT, CB extends YsGroupCB> SLFunction<CB, RESULT> doScalarSelect(Class<RESULT> resultType, CB cb) {
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
     * @param ysGroup The entity of ysGroup. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean set-upper for registering referrer condition. (NotNull)
     */
    public void loadYsGroupUserList(YsGroup ysGroup, ConditionBeanSetupper<YsGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysGroup, conditionBeanSetupper);
        loadYsGroupUserList(xnewLRLs(ysGroup), conditionBeanSetupper);
    }
    /**
     * Load referrer of ysGroupUserList with the set-upper for condition-bean of referrer.
     * <pre>
     * ysGroupBhv.<span style="color: #FD4747">loadYsGroupUserList</span>(ysGroupList, new ConditionBeanSetupper&lt;YsGroupUserCB&gt;() {
     *     public void setup(YsGroupUserCB cb) {
     *         cb.setupSelect...();
     *         cb.query().setFoo...(value);
     *         cb.query().addOrderBy_Bar...(); <span style="color: #3F7E5E">// basically you should order referrer list</span>
     *     }
     * });
     * for (YsGroup ysGroup : ysGroupList) {
     *     ... = ysGroup.<span style="color: #FD4747">getYsGroupUserList()</span>;
     * }
     * </pre>
     * About internal policy, the value of primary key(and others too) is treated as case-insensitive. <br />
     * The condition-bean that the set-upper provides have settings before you touch it. It is as follows:
     * <pre>
     * cb.query().setGroupId_InScope(pkList);
     * cb.query().addOrderBy_GroupId_Asc();
     * </pre>
     * @param ysGroupList The entity list of ysGroup. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean set-upper for registering referrer condition. (NotNull)
     */
    public void loadYsGroupUserList(List<YsGroup> ysGroupList, ConditionBeanSetupper<YsGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysGroupList, conditionBeanSetupper);
        loadYsGroupUserList(ysGroupList, new LoadReferrerOption<YsGroupUserCB, YsGroupUser>().xinit(conditionBeanSetupper));
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
            public void spFKCol(YsGroupUserCB cb) { cb.specify().columnGroupId(); }
            public List<YsGroupUser> selRfLs(YsGroupUserCB cb) { return referrerBhv.selectList(cb); }
            public Long getFKVal(YsGroupUser e) { return e.getGroupId(); }
            public void setlcEt(YsGroupUser re, YsGroup le) { re.setYsGroup(le); }
        });
    }

    /**
     * {Refer to overload method that has an argument of the list of entity.}
     * @param ysGroup The entity of ysGroup. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean set-upper for registering referrer condition. (NotNull)
     */
    public void loadYsRoleGroupUserList(YsGroup ysGroup, ConditionBeanSetupper<YsRoleGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysGroup, conditionBeanSetupper);
        loadYsRoleGroupUserList(xnewLRLs(ysGroup), conditionBeanSetupper);
    }
    /**
     * Load referrer of ysRoleGroupUserList with the set-upper for condition-bean of referrer.
     * <pre>
     * ysGroupBhv.<span style="color: #FD4747">loadYsRoleGroupUserList</span>(ysGroupList, new ConditionBeanSetupper&lt;YsRoleGroupUserCB&gt;() {
     *     public void setup(YsRoleGroupUserCB cb) {
     *         cb.setupSelect...();
     *         cb.query().setFoo...(value);
     *         cb.query().addOrderBy_Bar...(); <span style="color: #3F7E5E">// basically you should order referrer list</span>
     *     }
     * });
     * for (YsGroup ysGroup : ysGroupList) {
     *     ... = ysGroup.<span style="color: #FD4747">getYsRoleGroupUserList()</span>;
     * }
     * </pre>
     * About internal policy, the value of primary key(and others too) is treated as case-insensitive. <br />
     * The condition-bean that the set-upper provides have settings before you touch it. It is as follows:
     * <pre>
     * cb.query().setGroupId_InScope(pkList);
     * cb.query().addOrderBy_GroupId_Asc();
     * </pre>
     * @param ysGroupList The entity list of ysGroup. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean set-upper for registering referrer condition. (NotNull)
     */
    public void loadYsRoleGroupUserList(List<YsGroup> ysGroupList, ConditionBeanSetupper<YsRoleGroupUserCB> conditionBeanSetupper) {
        xassLRArg(ysGroupList, conditionBeanSetupper);
        loadYsRoleGroupUserList(ysGroupList, new LoadReferrerOption<YsRoleGroupUserCB, YsRoleGroupUser>().xinit(conditionBeanSetupper));
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
            public void spFKCol(YsRoleGroupUserCB cb) { cb.specify().columnGroupId(); }
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
     * <pre>
     * YsGroup ysGroup = new YsGroup();
     * <span style="color: #3F7E5E">// if auto-increment, you don't need to set the PK value</span>
     * ysGroup.setFoo...(value);
     * ysGroup.setBar...(value);
     * ysGroupBhv.<span style="color: #FD4747">insert</span>(ysGroup);
     * ... = ysGroup.getPK...(); <span style="color: #3F7E5E">// if auto-increment, you can get the value after</span>
     * </pre>
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
     * Update the entity modified-only. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * YsGroup ysGroup = new YsGroup();
     * ysGroup.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysGroup.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * ysGroup.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     ysGroupBhv.<span style="color: #FD4747">update</span>(ysGroup);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * } 
     * </pre>
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
     * Update the entity non-strictly modified-only. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsGroup ysGroup = new YsGroup();
     * ysGroup.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysGroup.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysGroup.setVersionNo(value);</span>
     * ysGroupBhv.<span style="color: #FD4747">updateNonstrict</span>(ysGroup);
     * </pre>
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
     * Insert or update the entity modified-only. {ExclusiveControl(when update)}
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
     * Insert or update the entity non-strictly modified-only. {NonExclusiveControl(when update)}
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
     * Delete the entity. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * YsGroup ysGroup = new YsGroup();
     * ysGroup.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * ysGroup.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     ysGroupBhv.<span style="color: #FD4747">delete</span>(ysGroup);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * } 
     * </pre>
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
     * Delete the entity non-strictly. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsGroup ysGroup = new YsGroup();
     * ysGroup.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysGroup.setVersionNo(value);</span>
     * ysGroupBhv.<span style="color: #FD4747">deleteNonstrict</span>(ysGroup);
     * </pre>
     * @param ysGroup Entity. (NotNull) {PrimaryKeyRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void deleteNonstrict(YsGroup ysGroup) {
        helpDeleteNonstrictInternally(ysGroup, new InternalDeleteNonstrictCallback<YsGroup>() {
            public int callbackDelegateDeleteNonstrict(YsGroup entity) { return delegateDeleteNonstrict(entity); } });
    }

    /**
     * Delete the entity non-strictly ignoring deleted. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsGroup ysGroup = new YsGroup();
     * ysGroup.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysGroup.setVersionNo(value);</span>
     * ysGroupBhv.<span style="color: #FD4747">deleteNonstrictIgnoreDeleted</span>(ysGroup);
     * <span style="color: #3F7E5E">// if the target entity doesn't exist, no exception</span>
     * </pre>
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
     * Batch-insert the list. This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupList The list of the entity. (NotNull)
     * @return The array of inserted count.
     */
    public int[] batchInsert(List<YsGroup> ysGroupList) {
        assertObjectNotNull("ysGroupList", ysGroupList);
        return delegateInsertList(ysGroupList);
    }

    /**
     * Batch-update the list. <br />
     * All columns are update target. {NOT modified only} <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchUpdate(List<YsGroup> ysGroupList) {
        assertObjectNotNull("ysGroupList", ysGroupList);
        return delegateUpdateList(ysGroupList);
    }

    /**
     * Batch-update the list non-strictly. <br />
     * All columns are update target. {NOT modified only} <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchUpdateNonstrict(List<YsGroup> ysGroupList) {
        assertObjectNotNull("ysGroupList", ysGroupList);
        return delegateUpdateListNonstrict(ysGroupList);
    }

    /**
     * Batch-delete the list. <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param ysGroupList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException When the entity has already been updated. This exception extends EntityAlreadyUpdatedException.
     */
    public int[] batchDelete(List<YsGroup> ysGroupList) {
        assertObjectNotNull("ysGroupList", ysGroupList);
        return delegateDeleteList(ysGroupList);
    }

    /**
     * Batch-delete the list non-strictly. <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
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
     * Query-update the several entities non-strictly modified-only. {NonExclusiveControl}
     * <pre>
     * YsGroup ysGroup = new YsGroup();
     * <span style="color: #3F7E5E">// you don't need to set PK value</span>
     * <span style="color: #3F7E5E">//ysGroup.setPK...(value);</span>
     * ysGroup.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysGroup.setVersionNo(value);</span>
     * YsGroupCB cb = new YsGroupCB();
     * cb.query().setFoo...(value);
     * ysGroupBhv.<span style="color: #FD4747">queryUpdate</span>(ysGroup, cb);
     * </pre>
     * @param ysGroup The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @return The updated count.
     */
    public int queryUpdate(YsGroup ysGroup, YsGroupCB cb) {
        return delegateQueryUpdate(ysGroup, cb);
    }

    /**
     * Query-delete the several entities. {NonExclusiveControl}
     * <pre>
     * YsGroupCB cb = new YsGroupCB();
     * cb.query().setFoo...(value);
     * ysGroupBhv.<span style="color: #FD4747">queryDelete</span>(ysGroup, cb);
     * </pre>
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @return The deleted count.
     */
    public int queryDelete(YsGroupCB cb) {
        return delegateQueryDelete(cb);
    }

    /**
     * Varying-update the entity modified-only. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * YsGroup ysGroup = new YsGroup();
     * ysGroup.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysGroup.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * ysGroup.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     UpdateOption&lt;YsGroupCB&gt; option = new UpdateOption&lt;YsGroupCB&gt;();
     *     option.self(new SpecifyQuery&lt;YsGroupCB&gt;() {
     *         public void specify(YsGroupCB cb) {
     *             cb.specify().<span style="color: #FD4747">columnXxxCount()</span>;
     *         }
     *     }).plus(1); <span style="color: #3F7E5E">// XXX_COUNT = XXX_COUNT + 1</span>
     *     ysGroupBhv.<span style="color: #FD4747">varyingUpdate</span>(ysGroup, option);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * }
     * </pre>
     * @param ysGroup The entity of update target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @param option The option of update for varying values. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyUpdatedException When the entity has already been updated.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void varyingUpdate(YsGroup ysGroup, final UpdateOption<YsGroupCB> option) {
        processVaryingUpdate(option);
        helpUpdateInternally(ysGroup, new InternalUpdateCallback<YsGroup>() {
            public int callbackDelegateUpdate(YsGroup entity) { return delegateVaryingUpdate(entity, option); } });
    }

    /**
     * Varying-update the entity non-strictly modified-only. {UpdateCountZeroException, NonExclusiveControl}
     * <pre>
     * YsGroup ysGroup = new YsGroup();
     * ysGroup.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * ysGroup.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysGroup.setVersionNo(value);</span>
     * UpdateOption&lt;YsGroupCB&gt; option = new UpdateOption&lt;YsGroupCB&gt;();
     * option.self(new SpecifyQuery&lt;YsGroupCB&gt;() {
     *     public void specify(YsGroupCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooCount()</span>;
     *     }
     * }).plus(1); <span style="color: #3F7E5E">// FOO_COUNT = FOO_COUNT + 1</span>
     * ysGroupBhv.<span style="color: #FD4747">varyingUpdateNonstrict</span>(ysGroup, option);
     * </pre>
     * @param ysGroup The entity of update target. (NotNull) {PrimaryKeyRequired}
     * @param option The option of update for varying values. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void varyingUpdateNonstrict(YsGroup ysGroup, final UpdateOption<YsGroupCB> option) {
        processVaryingUpdate(option);
        helpUpdateNonstrictInternally(ysGroup, new InternalUpdateNonstrictCallback<YsGroup>() {
            public int callbackDelegateUpdateNonstrict(YsGroup entity) { return delegateVaryingUpdateNonstrict(entity, option); } });
    }

    /**
     * Varying-query-update the several entities non-strictly modified-only. {NonExclusiveControl}
     * <pre>
     * YsGroup ysGroup = new YsGroup();
     * <span style="color: #3F7E5E">// you don't need to set PK value</span>
     * <span style="color: #3F7E5E">//ysGroup.setPK...(value);</span>
     * ysGroup.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set the value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//ysGroup.setVersionNo(value);</span>
     * YsGroupCB cb = new YsGroupCB();
     * cb.query().setFoo...(value);
     * UpdateOption&lt;YsGroupCB&gt; option = new UpdateOption&lt;YsGroupCB&gt;();
     * option.self(new SpecifyQuery&lt;YsGroupCB&gt;() {
     *     public void specify(YsGroupCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooCount()</span>;
     *     }
     * }).plus(1); <span style="color: #3F7E5E">// FOO_COUNT = FOO_COUNT + 1</span>
     * ysGroupBhv.<span style="color: #FD4747">varyingQueryUpdate</span>(ysGroup, cb, option);
     * </pre>
     * @param ysGroup The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of YsGroup. (NotNull)
     * @param option The option of update for varying values. (NotNull)
     * @return The updated count.
     */
    public int varyingQueryUpdate(YsGroup ysGroup, YsGroupCB cb, final UpdateOption<YsGroupCB> option) {
        processVaryingUpdate(option);
        return delegateVaryingQueryUpdate(ysGroup, cb, option);
    }

    protected void processVaryingUpdate(UpdateOption<YsGroupCB> option) {
        assertUpdateOptionNotNull(option);
        YsGroupCB cb = newMyConditionBean();
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
    protected int delegateSelectCount(YsGroupCB cb) { return invoke(createSelectCountCBCommand(cb)); }
    protected <ENTITY extends YsGroup> void delegateSelectCursor(YsGroupCB cb, EntityRowHandler<ENTITY> entityRowHandler, Class<ENTITY> entityType)
    { invoke(createSelectCursorCBCommand(cb, entityRowHandler, entityType)); }
    protected <ENTITY extends YsGroup> List<ENTITY> delegateSelectList(YsGroupCB cb, Class<ENTITY> entityType)
    { return invoke(createSelectListCBCommand(cb, entityType)); }

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

    protected int delegateQueryUpdate(YsGroup e, YsGroupCB cb)
    { if (!processBeforeQueryUpdate(e, cb)) { return 0; } return invoke(createQueryUpdateEntityCBCommand(e, cb));  }
    protected int delegateQueryDelete(YsGroupCB cb)
    { if (!processBeforeQueryDelete(cb)) { return 0; } return invoke(createQueryDeleteCBCommand(cb));  }

    protected int delegateVaryingUpdate(YsGroup e, UpdateOption<YsGroupCB> op)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createVaryingUpdateEntityCommand(e, op)); }
    protected int delegateVaryingUpdateNonstrict(YsGroup e, UpdateOption<YsGroupCB> op)
    { if (!processBeforeUpdate(e)) { return 1; } return invoke(createVaryingUpdateNonstrictEntityCommand(e, op)); }
    protected int delegateVaryingQueryUpdate(YsGroup e, YsGroupCB cb, UpdateOption<YsGroupCB> op)
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
    protected YsGroup downcast(Entity entity) {
        return helpEntityDowncastInternally(entity, YsGroup.class);
    }

    protected YsGroupCB downcast(ConditionBean cb) {
        return helpConditionBeanDowncastInternally(cb, YsGroupCB.class);
    }
}
