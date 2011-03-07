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
 * The DB meta of YS_GROUP. (Singleton)
 * @author DBFlute(AutoGenerator)
 */
public class YsGroupDbm extends AbstractDBMeta {

    // ===================================================================================
    //                                                                           Singleton
    //                                                                           =========
    private static final YsGroupDbm _instance = new YsGroupDbm();
    private YsGroupDbm() {}
    public static YsGroupDbm getInstance() { return _instance; }

    // ===================================================================================
    //                                                                       Current DBDef
    //                                                                       =============
    public DBDef getCurrentDBDef() { return DBCurrent.getInstance().currentDBDef(); }

    // ===================================================================================
    //                                                                          Table Info
    //                                                                          ==========
    protected final String _tableDbName = "YS_GROUP";
    protected final String _tablePropertyName = "ysGroup";
    protected final TableSqlName _tableSqlName = new TableSqlName("YS_GROUP", _tableDbName);
    { _tableSqlName.xacceptFilter(DBFluteConfig.getInstance().getTableSqlNameFilter()); }
    public String getTableDbName() { return _tableDbName; }
    public String getTablePropertyName() { return _tablePropertyName; }
    public TableSqlName getTableSqlName() { return _tableSqlName; }
    protected final String _tableAlias = "グループ";
    public String getTableAlias() { return _tableAlias; }

    // ===================================================================================
    //                                                                         Column Info
    //                                                                         ===========
    protected final ColumnInfo _columnId = cci("ID", "ID", null, "ID", true, "id", Long.class, true, true, "BIGINT", 19, 0, false, null, null, null, "ysGroupUserList,ysRoleGroupUserList", null);
    protected final ColumnInfo _columnName = cci("NAME", "NAME", null, "グループ名", true, "name", String.class, false, false, "VARCHAR", 200, 0, false, null, null, null, null, null);
    protected final ColumnInfo _columnDisplayName = cci("DISPLAY_NAME", "DISPLAY_NAME", null, "表示名", true, "displayName", String.class, false, false, "VARCHAR", 200, 0, false, null, null, null, null, null);
    protected final ColumnInfo _columnCreatedDate = cci("CREATED_DATE", "CREATED_DATE", null, "作成日時", true, "createdDate", java.sql.Timestamp.class, false, false, "TIMESTAMP", 23, 10, false, null, null, null, null, null);
    protected final ColumnInfo _columnModifiedDate = cci("MODIFIED_DATE", "MODIFIED_DATE", null, "更新日時", true, "modifiedDate", java.sql.Timestamp.class, false, false, "TIMESTAMP", 23, 10, false, null, null, null, null, null);
    protected final ColumnInfo _columnVersionNo = cci("VERSION_NO", "VERSION_NO", null, "バージョン番号", true, "versionNo", Long.class, false, false, "BIGINT", 19, 0, false, OptimisticLockType.VERSION_NO, null, null, null, null);

    public ColumnInfo columnId() { return _columnId; }
    public ColumnInfo columnName() { return _columnName; }
    public ColumnInfo columnDisplayName() { return _columnDisplayName; }
    public ColumnInfo columnCreatedDate() { return _columnCreatedDate; }
    public ColumnInfo columnModifiedDate() { return _columnModifiedDate; }
    public ColumnInfo columnVersionNo() { return _columnVersionNo; }

    protected List<ColumnInfo> ccil() {
        List<ColumnInfo> ls = newArrayList();
        ls.add(columnId());
        ls.add(columnName());
        ls.add(columnDisplayName());
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

    // -----------------------------------------------------
    //                                     Referrer Property
    //                                     -----------------
    public ReferrerInfo referrerYsGroupUserList() {
        Map<ColumnInfo, ColumnInfo> map = newLinkedHashMap(columnId(), YsGroupUserDbm.getInstance().columnGroupId());
        return cri("ysGroupUserList", this, YsGroupUserDbm.getInstance(), map, false);
    }
    public ReferrerInfo referrerYsRoleGroupUserList() {
        Map<ColumnInfo, ColumnInfo> map = newLinkedHashMap(columnId(), YsRoleGroupUserDbm.getInstance().columnGroupId());
        return cri("ysRoleGroupUserList", this, YsRoleGroupUserDbm.getInstance(), map, false);
    }

    // ===================================================================================
    //                                                                        Various Info
    //                                                                        ============
    public boolean hasIdentity() { return true; }
    public boolean hasVersionNo() { return true; }
    public ColumnInfo getVersionNoColumnInfo() { return _columnVersionNo; }

    // ===================================================================================
    //                                                                           Type Name
    //                                                                           =========
    public String getEntityTypeName() { return "org.seasar.ymir.scaffold.dbflute.exentity.YsGroup"; }
    public String getConditionBeanTypeName() { return "org.seasar.ymir.scaffold.dbflute.cbean.bs.YsGroupCB"; }
    public String getDaoTypeName() { return "org.seasar.ymir.scaffold.dbflute.exdao.YsGroupDao"; }
    public String getBehaviorTypeName() { return "org.seasar.ymir.scaffold.dbflute.exbhv.YsGroupBhv"; }

    // ===================================================================================
    //                                                                         Object Type
    //                                                                         ===========
    public Class<YsGroup> getEntityType() { return YsGroup.class; }

    // ===================================================================================
    //                                                                     Object Instance
    //                                                                     ===============
    public Entity newEntity() { return newMyEntity(); }
    public YsGroup newMyEntity() { return new YsGroup(); }

    // ===================================================================================
    //                                                                     Entity Handling
    //                                                                     ===============  
    public void acceptPrimaryKeyMap(Entity e, Map<String, ? extends Object> m)
    { doAcceptPrimaryKeyMap((YsGroup)e, m, _epsMap); }
    public Map<String, Object> extractPrimaryKeyMap(Entity e) { return doExtractPrimaryKeyMap(e); }
    public Map<String, Object> extractAllColumnMap(Entity e) { return doExtractAllColumnMap(e); }

    // ===================================================================================
    //                                                               Entity Property Setup
    //                                                               =====================
    // It's very INTERNAL!
    protected final Map<String, Eps<YsGroup>> _epsMap = StringKeyMap.createAsFlexibleConcurrent();
    {
        setupEps(_epsMap, new EpsId(), columnId());
        setupEps(_epsMap, new EpsName(), columnName());
        setupEps(_epsMap, new EpsDisplayName(), columnDisplayName());
        setupEps(_epsMap, new EpsCreatedDate(), columnCreatedDate());
        setupEps(_epsMap, new EpsModifiedDate(), columnModifiedDate());
        setupEps(_epsMap, new EpsVersionNo(), columnVersionNo());
    }

    public boolean hasEntityPropertySetupper(String propertyName) { return _epsMap.containsKey(propertyName); }
    public void setupEntityProperty(String propertyName, Object entity, Object value)
    { findEps(_epsMap, propertyName).setup((YsGroup)entity, value); }

    public class EpsId implements Eps<YsGroup>
    { public void setup(YsGroup e, Object v) { e.setId(ctl(v)); } }
    public static class EpsName implements Eps<YsGroup>
    { public void setup(YsGroup e, Object v) { e.setName((String)v); } }
    public static class EpsDisplayName implements Eps<YsGroup>
    { public void setup(YsGroup e, Object v) { e.setDisplayName((String)v); } }
    public static class EpsCreatedDate implements Eps<YsGroup>
    { public void setup(YsGroup e, Object v) { e.setCreatedDate((java.sql.Timestamp)v); } }
    public static class EpsModifiedDate implements Eps<YsGroup>
    { public void setup(YsGroup e, Object v) { e.setModifiedDate((java.sql.Timestamp)v); } }
    public class EpsVersionNo implements Eps<YsGroup>
    { public void setup(YsGroup e, Object v) { e.setVersionNo(ctl(v)); } }
}
