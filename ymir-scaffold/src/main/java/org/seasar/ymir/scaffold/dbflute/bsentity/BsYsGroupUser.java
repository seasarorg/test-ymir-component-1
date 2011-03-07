package org.seasar.ymir.scaffold.dbflute.bsentity;

import java.io.Serializable;
import java.util.*;

import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.Entity;
import org.seasar.ymir.scaffold.dbflute.allcommon.DBMetaInstanceHandler;
import org.seasar.ymir.scaffold.dbflute.exentity.*;

/**
 * The entity of YS_GROUP_USER as TABLE. <br />
 * <pre>
 * [primary-key]
 *     ID
 * 
 * [column]
 *     ID, GROUP_ID, USER_ID, CREATED_DATE, MODIFIED_DATE, VERSION_NO
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
 *     YS_GROUP, YS_USER
 * 
 * [referrer-table]
 *     
 * 
 * [foreign-property]
 *     ysGroup, ysUser
 * 
 * [referrer-property]
 *     
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsYsGroupUser implements Entity, Serializable {

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
    /** ID: {PK, ID, NotNull, BIGINT(19)} */
    protected Long _id;

    /** GROUP_ID: {UQ, NotNull, BIGINT(19), FK to YS_GROUP} */
    protected Long _groupId;

    /** USER_ID: {UQ+, IX, NotNull, BIGINT(19), FK to YS_USER} */
    protected Long _userId;

    /** CREATED_DATE: {NotNull, TIMESTAMP(23, 10)} */
    protected java.sql.Timestamp _createdDate;

    /** MODIFIED_DATE: {NotNull, TIMESTAMP(23, 10)} */
    protected java.sql.Timestamp _modifiedDate;

    /** VERSION_NO: {NotNull, BIGINT(19), default=[1]} */
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
        return "YS_GROUP_USER";
    }

    /**
     * {@inheritDoc}
     */
    public String getTablePropertyName() { // according to Java Beans rule
        return "ysGroupUser";
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
    /** (グループ)YS_GROUP as 'ysGroup'. */
    protected YsGroup _ysGroup;

    /**
     * (グループ)YS_GROUP as 'ysGroup'.
     * @return The entity of foreign property 'ysGroup'. (Nullable: If the foreign key does not have 'NotNull' constraint, please check null.)
     */
    public YsGroup getYsGroup() {
        return _ysGroup;
    }

    /**
     * (グループ)YS_GROUP as 'ysGroup'.
     * @param ysGroup The entity of foreign property 'ysGroup'. (Nullable)
     */
    public void setYsGroup(YsGroup ysGroup) {
        _ysGroup = ysGroup;
    }

    /** (ユーザ)YS_USER as 'ysUser'. */
    protected YsUser _ysUser;

    /**
     * (ユーザ)YS_USER as 'ysUser'.
     * @return The entity of foreign property 'ysUser'. (Nullable: If the foreign key does not have 'NotNull' constraint, please check null.)
     */
    public YsUser getYsUser() {
        return _ysUser;
    }

    /**
     * (ユーザ)YS_USER as 'ysUser'.
     * @param ysUser The entity of foreign property 'ysUser'. (Nullable)
     */
    public void setYsUser(YsUser ysUser) {
        _ysUser = ysUser;
    }

    // ===================================================================================
    //                                                                   Referrer Property
    //                                                                   =================
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
        if (other == null || !(other instanceof BsYsGroupUser)) { return false; }
        BsYsGroupUser otherEntity = (BsYsGroupUser)other;
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
        if (_ysGroup != null)
        { sb.append(l).append(xbRDS(_ysGroup, "ysGroup")); }
        if (_ysUser != null)
        { sb.append(l).append(xbRDS(_ysUser, "ysUser")); }
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
        sb.append(c).append(getGroupId());
        sb.append(c).append(getUserId());
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
        if (_ysGroup != null) { sb.append(c).append("ysGroup"); }
        if (_ysUser != null) { sb.append(c).append("ysUser"); }
        if (sb.length() > 0) { sb.delete(0, c.length()).insert(0, "(").append(")"); }
        return sb.toString();
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] ID: {PK, ID, NotNull, BIGINT(19)} <br />
     * @return The value of the column 'ID'. (Nullable)
     */
    public Long getId() {
        return _id;
    }

    /**
     * [set] ID: {PK, ID, NotNull, BIGINT(19)} <br />
     * @param id The value of the column 'ID'. (Nullable)
     */
    public void setId(Long id) {
        __modifiedProperties.addPropertyName("id");
        this._id = id;
    }

    /**
     * [get] GROUP_ID: {UQ, NotNull, BIGINT(19), FK to YS_GROUP} <br />
     * @return The value of the column 'GROUP_ID'. (Nullable)
     */
    public Long getGroupId() {
        return _groupId;
    }

    /**
     * [set] GROUP_ID: {UQ, NotNull, BIGINT(19), FK to YS_GROUP} <br />
     * @param groupId The value of the column 'GROUP_ID'. (Nullable)
     */
    public void setGroupId(Long groupId) {
        __modifiedProperties.addPropertyName("groupId");
        this._groupId = groupId;
    }

    /**
     * [get] USER_ID: {UQ+, IX, NotNull, BIGINT(19), FK to YS_USER} <br />
     * @return The value of the column 'USER_ID'. (Nullable)
     */
    public Long getUserId() {
        return _userId;
    }

    /**
     * [set] USER_ID: {UQ+, IX, NotNull, BIGINT(19), FK to YS_USER} <br />
     * @param userId The value of the column 'USER_ID'. (Nullable)
     */
    public void setUserId(Long userId) {
        __modifiedProperties.addPropertyName("userId");
        this._userId = userId;
    }

    /**
     * [get] CREATED_DATE: {NotNull, TIMESTAMP(23, 10)} <br />
     * @return The value of the column 'CREATED_DATE'. (Nullable)
     */
    public java.sql.Timestamp getCreatedDate() {
        return _createdDate;
    }

    /**
     * [set] CREATED_DATE: {NotNull, TIMESTAMP(23, 10)} <br />
     * @param createdDate The value of the column 'CREATED_DATE'. (Nullable)
     */
    public void setCreatedDate(java.sql.Timestamp createdDate) {
        __modifiedProperties.addPropertyName("createdDate");
        this._createdDate = createdDate;
    }

    /**
     * [get] MODIFIED_DATE: {NotNull, TIMESTAMP(23, 10)} <br />
     * @return The value of the column 'MODIFIED_DATE'. (Nullable)
     */
    public java.sql.Timestamp getModifiedDate() {
        return _modifiedDate;
    }

    /**
     * [set] MODIFIED_DATE: {NotNull, TIMESTAMP(23, 10)} <br />
     * @param modifiedDate The value of the column 'MODIFIED_DATE'. (Nullable)
     */
    public void setModifiedDate(java.sql.Timestamp modifiedDate) {
        __modifiedProperties.addPropertyName("modifiedDate");
        this._modifiedDate = modifiedDate;
    }

    /**
     * [get] VERSION_NO: {NotNull, BIGINT(19), default=[1]} <br />
     * @return The value of the column 'VERSION_NO'. (Nullable)
     */
    public Long getVersionNo() {
        return _versionNo;
    }

    /**
     * [set] VERSION_NO: {NotNull, BIGINT(19), default=[1]} <br />
     * @param versionNo The value of the column 'VERSION_NO'. (Nullable)
     */
    public void setVersionNo(Long versionNo) {
        __modifiedProperties.addPropertyName("versionNo");
        this._versionNo = versionNo;
    }
}
