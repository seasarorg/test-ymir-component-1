package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.impl.MatchedConstraint;

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
     * patternプロパティとともに指定された場合はpatternプロパティが優先されます。
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
     * 
     * @return エラーメッセージのキー。
     */
    String messageKey() default "";
}
