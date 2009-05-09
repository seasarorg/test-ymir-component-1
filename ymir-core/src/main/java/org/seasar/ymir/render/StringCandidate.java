package org.seasar.ymir.render;

import static org.seasar.ymir.util.StringUtils.asString;

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
public class StringCandidate extends AbstractCandidate {
    private static final long serialVersionUID = 1L;

    private String label_;

    public StringCandidate() {
    }

    public StringCandidate(Object value) {
        this(value, asString(value), false);
    }

    public StringCandidate(Object value, boolean selected) {
        this(value, asString(value), selected);
    }

    public StringCandidate(Object value, String label, boolean selected) {
        setValueObject(value);
        setSelected(selected);
        label_ = label;
    }

    public String getLabel() {
        return label_;
    }

    public void setLabel(String label) {
        label_ = label;
    }
}
