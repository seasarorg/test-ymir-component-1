package org.seasar.ymir.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.plugin.Plugin;

/**
 * プラグインの適用を指定するためのアノテーションに付与するメタアノテーションです。
 * 
 * @author yokota
 * @since 0.9.6
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface PluginAnnotation {
    Class<? extends Plugin<?>> value();
}
