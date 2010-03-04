package org.seasar.ymir.impl;

/**
 * 直接リクエストされることが許可されていないパスのパターンを表すPathMappingです。
 * <p>forwardやincludeなど、内部でディスパッチされた場合は処理が許可されます。
 * </p>
 * 
 * @author skirnir
 */
public class DeniedYmirPathMapping extends YmirPathMapping {
    public DeniedYmirPathMapping(String patternString) {
        this(patternString, null);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param patternString パスのパターンです。
     * @param pageComponentNameTemplate パスに対応するPageコンポーネント名を構築するためのテンプレートです。
     * nullを指定することもできます。
     * あるページからフォワードさせた先でもPageクラスのアクションを呼び出したい場合には
     * この引数にnullでないテンプレート文字列を指定して下さい。
     * @since 1.0.7
     */
    public DeniedYmirPathMapping(String patternString,
            String pageComponentNameTemplate) {
        super(true, patternString, pageComponentNameTemplate);
    }
}
