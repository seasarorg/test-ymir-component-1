package org.seasar.ymir.gson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * アクションの返り値をJSONとして返却することを表すアノテーションです。
 * <p>リクエストがJSONの場合は自動的にアクションの返り値がJSONとして返却されますが、
 * そうでない場合は返り値は通常のYmirのプロセスで解釈されます。
 * アクションメソッドにこのアノテーションを付与すると、返り値が強制的にJSONとして返却されるようになります。
 * </p>
 * 
 * @author skirnir
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD })
public @interface JSONResponse {
}
