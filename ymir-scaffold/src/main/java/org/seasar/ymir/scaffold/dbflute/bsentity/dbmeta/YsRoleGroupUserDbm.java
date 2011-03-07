package org.seasar.ymir.scaffold.dbflute.bsentity.dbmeta;

import java.util.List;
import java.util.Map;

import org.seasar.dbflute.DBDef;
import org.seasar.dbflute.Entity;
import org.seasar.dbflute.dbmeta.AbstractDBMeta;
import org.seasar.dbflute.dbmeta.info.*;
import org.seasar.dbflute.dbmeta.name.*;
import org.seasar.dbflute.helper.StringKeyMap;
import org.seasar.ymir.scaffold.dbflute.allcommon.*;
import org.seasar.ymir.scaffold.dbflute.exentity.*;

/**
 * The DB meta of YS_ROLE_GROUP_USER. (Singleton)
 * @author DBFlute(AutoGenerator)
 */
public class YsRoleGroupUserDbm extends AbstractDBMeta {

    // ===================================================================================
    //                                                                           Singleton
    //                                                                           =========
    private static final YsRoleGroupUserDbm _instance = new YsRoleGroupUserDbm();
    private YsRoleGroupUserDbm() {}
    public static YsRoleGroupUserDbm getInstance() { return _instance; }

    // ===================================================================================
    //                                                                       Current DBDef
    //                                                                       =============
    public DBDef getCurrentDBDef() { return DBCurrent.getInstance().currentDBDef(); }

    // ===================================================================================
    //                                                                          Table Info
    //                                                                          ==========
    protected final String _tableDbName = "YS_ROLE_GROUP_USER";
    protected final String _tablePropertyName = "ysRoleGroupUser";
    protected final TableSqlName _tableSqlName = new TableSqlName("YS_ROLE_GROUP_USER", _tableDbName);
    { _tableSqlName.xacceptFilter(DBFluteConfig.getInstance().getTableSqlNameFilter()); }
    public String getTableDbName() { return _tableDbName; }
    public String getTablePropertyName() { return _tablePropertyName; }
    public TableSqlName getTableSqlName() { return _tableSqlName; }

    // ===================================================================================
    //                                                                         Column Info
    //                                                                         ===========
    protected final ColumnInfo _columnId = cci("ID", "ID", null, null, true, "id", Long.class, true, true, "BIGINT", 19, 0, false, null, null, null, null, null);
    protected final ColumnInfo _columnRoleId = cci("ROLE_ID", "ROLE_ID", null, null, true, "roleId", Long.class, false, false, "BIGINT", 19, 0, false, null, null, "ysRole", null, null);
    protected final ColumnInfo _columnGroupId = cci("GROUP_ID", "GROUP_ID", null, null, false, "groupId", Long.class, false, false, "BIGINT", 19, 0, false, null, null, "ysGroup", null, null);
    protected final ColumnInfo _columnUserId = cci("USER_ID", "USER_ID", null, null, false, "userId", Long.class, false, false, "BIGINT", 19, 0, false, null, null, "ysUser", null, null);
    protected final ColumnInfo _columnCreatedDate = cci("CREATED_DATE", "CREATED_DATE", null, null, true, "createdDate", java.sql.Timestamp.class, false, false, "TIMESTAMP", 23, 10, false, null, null, null, null, null);
    protected final ColumnInfo _columnModifiedDate = cci("MODIFIED_DATE", "MODIFIED_DATE", null, null, true, "modifiedDate", java.sql.Timestamp.class, false, false, "TIMESTAMP", 23, 10, false, null, null, null, null, null);
    protected final ColumnInfo _columnVersionNo = cci("VERSION_NO", "VERSION_NO", null, null, true, "versionNo", Long.class, false, false, "BIGINT", 19, 0, false, OptimisticLockType.VERSION_NO, null, null, null, null);

    public ColumnInfo columnId() { return _columnId; }
    public ColumnInfo columnRoleId() { return _columnRoleId; }
    public ColumnInfo columnGroupId() { return _columnGroupId; }
    public ColumnInfo columnUserId() { return _columnUserId; }
    public ColumnInfo columnCreatedDate() { return _columnCreatedDate; }
    public ColumnInfo columnModifiedDate() { return _columnModifiedDate; }
    public ColumnInfo columnVersionNo() { return _columnVersionNo; }

    protected List<ColumnInfo> ccil() {
        List<ColumnInfo> ls = newArrayList();
        ls.add(columnId());
        ls.add(columnRoleId());
        ls.add(columnGroupId());
        ls.add(columnUserId());
        ls.add(columnCreatedDate());
        ls.add(columnModifiedDate());
        ls.add(columnVersionNo());
        return ls;
    }

    { initializeInformationResource(); }

    // ===================================================================================
    //                                                                         Unique Info
    //                                                                         ===========
    // -----------------------------------------------------
    //                                       Primary Element
    //                                       ---------------
    public UniqueInfo getPrimaryUniqueInfo() { return cpui(columnId()); }
    public boolean hasPrimaryKey() { return true; }
    public boolean hasCompoundPrimaryKey() { return false; }

    // ===================================================================================
    //                                                                       Relation Info
    //                                                                       =============
    // -----------------------------------------------------
    //                                      Foreign Property
    //                                      ----------------
    public ForeignInfo foreignYsGroup() {
        Map<ColumnInfo, ColumnInfo> map = newLinkedHashMap(columnGroupId(), YsGroupDbm.getInstance().columnId());
        return cfi("ysGroup", this, YsGroupDbm.getInstance(), map, 0, false, false);
    }
    public ForeignInfo foreignYsRole() {
        Map<ColumnInfo, ColumnInfo> map = newLinkedHashMap(columnRoleId(), YsRoleDbm.getInstance().columnId());
        return cfi("ysRole", this, YsRoleDbm.getInstance(), map, 1, false, false);
    }
    public ForeignInfo foreignYsUser() {
        Map<ColumnInfo, ColumnInfo> map = newLinkedHashMap(columnUserId(), YsUserDbm.getInstance().columnId());
        return cfi("ysUser", this, YsUserDbm.getInstance(), map, 2, false, false);
    }

    // -----------------------------------------------------
    //                                     Referrer Property
    //                                     -----------------

    // ===================================================================================
    //                                                                        Various Info
    //                                                                        ============
    public boolean hasIdentity() { return true; }
    public boolean hasVersionNo() { return true; }
    public ColumnInfo getVersionNoColumnInfo() { return _columnVersionNo; }

    // ===================================================================================
    //                                                                           Type Name
    //                                                                           =========
    public String getEntityTypeName() { return "org.seasar.ymir.scaffold.dbflute.exentity.YsRoleGroupUser"; }
    public String getConditionBeanTypeName() { return "org.seasar.ymir.scaffold.dbflute.cbean.bs.YsRoleGroupUserCB"; }
    public String getDaoTypeName() { return "org.seasar.ymir.scaffold.dbflute.exdao.YsRoleGroupUserDao"; }
    public String getBehaviorTypeName() { return "org.seasar.ymir.scaffold.dbflute.exbhv.YsRoleGroupUserBhv"; }

    // ===================================================================================
    //                                                                         Object Type
    //                                                                         ===========
    public Class<YsRoleGroupUser> getEntityType() { return YsRoleGroupUser.class; }

    // ===================================================================================
    //                                                                     Object Instance
    //                                                                     ===============
    public Entity newEntity() { return newMyEntity(); }
    public YsRoleGroupUser newMyEntity() { return new YsRoleGroupUser(); }

    // ===================================================================================
    //                                                                     Entity Handling
    //                                                                     ===============  
    public void acceptPrimaryKeyMap(Entity e, Map<String, ? extends Object> m)
    { doAcceptPrimaryKeyMap((YsRoleGroupUser)e, m, _epsMap); }
    public Map<String, Object> extractPrimaryKeyMap(Entity e) { return doExtractPrimaryKeyMap(e); }
    public Map<String, Object> extractAllColumnMap(Entity e) { return doExtractAllColumnMap(e); }

    // ===================================================================================
    //                                                               Entity Property Setup
    //                                                               =====================
    // It's very INTERNAL!
    protected final Map<String, Eps<YsRoleGroupUser>> _epsMap = StringKeyMap.createAsFlexibleConcurrent();
    {
        setupEps(_epsMap, new EpsId(), columnId());
        setupEps(_epsMap, new EpsRoleId(), columnRoleId());
        setupEps(_epsMap, new EpsGroupId(), columnGroupId());
        setupEps(_epsMap, new EpsUserId(), columnUserId());
        setupEps(_epsMap, new EpsCreatedDate(), columnCreatedDate());
        setupEps(_epsMap, new EpsModifiedDate(), columnModifiedDate());
        setupEps(_epsMap, new EpsVersionNo(), columnVersionNo());
    }

    public boolean hasEntityPropertySetupper(String propertyName) { return _epsMap.containsKey(propertyName); }
    public void setupEntityProperty(String propertyName, Object entity, Object value)
    { findEps(_epsMap, propertyName).setup((YsRoleGroupUser)entity, value); }

    public class EpsId implements Eps<YsRoleGroupUser>
    { public void setup(YsRoleGroupUser e, Object v) { e.setId(ctl(v)); } }
    public class EpsRoleId implements Eps<YsRoleGroupUser>
    { public void setup(YsRoleGroupUser e, Object v) { e.setRoleId(ctl(v)); } }
    public class EpsGroupId implements Eps<YsRoleGroupUser>
    { public void setup(YsRoleGroupUser e, Object v) { e.setGroupId(ctl(v)); } }
    public class EpsUserId implements Eps<YsRoleGroupUser>
    { public void setup(YsRoleGroupUser e, Object v) { e.setUserId(ctl(v)); } }
    public static class EpsCreatedDate implements Eps<YsRoleGroupUser>
    { public void setup(YsRoleGroupUser e, Object v) { e.setCreatedDate((java.sql.Timestamp)v); } }
    public static class EpsModifiedDate implements Eps<YsRoleGroupUser>
    { public void setup(YsRoleGroupUser e, Object v) { e.setModifiedDate((java.sql.Timestamp)v); } }
    public class EpsVersionNo implements Eps<YsRoleGroupUser>
    { public void setup(YsRoleGroupUser e, Object v) { e.setVersionNo(ctl(v)); } }
}
