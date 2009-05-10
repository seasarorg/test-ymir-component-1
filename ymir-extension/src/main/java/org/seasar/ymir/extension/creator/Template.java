package org.seasar.ymir.extension.creator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 自動生成対象である画面テンプレートを表すインタフェースです。
 *  
 * @author YOKOTA Takehiko
 */
public interface Template {
    /**
     * テンプレートが既に存在するかどうかを返します。
     * 
     * @return テンプレートが既に存在するかどうか。
     */
    boolean exists();

    /**
     * テンプレートの内容を取り出すためのInputStreamとして返します。
     * 
     * @return テンプレートの内容を取り出すためのInputStream。
     * @throws IOException エラーが発生した場合、またはテンプレートが存在しない場合。
     */
    InputStream getInputStream() throws IOException;

    /**
     * テンプレートに対応するコンテキスト相対パスを返します。
     * <p>パスは末尾に「/」がつかない形に正規化されています。
     * </p>
     * 
     * @return テンプレートに対応するコンテキスト相対パス。
     */
    String getPath();

    /**
     * テンプレートの最終更新日時を表すlong値を返します。
     * <p>テンプレートが存在しない場合の返り値は不定です。
     * </p>
     * 
     * @return テンプレートの最終更新日時を表すlong値。
     */
    long lastModified();

    /**
     * テンプレートの名前を返します。
     * 
     * @return テンプレートの名前。
     */
    String getName();

    /**
     * テンプレートの内容を書き換えるためのOutputStreamを返します。
     * 
     * @return テンプレートの内容を書き換えるためのOutputStream。
     * @throws IOException エラーが発生した場合。
     */
    OutputStream getOutputStream() throws IOException;

    /**
     * テンプレートの文字エンコーディングを返します。
     * 
     * @return テンプレートの文字エンコーディング。
     */
    String getEncoding();

    /**
     * このテンプレートがディレクトリパスに紐づいているかどうかを返します。
     * <p>テンプレートがディレクトリパスに紐づいている場合は{@link #getInputStream()}や
     * {@link #getOutputStream()}を呼び出してはいけません。
     * </p>
     * 
     * @return このテンプレートがディレクトリパスに紐づいているかどうか。
     */
    boolean isDirectory();

    /**
     * このテンプレートのパスにディレクトリを作成します。
     * 
     * @return ディレクトリが作成されたかどうか。
     */
    boolean mkdirs();
}
