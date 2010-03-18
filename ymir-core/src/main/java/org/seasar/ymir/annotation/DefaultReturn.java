package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Pageのアクションのデフォルトの返り値を指定するためのアノテーションです。
 * <p>このアノテーションはPageクラスのアクションメソッドに付与したり
 * Pageクラスに付与したりすることができます。
 * </p>
 * <p>デフォルトの返り値の優先順位は、
 * <ol>
 * <li>アクションメソッドに付与された@DefaultReturn</li>
 * <li>Pageクラスに付与された@DefaultReturn</li>
 * <li>パスマッピングのdefaultReturnValue</li>
 * </ol>
 * となります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
public @interface DefaultReturn {
    /**
     * デフォルトの返り値です。
     * <p>パスマッピングのdefaultReturnValueと同じように、
     * 変数のプレースホルダを記述することができます。
     * </p>
     * 
     * @return デフォルトの返り値。
     */
    String value();
}
