package org.seasar.ymir.scaffold.dbflute.bsentity;

import java.io.Serializable;
import java.util.*;

import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.Entity;
import org.seasar.ymir.scaffold.dbflute.allcommon.DBMetaInstanceHandler;
import org.seasar.ymir.scaffold.dbflute.exentity.*;

/**
 * The entity of (ユーザ)YS_USER that is TABLE. <br />
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
public abstract class BsYsUser implements Entity, Serializable {

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
    /** (ID)ID: {PK : ID : NotNull : BIGINT(19)} */
    protected Long _id;

    /** (ユーザ名)NAME: {UQ : NotNull : VARCHAR(200)} */
    protected String _name;

    /** (表示名)DISPLAY_NAME: {NotNull : VARCHAR(200)} */
    protected String _displayName;

    /** (パスワード)PASSWORD: {NotNull : VARCHAR(200)} */
    protected String _password;

    /** (メールアドレス)MAIL_ADDRESS: {VARCHAR(200)} */
    protected String _mailAddress;

    /** (作成日時)CREATED_DATE: {NotNull : TIMESTAMP(23, 10)} */
    protected java.sql.Timestamp _createdDate;

    /** (更新日時)MODIFIED_DATE: {NotNull : TIMESTAMP(23, 10)} */
    protected java.sql.Timestamp _modifiedDate;

    /** (バージョン番号)VERSION_NO: {NotNull : BIGINT(19) : default=[1]} */
    protected Long _versionNo;

    // -----------------------------------------------------
    //                                              Internal
    //                                              --------
    /** The modified properties for this entity. */
    protected EntityModifiedProperties _modifiedProperties = newEntityModifiedProperties();

    // ===================================================================================
    //                                                                          Table Name
    //                                                                          ==========
    public String getTableDbName() {
        return "YS_USER";
    }

    public String getTablePropertyName() { // as JavaBeansRule
        return "ysUser";
    }

    // ===================================================================================
    //                                                                              DBMeta
    //                                                                              ======
    public DBMeta getDBMeta() {
        return DBMetaInstanceHandler.findDBMeta(getTableDbName());
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
     * YS_GROUP_USER as 'ysGroupUserList'. {without lazy-load} <br />
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
     * YS_ROLE_GROUP_USER as 'ysRoleGroupUserList'. {without lazy-load} <br />
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
    //                                                                       Determination
    //                                                                       =============
    public boolean hasPrimaryKeyValue() {
        if (_id == null) { return false; }
        return true;
    }

    // ===================================================================================
    //                                                                 Modified Properties
    //                                                                 ===================
    public Set<String> getModifiedPropertyNames() {
        return _modifiedProperties.getPropertyNames();
    }

    protected EntityModifiedProperties newEntityModifiedProperties() {
        return new EntityModifiedProperties();
    }

    public void clearModifiedPropertyNames() {
        _modifiedProperties.clear();
    }

    public boolean hasModification() {
        return !_modifiedProperties.isEmpty();
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
        if (other == null || !(other instanceof BsYsUser)) { return false; }
        BsYsUser otherEntity = (BsYsUser)other;
        if (!xSV(getId(), otherEntity.getId())) { return false; }
        return true;
    }
    private boolean xSV(Object value1, Object value2) { // isSameValue()
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
    private int xCH(int result, Object value) { // calculateHashcode()
        return InternalUtil.calculateHashcode(result, value);
    }

    /**
     * @return The display string of all columns and relation existences. (NotNull)
     */
    public String toString() {
        return buildDisplayString(getClass().getSimpleName(), true, true);
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
    private String xbRDS(Entity e, String name) { // buildRelationDisplayString()
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
        if (column) { sb.append(xbuildColumnString()); }
        if (relation) { sb.append(xbuildRelationString()); }
        sb.append("@").append(Integer.toHexString(hashCode()));
        return sb.toString();
    }
    private String xbuildColumnString() {
        String c = ", ";
        StringBuilder sb = new StringBuilder();
        sb.append(c).append(getId());
        sb.append(c).append(getName());
        sb.append(c).append(getDisplayName());
        sb.append(c).append(getPassword());
        sb.append(c).append(getMailAddress());
        sb.append(c).append(getCreatedDate());
        sb.append(c).append(getModifiedDate());
        sb.append(c).append(getVersionNo());
        if (sb.length() > 0) { sb.delete(0, c.length()); }
        sb.insert(0, "{").append("}");
        return sb.toString();
    }
    private String xbuildRelationString() {
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
     * [get] (ID)ID: {PK : ID : NotNull : BIGINT(19)} <br />
     * @return The value of the column 'ID'. (Nullable)
     */
    public Long getId() {
        return _id;
    }

    /**
     * [set] (ID)ID: {PK : ID : NotNull : BIGINT(19)} <br />
     * @param id The value of the column 'ID'. (Nullable)
     */
    public void setId(Long id) {
        _modifiedProperties.addPropertyName("id");
        this._id = id;
    }

    /**
     * [get] (ユーザ名)NAME: {UQ : NotNull : VARCHAR(200)} <br />
     * @return The value of the column 'NAME'. (Nullable)
     */
    public String getName() {
        return _name;
    }

    /**
     * [set] (ユーザ名)NAME: {UQ : NotNull : VARCHAR(200)} <br />
     * @param name The value of the column 'NAME'. (Nullable)
     */
    public void setName(String name) {
        _modifiedProperties.addPropertyName("name");
        this._name = name;
    }

    /**
     * [get] (表示名)DISPLAY_NAME: {NotNull : VARCHAR(200)} <br />
     * @return The value of the column 'DISPLAY_NAME'. (Nullable)
     */
    public String getDisplayName() {
        return _displayName;
    }

    /**
     * [set] (表示名)DISPLAY_NAME: {NotNull : VARCHAR(200)} <br />
     * @param displayName The value of the column 'DISPLAY_NAME'. (Nullable)
     */
    public void setDisplayName(String displayName) {
        _modifiedProperties.addPropertyName("displayName");
        this._displayName = displayName;
    }

    /**
     * [get] (パスワード)PASSWORD: {NotNull : VARCHAR(200)} <br />
     * @return The value of the column 'PASSWORD'. (Nullable)
     */
    public String getPassword() {
        return _password;
    }

    /**
     * [set] (パスワード)PASSWORD: {NotNull : VARCHAR(200)} <br />
     * @param password The value of the column 'PASSWORD'. (Nullable)
     */
    public void setPassword(String password) {
        _modifiedProperties.addPropertyName("password");
        this._password = password;
    }

    /**
     * [get] (メールアドレス)MAIL_ADDRESS: {VARCHAR(200)} <br />
     * @return The value of the column 'MAIL_ADDRESS'. (Nullable)
     */
    public String getMailAddress() {
        return _mailAddress;
    }

    /**
     * [set] (メールアドレス)MAIL_ADDRESS: {VARCHAR(200)} <br />
     * @param mailAddress The value of the column 'MAIL_ADDRESS'. (Nullable)
     */
    public void setMailAddress(String mailAddress) {
        _modifiedProperties.addPropertyName("mailAddress");
        this._mailAddress = mailAddress;
    }

    /**
     * [get] (作成日時)CREATED_DATE: {NotNull : TIMESTAMP(23, 10)} <br />
     * @return The value of the column 'CREATED_DATE'. (Nullable)
     */
    public java.sql.Timestamp getCreatedDate() {
        return _createdDate;
    }

    /**
     * [set] (作成日時)CREATED_DATE: {NotNull : TIMESTAMP(23, 10)} <br />
     * @param createdDate The value of the column 'CREATED_DATE'. (Nullable)
     */
    public void setCreatedDate(java.sql.Timestamp createdDate) {
        _modifiedProperties.addPropertyName("createdDate");
        this._createdDate = createdDate;
    }

    /**
     * [get] (更新日時)MODIFIED_DATE: {NotNull : TIMESTAMP(23, 10)} <br />
     * @return The value of the column 'MODIFIED_DATE'. (Nullable)
     */
    public java.sql.Timestamp getModifiedDate() {
        return _modifiedDate;
    }

    /**
     * [set] (更新日時)MODIFIED_DATE: {NotNull : TIMESTAMP(23, 10)} <br />
     * @param modifiedDate The value of the column 'MODIFIED_DATE'. (Nullable)
     */
    public void setModifiedDate(java.sql.Timestamp modifiedDate) {
        _modifiedProperties.addPropertyName("modifiedDate");
        this._modifiedDate = modifiedDate;
    }

    /**
     * [get] (バージョン番号)VERSION_NO: {NotNull : BIGINT(19) : default=[1]} <br />
     * @return The value of the column 'VERSION_NO'. (Nullable)
     */
    public Long getVersionNo() {
        return _versionNo;
    }

    /**
     * [set] (バージョン番号)VERSION_NO: {NotNull : BIGINT(19) : default=[1]} <br />
     * @param versionNo The value of the column 'VERSION_NO'. (Nullable)
     */
    public void setVersionNo(Long versionNo) {
        _modifiedProperties.addPropertyName("versionNo");
        this._versionNo = versionNo;
    }
}
