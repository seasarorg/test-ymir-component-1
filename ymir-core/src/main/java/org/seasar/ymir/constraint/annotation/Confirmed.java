package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.impl.ConfirmedConstraint;

/**
 * リクエストパラメータの値が全て一致していることを表す制約アノテーションです。
 * <p>指定されている全てのリクエストパラメータの値が一致することを要求します。
 * </p>
 * <p>制約チェックの対象とするリクエストパラメータは、
 * アノテーションが付与されているSetterメソッドに対応するプロパティの名前と同じ名前を持つリクエストパラメータと、
 * アノテーションのvalueプロパティで名前を指定されているリクエストパラメータです。
 * </p>
 * <p>この制約は、例えば確認のためにメールアドレスを2回入力させるようなフォームを処理するために利用することができます。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintAnnotation(type = ConstraintType.VALIDATION, component = ConfirmedConstraint.class)
public @interface Confirmed {
    /**
     * リクエストパラメータ名です。
     * <p>先頭が「#」で始まる名前は正規表現パターンと見なされます。
     * 実際に送信されてきたリクエストパラメータのうち、
     * 「#」を取り除いた正規表現パターンに全体マッチするもの全てが制約チェックの対象になります。
     * </p>
     * 
     * @return リクエストパラメータ名。
     */
    String[] value();

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
