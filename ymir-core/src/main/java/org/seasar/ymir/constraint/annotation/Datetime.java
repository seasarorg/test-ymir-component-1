package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.impl.DatetimeConstraint;
import org.seasar.ymir.converter.annotation.TypeConversionHint;

/**
 * リクエストパラメータの値が日付パターンにマッチしていることを表す制約アノテーションです。
 * <p>指定されている全てのリクエストパラメータの値が、
 * アノテーションのプロパティとして指定された日付パターンに全体マッチすることを要求します。
 * </p>
 * <p>制約チェックの対象とするリクエストパラメータは、
 * アノテーションが付与されているSetterメソッドに対応するプロパティの名前と同じ名前を持つリクエストパラメータと、
 * アノテーションのpropertyプロパティで名前を指定されているリクエストパラメータです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintAnnotation(type = ConstraintType.VALIDATION, component = DatetimeConstraint.class)
@TypeConversionHint
public @interface Datetime {
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
     * 日付のパターンです。
     * <p>patternプロパティと同じです。
     * patternプロパティとともに指定された場合はpatternプロパティが優先されます。
     * </p>
     * 
     * @return 日付のパターン。
     */
    String value() default "";

    /**
     * 日付のパターンです。
     * <p>書式は{@link SimpleDateFormat}の日付パターンと同じです。
     * </p>
     * 
     * @return 日付のパターン。
     * @see SimpleDateFormat
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
     * @since 1.0.7
     */
    String messageKey() default "";
}
