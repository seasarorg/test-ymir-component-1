package org.seasar.ymir;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * redirectまたはHTMLへの埋め込み用のURLを加工するためのインタフェースです。
 * <p>redirect先として指定されたURLやHTMLに埋め込まれるURLを一括して加工したい場合には、
 * このインタフェースの実装クラスを作成してymir-component.diconにコンポーネント定義を追加して下さい。
 * </p>
 * 
 * @author skirnir
 * @since 1.0.7
 */
public interface FollowingURLResolver {
    /**
     * 指定されたURLを最終的な形式に加工して返します。
     * <p>このメソッドに渡されるURLはredirectのためのURLまたはHTML内に埋め込まれるURLです。
     * forwardやproceedのためのURLは渡されません。
     * またURLは必要に応じてセッションIDが埋め込まれるなどフレームワークによって加工された最終的なURLで、
     * クエリストリングやパスパラメータが付与されていることもあります。
     * </p>
     * 
     * @param url URL。
     * nullが指定されることはありません。
     * @param httpRequest 現在のHttpServletResponseオブジェクト。
     * nullが指定されることはありません。
     * @param httpResponse 現在のHttpServletRequestオブジェクト。 
     * nullが指定されることはありません。
     * @param request 現在のRequestオブジェクト。
     * nullが指定されることはありません。
     * @return 最終的なURL。
     */
    String resolveURL(String url, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, Request request);

    /**
     * 指定されたURLが{@link #resolveURL(String, HttpServletRequest, HttpServletResponse, Request)}
     * によって加工済みかどうかを返します。
     * <p><code>resolveURL()</code>はredirectまたはHTMLへの埋め込み用のURLを出力する
     * フレームワークコンポーネントによって呼び出されますが、
     * 場合によっては複数回呼び出される可能性があります。
     * 既に加工済みのURLを再度加工してしまうことがないよう、フレームワークコンポーネントは
     * このメソッドを使って加工済みのURLについては重複して加工処理を呼び出さないようにします。
     * </p>
     * 
     * @param url URL。
     * nullが指定されることはありません。
     * @param httpRequest 現在のHttpServletResponseオブジェクト。
     * nullが指定されることはありません。
     * @param httpResponse 現在のHttpServletRequestオブジェクト。 
     * nullが指定されることはありません。
     * @param request 現在のRequestオブジェクト。
     * nullが指定されることはありません。
     * @return 加工済みかどうか。
     */
    boolean isResolved(String url, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, Request request);
}
