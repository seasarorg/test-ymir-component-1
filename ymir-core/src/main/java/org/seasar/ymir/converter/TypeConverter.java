package org.seasar.ymir.converter;

import java.lang.annotation.Annotation;

/**
 * オブジェクトの型の変換を行なうためのインタフェースです。
 * 
 * @author yokota
 * @since 1.0.0
 *
 * @param <T> 変換対象となる型。
 */
public interface TypeConverter<T> {
    /**
     * 変換対象である型を返します。
     * 
     * @return 変換対象である型。
     */
    Class<T> getType();

    /**
     * 指定された任意の型のオブジェクトをこのインタフェースに対応付けられている型のオブジェクトに変換します。
     * <p>変換できなかった場合はデフォルトの値が返されます。
     * </p>
     * 
     * @param value 変換元のオブジェクト。nullを指定することもできます。
     * @param hint 変換のためのヒント。nullを指定することはできません。
     * 指定したくない場合は空の配列を指定して下さい。
     * @return 変換結果。
     */
    T convert(Object value, Annotation[] hint);

    /**
     * 指定された任意の型のオブジェクトをこのインタフェースに対応付けられている型のオブジェクトに変換します。
     * <p>変換できなかった場合は{@link TypeConversionException}がスローされます。
     * </p>
     * 
     * @param value 変換元のオブジェクト。nullを指定することもできます。
     * @param hint 変換のためのヒント。nullを指定することはできません。
     * 指定したくない場合は空の配列を指定して下さい。
     * @return 変換結果。
     * @throws TypeConversionException 変換できなかった場合。
     * @since 1.0.7
     */
    T tryToConvert(Object value, Annotation[] hint)
            throws TypeConversionException;

    /**
     * このインタフェースに対応付けられている型のオブジェクトを文字列に変換します。
     * 
     * @param value 変換元のオブジェクト。nullを指定してはいけません。
     * @param hint 変換のためのヒント。nullを指定することはできません。
     * 指定したくない場合は空の配列を指定して下さい。
     * @return 変換結果。
     */
    String convertToString(T value, Annotation[] hint);
}
