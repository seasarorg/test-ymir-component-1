package org.seasar.ymir.extension.creator;

/**
 * パスに対応するテンプレートを表すTemplateオブジェクトを構築するためのインタフェースです。
 * 
 * @author YOKOTA Takehiko
 */
public interface TemplateProvider {
    /**
     * 指定されたパスに対応するTemplateオブジェクトを構築して返します。
     * 
     * @param path パス。末尾に「/」がつくこともあります。
     * @return 構築したTemplateオブジェクト。
     */
    Template getTemplate(String path);
}
