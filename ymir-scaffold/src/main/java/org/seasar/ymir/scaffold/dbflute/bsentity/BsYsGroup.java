package org.seasar.ymir.scaffold.dbflute.bsentity;

import java.io.Serializable;
import java.util.*;

import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.Entity;
import org.seasar.ymir.scaffold.dbflute.allcommon.DBMetaInstanceHandler;
import org.seasar.ymir.scaffold.dbflute.exentity.*;

/**
 * The entity of (グループ)YS_GROUP as TABLE. <br />
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
public abstract class BsYsGroup implements Entity, Serializable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** Serial version UID. (Default) */
    private static final long serialVersionUID = 1L;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                                Column
    //                                                ------
    /** (ID)ID: {PK, ID, NotNull, BIGINT(19)} */
    protected Long _id;

    /** (グループ名)NAME: {UQ, NotNull, VARCHAR(200)} */
    protected String _name;

    /** (表示名)DISPLAY_NAME: {NotNull, VARCHAR(200)} */
    protected String _displayName;

    /** (作成日時)CREATED_DATE: {NotNull, TIMESTAMP(23, 10)} */
    protected java.sql.Timestamp _createdDate;

    /** (更新日時)MODIFIED_DATE: {NotNull, TIMESTAMP(23, 10)} */
    protected java.sql.Timestamp _modifiedDate;

    /** (バージョン番号)VERSION_NO: {NotNull, BIGINT(19), default=[1]} */
    protected Long _versionNo;

    // -----------------------------------------------------
    //                                              Internal
    //                                              --------
    /** The modified properties for this entity. */
    protected EntityModifiedProperties __modifiedProperties = newEntityModifiedProperties();

    // ===================================================================================
    //                                                                          Table Name
    //                                                                          ==========
    /**
     * {@inheritDoc}
     */
    public String getTableDbName() {
        return "YS_GROUP";
    }

    /**
     * {@inheritDoc}
     */
    public String getTablePropertyName() { // according to Java Beans rule
        return "ysGroup";
    }

    // ===================================================================================
    //                                                                              DBMeta
    //                                                                              ======
    /**
     * {@inheritDoc}
     */
    public DBMeta getDBMeta() {
        return DBMetaInstanceHandler.findDBMeta(getTableDbName());
    }

    // ===================================================================================
    //                                                                         Primary Key
    //                                                                         ===========
    /**
     * {@inheritDoc}
     */
    public boolean hasPrimaryKeyValue() {
        if (getId() == null) { return false; }
        return true;
    }

    // ===================================================================================
    //                                                                    Foreign Property
    //                                                                    ================
    // ===================================================================================
    //                                                                   Referrer Property
    //                                                                   =================
    /** YS_GROUP_USER as 'ysGroupUserList'. */
    protected List<YsGroupUser> _ysGroupUserList;

    /**
     * YS_GROUP_USER as 'ysGroupUserList'.
     * @return The entity list of referrer property 'ysGroupUserList'. (NotNull: If it's not loaded yet, initializes the list instance of referrer as empty and returns it.)
     */
    public List<YsGroupUser> getYsGroupUserList() {
        if (_ysGroupUserList == null) { _ysGroupUserList = new ArrayList<YsGroupUser>(); }
        return _ysGroupUserList;
    }

    /**
     * YS_GROUP_USER as 'ysGroupUserList'.
     * @param ysGroupUserList The entity list of referrer property 'ysGroupUserList'. (Nullable)
     */
    public void setYsGroupUserList(List<YsGroupUser> ysGroupUserList) {
        _ysGroupUserList = ysGroupUserList;
    }

    /** YS_ROLE_GROUP_USER as 'ysRoleGroupUserList'. */
    protected List<YsRoleGroupUser> _ysRoleGroupUserList;

    /**
     * YS_ROLE_GROUP_USER as 'ysRoleGroupUserList'.
     * @return The entity list of referrer property 'ysRoleGroupUserList'. (NotNull: If it's not loaded yet, initializes the list instance of referrer as empty and returns it.)
     */
    public List<YsRoleGroupUser> getYsRoleGroupUserList() {
        if (_ysRoleGroupUserList == null) { _ysRoleGroupUserList = new ArrayList<YsRoleGroupUser>(); }
        return _ysRoleGroupUserList;
    }

    /**
     * YS_ROLE_GROUP_USER as 'ysRoleGroupUserList'.
     * @param ysRoleGroupUserList The entity list of referrer property 'ysRoleGroupUserList'. (Nullable)
     */
    public void setYsRoleGroupUserList(List<YsRoleGroupUser> ysRoleGroupUserList) {
        _ysRoleGroupUserList = ysRoleGroupUserList;
    }

    // ===================================================================================
    //                                                                 Modified Properties
    //                                                                 ===================
    /**
     * {@inheritDoc}
     */
    public Set<String> getModifiedPropertyNames() {
        return __modifiedProperties.getPropertyNames();
    }

    protected EntityModifiedProperties newEntityModifiedProperties() {
        return new EntityModifiedProperties();
    }

    /**
     * {@inheritDoc}
     */
    public void clearModifiedPropertyNames() {
        __modifiedProperties.clear();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasModification() {
        return !__modifiedProperties.isEmpty();
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    /**
     * If primary-keys or columns of the other are same as this one, returns true.
     * @param other The other entity. (Nullable)
     * @return Comparing result.
     */
    public boolean equals(Object other) {
        if (other == null || !(other instanceof BsYsGroup)) { return false; }
        BsYsGroup otherEntity = (BsYsGroup)other;
        if (!xSV(getId(), otherEntity.getId())) { return false; }
        return true;
    }
    protected boolean xSV(Object value1, Object value2) { // isSameValue()
        return InternalUtil.isSameValue(value1, value2);
    }

    /**
     * Calculates the hash-code from primary-keys or columns.
     * @return The hash-code from primary-key or columns.
     */
    public int hashCode() {
        int result = 17;
        result = xCH(result, getId());
        return result;
    }
    protected int xCH(int result, Object value) { // calculateHashcode()
        return InternalUtil.calculateHashcode(result, value);
    }

    /**
     * @return The display string of all columns and relation existences. (NotNull)
     */
    public String toString() {
        return buildDisplayString(InternalUtil.toClassTitle(this), true, true);
    }

    /**
     * @return The display string of basic informations with one-nested relation values. (NotNull)
     */
    public String toStringWithRelation() {
        StringBuilder sb = new StringBuilder();
        sb.append(toString());
        String l = "\n  ";
        if (_ysGroupUserList != null) { for (Entity e : _ysGroupUserList)
        { if (e != null) { sb.append(l).append(xbRDS(e, "ysGroupUserList")); } } }
        if (_ysRoleGroupUserList != null) { for (Entity e : _ysRoleGroupUserList)
        { if (e != null) { sb.append(l).append(xbRDS(e, "ysRoleGroupUserList")); } } }
        return sb.toString();
    }
    protected String xbRDS(Entity e, String name) { // buildRelationDisplayString()
        return e.buildDisplayString(name, true, true);
    }

    /**
     * @param name The name for display. (Nullable: If it's null, it does not have a name)
     * @param column Does it contains column values or not?
     * @param relation Does it contains relation existences or not?
     * @return The display string for this entity. (NotNull)
     */
    public String buildDisplayString(String name, boolean column, boolean relation) {
        StringBuilder sb = new StringBuilder();
        if (name != null) { sb.append(name).append(column || relation ? ":" : ""); }
        if (column) { sb.append(buildColumnString()); }
        if (relation) { sb.append(buildRelationString()); }
        sb.append("@").append(Integer.toHexString(hashCode()));
        return sb.toString();
    }
    protected String buildColumnString() {
        String c = ", ";
        StringBuilder sb = new StringBuilder();
        sb.append(c).append(getId());
        sb.append(c).append(getName());
        sb.append(c).append(getDisplayName());
        sb.append(c).append(getCreatedDate());
        sb.append(c).append(getModifiedDate());
        sb.append(c).append(getVersionNo());
        if (sb.length() > 0) { sb.delete(0, c.length()); }
        sb.insert(0, "{").append("}");
        return sb.toString();
    }
    protected String buildRelationString() {
        StringBuilder sb = new StringBuilder();
        String c = ",";
        if (_ysGroupUserList != null && !_ysGroupUserList.isEmpty())
        { sb.append(c).append("ysGroupUserList"); }
        if (_ysRoleGroupUserList != null && !_ysRoleGroupUserList.isEmpty())
        { sb.append(c).append("ysRoleGroupUserList"); }
        if (sb.length() > 0) { sb.delete(0, c.length()).insert(0, "(").append(")"); }
        return sb.toString();
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] (ID)ID: {PK, ID, NotNull, BIGINT(19)} <br />
     * @return The value of the column 'ID'. (Nullable)
     */
    public Long getId() {
        return _id;
    }

    /**
     * [set] (ID)ID: {PK, ID, NotNull, BIGINT(19)} <br />
     * @param id The value of the column 'ID'. (Nullable)
     */
    public void setId(Long id) {
        __modifiedProperties.addPropertyName("id");
        this._id = id;
    }

    /**
     * [get] (グループ名)NAME: {UQ, NotNull, VARCHAR(200)} <br />
     * @return The value of the column 'NAME'. (Nullable)
     */
    public String getName() {
        return _name;
    }

    /**
     * [set] (グループ名)NAME: {UQ, NotNull, VARCHAR(200)} <br />
     * @param name The value of the column 'NAME'. (Nullable)
     */
    public void setName(String name) {
        __modifiedProperties.addPropertyName("name");
        this._name = name;
    }

    /**
     * [get] (表示名)DISPLAY_NAME: {NotNull, VARCHAR(200)} <br />
     * @return The value of the column 'DISPLAY_NAME'. (Nullable)
     */
    public String getDisplayName() {
        return _displayName;
    }

    /**
     * [set] (表示名)DISPLAY_NAME: {NotNull, VARCHAR(200)} <br />
     * @param displayName The value of the column 'DISPLAY_NAME'. (Nullable)
     */
    public void setDisplayName(String displayName) {
        __modifiedProperties.addPropertyName("displayName");
        this._displayName = displayName;
    }

    /**
     * [get] (作成日時)CREATED_DATE: {NotNull, TIMESTAMP(23, 10)} <br />
     * @return The value of the column 'CREATED_DATE'. (Nullable)
     */
    public java.sql.Timestamp getCreatedDate() {
        return _createdDate;
    }

    /**
     * [set] (作成日時)CREATED_DATE: {NotNull, TIMESTAMP(23, 10)} <br />
     * @param createdDate The value of the column 'CREATED_DATE'. (Nullable)
     */
    public void setCreatedDate(java.sql.Timestamp createdDate) {
        __modifiedProperties.addPropertyName("createdDate");
        this._createdDate = createdDate;
    }

    /**
     * [get] (更新日時)MODIFIED_DATE: {NotNull, TIMESTAMP(23, 10)} <br />
     * @return The value of the column 'MODIFIED_DATE'. (Nullable)
     */
    public java.sql.Timestamp getModifiedDate() {
        return _modifiedDate;
    }

    /**
     * [set] (更新日時)MODIFIED_DATE: {NotNull, TIMESTAMP(23, 10)} <br />
     * @param modifiedDate The value of the column 'MODIFIED_DATE'. (Nullable)
     */
    public void setModifiedDate(java.sql.Timestamp modifiedDate) {
        __modifiedProperties.addPropertyName("modifiedDate");
        this._modifiedDate = modifiedDate;
    }

    /**
     * [get] (バージョン番号)VERSION_NO: {NotNull, BIGINT(19), default=[1]} <br />
     * @return The value of the column 'VERSION_NO'. (Nullable)
     */
    public Long getVersionNo() {
        return _versionNo;
    }

    /**
     * [set] (バージョン番号)VERSION_NO: {NotNull, BIGINT(19), default=[1]} <br />
     * @param versionNo The value of the column 'VERSION_NO'. (Nullable)
     */
    public void setVersionNo(Long versionNo) {
        __modifiedProperties.addPropertyName("versionNo");
        this._versionNo = versionNo;
    }
}
