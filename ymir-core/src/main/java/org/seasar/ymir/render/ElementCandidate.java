package org.seasar.ymir.render;

import static org.seasar.ymir.util.StringUtils.asString;

import java.io.Serializable;

import org.seasar.ymir.util.StringUtils;

/**
 * オブジェクトを内包するCandidateインタフェースの実装クラスです。
 * <p>候補に文字列値以外の付加情報を関連付ける必要があるケースに使用することができます。
 * </p>
 * 
 * @param <E> 付加情報を表す型。
 * @author YOKOTA Takehiko
 * @since 1.0.3
 * @see Candidate
 * @see Selector
 */
public class ElementCandidate<E> extends AbstractCandidate {
    private static final long serialVersionUID = 1L;

    private E element_;

    public ElementCandidate() {
    }

    public ElementCandidate(E element, Object value) {
        this(element, value, false);
    }

    public ElementCandidate(E element, Object value, boolean selected) {
        element_ = element;
        setValueObject(value);
        setSelected(selected);
    }

    /**
     * 付加情報を返します。
     * 
     * @return 付加情報。
     */
    public E getElement() {
        return element_;
    }

    /**
     * 付加情報を設定します。
     * 
     * @param element 付加情報。
     * @return このオブジェクト自身。
     */
    public ElementCandidate<E> setElement(E element) {
        element_ = element;
        return this;
    }
}
