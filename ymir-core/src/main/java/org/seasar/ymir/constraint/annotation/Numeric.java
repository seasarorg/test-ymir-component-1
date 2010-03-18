package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.impl.NumericConstraint;

/**
 * リクエストパラメータの値が数値であることを表す制約アノテーションです。
 * <p>指定されている全てのリクエストパラメータの値が、
 * アノテーションのプロパティとして指定された条件を満たす数値であることを要求します。
 * </p>
 * <p>制約チェックの対象とするリクエストパラメータは、
 * アノテーションが付与されているSetterメソッドに対応するプロパティの名前と同じ名前を持つリクエストパラメータと、
 * アノテーションのvalueプロパティとpropertyプロパティで名前を指定されているリクエストパラメータです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintAnnotation(type = ConstraintType.VALIDATION, component = NumericConstraint.class)
public @interface Numeric {
    /**
     * リクエストパラメータ名です。
     * <p>propertyプロパティとともに指定された場合でも両方とも有効です。
     * </p>
     * <p>先頭が「#」で始まる名前は正規表現パターンと見なされます。
     * 実際に送信されてきたリクエストパラメータのうち、
     * 「#」を取り除いた正規表現パターンに全体マッチするもの全てが制約チェックの対象になります。
     * </p>
     *  
     * @return リクエストパラメータ名。
     */
    String[] value() default {};

    /**
     * リクエストパラメータ名です。
     * <p>valueプロパティとともに指定された場合でも両方とも有効です。
     * </p>
     * <p>先頭が「#」で始まる名前は正規表現パターンと見なされます。
     * 実際に送信されてきたリクエストパラメータのうち、
     * 「#」を取り除いた正規表現パターンに全体マッチするもの全てが制約チェックの対象になります。
     * </p>
     * 
     * @return リクエストパラメータ名。
     */
    String[] property() default {};

    /**
     * 数値が整数であることを要求するかどうかです。
     * <p>デフォルト値はtrueです。
     * </p>
     * 
     * @return 数値が整数であることを要求するかどうか。
     */
    boolean integer() default true;

    /**
     * 数値の最小値です。
     * <p>数値がこのプロパティで指定された値よりも大きいことを要求します。
     * </p>
     * 
     * @return 数値の最小値。
     * @see #greaterEqual()
     */
    double greaterThan() default -Double.MAX_VALUE;

    /**
     * 数値の最小値です。
     * <p>数値がこのプロパティで指定された値以上であることを要求します。
     * </p>
     * 
     * @return 数値の最小値。
     * @see #greaterThan()
     */
    double greaterEqual() default -Double.MAX_VALUE;

    /**
     * 数値の最小値です。
     * <p>数値がこのプロパティで指定された値よりも小さいことを要求します。
     * </p>
     * 
     * @return 数値の最小値。
     * @see #lessEqual()
     */
    double lessThan() default Double.MAX_VALUE;

    /**
     * 数値の最小値です。
     * <p>数値がこのプロパティで指定された値以下であることを要求します。
     * </p>
     * 
     * @return 数値の最小値。
     * @see #lessThan()
     */
    double lessEqual() default Double.MAX_VALUE;

    /**
     * エラーメッセージのキーです。
     * <p>通常はエラーメッセージのキーは「error.constraint.XXXXX」ですが、
     * 例えばこのメンバの値を「abc」とするとキーが「error.constraint.XXXXX.abc」になります。
     * </p>
     * <p>キー全体を指定したい場合は先頭に「!」をつけて下さい。
     * 例えばメンバの値を「!error.custom」とするとキーは「error.custom」になります。
     * </p>
     * 
     * @return エラーメッセージのキー。
     * @since 1.0.7
     */
    String messageKey() default "";

    /**
     * エラーメッセージのカテゴリに付与する接頭語です。
     * <p>あるパラメータに関するエラーメッセージはパラメータ名と同じ名前のカテゴリに属するようにNotesオブジェクトに追加されます。
     * このメンバを指定すると、このメンバの値がカテゴリ名の先頭に付与されるようになります。
     * </p>
     * 
     * @return カテゴリに付与する接頭語。
     * @since 1.0.7
     */
    String namePrefixOnNote() default "";
}
