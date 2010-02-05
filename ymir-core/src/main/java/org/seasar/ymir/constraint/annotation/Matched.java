package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.impl.MatchedConstraint;
import org.seasar.ymir.converter.annotation.TypeConversionHint;

/**
 * リクエストパラメータの値が指定された正規表現パターンにマッチしていることを表す制約アノテーションです。
 * <p>指定されている全てのリクエストパラメータの値が、
 * アノテーションのプロパティとして指定された正規表現パターンに全体マッチすることを要求します。
 * </p>
 * <p>制約チェックの対象とするリクエストパラメータは、
 * アノテーションが付与されているSetterメソッドに対応するプロパティの名前と同じ名前を持つリクエストパラメータと、
 * アノテーションのpropertyプロパティで名前を指定されているリクエストパラメータです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintAnnotation(type = ConstraintType.VALIDATION, component = MatchedConstraint.class)
@TypeConversionHint
public @interface Matched {
    /**
     * リクエストパラメータ名です。
     * <p>先頭が「#」で始まる名前は正規表現パターンと見なされます。
     * 実際に送信されてきたリクエストパラメータのうち、
     * 「#」を取り除いた正規表現パターンに全体マッチするもの全てが制約チェックの対象になります。
     * </p>
     * 
     * @return リクエストパラメータ名。
     */
    String[] property() default {};

    /**
     * 正規表現パターンです。
     * <p>patternプロパティと同じです。
     * patternメンバとともに指定された場合はpatternメンバが優先されます。
     * </p>
     * 
     * @return 正規表現パターン。
     */
    String value() default "";

    /**
     * 正規表現パターンです。
     * 
     * @return 正規表現パターン。
     */
    String pattern() default "";

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
