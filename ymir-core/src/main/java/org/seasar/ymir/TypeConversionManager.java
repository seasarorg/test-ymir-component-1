package org.seasar.ymir;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

import org.seasar.ymir.annotation.Conversion;

/**
 * 値の型変換を統一的に行なうためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface TypeConversionManager {
    /**
     * 指定されたオブジェクトのプロパティとして値を設定します。
     * <p>値の型がStringまたはString[]である場合に限って、
     * プロパティ型と違う場合は自動的に型変換を行ないます。
     * </p>
     * <p>処理対象のオブジェクトが指定されたプロパティを持たない場合やプロパティが読み込み専用型であった場合は何もしません。
     * </p>
     * <p>{@link #copyProperty(Object, String, Object)}と異なり、
     * valueの型はStringかString[]である必要があります。
     * </p>
     * 
     * @param bean 対象となるオブジェクト。
     * @param name プロパティ名。Apache Commons BeanUtilsの
     * 拡張プロパティ名（ネストあり、インデックスあり、キーあり）を指定することができます。
     * @param value 設定する値。nullを指定することもできます。
     * @throws IllegalAccessException プロパティを設定するためのアクセッサにアクセスする権限がない場合。
     * @throws InvocationTargetException 値の設定中にエラーが発生した場合。
     */
    void setProperty(Object bean, String name, Object value)
            throws IllegalAccessException, InvocationTargetException;

    /**
     * 指定されたオブジェクトのプロパティとして値を設定します。
     * <p>値の型とプロパティの型が違う場合、自動的に型変換を行ないます。
     * </p>
     * <p>処理対象のオブジェクトが指定されたプロパティを持たない場合やプロパティが読み込み専用型であった場合は何もしません。
     * </p>
     * 
     * @param bean 対象となるオブジェクト。
     * @param name プロパティ名。Apache Commons BeanUtilsの
     * 拡張プロパティ名（ネストあり、インデックスあり、キーあり）を指定することができます。
     * @param value 設定する値。nullを指定することもできます。
     * @throws IllegalAccessException プロパティを設定するためのアクセッサにアクセスする権限がない場合。
     * @throws InvocationTargetException 値の設定中にエラーが発生した場合。
     */
    void copyProperty(Object bean, String name, Object value)
            throws IllegalAccessException, InvocationTargetException;

    /**
     * 指定された文字列値を型変換します。
     * 
     * @deprecated 代わりに{@link #convert(Object, Class)}を使って下さい。
     * @param <T> 変換結果の型。
     * @param value 変換元の値。
     * @param type 変換結果の型を表すClassオブジェクト。
     * @return 変換結果。
     */
    @Deprecated
    <T> T convert(String value, Class<T> type);

    /**
     * 指定された文字列の配列を型変換します。
     * 
     * @deprecated 代わりに{@link #convert(Object, Class)}を使って下さい。
     * @param <T> 変換結果の型。
     * @param value 変換元の文字列配列。
     * @param type 変換結果の型を表すClassオブジェクト。
     * @return 変換結果。
     */
    @Deprecated
    <T> T[] convert(String[] values, Class<T> type);

    /**
     * 指定された値を文字列に変換します。
     *
     * @deprecated 代わりに{@link #convert(Object, Class)}を使って下さい。
     * @param value 変換元の値。
     * @return 変換結果。
     */
    @Deprecated
    String convert(Object value);

    /**
     * 指定されたオブジェクトを型変換します。
     * <p>このメソッドは<code>convert(value, type, null)</code>と同じです。
     * </p>
     * 
     * @param <T> 変換結果の型。
     * @param value 変換元の値。
     * @param type 変換結果の型を表すClassオブジェクト。
     * @return 変換結果。
     * @see #convert(Object, Class, Annotation[])
     * @since 0.9.6
     */
    <T> T convert(Object value, Class<T> type);

    /**
     * 指定されたオブジェクトを型変換します。
     * <p>変換のためのヒントが指定された場合、
     * ヒントに基づいて変換の挙動が変わります。
     * ヒントは{@link TypeConversionManager}の実装依存ですので、
     * 変換の挙動をケースによって変化させたい場合はそのような
     * {@link TypeConversionManager}を実装して
     * デフォルトの実装と差し替えて下さい。
     * </p>
     * 
     * @param <T> 変換結果の型。
     * @param value 変換元の値。
     * @param hint 変換のためのヒント。nullを指定することもできます。
     * ヒントとしては変換がメソッドなどを経由して行なわれる際に対象メソッドに付与されている
     * アノテーションのうち{@link Conversion}メタアノテーションが付与されているものが渡されます。
     * @param type 変換結果の型を表すClassオブジェクト。
     * @return 変換結果。
     * @since 1.0.0
     */
    <T> T convert(Object value, Class<T> type, Annotation[] hint);

    /**
     * 指定されたオブジェクトの指定された名前のプロパティにアクセスするための
     * {@link PropertyHandler}オブジェクトを返します。
     * 
     * @param bean 対象となるオブジェクト。
     * @param name プロパティ名。Apache Commons BeanUtilsの
     * 拡張プロパティ名（ネストあり、インデックスあり、キーあり）を指定することができます。
     * @return {@link PropertyHandler}オブジェクト。
     * 指定された名前に対応するプロパティが存在しない場合はnullを返します。
     */
    PropertyHandler getPropertyHandler(Object bean, String name);
}
