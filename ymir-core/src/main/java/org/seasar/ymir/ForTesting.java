package org.seasar.ymir;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * テストのために用意されている要素に付与するアノテーションです。
 * <p>このアノテーションが付与されている要素はテスト以外で使用しないで下さい。
 * </p>
 * <p>ソースコードの可読性向上のために、テストのために用意されている要素にはこのアノテーションを付与するようにして下さい。
 * </p>
 * 
 * @since 0.9.6
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.SOURCE)
public @interface ForTesting {
}
