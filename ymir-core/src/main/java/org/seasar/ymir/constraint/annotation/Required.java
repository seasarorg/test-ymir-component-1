package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.impl.RequiredConstraint;

/**
 * リクエストパラメータの値が空でないことを表す制約アノテーションです。
 * <p>指定されている全てのリクエストパラメータがHTTPリクエストに存在してかつ値が空でないことを要求します。
 * </p>
 * <p>制約チェックの対象とするリクエストパラメータは、
 * アノテーションが付与されているSetterメソッドに対応するプロパティの名前と同じ名前を持つリクエストパラメータと、
 * アノテーションのpropertyプロパティで名前を指定されているリクエストパラメータです。
 * </p>
 * <p>リクエストパラメータ名を正規表現で与えた場合に、
 * 正規表現にマッチするリクエストパラメータが存在しないケースでは制約チェックエラーとならないことに注意して下さい。
 * 制約チェックエラーとなるのは、正規表現にマッチするリクエストパラメータが存在してかつ値が空の場合です。
 * これは、制約アノテーションにおけるリクエストパラメータ名指定においては、
 * 正規表現は制約チェックの対象とするリクエストパラメータ名を決定するために使用されるという共通のルールに従っているためです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintAnnotation(type = ConstraintType.VALIDATION, component = RequiredConstraint.class)
public @interface Required {
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
     * 空白文字列を空とみなさないかどうかです。
     * <p>このプロパティがtrueである場合は、長さ0の文字列だけを空とみなし、
     * 空白だけからなる文字列を空とみなしません。
     * falseである場合は、
     * 空白だけからなる文字列を空とみなします。
     * </p>
     * 
     * @return 空白文字列を空とみなさないかどうか。
     */
    boolean allowWhitespace() default true;

    /**
     * 正規表現で指定されているリクエストパラメータ名にマッチするリクエストパラメータの存在を必須とするかどうかです。
     * <p>このプロパティがtrueの場合は、
     * 正規表現で指定されているリクエストパラメータ名にマッチするリクエストパラメータが存在しない場合制約チェックエラーになります。
     * falseの場合は、
     * 正規表現で指定されているリクエストパラメータ名にマッチするリクエストパラメータが存在しなくても制約チェックエラーとみなしません。
     * </p>
     * 
     * @return 正規表現で指定されているリクエストパラメータ名にマッチするリクエストパラメータの存在を必須とするかどうか。
     */
    boolean matchedParameterRequired() default false;
}
