package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.impl.LengthConstraint;

/**
 * リクエストパラメータの値が指定された長さの条件を満たしていることを表す制約アノテーションです。
 * <p>指定されている全てのリクエストパラメータの値の長さが、
 * アノテーションのプロパティとして指定された最小値以上最大値以下であることを要求します。
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
@ConstraintAnnotation(type = ConstraintType.VALIDATION, component = LengthConstraint.class)
public @interface Length {
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
     * 値の最大値です。
     * <p>maxプロパティと同じです。
     * maxプロパティとともに指定された場合はmaxプロパティが優先されます。
     * </p>
     * 
     * @return 値の最大値。
     */
    int value() default Integer.MAX_VALUE;

    /**
     * 値の最小値です。
     * 
     * @return 値の最小値。
     */
    int min() default 0;

    /**
     * 値の最大値です。
     * 
     * @return 値の最大値。
     */
    int max() default Integer.MAX_VALUE;

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
