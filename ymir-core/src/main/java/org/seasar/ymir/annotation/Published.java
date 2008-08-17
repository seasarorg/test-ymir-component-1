package org.seasar.ymir.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * アプリケーションコードやフレームワークを拡張するコードから安定的に利用することのできる要素に付与するアノテーションです。
 * <p>このアノテーションが付与されている要素は、フレームワークのリビジョン番号（x.y.zのz）レベルのバージョンアップでは
 * 変更されないことが保証されます。
 * 変更される場合はマイナーバージョン番号（x.y.zのy）レベルのバージョンアップに伴ってまずdeprecatedされます。
 * その後、以降のマイナーバージョン番号レベルのバージョンアップにて変更・削除されます。
 * </p>
 * 
 * @since 0.9.6
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Published {
}
