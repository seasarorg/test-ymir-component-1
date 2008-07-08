package org.seasar.ymir;

import java.lang.reflect.InvocationTargetException;

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
     * @param name プロパティ名。
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
     * @param name プロパティ名。
     * @param value 設定する値。nullを指定することもできます。
     * @throws IllegalAccessException プロパティを設定するためのアクセッサにアクセスする権限がない場合。
     * @throws InvocationTargetException 値の設定中にエラーが発生した場合。
     */
    void copyProperty(Object bean, String name, Object value)
            throws IllegalAccessException, InvocationTargetException;

    /**
     * 指定された文字列値を型変換します。
     * 
     * @param <T> 変換結果の型。
     * @param value 変換元の値。
     * @param type 変換結果の型を表すClassオブジェクト。
     * @return 変換結果。
     */
    <T> T convert(String value, Class<T> type);

    /**
     * 指定された文字列の配列を型変換します。
     * 
     * @param <T> 変換結果の型。
     * @param value 変換元の文字列配列。
     * @param type 変換結果の型を表すClassオブジェクト。
     * @return 変換結果。
     */
    <T> T[] convert(String[] values, Class<T> type);

    /**
     * 指定された値を文字列に変換します。
     * 
     * @param value 変換元の値。
     * @return 変換結果。
     */
    String convert(Object value);
}
