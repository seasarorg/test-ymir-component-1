package org.seasar.ymir.render;

import static org.seasar.ymir.util.StringUtils.asString;

import java.io.Serializable;

import org.seasar.ymir.util.StringUtils;

/**
 * シンプルなCandidateインタフェースの実装クラスです。
 * <p>候補に付加情報を関連付ける必要がなく文字列値だけを関連付ければ良いケースに
 * 使用することができます。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 * @see Candidate
 * @see Selector
 */
public class StringCandidate implements Candidate, Serializable {
    private static final long serialVersionUID = 1L;

    private String value_;

    private boolean selected_;

    public StringCandidate() {
    }

    public StringCandidate(Object value) {
        this(value, false);
    }

    public StringCandidate(Object value, boolean selected) {
        value_ = asString(value);
        selected_ = selected;
    }

    public String getValue() {
        return value_;
    }

    public void setValue(String value) {
        value_ = value;
    }

    public void setValueObject(Object value) {
        setValue(asString(value));
    }

    public boolean isSelected() {
        return selected_;
    }

    public void setSelected(boolean selected) {
        selected_ = selected;
    }
}
