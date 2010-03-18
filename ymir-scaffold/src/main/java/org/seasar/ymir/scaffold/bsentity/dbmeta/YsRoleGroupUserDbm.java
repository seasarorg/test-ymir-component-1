package org.seasar.ymir.scaffold.bsentity.dbmeta;

import java.util.List;
import java.util.Map;

import org.seasar.dbflute.DBDef;
import org.seasar.dbflute.Entity;
import org.seasar.dbflute.dbmeta.AbstractDBMeta;
import org.seasar.dbflute.dbmeta.info.*;
import org.seasar.dbflute.helper.StringKeyMap;
import org.seasar.ymir.scaffold.allcommon.DBCurrent;
import org.seasar.ymir.scaffold.exentity.YsRoleGroupUser;

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
    public String getTableDbName() { return "YS_ROLE_GROUP_USER"; }
    public String getTablePropertyName() { return "ysRoleGroupUser"; }
    public String getTableSqlName() { return "YS_ROLE_GROUP_USER"; }

    // ===================================================================================
    //                                                                         Column Info
    //                                                                         ===========
    protected ColumnInfo _columnId = cci("ID", null, true, "id", Long.class, true, true, "BIGINT", 19, 0, false, null, null, null, null);
    protected ColumnInfo _columnRoleId = cci("ROLE_ID", null, true, "roleId", Long.class, false, false, "BIGINT", 19, 0, false, null, null, "ysRole", null);
    protected ColumnInfo _columnGroupId = cci("GROUP_ID", null, false, "groupId", Long.class, false, false, "BIGINT", 19, 0, false, null, null, "ysGroup", null);
    protected ColumnInfo _columnUserId = cci("USER_ID", null, false, "userId", Long.class, false, false, "BIGINT", 19, 0, false, null, null, "ysUser", null);
    protected ColumnInfo _columnCreatedDate = cci("CREATED_DATE", null, true, "createdDate", java.sql.Timestamp.class, false, false, "TIMESTAMP", 23, 10, false, null, null, null, null);
    protected ColumnInfo _columnModifiedDate = cci("MODIFIED_DATE", null, true, "modifiedDate", java.sql.Timestamp.class, false, false, "TIMESTAMP", 23, 10, false, null, null, null, null);
    protected ColumnInfo _columnVersionNo = cci("VERSION_NO", null, true, "versionNo", Long.class, false, false, "BIGINT", 19, 0, false, OptimisticLockType.VERSION_NO, null, null, null);

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
    public boolean hasTwoOrMorePrimaryKeys() { return false; }

    // ===================================================================================
    //                                                                       Relation Info
    //                                                                       =============
    // -----------------------------------------------------
    //                                      Foreign Property
    //                                      ----------------
    public ForeignInfo foreignYsGroup() {
        Map<ColumnInfo, ColumnInfo> map = newLinkedHashMap(columnGroupId(), YsGroupDbm.getInstance().columnId());
        return cfi("ysGroup", this, YsGroupDbm.getInstance(), map, 0, false);
    }
    public ForeignInfo foreignYsRole() {
        Map<ColumnInfo, ColumnInfo> map = newLinkedHashMap(columnRoleId(), YsRoleDbm.getInstance().columnId());
        return cfi("ysRole", this, YsRoleDbm.getInstance(), map, 1, false);
    }
    public ForeignInfo foreignYsUser() {
        Map<ColumnInfo, ColumnInfo> map = newLinkedHashMap(columnUserId(), YsUserDbm.getInstance().columnId());
        return cfi("ysUser", this, YsUserDbm.getInstance(), map, 2, false);
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
    public String getEntityTypeName() { return "org.seasar.ymir.scaffold.exentity.YsRoleGroupUser"; }
    public String getConditionBeanTypeName() { return "org.seasar.ymir.scaffold.cbean.bs.YsRoleGroupUserCB"; }
    public String getDaoTypeName() { return "org.seasar.ymir.scaffold.exdao.YsRoleGroupUserDao"; }
    public String getBehaviorTypeName() { return "org.seasar.ymir.scaffold.exbhv.YsRoleGroupUserBhv"; }

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
    // -----------------------------------------------------
    //                                                Accept
    //                                                ------
    public void acceptPrimaryKeyMap(Entity entity, Map<String, ? extends Object> primaryKeyMap)
    { doAcceptPrimaryKeyMap((YsRoleGroupUser)entity, primaryKeyMap, _epsMap); }
    public void acceptPrimaryKeyMapString(Entity entity, String primaryKeyMapString)
    { MapStringUtil.acceptPrimaryKeyMapString(primaryKeyMapString, entity); }
    public void acceptColumnValueMap(Entity entity, Map<String, ? extends Object> columnValueMap)
    { doAcceptColumnValueMap((YsRoleGroupUser)entity, columnValueMap, _epsMap); }
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

    public static class EpsId implements Eps<YsRoleGroupUser>
    { public void setup(YsRoleGroupUser e, Object v) { e.setId((Long)v); } }
    public static class EpsRoleId implements Eps<YsRoleGroupUser>
    { public void setup(YsRoleGroupUser e, Object v) { e.setRoleId((Long)v); } }
    public static class EpsGroupId implements Eps<YsRoleGroupUser>
    { public void setup(YsRoleGroupUser e, Object v) { e.setGroupId((Long)v); } }
    public static class EpsUserId implements Eps<YsRoleGroupUser>
    { public void setup(YsRoleGroupUser e, Object v) { e.setUserId((Long)v); } }
    public static class EpsCreatedDate implements Eps<YsRoleGroupUser>
    { public void setup(YsRoleGroupUser e, Object v) { e.setCreatedDate((java.sql.Timestamp)v); } }
    public static class EpsModifiedDate implements Eps<YsRoleGroupUser>
    { public void setup(YsRoleGroupUser e, Object v) { e.setModifiedDate((java.sql.Timestamp)v); } }
    public static class EpsVersionNo implements Eps<YsRoleGroupUser>
    { public void setup(YsRoleGroupUser e, Object v) { e.setVersionNo((Long)v); } }
}
