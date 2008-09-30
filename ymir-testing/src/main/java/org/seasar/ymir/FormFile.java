package org.seasar.ymir;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * マルチパートリクエストに含まれるファイル情報を表すクラスです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要はありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface FormFile extends Serializable {
    /**
     * 受け取ったファイルの内容を返します。
     * 
     * @return 受け取ったファイル内容。
     */
    byte[] get();

    /**
     * 受け取ったファイルのコンテントタイプを返します。
     * 
     * @return 受け取ったファイルのコンテントタイプ。
     */
    String getContentType();

    /**
     * 受け取ったファイルの内容を取り出すためのInputStreamを返します。
     * 
     * @return 受け取ったファイルの内容を取り出すためのInputStream。
     * @throws IOException エラーが発生した場合。
     */
    InputStream getInputStream() throws IOException;

    /**
     * 受け取ったファイルの名前を返します。
     * 
     * @return 受け取ったファイルの名前。
     */
    String getName();

    /**
     * 受け取ったファイルの大きさを返します。
     * <p>大きさはバイト数です。
     * </p>
     * 
     * @return 受け取ったファイルの大きさ。
     */
    long getSize();

    /**
     * 受け取ったファイルの内容を文字列として返します。
     * <p>このメソッド呼び出しは、システムの文字エンコーディングを指定して{@link #getString(String)}
     * を呼び出すのと同じです。
     * </p>
     * 
     * @return 受け取ったファイルの内容。
     */
    String getString();

    /**
     * 受け取ったファイルの内容を文字列として返します。
     *
     * @param encoding ファイルの文字エンコーディング。
     * @return 受け取ったファイルの内容。
     * @throws UnsupportedEncodingException サポートしていない文字エンコーディングが指定された場合。
     */
    String getString(String encoding) throws UnsupportedEncodingException;
}
