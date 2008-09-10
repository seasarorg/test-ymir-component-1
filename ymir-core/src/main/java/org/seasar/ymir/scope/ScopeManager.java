package org.seasar.ymir.scope;


public interface ScopeManager {
    /**
     * 指定されたスコープから指定された名前とタイプに対応する属性の値を取り出して返します。
     * <p>値は指定されたタイプに変換されて返されます。
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
}
