package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 画面テンプレート中に他の画面テンプレートをインクルードすることを指定するためのアノテーションです。
 * <p>画面テンプレート中に他の画面テンプレートをインクルードする場合、
 * インクルードされる画面テンプレートを描画する準備を行なうためのPageクラスを
 * インクルードする側のPageクラスに知らせる必要があります。
 * このアノテーションはインクルードする側のPageクラスにインクルードされる側のPageクラスを知らせるために使用されます。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Include {
    /**
     * インクルードされる画面テンプレートに対応するPageクラスのClassオブジェクトです。
     * 
     * @return インクルードされる画面テンプレートに対応するPageクラスのClassオブジェクト。
     */
    Class<?>[] value();
}
