package org.seasar.ymir;

/**
 * 属性を表すクラスです。
 * <p>属性は、名前と値の組です。
 * </p>
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class Attribute {
    private String name_;

    private Object value_;

    public Attribute(String name, Object value) {
        name_ = name;
        value_ = value;
    }

    public String getName() {
        return name_;
    }

    public Object getValue() {
        return value_;
    }
}
