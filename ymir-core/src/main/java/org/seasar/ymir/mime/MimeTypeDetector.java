package org.seasar.ymir.mime;

/**
 * MIMEタイプを判別するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface MimeTypeDetector {
    /**
     * 指定された名前からMIMEタイプを判別して返します。
     * <p>通常は指定された名前の拡張子部分からMIMEタイプを判別します。
     * 
     * @param name 名前。
     * @return MIMEタイプ。MIMEタイプの判別ができなかった場合はnullを返します。
     */
    String getMimeType(String name);
}
