package org.seasar.ymir;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import org.seasar.cms.pluggable.hotdeploy.LocalHotdeployS2Container;
import org.seasar.framework.container.S2Container;

/**
 * Ymirフレームワーク上で動作するアプリケーション（Ymirアプリケーション）を表すインタフェースです。
 * <p>通常、Ymirフレームワークを構成するフレームワークコンポーネントの1セットに対してYmirアプリケーションが1つだけ存在しますが、
 * Kvasir/Soraなどのアプリケーションうプラットフォーム上ではYmirフレームワークコンポーネントの1セットにつき
 * 複数のYmirアプリケーションが動作することがあります。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface Application {
    /**
     * アプリケーションのIDを返します。
     * 
     * @return アプリケーションのID。
     */
    String getId();

    /**
     * アプリケーションに対応するWebアプリケーションのルートディレクトリのパス文字列を返します。
     * <p>例えばWebアプリケーションが<code>/usr/local/tomcat/webapps/app</code>
     * にデプロイされて稼動している場合は、
     * このメソッドの返り値は<code>/usr/local/tomcat/webapps/app</code>になります。
     * </p>
     * 
     * @return Webアプリケーションのルートディレクトリのパス文字列。
     */
    String getWebappRoot();

    /**
     * アプリケーション開発プロジェクトの、
     * Webリソースが格納されているディレクトリのパス文字列を返します。
     * 
     * @return Webリソースが格納されているディレクトリのパス文字列。
     */
    String getWebappSourceRoot();

    /**
     * アプリケーションを構成するClassをトラバースする際の基準となるClassオブジェクトを返します。
     * <p>このメソッドが返すClassが属するファイルシステムやJAR等に存在するClassが
     * S2にコンポーネントを自動登録する際のトラバース対象となります。
     * 
     * @return 基準となるClassオブジェクト。
     */
    Class<?> getReferenceClass();

    /**
     * アプリケーション開発プロジェクトのルートディレクトリのパス文字列を返します。
     * 
     * @return アプリケーション開発プロジェクトのルートディレクトリのパス文字列。
     */
    String getProjectRoot();

    /**
     * アプリケーション開発プロジェクトのルートディレクトリのパス文字列を設定します。
     * 
     * @param projectRoot アプリケーション開発プロジェクトのルートディレクトリのパス文字列。
     */
    void setProjectRoot(String projectRoot);

    /**
     * アプリケーションのルートパッケージ名を返します。
     * 
     * @return アプリケーションのルートパッケージ名。
     */
    String getRootPackageName();

    /**
     * アプリケーションのルートパッケージ名を設定します。
     * 
     * @param rootPackageName アプリケーションのルートパッケージ名。
     */
    void setRootPackageName(String rootPackageName);

    /**
     * アプリケーション開発プロジェクトのソース格納ディレクトリのパス文字列を返します。
     * 
     * @return ソース格納ディレクトリのパス文字列。
     */
    String getSourceDirectory();

    /**
     * アプリケーション開発プロジェクトのソース格納ディレクトリのパス文字列を設定します。
     * 
     * @param sourceDirectory ソース格納ディレクトリのパス文字列。
     */
    void setSourceDirectory(String sourceDirectory);

    /**
     * アプリケーション開発プロジェクトのリソース格納ディレクトリのパス文字列を返します。
     * 
     * @return リソース格納ディレクトリのパス文字列。
     */
    String getResourcesDirectory();

    /**
     * アプリケーション開発プロジェクトのリソース格納ディレクトリのパス文字列を設定します。
     * 
     * @param resourcesDirectory リソース格納ディレクトリのパス文字列。
     */
    void setResourcesDirectory(String resourcesDirectory);

    /**
     * 指定されたコンテキスト相対パスに対応するリソースが存在するかどうかを返します。
     * 
     * @param path コンテキスト相対パス。
     * @return 指定されたコンテキスト相対パスに対応するリソースが存在するかどうか。
     */
    boolean isResourceExists(String path);

    /**
     * このアプリケーションに割り当てられたS2Containerを返します。
     * 
     * @return このアプリケーションに割り当てられたS2Container。
     */
    S2Container getS2Container();

    /**
     * 指定されたClassがこのアプリケーションのルートパッケージ以下に属するかどうかを返します。
     * 
     * @param clazz Class。
     * @return このアプリケーションのルートパッケージ以下に属するかどうか。
     */
    boolean isCapable(Class<?> clazz);

    /**
     * 指定されたキーに対応するプロパティの値を返します。
     * <p>プロパティは通常クラスパスにある<code>app.properties</code>に記述されています。
     * </p>
     * <p>値が存在しない場合はnullを返します。
     * </p>
     * 
     * @param key キー。
     * @return 指定されたキーに対応するプロパティの値。
     */
    String getProperty(String key);

    /**
     * 指定されたキーに対応するプロパティの値を返します。
     * <p>値が存在しない場合は<code>defaultValue</code>の値を返します。
     * </p>
     * 
     * @param key キー。
     * @return 指定されたキーに対応するプロパティの値。
     * @see #getProperty(String)
     */
    String getProperty(String key, String defaultValue);

    /**
     * プロパティの全てのキーを表すEnumerationを返します。
     * 
     * @return プロパティの全てのキーを表すEnumeration。
     */
    Enumeration<String> propertyNames();

    /**
     * 指定されたキーに対応するプロパティの値を設定します。
     * <p>値はメモリ上に設定されるだけです。
     * 設定した値を永続化したい場合は{@link #save(OutputStream, String)}を呼び出して下さい。
     * </p>
     * 
     * @param key キー。
     * @param value プロパティの値。
     * @see #getProperty(String)
     * @see #save(OutputStream, String)
     */
    void setProperty(String key, String value);

    /**
     * 指定されたキーに対応するプロパティを削除します。
     * <p>プロパティはメモリ上で削除されるだけです。
     * 削除した状態を永続化したい場合は{@link #save(OutputStream, String)}を呼び出して下さい。
     * </p>
     * 
     * @param key キー。
     * @see #getProperty(String)
     * @see #save(OutputStream, String)
     */
    void removeProperty(String key);

    /**
     * 指定されたOutputStreamに対して現在のメモリ上のプロパティの値を永続化します。
     * <p>デフォルトのプロパティファイルに対して永続化するには、
     * {@link #getDefaultPropertiesFilePath()}が返すパスに対応するファイルの
     * OutputStreamを指定して下さい。
     * </p>
     * 
     * @param out OutputStream。
     * @param header 出力するヘッダ情報。
     * @throws IOException I/Oエラーが発生した場合。
     */
    void save(OutputStream out, String header) throws IOException;

    /**
     * デフォルトのプロパティファイルのパスを返します。
     * <p>通常は<code>app.properties</code>を指すパスを返します。
     * </p>
     * 
     * @return デフォルトのプロパティファイルのパス。
     */
    String getDefaultPropertiesFilePath();

    /**
     * このアプリケーションに対応するLocalHotdeployS2Containerオブジェクトを返します。
     * 
     * @return このアプリケーションに対応するLocalHotdeployS2Containerオブジェクト。
     */
    LocalHotdeployS2Container getHotdeployS2Container();

    /**
     * このアプリケーションに対応するPathMappingProviderオブジェクトを返します。
     * 
     * @return このアプリケーションに対応するPathMappingProviderオブジェクト。
     */
    PathMappingProvider getPathMappingProvider();

    /**
     * このアプリケーションが開発中のステータスであるかどうかを返します。
     * <p>このメソッドは、このアプリケーションが開発中のステータスかどうかを返すだけであって、
     * Ymir自体が開発中のステータスを持つかどうかとは無関係であることに注意して下さい。
     * 仮にこのメソッドがtrueを返しても、Ymir自体が開発中でない場合はアプリケーションを開発中と
     * みなすべきではありません。
     * Ymirのステータスを含めてこのアプリケーションを開発中とみなしてよいかどうかを知るためには、
     * {@link Ymir#isUnderDevelopment()}を使用して下さい。
     * </p>
     *
     * @return このアプリケーションが開発中のステータスであるかどうか。
     */
    boolean isUnderDevelopment();

    /**
     * 指定されたClassにバインドされた関連情報を表すオブジェクトを返します。
     * <p>アプリケーションでは、Classに対して関連情報としてオブジェクトをバインドさせることができます。
     * このメソッドは、指定されたClassにバインドされたオブジェクトを返します。
     * </p>
     * <p>オブジェクトがバインドされていない場合はnullを返します。
     * </p>
     * 
     * @param <T> 関連情報を表すオブジェクトの型。
     * @param clazz バインド元のClass。
     * @return 関連情報。
     * @see #clear()
     */
    <T> T getRelatedObject(Class<T> clazz);

    /**
     * 指定されたClassに関連情報をバインドします。
     * <p>関連情報をアンバインドしたい場合はnullをバインドするようにして下さい。
     * </p>
     * 
     * @param <T> 関連情報を表すオブジェクトの型。
     * @param clazz バインド元のClass。
     * @param object 関連情報。nullを指定することもできます。
     * @see #getRelatedObject(Class)
     * @see #clear()
     */
    <T> void setRelatedObject(Class<T> clazz, T object);

    /**
     * アプリケーションに登録された関連情報をクリアします。
     * 
     * @see #getRelatedObject(Class)
     * @see #setRelatedObject(Class, Object)
     */
    void clear();

    /**
     * 画面テンプレートの文字エンコーディングを返します。
     * 
     * @return 画面テンプレートの文字エンコーディング。
     */
    String getTemplateEncoding();
}
