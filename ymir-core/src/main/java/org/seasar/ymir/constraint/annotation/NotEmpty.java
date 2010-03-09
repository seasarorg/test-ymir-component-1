package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.impl.NotEmptyConstraint;
import org.seasar.ymir.constraint.impl.RequiredConstraint;

/**
 * リクエストパラメータの値が空でないことを表す制約アノテーションです。
 * <p>指定されている全てのリクエストパラメータについて、HTTPリクエストに存在している場合に値が空でないことを要求します。
 * </p>
 * <p>このアノテーションと{@link Required}アノテーションの違いは、
 * このアノテーションではパラメータが存在しない場合にはエラーにならないのに対し、
 * {@link Required}アノテーションではパラメータが存在しない場合にエラーになる点です。
 * </p>
 * <p>制約チェックの対象とするリクエストパラメータは、
 * アノテーションが付与されているSetterメソッドに対応するプロパティの名前と同じ名前を持つリクエストパラメータと、
 * アノテーションのvalueプロパティで名前を指定されているリクエストパラメータです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintAnnotation(type = ConstraintType.VALIDATION, component = NotEmptyConstraint.class)
public @interface NotEmpty {
    /**
     * リクエストパラメータ名です。
     * <p>先頭が「#」で始まる名前は正規表現パターンと見なされます。
     * 実際に送信されてきたリクエストパラメータのうち、
     * 「#」を取り除いた正規表現パターンに全体マッチするもの全てが制約チェックの対象になります。
     * </p>
     * 
     * @return リクエストパラメータ名。
     */
    String[] value() default {};

    /**
     * 同一のリクエストパラメータに複数の値が指定されている場合に全ての値が空でないことをチェックするかどうかです。
     * <p>このプロパティがtrueである場合は、
     * 同一のリクエストパラメータの複数の値のうちどれか1つでも空であるものがある場合に制約チェックエラーになります。
     * falseである場合は、
     * 同一のリクエストパラメータの複数の値のうちどれか1つでも空でなければ制約チェックエラーになりません。
     * </p>
     * 
     * @return 同一のリクエストパラメータに複数の値が指定されている場合に全ての値が空でないことをチェックするかどうか。
     */
    boolean completely() default false;

    /**
     * 半角空白文字を空とみなさないかどうかです。
     * <p>このプロパティがtrueである場合は、例えば半角空白だけからなる文字列を空とみなしません。
     * falseである場合は、
     * 半角空白だけからなる文字列を空とみなします。
     * </p>
     * 
     * @return 半角空白文字列を空とみなさないかどうか。
     */
    boolean allowWhitespace() default true;

    /**
     * 全角空白文字を空とみなさないかどうかです。
     * <p>このプロパティがtrueである場合は、例えば全角空白だけからなる文字列を空とみなしません。
     * falseである場合は、
     * 全角空白だけからなる文字列を空とみなします。
     * このプロパティがfalseでかつ{@link #allowWhitespace()}もfalseの場合は、
     * 半角空白と全角空白だけからなる文字列を空とみなします。
     * </p>
     * 
     * @return 全角空白文字列を空とみなさないかどうか。
     */
    boolean allowFullWidthWhitespace() default true;

    /**
     * エラーメッセージのキーです。
     * <p>通常はエラーメッセージのキーは「error.constraint.XXXX」ですが、
     * 例えばこのメンバの値を「abc」とするとキーが「error.constraint.XXXX.abc」になります。
     * </p>
     * <p>キー全体を指定したい場合は先頭に「!」をつけて下さい。
     * 例えばメンバの値を「!error.custom」とするとキーは「error.custom」になります。
     * </p>
     * 
     * @return エラーメッセージのキー。
     */
    String messageKey() default "";
}