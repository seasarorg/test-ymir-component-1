package org.seasar.ymir.converter;

import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.annotation.TypeConversionHint;

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
     * <p>変換できなかった場合はデフォルトの値が返されます。
     * </p>
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
     * アノテーションのうち{@link TypeConversionHint}メタアノテーションが付与されているものが渡されます。
     * @param type 変換結果の型を表すClassオブジェクト。
     * @return 変換結果。
     * @since 1.0.0
     */
    <T> T convert(Object value, Class<T> type, Annotation[] hint);

    /**
     * 指定されたオブジェクトを型変換します。
     * <p>このメソッドは<code>tryToConvert(value, type, null)</code>と同じです。
     * </p>
     * 
     * @param <T> 変換結果の型。
     * @param value 変換元の値。
     * @param type 変換結果の型を表すClassオブジェクト。
     * @return 変換結果。
     * @see #tryToConvert(Object, Class, Annotation[])
     * @since 1.0.7
     */
    <T> T tryToConvert(Object value, Class<T> type)
            throws TypeConversionException;

    /**
     * 指定されたオブジェクトを型変換します。
     * <p>変換できなかった場合は{@link TypeConversionException}がスローされます。
     * </p>
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
     * アノテーションのうち{@link TypeConversionHint}メタアノテーションが付与されているものが渡されます。
     * @param type 変換結果の型を表すClassオブジェクト。
     * @return 変換結果。
     * @since 1.0.7
     */
    <T> T tryToConvert(Object value, Class<T> type, Annotation[] hint)
            throws TypeConversionException;

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
