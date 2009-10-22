package org.seasar.ymir;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@link HttpServletResponseFilter}のファクトリクラスです。
 * 
 * @author skirnir
 * @since 1.0.7
 */
public interface HttpServletResponseFilterFactory {
    /**
     * 何も特別な処理をしないようなHttpServletResponseFilterのインスタンスを構築して返します。
     * 
     * @param response HttpServletResponse。
     * @return 構築したHttpServletResponseFilter。
     */
    HttpServletResponseFilter newAsIsResponseFilter(HttpServletResponse response);

    /**
     * Updaterを実行するためのHttpServletResponseFilterのインスタンスを構築して返します。
     * 
     * @param request HttpServletRequest。
     * @param response HttpServletResponse。
     * @param updaters Updaterの配列。
     * @return 構築したHttpServletResponseFilter。
     */
    HttpServletResponseFilter newUpdaterResponseFilter(
            HttpServletRequest request, HttpServletResponse response,
            Updater[] updaters);
}
