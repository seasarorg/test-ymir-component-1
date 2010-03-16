package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 開発時の自動生成機能を抑制するためのアノテーションです。
 * <p>ページクラスにこのアノテーションを付与することによって、そのページに対するリクエストに関して
 * 自動生成ロジックが動作しないようになります。
 * </p>
 * 
 * @since 1.0.7
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SuppressUpdating {
}
