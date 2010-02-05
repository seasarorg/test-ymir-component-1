package org.seasar.ymir.token.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.annotation.ConstraintAnnotation;
import org.seasar.ymir.token.InvalidTokenRuntimeException;
import org.seasar.ymir.token.constraint.impl.TokenRequiredConstraint;

/**
 * トランザクショントークンが一致することを表す制約アノテーションです。
 * <p>リクエストパラメータに指定されているトランザクショントークンと
 * セッションに保存されているトランザクショントークンが一致することを要求します。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintAnnotation(type = ConstraintType.VALIDATION, component = TokenRequiredConstraint.class)
public @interface TokenRequired {
    /**
     * トークンキーの名前です。
     * @return トークンキーの名前。
     */
    String value() default "";

    /**
     * トークンのチェックをした後にセッションに保存されているトークンを削除するかどうか。
     * <p>デフォルトはtrueです。
     * </p>
     * 
     * @return トークンのチェックをした後にセッションに保存されているトークンを削除するかどうか。
     * @since 1.0.3
     */
    boolean reset() default true;

    /**
     * トークンが不正だった場合に例外をスローするかどうか。
     * <p>この要素の値がtrueだと、トークンが不正だった場合に{@link InvalidTokenRuntimeException}がスローされます。
     * falseの場合は例外はスローされず、通常のバリデーションエラーと同じようにエラー内容がNotesに追加されます。
     * </p>
     * <p>デフォルトはfalseです。
     * </p>
     * 
     * @return トークンが不正だった場合に例外をスローするかどうか。
     * @since 1.0.3
     */
    boolean throwException() default false;

    /**
     * エラーメッセージのキーです。
     * <p>通常はエラーメッセージのキーは「error.constraint.XXX」ですが、
     * 例えばこのメンバの値を「abc」とするとキーが「error.constraint.XXX.abc」になります。
     * </p>
     * <p>キー全体を指定したい場合は先頭に「!」をつけて下さい。
     * 例えばメンバの値を「!error.custom」とするとキーは「error.custom」になります。
     * </p>
     * 
     * @return エラーメッセージのキー。
     * @since 1.0.7
     */
    String messageKey() default "";
}
