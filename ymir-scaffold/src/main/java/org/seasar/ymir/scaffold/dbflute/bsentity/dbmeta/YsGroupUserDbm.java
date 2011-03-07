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
 * The DB meta of YS_GROUP_USER. (Singleton)
 * @author DBFlute(AutoGenerator)
 */
public class YsGroupUserDbm extends AbstractDBMeta {

    // ===================================================================================
    //                                                                           Singleton
    //                                                                           =========
    private static final YsGroupUserDbm _instance = new YsGroupUserDbm();
    private YsGroupUserDbm() {}
    public static YsGroupUserDbm getInstance() { return _instance; }

    // ===================================================================================
    //                                                                       Current DBDef
    //                                                                       =============
    public DBDef getCurrentDBDef() { return DBCurrent.getInstance().currentDBDef(); }

    // ===================================================================================
    //                                                                          Table Info
    //                                                                          ==========
    protected final String _tableDbName = "YS_GROUP_USER";
    protected final String _tablePropertyName = "ysGroupUser";
    protected final TableSqlName _tableSqlName = new TableSqlName("YS_GROUP_USER", _tableDbName);
    { _tableSqlName.xacceptFilter(DBFluteConfig.getInstance().getTableSqlNameFilter()); }
    public String getTableDbName() { return _tableDbName; }
    public String getTablePropertyName() { return _tablePropertyName; }
    public TableSqlName getTableSqlName() { return _tableSqlName; }

    // ===================================================================================
    //                                                                         Column Info
    //                                                                         ===========
    protected final ColumnInfo _columnId = cci("ID", "ID", null, null, true, "id", Long.class, true, true, "BIGINT", 19, 0, false, null, null, null, null, null);
    protected final ColumnInfo _columnGroupId = cci("GROUP_ID", "GROUP_ID", null, null, true, "groupId", Long.class, false, false, "BIGINT", 19, 0, false, null, null, "ysGroup", null, null);
    protected final ColumnInfo _columnUserId = cci("USER_ID", "USER_ID", null, null, true, "userId", Long.class, false, false, "BIGINT", 19, 0, false, null, null, "ysUser", null, null);
    protected final ColumnInfo _columnCreatedDate = cci("CREATED_DATE", "CREATED_DATE", null, null, true, "createdDate", java.sql.Timestamp.class, false, false, "TIMESTAMP", 23, 10, false, null, null, null, null, null);
    protected final ColumnInfo _columnModifiedDate = cci("MODIFIED_DATE", "MODIFIED_DATE", null, null, true, "modifiedDate", java.sql.Timestamp.class, false, false, "TIMESTAMP", 23, 10, false, null, null, null, null, null);
    protected final ColumnInfo _columnVersionNo = cci("VERSION_NO", "VERSION_NO", null, null, true, "versionNo", Long.class, false, false, "BIGINT", 19, 0, false, OptimisticLockType.VERSION_NO, null, null, null, null);

    public ColumnInfo columnId() { return _columnId; }
    public ColumnInfo columnGroupId() { return _columnGroupId; }
    public ColumnInfo columnUserId() { return _columnUserId; }
    public ColumnInfo columnCreatedDate() { return _columnCreatedDate; }
    public ColumnInfo columnModifiedDate() { return _columnModifiedDate; }
    public ColumnInfo columnVersionNo() { return _columnVersionNo; }

    protected List<ColumnInfo> ccil() {
        List<ColumnInfo> ls = newArrayList();
        ls.add(columnId());
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
    public ForeignInfo foreignYsUser() {
        Map<ColumnInfo, ColumnInfo> map = newLinkedHashMap(columnUserId(), YsUserDbm.getInstance().columnId());
        return cfi("ysUser", this, YsUserDbm.getInstance(), map, 1, false, false);
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
    public String getEntityTypeName() { return "org.seasar.ymir.scaffold.dbflute.exentity.YsGroupUser"; }
    public String getConditionBeanTypeName() { return "org.seasar.ymir.scaffold.dbflute.cbean.bs.YsGroupUserCB"; }
    public String getDaoTypeName() { return "org.seasar.ymir.scaffold.dbflute.exdao.YsGroupUserDao"; }
    public String getBehaviorTypeName() { return "org.seasar.ymir.scaffold.dbflute.exbhv.YsGroupUserBhv"; }

    // ===================================================================================
    //                                                                         Object Type
    //                                                                         ===========
    public Class<YsGroupUser> getEntityType() { return YsGroupUser.class; }

    // ===================================================================================
    //                                                                     Object Instance
    //                                                                     ===============
    public Entity newEntity() { return newMyEntity(); }
    public YsGroupUser newMyEntity() { return new YsGroupUser(); }

    // ===================================================================================
    //                                                                     Entity Handling
    //                                                                     ===============  
    public void acceptPrimaryKeyMap(Entity e, Map<String, ? extends Object> m)
    { doAcceptPrimaryKeyMap((YsGroupUser)e, m, _epsMap); }
    public Map<String, Object> extractPrimaryKeyMap(Entity e) { return doExtractPrimaryKeyMap(e); }
    public Map<String, Object> extractAllColumnMap(Entity e) { return doExtractAllColumnMap(e); }

    // ===================================================================================
    //                                                               Entity Property Setup
    //                                                               =====================
    // It's very INTERNAL!
    protected final Map<String, Eps<YsGroupUser>> _epsMap = StringKeyMap.createAsFlexibleConcurrent();
    {
        setupEps(_epsMap, new EpsId(), columnId());
        setupEps(_epsMap, new EpsGroupId(), columnGroupId());
        setupEps(_epsMap, new EpsUserId(), columnUserId());
        setupEps(_epsMap, new EpsCreatedDate(), columnCreatedDate());
        setupEps(_epsMap, new EpsModifiedDate(), columnModifiedDate());
        setupEps(_epsMap, new EpsVersionNo(), columnVersionNo());
    }

    public boolean hasEntityPropertySetupper(String propertyName) { return _epsMap.containsKey(propertyName); }
    public void setupEntityProperty(String propertyName, Object entity, Object value)
    { findEps(_epsMap, propertyName).setup((YsGroupUser)entity, value); }

    public class EpsId implements Eps<YsGroupUser>
    { public void setup(YsGroupUser e, Object v) { e.setId(ctl(v)); } }
    public class EpsGroupId implements Eps<YsGroupUser>
    { public void setup(YsGroupUser e, Object v) { e.setGroupId(ctl(v)); } }
    public class EpsUserId implements Eps<YsGroupUser>
    { public void setup(YsGroupUser e, Object v) { e.setUserId(ctl(v)); } }
    public static class EpsCreatedDate implements Eps<YsGroupUser>
    { public void setup(YsGroupUser e, Object v) { e.setCreatedDate((java.sql.Timestamp)v); } }
    public static class EpsModifiedDate implements Eps<YsGroupUser>
    { public void setup(YsGroupUser e, Object v) { e.setModifiedDate((java.sql.Timestamp)v); } }
    public class EpsVersionNo implements Eps<YsGroupUser>
    { public void setup(YsGroupUser e, Object v) { e.setVersionNo(ctl(v)); } }
}
