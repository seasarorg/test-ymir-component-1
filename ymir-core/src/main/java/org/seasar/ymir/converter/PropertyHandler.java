package org.seasar.ymir.converter;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * JavaBeansのプロパティを扱うためのインタフェースです。
 */
public interface PropertyHandler {
    /**
     * プロパティの型を返します。
     * 
     * @return プロパティの型。
     */
    Class<?> getPropertyType();

    /**
     * プロパティを読み出すためのメソッドを返します。
     * <p>プロパティが入れ子になっている場合、このメソッドが返すメソッドはプロパティを読み出す
     * 一番末端のメソッドになります。
     * 例えば<code>hoe.fuga</code>というプロパティの場合、
     * このメソッドはgetHoe()が返すオブジェクトのクラスのgetFuga()メソッドを返します。
     * 
     * @return プロパティを読み出すためのメソッド。
     * プロパティが読み出し用のメソッドを持たない場合はnullを返します。
     */
    Method getReadMethod();

    /**
     * プロパティを書き出すためのメソッドを返します。
     * <p>プロパティが入れ子になっている場合、このメソッドが返すメソッドはプロパティを書き出す
     * 一番末端のメソッドになります。
     * 例えば<code>hoe.fuga</code>というプロパティの場合、
     * このメソッドはgetHoe()が返すオブジェクトのクラスのsetFuga()メソッドを返します。
     * 
     * @return プロパティを書き出すためのメソッド。
     * プロパティが書き出し用のメソッドを持たない場合はnullを返します。
     */
    Method getWriteMethod();

    /**
     * プロパティに対応するPropertyDescriptorを返します。
     * <p>プロパティが入れ子になっている場合、このメソッドは
     * 一番末端のプロパティに関するPropertyDescriptorを返します。
     * 例えば<code>hoe.fuga</code>というプロパティの場合、
     * このメソッドはgetHoe()が返すオブジェクトのクラスのfugaプロパティのPropertyDescriptorを返します。
     * 
     * @return プロパティに対応するPropertyDescriptor。
     */
    PropertyDescriptor getPropertyDescriptor();

    /**
     * プロパティに値を設定します。
     * <p>値の自動変換は行なわないため、値の型はプロパティの型にキャスト可能である必要があります。
     * </p>
     * 
     * @param value 設定する値。
     * @throws IllegalAccessException アクセスできない場合。
     * @throws IllegalArgumentException 引数の型が合わない場合。
     * @throws InvocationTargetException 設定に失敗した場合。
     * @throws NoSuchMethodException 設定のためのメソッドが存在しない場合。
     */
    void setProperty(Object value) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException;

    /**
     * プロパティの値を返します。
     * 
     * @throws IllegalAccessException アクセスできない場合。
     * @throws InvocationTargetException 値の取得に失敗した場合。
     * @throws NoSuchMethodException プロパティ取得のためのメソッドが存在しない場合。
     * @since 1.0.7
     */
    Object getProperty() throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException;
}
