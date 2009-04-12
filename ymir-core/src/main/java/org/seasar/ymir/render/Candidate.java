package org.seasar.ymir.render;

import java.io.Serializable;

/**
 * {@list Selector}クラスに設定する候補オブジェクトを表すインタフェースです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public interface Candidate {
    /**
     * この候補を識別する値を返します。
     * 
     * @return この候補を識別する値。nullを返してはいけません。
     */
    String getValue();

    /**
     * この候補を識別する値を設定します。
     * 
     * @value この候補を識別する値。nullを設定してはいけません。
     */
    void setValue(String value);

    /**
     * この候補が選択されているかどうかを返します。
     * 
     * @return この候補が選択されているかどうか。
     */
    boolean isSelected();

    /**
     * この候補が選択されているかどうかを設定します。
     * 
     * @param selected この候補が選択されているかどうか。
     */
    void setSelected(boolean selected);
}
