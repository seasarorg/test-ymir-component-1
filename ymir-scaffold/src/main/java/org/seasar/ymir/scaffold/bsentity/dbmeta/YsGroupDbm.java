package org.seasar.ymir.scaffold.bsentity.dbmeta;

import java.util.List;
import java.util.Map;

import org.seasar.dbflute.DBDef;
import org.seasar.dbflute.Entity;
import org.seasar.dbflute.dbmeta.AbstractDBMeta;
import org.seasar.dbflute.dbmeta.info.*;
import org.seasar.dbflute.helper.StringKeyMap;
import org.seasar.ymir.scaffold.allcommon.DBCurrent;
import org.seasar.ymir.scaffold.exentity.YsGroup;

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
    public String getTableDbName() { return "YS_GROUP"; }
    public String getTablePropertyName() { return "ysGroup"; }
    public String getTableSqlName() { return "YS_GROUP"; }
    public String getTableAlias() { return "グループ"; }

    // ===================================================================================
    //                                                                         Column Info
    //                                                                         ===========
    protected ColumnInfo _columnId = cci("ID", "ID", true, "id", Long.class, true, true, "BIGINT", 19, 0, false, null, null, null, "ysGroupUserList,ysRoleGroupUserList");
    protected ColumnInfo _columnName = cci("NAME", "グループ名", true, "name", String.class, false, false, "VARCHAR", 200, 0, false, null, null, null, null);
    protected ColumnInfo _columnDisplayName = cci("DISPLAY_NAME", "表示名", true, "displayName", String.class, false, false, "VARCHAR", 200, 0, false, null, null, null, null);
    protected ColumnInfo _columnCreatedDate = cci("CREATED_DATE", "作成日時", true, "createdDate", java.sql.Timestamp.class, false, false, "TIMESTAMP", 23, 10, false, null, null, null, null);
    protected ColumnInfo _columnModifiedDate = cci("MODIFIED_DATE", "更新日時", true, "modifiedDate", java.sql.Timestamp.class, false, false, "TIMESTAMP", 23, 10, false, null, null, null, null);
    protected ColumnInfo _columnVersionNo = cci("VERSION_NO", "バージョン番号", true, "versionNo", Long.class, false, false, "BIGINT", 19, 0, false, OptimisticLockType.VERSION_NO, null, null, null);

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
    public boolean hasTwoOrMorePrimaryKeys() { return false; }

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
    public String getEntityTypeName() { return "org.seasar.ymir.scaffold.exentity.YsGroup"; }
    public String getConditionBeanTypeName() { return "org.seasar.ymir.scaffold.cbean.bs.YsGroupCB"; }
    public String getDaoTypeName() { return "org.seasar.ymir.scaffold.exdao.YsGroupDao"; }
    public String getBehaviorTypeName() { return "org.seasar.ymir.scaffold.exbhv.YsGroupBhv"; }

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
    // -----------------------------------------------------
    //                                                Accept
    //                                                ------
    public void acceptPrimaryKeyMap(Entity entity, Map<String, ? extends Object> primaryKeyMap)
    { doAcceptPrimaryKeyMap((YsGroup)entity, primaryKeyMap, _epsMap); }
    public void acceptPrimaryKeyMapString(Entity entity, String primaryKeyMapString)
    { MapStringUtil.acceptPrimaryKeyMapString(primaryKeyMapString, entity); }
    public void acceptColumnValueMap(Entity entity, Map<String, ? extends Object> columnValueMap)
    { doAcceptColumnValueMap((YsGroup)entity, columnValueMap, _epsMap); }
    public void acceptColumnValueMapString(Entity entity, String columnValueMapString)
    { MapStringUtil.acceptColumnValueMapString(columnValueMapString, entity); }

    // -----------------------------------------------------
    //                                               Extract
    //                                               -------
    public String extractPrimaryKeyMapString(Entity entity) { return MapStringUtil.extractPrimaryKeyMapString(entity); }
    public String extractPrimaryKeyMapString(Entity entity, String startBrace, String endBrace, String delimiter, String equal)
    { return doExtractPrimaryKeyMapString(entity, startBrace, endBrace, delimiter, equal); }
    public String extractColumnValueMapString(Entity entity) { return MapStringUtil.extractColumnValueMapString(entity); }
    public String extractColumnValueMapString(Entity entity, String startBrace, String endBrace, String delimiter, String equal)
    { return doExtractColumnValueMapString(entity, startBrace, endBrace, delimiter, equal); }

    // -----------------------------------------------------
    //                                               Convert
    //                                               -------
    public List<Object> convertToColumnValueList(Entity entity) { return newArrayList(convertToColumnValueMap(entity).values()); }
    public Map<String, Object> convertToColumnValueMap(Entity entity) { return doConvertToColumnValueMap(entity); }
    public List<String> convertToColumnStringValueList(Entity entity) { return newArrayList(convertToColumnStringValueMap(entity).values()); }
    public Map<String, String> convertToColumnStringValueMap(Entity entity) { return doConvertToColumnStringValueMap(entity); }

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

    public static class EpsId implements Eps<YsGroup>
    { public void setup(YsGroup e, Object v) { e.setId((Long)v); } }
    public static class EpsName implements Eps<YsGroup>
    { public void setup(YsGroup e, Object v) { e.setName((String)v); } }
    public static class EpsDisplayName implements Eps<YsGroup>
    { public void setup(YsGroup e, Object v) { e.setDisplayName((String)v); } }
    public static class EpsCreatedDate implements Eps<YsGroup>
    { public void setup(YsGroup e, Object v) { e.setCreatedDate((java.sql.Timestamp)v); } }
    public static class EpsModifiedDate implements Eps<YsGroup>
    { public void setup(YsGroup e, Object v) { e.setModifiedDate((java.sql.Timestamp)v); } }
    public static class EpsVersionNo implements Eps<YsGroup>
    { public void setup(YsGroup e, Object v) { e.setVersionNo((Long)v); } }
}
