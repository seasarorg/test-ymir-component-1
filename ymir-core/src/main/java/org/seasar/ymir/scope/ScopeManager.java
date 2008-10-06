package org.seasar.ymir.scope;

import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.converter.annotation.TypeConversionHint;

/**
 * スコープに関する操作を行なうためのインタフェースです。
 * 
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.0
 */
public interface ScopeManager {
    /**
     * 指定されたスコープから指定された名前とタイプに対応する属性の値を取り出して返します。
     * <p>このメソッドは<code>getAttribute(scope, name, type, null, required, convertNullToDefaultValueWhereTypeIsPrimitive)</code>
     * と同じです。
     * </p>
     * 
     * @param scope スコープ。
     * @param name 属性の名前。
     * @param type 属性のタイプ。
     * @param required 属性の値が存在する必要があるかどうか。
     * @param convertNullToDefaultValueWhereTypeIsPrimitive typeがプリミティブ型である場合、
     * 値が見つからなかったかnullの時にデフォルト値に変換するかどうか。
     * @return 属性の値。
     * @throws AttributeNotFoundRuntimeException requiredがtrueである場合で値が見つからなかったかnullであった時。
     */
    <T> T getAttribute(Scope scope, String name, Class<T> type,
            boolean required,
            boolean convertNullToDefaultValueWhereTypeIsPrimitive)
            throws AttributeNotFoundRuntimeException;

    /**
     * 指定されたスコープから指定された名前とタイプに対応する属性の値を取り出して返します。
     * <p>値は指定されたヒントに基づいて指定されたタイプに変換されて返されます。
     * </p>
     * <p>requiredがtrueである場合、値が見つからなかったかnullであった時はAttributeNotFoundRuntimeExceptionがスローされます。
     * </p>
     * <p>requiredがfalseかつconvertNullToDefaultValueWhereTypeIsPrimitiveがtrueかつ
     * typeがプリミティブ型である場合、値が見つからなかったかnullであった時はプリミティブ型に対応するデフォルト値を返します。
     * </p>
     * 
     * @param scope スコープ。
     * @param name 属性の名前。
     * @param type 属性のタイプ。
     * @param hint 変換のためのヒント。nullを指定することもできます。
     * ヒントとしては変換がメソッドなどを経由して行なわれる際に対象メソッドに付与されている
     * アノテーションのうち{@link TypeConversionHint}メタアノテーションが付与されているものが渡されます。
     * @param required 属性の値が存在する必要があるかどうか。
     * @param convertNullToDefaultValueWhereTypeIsPrimitive typeがプリミティブ型である場合、
     * 値が見つからなかったかnullの時にデフォルト値に変換するかどうか。
     * @return 属性の値。
     * @throws AttributeNotFoundRuntimeException requiredがtrueである場合で値が見つからなかったかnullであった時。
     * @see TypeConversionManager#convert(Object, Class, Annotation[])
     */
    <T> T getAttribute(Scope scope, String name, Class<T> type,
            Annotation[] hint, boolean required,
            boolean convertNullToDefaultValueWhereTypeIsPrimitive)
            throws AttributeNotFoundRuntimeException;

    /**
     * 指定されたクラスについてのスコープ関連のメタ情報を保持する{@link ScopeMetaData}オブジェクトを返します。
     * 
     * @param clazz クラス。nullを指定してはいけません。
     * @return {@link ScopeMetaData}オブジェクト。
     * @see ScopeMetaData
     */
    ScopeMetaData getMetaData(Class<?> clazz);
}
